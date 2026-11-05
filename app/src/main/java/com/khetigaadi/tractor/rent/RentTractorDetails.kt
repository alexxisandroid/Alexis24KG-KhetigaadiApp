package com.khetigaadi.tractor.rent
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder
import com.google.android.gms.analytics.Tracker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.firebase.dynamiclinks.ktx.component2
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.khetigaadi.R
import com.khetigaadi.base.model.MyResponse
import com.khetigaadi.base.util.StateData
import com.khetigaadi.commons.SharedPreferenceUtils
import com.khetigaadi.commons.activities.BaseActivity
import com.khetigaadi.commons.model.Recent
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.databinding.ImplementRowBinding
import com.khetigaadi.databinding.RentTractorDetailsBinding
import com.khetigaadi.def.RecentType
import com.khetigaadi.retrofit.KhetiAPIs
import com.khetigaadi.room.repository.RecentRepo
import com.khetigaadi.tractor.PopularTractorAdapter
import com.khetigaadi.tractor.rent.model.RentTractor
import com.khetigaadi.tractor.rent.model.RentTractorDetailsResp
import com.khetigaadi.tractor.rent.model.SimilarRentTractorsResp
import com.khetigaadi.tractor.usedTractor.UsedTractorDetailActivity
import com.khetigaadi.tractor.usedTractor.model.SimilarParams
import com.khetigaadi.user.SessionManager
import com.khetigaadi.util.NumberFormatting
import com.khetigaadi.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import androidx.core.graphics.toColorInt
@AndroidEntryPoint
class RentTractorDetails:BaseActivity() {
    private var tractorId:String? = null
    @JvmField
    @Inject
    var mTracker:Tracker? = null
    private var favoritesStatus = false
    private var mMap: GoogleMap? = null
    @Inject
    lateinit var khetiAPIs: KhetiAPIs
    private var mStrokeColor = 0
    private val mCircles: MutableList<DraggableCircle> = ArrayList(1)
    private val disposable = CompositeDisposable()
    @JvmField
    @Inject
    var session: SessionManager? = null
    @JvmField
    @Inject
    var pref: SharedPreferenceUtils? = null
    @JvmField
    @Inject
    var recentRepo: RecentRepo? = null
    private var position = -1
    private lateinit var binding: RentTractorDetailsBinding
    private lateinit var viewModel: RentTractorDetailsViewModel
    private var popularTractorAdapter: SimilarRentTractorAdapter? = null
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
       // WindowCompat.setDecorFitsSystemWindows(window,false)
        binding = RentTractorDetailsBinding.inflate(layoutInflater)
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

        user = session?.user
        viewModel = ViewModelProvider(this)[RentTractorDetailsViewModel::class.java]
        readData(intent.extras)
        readDeepLinkData()
        setSupportActionBar(binding.toolbars.toolbar)
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding.toolbars.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.toolbars.toolbar.title = resources.getString(R.string.app_name)
        binding.btnSendEnq.setOnClickListener {
            val intent = Intent(this@RentTractorDetails, RentTractorSendEnquiry::class.java)
            intent.putExtra("tractorId", tractorId)
            startActivity(intent)
        }
        binding.ivFavorites.setOnClickListener { v: View ->
            v.startAnimation(AnimationUtils.loadAnimation(this@RentTractorDetails, R.anim.anim_scale))
            setFavorites(tractorId, favoritesStatus)
        }
        Glide.with(this@RentTractorDetails)
            .load(R.drawable.loader_white)
            .into(binding.llProgress.ivLoader)
        if (Constants.isConnectionAvailable(this@RentTractorDetails)) {
            rentTrDetails
        } else {
            noInternetConnection(binding.parent)
        }
        initSimilar()
    }
    private fun initSimilar() {
        binding.includeSimilar.tvLabel.text = getString(R.string.similar_tractors)
        binding.includeSimilar.tvSeeMore.visibility = View.GONE
        popularTractorAdapter = SimilarRentTractorAdapter(this, binding.includeSimilar.rvList)
        binding.includeSimilar.rvList.adapter = popularTractorAdapter
        popularTractorAdapter!!.setOnItemClickListener(object : PopularTractorAdapter.Listener {
            override fun onGetRoadPriceClick(position: Int, v: View?) {}
            override fun onItemClick(position: Int, v: View) {
                finish()
                val intent = Intent(this@RentTractorDetails, RentTractorDetails::class.java)
                intent.putExtra("tractorId", ""+popularTractorAdapter!!.getItem(position)!!.rentTractorId)
                startActivity(intent)
            }
        })
    }
    private fun readData(bundle: Bundle?) {
        if (bundle != null) {
            tractorId = bundle.getString("tractorId")
            position = bundle.getInt("position", -1)
        }
    }
    private fun readDeepLinkData() {
        if (intent.action != null && intent.action == Intent.ACTION_VIEW) {
            try {
                val uri = intent.data
                val segments = uri!!.path!!.split("/".toRegex()).toTypedArray()
                val data = Base64.decode(segments[segments.size - 2], Base64.DEFAULT)
                tractorId = String(data, StandardCharsets.UTF_8)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Timber.e(tractorId)
        }
    }
    override fun onActivityResult(requestCode:Int,resultCode:Int,data:Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (session!!.isLoggedIn) {
                setFavorites(tractorId, favoritesStatus)
            }
        }
    }
    private fun setFavorites(tractorId: String?, fStatus: Boolean) {
        val rentTractor = RentTractor()
        rentTractor.rentTractorId = tractorId
        disposable.add(
            khetiAPIs.rentTractorFavoriteStateChange(rentTractor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : DisposableSingleObserver<MyResponse?>() {
//                    override fun onSuccess(myResponse: MyResponse) {
//                        if (myResponse != null) {
//                            if (!myResponse.isError) {
//                                favoritesStatus =
//                                    if (myResponse.msg[0].equals("Bookmark Removed Successfully")
//                                    ) {
//                                        binding.ivFavorites.setImageResource(R.drawable.ic_favorites_border)
//                                        false
//                                    } else {
//                                        binding.ivFavorites.setImageResource(R.drawable.ic_favorites)
//                                        true
//                                    }
//                            } else {
//                                Toast.makeText(
//                                    this@RentTractorDetails,
//                                    myResponse.msg[0],
//                                    Toast.LENGTH_LONG
//                                ).show()
//
//                            }
//                        }
//                    }
//                    override fun onError(e: Throwable) {
//                        Timber.e(e, "onError: ")
//                    }
//                })


                .subscribeWith(object : DisposableSingleObserver<MyResponse>() {
                    // This method handles the successful response.
                    override fun onSuccess(myResponse: MyResponse) {
                        if (myResponse != null) {
                            if (!myResponse.isError) {
                                favoritesStatus =
                                    if (myResponse.msg[0].equals("Bookmark Removed Successfully")
                                    ) {
                                        binding.ivFavorites.setImageResource(R.drawable.ic_favorites_border)
                                        false
                                    } else {
                                        binding.ivFavorites.setImageResource(R.drawable.ic_favorites)
                                        true
                                    }
                            } else {
                                Toast.makeText(
                                    this@RentTractorDetails,
                                    myResponse.msg[0],
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                    }
                    // This method handles any failure.
                    override fun onError(e: Throwable)  {
                        Timber.e(e, "onError: ")
                    }
                })
        )
    }
    private val rentTrDetails: Unit
        private get() {
            val params1 = HashMap<String, String?>()
            params1["rent_tractor_id"] = tractorId
            viewModel.getRentTractor(params1).observe(this) { rentTractorDetailsRespStateData: StateData<RentTractorDetailsResp?> ->
                when (rentTractorDetailsRespStateData.status) {
                    StateData.Status.LOADING -> binding.llProgress.llProgress.visibility = View.VISIBLE
                    StateData.Status.SUCCESS -> {
                        binding.llProgress.llProgress.visibility = View.GONE
                        if (rentTractorDetailsRespStateData.data != null) {
                            mapToUI(rentTractorDetailsRespStateData.data)
                            getSimilarRentTractors(rentTractorDetailsRespStateData.data!!.data)
                        }
                    }
                    StateData.Status.ERROR -> {
                        Toast.makeText(this@RentTractorDetails, rentTractorDetailsRespStateData.error, Toast.LENGTH_LONG).show()
                        binding.includeSimilar.container.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
    private fun getSimilarRentTractors(rentTractor: RentTractor) {
        val similarParams = SimilarParams()
        similarParams.drive = rentTractor.drive
        similarParams.hp = rentTractor.hp
        similarParams.city = user.city
        viewModel.getSimilarRentTractors(similarParams).observe(this) { rentTractorDetailsRespStateData: StateData<SimilarRentTractorsResp?> ->
            when (rentTractorDetailsRespStateData.status) {
                StateData.Status.LOADING -> binding.includeSimilar.container.visibility = View.GONE
                StateData.Status.SUCCESS -> if (rentTractorDetailsRespStateData.data != null && rentTractorDetailsRespStateData.data!!.data.size != 0) {
                    binding.includeSimilar.container.visibility = View.VISIBLE
                    popularTractorAdapter!!.updateItems(rentTractorDetailsRespStateData.data!!.data)
                }
                StateData.Status.ERROR -> {
                    Toast.makeText(this@RentTractorDetails, rentTractorDetailsRespStateData.error, Toast.LENGTH_LONG).show()
                    binding.includeSimilar.container.visibility = View.GONE
                }
                else -> {}
            }
        }
    }
    private fun mapToUI(data: RentTractorDetailsResp?) {
        val tractor = data!!.data
        if (tractor.exactHp != null) {
            binding.tvHP.text = tractor.exactHp
        }
        binding.tvViews.text = NumberFormatting.format(tractor.userViews.toLong())
        if (tractor.isBookmark) {
            binding.ivFavorites.setImageResource(R.drawable.ic_favorites)
        } else {
            binding.ivFavorites.setImageResource(R.drawable.ic_favorites_border)
        }
        if (tractor.drive != null) {
            binding.tvDrive.text = tractor.drive.name
        }
        if (tractor.rentTractorBrand != null && tractor.tractorModel != null) {
            binding.tvTractorName.text = tractor.rentTractorBrand.name + " " + tractor.tractorModel.getName()
            binding.toolbars.toolbar.title = String.format("%s %s", tractor.rentTractorBrand.name, tractor.tractorModel.getName())
        }
        binding.ivShare.setOnClickListener {
            Firebase.dynamicLinks.shortLinkAsync {
                link = Uri.parse("https://www.khetigaadi.com/rentTractor/$tractorId")
                domainUriPrefix = getString(R.string.domain_uri_prefix)
                androidParameters { }
            }.addOnSuccessListener { (shortLink, _) ->
                try {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, tractor.rentTractorBrand.name + " " + tractor.tractorModel.getName())
                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.i_was_viewing) + " " + resources.getString(R.string.rent) + " \"" + tractor.rentTractorBrand.name + (" " + tractor.tractorModel.getName()
                            + "\" " + shortLink))
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
        if (tractor.user != null) {
            if (user.id == tractor.user.id) {
                binding.btnSendEnq.visibility = View.GONE
            } else {
                binding.btnSendEnq.visibility = View.VISIBLE
            }
            if (!TextUtils.isEmpty(tractor.user.fname) && !TextUtils.isEmpty(tractor.user.lname)) {
                mTracker!!.setScreenName(TAG + " " + tractor.user.fname + " " + tractor.user.lname)
                mTracker!!.send(ScreenViewBuilder().build())
                binding.tvOwnerName.text = String.format("%s %s", tractor.user.fname, tractor.user.lname)
            }
            if (tractor.user.city != null) {
                binding.tvCity.text = tractor.user.city.name
                val latLng = Constants.getLatLong(this@RentTractorDetails, tractor.user.city.name)
                latLng?.let { getMap(it) }
            }
        }
        //newTractorImagePath
        Glide.with(this)
            .load(if(data.others != null && tractor.rentTractorTable.tractorImage != null) data.others.tractorImagePath+tractor.rentTractorTable.tractorImage.tractor_img_name else "")
            .error(R.mipmap.no_image)
            .placeholder(R.mipmap.no_image)
            .into(binding.ivImage)
        if (tractor.rentTractorImplements != null) {
            val imps = tractor.rentTractorImplements
            var i = 0
            while (i < imps.size) {
                val linearLayout = LinearLayout(this@RentTractorDetails)
                var j = 0
                while (j <= 1 && i + j < imps.size) {
                    val rowLayout = ImplementRowBinding.inflate(layoutInflater, binding.llImpsList, false)
                    val implement = imps[i + j]
                    rowLayout.tvRate.text = String.format("%s%s/%s", getString(R.string.rs), Utils.priceConvert(implement.rate), implement.unit.unitName)
                    rowLayout.tvImpName.text = implement.implementType.implementTypeName
                    Glide.with(this)
                        .load(if (data.others != null && implement.image != null) data.others.implementTypeImagePath + implement.image else "")
                        .error(R.mipmap.no_image)
                        .placeholder(R.mipmap.no_image)
                        .into(rowLayout.image)
                    linearLayout.addView(rowLayout.root)
                    j++
                }
                binding.llImpsList.addView(linearLayout)
                i += 2
            }
        }
        //newTractorImagePath
        recentRepo!!.insert(Recent(tractor.rentTractorId.toInt(), binding.tvTractorName.text.toString(), if (data.others != null && tractor.rentTractorTable.tractorImage != null) data.others.tractorImagePath + tractor.rentTractorTable.tractorImage.tractor_img_name else "", RecentType.RENT_TRACTOR, System.currentTimeMillis()))
    }
    private fun getMap(latLng: LatLng) {
        try {
            val mapFragment = this.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync { map: GoogleMap ->
                mMap = map
                map.setContentDescription(getString(R.string.map_circle_description))
                mStrokeColor = Color.BLACK
                val circle = DraggableCircle(latLng, DEFAULT_RADIUS, false)
                mCircles.add(circle)
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private inner class DraggableCircle(center: LatLng?, radius: Double, clickable: Boolean) {
        init {
            val centerMarker = mMap!!.addMarker(MarkerOptions()
                .position(center!!)
                .draggable(true))
            val circle = mMap!!.addCircle(CircleOptions()
                .center(center)
                .radius(radius)
                .strokeWidth(2f)
                .fillColor("#500084d3".toColorInt())
                .strokeColor(mStrokeColor))
        }
    }
    override fun onBackPressed() {
        val returnIntent = Intent()
        returnIntent.putExtra(UsedTractorDetailActivity.isFavourite, favoritesStatus)
        returnIntent.putExtra("position", position)
        setResult(RESULT_OK, returnIntent)
        super.onBackPressed()
    }
    companion object {
        private val TAG = RentTractorDetails::class.java.simpleName
        private const val DEFAULT_RADIUS = 500.0
    }
}