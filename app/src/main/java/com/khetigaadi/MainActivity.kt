@file:Suppress("DEPRECATION")
package com.khetigaadi
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.gson.Gson
import com.khetigaadi.BottomMenuListDialogFragment.Companion.newInstance
import com.khetigaadi.base.WebViewActivity
import com.khetigaadi.base.model.AllBannerResp
import com.khetigaadi.base.model.MyResponse
import com.khetigaadi.commons.SharedPreferenceUtils
import com.khetigaadi.commons.activities.BaseActivity
import com.khetigaadi.commons.activities.FeedBackActivity
import com.khetigaadi.commons.activities.OnRoadPriceActivity
import com.khetigaadi.commons.activities.VideosActivity
import com.khetigaadi.commons.fragments.AboutUs
import com.khetigaadi.commons.fragments.AdMobVideoFragment
import com.khetigaadi.commons.fragments.HomeFragment
import com.khetigaadi.commons.fragments.TabCompare
import com.khetigaadi.commons.model.BrandResp
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.dagger.module.TokenAuthenticator
import com.khetigaadi.databinding.ActivityMainBinding
import com.khetigaadi.def.CallFrom
import com.khetigaadi.def.DrawerMenu
import com.khetigaadi.def.DrawerSubMenu
import com.khetigaadi.def.NotificationType
import com.khetigaadi.ecom.EcomHomeActivity
import com.khetigaadi.ecom.ProductDetailsActivity
import com.khetigaadi.harvester.HarvesterDetailsActivity
import com.khetigaadi.harvester.SearchHarvesterFragment
import com.khetigaadi.implement.compare.CompareImp
import com.khetigaadi.implement.compare.CompareImpSelection
import com.khetigaadi.implement.newImplement.NewImpOverviewDetails
import com.khetigaadi.implement.newImplement.NewImplementsList
import com.khetigaadi.implement.newImplement.SearchNewImplementsFilter
import com.khetigaadi.isOwner.IsOwnerTractorBrand
import com.khetigaadi.language.SelectLanguage
import com.khetigaadi.mandi.MandiFragment
import com.khetigaadi.news.activity.NewsDetailsActivity
import com.khetigaadi.news.fragments.NewsListFragment
import com.khetigaadi.notification.NotificationResult
import com.khetigaadi.notification.model.Notification
import com.khetigaadi.retrofit.KhetiAPIs
import com.khetigaadi.room.repository.BrandRepository
import com.khetigaadi.sidemenu.ExpandableListAdapter
import com.khetigaadi.sidemenu.ExpandedMenuModel
import com.khetigaadi.sidemenu.ExpandedSubMenu
import com.khetigaadi.social.SocialMedia
import com.khetigaadi.tractor.dealer.DealerListFragment
import com.khetigaadi.tractor.newTractor.NewTractorDetails
import com.khetigaadi.tractor.newTractor.NewTractorsListFragment
import com.khetigaadi.tractor.newTractor.model.Filter
import com.khetigaadi.tractor.newTractor.search.SearchNewTractorFilter
import com.khetigaadi.tractor.rent.RentTractorDetails
import com.khetigaadi.tractor.rent.RentTractorListFragment
import com.khetigaadi.tractor.sellTractorNewUi.SellTractorActivity
import com.khetigaadi.tractor.serviceCenter.SCenterList
import com.khetigaadi.tractor.usedTractor.UsedTractorDetailActivity
import com.khetigaadi.tractor.usedTractor.UsedTractorsListFragment
import com.khetigaadi.tractor.usedTractor.search.SearchUsedTractorFilter
import com.khetigaadi.user.LoginActivity
import com.khetigaadi.user.SessionManager
import com.khetigaadi.user.UserDashboardFragment
import com.khetigaadi.user.buyserEnquiry.BuyersEnquiry
import com.khetigaadi.user.myRentTractor.MyUploadedRentTractorList
import com.khetigaadi.user.myUsedTractor.MyUploadedUsedTractorList
import com.khetigaadi.user.myfavorites.MyFavoritesTabs
import com.khetigaadi.user.profile.EditProfileActivity
import com.khetigaadi.user.profile.ProfileActivity
import com.khetigaadi.user.retrofit.UserAPIs
import com.khetigaadi.util.Utils
import com.khetigaadi.weather.WeatherNewActivity
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import hotchemi.android.rate.AppRate
import hotchemi.android.rate.StoreType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.net.URLEncoder
import javax.inject.Inject
@AndroidEntryPoint
open class MainActivity:BaseActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var listDataHeader:MutableList<ExpandedMenuModel>
    private var listDataChild:HashMap<ExpandedMenuModel,List<ExpandedSubMenu>>? = null
    private lateinit var mFragmentManager:FragmentManager
    private lateinit var mFragmentTransaction:FragmentTransaction
    private var doubleBackToExitPressedOnce = false
    private var civUserImage:CircleImageView? = null
    private var tvUserName:TextView? = null
    private var tvAddress:TextView? = null
    private var tvUpdateProfile:TextView? = null
    private var tvUserType:TextView? = null
    @Suppress("PROPERTY_HIDES_JAVA_FIELD")
    @Inject
    lateinit var session:SessionManager
    @JvmField
    var allBanner = AllBannerResp()
    @Inject
    lateinit var userAPIs:UserAPIs
    @set:Inject
    internal var brandRepository:BrandRepository? = null
    @Inject
    lateinit var khetiAPIs:KhetiAPIs
    private lateinit var binding:ActivityMainBinding
    private var aLBReceiver:BroadcastReceiver? = null
    private lateinit var appUpdateManager:AppUpdateManager
    private var installStateUpdatedListener:InstallStateUpdatedListener? = null
    private var requestPermissionLauncher:ActivityResultLauncher<String>? = null
    //new update
    // Variables for in-app update functionality
   // private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var activityResultLauncher:ActivityResultLauncher<IntentSenderRequest>
    @Inject
    lateinit var disposable:CompositeDisposable
    @Inject
    lateinit var pref:SharedPreferenceUtils
    @SuppressLint("UseKtx")
    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
       // listDataChild =  HashMap<ExpandedMenuModel,List<ExpandedSubMenu>>();
        val view:View = binding.root
        setContentView(view)
        //WindowCompat.setDecorFitsSystemWindows(window,true)

        enableEdgeToEdge()
        getAllBanner()   //  binding.clll
        ViewCompat.setOnApplyWindowInsetsListener(binding.drawerLayout) { v:View?,insets:WindowInsetsCompat? ->
            val systemBarsInsets:Insets = insets!!.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            v!!.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom)
            insets
        }


       // enableEdgeToEdge()
       // getAllBanner()

        // Apply responsive window insets to sidebar, app bar, and bottom navigation
       // binding.drawerLayout.doOnApplyWindowInsets { _, _, _ -> }

//        binding.appBar.doOnApplyWindowInsets { view, insets, initial ->
//            val systemBars = insets.getInsets(
//                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
//            )
//            view.setPadding(
//                initial.paddingLeft + systemBars.left,
//                initial.marginTop + systemBars.top,
//                initial.right + systemBars.right,
//                initial.bottom
//            )
//        }
//
//        binding.bottomNavigationView.doOnApplyWindowInsets { view, insets, initial ->
//            val systemBars = insets.getInsets(
//                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
//            )
//            view.setPadding(
//                initial.left + systemBars.left,
//                initial.top,
//                initial.right + systemBars.right,
//                initial.bottom + systemBars.bottom
//            )
//        }

        val navView = binding.drawerLayout.findViewById<NavigationView>(R.id.navView)

        //   val navView = binding.drawerLayout.findViewById<NavigationView>(R.id.navView)
//        if(navView != null) {
//            // 1. Sidebar Header View: Add status bar top inset to padding
//            if(navView.headerCount > 0) {
//                val headerView = navView.getHeaderView(0)
//                val initialHeaderState = headerView.getInitialLayoutState()
//                headerView.doOnApplyWindowInsets { view, insets, _ ->
//                    val systemBars = insets.getInsets(
//                        WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
//                    )
//                    view.setPadding(
//                        initialHeaderState.paddingLeft + systemBars.left,
//                        initialHeaderState.paddingTop + systemBars.top,
//                        initialHeaderState.paddingRight,
//                        initialHeaderState.paddingBottom
//                    )
//                }
//            }
//
//            // 2. ExpandableListView (navigationMenu): Push top margin down by status bar inset
//            // so it starts cleanly BELOW headerView without overlapping/cutting it off
//            val navigationMenu = navView.findViewById<ExpandableListView>(R.id.navigationMenu)
//            if(navigationMenu != null) {
//                val initialMenuState = navigationMenu.getInitialLayoutState()
//                navigationMenu.doOnApplyWindowInsets { view, insets, _ ->
//                    val systemBars = insets.getInsets(
//                        WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
//                    )
//                    val params = view.layoutParams as? ViewGroup.MarginLayoutParams
//                    if (params != null) {
//                        params.topMargin = initialMenuState.marginTop + systemBars.top
//                        view.layoutParams = params
//                    }
//                }
//            }
//
//            // 3. Sidebar Footer View: Push bottom margin up by navigation bar inset
//            val includeFooter = navView.findViewById<View>(R.id.includeFooter)
//            if(includeFooter != null) {
//                val initialFooterState = includeFooter.getInitialLayoutState()
//                includeFooter.doOnApplyWindowInsets { view, insets, _ ->
//                    val systemBars = insets.getInsets(
//                        WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
//                    )
//                    val params = view.layoutParams as? ViewGroup.MarginLayoutParams
//                    if (params != null) {
//                        params.bottomMargin = initialFooterState.marginBottom + systemBars.bottom
//                        view.layoutParams = params
//                    }
//                }
//            }
//        }
//



//        if (navView != null) {
//            if (navView.headerCount > 0) {
//                val headerView = navView.getHeaderView(0)
//                headerView.doOnApplyWindowInsets { view, insets, initial ->
//                    val systemBars = insets.getInsets(
//                        WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
//                    )
//                    view.setPadding(
//                        initial.left + systemBars.left,
//                        initial.top + systemBars.top,
//                        initial.right,
//                        initial.bottom
//                    )
//                }
//            }
//
//            val includeFooter = navView.findViewById<View>(R.id.includeFooter)
//            if (includeFooter != null) {
//                includeFooter.doOnApplyWindowInsets { view, insets, initial ->
//                    val systemBars = insets.getInsets(
//                        WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
//                    )
//                    view.setPadding(
//                        initial.left + systemBars.left,
//                        initial.top,
//                        initial.right,
//                        initial.bottom + systemBars.bottom
//                    )
//                }
//            }
//        }




//        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView) { v,insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
//            // Add top + bottom padding so content avoids status/nav bars
//            v.setPadding(v.paddingLeft,systemBars.top,v.paddingRight,systemBars.bottom)
//            // IMPORTANT: return insets UNCONSUMED so other children (like BNView) still get them
//            insets
//        }
//      enableEdgeToEdge()
        findViewById()
        getBrands()
//        requestPermissionLauncher = registerForActivityResult<String, Boolean>(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted: Boolean ->
//            if (isGranted) {
//                // Permission granted
//            } else {
//                // Permission denied
//            }
//        }
        requestPermissionLauncher = registerForActivityResult<String,Boolean>(ActivityResultContracts.RequestPermission())
        { isGranted:Boolean? ->
            if(!isGranted!!) {
                showAlertForNotification()
            }
        }
       // requestPermissionLauncher!!.launch(Manifest.permission.POST_NOTIFICATIONS);
  try {
      @Suppress("DEPRECATION")
      Handler().postDelayed({
          if(session.userLocation == null) {
              showGetCityDialog()
          }
          else {
              notificationPermission(this)
          }
      },3000)
  }
  catch(e:Exception) {
       e.printStackTrace()
  }
        user = session.user
        initListener()
        setupTouchUIToDismissKeyboard(binding.containerView)
        fragmentTransaction
        binding.drawerLayout.findViewById<View>(R.id.includeFooter).findViewById<TextView>(R.id.tvShare).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,resources.getString(R.string.share_msg))
            try {
                startActivity(Intent.createChooser(intent,"Select an action"))
            }
            catch(ex:ActivityNotFoundException) {
                ex.printStackTrace()
                // (handle error)
            }
        }
        binding.drawerLayout.findViewById<View>(R.id.includeFooter).findViewById<LinearLayout>(R.id.llRate).setOnClickListener {
       // binding.includeFooter.tvRate.setOnClickListener {
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
        binding.drawerLayout.findViewById<View>(R.id.includeFooter).findViewById<TextView>(R.id.tvFeedback).setOnClickListener {

       // binding.includeFooter.tvFeedback.setOnClickListener {
      //  getFragmentTransaction().replace(binding.containerView.getId(), new FeedbackFragment()).addToBackStack(TAG).commit();
            startActivity(Intent(this, FeedBackActivity::class.java))
            binding.drawerLayout.closeDrawers()
        }
        initToolbar()
        setupDrawerContent(binding.drawerLayout.findViewById(R.id.navView))
        fragmentTransaction.replace(R.id.containerView,HomeFragment()).commit()
        initDrawer()
        // callback listener.
        AppRate.with(this)
            .setStoreType(StoreType.GOOGLEPLAY) //default is Google, other option is Amazon
            .setInstallDays(2) // default 10, 0 means install day.
            .setLaunchTimes(10) // default 10 times.
            .setRemindInterval(2) // default 1 day.
            .setShowLaterButton(true) // default true.
            .setDebug(false) // default false.
            .setCancelable(true) // default false.
            .setOnClickButtonListener { which:Int -> Timber.d(which.toString()) }
            .setTitle(R.string.rate_dialog_title)
            .setTextLater(R.string.rate_dialog_later)
            .setTextNever(R.string.rate_dialog_never)
            .setTextRateNow(R.string.rate_dialog_ok)
            .monitor()
        AppRate.showRateDialogIfMeetsConditions(this)
        initProfileDetails()
        checkProfileAndForceToUpdate()
        try {
            allBanner = Gson().fromJson(SharedPreferenceUtils.getInstance(this).getStringValue(Constants.APP_BANNER_KEY,"{}"),AllBannerResp::class.java)
        }
        catch(e:Exception) {
            e.printStackTrace()
        }
        handleNotifications()
        aLBReceiver = object:BroadcastReceiver() {
            override fun onReceive(context:Context,intent:Intent) {
                if(intent.extras!!.getString("key") == MyUploadedUsedTractorList.TAG) {
                    val fragment = MyUploadedUsedTractorList()
                    val bundle = Bundle()
                    bundle.putString("filter","all")
                    fragment.arguments = bundle
                    supportFragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.enter,
                        R.anim.exit,
                        R.anim.pop_enter,
                        R.anim.pop_exit).replace(R.id.containerView, fragment).addToBackStack(TAG).commitAllowingStateLoss()
                }
                else if(intent.extras!!.getString("key") == RentTractorListFragment.TAG) {
                    val fragment: Fragment = RentTractorListFragment()
                    supportFragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.enter,
                        R.anim.exit,
                        R.anim.pop_enter,
                        R.anim.pop_exit).replace(R.id.containerView,fragment).addToBackStack(TAG).commitAllowingStateLoss()
                }
                else if(intent.extras?.getString("key")==TokenAuthenticator.TAG) {
                    val i = Intent(this@MainActivity,LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }
                // perform action here.
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(aLBReceiver as BroadcastReceiver,IntentFilter("anEvent"))
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        installStateUpdatedListener = InstallStateUpdatedListener { state:InstallState ->
            if(state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            }
            else if(state.installStatus() == InstallStatus.INSTALLED) {
                removeInstallStateUpdateListener()
            }
        }
        appUpdateManager.registerListener(installStateUpdatedListener!!)
        checkUpdate()
        try {
            val videoList = allBanner.banners.homePageVideo
           // println("cvbcvbcv231===="+videoList.size)
            if(videoList != null && videoList.size != 0)
                showAdVideoDialog()
        }
        catch(e:Exception) {
            println("cvbcvbcv231y===="+e.localizedMessage)
            e.printStackTrace()
        }
        checkForInAppUpdates()


    }
    private fun getAllBanner() {
        if(pref.getStringValue(Constants.APP_BANNER_KEY,"") != "") {
            return
        }
        disposable.add(khetiAPIs.banner
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data:AllBannerResp ->
                println("nvbnvnbvnb22===="+data.isError)
                if(!data.isError) {
                    pref.setValue(Constants.APP_BANNER_KEY,Gson().toJson(data))
                    try {
                        allBanner = Gson().fromJson(SharedPreferenceUtils.getInstance(this).getStringValue(Constants.APP_BANNER_KEY,"{}"),AllBannerResp::class.java)
                    }
                    catch(e:Exception) {
                        e.printStackTrace()
                    }
                }
            })
            { error:Throwable ->
                Timber.e(error.message,"onFailure: ")
            })
    }
//    private fun manualUpdate()
//    {
//        val appPackageName = packageName
//        try {
//            val intent = Intent(Intent.ACTION_VIEW, "market://details?id=$appPackageName".toUri())
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//        }
//        catch(e:ActivityNotFoundException) {
//            val intent = Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=$appPackageName".toUri())
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//        }
//    }
    // new update here
    // Function to check and handle in-app updates
    private fun checkForInAppUpdates()
    {
        // Creates an instance of the AppUpdateManager
        appUpdateManager = AppUpdateManagerFactory.create(this)
        // Registers an activity result launcher to handle the update process result
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
                if(result.resultCode != RESULT_OK) {
                    // If the update fails, show a Toast message and log the failure
                    Toast.makeText(this@MainActivity,"Update failed "+resources.getString(R.string.please_try_after_some_time),Toast.LENGTH_SHORT).show()
                   // Timber.tag("Update").d("Update flow failed! Result code: ${result.resultCode}")
                }
            }
        // Registers a listener to monitor the update installation process
        appUpdateManager.registerListener(listener)
        // Fetches the update information
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            // Checks if an update is available and allowed for flexible updates
           try {
               if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                   appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
               ) {
                   // Requests an in-app update
                   appUpdateManager.startUpdateFlowForResult(
                       appUpdateInfo,
                       activityResultLauncher,
                       AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                   )
               }
           }
           catch(e: Exception) {
               e.printStackTrace()
           }
        }
    }
    // Listener to track the state of the update installation process
    private val listener = InstallStateUpdatedListener { state ->
        // Checks if the update has been downloaded
        if(state.installStatus() == InstallStatus.DOWNLOADED) {
            // Shows a Snackbar prompting the user to restart the app to complete the update
            popupSnackbarForCompleteUpdate()
        }
    }
    // Displays a Snackbar to notify the user that the update is ready to install
    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(binding.root,"Update ready to install",Snackbar.LENGTH_INDEFINITE).apply {
            setAction("Restart") {
                appUpdateManager.completeUpdate()
            }
            show()
        }
    }
    // Unregisters the update listener when the app goes into the background
    override fun onStop() {
        super.onStop()
        appUpdateManager.unregisterListener(listener)
    }
    // Checks if an update was downloaded while the app was inactive and prompts the user
    //end update here
    protected fun notificationPermission(context:Context) {
      try {
          if(!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
              if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                  if(requestPermissionLauncher != null)
                      requestPermissionLauncher!!.launch(android.Manifest.permission.POST_NOTIFICATIONS)
              }
              else {
                  showAlertForNotification()
              }
          }
      }
      catch(e:Exception) {
          e.printStackTrace()
      }
    }
    private fun showAlertForNotification() {
        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setMessage(getResources().getString(R.string.mandatory_permission_access_required))
            .setNegativeButton(
                "Don’t Enable")
            { dialog:DialogInterface,which:Int -> dialog.cancel() }
            .setPositiveButton("Yes, Enable") { dialog:DialogInterface,which:Int ->
                try {
                    val intent = Intent("android.settings.APP_NOTIFICATION_SETTINGS")
                    intent.putExtra("android.provider.extra.APP_PACKAGE",packageName)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                catch(e:java.lang.Exception) {
                    e.printStackTrace()
                }
                dialog.cancel()
            }.show()
    }
    private fun showAdVideoDialog() {
        println("hjnfghfghfhf2133231=====")
        val fm = supportFragmentManager
        val editNameDialogFragment = AdMobVideoFragment.newInstance()
        editNameDialogFragment.isCancelable = false
        editNameDialogFragment.show(fm, AdMobVideoFragment.TAG)
        println("hjnfghfghfhf=====")
    }
    private fun checkUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startUpdateFlow(appUpdateInfo)
               // startImmediateUpdateFlow(appUpdateInfo)
            }
            else if(appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            }
            else if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startImmediateUpdateFlow(appUpdateInfo)
            }
            else if(appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startImmediateUpdateFlow(appUpdateInfo)
            }
        }
    }
    private fun startImmediateUpdateFlow(appUpdateInfo:AppUpdateInfo) {
        try {
            @Suppress("DEPRECATION")
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,this,IMMEDIATE_APP_UPDATE_REQ_CODE)
        }
        catch(e:IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }
    private fun startUpdateFlow(appUpdateInfo:AppUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.FLEXIBLE,this,FLEXIBLE_APP_UPDATE_REQ_CODE)
        }
        catch(e:IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }
    private fun popupSnackBarForCompleteUpdate() {
        binding.cvUpdate.visibility = View.VISIBLE
        binding.containerView.findViewById<CardView>(R.id.cvUpdate).findViewById<ConstraintLayout>(R.id.consLayout).findViewById<MaterialButton>(R.id.btnInstalls).setOnClickListener {
            appUpdateManager.completeUpdate()
            binding.cvUpdate.visibility = View.GONE
        }
    }
    private fun removeInstallStateUpdateListener() {
        appUpdateManager.unregisterListener(installStateUpdatedListener!!)
    }
    //latest app update
     fun initListener() {
        binding.drawerLayout.findViewById<NavigationView>(R.id.navView).findViewById<View>(R.id.includeFooter).findViewById<ImageView>(R.id.iv_facebook).setOnClickListener {
            navigateToSocialMedia(1)
            binding.drawerLayout.closeDrawers()
        }
        binding.drawerLayout.findViewById<NavigationView>(R.id.navView).findViewById<View>(R.id.includeFooter).findViewById<ImageView>(R.id.iv_instagram).setOnClickListener {
            navigateToSocialMedia(2)
            binding.drawerLayout.closeDrawers()
        }
        binding.drawerLayout.findViewById<NavigationView>(R.id.navView).findViewById<View>(R.id.includeFooter).findViewById<ImageView>(R.id.iv_linked_in).setOnClickListener {
            navigateToSocialMedia(3)
            binding.drawerLayout.closeDrawers()
        }
        binding.drawerLayout.findViewById<NavigationView>(R.id.navView).findViewById<View>(R.id.includeFooter).findViewById<ImageView>(R.id.ivWhatsApp).setOnClickListener {
            onWhatsAppClick(this)
            binding.drawerLayout.closeDrawers()
        }
        binding.drawerLayout.findViewById<NavigationView>(R.id.navView).findViewById<View>(R.id.includeFooter).findViewById<ImageView>(R.id.iv_youtude).setOnClickListener {
            startActivity(Intent(this@MainActivity, VideosActivity::class.java))
            binding.drawerLayout.closeDrawers()
        }
        //.findViewById<com.google.android.material.navigation.NavigationView>(R.id.navView).
        binding.drawerLayout.findViewById<CoordinatorLayout>(R.id.clll).findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnNavigationItemSelectedListener { item ->
            val fragmentTransaction = mFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
            if(item.getItemId() === R.id.action_home) {
                navigateToHomeFragment(fragmentTransaction)
                val nvv = binding.drawerLayout.findViewById<CoordinatorLayout>(R.id.clll).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                val itemO:MenuItem = nvv.menu.findItem(R.id.action_old_tractor)
                val itemN: MenuItem = nvv.menu.findItem(R.id.action_new_tractor)
              //  itemO.isChecked = false
               // itemN.isChecked = false
               // val iteme:MenuItem = nvv.menu.findItem(R.id.action_home)
               // iteme.setIcon(R.drawable.ic_home_24dp)
                true
            }
            else if(item.getItemId() === R.id.action_price) {
                val nvv = binding.drawerLayout.findViewById<CoordinatorLayout>(R.id.clll).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                val itemO:MenuItem = nvv.menu.findItem(R.id.action_old_tractor)
                val itemN:MenuItem = nvv.menu.findItem(R.id.action_new_tractor)

               // itemO.isChecked = false
               // itemN.isChecked = false
              //  item.isChecked = true
               ///  item.setIcon(R.drawable.ic_home_24dp)
               // itemO.setIcon(R.drawable.ic_old_tractor_bold_24dp)
               // itemN.setIcon(R.drawable.ic_new_tractor_bold_24dp)
                //itemN.
                removeBackStackFragmentsEntries()
                startActivity(Intent(this,OnRoadPriceActivity::class.java))
                false
            }
            else if(item.getItemId() === R.id.action_new_tractor) {
                val nvv = binding.drawerLayout.findViewById<CoordinatorLayout>(R.id.clll).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                val iteme:MenuItem = nvv.menu.findItem(R.id.action_home)
                val itemO:MenuItem = nvv.menu.findItem(R.id.action_old_tractor)
                val itemN: MenuItem = nvv.menu.findItem(R.id.action_new_tractor)
               // iteme.setIcon(R.drawable.ic_home_bold_24dp)
               // iteme.isChecked = false
              //  itemO.setIcon(R.drawable.ic_old_tractor_bold_24dp)
              ////  itemN.setIcon(R.drawable.ic_new_tractor_bold_24dp)
                //itemN.setIcon(R.drawable.ic_new_tractor)
               // item.setIcon(R.drawable.ic_home_bold_24dp)
                navigateToNewFragment()
                true
            }
            else if(item.getItemId() === R.id.action_old_tractor) {
                val nvv = binding.drawerLayout.findViewById<CoordinatorLayout>(R.id.clll).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                val iteme:MenuItem = nvv.menu.findItem(R.id.action_home)
                val itemO:MenuItem = nvv.menu.findItem(R.id.action_old_tractor)
                val itemN: MenuItem = nvv.menu.findItem(R.id.action_new_tractor)
               // iteme.isChecked = false
               // iteme.setIcon(R.drawable.ic_home_bold_24dp)
               // itemO.setIcon(R.drawable.ic_old_tractor_bold_24dp)
              //  itemN.setIcon(R.drawable.ic_new_tractor_bold_24dp)
                navigateToOldTractorsFragments()
                true
            }
            else if(item.getItemId() === R.id.action_more) {
                newInstance().show(fragmentTransaction,"BottomSheet")
                val nvv = binding.drawerLayout.findViewById<CoordinatorLayout>(R.id.clll).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                val itemO:MenuItem = nvv.menu.findItem(R.id.action_old_tractor)
                val itemN: MenuItem = nvv.menu.findItem(R.id.action_new_tractor)
              //  itemO.isChecked = false
              //  itemN.isChecked = false
               // item.setIcon(R.drawable.ic_home_24dp)
              //  item.isChecked = true
               // itemO.setIcon(R.drawable.ic_old_tractor_bold_24dp)
              //  itemN.setIcon(R.drawable.ic_new_tractor_bold_24dp)
                false
            }
            else false
           // true
        }
        val nvv = binding.drawerLayout.findViewById<CoordinatorLayout>(R.id.clll).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val item:MenuItem = nvv.menu.findItem(R.id.action_home)
        //item.isChecked = true
        //item.setIcon(R.drawable.ic_home_24dp)
     }
    fun reinstBNav() {
        val nvv = binding.drawerLayout.findViewById<CoordinatorLayout>(R.id.clll).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val item:MenuItem = nvv.menu.findItem(R.id.action_home)
        //item.isChecked = true
       // item.setIcon(R.drawable.ic_home_bold_24dp)
    }
    private fun navigateToSocialMedia(pageNo:Int) {
        val intent = Intent(this@MainActivity,SocialMedia::class.java)
        intent.putExtra("page",pageNo)
       // startActivity(intent)

        val socialMediaUrl = java.util.ArrayList<String?>()
        socialMediaUrl.add("https://www.facebook.com/khetigaadi.India/")
        socialMediaUrl.add("https://www.instagram.com/khetigaadi/")
        socialMediaUrl.add("https://www.linkedin.com/in/khetigaadi")

        when (pageNo) {
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
    private fun findViewById() {
        val view:View =  binding.drawerLayout.findViewById<NavigationView>(R.id.navView).getHeaderView(0)
        civUserImage = view.findViewById<View>(R.id.civUserImage) as CircleImageView
        tvUserType = view.findViewById<View>(R.id.tvUserType) as TextView
        tvUserName = view.findViewById<View>(R.id.tvUserName) as TextView
        tvAddress = view.findViewById<View>(R.id.tvAddress) as TextView
        tvUpdateProfile = view.findViewById<View>(R.id.tvUpdateProfile) as TextView
    }
    private fun initProfileDetails() {
        if(user != null) {
            if(user.image != null) {
                Glide.with(this)
                    .load(session.userOthers.userPath + user.image)
                    .placeholder(R.drawable.ic_user_profile)
                    .error(R.drawable.ic_user_profile)
                    .into(civUserImage!!)
            }
            if(user.userDetail != null && user.userDetail.type != null) {
                tvUserType!!.text = user.userDetail.type.userLevelName
            }
            else {
                tvUserType!!.visibility = View.GONE
            }
            tvUserName!!.text = String.format("%s %s",user.fname,user.lname)
            if(user.city != null && user.state != null) {
                tvAddress!!.text = String.format("%s, %s",user.city.name,user.state.name)
            }
            tvUpdateProfile!!.setOnClickListener {
                startActivity(Intent(this@MainActivity,EditProfileActivity::class.java))
                binding.drawerLayout.closeDrawers()
            }
            tvUserName!!.setOnClickListener {
                val intent = Intent(this@MainActivity,ProfileActivity::class.java)
                intent.putExtra("userId",user.id)
                startActivity(intent)
                binding.drawerLayout.closeDrawers()
            }
            civUserImage!!.setOnClickListener {
                val intent = Intent(this@MainActivity,ProfileActivity::class.java)
                intent.putExtra("userId",user.id)
                startActivity(intent)
                binding.drawerLayout.closeDrawers()
            }
            tvAddress!!.setOnClickListener {
                val intent = Intent(this@MainActivity,ProfileActivity::class.java)
                intent.putExtra("userId",user.id)
                startActivity(intent)
                binding.drawerLayout.closeDrawers()
            }
        }
    }
    private fun checkProfileAndForceToUpdate() {
        if(user != null)
            if(user.taluka == null || user.state == null || user.state.name.isEmpty() || !validateMobile(user.mobile) || user.fname == null || user.fname == "") {
            Toast.makeText(this,getString(R.string.pleaseUpdateYourProfile),Toast.LENGTH_LONG).show()
            val intent = Intent(this@MainActivity,EditProfileActivity::class.java)
            intent.putExtra(EditProfileActivity.IS_COMPULSORY,true)
            startActivity(intent)
            finish()
        }
    }
    private fun validateMobile(mobile:String?):Boolean {
        //if (mobile.isEmpty()) {
        return mobile?.let { Utils.isValidPhone(it) } ?: false
    }
    private fun initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }
    override fun onActivityResult(requestCode:Int,resultCode:Int,data:Intent?) {
        super.onActivityResult(requestCode,resultCode,data)
        if (requestCode == 1) {
            if (resultCode == 2) {
                fragmentTransaction
                mFragmentTransaction.replace(binding.containerView.id,UserDashboardFragment()).addToBackStack(TAG).commit()
            }
        }
        else if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            when (resultCode) {
                RESULT_CANCELED -> {
                    Timber.e("Update canceled by user! Result Code: %s", resultCode)
                }
                RESULT_OK -> {
                    Timber.e("Update success! Result Code: %s", resultCode)
                }
                else -> {
                    Timber.e("Update Failed! Result Code: %s", resultCode)
                    checkUpdate()
                }
            }
        }
        else if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            when (resultCode) {
                RESULT_CANCELED -> {
                    Timber.e("Update canceled by user! Result Code: %s", resultCode)
                }
                RESULT_OK -> {
                    Timber.e("Update success! Result Code: %s", resultCode)
                }
                else -> {
                    Timber.e("Update Failed! Result Code: %s", resultCode)
                    checkUpdate()
                }
            }
        }
    }
    private fun hideSoftKeyboardNew1() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0)
    }
    private fun logoutUser() {
        session.Logout()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }
    private fun logOutFCM() {
        userAPIs.deregisterPushToken().enqueue(object:Callback<MyResponse?> {
            override fun onResponse(call:Call<MyResponse?>,response:Response<MyResponse?>) {}
            override fun onFailure(call:Call<MyResponse?>,t:Throwable) {}
        })
    }
    private fun logout() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.are_you_sure_you_want))
            .setNegativeButton(getString(R.string.no)) { dialog: DialogInterface, which: Int -> dialog.cancel() }
            .setPositiveButton(getString(R.string.yes)) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                logOutFCM()
                logoutUser()
            }.show()
    }
    fun initDrawer() {
        // preparing list data
        prepareAfterLoginMenuData()
        val expandableListAdapter = ExpandableListAdapter(this,listDataHeader,listDataChild,binding.drawerLayout.findViewById<View>(R.id.navView).findViewById<ExpandableListView>(R.id.navigationMenu))
        binding.drawerLayout.findViewById<View>(R.id.navView).findViewById<ExpandableListView>(R.id.navigationMenu).setAdapter(expandableListAdapter)
        binding.drawerLayout.findViewById<View>(R.id.navView).findViewById<ExpandableListView>(R.id.navigationMenu).setOnChildClickListener(ExpandableListView.OnChildClickListener { parent:ExpandableListView?,v:View?,groupPosition:Int,childPosition:Int,id:Long ->
            val fragmentTransaction = mFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
            binding.drawerLayout.closeDrawers()
            val fm = supportFragmentManager
            val count = fm.backStackEntryCount
            for(i in 0 until count) {
                fm.popBackStackImmediate()
            }
            if(DrawerMenu.MY_FAVORITES == listDataHeader[groupPosition].name) {
                when(listDataChild!![listDataHeader[groupPosition]]!![childPosition].name) {
                    DrawerSubMenu.DASHBOARD -> {
                        fragmentTransaction.replace(binding.containerView.id,UserDashboardFragment()).addToBackStack(TAG).commit()
                    }
                    DrawerSubMenu.PROFILE -> {
//                      fragmentTransaction.replace(frameLayout.getId(), new Profile()).addToBackStack(TAG).commit();
                        val intent = Intent(this@MainActivity,ProfileActivity::class.java)
                        intent.putExtra("userId",user.id)
                        startActivity(intent)
                    }
                    DrawerSubMenu.MY_OLD_TRACTORS -> {
                        //fragmentTransaction.replace(frameLayout.getId(), new MyUploadedUsedTractorList()).addToBackStack(TAG).commit();
                        val fragment = MyUploadedUsedTractorList()
                        val bundle = Bundle()
                        bundle.putString("filter", "all")
                        fragment.arguments = bundle
                        fragmentTransaction.replace(R.id.containerView,fragment).addToBackStack(TAG).commit()
                    }
                    DrawerSubMenu.MY_RENT_TRACTORS -> {
                        fragmentTransaction.replace(
                            binding.containerView.id,
                            MyUploadedRentTractorList()
                        ).addToBackStack(TAG).commit()
                    }
                    DrawerSubMenu.BUY_ENQUIRY -> {
                        fragmentTransaction.replace(binding.containerView.id, BuyersEnquiry())
                            .addToBackStack(TAG).commit()
                    }
                    DrawerSubMenu.MY_FAVORITES_SUB -> {
                        fragmentTransaction.replace(binding.containerView.id, MyFavoritesTabs())
                            .addToBackStack(TAG).commit()
                    }
                    DrawerSubMenu.ADD_OWNER_TRACTOR -> {

                        //  fragmentTransaction.replace(frameLayout.getId(), new MyFavoritesTabs()).addToBackStack(TAG).commit();
                        val intent = Intent(this@MainActivity, IsOwnerTractorBrand::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }
            }
            else if(DrawerMenu.ABOUT_KHETIGAADI == listDataHeader[groupPosition].name) {
                when(listDataChild!![listDataHeader[groupPosition]]!![childPosition].name) {
                    DrawerSubMenu.ABOUT_US -> {
                        fragmentTransaction.replace(binding.containerView.id, AboutUs())
                            .addToBackStack(TAG).commit()
                    }
                    DrawerSubMenu.TERMS_AND_CONDITIONS -> {
                        val intent = Intent(this, WebViewActivity::class.java)
                        intent.putExtra(WebViewActivity.TITLE, "Terms and Conditions")
                        intent.putExtra(
                            WebViewActivity.URL,
                            "https://khetigaadi.com/terms-and-conditions/en"
                        )
                        startActivity(intent)
                    }
                    DrawerSubMenu.PRIVACY_POLICY -> {
                        val intent = Intent(this, WebViewActivity::class.java)
                        intent.putExtra(WebViewActivity.TITLE, "PRIVACY POLICY")
                        intent.putExtra(
                            WebViewActivity.URL,
                            "https://khetigaadi.com/privacy-policy/en"
                        )
                        startActivity(intent)
                    }
                }
            }
            binding.drawerLayout.findViewById<View>(R.id.navView).findViewById<ExpandableListView>(R.id.navigationMenu).collapseGroup(groupPosition)
            false
        })
        binding.drawerLayout.findViewById<View>(R.id.navView).findViewById<ExpandableListView>(R.id.navigationMenu).setOnGroupClickListener { parent, v, groupPosition, id ->
            val fragmentTransaction = mFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit)
            removeBackStackFragmentsEntries()
            when(listDataHeader[groupPosition].name) {
                DrawerMenu.Home -> {
                    navigateToHomeFragment(fragmentTransaction)
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.TRACTORS -> {
                    navigateToNewFragment()
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.IMPLEMENTS -> {
                    navigateToImplementFragment()
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.BUY_OLD_TRACTOR -> {
                    navigateToOldTractorsFragments()
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.SELL_OLD_TRACTOR -> {
                    //fragmentTransaction.replace(frameLayout.getId(), new SellTractor1()).addToBackStack(TAG).commit();
                    binding.drawerLayout.closeDrawers()
                    navigateToSellTractor()
                }
                DrawerMenu.RENT -> {
                    fragmentTransaction.replace(
                        binding.containerView.id,
                        RentTractorListFragment()
                    ).addToBackStack(TAG).commit()
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.HARVESTER -> {
                    fragmentTransaction.replace(
                        binding.containerView.id,
                        SearchHarvesterFragment.newInstance()
                    ).addToBackStack(TAG).commit()
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.COMPARE -> {
                    navigateToCompareFragment(CallFrom.NEW_TRACTOR)
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.SHOP -> {

                    //fragmentTransaction.replace(frameLayout.getId(), new SellTractor1()).addToBackStack(TAG).commit();
                    binding.drawerLayout.closeDrawers()
                    navigateToShop()
                }
                DrawerMenu.MANDI_PRICE -> {
                    fragmentTransaction.replace(
                        binding.containerView.id,
                        MandiFragment.newInstance()
                    ).addToBackStack(TAG).commit()
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.LANGUAGE -> {
                    binding.drawerLayout.closeDrawers()
                    startActivity(Intent(this@MainActivity, SelectLanguage::class.java))
                }
                DrawerMenu.NEWS -> {
                    navigateToNewsFragment()
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.DEALERS -> {
                    fragmentTransaction.replace(binding.containerView.id, DealerListFragment())
                        .addToBackStack(TAG).commit()
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.SERVICE_CENTER -> {
                    fragmentTransaction.replace(binding.containerView.id, SCenterList())
                        .addToBackStack(TAG).commit()
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.WEATHER -> {
                    if (Constants.isConnectionAvailable(this)) {
                        startActivity(Intent(this@MainActivity, WeatherNewActivity::class.java))
                        binding.drawerLayout.closeDrawers()
                    }
                    else {
                        noInternetConnection(binding.containerView)
                    }
                }
                DrawerMenu.SAFETY_TIPS -> {
                    startActivity(Intent(this@MainActivity,SafetyTipsActivity::class.java))
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.SHARE -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT,resources.getString(R.string.share_msg))
                    try {
                        startActivity(Intent.createChooser(intent,"Select an action"))
                    }
                    catch(ex:ActivityNotFoundException) {
                        ex.printStackTrace()
                        // (handle error)
                    }
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.FEEDBACK -> {
                    startActivity(Intent(this, FeedBackActivity::class.java))
                    binding.drawerLayout.closeDrawers()
                }
                DrawerMenu.LOGOUT -> {
                   // logout()
                    try {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse("market://details?id=$packageName")
                        startActivity(i)
                    }
                    catch(e:ActivityNotFoundException) {
                        e.printStackTrace()
                        startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(GOOGLE_PLAY_URL + packageName)))
                    }

                    binding.drawerLayout.closeDrawers()
                }
            }
            false
        }
        binding.drawerLayout.findViewById<View>(R.id.navView).findViewById<ExpandableListView>(R.id.navigationMenu).setIndicatorBounds(GetDipsFromPixel(205f),GetDipsFromPixel(0f))
        //        Here width means device width.
        // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
        // Code here will be triggered once the drawer open as we don't want anything to happen so we leave this blank
        val actionBarDrawerToggle:ActionBarDrawerToggle = object:ActionBarDrawerToggle(this,binding.drawerLayout,binding.toolbars.toolbar,R.string.app_name,R.string.app_name)
        {
            override fun onDrawerOpened(drawerView:View) {
                // Code here will be triggered once the drawer open as we don't want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView)
                hideSoftKeyboard()
            }
        }
        actionBarDrawerToggle.drawerArrowDrawable.color = ContextCompat.getColor(this,R.color.black)
        //Setting the actionbarToggle to drawer layout
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true;
        actionBarDrawerToggle.syncState()
    }
    fun navigateToShop() {
        val intent = Intent(this@MainActivity,EcomHomeActivity::class.java)
        startActivity(intent)
    }
    fun navigateToOldTractorsFragments() {
        removeBackStackFragmentsEntries()
        fragmentTransaction.replace(binding.containerView.id,SearchUsedTractorFilter()).addToBackStack(TAG).commit()
    }
    private fun navigateToNewFragment() {
        removeBackStackFragmentsEntries()
        fragmentTransaction
        mFragmentTransaction.replace(binding.containerView.id,SearchNewTractorFilter()).addToBackStack(TAG).commit()
    }
    private fun navigateToHomeFragment(fragmentTransaction:FragmentTransaction) {
        removeBackStackFragmentsEntries()
        fragmentTransaction.replace(binding.containerView.id,HomeFragment()).addToBackStack(TAG).commit()
    }
    private fun removeBackStackFragmentsEntries() {
        val fm = supportFragmentManager
        try {
            val count = fm.backStackEntryCount
            for(i in 0 until count) {
                fm.popBackStackImmediate()
            }
        }
        catch(e:Exception) {
            e.printStackTrace()
        }
    }
    private fun GetDipsFromPixel(pixels:Float):Int {
        // Get the screen's density scale
        val scale = resources.displayMetrics.density
        // Convert the dps to pixels, based on density scale
        return (pixels * scale + 0.5f).toInt()
    }
    private fun prepareAfterLoginMenuData() {
        listDataHeader = ArrayList()
        listDataChild = HashMap()
        // Adding data header
       // (listDataHeader as ArrayList<ExpandedMenuModel>).add(ExpandedMenuModel(DrawerMenu.Home,resources.getString(R.string.home),R.drawable.ic_home_black_24dp))

        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.TRACTORS,getString(R.string.tractors),R.drawable.ic_new_tractor_home))
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.IMPLEMENTS,getString(R.string.implements_lbl),R.drawable.resource_implements2))  //R.drawable.ic_implement_24dp
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.HARVESTER,getString(R.string.harvester),R.drawable.harvesterr))
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.BUY_OLD_TRACTOR,getString(R.string.buy_old_tractor),R.drawable.buy_old_tractorsvg))
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.SELL_OLD_TRACTOR,getString(R.string.sell_old_tractor),R.drawable.ic_sell_24dp))
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.RENT,getString(R.string.rent),R.drawable.rent_svg))
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.COMPARE,getString(R.string.compare),R.drawable.ic_compare_trr_24dp)) //ic_compare_tr_24dp
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.SHOP,getString(R.string.shop),R.drawable.ic_shop_24dp))
        // listDataHeader.add(new ExpandedMenuModel(DrawerMenu.MANDI_PRICE, getString(R.string.mandi_price), R.drawable.ic_shop_24dp));
        // listDataHeader.add(new ExpandedMenuModel("Khetiguru", getString(R.string.khetiguru), R.drawable.ic_choose_crops_48));
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.MY_FAVORITES,getString(R.string.my_favorites),R.drawable.my_fevorites_svg))  // myfavv1 my_fevorites_svg ic_my_favorites_24dp

//        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
//            ExpandedMenuModel(DrawerMenu.LANGUAGE,getString(R.string.language),R.drawable.ic_languages_24dp))
//
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(ExpandedMenuModel(DrawerMenu.NEWS,getString(R.string.news),R.drawable.ic_news_24dp))
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.DEALERS,getString(R.string.dealers),R.drawable.ic_dealers_24dp))
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.SERVICE_CENTER,getString(R.string.service_centers),R.drawable.ic_service_centers_24dp))
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.WEATHER,getString(R.string.weather),R.drawable.ic_weather_24dp))
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.SAFETY_TIPS,getString(R.string.safety_tips),R.drawable.safety_tips_24dp))
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.SHARE,getString(R.string.share),R.drawable.ic_share_khetiguru_app))
        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.FEEDBACK,getString(R.string.feedback),R.drawable.ic_rate_and_feedback)) //ic_feedback_24dp

       // (listDataHeader as ArrayList<ExpandedMenuModel>).add(
         //   ExpandedMenuModel(DrawerMenu.FEEDBACK,getString(R.string.feedback),R.drawable.ic_rate_and_feedback)) //ic_feedback_24dp


        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.ABOUT_KHETIGAADI,getString(R.string.about_khetigaadi),R.drawable.ic_about_24))

        (listDataHeader as ArrayList<ExpandedMenuModel>).add(
            ExpandedMenuModel(DrawerMenu.LOGOUT,getString(R.string.rate_dialog_title),R.drawable.ic_rating_24dp)) //R.drawable.ic_logout_24dp
        // ExpandedMenuModel(DrawerMenu.LOGOUT,getString(R.string.logout),R.drawable.ic_logout_24dp))
        // Adding child data
        // Header, Child data
        for(i in listDataHeader.indices) {
            val heading: MutableList<ExpandedSubMenu> = ArrayList()
            when(listDataHeader[i].name) {
                DrawerMenu.MY_FAVORITES -> {
                    heading.add(ExpandedSubMenu(DrawerSubMenu.DASHBOARD,getString(R.string.dashboard)))
                    heading.add(ExpandedSubMenu(DrawerSubMenu.PROFILE, getString(R.string.profile)))
                    heading.add(ExpandedSubMenu(DrawerSubMenu.MY_OLD_TRACTORS,getString(R.string.my_old_tractors)))
                    heading.add(ExpandedSubMenu(DrawerSubMenu.MY_RENT_TRACTORS,getString(R.string.my_rent_tractors)))
                    heading.add(ExpandedSubMenu(DrawerSubMenu.BUY_ENQUIRY,getString(R.string.buy_enquiry)))
                    heading.add(ExpandedSubMenu(DrawerSubMenu.MY_FAVORITES_SUB,getString(R.string.my_favorites)))
                    heading.add(ExpandedSubMenu(DrawerSubMenu.ADD_OWNER_TRACTOR,getString(R.string.add_owner_tractor)))
                }
                DrawerMenu.ABOUT_KHETIGAADI -> {
                    heading.add(ExpandedSubMenu(DrawerSubMenu.ABOUT_US,getString(R.string.about_us)))
                    heading.add(ExpandedSubMenu(DrawerSubMenu.TERMS_AND_CONDITIONS,getString(R.string.terms_and_conditions)))
                    heading.add(ExpandedSubMenu(DrawerSubMenu.PRIVACY_POLICY,getString(R.string.privacy_policy)))
                }
            }
            listDataChild!![listDataHeader[i]] = heading
        }
    }
    private fun setupDrawerContent(navigationView:NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem:MenuItem ->
            menuItem.isChecked = true
            binding.drawerLayout.closeDrawers()
            true
        }
    }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        //Checking for fragment count on backstack
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawers()
        }
        else if(supportFragmentManager.backStackEntryCount > 0) {
            val fm = supportFragmentManager
            val fragment = supportFragmentManager.findFragmentById(R.id.containerView)
            for(entry in 0 until fm.backStackEntryCount) {
                Timber.i("Found fragment: %s", fm.getBackStackEntryAt(entry).name)
                //                if (fragment instanceof UserDashboard) {//the fragment on which you want to handle your back press
//                    Log.i("BACK PRESSED", "BACK PRESSED");
//                    fm.popBackStack("Login", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                } else
                when(fragment) {
                    is NewTractorsListFragment -> {
                        fm.popBackStack(
                            "NewTractorListFilter",
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        fm.popBackStack("NewTractorsList", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }
                    is UsedTractorsListFragment -> {
                        fm.popBackStack(
                            "UsedTractorListFilter",
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        fm.popBackStack(
                            "UsedTractorsList",
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }
                    is NewImplementsList -> {
                        fm.popBackStack(
                            "NewImpListFilter",
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        fm.popBackStack(
                            "NewImplementsList",
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }
                    is DealerListFragment -> {
                        fm.popBackStack(
                            "DealerListFilter",
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        fm.popBackStack("DealerList", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }
                    is SCenterList -> {
                        fm.popBackStack("SCListFilter", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        fm.popBackStack("SCenterList", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }
                    is RentTractorListFragment -> {
                        fm.popBackStack(
                            "RentTractorListFilter",
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        fm.popBackStack("RentTractorList", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }
                    is CompareImpSelection -> {
                        fm.popBackStack("GetImpBrandName", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        fm.popBackStack("GetImpModelName", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        fm.popBackStack("GetImpModelName", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }
                    is CompareImp -> {
                        fm.popBackStack("GetImpBrandName", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        fm.popBackStack("GetImpModelName", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        fm.popBackStack("GetImpModelName", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }
                }
            }
            supportFragmentManager.popBackStack()
        }
        else if(!doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = true
            Toast.makeText(this,resources.getString(R.string.click_back_again_to_exit),Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false },2000)
        }
        else {
            super.onBackPressed()
        }
    }
    fun hideSoftKeyboard() {
        val view = this.currentFocus
        if(view != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
        }
    }
    override fun showSoftKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val v = currentFocus
        if(v != null) imm.showSoftInput(v,0)
    }
    private fun handleNotifications() {
        if(intent != null && intent.extras != null) {
            if(intent.hasExtra("filterValue")) {
                val filterValue = intent.extras!!.getString("filterValue")
                intent.replaceExtras(Bundle())
                if(filterValue != null) {
                    fragmentTransaction
                    mFragmentTransaction.replace(
                        binding.containerView.id,
                        NewTractorsListFragment.getInstance(
                            Gson().fromJson(filterValue,Filter::class.java))).addToBackStack(TAG).commit()
                }
            }
            else if(intent.hasExtra(Constants.NOTIFICATION_INTENT_KEY)) {
                val notification = intent.getParcelableExtra<Notification>(Constants.NOTIFICATION_INTENT_KEY)
                val intent:Intent
                if(notification!!.type != null) {
                    when(notification.type) {
                        NotificationType.NEW_TRACTOR_NOTIFICATION -> {
                            intent = Intent(this, NewTractorDetails::class.java)
                            intent.putExtra(NewTractorDetails.newTractorId, notification.id)
                        }
                        NotificationType.IMPLEMENT_NOTIFICATION -> {
                            intent = Intent(this, NewImpOverviewDetails::class.java)
                            intent.putExtra("brandId", notification.brandId)
                            intent.putExtra("typeId", notification.typeId)
                            intent.putExtra("modelId", notification.modelId)
                        }
                        NotificationType.NEWS_NOTIFICATION -> {
                            intent = Intent(this, NewsDetailsActivity::class.java)
                            intent.putExtra("news_id", notification.id)
                        }
                        else -> {
                            intent = Intent(this,NotificationResult::class.java)
                            intent.putExtras(getIntent())
                        }
                    }
                    startActivity(intent)
                }
            }
            else if(intent.data != null && intent.data.toString().contains("newTractor")) {
                val list = intent.data.toString().split("/")
                val j = Intent(this, NewTractorDetails::class.java)
                j.putExtra(NewTractorDetails.newTractorId, list[list.size - 1])
                startActivity(j)
            }
            else if (intent.data != null && intent.data.toString().contains("oldTractor")) {
                val list = intent.data.toString().split("/")
                val j = Intent(this, UsedTractorDetailActivity::class.java)
                j.putExtra("tractorId", list[list.size - 1])
                startActivity(j)
            }
            else if (intent.data != null && intent.data.toString().contains("rentTractor")) {
                val list = intent.data.toString().split("/")
                val j = Intent(this, RentTractorDetails::class.java)
                j.putExtra("tractorId", list[list.size - 1])
                startActivity(j)
            }
            else if (intent.data != null && intent.data.toString().contains("imp")) {
                val list = intent.data.toString().split("/")
                val j = Intent(this, NewImpOverviewDetails::class.java)
                j.putExtra("brandId", list[list.size - 3].toInt())
                j.putExtra("typeId", list[list.size - 2].toInt())
                j.putExtra("modelId", list[list.size - 1].toInt())
                startActivity(j)
            }
            else if (intent.data != null && intent.data.toString().contains("harvester")) {
                val list = intent.data.toString().split("/")
                val j = Intent(this, HarvesterDetailsActivity::class.java)
                j.putExtra("harvesterId", list[list.size - 1].toInt())
                startActivity(j)
            }
            else if (intent.data != null && intent.data.toString().contains("product")) {
                val list = intent.data.toString().split("/")
                val j = Intent(this, ProductDetailsActivity::class.java)
                j.putExtra("productId", list[list.size - 1].toInt())
                startActivity(j)
            }
            else if (intent.data != null && intent.data.toString().contains("profile")) {
                try {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtras(getIntent())
                    intent.action = getIntent().action
                    intent.data = getIntent().data
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else if (intent.data != null && intent.data.toString().contains("news")) {
                val list = intent.data.toString().split("/")
                val intent = Intent(this, NewsDetailsActivity::class.java)
                intent.putExtra("news_id", list[list.size - 1])
                startActivity(intent)
            }
            else if (intent.data != null && intent.data.toString().contains("video")) {
                val list = intent.data.toString().split("/")
                val intent = Intent(this, VideosActivity::class.java)
                intent.putExtra("videoId", list[list.size - 1])
                startActivity(intent)
            }
        }
    }
    override fun onNewIntent(intent:Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        //now getIntent() should always return the last received intent
    }
    override fun onResume() {
        super.onResume()
        //        initDrawer();
        user = session.user
        initProfileDetails()
        hideSoftKeyboardNew1()
        try {
            if( /*user.getCrops() == null || user.getCrops().size() == 0 ||*/user.userDetail.biography == null || user.userDetail.biography == "") {
                tvUpdateProfile!!.setTextColor(ContextCompat.getColor(this,R.color.red))
            }
            else {
                tvUpdateProfile!!.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary))
            }
            appUpdateManager.appUpdateInfo
                .addOnSuccessListener { appUpdateInfo ->
                    if(appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForCompleteUpdate()
                    }
                }
        }
        catch(e:Exception) {
            e.printStackTrace()
        }
    }
    fun noInternetConnection() {
        noInternetConnection(binding.containerView)
    }
    fun setToolbarTitle(title:String?) {
        binding.toolbars.toolbar.title = title
    }
    override fun onDestroy() {
        if(aLBReceiver != null) LocalBroadcastManager.getInstance(this).unregisterReceiver(aLBReceiver!!)
        super.onDestroy()
    }
    fun navigateToCompareFragment(@CallFrom callFrom:Int) {
        removeBackStackFragmentsEntries()
        fragmentTransaction.replace(binding.containerView.id,TabCompare.getInstance(callFrom)).addToBackStack(TAG).commit()
    }
    fun navigateToHarvester() {
        removeBackStackFragmentsEntries()
        fragmentTransaction.replace(binding.containerView.id,SearchHarvesterFragment.newInstance()).addToBackStack(TAG).commit()
    }
    private val fragmentTransaction:FragmentTransaction
        get() {
            mFragmentManager = supportFragmentManager
            mFragmentTransaction = mFragmentManager.beginTransaction()
            mFragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
            return mFragmentTransaction as FragmentTransaction
        }
    fun navigateToSellTractor() {
        val intent = Intent(this@MainActivity,SellTractorActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
    fun navigateToNewsFragment() {
        removeBackStackFragmentsEntries()
        fragmentTransaction.replace(binding.containerView.id,NewsListFragment())
            .addToBackStack(TAG).commit()
    }
    fun navigateToImplementFragment() {
       try {
           removeBackStackFragmentsEntries()
           fragmentTransaction.replace(binding.containerView.id,SearchNewImplementsFilter())
               .addToBackStack(TAG).commit()
       }
       catch(e:Exception) {
           e.printStackTrace()

       }
    }
    @SuppressLint("CheckResult")
    fun getBrands() {
     try {
         val cnfg = brandRepository!!.getCountt()
         if (cnfg > 0)
             return
         //brandRepository!!.deleteAll()
         khetiAPIs.brands
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribeWith(object : DisposableSingleObserver<BrandResp>() {
                 override fun onSuccess(myResponse: BrandResp) {
                     try {
                         val vcxbcx = myResponse.brand.size;
                         println("vcbcvbcvbcvb==$vcxbcx");
                         if (cnfg > 0)
                             brandRepository!!.deleteAll()
                         brandRepository!!.insertAll(myResponse.brand)
                         val lpath: String = myResponse.others
                             .brandLogoPath // "https://khetigaadi.com/images/brandlogos/";

                         val sharedPreferenceUtils =
                             SharedPreferenceUtils.getInstance(this@MainActivity)
                         sharedPreferenceUtils.setValue(Constants.BRAND_BASE_URL, lpath)
                     } catch (e: Exception) {
                         e.printStackTrace()
                     }
                 }

                 override fun onError(e: Throwable) {

                 }
                 // Missing the onError implementation
             })
     }
     catch (e: Exception) {
          e.printStackTrace()
     }
    }
    companion object {
        private const val FLEXIBLE_APP_UPDATE_REQ_CODE = 67
        private const val IMMEDIATE_APP_UPDATE_REQ_CODE = 763
        private const val GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id="
        @JvmStatic
        fun hideKeyboard(ctx:Context) {
            val inputManager = ctx.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            // check if no view has focus:
            val v = (ctx as Activity).currentFocus ?:return
            inputManager.hideSoftInputFromInputMethod(v.windowToken,0)
        }
       // @SuppressLint("UseKtx")
        @JvmStatic
        fun onWhatsAppClick(contextCompat:Activity) {
            try {
                val packageManager = contextCompat.packageManager
                val i = Intent(Intent.ACTION_VIEW)
                val url = "https://api.whatsapp.com/send?phone="+contextCompat.getString(R.string.whatsAppNo)+"&text="+URLEncoder.encode("Hello KhetiGaadi","UTF-8")
                //            i.setPackage("com.whatsapp");
                i.data = Uri.parse(url)
                if(i.resolveActivity(packageManager) != null) {
                    contextCompat.startActivity(i)
                }
            }
            catch(e:Exception) {
                e.printStackTrace()
            }
        }
    }

    data class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

//    fun View.doOnApplyWindowInsets(f: (view: View, insets: WindowInsetsCompat, initial: InitialPadding) -> Unit) {
//        val initialPadding = InitialPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
//        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
//            f(v, insets, initialPadding)
//            insets
//        }
//        if (isAttachedToWindow) {
//            requestApplyInsets()
//        } else {
//            addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
//                override fun onViewAttachedToWindow(v: View) {
//                    v.removeOnAttachStateChangeListener(this)
//                    v.requestApplyInsets()
//                }
//                override fun onViewDetachedFromWindow(v: View) {}
//            })
//        }
//    }

    data class InitialLayoutState(
        val paddingLeft: Int,
        val paddingTop: Int,
        val paddingRight: Int,
        val paddingBottom: Int,
        val marginTop: Int,
        val marginBottom: Int
    )

    fun View.getInitialLayoutState(): InitialLayoutState {
        val marginParams = layoutParams as? ViewGroup.MarginLayoutParams
        return InitialLayoutState(
            paddingLeft = paddingLeft,
            paddingTop = paddingTop,
            paddingRight = paddingRight,
            paddingBottom = paddingBottom,
            marginTop = marginParams?.topMargin ?: 0,
            marginBottom = marginParams?.bottomMargin ?: 0
        )
    }

    fun View.doOnApplyWindowInsets(f: (view: View, insets: WindowInsetsCompat, initial: InitialLayoutState) -> Unit) {
        val initialState = getInitialLayoutState()
        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            f(v, insets, initialState)
            insets
        }
        if (isAttachedToWindow) {
            requestApplyInsets()
        } else {
            addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    v.removeOnAttachStateChangeListener(this)
                    v.requestApplyInsets()
                }
                override fun onViewDetachedFromWindow(v: View) {}
            })
        }
    }
}
