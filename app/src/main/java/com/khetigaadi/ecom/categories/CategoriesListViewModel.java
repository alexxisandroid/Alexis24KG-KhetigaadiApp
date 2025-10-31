package com.khetigaadi.ecom.categories;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.retrofit.ShopAPIs;
import com.khetigaadi.room.repository.CategoryRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
@HiltViewModel
public class CategoriesListViewModel extends ViewModel {
    private static final String TAG = "CategoriesListViewModel";
    private final ShopAPIs shopAPIs;
    private final CategoryRepository categoryRepository;
    @Inject
    public CategoriesListViewModel(ShopAPIs shopAPIs,CategoryRepository categoryRepository,SavedStateHandle savedStateHandle) {
        this.shopAPIs = shopAPIs;
        this.categoryRepository = categoryRepository;
    }
}
