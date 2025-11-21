package com.khetigaadi.harvester
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder
import com.google.android.gms.analytics.Tracker
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.firebase.dynamiclinks.ktx.component2
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.khetigaadi.R
import com.khetigaadi.base.util.StateData
//import com.khetigaadi.commons.activities.BaseActivity
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.databinding.ActivityHarvesterDetailsBinding
import com.khetigaadi.def.PriceType
import com.khetigaadi.harvester.model.Harvester
import com.khetigaadi.harvester.model.HarvesterResp
import com.khetigaadi.harvester.model.Others
import com.khetigaadi.harvester.model.SimilarHarvesterListResp
import com.khetigaadi.retrofit.KhetiAPIs
import com.khetigaadi.tractor.newTractor.NewTrGetOnRoadPriceActivity
import com.khetigaadi.tractor.newTractor.NewTractorRequestForDemo
import com.khetigaadi.util.NumberFormatting
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class HarvesterDetailsActivity : AppCompatActivity() {
    private val TAG = HarvesterDetailsActivity::class.java.simpleName
    private var harvesterId: String? = null
    @Inject
    lateinit var mTracker:Tracker
    @Inject
    lateinit var khetiAPIs:KhetiAPIs
    private lateinit var binding:ActivityHarvesterDetailsBinding
    private lateinit var viewModel:HarvesterDetailsViewModel
    private lateinit var imp:Harvester
    private lateinit var similarListAdapter:SimilarHarvesterListAdapter
    private lateinit var others: Others
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false)
        binding = ActivityHarvesterDetailsBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
//        ViewCompat.setOnApplyWindowInsetsListener(
//            binding.getRoot(),
//            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
//                val systemBarsInsets = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
//                v!!.setPadding(
//                    systemBarsInsets.left,
//                    systemBarsInsets.top,
//                    systemBarsInsets.right,
//                    systemBarsInsets.bottom)
//                insets
//            })
//        this.enableEdgeToEdge()
        viewModel = ViewModelProvider(this)[HarvesterDetailsViewModel::class.java]
        readData(intent.extras)
        readDeepLinkData()
        setSupportActionBar(binding.toolbars.toolbar)
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding.toolbars.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.toolbars.toolbar.title = resources.getString(R.string.app_name)
        supportActionBar?.title =  resources.getString(R.string.harvester)
        binding.btnOnPrice.setOnClickListener {
            val intent = Intent(this@HarvesterDetailsActivity, NewTrGetOnRoadPriceActivity::class.java)
            intent.putExtra("id", imp.harvesterId)
            intent.putExtra("name", imp.name)
            intent.putExtra("image", if (imp.harvesterImage != null) others.harvesterImagePath + imp.harvesterImage.name else null)
            intent.putExtra("priceType", PriceType.HARVESTER)
            startActivity(intent)
        }
        binding.btnRequestForDemo.setOnClickListener {
            val intent = Intent(this@HarvesterDetailsActivity, NewTractorRequestForDemo::class.java)
            intent.putExtra("id", imp.harvesterId)
            intent.putExtra("name", imp.name)
            intent.putExtra("image", if (imp.harvesterImage != null) others.harvesterImagePath + imp.harvesterImage.name else null)
            intent.putExtra("demoType", PriceType.HARVESTER)
            startActivity(intent)
        }
        if(Constants.isConnectionAvailable(this@HarvesterDetailsActivity)) {
            if (harvesterId?.isNotEmpty() == true) details
        }
        else {
            noInternetConnection(binding.parent)
        }
        initSimilar()
    }
    private fun noInternetConnection(parent:CoordinatorLayout) {

    }
    private fun initSimilar() {
        binding.includeSimilar.tvLabel.text = getString(R.string.similar_implements)
        binding.includeSimilar.tvSeeMore.visibility = View.GONE
        similarListAdapter = SimilarHarvesterListAdapter(this, binding.includeSimilar.rvList)
        binding.includeSimilar.rvList.adapter = similarListAdapter
        similarListAdapter.setOnItemClickListener(object : SimilarHarvesterListAdapter.Listener {
            override fun onItemClick(position: Int, v: View?) {
                finish()
                val item = similarListAdapter.getItem(position)
                if (item != null) {
                    val intent = Intent(this@HarvesterDetailsActivity, HarvesterDetailsActivity::class.java)
                    intent.putExtra("harvesterId", item.harvesterId)
                    startActivity(intent)
                }
            }

            override fun onGetPriceClick(position: Int, v: View?) {
                val intent = Intent(this@HarvesterDetailsActivity, NewTrGetOnRoadPriceActivity::class.java)
                intent.putExtra("id",similarListAdapter.getItem(position).harvesterId)
                intent.putExtra("name",similarListAdapter.getItem(position).name)
                intent.putExtra("image",similarListAdapter.others.harvesterImagePath+similarListAdapter.getItem(position).harvesterImage.name)
                intent.putExtra("priceType",PriceType.HARVESTER)
                startActivity(intent)
            }
        })
        binding.includeSimilar.container.visibility = View.GONE
    }
    private fun readDeepLinkData() {
        if (intent.action != null && intent.action == Intent.ACTION_VIEW) {
            try {
                val uri = intent.data
                val segments = uri!!.path!!.split("/".toRegex()).toTypedArray()
                harvesterId = segments[segments.size - 4]
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun readData(bundle:Bundle?) {
        if (bundle != null) {
            harvesterId = bundle.getString("harvesterId", null)
        }
    }
    private val details:Unit
    private get() {
            try {
                viewModel.getNewImplementDetails(harvesterId).observe(this) { implementDetailsRespStateData: StateData<HarvesterResp?> ->
                    when (implementDetailsRespStateData.status) {
                        StateData.Status.LOADING -> binding.llProgresss.llProgress.visibility = View.VISIBLE
                        StateData.Status.SUCCESS -> {
                            binding.llProgresss.llProgress.visibility = View.GONE
                            if (implementDetailsRespStateData.data != null) {
                                mapToUI(implementDetailsRespStateData.data)
                                getSimilarImps(implementDetailsRespStateData.data!!.data)
                            }
                        }
                        StateData.Status.ERROR -> {
                            binding.llProgresss.llProgress.visibility = View.GONE
                            Toast.makeText(this, implementDetailsRespStateData.error, Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
            catch(e: Exception) {
                e.printStackTrace()
            }
        }
    private fun getSimilarImps(newImp:Harvester) {
        viewModel.getSimilarNewImplements(newImp).observe(this) { implementDetailsRespStateData: StateData<SimilarHarvesterListResp?> ->
            when (implementDetailsRespStateData.status) {
                StateData.Status.LOADING -> binding.includeSimilar.container.visibility = View.GONE
                StateData.Status.SUCCESS -> {
                    binding.includeSimilar.container.visibility = View.VISIBLE
                    if (implementDetailsRespStateData.data != null) {
                        similarListAdapter.others = implementDetailsRespStateData.data!!.others
                        similarListAdapter.updateItems(implementDetailsRespStateData.data!!.data)
                    }
                }
                StateData.Status.ERROR -> {
                    binding.includeSimilar.container.visibility = View.GONE
                    Toast.makeText(this, implementDetailsRespStateData.error, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
    private fun mapToUI(data:HarvesterResp?) {
        imp = data!!.data
        others = data.others
        binding.tvName.text = imp.name
        binding.toolbars.toolbar.title = imp.name
        if (imp.harvesterType != null) {
            if (imp.harvesterType.name.isNotEmpty()) {
                binding.tvType.text = imp.harvesterType.name
            }
        }
        binding.ivShare.setOnClickListener {
            Firebase.dynamicLinks.shortLinkAsync {
                link = Uri.parse("https://www.khetigaadi.com/harvester/$harvesterId")
                domainUriPrefix = getString(R.string.domain_uri_prefix)
                androidParameters { }
            }.addOnSuccessListener { (shortLink, _) ->
                try {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, imp.name)
                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.i_was_viewing) + "  \"" + imp.name + "\" " + resources.getString(R.string.harvester) + " " + shortLink)
                    startActivity(Intent.createChooser(intent, "Select an action"))
                } catch (ex: ActivityNotFoundException) {
                    // (handle error)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.addOnFailureListener {
                Timber.e(it)
            }
        }
        if (imp.views != 0) {
            binding.tvViews.text = NumberFormatting.format(imp.views.toLong())
        }
        //Google analytic code
        Timber.i("Setting screen name: %s", TAG)
        mTracker.setScreenName(TAG + " " + imp.name)
        mTracker.send(ScreenViewBuilder().build())
        Glide.with(this)
            .load(if (imp.harvesterImage != null && data.others != null) data.others.harvesterImagePath + imp.harvesterImage.name else null)
            .placeholder(R.mipmap.no_image)
            .error(R.mipmap.no_image)
            .into(binding.ivImage)
    }
}