package com.khetigaadi.ecom.list;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.khetigaadi.BuildConfig;
import com.khetigaadi.R;
import com.khetigaadi.base.model.Price;
import com.khetigaadi.databinding.ActivityProductListBinding;
import com.khetigaadi.ecom.ProductDetailsActivity;
import com.khetigaadi.retrofit.ShopAPIs;
import com.khetigaadi.shop.model.Category;
import com.khetigaadi.shop.model.Filter;
import com.khetigaadi.shop.model.Others;
import com.khetigaadi.shop.model.Product;
import com.khetigaadi.shop.model.ProductListFilter;
import com.khetigaadi.shop.model.ProductListResp;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class ProductListActivity extends AppCompatActivity {
    private ActivityProductListBinding binding;
    public static final String SELECTED_CATEGORY = "selectedCategory";
    private ProductListViewModel viewModel;
    private ProductListAdapter agricultureToolsAdapter;
    @Inject
     ShopAPIs shopAPIs;
    String catId = "";
    int page =1;
    int fpage=0;
    ActivityResultLauncher<Intent> productListFilterLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    viewModel.setFilter(data.getParcelableExtra(ProductListFilterActivity.FILTER));
                    Filter cxvcxv = data.getParcelableExtra(ProductListFilterActivity.FILTER);
                    Log.d("xcvcvcxvcxvxvx===",""+data.getParcelableExtra(ProductListFilterActivity.FILTER)+"=="+cxvcxv.getCategories().size());
                    //catId =
                   // getList(1);
                    Runnable runnable = new Runnable(){
                        public void run() {
                            //some code here
                            getDataValFilter(page);
                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityProductListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);
        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);
        initToolbar();
        initAdapter();
        if(getIntent().hasExtra(ProductListFilterActivity.FILTER)) {
            viewModel.setFilter(getIntent().getParcelableExtra(ProductListFilterActivity.FILTER));
         Log.d("bgvdfxgdgdfgdghjg====",""+getIntent().getParcelableExtra(ProductListFilterActivity.FILTER)+"=="+getIntent().getParcelableExtra(ProductListFilterActivity.FILTER).toString());
        }
        //getList(1);
        binding.fabFilter.setOnClickListener(v -> {
            Intent intent = new Intent(this,ProductListFilterActivity.class);
            intent.putExtra(ProductListFilterActivity.FILTER,viewModel.getFilter());
            productListFilterLauncher.launch(intent);
        });
        Log.d("cvxvcxvvcxvxv===",""+viewModel.getFilter());
        //getLists();
        Runnable runnable = new Runnable(){
            public void run() {
                //some code here
                getDataVal(page);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        //my_sticky_button
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runnableS();
            }
        });
        binding.rvProductList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v,int scrollX,int scrollY,int oldScrollX,int oldScrollY) {
                if (!binding.rvProductList.canScrollVertically(1)) {     //&& !isLoading && page <= limit
                 try {
                     int tpp = binding.rvProductList.getAdapter().getItemCount() / 10;
                     Log.d("cxvcvvcxvx====",""+tpp);
                     if (tpp >= 1)
                         page = tpp + 1;
                     //  page++;
                     // loadingPB.setVisibility(View.VISIBLE);
                     getDataVal(page);
                 }
                 catch (Exception e){
                     e.printStackTrace();
                 }
                }
            }
        });
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.shop));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.shop));
        }
    }
    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rvProductList.setLayoutManager(gridLayoutManager);
        agricultureToolsAdapter = new ProductListAdapter(this, binding.rvProductList);
        agricultureToolsAdapter.setOnLoadMoreListener(this::getList);
        agricultureToolsAdapter.setOnItemClickListener((position, v) -> navigateToDetails(agricultureToolsAdapter.getItem(position), agricultureToolsAdapter.getOthers().getShopProductImagePath()));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // If progress will be shown then span size will be 1 otherwise it will be 2
                return agricultureToolsAdapter.getItemViewType(position) == ProductListAdapter.EMPTY_VIEW || agricultureToolsAdapter.getItemViewType(position) == ProductListAdapter.VIEW_PROG ? 2 : 1;
            }
        });
        binding.rvProductList.setAdapter(agricultureToolsAdapter);
    }
    private void getList(int page) {
        Log.d("dfxgvdsgdsgdfg====",""+viewModel.getFilter());
        viewModel.getList(page).observe(this,productListRespStateData -> {
           // Log.d("cvbcvbcbc===",""+productListRespStateData.getStatus()+"=="+productListRespStateData.getError());
           // ProgressBar pbar = binding.parent.findViewById(R.id.rlData).findViewById(R.id.llProgresss).findViewById(R.id.llProgress);
               switch (productListRespStateData.getStatus()) {
                case LOADING:
                    if (page == 1) {
                        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
                        binding.fabFilter.setVisibility(View.GONE);
                    }
                    break;
                case SUCCESS:
                    if (productListRespStateData.getData() != null) {
                        Log.d("cxvcxvxvxcvcxv===",""+productListRespStateData.getStatus()+"=="+productListRespStateData.getData().getData().getData().size());

                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                        binding.fabFilter.setVisibility(View.VISIBLE);
                        agricultureToolsAdapter.setTotPages(productListRespStateData.getData().getLastPage());
                        if (page == 1) {
                            agricultureToolsAdapter.setOthers(productListRespStateData.getData().getOthers());
                            agricultureToolsAdapter.updateItems(productListRespStateData.getData().getData().getData());
                        } else
                            agricultureToolsAdapter.addItems((List<Product>) productListRespStateData.getData().getData().getData(),page,productListRespStateData.getData().getOthers());
                    }
                    break;
                case ERROR:
                    binding.llProgresss.llProgress.setVisibility(View.GONE);
                    Toast.makeText(this,productListRespStateData.getError(),Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
    private void getLists() {
        if(getIntent().hasExtra(ProductListFilterActivity.FILTER)) {
            viewModel.setFilter(getIntent().getParcelableExtra(ProductListFilterActivity.FILTER));
        }
        Filter filter = getIntent().getParcelableExtra(ProductListFilterActivity.FILTER);
        filter.setPrice(new Price());
        ProductListFilter productListFilter = new ProductListFilter();
        Log.d("bnmnmbnmb====",""+filter);

        productListFilter.setFilter(filter);
        shopAPIs.getProducts(productListFilter,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProductListResp>() {
                    @Override
                    public void onSuccess(@NonNull ProductListResp productListResp) {
                        Log.d("dzfszfsvdsfdsf====",""+productListResp.getData().toString()+"=="+filter);
                        if(productListResp.isError()) {

                        }
                        else {
                            Log.d("dzfszfsvdsfdsf2345235====",""+productListResp.getData().toString()+"=="+filter);
                           // gardenTools.postSuccess(productListResp);
                           // sessionManager.setEcomOther(productListResp.getOthers());
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                       // gardenTools.postError(e.getMessage());
                    }
                });
    }
    private void navigateToDetails(Product product,String imagePath) {
        Intent intent = new Intent(this,ProductDetailsActivity.class);
        intent.putExtra("product",product);
        intent.putExtra("imagePath",imagePath);
        startActivity(intent);
    }
    public void getDataVal(int page) {
        Log.d("vcbcvbcvbcb===",""+fpage+"=="+page);
        if(fpage==page)
            return;
        fpage=page;
    SessionManager sessionManager = new SessionManager(this);
    User usrr = sessionManager.getUser();
    String usrrToken = usrr.getToken();
    if(usrrToken==null || usrrToken.equals(null))
        usrrToken="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI3IiwianRpIjoiMTgyMzNiNmNmMGI0ZDllZWI1OWQ5YmEyM2E2MjEwNWZjZGIzOGU5MGIzZjkxYjlhZDE0YjdiMDJlNWNjOTM5ZTI0NTgyOWJmYWYyMzVjZjMiLCJpYXQiOjE3NTY1NDg5MTksIm5iZiI6MTc1NjU0ODkxOSwiZXhwIjoxNzg4MDg0OTE5LCJzdWIiOiIxMDE2NDk3Iiwic2NvcGVzIjpbXX0.Sdk0CZeX5I1m3P2fqsvolpyxIybtjPpkgjBvKQ_GYsrU-vxIYAE-JN2_V2JUcHKSaGkgARD55OtZcsfwiszyGdJYpjz-d-am4KnS3ph_0I_4MWhXbcbsleN9hapf0idNsxJ-kDEjh8xSBdfSj3FAPcAd-fnP7iB_ni_QKiXJjTR6SucaPU4ZXKVIrs4iIXGx_-hjzloNHuIY0TaJhsIGBXLfx1jE37c3sCt7rttWqkPpVOT3eIBA2-6t4Ur7xl68CGk1cuwlg1SrfF9xev4QgxvrjPfEzb4ozCqOyBF-YEdTHNyXFB-6ba5JmY7k2roGBMwEqnqj7eWISY6gQdknZzFFAFlirCiKg2tiWhdyeFxwRFbf7dzEbL8NOBm4T9J-tpzisTiuTlnkr5U93hci5TYcUXV1IzotMUbBeK7gBbzn_NQhNPGbqmo7aIeJYp83TCdAVyvNf6x4NUrFBTqBg9nlRS4twRxo8OU0IdOS629oVnPZKn5jdLGHYkGZZMpREaTHeWp4AiUrLjVFPJy-hyPNYr1INayZnEWjorPlvOyHyqib846a9yaSJ9oFf33uKmOLXYwDyybkMewq2bnt0upwESrPI9fxrzyVY89LpDx2FjcABFaglUimzPw5qKWr2un0CSVwPjJzF-Iq5ctcG7WbADVj_eH_-IBubI0X9-g";
    Log.d("cbvbcbvcbcv====",""+usrr);
   // catId =
    // usrr.getToken();
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");
   // RequestBody body = RequestBody.create(mediaType, "{\r\n    \"filter\": {\r\n        \"price\":null,\r\n        \"categories\": [\r\n            {\r\n                \"id\": 3\r\n            }\r\n        ]\r\n    }\r\n   \r\n}");
    List<Category> cvc = viewModel.getFilter().getCategories();
    JSONArray jsonArray1 = new JSONArray();
    for(int i=0;i<cvc.size();i++) {
        Category category = cvc.get(i);
        // Create a Gson instance
        Gson gson = new Gson();

        // Convert the User object to a JSON string
        String json = gson.toJson(category);
        try {
            JSONObject jsonObject = new JSONObject(json);
            int id = jsonObject.getInt("id");
            //jsonObject.put("id",id);
            jsonArray1.put(new JSONObject().put("id",id));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    Log.d("jsonArray123423423=====",""+jsonArray1);
    RequestBody body1 = RequestBody.create(mediaType, "{\r\n    \"filter\": {\r\n        \"price\":null,\r\n        \"categories\": "+jsonArray1+"\r\n    }\r\n   \r\n}");
    RequestBody body = RequestBody.create(mediaType,"{\r\n    \"filter\": {\r\n        \"price\":null,\r\n        \"categories\":  "+jsonArray1+"\r\n    },\r\n   \"page\":"+page+"\r\n}");
    Request request = new Request.Builder()
            .url(BuildConfig.BASE_URL+"mobile/api/v2/shop/products")
            .method("POST",body)
            .addHeader("Authorization",usrrToken)
            .addHeader("Content-Type","application/json")
            //.addHeader("Cookie", "khetiigaadi_session=eyVmKtezWX8vLTb0Ih9DaP5J7oWcOvqnIjAwvull")
            .build();
    try {
        Response response = client.newCall(request).execute();

      String resp = response.body().string();
        Log.d("vcxbcvbcvxbcb===",""+usrr.getToken()+"=="+response.code()+"=="+resp+"=="+response.isSuccessful()+"=="+response.body().toString()+"==");

        if(response.isSuccessful()) {
            if(response.code()==200) {
                JSONObject jsonObject = new JSONObject(resp);
                JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                JSONArray jsonArray = jsonObject1.optJSONArray("data");
                Gson gson = new Gson();
                JSONObject jsonObject22 = jsonObject.getJSONObject("others");
                Others others = gson.fromJson(jsonObject22.toString(),Others.class);
                List<Product> lprv= new ArrayList<>();
                for (int i=0;i<jsonArray.length();i++) {
                 JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                    Product  data = gson.fromJson(jsonObject2.toString(),Product.class);
                    lprv.add(data);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (page == 1) {
                                agricultureToolsAdapter.setOthers(others);
                                agricultureToolsAdapter.updateItems(lprv);
                            }
                            else
                                agricultureToolsAdapter.addItems(lprv,page,others);
                        }
                        catch(Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
            else {

            }
        }
        else {

        }
    }
    catch(IOException e) {
        //e.printStackTrace();
       // throw new RuntimeException(e);
    }
    catch(JSONException e) {
      //  throw new RuntimeException(e);
    }
}
    public void getDataValFilter(int page) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runnableS();
            }
        });
        SessionManager sessionManager = new SessionManager(this);
        User usrr = sessionManager.getUser();
        // catId =
        // usrr.getToken();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        // RequestBody body = RequestBody.create(mediaType, "{\r\n    \"filter\": {\r\n        \"price\":null,\r\n        \"categories\": [\r\n            {\r\n                \"id\": 3\r\n            }\r\n        ]\r\n    }\r\n   \r\n}");

        List<Category> cvc = viewModel.getFilter().getCategories();
        Price price = viewModel.getFilter().getPrice();
        Gson gson = new Gson();
        String jsonPrice = gson.toJson(price);
        JSONArray jsonArray1 = new JSONArray();
        for(int i=0;i<cvc.size();i++) {
            Category category = cvc.get(i);
            // Create a Gson instance
            // Convert the User object to a JSON string
            String json = gson.toJson(category);
            try {
                JSONObject jsonObject = new JSONObject(json);
               // jsonArray1.put(jsonObject);

                jsonArray1.put(new JSONObject().put("id",jsonObject.getInt("id")));


            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        Log.d("fdgdsgdgdf===",""+jsonPrice+"=="+jsonArray1);
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"filter\": {\r\n        \"price\":"+jsonPrice+",\r\n        \"categories\": "+jsonArray1+"\r\n    }\r\n   \r\n}");
        Request request = new Request.Builder()
                .url(BuildConfig.BASE_URL+"mobile/api/v2/shop/products")
                .method("POST",body)
                .addHeader("Authorization",usrr.getToken())
                .addHeader("Content-Type","application/json")
                //.addHeader("Cookie", "khetiigaadi_session=eyVmKtezWX8vLTb0Ih9DaP5J7oWcOvqnIjAwvull")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String resp = response.body().string();
            Log.d("vcxbcvbcvxbcb===",""+usrr.getToken()+"=="+response.code()+"=="+resp+"=="+response.isSuccessful()+"=="+response.body().toString()+"==");
            if(response.isSuccessful()) {
                if(response.code()==200) {
                    JSONObject jsonObject = new JSONObject(resp);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                    JSONArray jsonArray = jsonObject1.optJSONArray("data");
                   // Gson gson = new Gson();
                    JSONObject jsonObject22 = jsonObject.getJSONObject("others");
                    Others others = gson.fromJson(jsonObject22.toString(),Others.class);
                    List<Product> lprv= new ArrayList<>();
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                        Product  data = gson.fromJson(jsonObject2.toString(),Product.class);
                        lprv.add(data);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (page == 1) {
                                    agricultureToolsAdapter.setOthers(others);
                                    agricultureToolsAdapter.updateItems(lprv);
                                }
                                else
                                    agricultureToolsAdapter.addItems(lprv,page,others);
                            }
                            catch(Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
                else {

                }
            }
            else{

            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void runnableS() {
        binding.myStickyButton.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                binding.myStickyButton.setVisibility(View.GONE);
                handler.postDelayed(this,2000);
            }
        };
        handler.postDelayed(r,2000);
    }
    public void setToolbarTitle(String title) {
        binding.toolbars.toolbar.setTitle(getResources().getString(R.string.shop));
    }
}