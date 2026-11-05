package com.khetigaadi.user.profile
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.ActionBar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import com.khetigaadi.base.BaseActivity
import com.khetigaadi.databinding.ActivityProfilrSettingBinding
//import com.khetigaadi.user.SessionManager
//import com.khetigaadi.user.model.User
import com.khetigaadi.user.retrofit.UserAPIs
import com.khetigaadi.BuildConfig
import com.khetigaadi.MainActivity
import com.khetigaadi.R
//import com.khetigaadi.about.AmbWebViewActivity
import com.khetigaadi.base.WebViewActivity
import com.khetigaadi.base.util.StateData
//import com.khetigaadi.commons.WebViewActivity
import com.khetigaadi.commons.activities.BaseActivity
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.databinding.ActivityProfileOtherBinding
import com.khetigaadi.databinding.ActivityProfileSettingsBinding
import com.khetigaadi.ecom.AddressListActivity
import com.khetigaadi.ecom.order.MyOrderListActivity
import com.khetigaadi.language.SelectLanguage
import com.khetigaadi.pick_image.CropParam
import com.khetigaadi.pick_image.GalleryImagesActivity
//import com.khetigaadi.model.UpdateProfileImageResp
//import com.khetigaadi.retrofit.UserAPIs
import com.khetigaadi.user.LoginActivity
import com.khetigaadi.user.SessionManager
import com.khetigaadi.user.model.UpdateProfileImageResp
//import com.khetigaadi.user.model.Brand
//import com.khetigaadi.user.model.Crop
import com.khetigaadi.user.model.User
import com.khetigaadi.user.model.UserType
//import com.khetigaadi.util.StateData
import com.khetigaadi.util.Utils
//import com.khetigaadi.util.pick_image.CropParam
//import com.khetigaadi.util.pick_image.GalleryImagesActivity
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder
import java.util.Objects
import javax.inject.Inject
@AndroidEntryPoint
class ProfilrSettingActivity: BaseActivity() {
    var binding: ActivityProfilrSettingBinding? = null
    @Inject
    lateinit var userAPIs: UserAPIs
    @Suppress("PROPERTY_HIDES_JAVA_FIELD")
    @Inject
    lateinit var session: SessionManager
//    private var selectedCrops:ArrayList<Crop?>? = null
//    private var selectedBrand:Brand? = null
//    private var selectedUserType:UserType? = null
    private var user: User? = null
    private var profileImage:Uri? = null
    private var isProfileImageChange = false
    private var isCompulsory = false
    private var viewModel:EditProfileViewModel? = null
    private var doubleBackToExitPressedOnce = false
    var galleryActivityResultLauncher:ActivityResultLauncher<Intent?> = registerForActivityResult<Intent?,ActivityResult?>(
        StartActivityForResult() as ActivityResultContract<Intent?,ActivityResult?>,
        ActivityResultCallback { result: ActivityResult? ->
            if(result!!.resultCode == RESULT_OK) {
                val data = result.data
                if(data != null) {
                    handleCropResult(data.getData())
                }
            }
        })
    override fun onActivityResult(requestCode:Int,resultCode:Int,data:Intent?) {
        super.onActivityResult(requestCode,resultCode,data)
        when(requestCode) {

        }
    }
    private fun handleCropResult(resultUri:Uri?) {
        if(resultUri != null) {
            isProfileImageChange = true
            profileImage = resultUri
            Glide.with(this)
                .load(resultUri)
                .error(R.drawable.camera)
                .into(binding!!.ivImage)
            uploadProfileImage()
        }
        else {
            Toast.makeText(this@ProfilrSettingActivity,R.string.toast_cannot_retrieve_cropped_image,Toast.LENGTH_SHORT).show()
        }
    }
//    private fun getSelectedCropsString(selectedCrops: MutableList<Crop?>): String {
//        try {
//            val cropName = StringBuilder()
//            for (i in selectedCrops.indices) {
//                cropName.append(selectedCrops.get(i)!!.getName())
//                if (i != selectedCrops.size - 1) {
//                    cropName.append(", ")
//                }
//            }
//            return cropName.toString()
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//        return ""
//    }
    private fun uploadProfileImage() {
        viewModel?.uploadProfileImage(profileImage)?.observe(this,
            Observer { updateProfileImageRespStateData: StateData<UpdateProfileImageResp?>? ->
                when(updateProfileImageRespStateData!!.getStatus()) {
                    StateData.Status.LOADING -> showOrUpdateProgressDialog("Wait image uploading!")
                    StateData.Status.SUCCESS -> {
                        if(updateProfileImageRespStateData.getData() != null) {
                            Toast.makeText(applicationContext,updateProfileImageRespStateData.getData()!!.msg[0],Toast.LENGTH_SHORT).show()
                        }
                        user?.image = updateProfileImageRespStateData.getData()!!.updateProfileImage.imageName
                        session.setUser(user)
                        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(Constants.PROFILE_UPDATED))
                        if(isCompulsory) {
                            startActivity(Intent(this@ProfilrSettingActivity,MainActivity::class.java))
                        }
                        // Toast.makeText(this@ProfileSettingsActivity,resources.getString(R.string.success),Toast.LENGTH_SHORT).show()
                        // finish()
                        dismissProgressDialog()
                    }
                    StateData.Status.ERROR -> {
                        Toast.makeText(this@ProfilrSettingActivity,updateProfileImageRespStateData.error,Toast.LENGTH_SHORT).show()
                        dismissProgressDialog()
                    }
                    StateData.Status.CREATED -> {}
                }
            })
    }
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ActivityProfilrSettingBinding.inflate(layoutInflater)
        val view: View = binding!!.getRoot()
        setContentView(view)
        viewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]
        //setContentView(R.layout.activity_profile_settings)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v,insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left,systemBars.top,systemBars.right,systemBars.bottom)
            insets
        }
        user = session.getUser()
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
        const val USER_TYPE_SELECTION_REQUEST = 77
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
            appRate()
        }
        binding?.tvChangeLanguage?.setOnClickListener {
            startActivity(Intent(this,SelectLanguage::class.java))
            finish()
        }
        binding?.tvLogout?.setOnClickListener {
            logout()
        }
        binding?.tvEditProfile?.setOnClickListener {
            startActivity(Intent(this@ProfilrSettingActivity,EditProfileActivity::class.java))
        }
        binding!!.tvDeleteAccount.paintFlags = binding!!.tvDeleteAccount.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding!!.tvDeleteAccount.setOnClickListener { v ->
            val intent = Intent(this@ProfilrSettingActivity, WebViewActivity::class.java)
            intent.putExtra(WebViewActivity.TITLE,"Terms and Conditions")
            intent.putExtra(WebViewActivity.URL,"https://khetigaadi.com/delete-user/user/en")
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
            onWhatsAppClick()
        }
        binding!!.tvBlockedUsers.setOnClickListener {
           // val intent = Intent(this@ProfilrSettingActivity,BlockedUserListActivity::class.java)
            startActivity(intent)
        }

        binding!!.ivImage.setOnClickListener({ v ->
            val cropParams = CropParam()
            cropParams.cropImageName = System.currentTimeMillis().toString()+".png"
            cropParams.rounded = true
            val intent = Intent(this@ProfilrSettingActivity, GalleryImagesActivity::class.java)
            intent.putExtra("cropParams",cropParams)
            galleryActivityResultLauncher.launch(intent)
        })
        binding!!.ivEditImage.setOnClickListener { v -> binding!!.ivImage.callOnClick() }

        if(user!!.image != null) try {
            Glide.with(this@ProfilrSettingActivity)
                .load(Utils.decodeURL(session.getUserOthers().userPath+user!!.image))
                .error(R.drawable.ic_user_profile)
                .placeholder(R.drawable.ic_user_profile)
                .into(binding!!.ivImage)
        }
        catch(e:java.lang.Exception) {
            e.printStackTrace()
        }

        binding!!.tvAppVerision.text = resources.getString(R.string.version)+": ${BuildConfig.VERSION_NAME}"

        binding!!.tvAboutUs.setOnClickListener {
            val intent = Intent(this@ProfilrSettingActivity,AmbWebViewActivity::class.java)
            intent.putExtra("page",1)
            startActivity(intent)
        }

        binding!!.tvContactus.setOnClickListener {
            val intent = Intent(this@ProfilrSettingActivity,AmbWebViewActivity::class.java)
            intent.putExtra("page",2)
            startActivity(intent)
        }

        binding!!.tvPrivacypolicy.setOnClickListener {
            val intent = Intent(this@ProfilrSettingActivity,AmbWebViewActivity::class.java)
            intent.putExtra("page",3)
            startActivity(intent)
        }

        binding!!.tvReturnpolicy.setOnClickListener {
            val intent = Intent(this@ProfilrSettingActivity,AmbWebViewActivity::class.java)
            intent.putExtra("page",4)
            startActivity(intent)
        }
        binding!!.tvMyAddress.setOnClickListener {
            val intent = Intent(this@ProfilrSettingActivity,AddressListActivity::class.java)
            startActivity(intent)
        }
        binding!!.tvMyorders.setOnClickListener {
            val intent = Intent(this@ProfilrSettingActivity,MyOrderListActivity::class.java)
            startActivity(intent)
        }
        binding!!.tvChatWithExpert.setOnClickListener {
            onWhatsAppClick()
        }
    }
    protected fun onWhatsAppClick() {
        try {
            val packageManager = packageManager
            val i = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone="+getString(R.string.expert_mobile_number)+"&text=" + URLEncoder.encode("Hello "+getString(
                R.string.app_name
            ),"UTF-8")
            //            i.setPackage("com.whatsapp");
            i.data = Uri.parse(url)
            if(i.resolveActivity(packageManager) != null) {
                this.startActivity(i)
            }
        }
        catch(e:Exception) {
            e.printStackTrace()
        }
    }
    private fun appRate() {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("market://details?id=$packageName")
            startActivity(i)
        }
        catch(e:ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(GOOGLE_PLAY_URL + packageName)))
        }
    }
    private fun navigateToSocialMedia(pageNo:Int) {
        val intent = Intent(this@ProfilrSettingActivity,SocialMedia::class.java)
        intent.putExtra("page", pageNo)
        // startActivity(intent)

        val socialMediaUrl = ArrayList<String?>()
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
        //session?.logout()
        logoutUser()
        startActivity(Intent(this@ProfilrSettingActivity,LoginActivity::class.java))
        finish()
    }
    private fun logOutFCM() {
//        userAPIs.deregisterPushToken()?.enqueue(object:Callback<MyResponse?> {
//            override fun onResponse(call:Call<MyResponse?>,response:Response<MyResponse?>) {
//                // Implementation here
//            }
//            override fun onFailure(call:Call<MyResponse?>,t:Throwable) {
//                // Implementation here
//            }
//        })
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
    //CROP_SELECTION_REQUEST
}