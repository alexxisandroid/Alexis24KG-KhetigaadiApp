package com.khetigaadi.ecom.list;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.khetigaadi.base.model.Price;
import com.khetigaadi.databinding.ActivityProductListFilterBinding;
import com.khetigaadi.room.repository.BrandRepository;
import com.khetigaadi.room.repository.CategoryRepository;
import com.khetigaadi.shop.model.Category;
import com.khetigaadi.shop.model.Filter;
import com.khetigaadi.util.NumberFormatting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class ProductListFilterActivity extends AppCompatActivity {
    public static final String FILTER = "filter";
    private static final String TAG = ProductListFilterActivity.class.getSimpleName();
    private ListFilterCategoryItemAdapter mBrandNameAdapter;
    @Inject
    BrandRepository brandRepository;
    private ActivityProductListFilterBinding binding;
    private ProductListFilterViewModel viewModel;
    private List<Category> list;
    private Filter filter;
    @Inject
    CategoryRepository categoryRepository;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityProductListFilterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        Log.d("bvcbcbvbcv22====","");
        viewModel = new ViewModelProvider(this).get(ProductListFilterViewModel.class);
        initToolbar();
        filter = viewModel.getFilter();

       try {
           if(filter.getPrice() != null && ((float) filter.getPrice().getPriceFrom() / 1000)<((float) filter.getPrice().getPriceTo()/ 1000))
               binding.rangeBarPrice.setValues((float) filter.getPrice().getPriceFrom() / 1000, (float) filter.getPrice().getPriceTo() / 1000);
           else
               binding.rangeBarPrice.setValues(1f, 100f);
           binding.tvMinPrice.setText(String.format("%s%s", getString(R.string.rs), NumberFormatting.format((long) (binding.rangeBarPrice.getValues().get(0) * 1000))));
           binding.tvMaxPrice.setText(String.format("%s%s", " - " + getString(R.string.rs), NumberFormatting.format((long) (binding.rangeBarPrice.getValues().get(1) * 1000))));
           binding.rangeBarPrice.addOnChangeListener((slider, value, fromUser) -> {
               binding.tvMinPrice.setText(String.format("%s%s", getString(R.string.rs), NumberFormatting.format((long) (slider.getValues().get(0) * 1000))));
               binding.tvMaxPrice.setText(String.format("%s%s", getString(R.string.rs), NumberFormatting.format((long) (slider.getValues().get(1) * 1000))));
//                Timber.e("onRangeChangeListener: leftPinValue= " + leftPinValue + " rightPinValue= " + rightPinValue);
           });
           binding.rangeBarPrice.setLabelFormatter(value -> String.format("%s%s", getString(R.string.rs), NumberFormatting.format((long) (value * 1000))));
           mBrandNameAdapter = new ListFilterCategoryItemAdapter();
           // set the adapter object to the Recyclerview
           binding.rvCategories.setAdapter(mBrandNameAdapter);
       }
       catch(Exception e) {
           e.printStackTrace();
       }

        binding.btnClearAll.setOnClickListener(v -> {
           // binding.rangeBarPrice.setValues(1f, 150f);
         try {
             float value = binding.rangeBarPrice.getValueTo();
             if (value >= binding.rangeBarPrice.getValueFrom() && value <= binding.rangeBarPrice.getValueTo()) {
                 binding.rangeBarPrice.setValues(value);
             } else {
                 binding.rangeBarPrice.setValues(binding.rangeBarPrice.getValueTo()); // or handle gracefully
             }
         }
         catch(Exception e) {
             e.printStackTrace();
         }
                mBrandNameAdapter.clearSelection();
        });
        binding.btnApplyFilter.setOnClickListener(rippleView -> {
            try {
                filter.setPrice(new Price((int) (binding.rangeBarPrice.getValues().get(0)*1000),(int)(binding.rangeBarPrice.getValues().get(1)*1000)));
                filter.setCategories(mBrandNameAdapter.getSelectedItems());
                Intent returnIntent = new Intent();
                returnIntent.putExtra(FILTER,filter);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        categoryRepository.getAllCategories().observe(this,listStateData -> {
            Log.d("bvcbcbvbcv====",""+listStateData.size());
            if (listStateData.size()>0) {
                try {
                    List<Category> brandArray1 = new ArrayList<>();
                    for(Iterator<Category> it = listStateData.iterator(); it.hasNext(); ) {
                        Category item = it.next();
                        item.setSelected(filter.getCategories() != null && filter.getCategories().contains(item));
                        brandArray1.add(item);
                    }
                    mBrandNameAdapter.updateItems(brandArray1);
                }
                catch(Exception e) {
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
