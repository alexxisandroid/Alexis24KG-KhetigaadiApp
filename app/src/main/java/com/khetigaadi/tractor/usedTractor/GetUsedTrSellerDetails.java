package com.khetigaadi.tractor.usedTractor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.R;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.databinding.ActivityGetUsedTrSellerDetailsBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.user.SessionManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
@AndroidEntryPoint
public class GetUsedTrSellerDetails extends BaseActivity {
    private static final String TAG = GetUsedTrSellerDetails.class.getSimpleName();
    private String dealerId;
    @Inject
    Tracker mTracker;
    private ActivityGetUsedTrSellerDetailsBinding binding;
    @Inject
    KhetiAPIs khetiAPIs;
    @Inject
    CompositeDisposable disposable;
    @Inject
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetUsedTrSellerDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        user=session.getUser();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dealerId = bundle.getString("tractorId");
        }
        initToolbar();
        findViewById();
        binding.riSubmit.setOnRippleCompleteListener(rippleView -> {
            // do something
            if (validation()) {
                getDetails();

            }
        });
        binding.llGetDealerDetails.setVisibility(View.VISIBLE);
        binding.llProgress.setVisibility(View.GONE);
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getResources().getString(R.string.contact_details));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.contact_details));
    }
    private void findViewById() {
        Glide.with(GetUsedTrSellerDetails.this)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
    }
    private void getDetails() {
        binding.llProgress.setVisibility(View.VISIBLE);
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user.getId());
        params.put("used_tractor_id", dealerId);
        disposable.add(khetiAPIs.getSellerContact(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse response) {
                        Toast.makeText(GetUsedTrSellerDetails.this, response.getMsg().get(0), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(GetUsedTrSellerDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }));
    }
    private boolean validation() {
        if (!binding.cbAgree.isChecked()) {
            Toast.makeText(this, getString(R.string.please_check_agree_button), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    protected void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
