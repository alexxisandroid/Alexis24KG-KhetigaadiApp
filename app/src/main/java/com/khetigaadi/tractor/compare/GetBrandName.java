package com.khetigaadi.tractor.compare;
import static com.khetigaadi.base.util.StateData.Status.ERROR;
import static com.khetigaadi.base.util.StateData.Status.LOADING;
import static com.khetigaadi.base.util.StateData.Status.SUCCESS;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.khetigaadi.AppController;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.base.room.repository.utils.Status;
import com.khetigaadi.databinding.SearchByNameBinding;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.BrandRepository;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class GetBrandName extends Fragment {
    private static final String TAG = GetBrandName.class.getSimpleName();
    private BrandName1CompAdapter adapter;
    private String value;
    private JSONObject valueJson;
    private FragmentActivity activity;
    @Inject
    BrandRepository brandRepository;
    private SearchByNameBinding binding;
    @Inject
    KhetiAPIs khetiAPIs;
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            value = bundle.getString("value");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        binding = SearchByNameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.llProgress.ivLoader);
        binding.edSearchTractorByBrand.setHint(getString(R.string.type_to_select_brand_name));
        initAdapter();
        binding.edSearchTractorByBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s,int start,int before,int count) {
                adapter.getFilter().filter(s);
                System.out.println("addTextChangedListener");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
    private void initAdapter() {
        //Set layout manager to position the items*/
        binding.newTractorSearchRes.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL, false));
        adapter = new BrandName1CompAdapter(new ArrayList<>());
        binding.newTractorSearchRes.setAdapter(adapter);
        adapter.setOnItemClickListener((v,position,brand) -> {
            try {
                valueJson = new JSONObject(value);
                JSONObject tractor = new JSONObject();
                tractor.put("brandId",brand.getId());
                tractor.put("brandName",brand.getName());
                if (valueJson.getString("selected").equals("1")) {
                    valueJson.put("tractor1",tractor);
                } else {
                    valueJson.put("tractor2",tractor);
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
            ((MainActivity) activity).hideSoftKeyboard();
            GetModelName fragment = new GetModelName();
            Bundle bundle = new Bundle();
            bundle.putString("value",valueJson.toString());
            fragment.setArguments(bundle);
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.enter);
            FrameLayout contentView = (FrameLayout)activity.findViewById(R.id.containerView);
            mFragmentManager.popBackStack("CompareTractorSelection", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.replace(contentView.getId(),fragment).addToBackStack("GetBrandName").commit();
        });
    }
    @Override
    public void onStart() {
        super.onStart();
//        brandRepository.getNewTractorBrands().observe(getViewLifecycleOwner(),brands -> {
//            switch (brands.getStatus()) {
//                case LOADING:
//                    binding.newTractorSearchRes.setVisibility(View.GONE);
//                    binding.llProgress.llProgress.setVisibility(View.VISIBLE);
//                    break;
//                case SUCCESS:
//                    if (brands.getData() != null && brands.getData().size() > 0) {
//                        binding.llProgress.llProgress.setVisibility(View.GONE);
//                        binding.newTractorSearchRes.setVisibility(View.VISIBLE);
//                        adapter.setData(brands.getData());
//                    }
//                    break;
//                case ERROR:
//                    Timber.e("onStart: %s",""+brands.getMessage());
//                    break;
//            }
//        });
        brandRepository.getNewTractorBrands().observe(getViewLifecycleOwner(),new Observer<List<Brand>>() {
            @Override
            public void onChanged(@Nullable List<Brand> brands) {
                try {
                    assert brands != null;
                    if(!brands.isEmpty()) {
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                        binding.newTractorSearchRes.setVisibility(View.VISIBLE);
                        adapter.setData(brands);
                    }
                    else {

                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    private void getBrands() {
//        khetiAPIs.getNewTractorModels()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ data: AllBannerResp ->
//        if (!data.isError) {
//            pref.setValue(Constants.APP_BANNER_KEY, Gson().toJson(data))
//        }
//            }
//            ) { error: Throwable ->
//                Timber.e(error.message,"onFailure: ")
//        }
//    }

}
