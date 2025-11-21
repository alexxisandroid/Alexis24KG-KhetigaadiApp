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
import com.bumptech.glide.Glide;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.databinding.SearchByNameBinding;
import com.khetigaadi.room.repository.BrandRepository;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class GetImpBrandName extends Fragment {
    public static final String SELECTED_BRAND = "selectedBrand";
    private static final String TAG = GetImpBrandName.class.getSimpleName();
    private ImpBrandCompAdapter adapter;
    private String value;
    private JSONObject valueJson;
    private FragmentActivity activity;
    @Inject
    BrandRepository brandRepository;
    private SearchByNameBinding binding;
    @Override
    public void onAttach(@NonNull Context context) {
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
        binding = SearchByNameBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.llProgress.ivLoader);
        binding.edSearchTractorByBrand.setHint(getString(R.string.type_to_select_brand_name));
        try {
            valueJson = new JSONObject(value);
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        initAdaper();
        binding.edSearchTractorByBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after) {

            }
            @Override
            public void onTextChanged(CharSequence s,int start,int before,int count) {
                if (adapter != null)
                    adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
    private void initAdaper() {
        //Set layout manager to position the items
        binding.newTractorSearchRes.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new ImpBrandCompAdapter(new ArrayList<>());
        binding.newTractorSearchRes.setAdapter(adapter);
        adapter.setOnItemClickListener((v,position,brand) -> {
            try {
                if (valueJson.getString("selected").equals("1")) {
                    try {
                        JSONObject tractor = new JSONObject();
                        tractor.put("brandId", brand.getId());
                        tractor.put("brandName", brand.getName());
                        valueJson.put("imp1", tractor);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ((MainActivity) activity).hideSoftKeyboard();
                    GetImpTypeName fragment = new GetImpTypeName();
                    Bundle bundle = new Bundle();
                    bundle.putString("value", valueJson.toString());
                    fragment.setArguments(bundle);
                    FragmentManager mFragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.enter);
                    FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
                    mFragmentManager.popBackStack("CompareImpSelection", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(contentView.getId(), fragment).addToBackStack(TAG).commit();
                } else {
                    try {
                        JSONObject tractor = new JSONObject();
                        tractor.put("brandId", brand.getId());
                        tractor.put("brandName", brand.getName());
                        /* Temporarly commented by PRANAV
                        tractor.put("TypeId", valueJson.getJSONObject("imp1").getString("TypeId"));
                        tractor.put("typeName", valueJson.getJSONObject("imp1").getString("typeName"));*/
                        valueJson.put("imp2", tractor);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ((MainActivity) activity).hideSoftKeyboard();
                    //Temporaryly commented by PRANAV
                    //GetImpModelName fragment = new GetImpModelName();
                    GetImpTypeName fragment = new GetImpTypeName();
                    Bundle bundle = new Bundle();
                    bundle.putString("value", valueJson.toString());
                    fragment.setArguments(bundle);
                    FragmentManager mFragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.enter);
                    FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
                    mFragmentManager.popBackStack("CompareImpSelection", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(contentView.getId(), fragment).addToBackStack(TAG).commit();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        binding.newTractorSearchRes.setVisibility(View.GONE);
        binding.llProgress.llProgress.setVisibility(View.VISIBLE);
        try {
            if(valueJson.getString("selected").equals("1")) {
                brandRepository.getImplementBrands().observe(getViewLifecycleOwner(),brands -> {
                    try {
                        if(brands.size()>0) {
                            if(brands != null && brands.size() > 0) {
                                binding.llProgress.llProgress.setVisibility(View.GONE);
                                binding.newTractorSearchRes.setVisibility(View.VISIBLE);
                                adapter.setData(brands);
                            }
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                });

            } else {
                /* Temporarly commented by PRANAV
                brandRepository.getImplementBrandsByType(selectedTypeId).observe(getViewLifecycleOwner(), brands -> {
                    try {
                        if (brands.size() > 0) {
                            llProgress.setVisibility(View.GONE);
                            brandlistRecycleView.setVisibility(View.VISIBLE);
                            adapter.setData(brands);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });*/
                brandRepository.getImplementBrands().observe(getViewLifecycleOwner(),brands -> {
                    if (brands.size()>0) {
                        try {
                            if (brands != null && brands.size() > 0) {
                                binding.llProgress.llProgress.setVisibility(View.GONE);
                                binding.newTractorSearchRes.setVisibility(View.VISIBLE);
                                adapter.setData(brands);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
