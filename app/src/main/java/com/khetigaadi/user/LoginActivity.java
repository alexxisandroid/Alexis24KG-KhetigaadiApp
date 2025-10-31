package com.khetigaadi.user;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.*;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.khetigaadi.FcmApi;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.GetMobileDialogBinding;
import com.khetigaadi.databinding.LoginBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.user.model.User;
import com.khetigaadi.user.model.UserResponse;
import com.khetigaadi.user.registration.ConfirmRegistrationActivity;
import com.khetigaadi.user.retrofit.UserAPIs;
import com.khetigaadi.util.Utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
/** @noinspection unchecked*/
@AndroidEntryPoint
public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    public static LoginActivity loginActivity;
    @Inject
    Tracker mTracker;
    private GoogleSignInClient mGoogleApiClient;
    private final int RC_SIGN_IN = 100;
    private UserResponse user;
    @Inject
    SessionManager session;
    @Inject
    SharedPreferenceUtils pref;
    @Inject
    UserAPIs userAPIs;
    LoginBinding binding;
    private FirebaseAuth firebaseAuth;
    @Inject
    CompositeDisposable disposable;
    @Inject
    KhetiAPIs khetiAPIs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         try {
             WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
             binding = LoginBinding.inflate(getLayoutInflater());
             View view = binding.getRoot();
             setContentView(view);

             ViewCompat.setOnApplyWindowInsetsListener(view, (v,insets) -> {
                 Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                 v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
                 return insets;
             });
             EdgeToEdge.enable(this);
             initToolbar();
             binding.etMobile.addTextChangedListener(new MyTextWatcher(binding.etMobile));
             binding.btnLogin.setOnRippleCompleteListener(rippleView -> {
                 if (validateMobile()) {
                     if (Constants.isConnectionAvailable(LoginActivity.this)) {
                         Intent intent = new Intent(LoginActivity.this, ConfirmRegistrationActivity.class);
                         User user = new User();
                         user.setMobile(binding.etMobile.getText().toString());
                         intent.putExtra("user",user);
                         startActivity(intent);
                     } else {
                         noInternetConnection(binding.parent);
                     }
                 }
             });
             //Setting onclick listener to signing button
             binding.signInWithGoogle.setOnClickListener(v -> {
                 if (Constants.isConnectionAvailable(LoginActivity.this)) {
                     showProgressDialog(getString(R.string.loading),this);
                     startActivityForResult(mGoogleApiClient.getSignInIntent(),RC_SIGN_IN);
                 } else {
                     noInternetConnection(binding.parent);
                 }
             });
             loginActivity = this;
             configureGoogleClient();
         }
         catch (Exception b){
             Log.d("xcvcvxvxvxvcxvx2211aa====",""+b.getLocalizedMessage());
        }
        Log.d("xcvcvxvxvxvcxvx2211=====","");
    }
    private void configureGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = GoogleSignIn.getClient(this, gso);
        binding.signInWithGoogle.setSize(SignInButton.SIZE_WIDE);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    private void initToolbar() {
      setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24); //abc_ic_ab_back_material
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getResources().getString(R.string.btn_login));
        binding.toolbars.toolbar.setBackgroundColor(Color.WHITE);
        binding.toolbars.toolbar.setAlpha(0);
        //  binding.toolbars.toolbar.setVisibility(View.GONE);
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RC_SIGN_IN) {
                dismissProgressDialog();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    handleSignInResult(task.getResult(ApiException.class));
                } catch (ApiException e) {
                    Timber.tag(TAG).w(e, "Google sign in failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            Timber.d("signInWithCredential:success: currentUser: %s", user.getEmail());
                            loginOrRegisterWithSocialMedia(user.getDisplayName(), user.getEmail(), null);
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.tag(TAG).w(task.getException(), "signInWithCredential:failure");
                        Toast.makeText(LoginActivity.this, "Login Failed ", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void showDialogForMobile(User user) {
        final Dialog d = new Dialog(LoginActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setCancelable(false);
        d.setCanceledOnTouchOutside(true);
        GetMobileDialogBinding dialogBinding = GetMobileDialogBinding.inflate(getLayoutInflater());
        d.setContentView(dialogBinding.getRoot());
        d.show();
        dialogBinding.tvMobile.setText(getMyPhoneNumber());
        dialogBinding.btnSubmit.setOnClickListener(v -> {
            if (Utils.isValidPhone(dialogBinding.tvMobile.getText().toString())) {
                user.setMobile(dialogBinding.tvMobile.getText().toString());
                SharedPreferenceUtils.getInstance(LoginActivity.this).setValue(Constants.TOKEN_ID, user.getToken());
                callUpdateProfile(user);
                d.dismiss();
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.err_msg_mobile), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void callUpdateProfile(User user) {
        disposable.add(userAPIs.updateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserResponse>() {
                    @Override
                    public void onSuccess(@NonNull UserResponse userResponse) {
                        if (userResponse.getUser().getMobile() != null) {
                            SharedPreferenceUtils.getInstance(LoginActivity.this).setValue(Constants.TOKEN_ID, user.getToken());
                            afterLoginSuccessfully(userResponse);
                        } else {
                            Toast.makeText((Context) LoginActivity.this,""+userResponse.getMsg().get(0),Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onFailure: ");
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }
    public String getMyPhoneNumber() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return null;
                }
            }
            return ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getLine1Number();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void afterLoginSuccessfully(UserResponse userResponse) {
        // Create login session
        session.setUser(userResponse.getUser());
        session.setUserOthers(userResponse.getOthers());
        FcmApi.registerFCMToken(userAPIs, session, () -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });

    }
    private void loginOrRegisterWithSocialMedia(final String name,final String email,final String mobile) {
//        llProgress.setVisibility(View.VISIBLE);
        showProgressDialog(getString(R.string.loading),this);
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            if (!TextUtils.isEmpty(name)) {
                List<String> s = Arrays.asList(name.split(" "));
                params.put("first_name", s.size() > 0 ? s.get(0) : "");
                params.put("last_name", s.size() > 1 ? s.get(1) : "");
            }
            disposable.add(khetiAPIs.loginWithSocialMedia(params)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<UserResponse>() {
                        @Override
                        public void onSuccess(@NonNull UserResponse user) {
                            dismissProgressDialog();
                            try {
                                if (!user.isError()) {
                                    if (user.getUser() != null) {
                                        if (Utils.isValidPhone(user.getUser().getMobile())) {
                                            SharedPreferenceUtils.getInstance(LoginActivity.this).setValue(Constants.TOKEN_ID, user.getUser().getToken());
                                            afterLoginSuccessfully(user);
                                        } else {
                                            showDialogForMobile(user.getUser());
                                        }
                                    }
                                } else {
                                    Toast.makeText((Context) LoginActivity.this,""+user.getMsg().get(0), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Timber.e("userLogin Error: %s", e.getMessage());
                            dismissProgressDialog();
                        }
                    }));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private class MyTextWatcher implements TextWatcher {
        private final View view;
        public MyTextWatcher(View view) {
            this.view = view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            if (view.getId() == R.id.et_mobile) {
                validateMobile();
            }
        }
    }
    private boolean validateMobile() {
        String mobile = binding.etMobile.getText().toString().trim();
        if (mobile.isEmpty() || !Utils.isValidPhone(mobile)) {
            binding.tilMobile.setError(getString(R.string.err_msg_mobile));
            requestFocus(binding.etMobile);
            return false;
        } else {
            binding.tilMobile.setErrorEnabled(false);
        }

        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        Constants.hideSoftKeyboard(this);
    }
}
