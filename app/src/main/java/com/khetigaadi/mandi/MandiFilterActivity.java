package com.khetigaadi.mandi;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.khetigaadi.R;
import com.khetigaadi.databinding.ActivityMandiFilterBinding;
import com.khetigaadi.ecom.list.ListFilterCategoryItemAdapter;
import com.khetigaadi.ecom.list.ProductListFilterViewModel;
import com.khetigaadi.room.repository.BrandRepository;
import com.khetigaadi.room.repository.CategoryRepository;
import com.khetigaadi.shop.model.Category;
import com.khetigaadi.shop.model.Filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class MandiFilterActivity extends AppCompatActivity {
    public static final String FILTER = "filter";
    private static final String TAG = com.khetigaadi.ecom.list.ProductListFilterActivity.class.getSimpleName();
    private ListFilterCategoryItemAdapter mBrandNameAdapter;
    @Inject
    BrandRepository brandRepository;
    private ActivityMandiFilterBinding binding;
    private ProductListFilterViewModel viewModel;
    private List<Category> list;
    private Filter filter;
    @Inject
    CategoryRepository categoryRepository;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ActivityMandiFilterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        viewModel = new ViewModelProvider(this).get(ProductListFilterViewModel.class);
        initToolbar();
        filter = viewModel.getFilter();
        mBrandNameAdapter = new ListFilterCategoryItemAdapter();
        // set the adapter object to the Recyclerview
        binding.rvCategories.setAdapter(mBrandNameAdapter);
        binding.btnClearAll.setOnClickListener(v -> {

        });
        binding.btnApplyFilter.setOnClickListener(rippleView -> {

        });
        categoryRepository.getAllCategories().observe(this, listStateData -> {
            if (listStateData.size()>0) {
                try {
                    List<Category> brandArray1 = new ArrayList<>();
                    for (Iterator<Category> it = listStateData.iterator(); it.hasNext(); ) {
                        Category item = it.next();
                        item.setSelected(filter.getCategories() != null && filter.getCategories().contains(item));
                        brandArray1.add(item);
                    }
                    mBrandNameAdapter.updateItems(brandArray1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.shop));
    }
}
