package com.khetigaadi.user.profile
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.khetigaadi.MainActivity.Companion.onWhatsAppClick
import com.khetigaadi.R
import com.khetigaadi.base.WebViewActivity
import com.khetigaadi.base.model.MyResponse
import com.khetigaadi.commons.activities.FeedBackActivity
import com.khetigaadi.commons.activities.VideosActivity
import com.khetigaadi.databinding.ActivityProfileSettingsBinding
import com.khetigaadi.isOwner.IsOwnerTractorBrand
import com.khetigaadi.language.SelectLanguage
import com.khetigaadi.social.SocialMedia
import com.khetigaadi.user.LoginActivity
import com.khetigaadi.user.SessionManager
import com.khetigaadi.user.retrofit.UserAPIs
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Objects
import javax.inject.Inject
@AndroidEntryPoint
class ProfileSettingsActivity:AppCompatActivity() {
    var binding:ActivityProfileSettingsBinding? = null
    @Inject
    lateinit var userAPIs:UserAPIs
    @Suppress("PROPERTY_HIDES_JAVA_FIELD")
    @Inject
    lateinit var session:SessionManager
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        val view: View = binding!!.getRoot()
        setContentView(view)
        //setContentView(R.layout.activity_profile_settings)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v,insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left,systemBars.top,systemBars.right,systemBars.bottom)
            insets
        }
        initToolbar()
        initUI()
    }
    private fun initToolbar() {
        setSupportActionBar(binding?.toolbars?.toolbar)
        binding?.toolbars?.toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding?.toolbars?.toolbar?.setNavigationOnClickListener { v -> onBackPressed() }
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        Objects.requireNonNull<ActionBar?>(supportActionBar).setDisplayShowTitleEnabled(true)
        supportActionBar?.title = resources.getString(R.string.profile_settings)
    }
    companion object {
        private const val GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id="
    }
    private fun initUI() {
        binding?.tvShare?.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_msg))
            try {
                startActivity(Intent.createChooser(intent, "Select an action"))
            }
            catch(ex:ActivityNotFoundException) {
                ex.printStackTrace()
                // (handle error)
            }
        }
        binding?.tvRate?.setOnClickListener {
            try {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("market://details?id=$packageName")
                startActivity(i)
            }
            catch(e:ActivityNotFoundException) {
                e.printStackTrace()
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL + packageName)))
            }
        }
        binding?.tvFeedback?.setOnClickListener {
            startActivity(Intent(this,FeedBackActivity::class.java))
        }
        binding?.tvChangeLanguage?.setOnClickListener {
            startActivity(Intent(this,SelectLanguage::class.java))
            finish()
        }
        binding?.tvLogout?.setOnClickListener {
            logout()
        }
        binding?.tvEditProfile?.setOnClickListener {
            startActivity(Intent(this@ProfileSettingsActivity,EditProfileActivity::class.java))
        }
        binding!!.tvDeleteAccount.paintFlags = binding!!.tvDeleteAccount.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding!!.tvDeleteAccount.setOnClickListener { v ->
            val intent = Intent(this@ProfileSettingsActivity,WebViewActivity::class.java)
            intent.putExtra(WebViewActivity.TITLE,"Terms and Conditions")
            intent.putExtra(WebViewActivity.URL,"https://khetigaadi.com/delete-user/user/en")
            startActivity(intent)
        }
        binding!!.tvAddOwnerTractor.setOnClickListener {
            val intent = Intent(this@ProfileSettingsActivity,IsOwnerTractorBrand::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        binding!!.llSmedd.findViewById<ImageView>(R.id.iv_facebook).setOnClickListener {
            navigateToSocialMedia(1)
        }
        binding!!.llSmedd.findViewById<ImageView>(R.id.iv_instagram).setOnClickListener {
            navigateToSocialMedia(2)
        }
        binding!!.llSmedd.findViewById<ImageView>(R.id.iv_linked_in).setOnClickListener {
            navigateToSocialMedia(3)
        }
        binding!!.llSmedd.findViewById<ImageView>(R.id.ivWhatsApp).setOnClickListener {
            onWhatsAppClick(this)
        }
        binding!!.llSmedd.findViewById<ImageView>(R.id.iv_youtude).setOnClickListener {
            startActivity(Intent(this@ProfileSettingsActivity,VideosActivity::class.java))
        }
    }
    private fun navigateToSocialMedia(pageNo:Int) {
        val intent = Intent(this@ProfileSettingsActivity,SocialMedia::class.java)
        intent.putExtra("page", pageNo)
        // startActivity(intent)

        val socialMediaUrl = java.util.ArrayList<String?>()
        socialMediaUrl.add("https://www.facebook.com/khetigaadi.India/")
        socialMediaUrl.add("https://www.instagram.com/khetigaadi/")
        socialMediaUrl.add("https://www.linkedin.com/in/khetigaadi")

        when(pageNo) {
            1 -> {

                // binding.webView.loadUrl(getArguments().getString(url1));
                val intent = Intent()
                intent.setAction(Intent.ACTION_VIEW)

                // intent.user
                intent.setData(Uri.parse(socialMediaUrl[0]))
                startActivity(intent)
            }
            2 -> {
                val intent = Intent()
                intent.setAction(Intent.ACTION_VIEW)

                // intent.user
                intent.setData(Uri.parse(socialMediaUrl[1]))
                startActivity(intent)
            }
            3 -> {
                val intent = Intent()
                intent.setAction(Intent.ACTION_VIEW)

                // intent.user
                intent.setData(Uri.parse(socialMediaUrl[2]))
                startActivity(intent)
            }
        }
    }
    private fun logoutUser() {
        session?.Logout()
        startActivity(Intent(this@ProfileSettingsActivity,LoginActivity::class.java))
        finish()
    }
    private fun logOutFCM() {
        userAPIs.deregisterPushToken()?.enqueue(object:Callback<MyResponse?> {
            override fun onResponse(call:Call<MyResponse?>,response:Response<MyResponse?>) {
                // Implementation here
            }
            override fun onFailure(call:Call<MyResponse?>,t:Throwable) {
                // Implementation here
            }
        })
    }
    private fun logout() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.are_you_sure_you_want))
            .setNegativeButton(
                getString(R.string.no),
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int -> dialog!!.cancel() })
            .setPositiveButton(
                getString(R.string.yes),
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    dialog!!.dismiss()
                    logOutFCM()
                    logoutUser()
                }).show()
    }
}