package com.khetigaadi.tractor.usedTractor.search;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
//import com.khetigaadi.base.model.Brand;
 import com.khetigaadi.implement.newImplement.util.Brand;
//import com.khetigaadi.base.model.Model;

import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.databinding.SearchByNameBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.usedTractor.UsedTractorsListFragment;
import com.khetigaadi.tractor.usedTractor.util.UsedTrByName;
import java.util.Collections;
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
@AndroidEntryPoint
public class UsedTractorSearchByName extends Fragment {
    private static final String TAG = UsedTractorSearchByName.class.getSimpleName();
    private UsedTractorSearchResAdapter newTractorSearchResAdapter;
    private FragmentActivity activity;
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
        binding = SearchByNameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        if (activity != null) {
            ((MainActivity) activity).setToolbarTitle(getString(R.string.old_tractor));
            Glide.with(activity)
                    .load(R.drawable.loader_white)
                    .into(binding.llProgress.ivLoader);
        }
        ((MainActivity) activity).showSoftKeyboard();
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
                    userInputSubject.onNext(binding.edSearchTractorByBrand.getText().toString().trim());
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
                        getUsedTractorSearchItem(strings);
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
    private void getUsedTractorSearchItem(final String text) {
        binding.newTractorSearchRes.setVisibility(View.GONE);
        binding.llProgress.llProgress.setVisibility(View.VISIBLE);
        khetiAPIs.searchOldTrByName(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<>() {
                    @Override
                    public void onSuccess(@NonNull UsedTrByName searchListRes) {
                        binding.newTractorSearchRes.setVisibility(View.VISIBLE);
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                        try {
                            if (!searchListRes.isError()) {
                                newTractorSearchResAdapter = new UsedTractorSearchResAdapter(searchListRes.getTractors());
                                binding.newTractorSearchRes.setAdapter(newTractorSearchResAdapter);
                                newTractorSearchResAdapter.setOnItemClickListener((position, v) -> {
                                    ((MainActivity) activity).hideSoftKeyboard();
                                    getList(searchListRes.getTractors().get(position).getBrandId(), searchListRes.getTractors().get(position).getModelId());
                                });
                            } else {
                                for (int i = 0; i < searchListRes.getMsg().size(); i++) {
                                    Toast.makeText(activity, searchListRes.getMsg().get(i), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(activity, getString(R.string.something_went_wrong
                        ), Toast.LENGTH_LONG).show();
                        binding.newTractorSearchRes.setVisibility(View.VISIBLE);
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                    }
                });
    }
    private void getList(String brandId,String selectedModel) {
        try {
            Filter filter = new Filter();
            if (brandId != null && brandId.length() > 0) {
                Brand brand = new Brand();
                brand.setId(Integer.parseInt(String.valueOf(Integer.parseInt(brandId))));
                filter.setBrands(Collections.singletonList(brand));
                if (selectedModel != null) {
                    filter.setModels(Collections.singletonList(new Model(Integer.parseInt(selectedModel), "")));
                }
            }
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            fragmentTransaction.replace(contentView.getId(), UsedTractorsListFragment.getInstance(filter)).addToBackStack(TAG).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
