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
import com.khetigaadi.BuildConfig;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.databinding.SearchByNameBinding;
import com.khetigaadi.implement.newImplement.util.Type;
import com.khetigaadi.room.repository.TypeRepository;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;
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
        binding = SearchByNameBinding.inflate(inflater,container,false);
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
            else {
                Runnable runnable = new Runnable(){
                    public void run() {
                        //some code here
                        try {
                            getData(brandId);

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
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
    public void getData(String brand_id) {
        SessionManager sessionManager = new SessionManager(activity);
        User usrr = sessionManager.getUser();
        String usrrToken = usrr.getToken();
        if(usrrToken==null || usrrToken.equals(null))
            usrrToken="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI3IiwianRpIjoiMTgyMzNiNmNmMGI0ZDllZWI1OWQ5YmEyM2E2MjEwNWZjZGIzOGU5MGIzZjkxYjlhZDE0YjdiMDJlNWNjOTM5ZTI0NTgyOWJmYWYyMzVjZjMiLCJpYXQiOjE3NTY1NDg5MTksIm5iZiI6MTc1NjU0ODkxOSwiZXhwIjoxNzg4MDg0OTE5LCJzdWIiOiIxMDE2NDk3Iiwic2NvcGVzIjpbXX0.Sdk0CZeX5I1m3P2fqsvolpyxIybtjPpkgjBvKQ_GYsrU-vxIYAE-JN2_V2JUcHKSaGkgARD55OtZcsfwiszyGdJYpjz-d-am4KnS3ph_0I_4MWhXbcbsleN9hapf0idNsxJ-kDEjh8xSBdfSj3FAPcAd-fnP7iB_ni_QKiXJjTR6SucaPU4ZXKVIrs4iIXGx_-hjzloNHuIY0TaJhsIGBXLfx1jE37c3sCt7rttWqkPpVOT3eIBA2-6t4Ur7xl68CGk1cuwlg1SrfF9xev4QgxvrjPfEzb4ozCqOyBF-YEdTHNyXFB-6ba5JmY7k2roGBMwEqnqj7eWISY6gQdknZzFFAFlirCiKg2tiWhdyeFxwRFbf7dzEbL8NOBm4T9J-tpzisTiuTlnkr5U93hci5TYcUXV1IzotMUbBeK7gBbzn_NQhNPGbqmo7aIeJYp83TCdAVyvNf6x4NUrFBTqBg9nlRS4twRxo8OU0IdOS629oVnPZKn5jdLGHYkGZZMpREaTHeWp4AiUrLjVFPJy-hyPNYr1INayZnEWjorPlvOyHyqib846a9yaSJ9oFf33uKmOLXYwDyybkMewq2bnt0upwESrPI9fxrzyVY89LpDx2FjcABFaglUimzPw5qKWr2un0CSVwPjJzF-Iq5ctcG7WbADVj_eH_-IBubI0X9-g";

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,"{\n\t\"brand\":{\n\t\t\"brand_id\":\""+brand_id+"\"\n\t}\n}");
        Request request = new Request.Builder()
                .url(BuildConfig.BASE_URL+"mobile/api/v1/implement/new/brand")
                .method("POST",body)
                .addHeader("Content-Type","application/json")
                .addHeader("Accept","application/json")
                .addHeader("Authorization",usrrToken)
                .build();
      try {
          Response response = client.newCall(request).execute();
          String cvcx = response.body().string();
          Log.d("cvbcvbcvbcvbcbcv====",""+cvcx);
          if(response.isSuccessful()) {
            JSONObject jsonObject = new JSONObject(cvcx);
              JSONArray jsonArray = jsonObject.getJSONArray("data");
              List<Type> types = new ArrayList<Type>();
              Log.d("cvbcvbcvbcvbcbcv22====",""+jsonArray.length());
              for(int i=0;i<jsonArray.length();i++) {
                  Type type = new Type();
                  JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                  //String brandId = jsonObject1.getString("brand_id");
                  //int type_id = jsonObject1.getInt("type_id");

                  JSONObject implement_type =  jsonObject1.getJSONObject("implement_type");
                  int implement_type_id =   implement_type.getInt("implement_type_id");
                  String implement_type_name = implement_type.getString("implement_type_name");

                  type.setId(implement_type_id);
                  type.setName(implement_type_name);
                  types.add(type);
              }
              activity.runOnUiThread(new Runnable() {
                  public void run() {
                      if (adapter != null) {
                          adapter.setData(types);
                      }
                  }
              });
          }
          else {

          }
      }
      catch(Exception e) {
          Log.d("cxvbcvbnvbcvbcv===",""+e.getLocalizedMessage());
      }
    }
}
