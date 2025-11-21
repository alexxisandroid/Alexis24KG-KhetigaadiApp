package com.khetigaadi.commons.activities
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.firebase.dynamiclinks.ktx.component2
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.khetigaadi.base.model.youtube.ListItem
import com.khetigaadi.databinding.ActivityYouTubePlayerBinding
import com.khetigaadi.youtube.YouTubePlayerViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat

//val String?.videoId: Any
//    get() { return ListItem().id.videoId }
@AndroidEntryPoint
class YouTubePlayerActivity:BaseActivity() {
    private lateinit var viewModel:YouTubePlayerViewModel
    private var item:ListItem? = null
    private lateinit var binding:ActivityYouTubePlayerBinding
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false)
        binding = ActivityYouTubePlayerBinding.inflate(layoutInflater)
        val view:View = binding.root
        setContentView(view)
//        ViewCompat.setOnApplyWindowInsetsListener(
//            binding.getRoot(),
//            OnApplyWindowInsetsListener { v:View?,insets:WindowInsetsCompat? ->
//                val systemBarsInsets = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
//                v!!.setPadding(
//                    systemBarsInsets.left,
//                    systemBarsInsets.top,
//                    systemBarsInsets.right,
//                    systemBarsInsets.bottom)
//                insets
//            })
//        this.enableEdgeToEdge()
        viewModel = ViewModelProvider(this)[YouTubePlayerViewModel::class.java]
        item = intent.extras?.getParcelable("video")
        init()
        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object:AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer:YouTubePlayer) {
                item?.id?.videoId?.let { youTubePlayer.loadVideo(""+it,viewModel.currentSeconds) }
            }
            override fun onCurrentSecond(youTubePlayer:YouTubePlayer,second:Float) {
                super.onCurrentSecond(youTubePlayer,second)
                viewModel.currentSeconds = second
            }
        })
    }
    private fun init() {
        try {
            println("xvcvcxv==="+item!!.snippet+"==")
            binding.tvName.text = item!!.snippet.title

//            binding.tvDate.text = String.format(
//                "%s %s",
//                getString(com.khetigaadi.R.string.published_on),
//                DateUtils.convertDateToDate(
//                    item!!.snippet.publishedAt,
//                    DateUtils.DateFormat.yyyy_MM_dd_T_HH_mm_ss_Z,
//                    DateUtils.DateFormat.dd_mm_yyyy
//                )
//            )
            // ((ViewHolder) holder).binding.tvDate.setText(DateUtils.convertDateToDate(item.getPostDate(), DateUtils.DateFormat.yyyy_MM_dd_HH_mm_ss, DateUtils.DateFormat.MMM_dd_yyyy));
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateInString: String = item!!.snippet.publishedAt
            val formatterOut = SimpleDateFormat("MM-dd-yyyy")
            try {
                val date = formatter.parse(dateInString)
                println(date)
                assert(date != null)
                val dtt = formatterOut.format(date)
                binding.tvDate.text =String.format( "%s %s",dtt)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            binding.tvDesc.text = item!!.snippet.description
            binding.ivShare.setOnClickListener { shareNews() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun shareNews() {
        Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://www.khetigaadi.com/video/${item?.id}")
            domainUriPrefix = getString(com.khetigaadi.R.string.domain_uri_prefix)
            androidParameters { }
        }.addOnSuccessListener { (shortLink, _) ->
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, item!!.snippet.title)
            intent.putExtra(
                Intent.EXTRA_TEXT,
                resources.getString(com.khetigaadi.R.string.i_was_viewing) + " " + resources.getString(
                    com.khetigaadi.R.string.video
                ) + "\" " + shortLink
            )
            try {
                startActivity(Intent.createChooser(intent, "Select an action"))
            } catch (ex: ActivityNotFoundException) {
                // (handle error)
            }
        }.addOnFailureListener {
            Timber.e(it)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release()
    }
    companion object {
        private val TAG = YouTubePlayerActivity::class.java.simpleName
    }
}