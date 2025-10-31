package com.khetigaadi.news.activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.khetigaadi.commons.activities.BaseActivity
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.commons.utils.News
import com.khetigaadi.commons.utils.NewsDetailResp
import com.khetigaadi.databinding.ActivityNewsDetailsBinding
import com.khetigaadi.news.adapter.CommentListAdapter
import com.khetigaadi.news.adapter.CommentListAdapter.CommentListClickListener
import com.khetigaadi.news.model.Comment
import com.khetigaadi.news.model.CommentResp
import com.khetigaadi.user.SessionManager
import com.khetigaadi.user.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.inject.Inject
@AndroidEntryPoint
class NewsDetailsActivity:BaseActivity() {
    private var newsId:String? = null
    @JvmField
    @Inject
    var mTracker:Tracker? = null
    private var mAdapter:CommentListAdapter? = null
    @Inject
    lateinit var sessionManager:SessionManager
    private lateinit var binding:ActivityNewsDetailsBinding
    private lateinit var viewModel:NewsDetailsViewModel
    private var news:News? = null
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        println("vcbcvbcvbc====")
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false)
        binding = ActivityNewsDetailsBinding.inflate(layoutInflater)
        val view:View = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(
            view,
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBarsInsets: Insets =
                    insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(
                    systemBarsInsets.left,
                    systemBarsInsets.top,
                    systemBarsInsets.right,
                    systemBarsInsets.bottom)
                insets
            })
        this.enableEdgeToEdge()
        viewModel = ViewModelProvider(this).get(NewsDetailsViewModel::class.java)
        initToolbar()
        readData(intent.extras)
        readDeepLinkData()
        Glide.with(this@NewsDetailsActivity)
            .load(R.drawable.loader_white)
            .into(binding.llProgresss.ivLoader)
        getNewDetails()
        initView()
        initCommentAdapter()
    }
    private fun initView() {
        binding.coParent.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            binding.coParent.getWindowVisibleDisplayFrame(r)
            val screenHeight = binding.coParent.rootView.height
            // r.bottom is the position above soft keypad or device button.
            // if keypad is shown, the r.bottom is smaller than that before.
            val keypadHeight = screenHeight - r.bottom
            Timber.d("keypadHeight = %s", keypadHeight)
            if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                // keyboard is opened
                requestFocus(binding.etComment)
            } else {
                // keyboard is closed
                binding.etComment.clearFocus()
            }
        }
        binding.etComment.onFocusChangeListener = View.OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus) {
                binding.tvComments.visibility = View.GONE
                binding.tvLike.visibility = View.GONE
                binding.tvShare.visibility = View.GONE
                binding.ivSend.visibility = View.VISIBLE
            } else {
                binding.tvComments.visibility = View.VISIBLE
                binding.tvLike.visibility = View.VISIBLE
                binding.tvShare.visibility = View.VISIBLE
                binding.ivSend.visibility = View.GONE
            }
        }
        binding.etComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 3) {
                    binding.ivSend.isClickable = true
                    binding.ivSend.setColorFilter(ContextCompat.getColor(this@NewsDetailsActivity, R.color.colorPrimaryDark))
                } else {
                    binding.ivSend.isClickable = false
                    binding.ivSend.setColorFilter(ContextCompat.getColor(this@NewsDetailsActivity, R.color.grey_line))
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.ivSend.setOnClickListener { addComment(binding.etComment.text.toString()) }
        binding.tvLike.setOnClickListener { callLikeNewsApi(newsId) }
        binding.tvComments.setOnClickListener { NewsCommentListActivity.startActivity(this@NewsDetailsActivity, newsId) }
        binding.tvShare.setOnClickListener { shareNews() }
    }
    private fun callLikeCommentApi(id:String,position:Int) {
        viewModel.callLikeCommentApi(id).observe(this) { commentResp: StateData<MyResponse?> ->
            when (commentResp.status) {
                StateData.Status.LOADING -> {}
                StateData.Status.ERROR -> Toast.makeText(this, commentResp.error, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }
    private fun callLikeNewsApi(id:String?) {
        viewModel.callLikeNewsApi(id).observe(this) { newsLikeStateData: StateData<MyResponse?> ->
            when (newsLikeStateData.status) {
                StateData.Status.LOADING -> {}
                StateData.Status.ERROR -> Toast.makeText(this@NewsDetailsActivity, newsLikeStateData.error, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }
    private fun addComment(content:String) {
        viewModel.addComment(newsId,content).observe(this) {
            commentRespStateData:StateData<CommentResp?> ->
            when(commentRespStateData.status) {
                StateData.Status.LOADING -> {
                    binding.ivSend.isEnabled = false
                    showOrUpdateProgressDialog(getString(R.string.loading))
                }
                StateData.Status.SUCCESS -> {
                    binding.etComment.setText("")
                    if (mAdapter != null) {
                        binding.llComments.visibility = View.VISIBLE
                        if (commentRespStateData.data != null) {
                            mAdapter!!.addItem(0, commentRespStateData.data!!.comment)
                            binding.rvComments.scrollToPosition(0)
                        }
                    }
                    dismissProgressDialog()
                    binding.ivSend.isEnabled = true
                }
                StateData.Status.ERROR -> {
                    Toast.makeText(this@NewsDetailsActivity, commentRespStateData.error, Toast.LENGTH_SHORT).show()
                    dismissProgressDialog()
                    binding.ivSend.isEnabled = true
                }
                else -> {}
            }
        }
    }
    override fun onCreateOptionsMenu(menu:Menu):Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_news, menu)
        return true
    }
    override fun onOptionsItemSelected(item:MenuItem):Boolean {
        if (item.itemId == R.id.menu_share) {
            shareNews()
            return true
        }
        return false
    }
    private fun initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar)
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding.toolbars.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.toolbars.toolbar.title = getString(R.string.news)
    }
    private fun readDeepLinkData() {
        if (intent.action != null && intent.action.equals(Intent.ACTION_VIEW, ignoreCase = true)) {
            try {
                newsId = intent.data.toString().split("news/".toRegex()).toTypedArray()[1]
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun readData(bundle: Bundle?) {
        if (bundle != null) {
            newsId = bundle.getString("news_id")
        }
    }
    // (News) newsDetailsResp.getNews();
    //Google analytic code
    //                    getCommentList();
    private fun getNewDetails() {
        viewModel.newsDetails.observe(this) { newsDetailRespStateData: StateData<NewsDetailResp?> ->
           println("vcbvcbcvbcvbvcdfsg===="+newsDetailRespStateData.status);
            when (newsDetailRespStateData.status) {
                StateData.Status.LOADING -> {
                    binding.llComments.visibility = View.GONE
                    binding.llProgresss.llProgress.visibility = View.VISIBLE
                }
                StateData.Status.SUCCESS -> {
                    if (newsDetailRespStateData.data != null) {
                        news = newsDetailRespStateData.data!!.news
                    }
                    // (News) newsDetailsResp.getNews();
                    //Google analytic code
                    Timber.i("Setting screen name: %s", TAG)
                    mTracker!!.setScreenName(TAG + " " + news!!.postTitle)
                    mTracker!!.send(ScreenViewBuilder().build())
                    binding.tvNewsHeading.text = news!!.postTitle
                    if (news!!.postContent != null) {
                        binding.description.settings.javaScriptEnabled = true
                        binding.description.visibility = View.VISIBLE
                        if (news!!.postContent != null) {
                            val html = "<html><body>" + news!!.postContent + "</body></html>"
                            binding.description.loadDataWithBaseURL("", html, "text/html", "UTF-8", "")
                        }
                    }
                    if (news!!.image != null) {
                        Glide.with(this@NewsDetailsActivity).load(Constants.decodeURL(news!!.image.image))
                            .placeholder(R.mipmap.no_image)
                            .error(R.mipmap.no_image)
                            .into(binding.ivImage)
                        binding.ivImage.visibility = View.VISIBLE
                    } else binding.ivImage.visibility = View.GONE
                    if (news!!.postDate != null) {
                       // binding.tvDate.text = DateUtils.convertDateToDate(news!!.postDate, DateUtils.DateFormat.yyyy_MM_dd_HH_mm_ss, DateUtils.DateFormat.MMM_dd_yyyy)


                        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val dateInString = news!!.postDate
                        val formatterOut = SimpleDateFormat("MMM-dd-yyyy")
                        try {
                            val date = formatter.parse(dateInString)
                            println(date)
                            assert(date != null)
                            val dtt = formatterOut.format(date)
                            // binding.tvRegisteredOn.setText(String.format("%s %s", getString(R.string.registered_on),dtt));
                            binding.tvDate.text = dtt
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    }
                    //                    getCommentList();
                    binding.llProgresss.llProgress.visibility = View.GONE
                }
                StateData.Status.ERROR -> {
                    binding.llProgresss.llProgress.visibility = View.GONE
                    Toast.makeText(this@NewsDetailsActivity, newsDetailRespStateData.error, Toast.LENGTH_LONG).show()
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
    private val commentList: Unit
    private get() {
            viewModel.getComments(newsId).observe(this) { commentListRespStateData: StateData<CommentListResp?> ->
                when (commentListRespStateData.status) {
                    StateData.Status.LOADING -> {
                        binding.llProgresss.llProgress.visibility = View.VISIBLE
                        binding.llComments.visibility = View.GONE
                    }
                    StateData.Status.SUCCESS -> {
                        if (commentListRespStateData.data != null) {
                            if (commentListRespStateData.data!!.data.comments != null && commentListRespStateData.data!!.data.comments.size != 0) {
                                if (mAdapter != null) mAdapter!!.addItems(commentListRespStateData.data!!.data.comments)
                                binding.llComments.visibility = View.VISIBLE
                            } else {
                                binding.llComments.visibility = View.GONE
                            }
                        }
                        binding.llProgresss.llProgress.visibility = View.GONE
                    }
                    StateData.Status.ERROR -> {
                        binding.llProgresss.llProgress.visibility = View.GONE
                        Toast.makeText(this, commentListRespStateData.error, Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                    else -> {}
                }
            }
        }
    private fun initCommentAdapter() {
        mAdapter = CommentListAdapter(this@NewsDetailsActivity, binding.rvComments, sessionManager)
        binding.rvComments.layoutManager = LinearLayoutManager(this@NewsDetailsActivity, LinearLayoutManager.VERTICAL, false)
        binding.rvComments.adapter = mAdapter
        mAdapter!!.setClickListener(object : CommentListClickListener {
            override fun onItemClick(post: Comment, adapterPosition: Int, v: View) {}
            override fun onLikeClick(post: Comment, adapterPosition: Int, v: View) {
//                callLikeCommentApi(post.getCommentId() + "", adapterPosition);
            }
            override fun onProfileClik(post: Comment, adapterPosition: Int, v: View) {
                if (post.user != null) {
                    val intent = Intent(this@NewsDetailsActivity, ProfileActivity::class.java)
                    intent.putExtra("userId", post.user.id)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@NewsDetailsActivity, "Data not available", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onReplyClick(comment: Comment, adapterPosition: Int, v: View) {
                NewsCommentReplyListActivity.startActivity(this@NewsDetailsActivity, newsId, comment)
            }
        })
    }
    private fun shareNews() {
        Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://www.khetigaadi.com/news/$newsId")
            domainUriPrefix = getString(R.string.domain_uri_prefix)
            androidParameters { }
        }.addOnSuccessListener { (shortLink, _) ->
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, news?.postTitle)
            intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.i_was_viewing) + " " + resources.getString(R.string.news) + "\" " + shortLink)
            try {
                startActivity(Intent.createChooser(intent, "Select an action"))
            } catch (ex: ActivityNotFoundException) {
                // (handle error)
            }
        }.addOnFailureListener {
            Timber.e(it)
        }
    }
    companion object {
        private const val TAG = "NewsDetailsActivity"
    }
}