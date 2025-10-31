package com.khetigaadi.tractor.rent.rentTractorNewUi;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.base.model.Model;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.RentTractorOthersBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.RentTractorRepository;
import com.khetigaadi.tractor.rent.RentTractorListFragment;
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentTractorUploadResp;
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.UploadRentTrReq;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
@AndroidEntryPoint
public class RentTractorOther extends BaseActivity {
    private static final String TAG = RentTractorOther.class.getSimpleName();
    @Inject
    SharedPreferenceUtils pref;
    //@Inject
    RentTractorRepository rentTractorRepository;
    @Inject
    KhetiAPIs khetiAPIs;
    private RentTractorOthersBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = RentTractorOthersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbars.toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding.toolbars.toolbar.setTitle(getString(R.string.rentTractor));
        hideSoftKeyboard(this);
        iconColorChange();
        binding.tabOther.sscroll.postDelayed(() -> binding.tabOther.sscroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT), 100L);
        binding.btnSubmit.setOnClickListener(View -> {
            if (!binding.etWorkingRadius.getText().toString().isEmpty()) {
                if (Constants.isConnectionAvailable(this)) {
                    saveRentTractorData();
                } else {
                    noInternetConnection(binding.parent);
                }
            } else {
                Toast.makeText(this, "Please select fields!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void iconColorChange() {
        binding.tabOther.tvBrand.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabOther.tvModel.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabOther.tvYear.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabOther.tvMonth.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabOther.tvHours.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabOther.tvNewImpl.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabOther.tvImage.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabOther.tvOthers.setTextColor(ContextCompat.getColor(this, R.color.red));
    }
    private void saveRentTractorData() {
        showProgressDialog(getString(R.string.please_wait));
        try {
            Brand brand = new Brand();
            brand.setId(Integer.parseInt(pref.getRentBrandId()));
            Model model = new Model();
            model.setId(Integer.parseInt(pref.getRentModelId()));
           // model.
       /*     List<ImplementType> impList = new Gson().fromJson(pref.getImplementImageList(), new TypeToken<List<ImplementType>>() {
            }.getType());*/

            UploadRentTrReq uploadRentTrReq = new UploadRentTrReq(brand, pref.getRentHour(), binding.etNote.getText().toString(), Integer.parseInt(binding.etWorkingRadius.getText().toString()), model, pref.getRentMonth(), pref.getRentYear());
            khetiAPIs.uploadRentTractor(uploadRentTrReq)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<>() {
                        @Override
                        public void onSuccess(@NonNull RentTractorUploadResp resp) {
                            try {
                                if (!resp.isError()) {
                                    rentTractorRepository.updateRentTractorIdToImplement(resp.getData().getRentTractorId());
                                    @SuppressLint("RestrictedApi") WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(RentTractorImpUploadWorker.class)
                                            .setInputData(new Data.Builder()
                                                    .putString(RentTractorImpUploadWorker.ID, String.valueOf(resp.getData().getRentTractorId()))
                                                    .build())
                                            .build();
                                    WorkManager
                                            .getInstance(RentTractorOther.this)
                                            .enqueue(uploadWorkRequest);
                                    pref.removeKey("RENTBRANDID");
                                    pref.removeKey("RENTMODELID");
                                    pref.removeKey("RENTYEAR");
                                    pref.removeKey("RENTMONTH");
                                    pref.removeKey("RENTHOUR");
                                    pref.removeKey("IMPLEMENTLIST");
                                    pref.removeKey("RENTIMAGE1");
                                    pref.removeKey("RENTIMAGE2");
                                    pref.removeKey("RENTIMAGE3");
                                    pref.removeKey("RENTIMAGE4");
                                    if (resp.getMsg() != null && resp.getMsg().get(0) != null)
                                        Toast.makeText(RentTractorOther.this, resp.getMsg().get(0), Toast.LENGTH_SHORT).show();
                                    dismissProgressDialog();
                                    Intent intent = new Intent(RentTractorOther.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    Intent intent1 = new Intent("anEvent");
                                    intent1.putExtra("key", RentTractorListFragment.TAG);
                                    LocalBroadcastManager.getInstance(RentTractorOther.this).sendBroadcast(intent1);
                                } else {
                                    dismissProgressDialog();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                dismissProgressDialog();
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            e.printStackTrace();
                            dismissProgressDialog();
                            Toast.makeText(RentTractorOther.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
                .setNegativeButton(getString(R.string.no),(dialog,which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes),(dialog,which) -> {
                    dialog.cancel();
                    addSomething();
                }).show();
    }
    private void addSomething() {
        if (pref.getImplementImageList() != null) {
            pref.removeKey("IMPLEMENTIMAGELIST");
        }
        if (!pref.getRentHour().isEmpty()) {
            pref.removeKey("RENTHOUR");
        }
        if (!pref.getRentMonth().isEmpty()) {
            pref.removeKey("RENTMONTH");
        }
        if (!pref.getRentYear().isEmpty()) {
            pref.removeKey("RENTYEAR");
        }
        if (!pref.getRentModelId().isEmpty()) {
            pref.removeKey("RENTMODELID");
        }
        if (!pref.getRentBrandId().isEmpty()) {
            pref.removeKey("RENTBRANDID");
        }
        Intent intent = new Intent(RentTractorOther.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Check", "1");
        startActivity(intent);
        finish();
    }
}
