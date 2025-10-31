package com.khetigaadi.tractor.dealer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.dealer.model.DealerListResp;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
@HiltViewModel
public class DealerListViewModel extends ViewModel {
    private final DealersRepository dealersRepository;
    private Filter filter;
    @Inject
    public DealerListViewModel(KhetiAPIs khetiAPIs,SavedStateHandle savedStateHandle) {
        dealersRepository = new DealersRepository(khetiAPIs);
        filter = savedStateHandle.contains("filter") ? savedStateHandle.get("filter") : new Filter();
    }
    public Filter getFilter() {
        return filter;
    }
    public StateMutableLiveData<DealerListResp> getDealers(int page) { ;
        TractorFilter usedTraFilter=new TractorFilter(filter);
        usedTraFilter.setFilter(filter);
        return dealersRepository.getDealers(page, usedTraFilter);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        dealersRepository.onCleared();
    }
    public void setFilter(com.khetigaadi.tractor.newTractor.model.Filter filter) {
        this.filter = filter;
    }
}
