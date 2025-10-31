package com.khetigaadi.tractor.newTractor.search;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.databinding.SearchByNameBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.newTractor.NewTractorDetails;
import com.khetigaadi.tractor.newTractor.model.NewTrSearchByNameResp;
import com.khetigaadi.tractor.newTractor.model.NewTractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import timber.log.Timber;
@AndroidEntryPoint
public class NewTractorSearchByName extends Fragment {
    private List<NewTractor> searchListRes;
    private NewTractorSearchResAdapter newTractorSearchResAdapter;
    private FragmentActivity activity;
    // Tag used to cancel the request
    private SearchByNameBinding binding;
    @Inject
    KhetiAPIs khetiAPIs;
    CompositeDisposable disposable = new CompositeDisposable();
    final BehaviorSubject<String> userInputSubject = BehaviorSubject.create();
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        binding = SearchByNameBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        if (activity != null) {
            ((MainActivity) activity).setToolbarTitle(getString(R.string.new_tractor));
            ((MainActivity) activity).showSoftKeyboard();
            Glide.with(activity)
                    .load(R.drawable.loader_white)
                    .into(binding.llProgress.ivLoader);
        }
        binding.edSearchTractorByBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2)
                    userInputSubject.onNext(binding.edSearchTractorByBrand.getText().toString());
            }
        });
        userInputSubject
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(String strings) {
                        getNewTractorSearchItem(strings);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        return view;
    }
    private void getNewTractorSearchItem(final String text) {
        binding.newTractorSearchRes.setVisibility(View.GONE);
        binding.llProgress.llProgress.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("search", text);
        khetiAPIs.searchNewTrByName(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<>() {
                    @Override
                    public void onSuccess(@NonNull NewTrSearchByNameResp myResponse) {
                        binding.newTractorSearchRes.setVisibility(View.VISIBLE);
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                        try {
                            if (!myResponse.isError()) {
                                searchListRes = myResponse.getData();
                                newTractorSearchResAdapter = new NewTractorSearchResAdapter(searchListRes);
                                binding.newTractorSearchRes.setAdapter(newTractorSearchResAdapter);
                                newTractorSearchResAdapter.setOnItemClickListener((position, v) -> {
                                    try {
                                        ((MainActivity) activity).hideSoftKeyboard();
                                        Intent intent = new Intent(activity, NewTractorDetails.class);
                                        intent.putExtra(NewTractorDetails.newTractorId, searchListRes.get(position).getId());
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });


                            } else {
                                Toast.makeText(activity, myResponse.getMsg().get(0), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("getNewTractorSearchItem Error: %s", e.getMessage());
                        binding.newTractorSearchRes.setVisibility(View.VISIBLE);
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                    }
                });
    }
}
