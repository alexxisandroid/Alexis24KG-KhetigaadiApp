package com.khetigaadi.tractor.dealer;
import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.snackbar.Snackbar;
import com.khetigaadi.R;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.NewTractorRequestForDemoBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class GetDealersDetails extends BaseActivity {
    private static final String TAG = GetDealersDetails.class.getSimpleName();
    private String dealerId;
    @Inject
    Tracker mTracker;
    @Inject
    SessionManager session;
    @Inject
    KhetiAPIs khetiAPIs;
    private int position;
    private User user;
    private NewTractorRequestForDemoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = NewTractorRequestForDemoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);

        user = session.getUser();
        binding.ivImage.setVisibility(GONE);
        binding.tvName.setVisibility(GONE);
        readParamets(getIntent().getExtras());
        initToolbar();
        binding.riRequest.setOnRippleCompleteListener(rippleView -> {
            if (validation()) {
                if (Constants.isConnectionAvailable(GetDealersDetails.this)) {
                    getDetails();
                } else {
                    noInternetConnection(binding.parent);
                }
            }
        });
    }
    private void readParamets(Bundle bundle) {
        if (bundle != null) {
            dealerId = bundle.getString("dealerId");
            position = bundle.getInt("position");
        }
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.get_details));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.get_details));
    }
    private void getDetails() {
        Constants.hideSoftKeyboard(GetDealersDetails.this);
        Map<String, Object> params = new HashMap<>();
        try {
            params.put("dealer_id", dealerId);
            params.put("user_id", Integer.parseInt(user.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.llProgress.llProgress.setVisibility(View.VISIBLE);
      khetiAPIs.getDealerDetails(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        binding.llProgress.llProgress.setVisibility(GONE);
                        try {
                            if (!myResponse.isError()) {
                                for (int i = 0; i < myResponse.getMsg().size(); i++) {
                                    Toast.makeText(GetDealersDetails.this, myResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                }
                                Intent intent = new Intent();
                                intent.putExtra("position", position);
                                setResult(2, intent);
                                finish();
                            } else {
                                for (int i = 0; i < myResponse.getMsg().size(); i++) {
                                    Toast.makeText(GetDealersDetails.this, myResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("response Error: %s", e);
                        binding.llProgress.llProgress.setVisibility(GONE);
                    }
                });
    }
    private boolean validation() {
        if (!binding.cbAgree.isChecked()) {
            Snackbar.make(binding.llContainer, getResources().getString(R.string.please_check_agree_button), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public void requestFocus(View view) {
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
