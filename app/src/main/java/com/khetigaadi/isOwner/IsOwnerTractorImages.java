package com.khetigaadi.isOwner;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.IsOwnerImagesBinding;
import com.khetigaadi.pick_image.CropParam;
import com.khetigaadi.pick_image.GalleryImagesActivity;
import com.khetigaadi.retrofit.KhetiAPIs;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class IsOwnerTractorImages extends BaseActivity {
    private static final String TAG = IsOwnerTractorImages.class.getSimpleName();
    private boolean imageFirst;
    @Inject
    SharedPreferenceUtils sharedPref;
    @Inject
    KhetiAPIs khetiAPIs;
    private CompositeDisposable disposable = new CompositeDisposable();
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        handleCropResult(data.getData());
                    }
                }
            });
    private IsOwnerImagesBinding binding;
    private Uri frontSideImage;
    private Uri rightSideImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = IsOwnerImagesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);

        setSupportActionBar(binding.toolbarI.toolbar);
        binding.toolbarI.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbarI.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding.toolbarI.toolbar.setTitle(getString(R.string.owner));
        iconColorChange();
        binding.ivFirst.setOnClickListener(v -> {
            try {
                imageFirst = true;
                CropParam cropParams = new CropParam();
                cropParams.setCropImageName(System.currentTimeMillis() + ".png");
                Intent intent = new Intent(IsOwnerTractorImages.this, GalleryImagesActivity.class);
                intent.putExtra("cropParams", cropParams);
                galleryActivityResultLauncher.launch(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.ivSecond.setOnClickListener(v -> {
            try {
                imageFirst = false;
                CropParam cropParams = new CropParam();
                cropParams.setCropImageName(System.currentTimeMillis() + ".png");
                Intent intent = new Intent(IsOwnerTractorImages.this, GalleryImagesActivity.class);
                intent.putExtra("cropParams", cropParams);
                galleryActivityResultLauncher.launch(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.btnIsOwnerSubmit.setOnClickListener(v -> {
            //API call
            if (frontSideImage != null && rightSideImage != null) {
                if (Constants.isConnectionAvailable(IsOwnerTractorImages.this)) {
                    saveIsOwnerData();
                } else {
                    noInternetConnection(binding.parent);
                }
            } else {
                Toast.makeText(IsOwnerTractorImages.this, "Please click all pictures", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handleCropResult(Uri resultUri) {
        if (resultUri != null) {
            try {
                if (imageFirst) {
                    frontSideImage = resultUri;
                    Glide.with(IsOwnerTractorImages.this)
                            .load(resultUri.getPath())
                            .into(binding.ivFirst);
                } else {
                    rightSideImage = resultUri;
                    Glide.with(IsOwnerTractorImages.this)
                            .load(resultUri.getPath())
                            .into(binding.ivSecond);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(IsOwnerTractorImages.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }
    private void saveIsOwnerData() {
        showProgressDialog(getString(R.string.please_wait),IsOwnerTractorImages.this);
        try {
            Map<String, Object> brand = new HashMap<>();
            brand.put("brand_id", Integer.parseInt(sharedPref.getIsOwnerBrandId()));
            Map<String, Object> model = new HashMap<>();
            model.put("tractor_model_id", Integer.parseInt(sharedPref.getIsOwnerModelId()));
            Map<String, Object> params = new HashMap<>();
            params.put("brand", brand);
            params.put("tractor_model", model);
            params.put("tractor_year", sharedPref.getIsOwnerYearId());
            params.put("tractor_month", sharedPref.getIsOwnerMonthId());
            disposable.add(khetiAPIs.insertOwnerTractor(params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<OwnerTractorResp>() {
                        @Override
                        public void onSuccess(@NonNull OwnerTractorResp response) {
                            if (!response.isError()) {
                                @SuppressLint("RestrictedApi") WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(OwnerTractorUploadWorker.class)
                                        .setInputData(new Data.Builder()
                                                .putString(OwnerTractorUploadWorker.ID, response.getOwnerTractorData().getId() + "")
                                                .putString(OwnerTractorUploadWorker.FRONT_SIDE_IMAGE, frontSideImage != null ? frontSideImage.getPath() : null)
                                                .putString(OwnerTractorUploadWorker.RIGHT_IMAGE, rightSideImage != null ? rightSideImage.getPath() : null)
                                                .build())
                                        .build();
                                WorkManager
                                        .getInstance(IsOwnerTractorImages.this)
                                        .enqueue(uploadWorkRequest);
                                dismissProgressDialog();
                                sharedPref.removeKey("ISOWNERBRANDID");
                                sharedPref.removeKey("ISOWNERMODELID");
                                sharedPref.removeKey("ISOWNERYEARID");
                                sharedPref.removeKey("ISOWNERMONTHID");
                                Intent intent = new Intent(IsOwnerTractorImages.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Timber.tag(TAG).e(e, "onError: ");
                        }
                    }));

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void iconColorChange() {
        binding.navTab.tvBrand.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.navTab.tvModel.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.navTab.tvYear.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.navTab.tvMonth.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.navTab.tvImage.setTextColor(ContextCompat.getColor(this, R.color.red));
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.close_button_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.menu_close) {
            showAlerDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAlerDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.are_you_sure_you_want_quit))
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    dialog.cancel();
                    addSomething();
                }).show();
    }
    private void addSomething() {
        if (!sharedPref.getIsOwnerMonthId().isEmpty()) {
            sharedPref.removeKey("ISOWNERMONTHID");
        }
        if (!sharedPref.getIsOwnerYearId().isEmpty()) {
            sharedPref.removeKey("ISOWNERYEARID");
        }
        if (!sharedPref.getIsOwnerModelId().isEmpty()) {
            sharedPref.removeKey("ISOWNERMODELID");
        }
        if (!sharedPref.getIsOwnerBrandId().isEmpty()) {
            sharedPref.removeKey("ISOWNERBRANDID");
        }
        Intent intent = new Intent(IsOwnerTractorImages.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
