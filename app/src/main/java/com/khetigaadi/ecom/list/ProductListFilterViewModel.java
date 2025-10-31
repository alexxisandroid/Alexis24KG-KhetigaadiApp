package com.khetigaadi.ecom.list;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.room.repository.CategoryRepository;
import com.khetigaadi.shop.model.Category;
import com.khetigaadi.shop.model.Filter;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProductListFilterViewModel extends ViewModel {
    private static final String TAG = "ProductListFilterViewMo";
    private final MutableLiveData<Filter> filter;
    private final CategoryRepository categoryRepository;


    StateMutableLiveData<List<Category>> categoryStateMutableLiveData = new StateMutableLiveData<>();

    @Inject
    ProductListFilterViewModel(CategoryRepository categoryRepository, SavedStateHandle savedStateHandle) {
        this.categoryRepository = categoryRepository;
        filter = savedStateHandle.getLiveData(ProductListFilterActivity.FILTER);
    }

    public Filter getFilter() {
        return filter.getValue();
    }

    public StateMutableLiveData<List<Category>> getCategoryStateMutableLiveData() {
        return categoryStateMutableLiveData;
    }

}
