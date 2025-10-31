package com.khetigaadi.tractor.newTractor
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.khetigaadi.base.model.MyResponse
import com.khetigaadi.base.util.StateData
import com.khetigaadi.commons.fragments.BaseFragment
import com.khetigaadi.commons.model.Recent
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.commons.utils.Drive
import com.khetigaadi.databinding.NewTractorOverviewDeatailsBinding
import com.khetigaadi.def.PriceType
import com.khetigaadi.def.RecentType
import com.khetigaadi.room.repository.RecentRepo
import com.khetigaadi.tractor.PopularTractorAdapter
import com.khetigaadi.tractor.newTractor.model.NewTractor
import com.khetigaadi.tractor.newTractor.model.NewTractorOverviewResp
import com.khetigaadi.tractor.newTractor.model.SimilarNewTractorResp
import com.khetigaadi.tractor.review.ReviewActivity
import com.khetigaadi.tractor.usedTractor.model.SimilarParams
import com.khetigaadi.user.SessionManager
import com.khetigaadi.util.NumberFormatting
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class NewTractorDetailsOverview : BaseFragment() {
    private val TAG = "NewTractorDetailsOvervi"
    @JvmField
    @Inject
    var mTracker: Tracker? = null
    private lateinit var binding: NewTractorOverviewDeatailsBinding
    private var viewModel: NewTractorViewModel? = null
    @Inject
    lateinit var sessionManager:SessionManager
    private var popularTractorAdapter: SimilarTractorAdapter? = null
    @JvmField
    @Inject
    var recentRepo: RecentRepo? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewTractorOverviewDeatailsBinding.inflate(inflater, container, false)
        val view: View = binding.root
        initSimilarTractor()
        Glide.with(requireActivity())
            .load(R.drawable.loader_white)
            .into(binding.progressBar.ivLoader)
        return view
    }
    private fun initUI(data: NewTractorOverviewResp?) {
        val tractor = data!!.data[0]
        binding.ivShare.setOnClickListener {
            try {
                Firebase.dynamicLinks.shortLinkAsync {
                    link = Uri.parse("https://www.khetigaadi.com/newTractor/" + tractor.id)
                    domainUriPrefix = getString(R.string.domain_uri_prefix)
                    // Open links with this app on Android
                    androidParameters { }
                }.addOnSuccessListener { (shortLink, _) ->
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, tractor.name)
                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.i_was_viewing) + " " + resources.getString(
                        R.string.btn_new
                    ) + " \"" + tractor.name + "\" " + shortLink)
                    requireContext().startActivity(Intent.createChooser(intent, getString(R.string.selectAnAction)))
                }.addOnFailureListener {
                    Timber.e(it)
                }
            } catch (ex: ActivityNotFoundException) {
                // (handle error)
            }
        }
        binding.tvTractorName.text = tractor.name
        if (!TextUtils.isEmpty(tractor.exactHp)) {
            binding.tvHP.text = String.format("%s %s", tractor.exactHp, getString(R.string.hp))
        } else {
            binding.tvHP.visibility = View.GONE
        }
        if (tractor.drive != null) {
            binding.ivDrive.visibility = View.VISIBLE
            if (tractor.drive.id == 1) {
                binding.ivDrive.setImageResource(R.drawable.ic_2wd)
            } else {
                binding.ivDrive.setImageResource(R.drawable.ic_4wd)
            }
        } else {
            binding.ivDrive.visibility = View.GONE
        }
        if (tractor.getShortSpecData != null) {
            for (j in tractor.getShortSpecData.indices) {
                val shortSpec = tractor.getShortSpecData[j]
                if (shortSpec.tractorSpecId == 4) {
                    if (!TextUtils.isEmpty(shortSpec.specData)) {
                        binding.tvDisplacementCC.text = shortSpec.specData
                    } else {
                        binding.tvDisplacementCC.visibility = View.GONE
                    }
                }
                if (shortSpec.tractorSpecId == 5) {
                    if (!TextUtils.isEmpty(shortSpec.specData)) {
                        binding.tvNoOfCylinder.text = shortSpec.specData
                    } else {
                        binding.tvNoOfCylinder.visibility = View.GONE
                    }
                }
                if (shortSpec.tractorSpecId == 25) {
                    if (!TextUtils.isEmpty(shortSpec.specData)) {
                        binding.tvPTO.text = shortSpec.specData
                    } else {
                        binding.tvPTO.visibility = View.GONE
                    }
                }
            }
        }
        if (tractor.isBookmark) {
            binding.ivFavorites.setImageResource(R.drawable.ic_favorites)
        } else {
            binding.ivFavorites.setImageResource(R.drawable.ic_favorites_border)
        }
        if (!TextUtils.isEmpty(tractor.userView)) {
            binding.tvViews.text = NumberFormatting.format(tractor.userView)
        } else {
            binding.tvViews.visibility = View.GONE
        }
        binding.ivFavorites.setOnClickListener { v: View ->
            v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_scale))
            setFavorites(tractor)
        }
        Glide.with(this)
            .load(
                if (data.others != null && tractor.image != null) Constants.decodeURL(
                    data.others.tractorImagePath + tractor.image.tractor_img_name
                ) else null
            )
            .placeholder(R.mipmap.no_image)
            .error(R.mipmap.no_image)
            .into(binding.ivImage)
        getSimilarTractors(tractor)
        recentRepo!!.insert(
            Recent(
                tractor.id.toInt(),
                tractor.name,
                if (data.others != null && tractor.image != null) Constants.decodeURL(
                    data.others.tractorImagePath + tractor.image.tractor_img_name
                ) else "",
                RecentType.NEW_TRACTOR,
                System.currentTimeMillis()
            )
        )
        binding.tvReview.setOnClickListener {
            val intent = Intent(activity, ReviewActivity::class.java)
            intent.putExtra("tractor", tractor)
            startActivity(intent)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider((activity as NewTractorDetails?)!!)[NewTractorViewModel::class.java]
        viewModel!!.overViewDetails.observe(viewLifecycleOwner) { newTractorOverviewRespStateData: StateData<NewTractorOverviewResp?> ->
            when (newTractorOverviewRespStateData.status) {
                StateData.Status.LOADING -> binding.progressBar.llProgress.visibility =
                    View.VISIBLE
                StateData.Status.SUCCESS -> {
                    binding.progressBar.llProgress.visibility = View.GONE
                    if (newTractorOverviewRespStateData.data != null) {
                        initUI(newTractorOverviewRespStateData.data)
                    }
                }
                StateData.Status.ERROR -> Toast.makeText(
                    activity,
                    newTractorOverviewRespStateData.error,
                    Toast.LENGTH_SHORT
                ).show()
                else -> {}
            }
        }
    }
    private fun getSimilarTractors(tractor: NewTractor) {
        val similarParams = SimilarParams()
        similarParams.exactHp = tractor.exactHp
        if (tractor.drive.name.equals("2wd", ignoreCase = true)) similarParams.drive =
            Drive(1, "2WD", false) else similarParams.drive = Drive(2, "4WD", false)
        similarParams.city = sessionManager.user!!.city
        viewModel!!.getSimilarTractors(similarParams)
            .observe(viewLifecycleOwner) { newTractorRespStateData: StateData<SimilarNewTractorResp?> ->
                when (newTractorRespStateData.status) {
                    StateData.Status.LOADING -> binding.includeSimilar.root.visibility = View.GONE
                    StateData.Status.SUCCESS -> if (newTractorRespStateData.data != null) {
                        popularTractorAdapter!!.others = newTractorRespStateData.data!!.others
                        popularTractorAdapter!!.updateItems(newTractorRespStateData.data!!.data)
                        if (newTractorRespStateData.data!!.data.size != 0) binding.includeSimilar.root.visibility =
                            View.VISIBLE
                    }
                    StateData.Status.ERROR -> {}
                    else -> {}
                }
            }
    }
    private fun initSimilarTractor() {
        binding.includeSimilar.tvLabel.text = getString(R.string.similar_tractors)
        binding.includeSimilar.tvSeeMore.visibility = View.GONE
        popularTractorAdapter =
            SimilarTractorAdapter(requireActivity(), binding.includeSimilar.rvList)
        binding.includeSimilar.rvList.adapter = popularTractorAdapter
        binding.includeSimilar.rvList.setHasFixedSize(true)
        binding.includeSimilar.rvList.isNestedScrollingEnabled = true
        binding.includeSimilar.rvList.addOnItemTouchListener(object :
            RecyclerView.OnItemTouchListener {
            var lastX = 0
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> lastX = e.x.toInt()
                    MotionEvent.ACTION_MOVE -> {
                        val isScrollingRight = e.x < lastX
                        (activity as NewTractorDetails?)!!.binding.viewPager2.isUserInputEnabled =
                            isScrollingRight && (binding.includeSimilar.rvList.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() == binding.includeSimilar.rvList.adapter!!
                                .itemCount - 1 ||
                                    !isScrollingRight && (binding.includeSimilar.rvList.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition() == 0
                    }
                    MotionEvent.ACTION_UP -> {
                        lastX = 0
                        (activity as NewTractorDetails?)!!.binding.viewPager2.isUserInputEnabled =
                            true
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
        popularTractorAdapter!!.setOnItemClickListener(object : PopularTractorAdapter.Listener {
            override fun onItemClick(position: Int, v: View?) {
                val intent = Intent(activity, NewTractorDetails::class.java)
                intent.putExtra(
                    NewTractorDetails.newTractorId, popularTractorAdapter!!.getItem(position)!!
                        .id
                )
                activity!!.finish()
                startActivity(intent)
            }

            override fun onGetRoadPriceClick(position: Int, v: View?) {
                val intent = Intent(activity, NewTrGetOnRoadPriceActivity::class.java)
                intent.putExtra("id", popularTractorAdapter?.getItem(position)!!.id)
                intent.putExtra("name", popularTractorAdapter?.getItem(position)!!.name)
                intent.putExtra(
                    "image",
                    popularTractorAdapter?.others?.tractorImagePath + popularTractorAdapter?.getItem(
                        position
                    )?.image?.tractor_img_name
                )
                intent.putExtra("priceType", PriceType.TRACTOR)
                startActivity(intent)
            }
        })
    }
    override fun onResume() {
        super.onResume()
        //Google analytic code
        mTracker!!.setScreenName(TAG)
        mTracker!!.send(ScreenViewBuilder().build())
    }
    private fun setFavorites(tractor: NewTractor) {
        viewModel!!.setMyFavorites(tractor)
            .observe(viewLifecycleOwner) { myResponse: StateData<MyResponse?> ->
                when (myResponse.status) {
                    StateData.Status.LOADING -> {}
                    StateData.Status.SUCCESS -> {
                        tractor.isBookmark = !tractor.isBookmark
                        if (tractor.isBookmark) {
                            binding.ivFavorites.setImageResource(R.drawable.ic_favorites)
                        } else {
                            binding.ivFavorites.setImageResource(R.drawable.ic_favorites_border)
                        }
                    }
                    StateData.Status.ERROR -> Toast.makeText(
                        activity,
                        myResponse.error,
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> {}
                }
            }
    }
}