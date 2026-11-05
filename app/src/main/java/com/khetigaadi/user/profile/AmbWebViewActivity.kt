package com.khetigaadi.user.profile
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.khetigaadi.R
import com.khetigaadi.base.WebViewFragment
import com.khetigaadi.databinding.ActivityAmbWebViewBinding
import com.khetigaadi.retrofit.ShopAPIs
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
@Suppress("UPPER_BOUND_VIOLATED_BASED_ON_JAVA_ANNOTATIONS")
@AndroidEntryPoint
class AmbWebViewActivity : AppCompatActivity() {
    private var binding:ActivityAmbWebViewBinding? = null
    private var page = 0
    @Inject
    lateinit var shopAPIs:ShopAPIs
    @Inject
    lateinit var disposable:CompositeDisposable
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAmbWebViewBinding.inflate(layoutInflater)
        setContentView(binding?.getRoot())
        page = intent.getIntExtra("page",1)
        initToolbar()
        getContent(page)
    }
    private fun getContent(page:Int) {
        var aboutUs:Single<AboutResp>? = null
        when (page) {
            1 -> {
                aboutUs = shopAPIs.getAboutUs()
            }
            2 -> {
                aboutUs = shopAPIs.getContactUs()
            }
            3 -> {
                aboutUs = shopAPIs.getPrivacyPolicy()
            }
            4 -> {
                aboutUs = shopAPIs.getReturnPolicy()
            }
            5 -> {
                aboutUs = shopAPIs.getRulesAndRegulation()
            }
            6 -> {
                aboutUs = shopAPIs.getUGCPolicy()
            }
            7 -> {
                aboutUs = shopAPIs.getTermsAndCondition()
            }
        }
        if(aboutUs != null) {
            binding?.progress?.llProgress?.visibility = View.VISIBLE
            disposable.add(
                aboutUs.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object:DisposableSingleObserver<AboutResp>() {
                        override fun onSuccess(productListResp:AboutResp) {
                            if(productListResp.isError) Toast.makeText(this@AmbWebViewActivity,productListResp.msg[0],Toast.LENGTH_SHORT).show()
                            else {
                                binding?.progress?.llProgress?.visibility = View.GONE
                                loadFragment(productListResp.data)
                            }
                        }
                        override fun onError(e:Throwable) {
                            Timber.e(e,"onError: ")
                            Toast.makeText(this@AmbWebViewActivity,e.message,Toast.LENGTH_SHORT).show()
                        }
                    }))
        }
    }
    private fun loadFragment(data:AboutData) {
        try {
            if(!isFinishing) {
                val exampleFragment: Fragment? =
                    WebViewFragment.newInstance(null,data.content)
                //  WebViewFragment.
                supportFragmentManager.beginTransaction().add(R.id.fragmentContainer,exampleFragment!!).commit()
            }
        }
        catch(e:Exception) {
            e.printStackTrace()
        }
    }
    private fun initToolbar() {
        setSupportActionBar(binding?.toolbarI?.toolbar)
        binding?.toolbarI?.toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding?.toolbarI?.toolbar?.setNavigationOnClickListener({ v -> onBackPressed() })
        binding?.toolbarI?.toolbar?.setTitle(getTitle(page))
    }
    private fun getTitle(page:Int):String? {
        return when (page) {
            1 -> getString(R.string.about_us)
            2 -> getString(R.string.contact_us)
            3 -> getString(R.string.privacy_policy)
            4 -> getString(R.string.return_policy)
            5 -> getString(R.string.rules_regulations)
            6 -> getString(R.string.ugc_policy)
            7 -> getString(R.string.terms_and_conditions)
            else -> getString(R.string.app_name)
        }
    }
    companion object {
        const val TITLE: String = "title"
    }
}