package com.khetigaadi.user.profile
//import android.content.ActivityNotFoundException
//import android.content.Intent
//import android.graphics.Paint
//import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.khetigaadi.R
//import com.khetigaadi.base.WebViewActivity
//import com.khetigaadi.commons.activities.FeedBackActivity
//import com.khetigaadi.databinding.ActivityMainBinding
import com.khetigaadi.databinding.ActivityProfileOtherBinding
//import com.khetigaadi.isOwner.IsOwnerTractorBrand
//import com.khetigaadi.language.SelectLanguage
import com.khetigaadi.user.UserDashboardFragment
import com.khetigaadi.user.buyserEnquiry.BuyersEnquiry
import com.khetigaadi.user.myAddedTractor.MyAddedTractorList
import com.khetigaadi.user.myRentTractor.MyUploadedRentTractorList
import com.khetigaadi.user.myUsedTractor.MyUploadedUsedTractorList
import com.khetigaadi.user.myfavorites.MyFavoritesTabs
import dagger.hilt.android.AndroidEntryPoint
import java.util.Objects
@Suppress("NULLABILITY_MISMATCH_BASED_ON_EXPLICIT_TYPE_ARGUMENTS_FOR_JAVA")
@AndroidEntryPoint
class ProfileOtherActivity:AppCompatActivity() {
    private lateinit var binding:ActivityProfileOtherBinding
    private val TAG = javaClass.simpleName
    private lateinit var mFragmentManager:FragmentManager
    private lateinit var mFragmentTransaction:FragmentTransaction
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileOtherBinding.inflate(layoutInflater)
        // listDataChild =  HashMap<ExpandedMenuModel,List<ExpandedSubMenu>>();
        val view:View = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v,insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left,systemBars.top,systemBars.right,systemBars.bottom)
            insets
        }
        fragmentTransaction
        initToolbar()
        initUI()
    }
    private val fragmentTransaction:FragmentTransaction
        get() {
            mFragmentManager = supportFragmentManager
            mFragmentTransaction = mFragmentManager.beginTransaction()
            mFragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
            return mFragmentTransaction
        }
    private fun initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar)
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding.toolbars.toolbar.setNavigationOnClickListener { finish() }
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        Objects.requireNonNull<ActionBar?>(supportActionBar).setDisplayShowTitleEnabled(true)
        supportActionBar?.title = resources.getString(R.string.profile)
    }
    companion object {
        private const val GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id="
    }
    private fun initUI() {
        println(GOOGLE_PLAY_URL)
      val typeV = intent.getStringExtra("typeVal")
       if(typeV == "dashboard") {
           supportActionBar?.title = resources.getString(R.string.dashboard)
           fragmentTransaction.replace(binding.containerView.id,UserDashboardFragment()).addToBackStack(TAG).commit()
       }
        else if(typeV == "my_old_tractor") {
           supportActionBar?.title = resources.getString(R.string.my_old_tractors)
           val fragment = MyUploadedUsedTractorList()
           val bundle = Bundle()
           bundle.putString("filter", "all")
           fragment.arguments = bundle
           fragmentTransaction.replace(R.id.containerView,fragment).addToBackStack(TAG).commit()
       }
        else if(typeV == "my_rent_tractor") {
           supportActionBar?.title = resources.getString(R.string.my_rent_tractors)
           fragmentTransaction.replace(binding.containerView.id,MyUploadedRentTractorList()).addToBackStack(TAG).commit()
       }
        else if(typeV == "buy_enq") {
           supportActionBar?.title = resources.getString(R.string.buy_enquiry)
           fragmentTransaction.replace(binding.containerView.id,BuyersEnquiry())
               .addToBackStack(TAG).commit()
       }
        else if(typeV == "my_fav") {
           supportActionBar?.title = resources.getString(R.string.my_favorites)
           fragmentTransaction.replace(binding.containerView.id,MyFavoritesTabs())
               .addToBackStack(TAG).commit()
       }
        else if(typeV == "my_tract") {
            println("nbcvcvb2134")
           supportActionBar?.title = resources.getString(R.string.my_added_tractors)
           fragmentTransaction.replace(binding.containerView.id,MyAddedTractorList())
               .addToBackStack(TAG).commit()
       }
    }
    fun setToolbarTitle(tittlee:String) {
        supportActionBar?.title =tittlee
    }
    fun noInternetConnection() {
      //  noInternetConnection(binding.containerView)
    }
//    private fun hideSoftKeyboardNew1() {
//        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        if (imm.isAcceptingText) imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0)
//    }
    fun hideSoftKeyboard() {
        val view = this.currentFocus
        if(view != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
        }
    }
}