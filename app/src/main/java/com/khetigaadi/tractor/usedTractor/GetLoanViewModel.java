package com.khetigaadi.tractor.usedTractor;
import android.net.Uri;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.usedTractor.model.GetLoan;
import com.khetigaadi.tractor.usedTractor.model.GetLoanResp;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
@HiltViewModel
public class GetLoanViewModel extends ViewModel {
    private Uri frontImage;
    private Uri backImage;
    KhetiAPIs khetiAPIs;
    CompositeDisposable disposable;
    GetLoan params = new GetLoan();
    @Inject
    GetLoanViewModel(KhetiAPIs khetiAPIs, SavedStateHandle savedStateHandle) {
        this.khetiAPIs = khetiAPIs;
        this.disposable = new CompositeDisposable();
    }
    public void setFrontImage(Uri uriFromBitmap) {
        frontImage = uriFromBitmap;
    }
    public void setBackImage(Uri uriFromBitmap) {
        backImage = uriFromBitmap;
    }
    public void setLoanAmount(String loanAmount) {
        params.setAmount(loanAmount);
    }
    public StateMutableLiveData<GetLoanResp> callGetLoanApi() {
        StateMutableLiveData<GetLoanResp> request = new StateMutableLiveData<>();
        request.postLoading();
        disposable.add(khetiAPIs.getLoanEnq(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetLoanResp>() {
                    @Override
                    public void onSuccess(@NonNull GetLoanResp getLoanResp) {
                        if (getLoanResp.isError())
                            request.postError(getLoanResp.getMsg().get(0));
                        else
                            request.postSuccess(getLoanResp);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        request.postError(e.getMessage());
                    }
                }));
        return request;
    }
    public Uri getFrontImage() {
        return frontImage;
    }
    public Uri getBackImage() {
        return backImage;
    }
    public String getLoanAmount() {
        return params.getAmount();
    }
    public void setTypeOfFinance(String financeType) {
        params.setType(financeType);
    }
    public String getFinanceType() {
        return params.getType();
    }
}
