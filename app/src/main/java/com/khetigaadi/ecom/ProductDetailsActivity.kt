package com.khetigaadi.ecom
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.khetigaadi.R
import com.khetigaadi.agri.activity.ImageViewActivity
import com.khetigaadi.base.model.MyResponse
import com.khetigaadi.base.util.StateData
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.databinding.ActivityProductDetailsBinding
import com.khetigaadi.ecom.adapter.AgricultureToolsAdapter
import com.khetigaadi.ecom.adapter.ProductImageSlideAdapter
import com.khetigaadi.ecom.cart.CartActivity
import com.khetigaadi.room.repository.CartRepository
import com.khetigaadi.shop.model.OrderProduct
import com.khetigaadi.shop.model.Product
import com.khetigaadi.shop.model.ProductImage
import com.khetigaadi.shop.model.ProductListResp
import com.khetigaadi.util.TextUtil
import com.khetigaadi.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject
@AndroidEntryPoint
class ProductDetailsActivity:CartBaseActivity() {
    @Inject
    lateinit var cartRepository:CartRepository
    private lateinit var handler:Handler
    private lateinit var animateViewPager:Runnable
    private val stopSliding = false
    private lateinit var binding:ActivityProductDetailsBinding
    private lateinit var viewModel:ProductDetailsViewModel
    private var quantity = 0
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        //WindowCompat.setDecorFitsSystemWindows(window,false)
        enableEdgeToEdge()
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
        viewModel = ViewModelProvider(this)[ProductDetailsViewModel::class.java]
        handler = Handler(Looper.getMainLooper())
        toolbarInit()
        setData()
        initAdapter()
        viewModel.quantity.observe(this) { integer:Int ->
            if(integer != 0) {
                quantity = integer
                binding.tvQuantity.text = String.format(Locale.US,"%d",integer)
                showBuyNow(false)
            }
            else {
                showBuyNow(true)
            }
        }
        binding.tvCommission.visibility = View.GONE
        binding.btnBuyNow.setOnClickListener { binding.btnAddToCart.callOnClick() }
        binding.ivShare.visibility = View.VISIBLE
        binding.ivShare.setOnClickListener(View.OnClickListener {
           // showProgressDialog(getString(R.string.please_wait))
            Firebase.dynamicLinks.shortLinkAsync {
                link = Uri.parse("https://www.khetigaadi.com/product/${viewModel.product!!.productId}")
                domainUriPrefix = getString(R.string.domain_uri_prefix)
                androidParameters { }
            }.addOnSuccessListener { (shortLink, _) ->
                try {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, viewModel.product!!.name)
                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.i_was_viewing) + "  \"" + viewModel.product!!.name + "\" " + " " + shortLink)
                    startActivity(Intent.createChooser(intent, "Select an action"))
                    dismissProgressDialog()
                } catch (ex: ActivityNotFoundException) {
                    // (handle error)
                    dismissProgressDialog()
                } catch (e: Exception) {
                    e.printStackTrace()
                    dismissProgressDialog()
                }
            }.addOnFailureListener {
                Timber.e(it)
                dismissProgressDialog()
            }
        })
    }
    private fun showBuyNow(visibility:Boolean) {
        if(visibility) {
            binding.btnBuyNow.visibility = View.VISIBLE
            binding.quantityGroup.visibility = View.GONE
        }
        else {
            binding.btnBuyNow.visibility = View.GONE
            binding.quantityGroup.visibility = View.VISIBLE
        }
    }
    private fun initUI(product:OrderProduct?) {
        binding.btnAddToCart.setOnClickListener {
            if (binding.btnAddToCart.text.toString() == getString(R.string.add_to_cart)) {
                val quantity = viewModel.quantity.value
                if (quantity != null) product!!.quantity = quantity

                if (product != null) {
                    println("dfdszxfvdfsf===="+""+product.product.toString())
                }
                else
                    println("dfdszxfvdfsf123313===="+""+product)

               cartRepository.insertItem(product)
               binding.btnAddToCart.setText(R.string.go_to_cart)
                Toast.makeText(this,"Added to Cart",Toast.LENGTH_SHORT).show()
            }
            else {
                startActivity(Intent(this,CartActivity::class.java))
            }
        }
        binding.btnExpertAdvice.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.customer_service_call))
                .setMessage(getString(R.string.you_will_get_a_call_from_our))
                .setNegativeButton(getString(R.string.cancel)) { dialog:DialogInterface, _:Int -> dialog.cancel() }
                .setPositiveButton(getString(R.string.ok)) { dialog:DialogInterface, _:Int ->
                    dialog.cancel()
                    callExpertApi()
                }.show()
        }
        viewModel.quantity.observe(this) { integer:Int? -> if (integer != null) binding.tvQuantity.text = String.format("%d", integer) }
        binding.tvPlus.setOnClickListener { viewModel.plusClick(quantity + 1) }
        binding.tvMinus.setOnClickListener { viewModel.minusClick(quantity - 1) }
    }
    private fun callExpertApi() {
        viewModel.onExpertAdviceClicked().observe(this) { myResponseStateData: StateData<MyResponse?> ->
            when (myResponseStateData.status) {
                StateData.Status.LOADING -> {
                   // showProgressDialog(getString(R.string.loading))
                }
                StateData.Status.SUCCESS, StateData.Status.ERROR -> {
                    dismissProgressDialog()
                    Toast.makeText(this@ProductDetailsActivity, myResponseStateData.error, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
    private fun insertItem(id:Int) {
        cartRepository.getItemByProductId(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : DisposableSingleObserver<OrderProduct>() {
                override fun onSuccess(orderProduct: OrderProduct) {
                    binding.btnAddToCart.setText(R.string.go_to_cart)
                    showBuyNow(false)
                }
                override fun onError(e: Throwable) {
                    Timber.e(e, "onError: ")
                    binding.btnAddToCart.setText(R.string.add_to_cart)
                    showBuyNow(true)
                }
            })
    }
    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
        viewModel.product?.let { insertItem(it.productId) }
        viewModel.onResume()
    }
    private fun initAdapter() {
        binding.include.rvProductList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val similarProductListAdapter = AgricultureToolsAdapter(this, binding.include.rvProductList)
        binding.include.rvProductList.adapter = similarProductListAdapter
        similarProductListAdapter.setOnItemClickListener { position: Int, _: View? ->
            val intent = Intent(this, ProductDetailsActivity::class.java)
           intent.putExtra("product", similarProductListAdapter.getItem(position))
            intent.putExtra("imagePath", similarProductListAdapter.others.shopProductImagePath)
            startActivity(intent)
        }
        viewModel.similarProducts.observe(this) { productListRespStateData: StateData<ProductListResp?> ->
            when (productListRespStateData.status) {
                StateData.Status.LOADING, StateData.Status.ERROR -> {
                    binding.include.rvProductList.visibility = View.GONE
                   // binding.tvSimilarProduct.visibility = View.GONE
                }
                StateData.Status.SUCCESS -> {
                    if (productListRespStateData.data != null) {
                        binding.include.rvProductList.visibility = View.VISIBLE
                       // binding.tvSimilarProduct.visibility = View.VISIBLE
                        similarProductListAdapter.addItems(productListRespStateData.data!!.data.data, 1, productListRespStateData.data!!.others)
                    }
                }
                else -> {}
            }
        }

    }
    private fun toolbarInit() {
        setSupportActionBar(binding.toolbars.toolbar)
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding.toolbars.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.toolbars.toolbar.title = getString(R.string.shop)
        binding.toolbars.toolbar.overflowIcon = ContextCompat.getDrawable(this,R.drawable.ic_baseline_more_vert_24)

        supportActionBar?.title =  getString(R.string.shop)
    }
    private fun setData() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.productDetails.observe(this) { orderProductStateData:StateData<OrderProduct?> ->
           println("xvcxvcxvcxvvxv==="+orderProductStateData.status)
            when(orderProductStateData.status) {
                StateData.Status.SUCCESS -> {
                    val product: Product
                    if (orderProductStateData.data != null) {
                        binding.progressBar.visibility = View.GONE
                        product = orderProductStateData.data!!.product
                        println("xvcxvcxvcxvvxv2321==="+orderProductStateData.data!!.product)
                        // binding.include.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                        // binding.include.findViewById<ProgressBar>(R.id.progressBar)
                        binding.clView.visibility = View.VISIBLE
                        if (product.images != null) {
                            setImages(product.images, viewModel.imagePath!!)
                            binding.toolbars.toolbar.title = product.name
                            binding.tvProductName.text = product.name
                            binding.tvDetails.text = Utils.fromHtml(product.description)
                            setPrice(product)
                            initUI(orderProductStateData.data)
                        }
                    }
                    else
                        binding.progressBar.visibility = View.GONE
                }
                StateData.Status.ERROR -> {
                 //   binding.include.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
                    binding.clView.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
    private fun setPrice(product:Product?) {
        binding.tvPrice.paintFlags = binding.tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.tvPrice.text = String.format("%s %s", getString(R.string.rs), Utils.priceConvert(product!!.price.toString() + ""))
        binding.tvDiscPrice.text = String.format("%s %s", getString(R.string.rs), Utils.priceConvert(product.finalPrice.toString() + ""))
        if(!TextUtil.isEmptyOrZero(product.discountPer)) {
            binding.priceGroup.visibility = View.VISIBLE
            binding.tvDiscPer.text =   String.format("%s%% %s",product.discountPer,getString(R.string.off))
            binding.tvDiscount.text =  String.format("%s%%",product.discountPer)
        }
        else {
            binding.priceGroup.visibility = View.INVISIBLE
        }
    }
    private fun setImages(images:MutableList<ProductImage>,imagePath:String) {
        if(images != null && images.isNotEmpty()) {
            binding.viewPager.adapter = ProductImageSlideAdapter(this@ProductDetailsActivity,images,imagePath) { baseUrl:String,productImages:List<ProductImage> ->
                val urls = ArrayList<String>()
                var baseUrlv = baseUrl
                if(baseUrl==null || baseUrl.equals(null) || baseUrl.equals("null") || baseUrl.isEmpty())
                    baseUrlv = "https://khetigaadi.com/images/shop_product/";
                for(image in productImages) {
                    urls.add(baseUrlv + image.image)
                }
                val intent = Intent(this@ProductDetailsActivity,ImageViewActivity::class.java)
                intent.putStringArrayListExtra("urls", urls)
                startActivity(intent)
            }
            binding.indicator.setViewPager(binding.viewPager)
            runnable(images.size)
            handler.postDelayed(animateViewPager,Constants.ANIM_VIEWPAGER_DELAY)
            binding.ivDefaultImage.visibility = View.GONE
        }
        else {
            binding.ivDefaultImage.visibility = View.VISIBLE
        }
    }
    fun runnable(size:Int) {
        animateViewPager = Runnable {
            if(!stopSliding) {
                if(binding.viewPager.currentItem == size - 1) {
                    binding.viewPager.currentItem = 0
                }
                else {
                    binding.viewPager.setCurrentItem(binding.viewPager.currentItem+1,true)
                }
                handler.postDelayed(animateViewPager,Constants.ANIM_VIEWPAGER_DELAY)
            }
        }
    }
}