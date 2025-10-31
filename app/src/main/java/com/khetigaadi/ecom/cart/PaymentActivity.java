package com.khetigaadi.ecom.cart;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.khetigaadi.AppController;
import com.khetigaadi.R;
import com.khetigaadi.databinding.ActivityPaymentBinding;
import com.khetigaadi.ecom.order.MyOrderListActivity;
import com.khetigaadi.shop.model.Order;
import com.khetigaadi.shop.payu.AppEnvironment;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;
import com.payu.base.models.ErrorResponse;
import com.payu.base.models.PayUPaymentParams;
import com.payu.checkoutpro.PayUCheckoutPro;
import com.payu.checkoutpro.utils.PayUCheckoutProConstants;
import com.payu.ui.model.listeners.PayUCheckoutProListener;
import com.payu.ui.model.listeners.PayUHashGenerationListener;
import org.jetbrains.annotations.Nullable;
import java.security.MessageDigest;
import java.util.HashMap;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class PaymentActivity extends AppCompatActivity {
    private static final String TAG = "PaymentActivity";
    private ActivityPaymentBinding binding;
    private PaymentViewModel viewModel;
    @Inject
    SessionManager sessionManager;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        viewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        initToolbar();
        user = sessionManager.getUser();
        binding.btnContinue.setOnClickListener(v -> {
            if (binding.radioOnline.isChecked() || binding.radioCashOnDelivery.isChecked()) {
                if (binding.radioOnline.isChecked()) {
                    viewModel.setPaymentTypeOnline(true);
                    Order order = viewModel.getOrder().getValue();
                    if (order != null) {
                        launchPayUMoneyFlow(order.getTotalBill());
                    }
                } else {
                    viewModel.setPaymentTypeOnline(false);
                    placeOrder(null);
                }
            } else {
                Toast.makeText(this,R.string.please_choose_payment_option,Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getOrder().observe(this,order -> binding.tvAmount.setText(String.format("%s %s", getString(R.string.rs), order.getTotalBill())));
    }
    private void placeOrder(String payuResp) {
        viewModel.insertOrder(payuResp).observe(this,myResponseStateData -> {
            switch (myResponseStateData.getStatus()) {
                case LOADING:
                    binding.btnContinue.setEnabled(false);
                    break;
                case SUCCESS:
                    binding.btnContinue.setEnabled(true);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(CartActivity.TAG));
                    if (myResponseStateData.getData() != null) {
                        Toast.makeText(this, myResponseStateData.getData().getMsg().get(0), Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(this,MyOrderListActivity.class));
                    finish();
                    break;
                case ERROR:
                    binding.btnContinue.setEnabled(true);
                    Toast.makeText(this, myResponseStateData.getError(),Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.payment));
    }
    private void launchPayUMoneyFlow(String totalBill) {
        PayUPaymentParams.Builder builder = new PayUPaymentParams.Builder();
        double amount = 0;
        try {
            amount = Double.parseDouble(totalBill);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        String txnId = "kheti" + System.currentTimeMillis();
        String phone = user.getMobile().trim();
        Order order = viewModel.getOrder().getValue();
        String productName = ""; // null;
        if (order != null) {
            productName = order.getProducts().get(0).getProduct().getName();
        }
        String firstName = user.getFname() != null ? user.getFname() : "KhetiShop";
        String email = user.getEmail() != null ? user.getEmail().trim() : "devkhetigaadi@gmail.com";
        AppEnvironment appEnvironment = ((AppController) getApplication()).getAppEnvironment();
        builder.setTransactionId(txnId)
                .setAmount(String.valueOf(amount))
                .setPhone(phone)
                .setProductInfo(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setSurl(appEnvironment.surl())
                .setFurl(appEnvironment.furl())
                .setIsProduction(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key());
        try {
            PayUPaymentParams mPaymentParams = builder.build();
            PayUCheckoutPro.open(this, mPaymentParams, new PayUCheckoutProListener() {
                @Override
                public void setWebViewProperties(@Nullable WebView webView, @Nullable Object o) {

                }
                @Override
                public void onPaymentSuccess(@NonNull Object response) {
                    binding.btnContinue.setEnabled(true);
                    HashMap<String, Object> result = (HashMap<String, Object>) response;
                    String payuResponse = (String) result.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE);
                    placeOrder(payuResponse);
                }
                @Override
                public void onPaymentFailure(@NonNull Object o) {
                    Toast.makeText(PaymentActivity.this,getString(R.string.payment_failed_please_try_again),Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onPaymentCancel(boolean b) {
                    Toast.makeText(PaymentActivity.this,getString(R.string.payu_payment_cancelled),Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onError(@NonNull ErrorResponse errorResponse) {
                    Timber.e(errorResponse.getErrorMessage());
                    Toast.makeText(PaymentActivity.this, errorResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void generateHash(@NonNull HashMap<String,String> valueMap, @NonNull PayUHashGenerationListener hashGenerationListener) {
                    String hashName = valueMap.get(PayUCheckoutProConstants.CP_HASH_NAME);
                    String hashData = valueMap.get(PayUCheckoutProConstants.CP_HASH_STRING);
                    if (!TextUtils.isEmpty(hashName) && !TextUtils.isEmpty(hashData)) {
                        //Generate Hash from your backend here
                        AppEnvironment appEnvironment = ((AppController) getApplication()).getAppEnvironment();
                        String salt = appEnvironment.salt();
                        if (valueMap.containsKey(PayUCheckoutProConstants.CP_POST_SALT))
                            salt = salt + "" + (valueMap.get(PayUCheckoutProConstants.CP_POST_SALT));
                        String hash = null;
                        //Calculate SHA-512 Hash here
                        hash = calculateHash(hashData + salt);
                        HashMap<String, String> dataMap = new HashMap<>();
                        Log.e(TAG, "hashData: " + hashData);
                        dataMap.put(hashName, hash);
                        hashGenerationListener.onHashGenerated(dataMap);
                    }
                }
            });
        }
        catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            Timber.e("launchPayUMoneyFlow: %s",e.getMessage());
            binding.btnContinue.setEnabled(true);
        }
    }
    private String calculateHash(String hashString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            return getHexString(mdbytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    private String getHexString(byte[] array) {
        StringBuilder hash = new StringBuilder();
        for (byte hashByte : array) {
            hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
        }
        return hash.toString();
    }
}