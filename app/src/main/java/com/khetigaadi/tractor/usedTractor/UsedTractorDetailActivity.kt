@file:Suppress("PROPERTY_HIDES_JAVA_FIELD", "DEPRECATION")

package com.khetigaadi.tractor.usedTractor
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.text.LineBreaker
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
//import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.MetricAffectingSpan
import android.text.style.StyleSpan
import android.util.Base64
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
//import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
//import androidx.core.text.set
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder
import com.google.android.gms.analytics.Tracker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.firebase.dynamiclinks.ktx.component2
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.khetigaadi.R
import com.khetigaadi.SafetyTipsActivity
import com.khetigaadi.agri.activity.ImageViewActivity
import com.khetigaadi.base.model.AllBannerResp
import com.khetigaadi.base.model.MyResponse
import com.khetigaadi.base.util.StateData
import com.khetigaadi.commons.SharedPreferenceUtils
import com.khetigaadi.commons.activities.BaseActivity
import com.khetigaadi.commons.model.Recent
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.databinding.UsedTractorDetailsBinding
import com.khetigaadi.def.RecentType
import com.khetigaadi.retrofit.KhetiAPIs
import com.khetigaadi.room.repository.RecentRepo
import com.khetigaadi.tractor.usedTractor.model.SimilarParams
import com.khetigaadi.tractor.usedTractor.model.SimilarUsedTractorsResp
import com.khetigaadi.tractor.usedTractor.model.UsedTractorImage
import com.khetigaadi.tractor.usedTractor.model.UsedTractorResp
import com.khetigaadi.tractor.usedTractor.util.UsedTractor
import com.khetigaadi.user.SessionManager
import com.khetigaadi.util.ImageUtils
import com.khetigaadi.util.NumberFormatting
import com.khetigaadi.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors
import javax.inject.Inject
@AndroidEntryPoint
class UsedTractorDetailActivity:BaseActivity() {
    private var tractorId:String? = null
    @SuppressLint("VisibleForTests")
    @JvmField
    @Inject
    var mTracker:Tracker? = null
    private var position = -1
    @JvmField
    @Inject
    var recentRepo:RecentRepo? = null
    @Inject
    lateinit var session:SessionManager
    @Inject
    lateinit var khetiAPIs:KhetiAPIs
    private lateinit var binding:UsedTractorDetailsBinding
    private var tractor:UsedTractor? = null
    private lateinit var viewModel:UsedTractorDetailsViewModel
    private lateinit var usedTractorListAdapter:UsedTractorListAdapter
    var urls: MutableList<String> = ArrayList()
   // @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseKtx")
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
       // WindowCompat.setDecorFitsSystemWindows(window,false)
        binding = UsedTractorDetailsBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
//        ViewCompat.setOnApplyWindowInsetsListener(
//            view,
//            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
//                val systemBarsInsets: Insets =
//                    insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
//                v!!.setPadding(
//                    systemBarsInsets.left,
//                    systemBarsInsets.top,
//                    systemBarsInsets.right,
//                    systemBarsInsets.bottom)
//                insets
//            })
//        this.enableEdgeToEdge()
        viewModel = ViewModelProvider(this)[UsedTractorDetailsViewModel::class.java]
        findViewById()
        initToolbar()
       if(!Utils.isConnectionAvailable(this)){
           Snackbar.make(binding.btnSafetyTips,resources.getString(R.string.no_internet_connection),
               Snackbar.LENGTH_LONG).show()
       }
        readData(intent.extras)
        readDeepLinkData()
        urls.add("")
        initImagesAdapter(urls)
        binding.btnSellerDetails.setOnClickListener {
            val intent = Intent(this@UsedTractorDetailActivity,GetUsedTrSellerDetails::class.java)
            intent.putExtra("tractorId",tractorId)
            startActivity(intent)
        }
        binding.ivFavorites.setOnClickListener { v: View ->
            v.startAnimation(AnimationUtils.loadAnimation(this@UsedTractorDetailActivity,R.anim.anim_scale))
            setFavorites()
        }
        binding.btnGetLoan.setOnClickListener {
            startActivity(Intent(this@UsedTractorDetailActivity,GetLoanActivity::class.java))
        }
        if(Constants.isConnectionAvailable(this)) {
            getDetails()
        }
        else {
            noInternetConnection(binding.parent)
        }
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.tvDisclaimer.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD)
            }
            val allBanner = Gson().fromJson(SharedPreferenceUtils.getInstance(this).getStringValue(Constants.APP_BANNER_KEY,"{}"),AllBannerResp::class.java)
            if(allBanner != null) {
                var word = Html.fromHtml(allBanner.others.disclaimer).toString()
                println("bvcbcvbcvb1234=====$word=="+session.language)
               if(session.language!=null && session.language.equals("hi")) {
                   val vall = word.substring(word.indexOf("hi"))
                   println("vcbvbvbcv====$vall")
                   word = vall
               }
                else {
                   val vall = word.substring(3,word.indexOf("hi")-3)
                   println("vcbvbvbcv22====$vall")
                   word = vall
               }
                val removeEn = word.replace("en:","")
                val removeHi = removeEn.replace("hi:","\n")
                val removeHi2 =  removeHi.replace("Disclaimer:","<b>Disclimer</b>")
                val wordToSpan:Spannable = SpannableString(removeHi)
                wordToSpan.setSpan(ForegroundColorSpan(Color.BLACK),0,12,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                wordToSpan.setSpan(StyleSpan(Typeface.BOLD),0,12,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                val typeface = ResourcesCompat.getFont(this,R.font.poppins_bold)
                wordToSpan.setSpan(CustomTypefaceSpan(typeface!!),0,12,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                binding.tvDisclaimer.text = wordToSpan
                //Html.fromHtml(
            }
        }
        catch(e:Exception) {
            e.printStackTrace()
        }
        binding.btnSafetyTips.setOnClickListener {
            startActivity(Intent(this@UsedTractorDetailActivity,SafetyTipsActivity::class.java))
        }
        binding.includeSimilar.tvLabel.text = getText(R.string.similar_old_tractors)
        binding.includeSimilar.tvSeeMore.visibility = View.GONE
        usedTractorListAdapter = UsedTractorListAdapter(this,
            session.user?.id,binding.includeSimilar.rvList,true)
        binding.includeSimilar.rvList.adapter = usedTractorListAdapter
        usedTractorListAdapter.setOnItemClickListener { position:Int,_:View ->
            try {
                finish()
                val intent = Intent(this,UsedTractorDetailActivity::class.java)
                intent.putExtra("tractorId",usedTractorListAdapter.getItem(position)!!.id)
                intent.putExtra("position",position)
                startActivity(intent)
            }
            catch(e:Exception) {
                e.printStackTrace()
            }
        }
        usedTractorListAdapter.setOnSellerDetailsClickListener { position:Int,_:View? ->
            try {
                val intent = Intent(this,GetUsedTrSellerDetails::class.java)
                intent.putExtra("tractorId",usedTractorListAdapter.getItem(position)!!.id)
                startActivity(intent)
            }
            catch(e:Exception) {
                e.printStackTrace()
            }
        }
        binding.btnCallNow.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + getString(R.string.whatsAppNo)))
            startActivity(intent)
        }
    }
    private fun initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar)
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding.toolbars.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.toolbars.toolbar.title = getString(R.string.old_tractor)
        supportActionBar?.title = getString(R.string.old_tractor)
    }
    private fun getSimilarTractors(tractor:UsedTractor?) {
        val similarParams = SimilarParams()
        similarParams.hp = tractor!!.hp
        similarParams.drive = tractor.drive
        similarParams.city = session.user?.city
        viewModel.getSimilarTractors(similarParams)
            .observe(this) { newTractorRespStateData:StateData<SimilarUsedTractorsResp?> ->
                when(newTractorRespStateData.status) {
                    StateData.Status.LOADING -> binding.includeSimilar.root.visibility = View.GONE
                    StateData.Status.SUCCESS -> if (newTractorRespStateData.data != null) {
                        usedTractorListAdapter.others = newTractorRespStateData.data!!.others
                        usedTractorListAdapter.updateItems(newTractorRespStateData.data!!.data)
                        if(newTractorRespStateData.data!!.data.size != 0) binding.includeSimilar.root.visibility =
                            View.VISIBLE
                    }
                    else -> {}
                }
            }
    }
    private fun readData(bundle:Bundle?) {
        if(bundle != null) {
            tractorId = bundle.getString("tractorId")
            position = bundle.getInt("position",-1)
        }
    }
    private fun readDeepLinkData() {
        if(intent.action != null && intent.action == Intent.ACTION_VIEW) {
            try {
                val uri = intent.data
                val segments = uri!!.path!!.split("/").toTypedArray()
                val data = Base64.decode(segments[segments.size - 2],Base64.DEFAULT)
                tractorId = String(data,StandardCharsets.UTF_8)
            }
            catch(e:Exception) {
                e.printStackTrace()
            }
        }
    }
    //Google analytic code
    private fun getDetails() {
        binding.llProgress.llProgress.visibility = View.VISIBLE
        khetiAPIs.getUsedTractor(tractorId).enqueue(object:Callback<UsedTractorResp> {
            @SuppressLint("VisibleForTests")
            override fun onResponse(
                call:Call<UsedTractorResp>,
                response:Response<UsedTractorResp>) {
                if(response.isSuccessful) {
                    if(response.body() != null) {
                        if(!response.body()!!.isError) {
                            tractor = response.body()!!.data
                            if(tractor == null)
                                return
                            getSimilarTractors(tractor)
                            if(tractor?.brand != null && tractor?.model != null) {
                                binding.toolbars.toolbar.title = String.format("%s %s",tractor?.brand?.name,tractor?.model?.getName())
                                binding.tvTractorName.text = binding.toolbars.toolbar.title
                                binding.ivShare.setOnClickListener {
                                    showProgressDialog(getString(R.string.please_wait))
                                    try {
                                       // println("vcbvcbcv_bcbc324==="+urls.size+urls[0])
                                        if(urls.isNotEmpty())
                                            Glide.with(this@UsedTractorDetailActivity)
                                            .asBitmap()
                                            .load(urls[0])
                                            .into(object:CustomTarget<Bitmap>() {
                                                override fun onResourceReady(resource:Bitmap,transition:Transition<in Bitmap>?) {
                                                    try {
                                                        share(ImageUtils.getUriFromBitmapForShare(this@UsedTractorDetailActivity,resource))
                                                    }
                                                    catch(e:Exception) {
                                                        e.printStackTrace()
                                                        dismissProgressDialog()
                                                    }
                                                }
                                                override fun onLoadCleared(placeholder:Drawable?) {}
                                            })
                                    }
                                    catch(ex:Exception) {
                                        ex.printStackTrace()
                                        dismissProgressDialog()
                                    }
                                }
                                if(tractor?.user != null) {
                                    //Google analytic code
                                    mTracker!!.setScreenName(TAG + " " + binding.toolbars.toolbar.title.toString() + " " + tractor?.user?.fname + " " + tractor?.user?.lname)
                                    mTracker!!.send(ScreenViewBuilder().build())
                                    if(tractor?.user?.city != null) {
                                        binding.tvCity.text = tractor?.user?.city?.name
                                    }
                                }
                            }
                            if(tractor?.price != null) {
                                binding.tvPrice.text = String.format("%s%s",getString(R.string.rs),Utils.priceConvert(tractor?.price))
                            }
                            if(tractor?.modelYear != null && tractor?.modelMonth != null) {
                                binding.tvYear.text = String.format("%s %s", tractor?.modelYear, tractor?.modelMonth)
                            } else binding.tvYear.visibility = View.GONE
                            if(tractor?.hoursDriven != null && !TextUtils.isEmpty(tractor?.hoursDriven)) {
                                binding.tvDriven.text = tractor?.hoursDriven
                            } else binding.tvDriven.visibility = View.GONE
                            if(tractor?.registrationNo != null) {
                                binding.tvRTONo.text = tractor?.registrationNo
                            }
                            else {
                                binding.tvRTONo.visibility = View.GONE
                            }
                            if(tractor?.multipleUsedTractorImage != null && tractor?.multipleUsedTractorImage?.size != 0) {
                                urls = tractor?.multipleUsedTractorImage!!.stream()
                                    .map { n:UsedTractorImage ->
                                        response.body()!!.others.tractorImagePath + n.usedTractorImage
                                    }
                                    .collect(Collectors.toList())
                                initImagesAdapter(urls)
                                if(urls.size != 1) binding.indicator.setViewPager(binding.viewpager2)
                            }
                            try {
                                if(tractor?.user != null && user.id == tractor?.user?.id) {
                                    binding.btnSellerDetails.visibility = View.GONE
                                }
                                else if(tractor?.soldUnsoldStatus != null && tractor?.soldUnsoldStatus != "0") {
                                    binding.ivSold.visibility = View.VISIBLE
                                    binding.btnSellerDetails.visibility = View.GONE
                                    binding.tvViews.visibility = View.GONE
                                }
                                else {
                                    binding.btnSellerDetails.visibility = View.VISIBLE
                                    binding.ivSold.visibility = View.GONE
                                    binding.tvViews.visibility = View.VISIBLE
                                }
                            }
                            catch(e:Exception) {
                                e.printStackTrace()
                            }
                            if(tractor?.favoritesStatus == true) {
                                binding.ivFavorites.setImageResource(R.drawable.ic_favorites)
                            }
                            else {
                                binding.ivFavorites.setImageResource(R.drawable.ic_favorites_border)
                            }
                            if(tractor?.userViews != null) {
                                binding.tvViews.text = tractor?.userViews?.toInt()?.let {
                                    NumberFormatting.format(it.toLong())
                                }
                            }
                            if(tractor?.exactHp != null && !TextUtils.isEmpty(tractor?.exactHp)) {
                                binding.includeExactHp.tvLabel.text = getString(R.string.exact_HP)
                                binding.includeExactHp.tvValue.text = tractor?.exactHp
                            }
                            else binding.includeExactHp.container.visibility = View.GONE
                            if(tractor?.tractorNoc != null && !TextUtils.isEmpty(tractor?.tractorNoc)) {
                                binding.includeNOC.tvLabel.text = getString(R.string.financier_hypo_noc)
                                binding.includeNOC.tvValue.text = tractor?.tractorNoc
                            }
                            else binding.includeNOC.container.visibility = View.GONE
                            if(tractor?.rc != null && !TextUtils.isEmpty(tractor?.rc)) {
                                binding.includeRCDoc.tvLabel.text = getString(R.string.rc_document)
                                binding.includeRCDoc.tvValue.text = tractor?.rc
                            } else binding.includeRCDoc.container.visibility = View.GONE
                            if(tractor?.owner != null && !TextUtils.isEmpty(tractor?.owner)) {
                                binding.includeNoOfOwner.tvLabel.text = getString(R.string.rc_document)
                                binding.includeNoOfOwner.tvValue.text = tractor?.owner
                            } else binding.includeNoOfOwner.container.visibility = View.GONE
                            if(tractor?.engineCondition != null && !TextUtils.isEmpty(tractor?.engineCondition)) {
                                binding.includeEngineCondition.tvLabel.text = getString(R.string.engine_condition)
                                binding.includeEngineCondition.tvValue.text = tractor?.engineCondition
                            }
                            else binding.includeEngineCondition.container.visibility = View.GONE
                            if(tractor?.tyreCondition != null && !TextUtils.isEmpty(tractor?.tyreCondition)) {
                                binding.includeTyreCondition.tvLabel.text = getString(R.string.tyre_condition)
                                binding.includeTyreCondition.tvValue.text = tractor?.tyreCondition
                            } else binding.includeTyreCondition.container.visibility = View.GONE
                            if(tractor?.modelYear != null && !TextUtils.isEmpty(tractor?.modelYear)) {
                                binding.includeMonthYear.tvLabel.text = getString(R.string.month_year)
                                binding.includeMonthYear.tvValue.text = tractor?.modelYear
                            } else binding.includeMonthYear.container.visibility = View.GONE
                            if(tractor?.registrationNo != null && !TextUtils.isEmpty(tractor?.registrationNo)) {
                                binding.includeRTONo.tvLabel.text = getString(R.string.rto_no)
                                binding.includeRTONo.tvValue.text = tractor?.registrationNo
                            }
                            else binding.includeRTONo.container.visibility = View.GONE
                            if(tractor?.about != null && !TextUtils.isEmpty(tractor?.about)) {
                                binding.includeAboutUs.tvLabel.text = getString(R.string.about_us)
                                binding.includeAboutUs.tvValue.text = tractor?.about
                            }
                            else binding.includeAboutUs.container.visibility = View.GONE
                            binding.llProgress.llProgress.visibility = View.GONE
                            recentRepo!!.insert(tractor?.id?.toInt()?.let {
                                    Recent(
                                        it,
                                        binding.toolbars.toolbar.title.toString(),
                                        urls[0],
                                        RecentType.OLD_TRACTOR,
                                        System.currentTimeMillis())
                                })
                        }
                        else {
                            Toast.makeText(this@UsedTractorDetailActivity,response.body()!!.msg.toString(),Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else {
                    Toast.makeText(this@UsedTractorDetailActivity,response.message(),Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call:Call<UsedTractorResp?>,t:Throwable) {
                Timber.e(t,"onFailure: ")
                Toast.makeText(this@UsedTractorDetailActivity,t.message,Toast.LENGTH_SHORT).show()
            }
        })
    }
    @SuppressLint("UseKtx")
    private fun share(fileUri:Uri) {
        Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://www.khetigaadi.com/oldTractor/$tractorId")
            domainUriPrefix = getString(R.string.domain_uri_prefix)
            androidParameters { }
        }.addOnSuccessListener { (shortLink, _) ->
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_STREAM,fileUri)
            intent.putExtra(Intent.EXTRA_SUBJECT,binding.toolbars.toolbar.title.toString())
            intent.putExtra(Intent.EXTRA_TEXT,resources.getString(R.string.i_was_viewing) + " " + resources.getString(
                    R.string.old
                ) + " \"" + binding.toolbars.toolbar.title.toString() + "\" " + shortLink)
            startActivity(Intent.createChooser(intent,"Select an action"))
            dismissProgressDialog()
        }.addOnFailureListener {
            Timber.e(it)
        }
    }
    private fun initImagesAdapter(urls:List<String>?) {
        val imagesAdapter = UsedTractorImagesAdapter(this@UsedTractorDetailActivity,urls) {
            val intent = Intent(this@UsedTractorDetailActivity,ImageViewActivity::class.java)
            intent.putStringArrayListExtra("urls",this.urls as ArrayList<String>?)
            startActivity(intent)
        }
        binding.viewpager2.adapter = imagesAdapter
    }
    private fun setFavorites() {
        viewModel.setUsedTractorFavorites(tractor)
            .observe(this) { response:StateData<MyResponse?> ->
                when(response.status) {
                    StateData.Status.LOADING -> {}
                    StateData.Status.SUCCESS -> {
                        tractor?.favoritesStatus = !tractor?.favoritesStatus!!
                        if(tractor?.favoritesStatus == true) {
                            binding.ivFavorites.setImageResource(R.drawable.ic_favorites)
                        }
                        else {
                            binding.ivFavorites.setImageResource(R.drawable.ic_favorites_border)
                        }
                    }
                    StateData.Status.ERROR -> Toast.makeText(this,response.error,Toast.LENGTH_SHORT).show()
                    else -> {}
                }
            }
    }
    private fun findViewById() {
        Glide.with(this@UsedTractorDetailActivity)
            .load(R.drawable.loader_white)
            .into(binding.llProgress.ivLoader)
    }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        val returnIntent = Intent()
        if(tractor != null)
            returnIntent.putExtra(isFavourite,tractor?.favoritesStatus)
        returnIntent.putExtra("position",position)
        setResult(RESULT_OK,returnIntent)
        super.onBackPressed()
    }
    companion object {
        private val TAG = UsedTractorDetailActivity::class.java.simpleName
        const val isFavourite = "isFavorites"
    }
    class CustomTypefaceSpan(private val typeface:Typeface):MetricAffectingSpan() {
        override fun updateDrawState(tp:TextPaint) {
            tp.typeface = typeface
        }
        override fun updateMeasureState(tp:TextPaint) {
            tp.typeface = typeface
        }
    }
}