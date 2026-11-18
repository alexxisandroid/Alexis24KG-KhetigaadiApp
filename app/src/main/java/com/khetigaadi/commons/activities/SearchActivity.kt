@file:Suppress("PROPERTY_HIDES_JAVA_FIELD")

package com.khetigaadi.commons.activities
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.text.Html
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.activity.enableEdgeToEdge
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.khetigaadi.R
import com.khetigaadi.commons.SharedPreferenceUtils
import com.khetigaadi.commons.adapter.SearchProductAdapter
import com.khetigaadi.commons.model.SearchResp
import com.khetigaadi.databinding.ActivitySearchBinding
import com.khetigaadi.ecom.ProductDetailsActivity
import com.khetigaadi.harvester.HarvesterDetailsActivity
import com.khetigaadi.harvester.SearchHarvesterAdapter
import com.khetigaadi.harvester.model.Harvester
import com.khetigaadi.implement.SearchImplementAdapter
import com.khetigaadi.implement.newImplement.NewImpOverviewDetails
import com.khetigaadi.implement.newImplement.util.NewImp
import com.khetigaadi.retrofit.KhetiAPIs
import com.khetigaadi.tractor.newTractor.NewTractorDetails
import com.khetigaadi.tractor.newTractor.model.NewTractor
import com.khetigaadi.tractor.newTractor.search.SearchNewTractorAdapter
import com.khetigaadi.tractor.usedTractor.SearchUsedTractorAdapter
import com.khetigaadi.tractor.usedTractor.UsedTractorDetailActivity
import com.khetigaadi.tractor.usedTractor.util.UsedTractor
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.khetigaadi.shop.model.Product
@Suppress("DEPRECATION")
@AndroidEntryPoint
class SearchActivity:BaseActivity() {
    private lateinit var searchUsedTractorAdapter: SearchUsedTractorAdapter
    private lateinit var searchHarvesterAdapter:   SearchHarvesterAdapter
    private lateinit var searchImplementAdapter:   SearchImplementAdapter
    private lateinit var searchNewTractorAdapter:  SearchNewTractorAdapter
    private lateinit var searchProductAdapter:     SearchProductAdapter
    @Inject
    lateinit var khetiAPIs:KhetiAPIs
    @Inject
    lateinit var disposable:CompositeDisposable
    @Inject
    lateinit var pref:SharedPreferenceUtils
    private lateinit var binding:ActivitySearchBinding
    private lateinit var searchView:SearchView
    private var filterActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result:ActivityResult ->
        val data = result.data
        if (data != null) {
            val result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            searchView.requestFocusFromTouch()
            searchView.setQuery(result1!![0],false)
            Handler().postDelayed({hideSoftKeyboard(this@SearchActivity)},100)
        }
    }
    private val _textInput = BehaviorSubject.create<String>()
    private val textInput = _textInput.toFlowable(BackpressureStrategy.LATEST)
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view:View = binding.root
        setContentView(view)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(view) { v:View?,insets:WindowInsetsCompat? ->
            val systemBarsInsets:Insets = insets!!.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            v!!.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom)
            insets
        }
        Glide.with(this)
            .load(R.drawable.loader_white)
            .into(binding.llProgresss.ivLoader)
        initToolbar()
        if(intent != null && intent.hasExtra(IS_RECORDING) && intent.getBooleanExtra(IS_RECORDING,false)) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to text")
            try {
                filterActivityResultLauncher.launch(intent)
            }
            catch(e:Exception) {
                Toast.makeText(this@SearchActivity," "+e.message,Toast.LENGTH_SHORT).show()
            }
        }

        binding.sView.isFocusable = true
        binding.sView.isIconified = false
        // binding.sView.requestFocusFromTouch() // Removed to prevent keyboard from showing initially
        binding.sView.queryHint = Html.fromHtml("<font color = #999999>" + resources.getString(R.string.search_hint) + "</font>")
        
        // Show initial empty view
        binding.llEmptyView.visibility = View.VISIBLE
        binding.tvEmptyMsg.text = getString(R.string.start_typing_to_search)
        
        // Hide keyboard initially
        Handler(Looper.getMainLooper()).postDelayed({
            hideSoftKeyboard(this@SearchActivity)
        }, 300)

//        binding.sView.addTextChangedListener(object:TextWatcher {
//            override fun beforeTextChanged(s:CharSequence,start:Int,count:Int,after:Int) {}
//            override fun onTextChanged(s:CharSequence,start:Int,before:Int,count:Int) {
//                if(s.isNotEmpty()) {
//                    _textInput.onNext(""+s)
//                }
//            }
//            override fun afterTextChanged(s:Editable) {}
//        })
        binding.sView.setOnQueryTextListener(object:SearchView.OnQueryTextListener {
          override fun onQueryTextSubmit(query:String):Boolean {
              return true
          }
          override fun onQueryTextChange(newText:String):Boolean {
              if(newText.isNotEmpty()) {
                  _textInput.onNext(newText)
              }
              return false
          }
      })
    }
    private fun initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar)
       // binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding.toolbars.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.toolbars.toolbar.title = "" // getString(R.string.search)
        binding.toolbars.toolbar.setBackgroundColor(Color.WHITE)
        supportActionBar?.title = ""
      //  @RequiresApi(Build.VERSION_CODES.O)
       // binding.toolbars.toolbar.tooltipText = ""
    }
    override fun onCreateOptionsMenu(menu:Menu):Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        return true
    }
    override fun onPrepareOptionsMenu(menu:Menu):Boolean {
        super.onPrepareOptionsMenu(menu)
        val searchViewMenuItem = menu.findItem(R.id.search)
        searchView = searchViewMenuItem.actionView as SearchView
        val v = searchView.findViewById<View>(R.id.search_button) as ImageView
        v.setImageResource(R.drawable.ic_search2) //Changing the image
        val searchClose = searchView.findViewById<ImageView>(R.id.search_close_btn)
        searchClose.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()

        searchView.queryHint = Html.fromHtml("<font color = #999999>" + resources.getString(R.string.search_hint) + "</font>")
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query:String):Boolean {
                return true
            }
            override fun onQueryTextChange(newText:String):Boolean {
                if(newText.isNotEmpty()) {
                    _textInput.onNext(newText)
                }
                return false
            }
        })
        disposable.add(
            textInput
                .debounce(700, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(it.length > 2) {
                        search(it)
                    }
                })
        return true
    }
    private fun search(query:String) {
        binding.newTractorSearchRes.visibility = View.GONE
        binding.llProgresss.llProgress.visibility = View.VISIBLE
        val params = HashMap<String,String>()
        params["search"] = query
        disposable.add(
            khetiAPIs.search(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object:DisposableSingleObserver<SearchResp>() {
                    override fun onSuccess(searchResp:SearchResp) {
                        Timber.e("onSuccessResponse1234: %s",searchResp.msg[0])
                         println("cxzcxzcxzcxzc===="+searchResp.msg.size+"=="+searchResp.data.products.size+"=="+searchResp.data.harvester.size+"=="+searchResp.data.tractor.size+"=="+searchResp.data.usedTractor.size+"=="+searchResp.data.implement.size+"==")

                        println("bcvbvbvbbvcvbcv===="+params+"="+searchResp.data.implement)

                        println("bcvbvbvbbvcvbcv1234===="+params+"="+searchResp.data)
                        binding.chipGroup.clearCheck()
                        dismissProgressDialog()
                        binding.newTractorSearchRes.visibility = View.VISIBLE
                        binding.llProgresss.llProgress.visibility = View.GONE
                        if(!searchResp.isError) {
                            val isEmpty = searchResp.data.tractor.isEmpty() &&
                                    searchResp.data.implement.isEmpty() &&
                                    searchResp.data.harvester.isEmpty() &&
                                    searchResp.data.products.isEmpty() &&
                                    searchResp.data.usedTractor.isEmpty()

                            if (isEmpty) {
                                binding.llEmptyView.visibility = View.VISIBLE
                                binding.tvEmptyMsg.text = getString(R.string.no_data_found)
                                binding.newTractorSearchRes.visibility = View.GONE
                            } else {
                                binding.llEmptyView.visibility = View.GONE
                                binding.newTractorSearchRes.visibility = View.VISIBLE
                            }

                            searchNewTractorAdapter = SearchNewTractorAdapter(this@SearchActivity,searchResp.data.tractor,searchResp.others)
                            searchImplementAdapter =  SearchImplementAdapter(this@SearchActivity,searchResp.data.implement,searchResp.others)
                            searchHarvesterAdapter =  SearchHarvesterAdapter(this@SearchActivity,searchResp.data.harvester,searchResp.others)
                            searchProductAdapter =    SearchProductAdapter(this@SearchActivity,searchResp.data.products,searchResp.others)
                            searchUsedTractorAdapter = SearchUsedTractorAdapter(this@SearchActivity,searchResp.data.usedTractor,searchResp.others)
//                            binding.newTractorSearchRes.adapter =
//                                ConcatAdapter(searchNewTractorAdapter, searchImplementAdapter, searchHarvesterAdapter, searchProductAdapter, searchUsedTractorAdapter)
                            searchNewTractorAdapter.setOnItemClickListener(object:
                                SearchNewTractorAdapter.Listener {
                                override fun onItemClick(tractor:NewTractor) {
                                    val intent = Intent(this@SearchActivity,NewTractorDetails::class.java)
                                    intent.putExtra(NewTractorDetails.newTractorId,tractor.id)
                                    startActivity(intent)
                                }
                            })
                            searchImplementAdapter.setOnItemClickListener(object:
                                SearchImplementAdapter.Listener {
                                override fun onItemClick(implement:NewImp) {
                                    println("cvbvcbcbcv===$implement")
                                    val intent = Intent(this@SearchActivity,NewImpOverviewDetails::class.java)
                                    intent.putExtra("brandId",implement.brand?.id)
                                    intent.putExtra("typeId",implement.type?.id)
                                    intent.putExtra("modelId",implement.model?.implementModelId)
                                    startActivity(intent)
                                }
                            })
                            searchHarvesterAdapter.setOnItemClickListener(object :
                                SearchHarvesterAdapter.Listener {
                                override fun onItemClick(harvester: Harvester) {
                                    val intent = Intent(this@SearchActivity,HarvesterDetailsActivity::class.java)
                                    intent.putExtra("harvesterId",harvester.harvesterId)
                                    startActivity(intent)
                                }
                            })
                            searchProductAdapter.setOnItemClickListener(object :
                                SearchProductAdapter.Listener {
                                override fun onItemClick(product:Product) {
                                    val intent = Intent(this@SearchActivity,ProductDetailsActivity::class.java)
                                    intent.putExtra("productId",product.productId)
                                    startActivity(intent)
                                }
                            })
                            searchUsedTractorAdapter.setOnItemClickListener(object :
                                SearchUsedTractorAdapter.Listener {
                                override fun onItemClick(usedTractor:UsedTractor) {
                                    val intent = Intent(this@SearchActivity,UsedTractorDetailActivity::class.java)
                                    intent.putExtra("tractorId",usedTractor.id)
                                    startActivity(intent)
                                }
                            })
                            if(searchResp.data.tractor.isEmpty()) {
                                binding.chipNewTractor.visibility = View.GONE
                            }
                            else {
                                binding.chipNewTractor.visibility = View.VISIBLE
                            }
                            if(searchResp.data.implement.isEmpty()) {
                                binding.chipImplements.visibility = View.GONE
                            }
                            else {
                                binding.chipImplements.visibility = View.VISIBLE
                            }
                            if(searchResp.data.harvester.isEmpty()) {
                                binding.chipHarvester.visibility = View.GONE
                            }
                            else {
                                binding.chipHarvester.visibility = View.VISIBLE
                            }
                            if(searchResp.data.products.isEmpty()) {
                                binding.chipProduct.visibility = View.GONE
                            }
                            else {
                                binding.chipProduct.visibility = View.VISIBLE
                            }
                            if(searchResp.data.usedTractor.isEmpty()) {
                                binding.chipUsedTractor.visibility = View.GONE
                            }
                            else {
                                binding.chipUsedTractor.visibility = View.VISIBLE
                            }
                            binding.chipNewTractor.setOnCheckedChangeListener { _, isChecked ->
                                if(isChecked)
                                    binding.newTractorSearchRes.adapter = searchNewTractorAdapter
                            }
                            binding.chipImplements.setOnCheckedChangeListener { _, isChecked ->
                                if(isChecked)
                                    binding.newTractorSearchRes.adapter = searchImplementAdapter
                            }
                            binding.chipHarvester.setOnCheckedChangeListener { _, isChecked ->
                                if (isChecked)
                                    binding.newTractorSearchRes.adapter = searchHarvesterAdapter
                            }
                            binding.chipProduct.setOnCheckedChangeListener { _, isChecked ->
                                if(isChecked)
                                    binding.newTractorSearchRes.adapter = searchProductAdapter
                            }
                            binding.chipUsedTractor.setOnCheckedChangeListener { _, isChecked ->
                                if (isChecked)
                                    binding.newTractorSearchRes.adapter = searchUsedTractorAdapter
                            }
                            if(binding.chipNewTractor.isVisible) {
                                binding.chipNewTractor.isChecked=true
                            }
                            else if(binding.chipImplements.isVisible)
                                binding.chipImplements.isChecked=true
                            else if(binding.chipHarvester.isVisible)
                                binding.chipHarvester.isChecked=true
                            else if(binding.chipProduct.isVisible)
                                binding.chipProduct.isChecked=true
                            else if(binding.chipUsedTractor.isVisible)
                                binding.chipUsedTractor.isChecked=true
                        }
                        else {
                            Toast.makeText(this@SearchActivity,getString(R.string.something_went_wrong),Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onError(e:Throwable) {
                        Timber.e("onErrorResponse: %s", e)
                        binding.llProgresss.llProgress.visibility = View.GONE
                        binding.llEmptyView.visibility = View.VISIBLE
                        binding.tvEmptyMsg.text = getString(R.string.something_went_wrong)
                        binding.newTractorSearchRes.visibility = View.GONE
                        dismissProgressDialog()
                    }
                }))
    }
    companion object {
        const val IS_RECORDING = "isRecording"
    }
}