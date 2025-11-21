package com.khetigaadi.tractor.rent.rentTractorNewUi;
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
import com.khetigaadi.commons.model.Month;
import com.khetigaadi.databinding.ModelRowBinding;
import com.khetigaadi.databinding.RentTractorMonthBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class RentTractorMonth extends BaseActivity {
    private static final String TAG = RentTractorMonth.class.getSimpleName();
    private List<Month> alMonth;
    @Inject
    SharedPreferenceUtils pref;
    private RentTractorMonthBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = RentTractorMonthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);

        init();
        setSupportActionBar(binding.toolbarI.toolbar);
        binding.toolbarI.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbarI.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding.toolbarI.toolbar.setTitle(getString(R.string.rentTractor));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.rentTractor));
        iconColorChange();
    }
    private void init() {
        alMonth = new ArrayList<>();
    }
    private void iconColorChange() {
        binding.tabMonth.tvBrand.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabMonth.tvModel.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabMonth.tvYear.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabMonth.tvMonth.setTextColor(ContextCompat.getColor(this, R.color.red));
    }
    @Override
    public void onStart() {
        super.onStart();
        getMonth();
    }
    private void getMonth() {
        alMonth.clear();
        alMonth.add(new Month(1, "Jan"));
        alMonth.add(new Month(2, "Feb"));
        alMonth.add(new Month(3, "Mar"));
        alMonth.add(new Month(4, "April"));
        alMonth.add(new Month(5, "May"));
        alMonth.add(new Month(6, "June"));
        alMonth.add(new Month(7, "July"));
        alMonth.add(new Month(8, "Aug"));
        alMonth.add(new Month(9, "Sept"));
        alMonth.add(new Month(10, "Oct"));
        alMonth.add(new Month(11, "Nov"));
        alMonth.add(new Month(12, "Dec"));
        initList();
    }
    private void initList() {
        Timber.e("onChanged: " + alMonth.toString());
        try {
          binding.  llMonth.removeAllViews();
            for (int i = 0; i < alMonth.size(); i = i + 2) {
                LinearLayout linearLayout = new LinearLayout(RentTractorMonth.this);
                linearLayout.setWeightSum(2);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (int j = 0; j <= 1 && i + j < alMonth.size(); j++) {
                    @NonNull ModelRowBinding rowBinding = ModelRowBinding.inflate(getLayoutInflater(), linearLayout, false);
                    final Month month = alMonth.get(i + j);
                    rowBinding.tvModelName.setText(month.getName());
                    if (!pref.getRentMonth().isEmpty()) {
                        if (pref.getRentMonth().equalsIgnoreCase(month.getName())) {
                            rowBinding.ll.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector_red));
                        }
                    }
                    rowBinding.getRoot().setOnClickListener(v -> {
                        rowBinding.ll.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector_red));
                        pref.setRentMonth(month.getName());
                        initList();
                        searchMonthData(month.getName());
                    });
                    linearLayout.addView(rowBinding.getRoot());
                }
               binding. llMonth.addView(linearLayout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void searchMonthData(String Month) {
        String month = Month;
        if (!Month.isEmpty()) {
            // pref.setRentMonth(Month);
            Intent intent = new Intent(this, RentTractorHour.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //  intent.putExtra("month", Month);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!pref.getRentMonth().isEmpty()) {
            pref.removeKey("RENTMONTH");
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
            showAlertDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAlertDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.are_you_sure_you_want_quit))
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    dialog.cancel();
                    addSomething();
                }).show();
    }
    private void addSomething() {
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
        Intent intent = new Intent(RentTractorMonth.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Check", "1");
        startActivity(intent);
        finish();
    }
}