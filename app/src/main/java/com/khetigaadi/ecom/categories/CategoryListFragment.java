package com.khetigaadi.ecom.categories;
import static com.khetigaadi.base.util.StateData.Status.ERROR;
import static com.khetigaadi.base.util.StateData.Status.LOADING;
import static com.payu.custombrowser.util.CBConstant.SUCCESS;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.khetigaadi.R;
import com.khetigaadi.base.room.repository.utils.Status;
import com.khetigaadi.databinding.FragmentCategoryListBinding;
import com.khetigaadi.ecom.adapter.CategoriesAdapter;
import com.khetigaadi.room.repository.CategoryRepository;
import com.khetigaadi.shop.model.Category;
import com.khetigaadi.user.SessionManager;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class CategoryListFragment extends Fragment {
    public static final String SELECTED_CATEGORY = "selectedCategory";
    private FragmentCategoryListBinding binding;
    @Inject
    CategoryRepository categoryRepository;
    private CategoriesAdapter categoriesAdapter;
    @Inject
    SessionManager sessionManager;
    private FragmentActivity activity;
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentCategoryListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view,@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        initCategory();

//        categoryRepository.getAllCategories().observe(getViewLifecycleOwner(),listResource -> {
//            if (listResource.size()>0) {
//                binding.rvCategories.setVisibility(View.VISIBLE);
//                categoriesAdapter.addItems(listResource,1,sessionManager.getEcomOther());
//            }
//            else {
//                    binding.rvCategories.setVisibility(View.GONE);
//                    Toast.makeText(activity,getResources().getString(R.string.please_try_after_some_time),Toast.LENGTH_SHORT).show();
//            }
//        });
        categoryRepository.getAllCategories().observe(getViewLifecycleOwner(),listResource -> {
            if(!listResource.isEmpty()) {
                    binding.rvCategories.setVisibility(View.VISIBLE);
                    categoriesAdapter.addItems(listResource,1,sessionManager.getEcomOther());
            }
            else {
                binding.rvCategories.setVisibility(View.GONE);
                Toast.makeText(activity, getResources().getString(R.string.please_try_after_some_time),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initCategory() {
        binding.rvCategories.setLayoutManager(new GridLayoutManager(getContext(),3));
        categoriesAdapter = new CategoriesAdapter(getContext(),binding.rvCategories,false);
        binding.rvCategories.setAdapter(categoriesAdapter);
        categoriesAdapter.setListener((View v,int position,Category item) -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SELECTED_CATEGORY,categoriesAdapter.getItem(position));
            activity.setResult(Activity.RESULT_OK,returnIntent);
            activity.finish();
        });
    }
}