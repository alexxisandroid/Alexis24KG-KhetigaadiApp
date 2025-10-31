package com.khetigaadi.tractor.newTractor.search;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

//import com.khetigaadi.base.model.Brand;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.retrofit.KhetiAPIs;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
@HiltViewModel
public class SearchNewTractorFilterViewModel extends ViewModel {
    private final KhetiAPIs khetiAPIs;
    private final MutableLiveData<Brand> selectedBrand = new MutableLiveData<>();
    @Inject
    SearchNewTractorFilterViewModel(KhetiAPIs khetiAPIs,SavedStateHandle savedStateHandle) {
        this.khetiAPIs = khetiAPIs;
    }
    public MutableLiveData<Brand> getSelectedBrand() {
        return selectedBrand;
    }
    public void setSelectedBrand(Brand selectedBrand) {
        this.selectedBrand.setValue(selectedBrand);
    }
}
