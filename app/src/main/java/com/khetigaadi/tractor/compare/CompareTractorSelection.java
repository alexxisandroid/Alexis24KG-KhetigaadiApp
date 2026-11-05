package com.khetigaadi.tractor.compare;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.AppConfig;
import com.khetigaadi.BuildConfig;
import com.khetigaadi.R;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.utils.Drive;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.tractor.compare.model.CompTractResp;
import com.khetigaadi.tractor.newTractor.model.NewTractorPictureResp;
import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.databinding.CompareTrSelectionBinding;
import com.khetigaadi.model.LinearLayoutManagerWrapper;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.compare.model.CompareTractorParam;
import com.khetigaadi.tractor.compare.model.CompareTractorsListResp;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
import com.khetigaadi.tractor.newTractor.model.NewTractorResp;
import com.khetigaadi.tractor.newTractor.model.Tractor;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;
import com.khetigaadi.tractor.util.TractorImage;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;
@AndroidEntryPoint
public class CompareTractorSelection extends Fragment {
    private static final String TAG = CompareTractorSelection.class.getSimpleName();
    private String value;
    private JSONObject json;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    @Inject
    KhetiAPIs khetiAPIs;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private ComparePopularTractorAdapter popularTractorAdapter;
    private NewTractor tractor1;
    private NewTractor tractor2;
    private CompareTrSelectionBinding binding;
   // private CompareTractorViewModel viewModel;
    @Inject
    SharedPreferenceUtils pref;
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = getActivity();
        Bundle bundle = getArguments();
        if(bundle != null) {
            value = bundle.getString("value");
            //image = bu
        }
        try {
            if(value == null) {
                json = new JSONObject();
            }
            else {
                json = new JSONObject(value);
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        System.out.println("requestJson=" + value);
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        Timber.i("Setting screen name: %s", TAG);
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if(getArguments() != null) {
            if(getArguments().getBoolean("isCallFirstTr")) {
                getArguments().putBoolean("isCallFirstTr",false);
                binding.cvTractor1.callOnClick();
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        binding = CompareTrSelectionBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
       // viewModel = new ViewModelProvider(activity).get(CompareTractorViewModel.class);
        binding.rvPopularTractors.setNestedScrollingEnabled(false);
        binding.rvPopularTractors.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWrapper(activity,LinearLayoutManager.VERTICAL,false);
        binding.rvPopularTractors.setLayoutManager(mLayoutManager);
        if(value == null) {
            binding.llTractor1.setVisibility(View.GONE);
            binding.llTractor2.setVisibility(View.GONE);
            binding.llTractor1Empty.setVisibility(View.VISIBLE);
            binding.llTractor2Empty.setVisibility(View.VISIBLE);
        }
        else {
            try {
                String imgPath = BuildConfig.BASE_URL+"images/brandlogos/";
                imgPath = AppConfig.BASE_ASSET_URL+"new-tractor/";
                if(json.has("tractor1")) {
                    JSONObject tractor1Json = json.getJSONObject("tractor1");
                    Log.d("vcbcvbcvbcb===",""+tractor1Json);
                    if(tractor1Json.has("brandId") && !tractor1Json.isNull("brandId")) {

                        Runnable runnable = new Runnable() {
                            public void run() {
                                //some code here
                                try {
                                    getImageAndHp(tractor1Json.getString("brandId"),tractor1Json.getString("modelId"),1);
                                }
                                catch(Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                        Thread thread = new Thread(runnable);
                        thread.start();
                       // getCompareTractorDetails();
                        getImageAndHp2(tractor1Json.getString("brandId"),tractor1Json.getString("modelId"),1);

                        String image = tractor1Json.getString("image");

                        Log.d("vcbcxcxvxcvx2ds===",imgPath+image);
                        Glide.with(activity)
                                .load(imgPath+image)
                                .placeholder(R.mipmap.no_image)
                                .error(R.mipmap.no_image)
                                .into(binding.image);

                    }
                    if(tractor1Json.has("brandName") && tractor1Json.has("modelName")) {
                        binding.tvTractor1Name.setText(String.format("%s %s",tractor1Json.getString("brandName"),tractor1Json.getString("modelName")));
                    }
                    binding.llTractor1.setVisibility(View.VISIBLE);
                    binding.llTractor1Empty.setVisibility(View.GONE);
                }
                else {
                    binding.llTractor1.setVisibility(View.GONE);
                    binding.llTractor1Empty.setVisibility(View.VISIBLE);
                }
                if(json.has("tractor2")) {
                    JSONObject tractor1Json = json.getJSONObject("tractor2");
                    Log.d("vcbcvbcvbcb22===",""+tractor1Json);
                    if (tractor1Json.has("brandId") && !tractor1Json.isNull("brandId")) {
                     //Thread thread = new Thread()
                        Runnable runnable = new Runnable(){
                            public void run() {
                                //some code here
                                try {
                                    getImageAndHp(tractor1Json.getString("brandId"),tractor1Json.getString("modelId"),2);
                                }
                                catch(Exception e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        };
                        Thread thread = new Thread(runnable);
                        thread.start();
                       // getImageAndHp(tractor1Json.getString("brandId"),tractor1Json.getString("modelId"),2);
                        getImageAndHp2(tractor1Json.getString("brandId"),tractor1Json.getString("modelId"),2);

                        // getCompareTractorDetails();
                        String image = tractor1Json.getString("image");

                        Log.d("vcbcxcxvxcvx22222===",imgPath+image);
                        Glide.with(activity)
                                .load(imgPath+image)
                                .placeholder(R.mipmap.no_image)
                                .error(R.mipmap.no_image)
                                .into(binding.image1);
                    }
                    if (tractor1Json.has("brandName") && tractor1Json.has("modelName")) {
                        binding.tvTractor2Name.setText(String.format("%s %s", tractor1Json.getString("brandName"), tractor1Json.getString("modelName")));
                    }
                    binding.llTractor2.setVisibility(View.VISIBLE);
                    binding.llTractor2Empty.setVisibility(View.GONE);
                }
                else {
                    binding.llTractor2.setVisibility(View.GONE);
                    binding.llTractor2Empty.setVisibility(View.VISIBLE);
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
        popularTractorAdapter = new ComparePopularTractorAdapter(activity, binding.rvPopularTractors);
        binding.rvPopularTractors.setAdapter(popularTractorAdapter);
        popularTractorAdapter.setOnItemClickListener((position,v,get) -> {
            try {
                Log.d("vbcvbcvbcvbv====",""+get.getFirstTractor().getName()+"=="+get.getSecondTractor().getName());
                CompareTractorParam tractorParam = new CompareTractorParam();
                List<Tractor> list = new ArrayList<>();
                Tractor tractor = new Tractor();
                tractor.setId(popularTractorAdapter.getItem(position).getFirstTractor().getId());
                Tractor tractor1 = new Tractor();
                tractor1.setId(popularTractorAdapter.getItem(position).getSecondTractor().getId());
                list.add(tractor);
                list.add(tractor1);
                tractorParam.setTractors(list);
                startCompareActivity(tractorParam);
            }
            catch(Exception e) {
                e.printStackTrace();
                binding.llProgressComTractors.setVisibility(View.GONE);
            }
        });
        binding.cvTractor1.setOnClickListener(v -> {
            try {
                json.put("tractor1",new JSONObject());
                json.put("selected","1");
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
            GetBrandName fragment = new GetBrandName();
            Bundle bundle = new Bundle();
            bundle.putString("value",json.toString());
            fragment.setArguments(bundle);
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.enter);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            mFragmentManager.popBackStack("GetModelName",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.replace(contentView.getId(),fragment).addToBackStack(TAG).commit();
        });
        binding.cvTractor2.setOnClickListener(v -> {
            try {
                json.put("tractor2", new JSONObject());
                json.put("selected", "2");
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
            GetBrandName fragment = new GetBrandName();
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
            if(tractor1 != null && tractor2 != null) {
                Log.d("vcbvcbcbvcbvcb===",""+tractor1.getId()+"=="+tractor2.getId());
                Tractor tempTractor1 = new Tractor();
                tempTractor1.setId(tractor1.getId());
                Tractor tempTractor2 = new Tractor();
                tempTractor2.setId(tractor2.getId());
                List<Tractor> list = new ArrayList<>();
                list.add(tempTractor1);
                list.add(tempTractor2);
                CompareTractorParam tractorParam = new CompareTractorParam();
                tractorParam.setTractors(list); //Arrays.asList(tempTractor1, tempTractor2));
                startCompareActivity(tractorParam);
            }
            else {
                if(tractor1!=null && tractor2!=null)
                    Log.d("vcbvcbcbvcbvcb===",""+tractor1.getId()+"=="+tractor2.getId());

                Toast.makeText(activity,getResources().getString(R.string.please_select_tractors_for_compare),Toast.LENGTH_LONG).show();
            }
        });
        getPopularTractorComparison();
        return view;
    }
    private void getPopularTractorComparison() {
        binding.llProgressComTractors.setVisibility(View.VISIBLE);
        disposable.add(khetiAPIs.getCompareTractors(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CompareTractorsListResp>() {
                    @Override
                    public void onSuccess(@NonNull CompareTractorsListResp compareTractorsListResp)
                    {
                        if(!compareTractorsListResp.isError()) {
                            popularTractorAdapter.setOthers(compareTractorsListResp.getOthers());
                            popularTractorAdapter.updateItems(compareTractorsListResp.getData().getTractors());
                            binding.llProgressComTractors.setVisibility(View.GONE);
                            binding.rvPopularTractors.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(activity,compareTractorsListResp.getMsg().get(0),Toast.LENGTH_LONG).show();
                            binding.llProgressComTractors.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e)
                    {
                        Timber.e(e,"onError: ");
                    }
                }));
    }
    private void startCompareActivity(CompareTractorParam tractorParam) {
        Intent intent = new Intent(activity,CompareTractorActivity.class);
        intent.putExtra("tractorParam",tractorParam);
        startActivity(intent);
    }
    private void getImageAndHp2(final String brandId,final String modelId,final int i) {
        Brand brand = new Brand();
        brand.setId(Integer.parseInt(brandId));
        Model model = new Model();
        model.setId(Integer.parseInt(modelId));
        Filter filter = new Filter();
        List<Brand> bnd = new ArrayList<>();
        bnd.add(brand);

        List<Model> bndModel = new ArrayList<>();
        bndModel.add(model);
        filter.setBrands(bnd);  //Collections.singletonList(brand));
        filter.setModels(bndModel); //Collections.singletonList(model));

        TractorFilter tractorFilter = new TractorFilter();
        tractorFilter.setFilter(filter);
        disposable.add(khetiAPIs.getNewTractors(tractorFilter,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NewTractorResp>() {
                    @Override
                    public void onSuccess(@NonNull NewTractorResp newTractorResp) {
                        Log.d("cxvxvcxvcxvcx_succ===",""+newTractorResp.isError());
                        try {
                            if (!newTractorResp.isError()) {
                                if (i == 1) {
                                    tractor1 = newTractorResp.getData().getData().get(0);
                                    if (tractor1.getImage() != null) {
                                        Log.d("vcbcxcxvxcvx2===",newTractorResp.getOthers().getTractorImagePath() + tractor1.getImage().getTractor_img_name());
                                        Glide.with(activity)
                                                .load(newTractorResp.getOthers().getTractorImagePath() + tractor1.getImage().getTractor_img_name())
                                                .placeholder(R.mipmap.no_image)
                                                .error(R.mipmap.no_image)
                                                .into(binding.image);

                                    }
                                    if (!tractor1.getExactHp().isEmpty()) {
                                        binding.tvHp.setText(tractor1.getExactHp().concat(" "+getResources().getString(R.string.hp)));
                                    }
                                } else {
                                    tractor2 = newTractorResp.getData().getData().get(0);
                                    if (tractor2.getImage() != null) {
                                        Log.d("vcbcxcxvxcvx22===",newTractorResp.getOthers().getTractorImagePath()+tractor2.getImage().getTractor_img_name());
                                        Glide.with(activity)
                                                .load(newTractorResp.getOthers().getTractorImagePath()+tractor2.getImage().getTractor_img_name())
                                                .placeholder(R.mipmap.no_image)
                                                .error(R.mipmap.no_image)
                                                .into(binding.image1);

                                    }
                                    if (!tractor2.getExactHp().isEmpty()) {
                                        binding.tvHp1.setText(tractor2.getExactHp().concat(" " + getResources().getString(R.string.hp)));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("cxvxvcxvcxvcx===",""+e.getLocalizedMessage());
                        Timber.e(e, "onError: ");
                    }
                }));
    }
    private void getImages(String tractorId) {
        khetiAPIs.getNewTractorPicture(tractorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NewTractorPictureResp>() {
                    @Override
                    public void onSuccess(@NonNull NewTractorPictureResp pictureResp) {

                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                       // picture.postError(e.getMessage());
                    }
                });
    }
    private void getImageAndHp(final String brandId,final String modelId,final int ii) {
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
        try(Response response = client.newCall(request).execute()) {
            if(response.isSuccessful()) {
                String path =AppConfig.BASE_ASSET_URL+"new-tractor/";
                String vbnbvnv = response.body().string();
                Log.d("cxvcvcxvxcvx===",""+vbnbvnv+"=="+response.code());
                JSONObject jsonObject= new JSONObject(vbnbvnv);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                JSONArray jsonArray = jsonObject1.getJSONArray("data");
                if(ii==1) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                    String brand_id = jsonObject2.getString("brand_id");
                    String tractor_model_id = jsonObject2.getString("tractor_model_id");
                    String tractor_id = jsonObject2.getString("tractor_id");
                    String tractor_name = jsonObject2.getString("tractor_name");
                    String price_from = jsonObject2.getString("price_from");
                    String price_to = jsonObject2.getString("price_to");
                    String exact_hp = jsonObject2.getString("exact_hp");
                    String views = jsonObject2.getString("views");
                    boolean is_bookmark = jsonObject2.getBoolean("is_bookmark");
                    String tractor_image = jsonObject2.getJSONObject("tractor_image").getString("tractor_image_name");
                    JSONObject jsonObject3 = jsonObject2.optJSONObject("drive");
                    String drive_id = jsonObject3.getString("drive_id");
                    String drive_name = jsonObject3.getString("drive_name");
                    NewTractor newTractor = new NewTractor();
                    TractorImage tractorImage = new TractorImage();
                    tractorImage.setTractor_img_name(tractor_image);
                    newTractor.setImage(tractorImage);
                    newTractor.setExactHp(exact_hp);
                    com.khetigaadi.base.model.Model model = new com.khetigaadi.base.model.Model();
                    model.setId(Integer.parseInt(tractor_model_id));
                    newTractor.setTractorModel(model);
                    newTractor.setId(tractor_id);
                    newTractor.setName(tractor_name);
                    newTractor.setBookmark(is_bookmark);
                    Drive drive = new Drive();
                    drive.setName(drive_name);
                    drive.setId(Integer.parseInt(drive_id));
                    newTractor.setDrive(drive);
                    newTractor.setUserView(jsonObject2.getString("views"));
                    tractor1 = newTractor;
                }
                else {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                    String brand_id = jsonObject2.getString("brand_id");
                    String tractor_model_id = jsonObject2.getString("tractor_model_id");
                    String tractor_id = jsonObject2.getString("tractor_id");
                    String tractor_name = jsonObject2.getString("tractor_name");
                    String price_from = jsonObject2.getString("price_from");
                    String price_to = jsonObject2.getString("price_to");
                    String exact_hp = jsonObject2.getString("exact_hp");
                    String views = jsonObject2.getString("views");
                    boolean is_bookmark = jsonObject2.getBoolean("is_bookmark");
                    String tractor_image = jsonObject2.getJSONObject("tractor_image").getString("tractor_image_name");
                    JSONObject jsonObject3 = jsonObject2.optJSONObject("drive");
                    String drive_id = jsonObject3.getString("drive_id");
                    String drive_name = jsonObject3.getString("drive_name");
                    NewTractor newTractor = new NewTractor();
                    TractorImage tractorImage = new TractorImage();
                    tractorImage.setTractor_img_name(tractor_image);
                    newTractor.setImage(tractorImage);
                    newTractor.setId(tractor_id);
                    Brand brand = new Brand();
                    brand.setId(Integer.parseInt(brand_id));
                    newTractor.setBrand(brand);
                    newTractor.setExactHp(exact_hp);
                    com.khetigaadi.base.model.Model model = new com.khetigaadi.base.model.Model();
                    model.setId(Integer.parseInt(tractor_model_id));
                    newTractor.setTractorModel(model);
                    newTractor.setName(tractor_name);
                    newTractor.setBookmark(is_bookmark);
                    Drive drive = new Drive();
                    drive.setName(drive_name);
                    drive.setId(Integer.parseInt(drive_id));
                    newTractor.setDrive(drive);
                    newTractor.setUserView(jsonObject2.getString("views"));
                    tractor2 = newTractor;
                }
                for(int i=0;i<jsonArray.length();i++) {
                    String brand_id = jsonArray.getJSONObject(i).getString("brand_id");
                    String tractor_model_id = jsonArray.getJSONObject(i).getString("tractor_model_id");
                // Log.d("cxvcxcxvcxvcxvcxvcx===",""+(brand_id.equals(brandId) && Objects.equals(modelId,tractor_model_id)));
                 Log.d("cxvcxvcxvcxvcxc====",""+brand_id+"=="+brandId+"=="+modelId+"=="+tractor_model_id);
                   if(brand_id.equals(brandId) && tractor_model_id.equals(modelId)) {  // Objects.equals(modelId,tractor_model_id)
                      if(ii==1) {
                          String exact_hp = jsonArray.getJSONObject(i).getString("exact_hp");
                          String tractor_image = jsonArray.getJSONObject(i).getJSONObject("tractor_image").getString("tractor_image_name");
                          //tractor1.setImage(tractor_image);

                          activity.runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  if (tractor_image != null) {
                                      Log.d("vcbcxcxvxcvx2===",path+tractor_image);

                                      Glide.with(activity)
                                              .load(path+tractor_image)
                                              .placeholder(R.mipmap.no_image)
                                              .error(R.mipmap.no_image)
                                              .into(binding.image);


                                  }
                                  if (!exact_hp.isEmpty()) {
                                      tractor1.setExactHp(exact_hp);
                                      binding.tvHp.setText(tractor1.getExactHp().concat(" "+getResources().getString(R.string.hp)));
                                  }
                              }
                          });

                      }
                      else {
                          String exact_hp = jsonArray.getJSONObject(i).getString("exact_hp");
                          String tractor_image = jsonArray.getJSONObject(i).getJSONObject("tractor_image").getString("tractor_image_name");
                          //tractor1.setImage(tractor_image);
                          activity.runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                          if (tractor_image != null) {
                              Log.d("vcbcxcxvxcvx2===",path+tractor_image);

                              Glide.with(activity)
                                      .load(path+tractor_image)
                                      .placeholder(R.mipmap.no_image)
                                      .error(R.mipmap.no_image)
                                      .into(binding.image1);
                          }
                          if (!exact_hp.isEmpty()) {
                              tractor2.setExactHp(exact_hp);
                              binding.tvHp1.setText(tractor2.getExactHp().concat(" " + getResources().getString(R.string.hp)));
                          }
                              }
                          });
                      }
                       break;
                   }
                   else Log.d("vbcbvbvcvbcvbcv===",""+brandId+"=="+brand_id+"=="+modelId+"=="+tractor_model_id);
                }
            }
            else {
                Log.d("cxvcvcxvxcvfdgd===","");
            }
        }
        catch(Exception e) {
            Log.d("cxvcvcxvxcvx235===",""+e.getLocalizedMessage());
        }
    }
    private void getCompareTractorDetails() {
        // Tag used to cancel the request
//        viewModel.getCompare().observe(activity, compTractRespStateData -> {
//            Log.d("bvcvbvcbcvb===",""+compTractRespStateData.getStatus());
//            switch (compTractRespStateData.getStatus()) {
//                case LOADING:
//                   // binding.llProgress.setVisibility(View.VISIBLE);
//                    break;
//                case SUCCESS:
//                  //  binding.llProgress.setVisibility(View.GONE);
//                    if(compTractRespStateData.getData() != null) {
//                        pref.setTractorImagePath(compTractRespStateData.getData().getOthers().getTractorImagePath());
//                        bindData(compTractRespStateData.getData());
//                      //  binding.viewPager.setAdapter(new CompareTractorTab.MyAdapter(getChildFragmentManager()));
//                       // binding.tabs.post(() -> binding.tabs.setupWithViewPager(binding.viewPager));
//                    }
//                    break;
//                case ERROR:
//                   //// binding.llProgress.setVisibility(View.GONE);
//                    Toast.makeText(activity, compTractRespStateData.getError(), Toast.LENGTH_SHORT).show();
//                    activity.finish();
//                    break;
//            }
//        });
    }
    private void bindData(CompTractResp data) {
        NewTractor tractorsJsonArray1;
        NewTractor tractorsJsonArray2;
        try {
            tractorsJsonArray1 = data.getOthers().getTractorData().get(0);
            tractorsJsonArray2 = data.getOthers().getTractorData().get(1);
            Glide.with(activity)
                    .load(pref.getTractorImagePath() + tractorsJsonArray1.getImage().getTractor_img_name())
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(binding.image);
            Glide.with(activity)
                    .load(pref.getTractorImagePath() + tractorsJsonArray2.getImage().getTractor_img_name())
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(binding.image1);
            binding.tvTractor1Name.setText(tractorsJsonArray1.getName());
            binding.tvTractor2Name.setText(tractorsJsonArray2.getName());
            binding.tvHp.setText(String.format("%s %s", tractorsJsonArray1.getExactHp(), getString(R.string.hp)));
            binding.tvHp1.setText(String.format("%s %s", tractorsJsonArray2.getExactHp(), getString(R.string.hp)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
