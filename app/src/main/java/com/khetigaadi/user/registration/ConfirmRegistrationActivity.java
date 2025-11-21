package com.khetigaadi.user.registration;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.khetigaadi.FcmApi;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ComfirmRegBinding;
import com.khetigaadi.user.LoginActivity;
import com.khetigaadi.user.MySMSBroadcastReceiver;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.OtpResp;
import com.khetigaadi.user.model.User;
import com.khetigaadi.user.model.UserResponse;
import com.khetigaadi.user.retrofit.UserAPIs;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import timber.log.Timber;
@AndroidEntryPoint
public class ConfirmRegistrationActivity extends BaseActivity {
    private final String TAG = ConfirmRegistrationActivity.class.getSimpleName();
    @Inject
    Tracker mTracker;
    private User userParam;
    @Inject
    SessionManager session;
    private OtpResp otpResp;
    private int counter = 40;
    @Inject
    UserAPIs userAPIs;
    private MySMSBroadcastReceiver smsBroadcastReceivers;
    private ComfirmRegBinding binding;
    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ComfirmRegBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        EdgeToEdge.enable(this);
//        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
        readParams(getIntent().getExtras());
        toolbarInit();
        getCode(userParam,false);
        binding.btnSubmit.setOnClickListener(v -> {
            if (binding.inputCode.getText().length() == 4) {
                if (otpResp != null) {
                    if (binding.inputCode.getText().toString().equals(otpResp.getVerificationCode().getOtp())) {
                        userParam.setOtp(Integer.parseInt(binding.inputCode.getText().toString()));
                        loginOrRegistration(userParam);
                    } else {
                        Toast.makeText(ConfirmRegistrationActivity.this, getString(R.string.please_enter_correct_otp_code), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        initUI();
        startSmsListener();
    }
    private void startSmsListener() {
        smsBroadcastReceivers = new MySMSBroadcastReceiver();
        smsBroadcastReceivers.initOTPListener(new MySMSBroadcastReceiver.OTPReceiveListener() {
            @Override
            public void onOTPReceived(String otp) {
                binding.inputCode.setText(otp);
            }
            @Override
            public void onOTPTimeOut() {

            }
        });
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(smsBroadcastReceivers, intentFilter);
        SmsRetriever.getClient(this)
                .startSmsRetriever()
                .addOnSuccessListener(aVoid -> Timber.e("Successfully started retriever"))
                .addOnFailureListener(e -> Timber.e("Failed to start retriever"));
    }
    private void initUI() {
        if (userParam != null && userParam.getMobile() != null) {
            binding.tvMobile.setText(userParam.getMobile());
            binding.tvMobile.setVisibility(View.VISIBLE);
        } else {
            binding.tvMobile.setVisibility(View.GONE);
        }
        binding.tvTimer.setOnClickListener(rippleView -> {
            Log.d("user_paramm====",""+userParam);
            if (userParam != null) {
                getCode(userParam, true);
                startTimer();
            }
        });
        binding.tvTimer.setEnabled(false);
        countDownTimer = new CountDownTimer(counter * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvTimer.setText(String.format("%s Sec", counter));
                counter--;
            }

            public void onFinish() {
                binding.tvTimer.setEnabled(true);
                binding.tvTimer.setText(getString(R.string.resend_code));
            }
        }.start();
    }
    private void startTimer() {
        binding.tvTimer.setEnabled(false);
        counter = 40 * 2;
        countDownTimer.start();
    }
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.search);
        shareItem.setVisible(false);
        return true;
    }
    private void readParams(Bundle bundle) {
        if (bundle != null) {
            userParam = bundle.getParcelable("user");
        }
    }
    private void toolbarInit() {
         setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getResources().getString(R.string.verification_code));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.verification_code));
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    private void getCode(User userParam,boolean isCallFromResentOtp) {
        userAPIs.getOTP(userParam).enqueue(new Callback<OtpResp>() {
            @Override
            public void onResponse(@NotNull Call<OtpResp> call, retrofit2.@NotNull Response<OtpResp> response) {
                if (response.isSuccessful()) {
                    Log.d("vccxvxcvxv===",""+response.body().getVerificationCode().getOtp());
                    otpResp = response.body();
                    if (isCallFromResentOtp)
                        Toast.makeText(ConfirmRegistrationActivity.this, getString(R.string.verification_code_resent_successfully),Toast.LENGTH_SHORT).show();
                    if (otpResp != null && otpResp.isError()) {
                        Toast.makeText((Context) ConfirmRegistrationActivity.this,""+otpResp.getMsg().get(0),Toast.LENGTH_SHORT).show();
                    }
                }
                else   Log.d("vccxvxcvxv22===",""+response.body().getMsg());
            }
            @Override
            public void onFailure(@NotNull Call<OtpResp> call,@NotNull Throwable t) {
                Toast.makeText(ConfirmRegistrationActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.d("vccxvxcvxv223===",""+t.getMessage());

            }
        });
    }
    private void loginOrRegistration(User userParam) {
        showProgressDialog(getString(R.string.please_wait),this);
        userAPIs.loginWithOTP(userParam).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NotNull Call<UserResponse> call, retrofit2.@NotNull Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("ab===",""+response.body()+"=="+response.body().isError()+"=="+response.body().getUser()+"=="+userParam.toString());
                    if (response.body() != null) {
                        if (!response.body().isError()) {
                            if (response.body().getUser() != null) {
                                afterLoginSuccessfully(response.body());
                            }
                        } else {
                            Toast.makeText((Context)ConfirmRegistrationActivity.this,""+response.body().getMsg().get(0),Toast.LENGTH_LONG).show();
                        }
                    }
                    else Log.d("cxvvcx==",""+response.errorBody());
                }
                else  Log.d("abc===",""+response.body().getMsg());
            }

            @Override
            public void onFailure(@NotNull Call<UserResponse> call, @NotNull Throwable t) {
                Toast.makeText(ConfirmRegistrationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("abcd===",""+call.toString());
            }
        });
    }
    private void afterLoginSuccessfully(UserResponse user) {
        session.setLogin(true);
        session.setUser(user.getUser());
        session.setUserOthers(user.getOthers());
        SharedPreferenceUtils.getInstance(ConfirmRegistrationActivity.this).setValue(Constants.TOKEN_ID, user.getUser().getToken());
        FcmApi.registerFCMToken(userAPIs, session, this::navigateToMainActivity);
    }
    private void navigateToMainActivity() {
        dismissProgressDialog();
        startActivity(new Intent(ConfirmRegistrationActivity.this, MainActivity.class));
        if (LoginActivity.loginActivity != null)
            LoginActivity.loginActivity.finish();
        finish();
    }
    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (smsBroadcastReceivers != null) {
                smsBroadcastReceivers.initOTPListener(null);
                LocalBroadcastManager.getInstance(this).unregisterReceiver(smsBroadcastReceivers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
