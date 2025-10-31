package com.khetigaadi.mandi;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.model.State;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.mandi.model.MondiPriceResp;
import com.khetigaadi.retrofit.KhetiAPIs;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MandiViewModel extends ViewModel {
    private final MandiPriceRepository repository;
    private KhetiAPIs khetiAPIs;
    private String state;
    private String commodity;
    private State selectedState;

    @Inject
    public MandiViewModel(KhetiAPIs khetiAPIs, SavedStateHandle savedStateHandle) {
        this.khetiAPIs = khetiAPIs;
        repository = new MandiPriceRepository(khetiAPIs);
    }

    public StateMutableLiveData<MondiPriceResp> getMandiPrice(int page) {
        int offset = page * 10;
        return repository.getMandiPrice(offset, selectedState != null ?
                selectedState.getName() : null, commodity);
    }

    public void setState(State selectedState) {
        this.selectedState = selectedState;
    }
}