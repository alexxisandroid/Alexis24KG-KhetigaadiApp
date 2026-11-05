package com.khetigaadi.tractor.compare;
import android.annotation.SuppressLint;
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

import com.khetigaadi.AppConfig;
import com.khetigaadi.BuildConfig;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.model.ModelResp;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.databinding.SearchByNameBinding;
import com.khetigaadi.room.repository.ModelRepository;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;
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
    @Inject
    KhetiAPIs khetiAPIs;
    String image;
    List<Model> cvxxccxVal= new ArrayList<>();
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            value = bundle.getString("value");
            Log.d("cvbcvbcvbcvbcvbc===",""+value);
            image = bundle.getString("image");
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
                    if (valueJson.has("tractor1") && valueJson.getJSONObject("tractor2").has("modelId"))
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
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
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
        binding.newTractorSearchRes.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL, false));
        adapter = new ModelNameCompAdapter(new ArrayList<>());
        binding.newTractorSearchRes.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position, model) -> {
            try {
                if (valueJson.getString("selected").equals("1")) {
                    JSONObject tractor = valueJson.getJSONObject("tractor1");
                    tractor.put("modelId", model.getId());
                    tractor.put("modelName", model.getName());
                    tractor.put("image",image);
                    valueJson.put("tractor1", tractor);
                } else {
                    JSONObject tractor = valueJson.getJSONObject("tractor2");
                    tractor.put("modelId", model.getId());
                    tractor.put("modelName", model.getName());
                    tractor.put("image",image);
                    valueJson.put("tractor2", tractor);
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
//            Runnable runnable = new Runnable(){
//                public void run() {
//                    //some code here
////                    try {
////                      //  if(valueJson.has("tractor1"))
////                         //   getImageAndHp( valueJson.getJSONObject("tractor1").getString("brandId"),""+model.getId(),1);
////                      //  else if(valueJson.has("tractor2"))
////                           // getImageAndHp( valueJson.getJSONObject("tractor2").getString("brandId"),""+model.getId(),2);
////                    } catch (Exception e) {
////                        throw new RuntimeException(e);
////                    }
//
//                }
//            };
//            Thread thread = new Thread(runnable);
           // thread.start();

            ((MainActivity) activity).hideSoftKeyboard();
            CompareTractorSelection fragment = new CompareTractorSelection();
            Bundle bundle = new Bundle();
            bundle.putString("value", valueJson.toString());
            bundle.putString("image",image);
            fragment.setArguments(bundle);
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.enter);
  //        fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            mFragmentManager.popBackStack("GetBrandName",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            mFragmentManager.popBackStack("MainActivity",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.replace(contentView.getId(),fragment).addToBackStack("GetModelName").commit();

        });
    }
    @SuppressLint("FragmentLiveDataObserve")
    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
       // cvxxccxVal.clear();
        binding.llProgress.llProgress.setVisibility(View.VISIBLE);
        modelRepository.getNewTractorModels(Integer.parseInt(brandId)).observe(getViewLifecycleOwner(),listResource -> {
            Log.d("vcbcvbcvbcvb4142342====",""+brandId+"=="+listResource.size()+"=="+adapter.getItemCount()+"=="+listResource.size());
            if(listResource.size()>0) {
                List<Model> models = listResource;
                if(models != null && selectedModelId != null) {
                    models = models.stream().filter(c -> c.getId() != selectedModelId).collect(Collectors.toList());
                }
                adapter.setData(models);
            }
            else getModelss(brandId);
        });
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    void getModelss(String brand_idV) {
        if (cvxxccxVal.size()>0)
            return;
        binding.llProgress.llProgress.setVisibility(View.VISIBLE);
        khetiAPIs.getNewTractorModels(Integer.parseInt(brand_idV))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ModelResp>() {
                    @Override
                    public void onSuccess(@NotNull ModelResp safetyTipsResp) {
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                        List<Model> cvxxccx = safetyTipsResp.getData().getData();
                        Log.d("vbcbcv===",""+cvxxccx.size());
                       if(cvxxccx.size()>0){
                           List<Model> models = cvxxccx;
                           cvxxccxVal.addAll(cvxxccx);
                           if (models != null && selectedModelId != null) {
                               models = models.stream().filter(c -> c.getId() != selectedModelId).collect(Collectors.toList());
                           }
                           adapter.setData(models);
                           for(int i=0;i<cvxxccx.size();i++) {
                               Model mdl = cvxxccx.get(i);
                               modelRepository.deleteModel(mdl);
                               modelRepository.insertModel(mdl);
                           }
                       }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                       // binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
                });
    }
    private void getImageAndHp(final String brandId,final String modelId,final int ii)
    {
        Log.d("cbvbcbvcbcv1====",""+brandId+"=="+modelId+"=="+ii);
        SessionManager sessionManager = new SessionManager(activity);
        User usrr = sessionManager.getUser();
        String usrrToken = usrr.getToken();
        if(usrrToken==null || usrrToken.equals(null))
            usrrToken="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI3IiwianRpIjoiMTgyMzNiNmNmMGI0ZDllZWI1OWQ5YmEyM2E2MjEwNWZjZGIzOGU5MGIzZjkxYjlhZDE0YjdiMDJlNWNjOTM5ZTI0NTgyOWJmYWYyMzVjZjMiLCJpYXQiOjE3NTY1NDg5MTksIm5iZiI6MTc1NjU0ODkxOSwiZXhwIjoxNzg4MDg0OTE5LCJzdWIiOiIxMDE2NDk3Iiwic2NvcGVzIjpbXX0.Sdk0CZeX5I1m3P2fqsvolpyxIybtjPpkgjBvKQ_GYsrU-vxIYAE-JN2_V2JUcHKSaGkgARD55OtZcsfwiszyGdJYpjz-d-am4KnS3ph_0I_4MWhXbcbsleN9hapf0idNsxJ-kDEjh8xSBdfSj3FAPcAd-fnP7iB_ni_QKiXJjTR6SucaPU4ZXKVIrs4iIXGx_-hjzloNHuIY0TaJhsIGBXLfx1jE37c3sCt7rttWqkPpVOT3eIBA2-6t4Ur7xl68CGk1cuwlg1SrfF9xev4QgxvrjPfEzb4ozCqOyBF-YEdTHNyXFB-6ba5JmY7k2roGBMwEqnqj7eWISY6gQdknZzFFAFlirCiKg2tiWhdyeFxwRFbf7dzEbL8NOBm4T9J-tpzisTiuTlnkr5U93hci5TYcUXV1IzotMUbBeK7gBbzn_NQhNPGbqmo7aIeJYp83TCdAVyvNf6x4NUrFBTqBg9nlRS4twRxo8OU0IdOS629oVnPZKn5jdLGHYkGZZMpREaTHeWp4AiUrLjVFPJy-hyPNYr1INayZnEWjorPlvOyHyqib846a9yaSJ9oFf33uKmOLXYwDyybkMewq2bnt0upwESrPI9fxrzyVY89LpDx2FjcABFaglUimzPw5qKWr2un0CSVwPjJzF-Iq5ctcG7WbADVj_eH_-IBubI0X9-g";
        Log.d("cbvbcbvcbcv====",""+usrr);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"filter\":{\n\t\"brands\":[\n            {\n                \"brand_id\": \""+brandId+"\"\n            }\n        ],\n        \"tractor_models\":[\n            {\n                \"tractor_model_id\": \""+modelId+"\"\n            }\n        ]\n    },\n    \"perPage\":12\n}");
        Request request = new Request.Builder()
                .url(BuildConfig.BASE_URL+"mobile/api/v1/tractor/new/filter")
                .method("POST", body)
                .addHeader("Content-Type","application/json")
                .addHeader("Accept","application/json")
                .addHeader("Authorization",usrrToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if(response.isSuccessful()) {
                String path = AppConfig.BASE_ASSET_URL+"new-tractor/";
                String vbnbvnv = response.body().string();
                Log.d("cxvcvcxvxcvx===",""+vbnbvnv);
                JSONObject jsonObject= new JSONObject(vbnbvnv);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                JSONArray jsonArray = jsonObject1.getJSONArray("data");
                String tractor_image = jsonArray.getJSONObject(0).getJSONObject("tractor_image").getString("tractor_image_name");
                image = tractor_image;

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        ((MainActivity) activity).hideSoftKeyboard();
                        CompareTractorSelection fragment = new CompareTractorSelection();
                        Bundle bundle = new Bundle();
                        bundle.putString("value", valueJson.toString());
                        bundle.putString("image",image);
                        fragment.setArguments(bundle);
                        FragmentManager mFragmentManager = activity.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.enter);
                        //  fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
                        FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
                        mFragmentManager.popBackStack("GetBrandName",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        mFragmentManager.popBackStack("MainActivity",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.replace(contentView.getId(),fragment).addToBackStack("GetModelName").commit();

                    }
                });

//                for (int i=0;i<jsonArray.length();i++) {
//                    String brand_id = jsonArray.getJSONObject(i).getString("brand_id");
//                    String tractor_model_id = jsonArray.getJSONObject(i).getString("tractor_model_id");
//                    // Log.d("cxvcxcxvcxvcxvcxvcx===",""+(brand_id.equals(brandId) && Objects.equals(modelId,tractor_model_id)));
//                    Log.d("cxvcxvcxvcxvcxc====",""+brand_id+"=="+brandId+"=="+modelId+"=="+tractor_model_id);
//                    if(brand_id.equals(brandId) && tractor_model_id.equals(modelId)) {  // Objects.equals(modelId,tractor_model_id)
//                        if(ii==1) {
//                            String exact_hp = jsonArray.getJSONObject(i).getString("exact_hp");
//                          //  String tractor_image = jsonArray.getJSONObject(i).getJSONObject("tractor_image").getString("tractor_image_name");
//                            //tractor1.setImage(tractor_image);
//                            if (tractor_image != null) {
//                                Log.d("vcbcxcxvxcvx2===",path+tractor_image);
//
////                                Glide.with(activity)
////                                        .load(path+tractor_image)
////                                        .placeholder(R.mipmap.no_image)
////                                        .error(R.mipmap.no_image)
////                                        .into(binding.image);
//                            }
//                            if (!exact_hp.isEmpty()) {
//                               // tractor1.setExactHp(exact_hp);
//                               // binding.tvHp.setText(tractor1.getExactHp().concat(" "+getResources().getString(R.string.hp)));
//                            }
//                        }
//                        else {
//                            String exact_hp = jsonArray.getJSONObject(i).getString("exact_hp");
//                            String tractor_image = jsonArray.getJSONObject(i).getJSONObject("tractor_image").getString("tractor_image_name");
//                            //tractor1.setImage(tractor_image);
//                            if (tractor_image != null) {
//                                Log.d("vcbcxcxvxcvx2===",path+tractor_image);
////                                Glide.with(activity)
////                                        .load(path+tractor_image)
////                                        .placeholder(R.mipmap.no_image)
////                                        .error(R.mipmap.no_image)
////                                        .into(binding.image1);
//                            }
//                            if (!exact_hp.isEmpty()) {
//                               // tractor2.setExactHp(exact_hp);
//                               // binding.tvHp1.setText(tractor2.getExactHp().concat(" " + getResources().getString(R.string.hp)));
//                            }
//                        }
//                        break;
//                    }
//                    else Log.d("vbcbvbvcvbcvbcv===",""+brandId+"=="+brand_id+"=="+modelId+"=="+tractor_model_id);
//                }
            }
            else {
                Log.d("cxvcvcxvxcvfdgd===","");
            }
        }
        catch(Exception e) {
            Log.d("cxvcvcxvxcvx235===",""+e.getLocalizedMessage());
        }
    }
}
