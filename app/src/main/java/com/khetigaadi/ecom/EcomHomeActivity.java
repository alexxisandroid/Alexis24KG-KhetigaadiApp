package com.khetigaadi.ecom;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.khetigaadi.BuildConfig;
import com.khetigaadi.R;
import com.khetigaadi.base.WebViewActivity;
import com.khetigaadi.base.adapter.TwoBannerSlideAdapter;
import com.khetigaadi.base.model.AllBannerResp;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityEcomHomeBinding;
import com.khetigaadi.ecom.adapter.HomeCategoriesAdapter;
import com.khetigaadi.ecom.adapter.HomeListAdapter;
import com.khetigaadi.ecom.categories.CategoriesListActivity;
import com.khetigaadi.ecom.list.ProductListActivity;
import com.khetigaadi.ecom.list.ProductListFilterActivity;
import com.khetigaadi.model.Others;
import com.khetigaadi.room.repository.CategoryRepository;
import com.khetigaadi.shop.model.Category;
import com.khetigaadi.shop.model.Filter;
import com.khetigaadi.shop.model.Product;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.util.TextUtil;
import com.khetigaadi.util.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import kotlin.jvm.JvmField;
@AndroidEntryPoint
public class EcomHomeActivity extends CartBaseActivity {
    private ActivityEcomHomeBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable animateViewPager;
    private boolean stopSliding;
    private EComHomeViewModel viewModel;
    @Inject
    SessionManager sessionManager;
    @Inject
    CategoryRepository categoryRepository;
    private HomeCategoriesAdapter categoriesAdapter;
    ActivityResultLauncher<Intent> categoryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        try {
                            navigateToProductList(data.getParcelableExtra(CategoriesListActivity.SELECTED_CATEGORY));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
   // @Inject
  //  CategoryRepository categoryRepository;
    @JvmField
    public AllBannerResp allBanner = new AllBannerResp();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEcomHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(),(v,insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
//        EdgeToEdge.enable(this);
        viewModel = new ViewModelProvider(this).get(EComHomeViewModel.class);
        runnableS();
        initToolbar();
        initUI();
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.shop));
        binding.toolbars.toolbar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.ic_baseline_more_vert_24));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.shop));
    }
    private void initUI() {
        initBannerAdapter();
        initCategory();
        initHomeProducts();
     //   binding.myStickyButton.setVisibility(View.VISIBLE);
      //  binding.myStickyButton.startShimmer();
        categoryRepository.getAllCategories().observe(this,listStateData -> {
            if(listStateData.size()>0) {
                    binding.rvCategories.setVisibility(View.VISIBLE);
                List<Category> categories = new ArrayList<>();
                    for(int i=0;i<listStateData.size();i++) {
                        Category category = listStateData.get(i);
//                        if(category.getName().equals("Agriculture Drone")) {
//                            category.setImage("drone.svg");
//                            category.colorCode = category.colorCode;
//                        }
                        categories.add(category);
                    }
                   // List<Category> categories = listStateData;
                    Category allCategory = new Category();
                    allCategory.setName("All");
                    //allCategory.setImage("all-apps-icon.png");
                    allCategory.setImage("lady_bug1.png");
                    allCategory.colorCode = "#3498db";
                    if (categories != null) {
                        categories.add(0,allCategory);
                    }
                    categoriesAdapter.setOthers(sessionManager.getEcomOther());
                    categoriesAdapter.updateItems(categories);
            }
            else {
                binding.rvCategories.setVisibility(View.GONE);
               // Toast.makeText(this,getResources().getString(R.string.please_try_after_some_time),Toast.LENGTH_SHORT).show();
            }
        });
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                    if(item.getItemId() == R.id.action_home) {
                        finish();
                        return true;
                    }
                    else if(item.getItemId() == R.id.action_category) {
                        categoryActivityResultLauncher.launch(new Intent(EcomHomeActivity.this, CategoriesListActivity.class));
                        return false;
                    }
                    else if(item.getItemId() == R.id.action_my_orders) {
                        navigateToMyOrders();
                        return false;
                    }
                    else if(item.getItemId() == R.id.action_cart) {
                        navigateToCart();
                        return false;
                    }
                    else
                      return true;
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        invalidateOptionsMenu();
    }
    private void initHomeProducts() {
        binding.rvHomeList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        HomeListAdapter agricultureToolsAdapter = new HomeListAdapter(this,binding.rvHomeList);
        binding.rvHomeList.setAdapter(agricultureToolsAdapter);
        binding.rvHomeList.setNestedScrollingEnabled(false);
        agricultureToolsAdapter.setOnItemClickListener(new HomeListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position1,int position,View v,Product product) {
                navigateToDetails(product,agricultureToolsAdapter.getOthers().getShopProductImagePath());
            }
            @Override
            public void onSeeMoreClick(int position,View v) {
                navigateToProductList(agricultureToolsAdapter.getItem(position));
            }
        });
        viewModel.getHomeProducts().observe(this,productListRespStateData -> {
            switch(productListRespStateData.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                case ERROR:
                    binding.rvHomeList.setVisibility(View.GONE);
                    binding.groupTrending.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.GONE);
                    // binding.myStickyButton.setVisibility(View.GONE);
                    // binding.myStickyButton.stopShimmer();
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    if(productListRespStateData.getData() != null) {
                        binding.rvHomeList.setVisibility(View.VISIBLE);
                        // productListRespStateData.getData().
                        Others others = sessionManager.getEcomOther();
                        if(others != null && productListRespStateData.getData().getTrendingProduct().getShopProductImagePath() != null) {
                            others.setShopProductImagePath(productListRespStateData.getData().getTrendingProduct().getShopProductImagePath());
                            Log.d("vbcxcvxvxvxcv====",""+productListRespStateData.getData().getTrendingProduct().getShopProductImagePath());
                            sessionManager.setEcomOther(others);
                            agricultureToolsAdapter.addItems(productListRespStateData.getData().getData(),1,others);
                            binding.groupTrending.setVisibility(View.VISIBLE);
                            initTrendingProducts(productListRespStateData.getData().getTrendingProduct().getTrendingProducts(),others);
                        }
                    }
                    break;
            }
        });
    }
    private void navigateToDetails(Product product,String imagePath) {
        Intent intent = new Intent(this,ProductDetailsActivity.class);
        intent.putExtra("product",product);
        intent.putExtra("imagePath",imagePath);
        startActivity(intent);
    }
    private void initTrendingProducts(List<Product> products,Others others) {
       binding.progressBar.setVisibility(View.VISIBLE);
        if(products.size() > 0) {
            Product item = products.get(0);
            binding.includeTrendingLarge.tvTitle.setText(item.getName());
            String spath = others.getShopProductImagePath();
            if(spath==null || spath.equalsIgnoreCase(null) || spath.equalsIgnoreCase("null") || spath.isEmpty())
                spath = BuildConfig.BASE_URL+"images/shop_product/";
       //  Log.d("zxcxczcxzcxzcz===",""+spath+item.getImage().getImage());
            Glide.with(this)
                    //.load(others.getShopProductImagePath() + item.getImage().getImage())
                    .load(spath+item.getImage().getImage())
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image).into(binding.includeTrendingLarge.image);
            binding.includeTrendingLarge.getRoot().setOnClickListener(v -> navigateToDetails(products.get(0), others.getShopProductImagePath()));
            binding.includeTrendingLarge.tvPrice.setPaintFlags(binding.includeTrendingLarge.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.includeTrendingLarge.tvPrice.setText(String.format("%s %s",getString(R.string.rs),Utils.priceConvert(item.getPrice() + "")));
            binding.includeTrendingLarge.tvDiscPrice.setText(String.format("%s %s",getString(R.string.rs),Utils.priceConvert(item.getFinalPrice() + "")));

           binding.llincludeTrendingLarge.setVisibility(View.VISIBLE);
            if(!TextUtil.isEmptyOrZero(item.getDiscountPer())) {
                binding.includeTrendingLarge.priceGroup.setVisibility(View.VISIBLE);
               // binding.includeTrendingLarge.tvDiscPer.setText(String.format("%s%% %s",item.getDiscountPer(),getString(R.string.off)));

                binding.includeTrendingLarge.tvDiscount.setText(String.format("%s%% %s",item.getDiscountPer(),getString(R.string.off)));

            }
            else
                binding.includeTrendingLarge.priceGroup.setVisibility(View.INVISIBLE);
        }
        if(products.size() > 1) {
            Product item = products.get(1);
            binding.includeTrending1.tvTitle.setText(item.getName());
            String spath =   others.getShopProductImagePath();
            if(spath==null || spath.equalsIgnoreCase(null) || spath.equalsIgnoreCase("null") || spath.equalsIgnoreCase("null") || spath.isEmpty())
                spath = "https://khetigaadi.com/images/shop_product/";
            Glide.with(this)
                    //.load(others.getShopProductImagePath() + item.getImage().getImage())
                    .load(spath+item.getImage().getImage())
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image).into(binding.includeTrending1.image);
            binding.includeTrending1.getRoot().setOnClickListener(v -> navigateToDetails(products.get(1), others.getShopProductImagePath()));
            binding.includeTrending1.tvPrice.setPaintFlags(binding.includeTrending1.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.includeTrending1.tvPrice.setText(String.format("%s %s",getResources().getString(R.string.rs),Utils.priceConvert(item.getPrice() + "")));
            binding.includeTrending1.tvDiscPrice.setText(String.format("%s %s", getString(R.string.rs), Utils.priceConvert(item.getFinalPrice() + "")));
            if(!TextUtil.isEmptyOrZero(item.getDiscountPer())) {
                binding.includeTrending1.tvDiscPer.setVisibility(View.VISIBLE);
               // binding.includeTrending1.tvDiscPer.setText(String.format("%s%% %s", item.getDiscountPer(), getString(R.string.off)));

                binding.includeTrending1.tvDiscount.setText(String.format("%s%% %s",item.getDiscountPer(), getString(R.string.off)));

            } else
                binding.includeTrending1.tvDiscPer.setVisibility(View.INVISIBLE);
        }
        if(products.size() > 2) {
            Product item = products.get(2);
            binding.includeTrending2.tvTitle.setText(item.getName());
            String spath =   others.getShopProductImagePath();
            if (spath==null || spath.equalsIgnoreCase(null) || spath.equalsIgnoreCase("null") || spath.equalsIgnoreCase("null") || spath.isEmpty())
                spath = BuildConfig.BASE_URL+"images/shop_product/";
            Glide.with(this)
                   // .load(others.getShopProductImagePath() + item.getImage().getImage())
                    .load(spath+item.getImage().getImage())
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image).into(binding.includeTrending2.image);
            binding.includeTrending2.getRoot().setOnClickListener(v -> navigateToDetails(products.get(2), others.getShopProductImagePath()));
            binding.includeTrending2.tvPrice.setPaintFlags(binding.includeTrending2.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.includeTrending2.tvPrice.setText(String.format("%s %s",getResources().getString(R.string.rs),Utils.priceConvert(item.getPrice() + "")));
            binding.includeTrending2.tvDiscPrice.setText(String.format("%s %s", getString(R.string.rs), Utils.priceConvert(item.getFinalPrice() + "")));
            if(!TextUtil.isEmptyOrZero(item.getDiscountPer())) {
                binding.includeTrending2.tvDiscPer.setVisibility(View.VISIBLE);
               // binding.includeTrending2.tvDiscPer.setText(String.format("%s%% %s",item.getDiscountPer(), getString(R.string.off)));

                binding.includeTrending2.tvDiscount.setText(String.format("%s%% %s",item.getDiscountPer(), getString(R.string.off)));

            }
            else
                binding.includeTrending2.tvDiscPer.setVisibility(View.INVISIBLE);
        }
       binding.progressBar.setVisibility(View.GONE);
    }
    private void initCategory() {
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        categoriesAdapter = new HomeCategoriesAdapter(this,binding.rvCategories);
        binding.rvCategories.setAdapter(categoriesAdapter);
        categoriesAdapter.setListener((View v,int position,Category item) -> {
            if(position != 0) {
                navigateToProductList(categoriesAdapter.getItem(position));
            }
            else {
               // Log.d("xcvcvcxvcxvxvx21===",""+categoriesAdapter.getItem(position)+"=="+categoriesAdapter.getItem(position).getName());
                categoryActivityResultLauncher.launch(new Intent(EcomHomeActivity.this,CategoriesListActivity.class));
               // Log.d("xcvcvcxvcxvxvx11===",""+categoriesAdapter.getItem(position).getImage().length()+"=="+categoriesAdapter.getItem(position).getId());
            }
        });
    }
    private void navigateToProductList(Category category) {
        Intent intent = new Intent(EcomHomeActivity.this,ProductListActivity.class);
        Filter innerFilter = new Filter();
        List<Category> catt= new ArrayList<>();
        catt.add(category);
        //innerFilter.setCategories(Collections.singletonList(category));
        innerFilter.setCategories(catt);
        intent.putExtra(ProductListFilterActivity.FILTER,innerFilter);
        startActivity(intent);
       // categoryActivityResultLauncher.launch(intent);
    }
    private void initBannerAdapter() {
        AllBannerResp allBanner = new Gson().fromJson(SharedPreferenceUtils.getInstance(this).getStringValue(Constants.APP_BANNER_KEY,"{}"),AllBannerResp.class);
        if(allBanner != null && allBanner.getBanners() != null && allBanner.getBanners().getShop() != null) {
            binding.viewPager.setAdapter(new TwoBannerSlideAdapter(this, allBanner.getBanners().getShop()));
            binding.viewPager.setClipToPadding(false);
            binding.viewPager.setPadding(60, 0, 60, 0);
            binding.viewPagerIndicator.setViewPager(binding.viewPager);
            runnable(allBanner.getBanners().getShop().size());
            //Re-run callback
            handler.postDelayed(animateViewPager, Constants.ANIM_VIEWPAGER_DELAY);
        }
        if(allBanner != null && allBanner.getOthers() != null && allBanner.getOthers().isShopAdStatus()) {
            Glide.with(this)
                    .load(allBanner.getOthers().getShopImagePath())
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image)
                    .into(binding.ivShopMiddleBanner);
            binding.shopMiddleBanner.setVisibility(View.VISIBLE);
            binding.shopMiddleBanner.setOnClickListener(v -> {
                viewModel.onBannerClicked();
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.TITLE, getString(R.string.shop));
                intent.putExtra(WebViewActivity.URL, allBanner.getOthers().getShopAdUrl());
                startActivity(intent);
            });
        }
        else {
            binding.shopMiddleBanner.setVisibility(View.GONE);
        }
    }
    public void runnable(final int size) {
        animateViewPager = () -> {
            if (!stopSliding) {
                if(binding.viewPager.getCurrentItem() == size - 1) {
                    binding.viewPager.setCurrentItem(0);
                }
                else {
                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem()+1,true);
                }
                handler.postDelayed(animateViewPager,Constants.ANIM_VIEWPAGER_DELAY);
            }
        };
    }
    public void runnableS() {
      binding.myStickyButton.setVisibility(View.GONE);
      Handler  handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                binding.myStickyButton.setVisibility(View.GONE);
                handler.postDelayed(this,7000);
            }
        };
        handler.postDelayed(r,7000);
    }
    public void setToolbarTitle(String title) {
        binding.toolbars.toolbar.setTitle(getResources().getString(R.string.shop));
    }
}