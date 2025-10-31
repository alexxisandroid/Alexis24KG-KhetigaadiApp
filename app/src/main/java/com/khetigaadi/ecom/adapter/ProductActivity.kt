package com.khetigaadi.ecom.adapter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khetigaadi.R
import com.khetigaadi.databinding.ActivityProductBinding
import com.khetigaadi.ecom.categories.CategoriesListActivity
import com.khetigaadi.shop.model.ProductListResp
import com.khetigaadi.shop.model.Category
import com.khetigaadi.shop.retrofit.ShopParentAPIs
import com.khetigaadi.user.SessionManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
class ProductActivity : AppCompatActivity() {
    private var binding: ActivityProductBinding? = null
    private var categoriesAdapter: ProductsAdapter? = null
    private var rvProductList: RecyclerView? = null
    @set:Inject
    internal  var sessionManager:SessionManager? = null
   //  @Inject
   //  var categoryRepository:CategoryRepository? = null
   @set:Inject
   internal var shopAPIs: ShopParentAPIs? = null
    var selectedCategory = ""
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        val view: View = binding!!.getRoot()
        setContentView(view)
       // viewModel = ViewModelProvider(this).get(CategoriesListViewModel::class.java)
        initToolbar()
        findViewById()
        initCategory()
        if(intent.hasExtra(CategoriesListActivity.SELECTED_CATEGORY)) {
            val bv = intent.getParcelableExtra<Category>(CategoriesListActivity.SELECTED_CATEGORY)
            shopAPIs!!.getProduct(bv!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : DisposableSingleObserver<ProductListResp?>() {
//                    override fun onSuccess(productListResp: ProductListResp) {
//                        if (productListResp.isError) {
//                        } else {
//                            binding!!.rvProductList.setVisibility(View.VISIBLE)
//                            categoriesAdapter!!.addItems(productListResp.data.data,1,sessionManager!!.getEcomOther())
//                        }
//                    }
//                    override fun onError(e: Throwable) {
//                        Timber.e(e,"onError: ")
//                        //gardenTools.postError(e.getMessage());
//                    }
//                })


                .subscribeWith(object : DisposableSingleObserver<ProductListResp>() {

//                    override fun onNext(productListResp: ProductListResp) {
//                        // Handle the incoming item here. This is the equivalent of 'onSuccess' for each item.
//                        if (productListResp.isError) {
//                        } else {
//                            binding!!.rvProductList.setVisibility(View.VISIBLE)
//                            categoriesAdapter!!.addItems(productListResp.data.data,1,sessionManager!!.getEcomOther())
//                        }
//                    }

                    override fun onSuccess(productListResp: ProductListResp) {
                       // TODO("Not yet implemented")
                        if (productListResp.isError) {
                        } else {
                            binding!!.rvProductList.setVisibility(View.VISIBLE)
                            categoriesAdapter!!.addItems(productListResp.data.data,1,sessionManager!!.getEcomOther())
                        }
                    }
                    override fun onError(e: Throwable) {
                        // Handle any error that occurs in the stream.
                        Timber.e(e,"onError: ")
                    }
                })
        }
        else {
        }
    }
    private fun findViewById() {
        rvProductList = binding!!.rvProductList
    }
    private fun initToolbar() {
        setSupportActionBar(binding!!.toolbarI.toolbar)
        binding!!.toolbarI.toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp)
        binding!!.toolbarI.toolbar.setNavigationOnClickListener { v -> onBackPressed() }
        binding!!.toolbarI.toolbar.setTitle(getString(R.string.shop))
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.title = getResources().getString(R.string.category)
        }
    }
    private fun initCategory() {
        binding!!.rvProductList.setLayoutManager(GridLayoutManager(this, 3))
        categoriesAdapter = ProductsAdapter(this, binding!!.rvProductList, false)
        binding!!.rvProductList.setAdapter(categoriesAdapter)
//        categoriesAdapter!!.setListener(( v: View?, position: Int, item: Product? ->
//            val returnIntent = Intent(
//                this,
//                ProductDetailsActivity::class.java)
//            returnIntent.putExtra(
//                CategoriesListActivity.SELECTED_CATEGORY,
//                categoriesAdapter!!.getItem(position)
//            )
//            setResult(RESULT_OK, returnIntent)
//            finish()
//        })


    }
    override fun onOptionsItemSelected(item:MenuItem):Boolean {
        if (item.itemId == android.R.id.home) {
            finish() // Close this activity and return to the previous activity
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}