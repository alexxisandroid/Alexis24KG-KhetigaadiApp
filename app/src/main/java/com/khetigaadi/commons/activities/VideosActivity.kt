package com.khetigaadi.commons.activities
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.khetigaadi.R
import com.khetigaadi.base.model.youtube.Id
import com.khetigaadi.base.model.youtube.Item
import com.khetigaadi.base.model.youtube.ListItem
import com.khetigaadi.base.model.youtube.VideoListResp
import com.khetigaadi.base.model.youtube.VideoResp
import com.khetigaadi.commons.adapter.VideoListAdapter
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.databinding.ActivityVideosBinding
import com.khetigaadi.retrofit.KhetiAPIs
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class VideosActivity:BaseActivity() {
    private var nextPageToken:String = ""
    private lateinit var mAdapter:VideoListAdapter
    private lateinit var binding:ActivityVideosBinding
    @Inject
    lateinit var khetiAPIs:KhetiAPIs
    @Inject
    lateinit var disposable:CompositeDisposable
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
       // WindowCompat.setDecorFitsSystemWindows(window,false)

        enableEdgeToEdge()
        binding = ActivityVideosBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(
            binding.getRoot(),
            OnApplyWindowInsetsListener { v:View?,insets:WindowInsetsCompat? ->
                val systemBarsInsets = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(
                    systemBarsInsets.left,
                    systemBarsInsets.top,
                    systemBarsInsets.right,
                    systemBarsInsets.bottom)
                insets
            })
//        this.enableEdgeToEdge()
        initToolbar()
        binding.myRecyclerView.setHasFixedSize(true)
        var mLayoutManager = LinearLayoutManager(this@VideosActivity)
        binding.myRecyclerView.layoutManager = mLayoutManager
        Glide.with(this@VideosActivity).load(R.drawable.loader_white).into(binding.ivLoader)
        binding.myRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this@VideosActivity)
        binding.myRecyclerView.layoutManager = mLayoutManager
        if(Constants.isConnectionAvailable(this)) {
            getVideos(1)
        }
        else {
            noInternetConnection(binding.clParent)
        }
        mAdapter = VideoListAdapter(this@VideosActivity, binding.myRecyclerView)
        binding.myRecyclerView.layoutManager = LinearLayoutManager(this@VideosActivity,LinearLayoutManager.VERTICAL,false)
        binding.myRecyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener { position:Int,_:View? -> getVideoDetails(mAdapter.list[position].id.videoId) }
        binding.btnSubscribe.setOnClickListener { callSubscribe() }
        mAdapter.totPages = 1000
        mAdapter.setOnLoadMoreListener { page: Int -> getVideos(page) }
        readData(intent.extras)
    }
    private fun readData(bundle:Bundle?) {
        if(bundle != null) {
            val videoId = bundle.getString("videoId")
            videoId?.let { getVideoDetails(it) }
        }
    }
    private fun callSubscribe() {
        startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/channel/" + getString(R.string.youtubeChannelID))))
    }
    private fun initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar)
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding.toolbars.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.toolbars.toolbar.title = getString(R.string.videos)
        supportActionBar?.title =  getString(R.string.videos)
    }
    @SuppressLint("CheckResult")
    private fun getVideos(page: Int) {
        if (page == 1) binding.llProgress.visibility = View.VISIBLE
        khetiAPIs.getYouTubeVideoList(getString(R.string.youtubeChannelID),getString(R.string.google_api_key), nextPageToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object:DisposableSingleObserver<VideoListResp?>() {
                override fun onSuccess(videoResp:VideoListResp?) {
                    if(page == 1) binding.llProgress.visibility = View.GONE
                    if(videoResp?.items?.size?.rem(10) !=0) {
                       // binding.llProgress.visibility = View.GONE
                        //return
                    }
                    if(videoResp != null && videoResp.nextPageToken!=null && !videoResp.nextPageToken.equals(null)) {
                        nextPageToken = videoResp.nextPageToken
                    }
                    if(videoResp != null) {
                        println("vbvcbvbcvbv===="+page+"=="+videoResp.items.size+"=="+ videoResp.items[0].id)
                        mAdapter.addItems(videoResp.items,page)
                    }
                }
                override fun onError(e:Throwable) {
                    Timber.e(e.message,"onFailure: ")
                   binding.llProgress.visibility = View.GONE
                }
            })
    }
    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
    private fun getVideoDetails(id:String) {
        disposable.add(khetiAPIs.getYouTubeVideo(id,getString(R.string.google_api_key))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<VideoResp>() {
                override fun onSuccess(t: VideoResp) {
                    callVideoPlayer(convertIntoListItem(t.items[0]))
                }
                override fun onError(e:Throwable) {
                    Timber.e(e)
                }
            }))
    }
    private fun convertIntoListItem(item:Item?):ListItem {
        val id = Id()
        id.videoId = item!!.id
        val listItem = ListItem(id)
        listItem.kind = item.kind
        listItem.etag = item.etag
        listItem.snippet = item.snippet
        listItem.contentDetails = item.contentDetails
        return listItem
    }
    private fun callVideoPlayer(item:ListItem) {
        val intent = Intent(this@VideosActivity,YouTubePlayerActivity::class.java)
        intent.putExtra("video",item)
        intent.putExtra("force_fullscreen",true)
        startActivity(intent)
    }
}