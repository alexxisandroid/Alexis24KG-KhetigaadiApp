package com.khetigaadi.tractor.newTractor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.khetigaadi.R;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.databinding.ActivityNewTrGetOnRoadPriceBinding;
import com.khetigaadi.def.PriceType;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.user.SessionManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
@AndroidEntryPoint
public class NewTrGetOnRoadPriceActivity extends BaseActivity {
    private String id;
    private String trName;
    //    private User user;
    private String toolbarLabel;
    @PriceType
    private int priceType;
    @Inject
    KhetiAPIs khetiAPIs;
    private ActivityNewTrGetOnRoadPriceBinding binding;
    private String imageUrl;
    @Inject
    SessionManager session;
    CompositeDisposable composite = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ActivityNewTrGetOnRoadPriceBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);

        user = session.getUser();
        readParameters(getIntent().getExtras());
        initToolbar();
        initUi();
        binding.riRequest.setOnRippleCompleteListener(rippleView -> {
            if (validation()) {
                if (priceType == PriceType.TRACTOR) {
                    sendRequest();
                } else if (priceType == PriceType.IMPLEMENT) {
                    sendImpRequest();
                } else if (priceType == PriceType.HARVESTER) {
                    sendHarvesterRequest();
                }
            }
        });
    }
    private void initUi() {
        Glide.with(this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
        binding.tvName.setText(trName);
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)
                .into(binding.ivImage);
    }
    private void sendHarvesterRequest() {
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("harvester_id", id);
        composite.add(khetiAPIs.getHarvesterPrice(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        Toast.makeText(NewTrGetOnRoadPriceActivity.this, myResponse.getMsg().get(0), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(NewTrGetOnRoadPriceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
                }));
    }
    private void sendImpRequest() {
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("implement_id", id);
        params.put("user_id", user.getId());
        composite.add(khetiAPIs.getImplementPrice(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        Toast.makeText(NewTrGetOnRoadPriceActivity.this,myResponse.getMsg().get(0),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(NewTrGetOnRoadPriceActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
                }));
    }
    private void readParameters(Bundle bundle) {
        if (bundle != null) {
            id = bundle.getString("id");
            trName = bundle.getString("name");
            imageUrl = bundle.getString("image");
            priceType = bundle.getInt("priceType", PriceType.TRACTOR);
            toolbarLabel = bundle.getString("toolbarlabel");
        }
    }
    private void sendRequest() {
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("tractor_id", id);
        params.put("user_id", user.getId());
        composite.add(khetiAPIs.getNewTractorPrice(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        Toast.makeText(NewTrGetOnRoadPriceActivity.this,myResponse.getMsg().get(0),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(NewTrGetOnRoadPriceActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
                }));
    }
    private boolean validation() {
        if (!binding.cbAgree.isChecked()) {
            Snackbar.make(binding.llContainer, getResources().getString(R.string.please_check_agree_button), Snackbar.LENGTH_LONG).show();
        } else {
            return true;
        }
        return false;
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(Objects.requireNonNullElseGet(toolbarLabel, () -> getString(R.string.getOnRoadPrice)));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.getOnRoadPrice));
    }
}
