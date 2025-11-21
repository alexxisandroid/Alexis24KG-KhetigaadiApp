package com.khetigaadi.tractor.newTractor;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.BuildConfig;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.commons.utils.Drive;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.def.CallFrom;
import com.khetigaadi.def.PriceType;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.tractor.TractorFilterActivity;
import com.khetigaadi.tractor.compare.model.TractorShortSpec;
import com.khetigaadi.tractor.compare.model.TractorUnit;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
import com.khetigaadi.tractor.newTractor.model.NewTractorData;
import com.khetigaadi.tractor.newTractor.model.NewTractorResp;
import com.khetigaadi.tractor.newTractor.model.NewTractorShortSpec;
import com.khetigaadi.tractor.usedTractor.UsedTractorDetailActivity;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
@AndroidEntryPoint
public class NewTractorsListFragment extends BaseFragment {
    private static final String TAG = NewTractorsListFragment.class.getSimpleName();
    private NewTractorListAdapter mAdapter;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    @Inject
    SessionManager session;
    public int position1 = 0;
    private NewTractorsListViewModel viewModel;
    private RecycleListBinding binding;
    public static Filter filters;
    public NewTractorsListFragment() {
    }
    public static NewTractorsListFragment getInstance(Filter filter) {
        NewTractorsListFragment newTractorsListFragment = new NewTractorsListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("filter",filter);
        newTractorsListFragment.setArguments(bundle);
         filters = filter;
       // Log.d("vbvcbvcbcb====",""+filters+"=="+filters.getBrands().get(0).getId()+"=="+filters.getModels().get(0).getId());

      //  Log.d("cvbcvbcvbcvbc=====",""+filters.getBrands().get(0).getId()+"=="+filters.getModels().get(0).getId());

        return newTractorsListFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if(mAdapter != null) {
            mAdapter.notifyItemChanged(position1);
        }
    }
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,final ViewGroup container,Bundle savedInstanceState)
    {
        binding = RecycleListBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NewTractorsListViewModel.class);
        if(filters!=null)
            viewModel.setFilter(filters);
        Log.d("vcbvbvcvbc====",""+"filters.getBrands().toString()"+"=="+viewModel.getFilter());
        ((MainActivity) activity).setToolbarTitle(getString(R.string.new_tractor));
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
        binding.filterIcon.setOnClickListener(v -> {
            Intent intent = new Intent(activity, TractorFilterActivity.class);
            intent.putExtra("filter", viewModel.getFilter());
            intent.putExtra("callFrom", CallFrom.NEW_TRACTOR);
            filterActivityResultLauncher.launch(intent);

        });
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        init();
    }
    ActivityResultLauncher<Intent> filterActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),result -> {
                Intent data = result.getData();
              //  Log.d("gfhdfgdfgdf===",data+"=="+result.getData().getExtras().getParcelable("filter"));
                if (data != null) {
                    viewModel.setFilter(result.getData().getExtras().getParcelable("filter"));
                    mAdapter.itemRemoveAll();
                    getNewTractorList(1);
                }
            });
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(Constants.isConnectionAvailable(activity)) {
            Log.d("vcxcxvxvc====",""+viewModel.getFilter());
            getNewTractorList(1);
        }
        else {
            ((MainActivity)activity).noInternetConnection();
        }
    }
    ActivityResultLauncher<Intent> tractorDetailsActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                Log.d("vcbvcbvcbcvb=====","");
                if (data != null) {
                    try {
                        Bundle extras = result.getData().getExtras();
                        int position = extras.getInt("position",-1);
                        NewTractor tractor = mAdapter.getItem(position);
                        tractor.setBookmark(extras.getBoolean(UsedTractorDetailActivity.isFavourite, false));
                        mAdapter.updateItem(tractor, position);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    private void init() {
        mAdapter = new NewTractorListAdapter(activity, binding.myRecyclerView);
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new NewTractorListAdapter.Listener() {
            @Override
            public void onClick(int position,@NotNull View view) {
                position1 = position;
                Intent intent = new Intent(activity,NewTractorDetails.class);
                intent.putExtra("position",position);
                intent.putExtra(NewTractorDetails.newTractorId,mAdapter.getItem(position).getId());
                tractorDetailsActivityResultLauncher.launch(intent);
            }
            @Override
            public void onFavoriteClick(int position,@NotNull View view) {
                view.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.anim_scale));
                setMyFavorites(mAdapter.getItem(position),position);
            }
            @Override
            public void onGetRoadPriceClick(int position,@NotNull View view) {
                Intent intent = new Intent(activity, NewTrGetOnRoadPriceActivity.class);
                intent.putExtra("id", mAdapter.getItem(position).getId());
                intent.putExtra("name", mAdapter.getItem(position).getName());
                if (mAdapter.getItem(position).getImage() != null)
                    intent.putExtra("image", mAdapter.getOthers().getTractorImagePath() + mAdapter.getItem(position).getImage().getTractor_img_name());
                intent.putExtra("priceType", PriceType.TRACTOR);
                startActivity(intent);
            }
            @Override
            public void onGetDemoClick(int position,@NotNull View view) {
                Intent intent = new Intent(activity, NewTractorRequestForDemo.class);
                intent.putExtra("id", mAdapter.getItem(position).getId());
                intent.putExtra("name", mAdapter.getItem(position).getName());
                intent.putExtra("image", mAdapter.getOthers().getTractorImagePath() + mAdapter.getItem(position).getImage().getTractor_img_name());
                intent.putExtra("demoType", PriceType.TRACTOR);
                startActivity(intent);
            }
        });
        mAdapter.setOnLoadMoreListener(this::getNewTractorList);
    }
    private void getNewTractorList(int page) {
        viewModel.setFilter(filters);
        if(getView()!=null) {
            viewModel.getNewTractors(page).observe(getViewLifecycleOwner(), newTractorRespStateData -> {
                Log.d("vcbcbcvbvcb====", "" + newTractorRespStateData.getStatus() + "==" + viewModel.getFilter());
                switch (newTractorRespStateData.getStatus()) {
                    case LOADING:
                        if (page == 1) {
                            binding.llProgress.setVisibility(View.VISIBLE);
                            binding.filterIcon.setVisibility(View.GONE);
                        }
                        break;
                    case SUCCESS:
                        NewTractorResp newTractorResp = newTractorRespStateData.getData();
                        Log.d("vcbcvbcvbccxvcx===", "" + newTractorResp);
                        binding.llProgress.setVisibility(View.GONE);
                        binding.filterIcon.setVisibility(View.VISIBLE);
                        if (newTractorResp != null) {
                            mAdapter.setTotPages(newTractorResp.getData().getLastPage());
                            mAdapter.addItems(addSponsoredItems(newTractorResp), page, newTractorResp.getOthers());
                            Log.d("vcbcvbcvbc11===", "" + newTractorResp.getData().getLastPage());
                            Log.d("vcbcvbcvbc22===", "" + addSponsoredItems(newTractorResp).size() + "==" + page + "==" + newTractorResp.getOthers());
                        }
                        break;
                    case ERROR:
                        //  Toast.makeText(activity,newTractorRespStateData.getError(),Toast.LENGTH_SHORT).show();
                        mAdapter.addItems(new ArrayList<>(), page);
                        binding.llProgress.setVisibility(View.GONE);
                        binding.filterIcon.setVisibility(View.VISIBLE);
                        Log.d("cvbcvbcvbvbcbfgfg===", "" + newTractorRespStateData.getError());

                        if (newTractorRespStateData.getError().equals("HTTP 500 Internal Server Error")) {
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    //some code here
                                    try {
                                        if (filters.getBrands().get(0).getId() != 0 && filters.getModels().get(0).getId() != 0)
                                            getDataa(filters.getBrands().get(0).getId(), filters.getModels().get(0).getId(), page);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            };
                            Thread thread = new Thread(runnable);
                            thread.start();
                        }
                        break;
                }
            });
        }
    }
    private void getDataa(int brandId,int tractorModelId,int pagge) {
        SessionManager sessionManager = new SessionManager(activity);
        User usrr = sessionManager.getUser();
        String usrrToken = usrr.getToken();
        if(usrrToken==null || usrrToken.equals(null))
            usrrToken="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI3IiwianRpIjoiMTgyMzNiNmNmMGI0ZDllZWI1OWQ5YmEyM2E2MjEwNWZjZGIzOGU5MGIzZjkxYjlhZDE0YjdiMDJlNWNjOTM5ZTI0NTgyOWJmYWYyMzVjZjMiLCJpYXQiOjE3NTY1NDg5MTksIm5iZiI6MTc1NjU0ODkxOSwiZXhwIjoxNzg4MDg0OTE5LCJzdWIiOiIxMDE2NDk3Iiwic2NvcGVzIjpbXX0.Sdk0CZeX5I1m3P2fqsvolpyxIybtjPpkgjBvKQ_GYsrU-vxIYAE-JN2_V2JUcHKSaGkgARD55OtZcsfwiszyGdJYpjz-d-am4KnS3ph_0I_4MWhXbcbsleN9hapf0idNsxJ-kDEjh8xSBdfSj3FAPcAd-fnP7iB_ni_QKiXJjTR6SucaPU4ZXKVIrs4iIXGx_-hjzloNHuIY0TaJhsIGBXLfx1jE37c3sCt7rttWqkPpVOT3eIBA2-6t4Ur7xl68CGk1cuwlg1SrfF9xev4QgxvrjPfEzb4ozCqOyBF-YEdTHNyXFB-6ba5JmY7k2roGBMwEqnqj7eWISY6gQdknZzFFAFlirCiKg2tiWhdyeFxwRFbf7dzEbL8NOBm4T9J-tpzisTiuTlnkr5U93hci5TYcUXV1IzotMUbBeK7gBbzn_NQhNPGbqmo7aIeJYp83TCdAVyvNf6x4NUrFBTqBg9nlRS4twRxo8OU0IdOS629oVnPZKn5jdLGHYkGZZMpREaTHeWp4AiUrLjVFPJy-hyPNYr1INayZnEWjorPlvOyHyqib846a9yaSJ9oFf33uKmOLXYwDyybkMewq2bnt0upwESrPI9fxrzyVY89LpDx2FjcABFaglUimzPw5qKWr2un0CSVwPjJzF-Iq5ctcG7WbADVj_eH_-IBubI0X9-g";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,"{\n\t\"filter\":{\n\t\"brands\": [\n{\n\"brand_id\": \""+brandId+"\"\n}\n],\n\"tractor_models\":[\n{\n\"tractor_model_id\": \""+tractorModelId+"\"\n}\n]\n},\n\"perPage\":12\n}");
        Request request = new Request.Builder()
                .url(BuildConfig.BASE_URL+"mobile/api/v1/tractor/new/filter")
                .method("POST", body)
                .addHeader("Content-Type","application/json")
                .addHeader("Accept","application/json")
                .addHeader("Authorization",usrrToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
              String dataVal = response.body().string();
              Log.d("vcbgvbcvbc====",""+dataVal);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject newTractorResp = null; //.getJSONArray("data");
                        try {
                            newTractorResp = new JSONObject(dataVal);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("vcbcvbcvbccxvcx===",""+newTractorResp);
                binding.llProgress.setVisibility(View.GONE);
                binding.filterIcon.setVisibility(View.VISIBLE);
                if(newTractorResp != null) {
                    mAdapter.setTotPages(newTractorResp.optJSONObject("data").optInt("last_page"));

                    NewTractorResp tractorResp = new NewTractorResp();
                    NewTractorData newTractorData = new NewTractorData();
                    NewTractor newTractor = new NewTractor();

                    JSONObject jsonObject = null; //tractor_id
                    try {
                        jsonObject = newTractorResp.optJSONObject("data").getJSONArray("data").optJSONObject(0);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    String tractor_id = jsonObject.optString("tractor_id");
                    String tractor_name = jsonObject.optString("tractor_name");
                    boolean is_bookmark = jsonObject.optBoolean("is_bookmark");
                    int views = jsonObject.optInt("views");
                    String tractor_image = null;
                    try {
                        tractor_image = jsonObject.getJSONObject("tractor_image").getString("tractor_image_name");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    com.khetigaadi.tractor.util.TractorImage tractorImage = new  com.khetigaadi.tractor.util.TractorImage();
                    tractorImage.setTractor_img_name(tractor_image);
                    try {
                        tractorImage.setTractor_id(jsonObject.getJSONObject("tractor_image").getString("tractor_id"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    newTractor.setId(tractor_id);
                    newTractor.setName(tractor_name);
                    newTractor.setImage(tractorImage);
                    newTractor.setBookmark(is_bookmark);
                    newTractor.setUserView(String.valueOf(views));
                    Brand brand = new Brand();
                    int bid = 0;
                    try {
                        bid = jsonObject.optJSONObject("brand").getInt("brand_id");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    String brand_name =   jsonObject.optJSONObject("brand").optString("brand_name");
                    String brand_image =  jsonObject.optJSONObject("brand").optString("brand_image");
                    brand.setId(bid);
                    brand.setName(brand_name);
                    brand.setImage(brand_image);


                    newTractor.setBrand(brand);

                    com.khetigaadi.base.model.Model model = new com.khetigaadi.base.model.Model();
                    //tractor_model_id  tractor_model_name
                    JSONObject modelData =  jsonObject.optJSONObject("tractor_model");
                    model.setId(modelData.optInt("tractor_model_id"));
                    model.setName(modelData.optString("tractor_model_name"));
                    newTractor.setTractorModel(model);

                    JSONObject driveData =  jsonObject.optJSONObject("drive");

                    Drive drive = new Drive();
                    drive.setId(driveData.optInt("drive_id"));
                    drive.setName(driveData.optString("drive_name"));

                    newTractor.setDrive(drive);

                    newTractor.setExactHp(jsonObject.optString("exact_hp"));
                    JSONArray modelDataV =  jsonObject.optJSONArray("get_short_spec_data"); //.optJSONObject(0);
                    List<NewTractorShortSpec> newTractorShortSpecs = new ArrayList<>();

                    for(int i=0;i<modelDataV.length();i++) {
                        try {
                            JSONObject jsonObject1 = modelDataV.getJSONObject(i);
                            int data_id = jsonObject1.optInt("data_id");
                            int tractor_spec_id = jsonObject1.optInt("tractor_spec_id");
                            String spec_data = jsonObject1.optString("spec_data");
                            String spec_data_mr = jsonObject1.optString("spec_data_mr");
                            String spec_data_hi = jsonObject1.optString("spec_data_hi");
                            int tractor_idd = jsonObject1.optInt("tractor_id");
                            JSONObject tractor_spec = jsonObject1.optJSONObject("tractor_spec");
                            int tractor_spec_idd = tractor_spec.optInt("tractor_spec_id");
                            String tractor_spec_name = tractor_spec.getString("tractor_spec_name");
                            int unit_id = tractor_spec.optInt("unit_id");
                            String phase = tractor_spec.optString("phase");
                            String spec_icon = tractor_spec.optString("spec_icon");
                            com.khetigaadi.tractor.compare.model.TractorSpec tractorSpec = new com.khetigaadi.tractor.compare.model.TractorSpec();
                            tractorSpec.setTractorSpecId(String.valueOf(tractor_spec_idd));
                            TractorShortSpec tractorShortSpec = new TractorShortSpec();
                            tractorShortSpec.setName(tractor_spec_name);
                            tractorShortSpec.setId(String.valueOf(tractor_spec_idd));
                            tractorSpec.setTractorSpec(tractorShortSpec);

                            TractorUnit tractorUnit = new TractorUnit();
                            tractorUnit.setUnitId(unit_id);
                            tractorUnit.setUnitName(tractor_spec_name);

                            tractorSpec.setTractorUnit(tractorUnit);

                           // NewTractorShortSpec tractorShortSpec = new NewTractorShortSpec();
                            //tractorSpec.setTractorSpec();
                           // tractorShortSpec.setTractorSpec();
                           // tractorSpec.setTractorSpec();

                         //   tractorSpec.set

                            NewTractorShortSpec newTractorShortSpec = new NewTractorShortSpec();
                            newTractorShortSpec.setTractorId(tractor_idd);
                            newTractorShortSpec.setTractorSpecId(tractor_spec_id);
                            newTractorShortSpec.setSpecData(spec_data);
                            newTractorShortSpec.setDataId(data_id);
                            newTractorShortSpec.setTractorSpec(tractorSpec);
                            newTractorShortSpecs.add(newTractorShortSpec);
                            //  tractorSpec.set
                           // newTractorShortSpec.set
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    newTractor.setGetShortSpecData(newTractorShortSpecs);
                    List<NewTractor> newTractors = new ArrayList<>();
                    newTractors.add(newTractor);
                    newTractorData.setData(newTractors);
                    tractorResp.setData(newTractorData);

                    com.khetigaadi.model.Others others = new com.khetigaadi.model.Others();
                    String jsonObjecti = newTractorResp.optJSONObject("others").optString("brandLogoPath");
                    String tractorImagePathcv = newTractorResp.optJSONObject("others").optString("tractorImagePath");
                    others.setBrandLogoPath(jsonObjecti);
                    others.setTractorImagePath(tractorImagePathcv);
                    mAdapter.addItems(addSponsoredItems(tractorResp),pagge,others);

                    Log.d("vcbcvbcvbc11===",""+newTractorResp.optJSONObject("data").optInt("last_page"));
                    Log.d("vcbcvbcvbc22===",""+addSponsoredItems(tractorResp).size()+"=="+pagge+"=="+others);
                }
                    }

                });
            }
            else {

            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private List<NewTractor> addSponsoredItems(NewTractorResp newTractorResp) {
        try {
            if (newTractorResp.getData().getData().size() > 0 && newTractorResp.getData().getData().size() > 0 && newTractorResp.getOthers().getNewTractorSponsored() != null && newTractorResp.getOthers().getNewTractorSponsored().size() > 0) {
                if (newTractorResp.getData().getData().size() > 5 && newTractorResp.getOthers().getNewTractorSponsored().size() > 0) {
                    newTractorResp.getOthers().getNewTractorSponsored().get(0).getNewTractor().setSponsor(true);
                    newTractorResp.getData().getData().add(4, newTractorResp.getOthers().getNewTractorSponsored().get(0).getNewTractor());
                    if (newTractorResp.getData().getData().size() > 8 && newTractorResp.getOthers().getNewTractorSponsored().size() > 1) {
                        newTractorResp.getOthers().getNewTractorSponsored().get(1).getNewTractor().setSponsor(true);
                        newTractorResp.getData().getData().add(9, newTractorResp.getOthers().getNewTractorSponsored().get(1).getNewTractor());
                    }
                }

            }
        } catch (Exception e) {
            return newTractorResp.getData().getData();
        }
        return newTractorResp.getData().getData();
    }
    private void setMyFavorites(final NewTractor tractor,final int position) {
        viewModel.setMyFavorites(tractor).observe(getViewLifecycleOwner(), myResponse -> {
            switch (myResponse.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (myResponse.getData() != null) {
                        if (!myResponse.getData().isError()) {
                            mAdapter.getItem(position).setBookmark(!myResponse.getData().getMsg().get(0).equalsIgnoreCase("Bookmark Removed Successfully"));
                        } else {
                            Toast.makeText(activity, myResponse.getData().getMsg().get(0), Toast.LENGTH_LONG).show();
                        }
                    }
                    mAdapter.notifyItemChanged(position);
                    break;
                case ERROR:
                    mAdapter.notifyItemChanged(position);
                    break;
            }
        });
    }
}
