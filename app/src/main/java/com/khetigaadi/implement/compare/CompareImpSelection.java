package com.khetigaadi.implement.compare;
import android.content.Context;
import android.os.Bundle;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.R;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.CompareImpSelectionBinding;
import com.khetigaadi.implement.compare.adapter.ComparePopularImplementAdapter;
import com.khetigaadi.implement.compare.model.CompareImplementListResp;
import com.khetigaadi.implement.compare.model.GetImpByModelReq;
import com.khetigaadi.implement.compare.model.ImplementModel;
import com.khetigaadi.implement.compare.model.ImplementsInfo;
import com.khetigaadi.implement.model.ImplementDetailsResp;
import com.khetigaadi.implement.newImplement.util.Type;
import com.khetigaadi.retrofit.KhetiAPIs;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class CompareImpSelection extends Fragment {
    private FragmentActivity activity;
    private String value;
    private JSONObject json;
    private final String TAG = CompareImpSelection.class.getSimpleName();
    @Inject
    Tracker mTracker;
    @Inject
    SharedPreferenceUtils pref;
    private int pageCount = 1;
    @Inject
    KhetiAPIs khetiAPIs;
    private ComparePopularImplementAdapter comparePopularImplementAdapter;
    // private NestedScrollView mScrollView;
    private CompareImpSelectionBinding binding;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            value = bundle.getString("value");
        }
        try {
            if (value == null) {
                json = new JSONObject();
            } else {
                json = new JSONObject(value);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("requesrJson=" + value);
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CompareImpSelectionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        if (value == null) {
            binding.llTractor1.setVisibility(View.GONE);
            binding.llTractor2.setVisibility(View.GONE);
            binding.llTractor1Empty.setVisibility(View.VISIBLE);
            binding.llTractor2Empty.setVisibility(View.VISIBLE);
        } else {
            try {
                if (json.has("imp1")) {
                    JSONObject tractor1Json = json.getJSONObject("imp1");
                    if (tractor1Json.has("brandId") && tractor1Json.has("TypeId") && tractor1Json.has("modelId")) {
                        getImpImage(tractor1Json.getString("brandId"), tractor1Json.getString("TypeId"), tractor1Json.getString("modelId"), 1);
                        if (tractor1Json.has("brandName") && tractor1Json.has("typeName")) {
                            binding.tvTractor1Name.setText(tractor1Json.getString("brandName").concat(" " + tractor1Json.getString("typeName")));
                            if (tractor1Json.has("modelName")) {
                                binding.tvModel1.setText(tractor1Json.getString("modelName"));
                            }
                        }
                    }
                } else {
                    binding.llTractor1.setVisibility(View.GONE);
                    binding.llTractor1Empty.setVisibility(View.VISIBLE);
                }
                if (json.has("imp2")) {
                    JSONObject tractor1Json = json.getJSONObject("imp2");
                    if (tractor1Json.has("brandId") && tractor1Json.has("TypeId") && tractor1Json.has("modelId")) {
                        getImpImage(tractor1Json.getString("brandId"), tractor1Json.getString("TypeId"), tractor1Json.getString("modelId"), 2);
                        if (tractor1Json.has("brandName") && tractor1Json.has("typeName")) {
                            binding.tvTractor2Name.setText(tractor1Json.getString("brandName").concat(" " + tractor1Json.getString("typeName")));
                            if (tractor1Json.has("modelName")) {
                                binding.tvModel2.setText(tractor1Json.getString("modelName"));
                            }
                        }
                    }
                } else {
                    binding.llTractor2.setVisibility(View.GONE);
                    binding.llTractor2Empty.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        comparePopularImplementAdapter = new ComparePopularImplementAdapter(activity, binding.rvPopularImpl);
        binding.rvPopularImpl.setAdapter(comparePopularImplementAdapter);
        comparePopularImplementAdapter.setOnItemClickListener((position, v) -> {
            CompareImp fragment = new CompareImp();
            pageCount = 1;
            Bundle bundle = new Bundle();
            JSONObject tr1JsonObj = new JSONObject();
            JSONObject tr2JsonObj = new JSONObject();
            try {
                tr1JsonObj.put("brand_id", comparePopularImplementAdapter.getItem(position).getFirstImplement().getImplBrandId());
                tr1JsonObj.put("Type_id", comparePopularImplementAdapter.getItem(position).getFirstImplement().getImplTypeId());
                tr1JsonObj.put("model_id", comparePopularImplementAdapter.getItem(position).getFirstImplement().getImplModelId());
                tr2JsonObj.put("brand_id", comparePopularImplementAdapter.getItem(position).getSecondImplement().getImplBrandId());
                tr2JsonObj.put("Type_id", comparePopularImplementAdapter.getItem(position).getSecondImplement().getImplTypeId());
                tr2JsonObj.put("model_id", comparePopularImplementAdapter.getItem(position).getSecondImplement().getImplModelId());
                JSONObject jsonObjectTractorComp = new JSONObject();
                jsonObjectTractorComp.put("first_implement", tr1JsonObj);
                jsonObjectTractorComp.put("secound_implement", tr2JsonObj);
                JSONObject postJsonObject = new JSONObject();
                postJsonObject.put("implements", jsonObjectTractorComp);
                bundle.putString("value", postJsonObject.toString());
                fragment.setArguments(bundle);
                FragmentManager mFragmentManager = activity.getSupportFragmentManager();
                if (!fragment.isAdded()) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
                    fragmentTransaction.replace(contentView.getId(), fragment).addToBackStack(TAG).commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        comparePopularImplementAdapter.setOnLoadMoreListener(this::getPopularImpComparison);
        binding.cvTractor1.setOnClickListener(v -> {
            try {
                json.put("imp1", new JSONObject());
                json.put("selected", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GetImpBrandName fragment = new GetImpBrandName();
            Bundle bundle = new Bundle();
            bundle.putString("value", json.toString());
            fragment.setArguments(bundle);
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.enter);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            fragmentTransaction.replace(contentView.getId(), fragment).addToBackStack(TAG).commit();
        });
        binding.cvTractor2.setOnClickListener(v -> {
            try {
                json.put("imp2", new JSONObject());
                json.put("selected", "2");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GetImpBrandName fragment = new GetImpBrandName();
            Bundle bundle = new Bundle();
            bundle.putString("value", json.toString());
            fragment.setArguments(bundle);
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.enter);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            mFragmentManager.popBackStack("GetModelName", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.replace(contentView.getId(), fragment).addToBackStack(TAG).commit();
        });
        binding.btnCompare.setOnRippleCompleteListener(rippleView -> {
            if (json.has("imp1") && json.has("imp2")) {
                CompareImp fragment = new CompareImp();
                Bundle bundle = new Bundle();
                JSONObject tr1JsonObj = new JSONObject();
                JSONObject tr2JsonObj = new JSONObject();
                try {
                    pageCount = 1;
                    tr1JsonObj.put("brand_id", json.getJSONObject("imp1").getString("brandId"));
                    tr1JsonObj.put("Type_id", json.getJSONObject("imp1").getString("TypeId"));
                    tr1JsonObj.put("model_id", json.getJSONObject("imp1").getString("modelId"));

                    tr2JsonObj.put("brand_id", json.getJSONObject("imp2").getString("brandId"));
                    tr2JsonObj.put("Type_id", json.getJSONObject("imp2").getString("TypeId"));
                    tr2JsonObj.put("model_id", json.getJSONObject("imp2").getString("modelId"));
                    JSONObject jsonObjectImp = new JSONObject();
                    jsonObjectImp.put("first_implement", tr1JsonObj);
                    jsonObjectImp.put("secound_implement", tr2JsonObj);
                    JSONObject postJsonObject = new JSONObject();
                    postJsonObject.put("implements", jsonObjectImp);
                    bundle.putString("value", postJsonObject.toString());
                    fragment.setArguments(bundle);
                    FragmentManager mFragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
                    fragmentTransaction.replace(contentView.getId(), fragment).addToBackStack(TAG).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, getResources().getString(R.string.please_select_implement_for_compare), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        pageCount = 1;
        getPopularImpComparison(pageCount);
    }
    private void getPopularImpComparison(int page) {
        // Tag used to cancel the request
        if (page == 1) binding.llProgressComTractors.setVisibility(View.VISIBLE);
        khetiAPIs.getCompareImplements(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<>() {
                    @Override
                    public void onSuccess(@NonNull CompareImplementListResp myResponse) {
                        binding.llProgressComTractors.setVisibility(View.GONE);
                        try {
                            if (!myResponse.isError()) {
                                comparePopularImplementAdapter.addItems(myResponse.getData().getImplement(), page, myResponse.getOthers());
                            } else {
                                for (int i = 0; i < myResponse.getMsg().size(); i++) {
                                    Toast.makeText(activity, myResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("getNewTractorSearchItem Error: %s", e.getMessage());
                    }
                });
    }
    private void getImpImage(final String brandId, final String typeId, final String modelId, final int i) {
        Brand brand = new Brand();
        brand.setId(Integer.parseInt(brandId));
        ImplementModel model = new ImplementModel();
        model.setImplementModelId(Integer.parseInt(modelId));
        Type type = new Type();
        type.setId(Integer.parseInt(typeId));
        ImplementsInfo implementsInfo = new ImplementsInfo(brand, model, type);
        khetiAPIs.getImpByBrandTypeModel(new GetImpByModelReq(implementsInfo))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<>() {
                    @Override
                    public void onSuccess(@NonNull ImplementDetailsResp myResponse) {
                        try {
                            if (!myResponse.isError()) {
                                if (i == 1) {
                                    for (int j = 0; j < 1; j++) {
                                        Glide.with(activity)
                                                .load(Constants.decodeURL(myResponse.getOthers().getImplementImagePath() + myResponse.getData().get(j).getImplementImage().getImage()))
                                                .placeholder(R.mipmap.no_image)
                                                .error(R.mipmap.no_image)
                                                .into(binding.image);
                                    }
                                } else {
                                    for (int j = 0; j < 1; j++) {
                                        Glide.with(activity)
                                                .load(Constants.decodeURL(myResponse.getOthers().getImplementImagePath() + myResponse.getData().get(j).getImplementImage().getImage()))
                                                .placeholder(R.mipmap.no_image)
                                                .error(R.mipmap.no_image)
                                                .into(binding.image1);
                                    }
                                }
                            } else {
                                for (int j = 0; j < myResponse.getMsg().size(); j++) {
                                    Toast.makeText(activity, myResponse.getMsg().get(j), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("getNewTractorSearchItem Error: %s", e.getMessage());
                    }
                });
    }
}

