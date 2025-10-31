package com.khetigaadi.harvester;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.harvester.model.HarvesterListResp;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
@HiltViewModel
public class HarvesterListViewModel extends ViewModel {
    private static final String TAG = "NewImplementListViewMod";
    private Filter filter;
    private final HarvesterRepository implementRepository;
    @Inject
    HarvesterListViewModel(KhetiAPIs khetiAPIs, SavedStateHandle savedStateHandle) {
        filter = savedStateHandle.get("filter");
        implementRepository = new HarvesterRepository(khetiAPIs);
    }
    public Filter getFilter() {
        return filter;
    }
    public StateMutableLiveData<HarvesterListResp> getImpList(int page) {
        TractorFilter tractorFilter = new TractorFilter();
        tractorFilter.setFilter(filter);
        return implementRepository.getHarvesterList(page, tractorFilter);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        implementRepository.onCleared();
    }
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
