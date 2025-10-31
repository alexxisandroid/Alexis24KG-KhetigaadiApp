package com.khetigaadi.tractor.rent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
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
import com.khetigaadi.R;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.RentTrSendEnqBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;
import com.khetigaadi.util.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class RentTractorSendEnquiry extends BaseActivity {
    private final String TAG = RentTractorSendEnquiry.class.getSimpleName();
    private String tractorId = "1";
    @Inject
    Tracker mTracker;
    @Inject
    KhetiAPIs khetiAPIs;
    @Inject
    SessionManager session;
    private User user;
    private RentTrSendEnqBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = RentTrSendEnqBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);

        user = session.getUser();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tractorId = bundle.getString("tractorId");
        }
        initView();
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getResources().getString(R.string.send_email_enquiry));
        if (user.getMobile() != null) {
            binding.inputMobile.setText(user.getMobile());
        }
        if (user.getEmail() != null) {
            binding.inputEmail.setText(user.getEmail());
        }
        if (!Constants.isConnectionAvailable(this)) {
            noInternetConnection(binding.parent);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    private void initView() {
        binding.inputMsg.addTextChangedListener(new MyTextWatcher(binding.inputMsg));
        binding.inputEmail.addTextChangedListener(new MyTextWatcher(binding.inputEmail));
        binding.inputMobile.addTextChangedListener(new MyTextWatcher(binding.inputMobile));
        binding.riSendEnquiry.setOnRippleCompleteListener(rippleView -> {
            if (validation()) {
                if (Constants.isConnectionAvailable(RentTractorSendEnquiry.this)) {
                    sendEnquiry();
                } else {
                    noInternetConnection(binding.parent);
                }
            }
        });
    }
    private void sendEnquiry() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> params = new HashMap<>();
        try {
            params.put("rent_tractor_id", tractorId);
            params.put("user_id", user.getId());
            khetiAPIs.rentTrEnquiry(params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<>() {
                        @Override
                        public void onSuccess(@NonNull MyResponse myResponse) {
                            binding.progressBar.setVisibility(View.GONE);
                            try {
                                if (!myResponse.isError()) {
                                    for (int i = 0; i < myResponse.getMsg().size(); i++) {
                                        Toast.makeText(RentTractorSendEnquiry.this, myResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                    }
                                    finish();
                                } else {
                                    for (int i = 0; i < myResponse.getMsg().size(); i++) {
                                        Toast.makeText(RentTractorSendEnquiry.this, myResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Timber.e("response Error: %s", e);
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean validation() {
        if (!validateMsg()) {
//        } else if (!validateEmail()) {
//        } else if (!validateMobile()) {
        } else {
            binding.inputLayoutMsg.setErrorEnabled(false);
            binding.inputLayoutMobile.setErrorEnabled(false);
            binding.inputLayoutEmail.setErrorEnabled(false);
            return true;
        }
        return false;
    }
    private class MyTextWatcher implements TextWatcher {
        private final View view;
        private MyTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void afterTextChanged(Editable editable) {
            if (view.getId() == R.id.input_msg) {
                validateMsg();
            } else if (view.getId() == R.id.input_email) {
                if (binding.inputEmail.getText().toString().trim().length() != 0) {
                    validateEmail();
                } else {
                    binding.inputLayoutEmail.setErrorEnabled(false);
                }
            } else if (view.getId() == R.id.input_mobile) {
                validateMobile();
            }
        }
    }
    private boolean validateMobile() {
        String mobile = binding.inputMobile.getText().toString().trim();
        if (!Utils.isValidPhone(mobile)) {
            binding.inputLayoutMobile.setError(getString(R.string.err_msg_mobile));
            requestFocus(binding.inputMobile);
            return false;
        } else {
            binding.inputLayoutMobile.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateEmail() {
        String email = binding.inputEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            binding.inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(binding.inputEmail);
            return false;
        } else {
            binding.inputLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean validateMsg() {
        String name = binding.inputMsg.getText().toString().trim();
        if (name.isEmpty()) {
            binding.inputLayoutMsg.setErrorEnabled(true);
            binding.inputLayoutMsg.setError(getString(R.string.err_msg_msg));
            requestFocus(binding.inputMsg);
            return false;
        } else {
            binding.inputLayoutMsg.setErrorEnabled(false);
        }
        return true;
    }
    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}

