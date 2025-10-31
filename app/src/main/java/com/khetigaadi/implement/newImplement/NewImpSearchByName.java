package com.khetigaadi.implement.newImplement;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.SearchByNameBinding;
import com.khetigaadi.implement.newImplement.util.ImplementList;
import com.khetigaadi.retrofit.KhetiAPIs;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import timber.log.Timber;
@AndroidEntryPoint
public class NewImpSearchByName extends Fragment {
    private static final String TAG = NewImpSearchByName.class.getSimpleName();
    private SearchResAdapter adapter;
    private FragmentActivity activity;
    private SearchByNameBinding binding;
    @Inject
    KhetiAPIs khetiAPIs;
    @Inject
    CompositeDisposable disposable;
    private BehaviorSubject _textInput;
    private Flowable<String> textInput;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
    {
        binding = SearchByNameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        activity = getActivity();
        binding.edSearchTractorByBrand.setHint(R.string.type_to_select_implement_name);
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.llProgress.ivLoader);
        ((MainActivity) activity).showSoftKeyboard();
        _textInput = BehaviorSubject.create();
        textInput = _textInput.toFlowable(BackpressureStrategy.LATEST);
        disposable.add(
                textInput
                        .debounce(1000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::getNewImpSearchItem));
        binding.edSearchTractorByBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println(binding.edSearchTractorByBrand.getText());
                if (s.length() > 2)
                    if (Constants.isConnectionAvailable(activity)) {
                        _textInput.onNext(binding.edSearchTractorByBrand.getText().toString());
                    } else {
                        ((MainActivity) activity).noInternetConnection();
                    }

            }
        });

        return view;
    }
    private void getNewImpSearchItem(final String text) {
        binding.newTractorSearchRes.setVisibility(View.GONE);
        binding.llProgress.llProgress.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("search", text);
        disposable.add(khetiAPIs.newImpSearch(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ImplementList>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ImplementList implementList) {
                        try {
                            binding.newTractorSearchRes.setVisibility(View.VISIBLE);
                            binding.llProgress.llProgress.setVisibility(View.GONE);
                            //Set layout manager to position the items
                            binding.newTractorSearchRes.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                            adapter = new SearchResAdapter(implementList.getImplementse());
                            binding.newTractorSearchRes.setAdapter(adapter);
                            adapter.setOnItemClickListener((position, v) -> {
                                ((MainActivity) activity).hideSoftKeyboard();
                                Intent intent = new Intent(activity, NewImpOverviewDetails.class);
                                intent.putExtra("brandId", implementList.getImplementse().get(position).getBrandId());
                                intent.putExtra("typeId", implementList.getImplementse().get(position).getTypeId());
                                intent.putExtra("modelId", implementList.getImplementse().get(position).getModelId());
                                startActivity(intent);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Timber.e("Error: %s", e.getMessage());
                        binding.newTractorSearchRes.setVisibility(View.VISIBLE);
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                    }
                }));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
