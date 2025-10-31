package com.khetigaadi.tractor.compare;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
//import com.khetigaadi.base.model.Model;

//import com.khetigaadi.base.model.Model;
import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.base.room.repository.utils.Status;
import com.khetigaadi.databinding.SearchByNameBinding;
import com.khetigaadi.room.repository.ModelRepository;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by Irfan on 1/29/2016.
 */
@AndroidEntryPoint
public class GetModelName extends Fragment {
    private static final String GET_MODELS = "getModels";
    private ModelNameCompAdapter adapter;
    private String value;
    private JSONObject valueJson;
    private String brandId;
    private FragmentActivity activity;
    @Inject
    ModelRepository modelRepository;
    private Integer selectedModelId;
    private SearchByNameBinding binding;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            value = bundle.getString("value");
        }
        try {
            valueJson = new JSONObject(value);
            if (valueJson.getString("selected").equals("1")) {
                if (valueJson.has("tractor1")) {
                    brandId = valueJson.getJSONObject("tractor1").getString("brandId");
                    if (valueJson.has("tractor1") && valueJson.getJSONObject("tractor1").has("modelId"))
                        selectedModelId = Integer.parseInt(valueJson.getJSONObject("tractor2").getString("modelId"));

                }
            } else {
                if (valueJson.has("tractor2")) {
                    brandId = valueJson.getJSONObject("tractor2").getString("brandId");
                    if (valueJson.has("tractor1") && valueJson.getJSONObject("tractor1").has("modelId"))
                        selectedModelId = Integer.parseInt(valueJson.getJSONObject("tractor1").getString("modelId"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(value);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchByNameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.edSearchTractorByBrand.setHint(getString(R.string.type_to_select_tractor_model));
        initAdapter();
        binding.edSearchTractorByBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null)
                    adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void initAdapter() {
        binding.newTractorSearchRes.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new ModelNameCompAdapter(new ArrayList<>());
        binding.newTractorSearchRes.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position, model) -> {
            try {
                if (valueJson.getString("selected").equals("1")) {
                    JSONObject tractor = valueJson.getJSONObject("tractor1");
                    tractor.put("modelId", model.getId());
                    tractor.put("modelName", model.getName());
                    valueJson.put("tractor1", tractor);
                } else {
                    JSONObject tractor = valueJson.getJSONObject("tractor2");
                    tractor.put("modelId", model.getId());
                    tractor.put("modelName", model.getName());
                    valueJson.put("tractor2", tractor);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ((MainActivity) activity).hideSoftKeyboard();
            CompareTractorSelection fragment = new CompareTractorSelection();
            Bundle bundle = new Bundle();
            bundle.putString("value", valueJson.toString());
            fragment.setArguments(bundle);
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.enter);
//                                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            mFragmentManager.popBackStack("GetBrandName", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            mFragmentManager.popBackStack("MainActivity", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.replace(contentView.getId(), fragment).addToBackStack("GetModelName").commit();
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        binding.llProgress.llProgress.setVisibility(View.VISIBLE);
        modelRepository.getNewTractorModels(Integer.parseInt(brandId)).observe(this, listResource -> {
            if (listResource.size()>0) {
                List<Model> models = listResource;
                if (models != null && selectedModelId != null) {
                    models = models.stream().filter(c -> c.getId() != selectedModelId).collect(Collectors.toList());
                }
                adapter.setData(models);
            }
        });
    }
}
