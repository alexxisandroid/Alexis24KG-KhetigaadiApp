@file:Suppress("DEPRECATION")

package com.khetigaadi.implement.newImplement
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
//import androidx.core.view.WindowCompat
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
import com.google.gson.Gson
import com.khetigaadi.R
import com.khetigaadi.base.util.StateData
import com.khetigaadi.commons.activities.BaseActivity
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.databinding.NewImplementOverviewDeatailsBinding
import com.khetigaadi.databinding.UsedTractorDetailsListItemBinding
import com.khetigaadi.def.PriceType
import com.khetigaadi.implement.ImpsAdapter
import com.khetigaadi.implement.model.ImplementDetailsResp
import com.khetigaadi.implement.model.SimilarImplementListResp
import com.khetigaadi.implement.newImplement.model.NewImpParams
import com.khetigaadi.implement.newImplement.model.Others
import com.khetigaadi.implement.newImplement.util.NewImp
import com.khetigaadi.tractor.newTractor.NewTrGetOnRoadPriceActivity
import com.khetigaadi.tractor.newTractor.NewTractorRequestForDemo
import com.khetigaadi.util.NumberFormatting
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class NewImpOverviewDetails : BaseActivity() {
    private val TAG = NewImpOverviewDetails::class.java.simpleName
    private var brandId = 0
    private var typeId = 0
    private var modelId = 0
    @SuppressLint("VisibleForTests")
    @JvmField
    @Inject
    var mTracker:Tracker? = null
    private lateinit var binding:NewImplementOverviewDeatailsBinding
    private lateinit var viewModel:NewImplementDetailsViewModel
    private  var imp: NewImp?=null
    private lateinit var similarListAdapter:ImpsAdapter
    private lateinit var others: Others
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewImplementOverviewDeatailsBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
       try {
           //WindowCompat.setDecorFitsSystemWindows(window,false)
           enableEdgeToEdge()
           ViewCompat.setOnApplyWindowInsetsListener(
               binding.getRoot()
           ) { v: View?, insets: WindowInsetsCompat? ->
               val systemBarsInsets = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
               v!!.setPadding(
                   systemBarsInsets.left,
                   systemBarsInsets.top,
                   systemBarsInsets.right,
                   systemBarsInsets.bottom
               )
               insets
           }
       }
       catch(e:Exception) {
           e.printStackTrace()
       }
        this.enableEdgeToEdge()
        viewModel = ViewModelProvider(this)[NewImplementDetailsViewModel::class.java]
        readData(intent.extras)
        readDeepLinkData()
        setSupportActionBar(binding.toolbars.toolbar)
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding.toolbars.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.toolbars.toolbar.title = resources.getString(R.string.app_name)
        binding.btnOnPrice.setOnClickListener {
            val intent = Intent(this@NewImpOverviewDetails, NewTrGetOnRoadPriceActivity::class.java)
            intent.putExtra("id", imp?.id)
            intent.putExtra("name", imp?.implementName)
            intent.putExtra("image", if (imp?.implementImage != null) others.implementImagePath + imp?.implementImage?.image else null)
            intent.putExtra("priceType", PriceType.IMPLEMENT)
            startActivity(intent)
        }
        binding.btnRequestForDemo.setOnClickListener {
            val intent = Intent(this@NewImpOverviewDetails, NewTractorRequestForDemo::class.java)
            intent.putExtra("id", imp?.id)
            intent.putExtra("name", imp?.implementName)
            intent.putExtra("image", if (imp?.implementImage != null) others.implementImagePath + imp?.implementImage?.image else null)
            intent.putExtra("demoType", PriceType.IMPLEMENT)
            startActivity(intent)
        }
        if(Constants.isConnectionAvailable(this@NewImpOverviewDetails)) {
            if(brandId != 0 && typeId != 0 && modelId != 0)
                getDetails()
        }
        else {
            noInternetConnection(binding.parent)
        }
        initSimilar()
    }
    private fun initSimilar() {
        binding.includeSimilar.tvLabel.text = getString(R.string.similar_implements)
        binding.includeSimilar.tvSeeMore.visibility = View.GONE
        similarListAdapter = ImpsAdapter(this,binding.includeSimilar.rvList,false)
        binding.includeSimilar.rvList.adapter = similarListAdapter
        similarListAdapter.setOnItemClickListener(object:ImpsAdapter.Listener {
            override fun onItemClick(position:Int,v:View?) {
                finish()
                val item = similarListAdapter.getItem(position)
                if(item != null) {
                    val intent = Intent(this@NewImpOverviewDetails,NewImpOverviewDetails::class.java)
                    intent.putExtra("brandId",if (item.brand != null) item.brand.id else null)
                    intent.putExtra("typeId",if (item.implementTypeId != null) item.implementTypeId else null)
                    intent.putExtra("modelId",if (item.model != null) item.model.implementModelId.toInt() else null)
                    startActivity(intent)
                }
            }
            override fun onGetPriceClick(position:Int,v:View?) {
                val intent = Intent(this@NewImpOverviewDetails,NewTrGetOnRoadPriceActivity::class.java)
                intent.putExtra("id",similarListAdapter.getItem(position)!!.id /*implementListResp.getImplementArrayList().get(position).getId()*/)
                intent.putExtra("name",similarListAdapter.getItem(position)!!.implementName /*implementListResp.getImplementArrayList().get(position).getImplementName()*/)
                intent.putExtra("image",if(similarListAdapter.getItem(position)!!.implementImage != null) others.implementImagePath + similarListAdapter.getItem(position)!!.implementImage.image else null)
                intent.putExtra("priceType",PriceType.IMPLEMENT)
                intent.putExtra("toolbarlabel","Get Price")
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
                brandId = segments[segments.size - 4].toInt()
                typeId = segments[segments.size - 3].toInt()
                modelId = segments[segments.size - 2].toInt()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Timber.e("$brandId $typeId $modelId")
        }
    }
    private fun readData(bundle:Bundle?) {
        if(bundle != null) {
            brandId = bundle.getInt("brandId")
            typeId =  bundle.getInt("typeId")
            modelId = bundle.getInt("modelId")
            Timber.e("$brandId $typeId $modelId")
            println("cxvvcxvxvxv===$brandId==$typeId==$modelId")
        }
    }
    // Tag used to cancel the request
    private fun getDetails() {
            // Tag used to cancel the request
            val jsonObjectBrand = JSONObject()
            try {
                jsonObjectBrand.put("brand_id",brandId)
                val jsonObjectImplType = JSONObject()
                jsonObjectImplType.put("implement_type_id",typeId)
                val jsonObjectModel = JSONObject()
                jsonObjectModel.put("implement_model_id",modelId)
                val jsonObjectOverview = JSONObject()
                jsonObjectOverview.put("brands",jsonObjectBrand)
                jsonObjectOverview.put("type",jsonObjectImplType)
                jsonObjectOverview.put("model",jsonObjectModel)
                val params = JSONObject()
                params.put("implements",jsonObjectOverview)
                viewModel.getNewImplementDetails(Gson().fromJson(params.toString(), NewImpParams::class.java)).observe(this) { implementDetailsRespStateData: StateData<ImplementDetailsResp?> ->
                    when (implementDetailsRespStateData.status) {
                        StateData.Status.LOADING -> binding.llProgress.llProgress.visibility = View.VISIBLE
                        StateData.Status.SUCCESS -> {
                            binding.llProgress.llProgress.visibility = View.GONE
                            if (implementDetailsRespStateData.data != null && implementDetailsRespStateData.data!!.data[0] != null) {
                                mapToUI(implementDetailsRespStateData.data)
                                getSimilarImps(implementDetailsRespStateData.data!!.data[0])
                            }
                        }
                        StateData.Status.ERROR -> {
                            binding.llProgress.llProgress.visibility = View.GONE
                            Toast.makeText(this, implementDetailsRespStateData.error, Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            } catch(e:Exception) {
                e.printStackTrace()
            }
        }
    private fun getSimilarImps(newImp:NewImp) {
        viewModel.getSimilarNewImplements(newImp).observe(this) { implementDetailsRespStateData: StateData<SimilarImplementListResp?> ->
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
    @SuppressLint("UseKtx", "VisibleForTests")
    private fun mapToUI(data:ImplementDetailsResp?) {
        imp = data!!.data[0]
        others = data.others
        binding.tvName.text = imp?.implementName
        binding.toolbars.toolbar.title = imp?.implementName
        binding.ivShare.setOnClickListener {
            Firebase.dynamicLinks.shortLinkAsync {
                link = Uri.parse("https://www.khetigaadi.com/imp/$brandId/$typeId/$modelId")
                domainUriPrefix = getString(R.string.domain_uri_prefix)
                androidParameters { }
            }.addOnSuccessListener { (shortLink, _) ->
                try {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, imp?.implementName)
                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.i_was_viewing) + "  \"" + imp?.implementName + "\" " + resources.getString(R.string.implement) + " " + shortLink)
                    startActivity(Intent.createChooser(intent, "Select an action"))
                } catch (_: ActivityNotFoundException) {
                    // (handle error)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.addOnFailureListener {
                Timber.e(it)
                dismissProgressDialog()
            }
        }
        if (imp?.views != null) {
            binding.tvViews.text = imp?.views?.toInt()?.toLong()
                ?.let { NumberFormatting.format(it) }
        }
        //Google analytic code
        Timber.i("Setting screen name: %s", TAG)
        mTracker!!.setScreenName(TAG + " " + imp?.implementName)
        mTracker!!.send(ScreenViewBuilder().build())
        Glide.with(this)
            .load(if (imp?.implementImage != null && data.others != null) data.others.implementImagePath + imp?.implementImage?.image else null)
            .placeholder(R.mipmap.no_image)
            .error(R.mipmap.no_image)
            .into(binding.ivImage)
        //Pending because data not coming from server
        /* try {
            binding.llMainDetails.removeAllViews();
            if (!implementJsonObject.isNull("top_spec") && implementJsonObject.has("top_spec")) {
                JSONArray topLabelJsonArray = implementJsonObject.getJSONArray("top_spec");
                if (topLabelJsonArray.length() != 0) {
                    // JSONArray topValueJsonArray = implementJsonObject.getJSONArray("main_top_label_value");
                    for (int j = 0; j < topLabelJsonArray.length(); j++) {
                        JSONObject jsonObjectTopLabel = topLabelJsonArray.getJSONObject(j);
                        ImpDetailsRowBinding hiddenInfo = ImpDetailsRowBinding.inflate(getLayoutInflater(), binding.llMainDetails, false);
                        if (!jsonObjectTopLabel.isNull("spec_data") && jsonObjectTopLabel.has("spec_data")) {
                            hiddenInfo.tvValue.setText(jsonObjectTopLabel.getString("spec_data"));
                            JSONObject jsonObjectimplHarSpecLbl = jsonObjectTopLabel.getJSONObject("implement_harvester_specification_label");
                            hiddenInfo.tvLabel.setText(jsonObjectimplHarSpecLbl.getString("implement_spec_name")*/
        /*topLabelJsonArray.get(j).toString()*/ /*);
                            binding.llMainDetails.addView(hiddenInfo.getRoot());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            binding.llSubDetails.removeAllViews()
            if(imp?.implementSpecification != null) {
                if (imp?.implementSpecification?.size != 0) {
                    for (j in imp?.implementSpecification?.indices!!) {
                        val specification = imp?.implementSpecification!![j]
                        val hiddenInfo = UsedTractorDetailsListItemBinding.inflate(layoutInflater, binding.llSubDetails, false)
                        if (specification != null) {
                            if (binding.llSubDetails.childCount == 0) hiddenInfo.vLine.visibility = View.GONE else hiddenInfo.vLine.visibility = View.VISIBLE
                            hiddenInfo.tvValue.text = specification.specData
                            hiddenInfo.tvLabel.text = specification.implementHarvesterSpecificationLabel.implementSpecName
                            binding.llSubDetails.addView(hiddenInfo.root)
                        }
                    }
                } else {
                    binding.cvDetails.visibility = View.GONE
                }
            }
        }
        catch(e:Exception) {
            e.printStackTrace()
        }
    }
}