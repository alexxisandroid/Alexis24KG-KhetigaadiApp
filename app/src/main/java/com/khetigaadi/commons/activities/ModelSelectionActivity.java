package com.khetigaadi.commons.activities;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.agri.activity.BrandSelectionActivity;
import com.khetigaadi.commons.model.ModelResp;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.base.room.repository.utils.Resource;
import com.khetigaadi.base.room.repository.utils.Status;
import com.khetigaadi.commons.adapter.ModelSelectionAdapter;
import com.khetigaadi.databinding.ActivityModelSelectionBinding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.room.repository.ModelRepository;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class ModelSelectionActivity extends BaseActivity {
    private static final String TAG = "ModelSelectionActivity";
    public static final String SELECTED_MODEL = "selectedModel";
    public static final String SELECTED_BRAND = "selectedBrand";
    private ModelSelectionAdapter mAdapter;
    public static final int MODEL_SELECTION_REQ = 44;
    @Inject
    ModelRepository modelRepository;
    private Brand brand;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject
    KhetiAPIs khetiAPIs;
    private ActivityModelSelectionBinding binding;
    String brand_idV;
    String brand_nameV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityModelSelectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);
        brand = Objects.requireNonNull(getIntent().getExtras()).getParcelable(SELECTED_BRAND);
        brand_idV = getIntent().getStringExtra("brand_idV");
        brand_nameV = getIntent().getStringExtra("brand_nameV");
        findViewById();
        initToolbar();
        initView();

        try {
          if(brand_idV==null || brand_idV.equalsIgnoreCase(null) || brand_idV.equalsIgnoreCase("null")) {
              brand_idV = String.valueOf(brand.getId());
          }
          if(brand_nameV==null || brand_nameV.equalsIgnoreCase(null) || brand_nameV.equalsIgnoreCase("null")) {
                brand_nameV = String.valueOf(brand.getName());
          }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        @BrandType int type = getIntent().getExtras().getInt(BrandSelectionActivity.BRAND_TYPE, BrandType.NEW_TRACTOR);
        Log.d("dgfgfgsad=234==",""+brand_idV+"=="+(type==BrandType.OLD_TRACTOR)+"=="+brand+"=="+brand.getId()+"+=="+brand);
        if (brand != null)
            switch (type) {
                case BrandType.NEW_TRACTOR: {
                    modelRepository.getNewTractorModels(Integer.parseInt(brand_idV)).observe(this, this::setDataToAdapter);
                    break;
                }
                case BrandType.OLD_TRACTOR: {
                    modelRepository.getOldTractorModels(Integer.parseInt(brand_idV)).observe(this, this::setDataToAdapter);
                    break;
                }
                case BrandType.IMPLEMENT:
                    break;
            }
    }
    private void setDataToAdapter(Resource<List<Model>> listResource) {
        //  Log.d("dgfgfgsad6856===",""+brand.getId()+"=="+listResource.getStatus()+"=="+listResource.getData().size());

        if (listResource.getStatus()==Status.SUCCESS) {
            binding.llProgresss.llProgress.setVisibility(View.GONE);
            mAdapter.setData(listResource.getData());
            binding.edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s,int start,int before,int count) {
                    mAdapter.getFilter().filter(s.toString());
                }
                @Override
                public void beforeTextChanged(CharSequence s,int start,int count,int after) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
        else {
            getModels();
        }
        //  else
        // Toast.makeText(this,getResources().getString(R.string.please_try_after_some_time),Toast.LENGTH_SHORT).show();
    }
    private void setDataToAdapter(@NonNull List<Model> listResource) {
        Log.d("dgfgfgsad68===",""+brand.getId()+"=="+listResource.size());
        if (listResource.size()>0) {
                binding.llProgresss.llProgress.setVisibility(View.GONE);
                mAdapter.setData(listResource);
                binding.edSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s,int start,int before,int count) {
                        mAdapter.getFilter().filter(s.toString());
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s,int start,int count,int after) {

                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
        }
        else {
            getModels();
        }
           // Toast.makeText(this,getResources().getString(R.string.please_try_after_some_time),Toast.LENGTH_SHORT).show();
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.model));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.model));
    }
    private void findViewById() {
        Glide.with(ModelSelectionActivity.this).load(R.drawable.loader_white).into(binding.llProgresss.ivLoader);
    }
    private void initView() {
        mAdapter = new ModelSelectionAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(ModelSelectionActivity.this);
        binding.mRecyclerView.setHasFixedSize(true);
        binding.mRecyclerView.setLayoutManager(manager);
        binding.mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(brand -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SELECTED_MODEL, brand);
            setResult(RESULT_OK, returnIntent);
            finish();
        });
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) binding.mRecyclerView.getLayoutParams();
        marginLayoutParams.setMargins(0,0,0,0);
        binding.mRecyclerView.setLayoutParams(marginLayoutParams);
    }
    @Override
    protected void onPause() {
        super.onPause();
        hideSoftKeyboard(ModelSelectionActivity.this);
    }
    public void getModels() {
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        Log.d("dgfgfgsad===",""+brand.getId()+"=="+brand_idV);
        if(brand_idV==null || brand_idV.equalsIgnoreCase(null) || brand_idV.equalsIgnoreCase("null") || brand_idV.isEmpty())
            brand_idV = String.valueOf(brand.getId());
        disposable.add(khetiAPIs.getNewTractorModels(Integer.parseInt(brand_idV))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ModelResp>() {
                    @Override
                    public void onSuccess(@NotNull ModelResp safetyTipsResp) {
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                        try {
                           // binding.tvTips.setText(Html.fromHtml(safetyTipsResp.getData().getContent()));
                            Log.d("dgfgfgsad22===",""+brand.getId());
                            List<Model> listResource = safetyTipsResp.getData().getData();
                            Log.d("dgfgfgsad221234===",""+listResource.size());
                            binding.llProgresss.llProgress.setVisibility(View.GONE);
                            mAdapter.setData(listResource);
                            binding.edSearch.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void onTextChanged(CharSequence s,int start,int before,int count) {
                                    mAdapter.getFilter().filter(s.toString());
                                }
                                @Override
                                public void beforeTextChanged(CharSequence s,int start,int count,int after) {
                                }
                                @Override
                                public void afterTextChanged(Editable s) {
                                }
                            });
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
                }));
    }
}
