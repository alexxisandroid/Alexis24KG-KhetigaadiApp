package com.khetigaadi.tractor.rent.rentTractorNewUi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.activities.YearSelectionActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.commons.utils.Year;
import com.khetigaadi.databinding.ModelRowBinding;
import com.khetigaadi.databinding.RentTractorYearBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class RentTractorYear extends BaseActivity {
    private static final String TAG = RentTractorYear.class.getSimpleName();
    private Year selectedYear;
    private List<Year> alYear;
    @Inject
    SharedPreferenceUtils pref;
    private RentTractorYearBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = RentTractorYearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);

        alYear = new ArrayList<>();
        setSupportActionBar(binding.toolbarI.toolbar);
        binding.toolbarI.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbarI.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding.toolbarI.toolbar.setTitle(getString(R.string.rentTractor));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.rentTractor));
        iconColorChange();
        binding.etSellTractorYear.requestFocus();
        binding.etSellTractorYear.setOnClickListener(view ->
                startActivityForResult(new Intent(this,YearSelectionActivity.class),YearSelectionActivity.YEAR_SELECTION_REQ));
    }
    @Override
    public void onStart() {
        super.onStart();
        alYear = Constants.getYears();
        initList();
    }
    private void initList() {
        Timber.e("onChanged: %s", alYear.toString());
        try {
            binding.llYear.removeAllViews();
            for (int i = 0; i < alYear.size(); i = i + 2) {
                LinearLayout linearLayout = new LinearLayout(RentTractorYear.this);
                linearLayout.setWeightSum(2);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (int j = 0; j <= 1 && i + j < alYear.size(); j++) {
                    ModelRowBinding modelBinding = ModelRowBinding.inflate(getLayoutInflater(), linearLayout, false);
                    final Year year = alYear.get(i + j);
                    modelBinding.tvModelName.setText(year.getName());
                    if (!pref.getRentYear().isEmpty()) {
                        if (pref.getRentYear().equalsIgnoreCase(year.getName())) {
                            modelBinding.ll.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector_red));
                        }
                    }
                    modelBinding.getRoot().setOnClickListener(v -> {
                        binding.etSellTractorYear.setText(year.getName());
                        pref.setRentYear(year.getName());
                        initList();
                        searchYearData(binding.etSellTractorYear.getText().toString());
                    });
                    linearLayout.addView(modelBinding.getRoot());
                }
                binding.llYear.addView(linearLayout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == YearSelectionActivity.YEAR_SELECTION_REQ) {
                if (data != null) {
                    selectedYear = data.getParcelableExtra(YearSelectionActivity.SELECTED_YEAR);
                    if (selectedYear != null)
                        binding.etSellTractorYear.setText(selectedYear.getName());
                    searchYearData(binding.etSellTractorYear.getText().toString());
                }
            }
        }
    }
    private void iconColorChange() {
        binding.header.tvBrand.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.header.tvModel.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.header.tvYear.setTextColor(ContextCompat.getColor(this, R.color.red));
    }
    private void searchYearData(String Year) {
        // String modelnm = Year;
        if (!Year.isEmpty()) {
            Intent intent = new Intent(this, RentTractorMonth.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!pref.getRentYear().isEmpty()) {
            pref.removeKey("RENTYEAR");
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
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    dialog.cancel();
                    addSomething();

                }).show();
    }
    private void addSomething() {
        if (!pref.getRentYear().isEmpty()) {
            pref.removeKey("RENTYEAR");
        }
        if (!pref.getRentModelId().isEmpty()) {
            pref.removeKey("RENTMODELID");
        }
        if (!pref.getRentBrandId().isEmpty()) {
            pref.removeKey("RENTBRANDID");
        }
        Intent intent = new Intent(RentTractorYear.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Check", "1");
        startActivity(intent);
        finish();
    }
}