package com.khetigaadi.ecom.categories;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.khetigaadi.R;
import com.khetigaadi.databinding.ActivityCategoriesListBinding;
import com.khetigaadi.ecom.adapter.CategoriesAdapter;
import com.khetigaadi.retrofit.ShopAPIs;
import com.khetigaadi.room.repository.CategoryRepository;
import com.khetigaadi.shop.model.CategoryListResp;
import com.khetigaadi.shop.model.Category;
import com.khetigaadi.user.SessionManager;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class CategoriesListActivity extends AppCompatActivity {
    public static final String SELECTED_CATEGORY = "selectedCategory";
    private ActivityCategoriesListBinding binding;
    private CategoriesListViewModel viewModel;
    private CategoriesAdapter categoriesAdapter;
    private RecyclerView rvProductList;
    @Inject
    SessionManager sessionManager;
    @Inject
    CategoryRepository categoryRepository;
    @Inject
    ShopAPIs shopAPIs;
    String selectedCategory = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityCategoriesListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);
        viewModel = new ViewModelProvider(this).get(CategoriesListViewModel.class);
        initToolbar();
        findViewById();
        initCategory();
        categoryRepository.getAllCategories().observe(this,listResource -> {
            if (!listResource.isEmpty()) {
                binding.rvProductList.setVisibility(View.VISIBLE);
                categoriesAdapter.addItems(listResource, 1, sessionManager.getEcomOther());
            } else {
                binding.rvProductList.setVisibility(View.VISIBLE);
                //Toast.makeText(this, getResources().getString(R.string.please_try_after_some_time),Toast.LENGTH_SHORT).show();
                catList();
            }
        });


//      if(getIntent().hasExtra(SELECTED_CATEGORY)){
//          Category bv = getIntent().getParcelableExtra(SELECTED_CATEGORY);
//
//         // shopAPIs.getProduct( bv.getId())
//
//          shopAPIs.getProduct(bv.getId())
//                  .subscribeOn(Schedulers.io())
//                  .observeOn(AndroidSchedulers.mainThread())
//                  .subscribeWith(new DisposableSingleObserver<ProductResp>() {
//                      @Override
//                      public void onSuccess(@NonNull ProductResp productListResp) {
//                          if (productListResp.isError()) {
//
//                          }
//                          else {
//                              binding.rvProductList.setVisibility(View.VISIBLE);
//                              categoriesAdapter.addItems(productListResp.getData(),1,sessionManager.getEcomOther());
//                          }
//                      }
//                      @Override
//                      public void onError(@NonNull Throwable e) {
//                          Timber.e(e, "onError: ");
//                          //gardenTools.postError(e.getMessage());
//                      }
//                  });
//      }
//      else {
//
//      }
    }
    private void findViewById() {
        rvProductList = binding.rvProductList;
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbarI.toolbar);
        binding.toolbarI.toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        binding.toolbarI.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbarI.toolbar.setTitle(getString(R.string.category));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.shop));
        }
    }
    private void initCategory() {
        binding.rvProductList.setLayoutManager(new GridLayoutManager(this,3));
        categoriesAdapter = new CategoriesAdapter(this,binding.rvProductList,false);
        binding.rvProductList.setAdapter(categoriesAdapter);
        categoriesAdapter.setListener((View v,int position,Category item) -> {
            Intent returnIntent = new Intent(); //(this,ProductListActivity.class); //this,ProductActivity.class);
            returnIntent.putExtra(SELECTED_CATEGORY,categoriesAdapter.getItem(position));
           setResult(Activity.RESULT_OK,returnIntent);

//            Filter filter = new Filter();
//            List<Category> cvxcd = new ArrayList<>();
//            cvxcd.add(categoriesAdapter.getItem(position));
//            filter.setCategories(cvxcd);
//            returnIntent.putExtra(ProductListFilterActivity.FILTER,filter);

           Log.d("fgdsgddfg===",""+item.toString()+"=="+categoriesAdapter.getItem(position));
           // startActivity(returnIntent);

           finish();
        });
    }
    public void catList()
    {
        shopAPIs.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CategoryListResp>() {
                    @Override
                    public void onSuccess(@NonNull CategoryListResp productListResp) {
                        Log.d("cvxcxxcvxv234===",""+productListResp.isError()+"=="+productListResp.getCategories().size());
                        if (productListResp.isError()) {
                            binding.rvProductList.setVisibility(View.GONE);
                        }
                        else {
                            binding.rvProductList.setVisibility(View.VISIBLE);
                            Log.d("cvxcxxcvxv===",""+productListResp.getCategories().size());
                            categoryRepository.insertItems(productListResp.getCategories());
                            binding.rvProductList.setVisibility(View.VISIBLE);
                            categoriesAdapter.addItems(productListResp.getCategories(),1,sessionManager.getEcomOther());
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        binding.rvProductList.setVisibility(View.GONE);
                        //gardenTools.postError(e.getMessage());
                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}