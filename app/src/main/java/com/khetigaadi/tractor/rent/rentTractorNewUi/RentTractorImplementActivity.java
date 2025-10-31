package com.khetigaadi.tractor.rent.rentTractorNewUi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.activities.TypeSelectionActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityRentTractorImplementBinding;
import com.khetigaadi.databinding.AddImplementItemBinding;
import com.khetigaadi.implement.newImplement.util.Type;
import com.khetigaadi.pick_image.CropParam;
import com.khetigaadi.pick_image.GalleryImagesActivity;
import com.khetigaadi.room.repository.RentTractorRepository;
import com.khetigaadi.tractor.rent.rentTractorNewUi.adapter.AddImplementAdapter;
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentUnit;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class RentTractorImplementActivity extends BaseActivity {
    private static final String TAG = RentTractorImplementActivity.class.getSimpleName();
    private AddImplementAdapter addImplementAdapter;
    //    private final List<ImplementType> alAddImpl = new ArrayList<>();
    private Uri imageFile;
    // @Inject
    RentTractorRepository rentTractorRepository;
    @Inject
    SharedPreferenceUtils sharedPref;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                try {
                    handleCropResult((Uri) data.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }
    });
    private AddImplementItemBinding dialogBinding;
    private ActivityRentTractorImplementBinding binding;
    private boolean isValidate;
    private Type selectedType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ActivityRentTractorImplementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);

        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding.toolbars.toolbar.setTitle(getString(R.string.rentTractor));
        iconColorChange();
        binding.tabRent.sscroll.postDelayed(() -> binding.tabRent.sscroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT), 100L);
        addImplementAdapter = new AddImplementAdapter(this, item -> rentTractorRepository.deleteImplement(item));
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.rvImplementList.setLayoutManager(mLayoutManager);
        binding.rvImplementList.setItemAnimator(new DefaultItemAnimator());
        binding.rvImplementList.setAdapter(addImplementAdapter);
        binding.cvAddImpl.setOnClickListener(View -> {
            final Dialog dialog = new Dialog(this);
            dialogBinding = AddImplementItemBinding.inflate(getLayoutInflater());
            dialog.setContentView(dialogBinding.getRoot());
            dialog.setCanceledOnTouchOutside(false);
            ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.rentPer, R.layout.spinner_item);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            dialogBinding.spPerRent.setAdapter(adapter);
            dialogBinding.etType.requestFocus();
            dialogBinding.toolbarTitle.setOnClickListener(v -> dialog.dismiss());
            dialogBinding.etType.setOnClickListener(view -> {
                Intent intent = new Intent(this, TypeSelectionActivity.class);
                startActivityForResult(intent, TypeSelectionActivity.TYPE_SELECTION_REQ);
            });
            dialogBinding.ivImplement.setOnClickListener(view -> displayImageSelectionDialog());
            dialogBinding.addImplItem.setOnClickListener(v -> {
                if (validation()) {
                    if (Constants.isConnectionAvailable(RentTractorImplementActivity.this)) {
                        //This code is temporary commented
                        showProgressDialog(getString(R.string.please_wait));
                        expenseData();
                        imageFile = null;
                        dialog.dismiss();
                    } else {
                        noInternetConnection(binding.parent);
                    }
                }
            });
            dialog.show();
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        });
        binding.cvContinueRentList.setOnClickListener(view -> {
            if (isValidate) {
                Intent intent = new Intent(RentTractorImplementActivity.this, RentTractorOther.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.add_implements, Toast.LENGTH_SHORT).show();
            }
        });
        rentTractorRepository.getImplementsAsLive().observe(this, impList -> {
            if (impList != null) {
                isValidate = true;
                addImplementAdapter.setList(impList);
                if (impList.size() == 5) {
                    binding.cvAddImpl.setVisibility(View.GONE);
                }
            } else {
                isValidate = false;
            }
        });
    }
    private boolean validation() {
        if (dialogBinding.etType.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.pleaseSelectImplement), Toast.LENGTH_SHORT).show();
        } else if (dialogBinding.etRent.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.err_msg_rentPerHrs), Toast.LENGTH_SHORT).show();
        } else if (imageFile == null) {
            Toast.makeText(this, getString(R.string.pleaseSelectImage), Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }
    public void expenseData() {
        String implHour = dialogBinding.etRent.getText().toString();
        RentUnit implPerRent = new RentUnit();
        if (dialogBinding.spPerRent.getSelectedItemPosition() == 0) {
            implPerRent.setUnitId(4);
            implPerRent.setUnitName("Acer");
        } else {
            implPerRent.setUnitId(3);
            implPerRent.setUnitName("Hour");
        }
        sendImplData(selectedType,implHour,implPerRent,imageFile);
        //This part is added in implement response
    }
    private void displayImageSelectionDialog() {
        CropParam cropParams = new CropParam();
        cropParams.setCropImageName(System.currentTimeMillis() + ".png");
        Intent intent = new Intent(this, GalleryImagesActivity.class);
        intent.putExtra("cropParams", cropParams);
        galleryActivityResultLauncher.launch(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            hideSoftKeyboard(this);
            if (requestCode == TypeSelectionActivity.TYPE_SELECTION_REQ) {
                if (data != null) {
                    selectedType = (data.getParcelableExtra(TypeSelectionActivity.SELECTED_TYPE));
                    if (selectedType != null) {
                        dialogBinding.etType.setText(selectedType.getName());
                    }
                }
            }
        }
    }
    private void handleCropResult(Uri resultUri) {
        if (resultUri != null) {
            Glide.with(RentTractorImplementActivity.this).load(resultUri).into(dialogBinding.ivImplement);
            imageFile = resultUri;
        }
    }
    public void arraySize(int arraySize) {
        if (arraySize < 5) {
            binding.cvAddImpl.setVisibility(View.VISIBLE);
        }
    }
    private void iconColorChange() {
        binding.tabRent.tvBrand.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabRent.tvModel.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabRent.tvYear.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabRent.tvMonth.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabRent.tvHours.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabRent.tvNewImpl.setTextColor(ContextCompat.getColor(this, R.color.red));
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
            showAlertDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAlertDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder1.setMessage(getResources().getString(R.string.are_you_sure_you_want_quit));
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes", (dialog, id) -> {
            dialog.cancel();
            addSomething();
        });
        builder1.setNegativeButton("No", (dialog, id) -> dialog.cancel());
        androidx.appcompat.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    private void addSomething() {
        if (sharedPref.getImplementImageList() != null) {
            sharedPref.removeKey("IMPLEMENTIMAGELIST");
        }
        if (!sharedPref.getRentHour().isEmpty()) {
            sharedPref.removeKey("RENTHOUR");
        }
        if (!sharedPref.getRentMonth().isEmpty()) {
            sharedPref.removeKey("RENTMONTH");
        }
        if (!sharedPref.getRentYear().isEmpty()) {
            sharedPref.removeKey("RENTYEAR");
        }
        if (!sharedPref.getRentModelId().isEmpty()) {
            sharedPref.removeKey("RENTMODELID");
        }
        if (!sharedPref.getRentBrandId().isEmpty()) {
            sharedPref.removeKey("RENTBRANDID");
        }
        Intent intent = new Intent(RentTractorImplementActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Check", "1");
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (sharedPref.getImplementImageList() != null) {
            sharedPref.removeKey("IMPLEMENTIMAGELIST");
        }
    }
    private void sendImplData(Type type,String implHour,RentUnit implPerRent,Uri image) {
        rentTractorRepository.insertImplement(type,implHour,implPerRent,image);
        dismissProgressDialog();
    }
}
