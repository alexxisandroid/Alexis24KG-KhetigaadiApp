package com.khetigaadi.implement.compare;
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
import com.khetigaadi.base.room.repository.utils.Status;
import com.khetigaadi.databinding.SearchByNameBinding;
import com.khetigaadi.room.repository.TypeRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class GetImpTypeName extends Fragment {
    private static final String TAG = GetImpBrandName.class.getSimpleName();
    private ImpTypeCompAdapter adapter;
    private String value;
    private JSONObject valueJson;
    private FragmentActivity activity;
    private String brandId;
    @Inject
    TypeRepository typeRepository;
    private SearchByNameBinding binding;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            value = bundle.getString("value");
        }
        try {
            valueJson = new JSONObject(value);
            if (valueJson.getString("selected").equals("1")) {
                if (valueJson.has("imp1")) {
                    brandId = valueJson.getJSONObject("imp1").getString("brandId");
                }
            } else {
                if (valueJson.has("imp2")) {
                    brandId = valueJson.getJSONObject("imp2").getString("brandId");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(value);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        binding = SearchByNameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.edSearchTractorByBrand.setHint(getString(R.string.type_to_select_type));
        initAdapter();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        typeRepository.getTypeByBrandId(Integer.parseInt(brandId)).observe(getViewLifecycleOwner(), types -> {
            if (types.size()>0) {
                if (adapter != null) {
                    adapter.setData(types);
                }
            }
        });
    }
    private void initAdapter() {
        //Set layout manager to position the items
        binding.newTractorSearchRes.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new ImpTypeCompAdapter(new ArrayList<>());
        binding.newTractorSearchRes.setAdapter(adapter);
        adapter.setOnItemClickListener((v,position,type) -> {
            try {
                if (valueJson.getString("selected").equals("1")) {
                    JSONObject tractor = valueJson.getJSONObject("imp1");
                    tractor.put("TypeId", type.getId());
                    tractor.put("typeName", type.getName());
                    valueJson.put("imp1", tractor);
                } else {
                    JSONObject tractor = valueJson.getJSONObject("imp2");
                    tractor.put("TypeId", type.getId());
                    tractor.put("typeName", type.getName());
                    valueJson.put("imp2", tractor);
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
            ((MainActivity) activity).hideSoftKeyboard();
            GetImpModelName fragment = new GetImpModelName();
            Bundle bundle = new Bundle();
            bundle.putString("value", valueJson.toString());
            fragment.setArguments(bundle);
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.enter);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            mFragmentManager.popBackStack("GetImpBrandName", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            mFragmentManager.popBackStack("GetImpTypeName", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            mFragmentManager.popBackStack("MainActivity", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.replace(contentView.getId(), fragment).addToBackStack(TAG).commit();
        });
        binding.edSearchTractorByBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after) {

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
    }
}
