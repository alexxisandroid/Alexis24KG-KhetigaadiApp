package com.khetigaadi.implement.newImplement;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.implement.newImplement.util.ImplementListResp;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
@HiltViewModel
public class NewImplementListViewModel extends ViewModel {
    private static final String TAG = "NewImplementListViewMod";
    private Filter filter;
    private final ImplementRepository implementRepository;
    @Inject
    NewImplementListViewModel(KhetiAPIs khetiAPIs,  SavedStateHandle savedStateHandle) {
        filter = savedStateHandle.get("filter");
        implementRepository = new ImplementRepository(khetiAPIs);
    }
    public Filter getFilter() {
        return filter;
    }
    public StateMutableLiveData<ImplementListResp> getImpList(int page) {
        TractorFilter tractorFilter = new TractorFilter();
        tractorFilter.setFilter(filter);
        return implementRepository.getImpList(page, tractorFilter);
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
