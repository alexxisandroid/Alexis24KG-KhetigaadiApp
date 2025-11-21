package com.khetigaadi.base
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.khetigaadi.base.receiver.ConnectionReceiver
import java.util.Arrays
import java.util.Locale
abstract class BaseActivity:AppCompatActivity() {
    private var progressDialog:ProgressDialog? = null
    protected var noInternetDialog:Dialog? = null
    private var connectionReceiver:ConnectionReceiver? = null
    protected var isInternetAvailable = MutableLiveData<Boolean>()
    private fun createObjectOfConnectionReceiver() {
        connectionReceiver = ConnectionReceiver { isConnectionAvailable ->
            isInternetAvailable.setValue(isConnectionAvailable)
            if (!isConnectionAvailable) {
                noInternetDialog = Dialog(
                    this@BaseActivity,
                    android.R.style.Theme_NoTitleBar_Fullscreen
                )
                noInternetDialog!!.setContentView(R.layout.no_internet_dialog)
                noInternetDialog!!.setCancelable(false)
                noInternetDialog!!.setOnKeyListener { dialog: DialogInterface?, keyCode: Int, event: KeyEvent? ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        noInternetDialog!!.dismiss()
                        finishAndRemoveTask()
                    }
                    true
                }
                noInternetDialog!!.show()
            } else {
                if (noInternetDialog != null && noInternetDialog!!.isShowing) {
                    noInternetDialog!!.dismiss()
                }
            }
        }
    }
    fun broadcastIntent() {
        registerReceiver(connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
    override fun onResume() {
        super.onResume()
        if (connectionReceiver == null) {
            createObjectOfConnectionReceiver()
            broadcastIntent()
        }
    }
    override fun onPause() {
        super.onPause()
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver)
            connectionReceiver = null
        }
    }
    override fun onPostCreate(savedInstanceState:Bundle?) {
        super.onPostCreate(savedInstanceState)
    }
    override fun onNewIntent(intent:Intent) {
        super.onNewIntent(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        dismissProgressDialog()
    }
    override fun onSaveInstanceState(outState:Bundle) {
        super.onSaveInstanceState(outState)
    }
    /**
     * Set up the ui so every view and nested view that is not EditText will listen to touch event and dismiss the keyboard if touched.
     * http://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext
     */
    fun setupTouchUIToDismissKeyboard(view: View) {
        setupTouchUIToDismissKeyboard(view,
            { v: View?, event: MotionEvent? ->
                hideSoftKeyboard(this@BaseActivity)
                false
            }, -1
        )
    }
    fun showSoftKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val v = currentFocus
        if (v != null) imm.showSoftInput(v,0)
    }
    protected fun showProgressDialog(message:String?) {
        try {
            if(progressDialog == null) {
                progressDialog = ProgressDialog(this)
            }
            if(!progressDialog!!.isShowing) {
                progressDialog = ProgressDialog(this)
                progressDialog!!.setMessage(message)
                progressDialog!!.show()
            }
        }
        catch(e:Exception) {
            e.printStackTrace()
        }
    }
    protected fun setProgressDialogDismissOnTouchOutside(cancel:Boolean) {
        progressDialog!!.setCanceledOnTouchOutside(cancel)
    }
    protected fun showOrUpdateProgressDialog(message:String?) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }
        if (!progressDialog!!.isShowing) {
            progressDialog = ProgressDialog(this)
            progressDialog!!.setMessage(message)
            progressDialog!!.show()
            //        }
        } else progressDialog!!.setMessage(message)
    }
    protected fun dismissProgressDialog() {
        // For handling orientation changed.
        try {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        }
        catch(e:Exception) {
            e.printStackTrace()
        }
    }
    protected fun requestFocus(view:View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }
    fun refreshActionBarMenu() {
        invalidateOptionsMenu()
    }
    private fun getCityFromGEOCode(latitude:Double,longitude:Double):String? {
        val geocoder = Geocoder(this,Locale.getDefault())
        val addresses:List<Address>?
        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1)
            return addresses!![0].subAdminArea
        }
        catch(e:Exception) {
            e.printStackTrace()
        }
        return null
    }
    companion object {
        fun setupTouchUIToDismissKeyboard(view:View,onTouchListener:OnTouchListener?,vararg exceptIDs:Int?) {
            var ids:List<Int?> = ArrayList()
            if(exceptIDs != null) ids = Arrays.asList(*exceptIDs)
            //Set up touch listener for non-text box views to hide keyboard.
            if (view !is EditText) {
                if(!ids.isEmpty() && ids.contains(view.id)) {
                    return
                }
                view.setOnTouchListener(onTouchListener)
            }
            //If a layout container, iterate over children and seed recursion.
            if(view is ViewGroup) {
                for(i in 0 until view.childCount) {
                    val innerView = view.getChildAt(i)
                    setupTouchUIToDismissKeyboard(innerView,onTouchListener,*exceptIDs)
                }
            }
        }
        /**
         * Hide the Soft Keyboard.
         */
        fun hideSoftKeyboard(activity:Activity) {
            val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager ?: return
            if(activity.currentFocus != null && activity.currentFocus!!.windowToken != null) inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }
}
