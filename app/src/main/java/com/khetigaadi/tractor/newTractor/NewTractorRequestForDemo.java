package com.khetigaadi.tractor.newTractor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.snackbar.Snackbar;
import com.khetigaadi.R;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.databinding.NewTractorRequestForDemoBinding;
import com.khetigaadi.def.PriceType;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.user.SessionManager;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class NewTractorRequestForDemo extends BaseActivity {
    private final String TAG = NewTractorRequestForDemo.class.getSimpleName();
    private String id = "1";
    private String name = "";
    @PriceType
    private int type;
    @Inject
    Tracker mTracker;
    private NewTractorRequestForDemoBinding binding;
    @Inject
    KhetiAPIs khetiAPIs;
    private String imageUrl;
    @Inject
    SessionManager session;
    CompositeDisposable composite = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = NewTractorRequestForDemoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);

        user = session.getUser();
        readParameters(getIntent().getExtras());
        initToolbar();
        initView();
        binding.riRequest.setOnRippleCompleteListener(rippleView -> {
            if (validation()) {
                if (type == PriceType.TRACTOR) {
                    sendRequest();
                } else if (type == PriceType.IMPLEMENT) {
                    sendImplementRequest();
                } else if (type == PriceType.HARVESTER) {
                    sendHarvesterRequest();
                }
            }
        });
    }
    private void initView() {
        Glide.with(this)
                .load(R.drawable.loader_white)
                .into(binding.llProgress.ivLoader);
        binding.tvName.setText(name);
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)
                .into(binding.ivImage);
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getResources().getString(R.string.request_for_demo));
    }
    private void readParameters(Bundle bundle) {
        try {
            if (bundle != null) {
                id = bundle.getString("id");
                name = bundle.getString("name");
                imageUrl = bundle.getString("image");
                type = bundle.getInt("demoType", PriceType.TRACTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendRequest() {
        binding.llProgress.llProgress.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("tractor_id", id);
        params.put("user_id", user.getId());
        composite.add(khetiAPIs.getTractorGetDemo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        Toast.makeText(NewTractorRequestForDemo.this, myResponse.getMsg().get(0), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(NewTractorRequestForDemo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                    }
                }));

    }
    private void sendImplementRequest() {
        binding.llProgress.llProgress.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("implement_id", id);
        params.put("user_id", user.getId());
        composite.add(khetiAPIs.getImplementGetDemo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                        Toast.makeText(NewTractorRequestForDemo.this, myResponse.getMsg().get(0), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(NewTractorRequestForDemo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                    }
                }));
    }
    private void sendHarvesterRequest() {
        binding.llProgress.llProgress.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("harvester_id", id);
        composite.add(khetiAPIs.getHarvesterGetDemo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        binding.llProgress.llProgress.setVisibility(View.GONE);
                        Toast.makeText(NewTractorRequestForDemo.this, myResponse.getMsg().get(0), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(NewTractorRequestForDemo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.llProgress.llProgress.setVisibility(View.GONE);
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
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        Timber.i("Setting screen name: %s", TAG);
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
