package com.khetigaadi.implement.compare;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.SearchByNameBinding;
import com.khetigaadi.implement.model.Model;
import com.khetigaadi.implement.model.ModelResp;
import com.khetigaadi.implement.newImplement.util.Type;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.ModelRepository;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class GetImpModelName extends Fragment {
    private static final String TAG = GetImpModelName.class.getSimpleName();
    private ImpModelCompAdapter adapter;
    private String value;
    private JSONObject valueJson;
    private FragmentActivity activity;
    private int selectedBrandId;
    private int selectedTypeId;
    @Inject
    ModelRepository modelRepository;
    @Inject
    KhetiAPIs khetiAPIs;
    @Inject
    CompositeDisposable disposable;
    private Integer selectedModelId;
    private SearchByNameBinding binding;
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            activity = getActivity();
            Bundle bundle = getArguments();
            if (bundle != null) {
                value = bundle.getString("value");
            }
            valueJson = new JSONObject(value);
            Log.d("cvcvcvdgg1234====",""+valueJson.getString("selected").equals("1")+"=="+valueJson);
            if (valueJson.getString("selected").equals("1")) {
                JSONObject tractor = valueJson.getJSONObject("imp1");
                selectedBrandId = tractor.getInt("brandId");
                selectedTypeId = tractor.getInt("TypeId");
                if(valueJson.has("imp2") && valueJson.getJSONObject("imp2").has("modelId"))
                    selectedModelId = Integer.parseInt(valueJson.getJSONObject("imp2").getString("modelId"));
            } else {
                JSONObject tractor = valueJson.getJSONObject("imp2");
                selectedBrandId = tractor.getInt("brandId");
                selectedTypeId = tractor.getInt("TypeId");
                if(valueJson.has("imp1") && valueJson.getJSONObject("imp1").has("modelId"))
                    selectedModelId = Integer.parseInt(valueJson.getJSONObject("imp1").getString("modelId"));
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        binding = SearchByNameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.edSearchTractorByBrand.setHint(getString(R.string.type_to_select_tractor_model));
        if (Constants.isConnectionAvailable(activity)) {
            getModels();
        } else {
            ((MainActivity) activity).noInternetConnection();
        }
        initAdapter();
        return view;
    }
    private void initAdapter() {
        //Set layout manager to position the items
        binding.newTractorSearchRes.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        adapter = new ImpModelCompAdapter(new ArrayList<>());
        binding.newTractorSearchRes.setAdapter(adapter);
        adapter.setOnItemClickListener((v,position,model) -> {
            try {
                if (valueJson.getString("selected").equals("1")) {
                    JSONObject tractor = valueJson.getJSONObject("imp1");
                    tractor.put("modelId",model.getId());
                    tractor.put("modelName",model.getName());
                    valueJson.put("imp1",tractor);
                }
                else {
                    JSONObject tractor = valueJson.getJSONObject("imp2");
                    tractor.put("modelId",model.getId());
                    tractor.put("modelName",model.getName());
                    valueJson.put("imp2",tractor);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            ((MainActivity) activity).hideSoftKeyboard();
            CompareImpSelection fragment = new CompareImpSelection();
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
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void getModels() {
        try {
            Brand jsonObjectBrandId = new Brand();
            jsonObjectBrandId.setId(selectedBrandId);
            Type jsonObjectImplementType = new Type();
            jsonObjectImplementType.setId(selectedTypeId);
            Map<String, Object> params = new HashMap<>();
            params.put("brand", jsonObjectBrandId);
            params.put("implement_type", jsonObjectImplementType);
            disposable.add(khetiAPIs.getImpModels(params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<ModelResp>() {
                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ModelResp modelResp) {
                            List<Model> models = modelResp.getData();
                            if (selectedModelId != null) {
                                models = models.stream().filter(c -> c.getId() != selectedModelId).collect(Collectors.toList());
                            }
                            adapter.setData(models);
                            //Log.d("cvxxcvcxcxc1234====",""+modelResp.getData().get(0));
                        }
                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Log.d("cvxxcvcxcxc====",""+e.getLocalizedMessage());
                            Timber.e(e);
                        }
                    }));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
