package com.khetigaadi.tractor.usedTractor;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.databinding.ActivityGetLoanBinding;
import com.khetigaadi.pick_image.CropParam;
import com.khetigaadi.pick_image.GalleryImagesActivity;
import com.khetigaadi.tractor.usedTractor.service.GetLoanImagesService;
import java.util.Objects;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class GetLoanActivity extends BaseActivity {
    private ActivityGetLoanBinding binding;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        handleImageResult(data.getData());
                    }
                }
            });
    private boolean isBack;
    private GetLoanViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ActivityGetLoanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);
        viewModel = new ViewModelProvider(this).get(GetLoanViewModel.class);
        initToolbar();
        binding.toggleButton.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            Timber.e("onCreate:  isChecked");
            if (isChecked) {
                if (R.id.btnNewTractor == checkedId) {
                    viewModel.setTypeOfFinance("NEW TRACTOR");
                } else {
                    viewModel.setTypeOfFinance("OLD TRACTOR");
                }
            }
        });
        binding.rgLoanAmount.setOnCheckedChangeListener((radioGroup,checkedId) -> {
            if (checkedId == R.id.radio1To2) {
                viewModel.setLoanAmount(getString(R.string._1_2_lac));
            } else if (checkedId == R.id.radio2To3) {
                viewModel.setLoanAmount(getString(R.string._2_3_lac));
            } else if (checkedId == R.id.radio3To4) {
                viewModel.setLoanAmount(getString(R.string._3_4_lac));
            } else if (checkedId == R.id.radio4To5) {
                viewModel.setLoanAmount(getString(R.string._4_5_lac));
            } else if (checkedId == R.id.radioAbove5) {
                viewModel.setLoanAmount(getString(R.string.greater_than_5_lac));
            }
        });
        binding.btnApply.setOnClickListener(view12 -> {
            if (validate()) {
                viewModel.callGetLoanApi().observe(this, getLoanRespStateData -> {
                    switch (getLoanRespStateData.getStatus()) {
                        case LOADING -> showProgressDialog(getString(R.string.please_wait));
                        case SUCCESS -> {
                            dismissProgressDialog();
                            @SuppressLint("RestrictedApi") WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(GetLoanImagesService.class)
                                    .setInputData(new Data.Builder()
                                            .putString(GetLoanImagesService.ID, getLoanRespStateData.getData().getData().getId())
                                            .putString(GetLoanImagesService.FRONT, viewModel.getFrontImage().getPath())
                                            .putString(GetLoanImagesService.BACK, viewModel.getBackImage().getPath())
                                            .build())
                                    .build();
                            WorkManager
                                    .getInstance(GetLoanActivity.this)
                                    .enqueue(uploadWorkRequest);
                            Toast.makeText(GetLoanActivity.this, getLoanRespStateData.getData().getMsg().get(0), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        case ERROR -> {
                            dismissProgressDialog();
                            Toast.makeText(GetLoanActivity.this, getLoanRespStateData.getError(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        binding.btnCallNow.setOnClickListener(view13 -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.whatsAppNo)));
            startActivity(intent);
        });
        binding.btnWhatsApp.setOnClickListener(view1 -> MainActivity.onWhatsAppClick(GetLoanActivity.this));
        binding.ivFront.setOnClickListener(view1 -> {
            isBack = false;
            displayImageSelectionDialog();
        });
        binding.ivBack.setOnClickListener(view1 -> {
            isBack = true;
            displayImageSelectionDialog();
        });
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.get_loan));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.get_loan));
    }
    private void displayImageSelectionDialog() {
        CropParam cropParams = new CropParam();
        cropParams.setCropImageName(System.currentTimeMillis()+".png");
        Intent intent = new Intent(this,GalleryImagesActivity.class);
        intent.putExtra("cropParams",cropParams);
        galleryActivityResultLauncher.launch(intent);
    }
    private boolean validate() {
        if (TextUtils.isEmpty(viewModel.getFinanceType())) {
            Toast.makeText(this,getString(R.string.please_select_finance_type),Toast.LENGTH_SHORT).show();
            return false;
        } else if(TextUtils.isEmpty(viewModel.getLoanAmount())) {
            Toast.makeText(this,getString(R.string.please_select_loan_amount),Toast.LENGTH_SHORT).show();
            return false;
        } else if(viewModel.getFrontImage() == null) {
            Toast.makeText(this,getString(R.string.please_select_aadhar_front_image),Toast.LENGTH_SHORT).show();
            return false;
        } else if(viewModel.getBackImage() == null) {
            Toast.makeText(this,getString(R.string.please_select_aadhar_back_image),Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    private void handleImageResult(Uri uriFromBitmap) {
        if(!isBack) {
            binding.ivFront.setImageURI(uriFromBitmap);
            viewModel.setFrontImage(uriFromBitmap);
        } else {
            binding.ivBack.setImageURI(uriFromBitmap);
            viewModel.setBackImage(uriFromBitmap);
        }
    }
}