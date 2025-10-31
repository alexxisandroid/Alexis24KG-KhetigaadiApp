package com.khetigaadi.tractor.rent.rentTractorNewUi;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.khetigaadi.commons.model.Hours;
import com.khetigaadi.databinding.ModelRowBinding;
import com.khetigaadi.databinding.RentTractorHourBinding;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class RentTractorHour extends BaseActivity {
    private static final String TAG = RentTractorHour.class.getSimpleName();
    private ArrayList<Hours> alHours;
    //private HorizontalScrollView scrollView;
    @Inject
    SharedPreferenceUtils pref;
    private RentTractorHourBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = RentTractorHourBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        init();
        setSupportActionBar(binding.toolbarI.toolbar);
        binding.toolbarI.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbarI.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbarI.toolbar.setTitle(getString(R.string.rentTractor));
        iconColorChange();
    }
    private void init() {
        alHours = new ArrayList<>();
    }
    private void iconColorChange() {
        binding.tabs.tvBrand.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabs.tvModel.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabs.tvYear.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabs.tvMonth.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabs.tvHours.setTextColor(ContextCompat.getColor(this, R.color.red));
    }
    @Override
    public void onStart() {
        super.onStart();
        alHours.clear();
        getHours();
    }
    private void getHours() {
        alHours.add(new Hours(1, "Less than 500 Hrs"));
        alHours.add(new Hours(2, "500 - 1000 Hrs"));
        alHours.add(new Hours(3, "1001 - 2000 Hrs"));
        alHours.add(new Hours(4, "2001 - 3000 Hrs"));
        alHours.add(new Hours(5, "3001 - 4000 Hrs"));
        alHours.add(new Hours(6, "4001 - 5000 Hrs"));
        alHours.add(new Hours(7, "5001 - 6000 Hrs"));
        alHours.add(new Hours(8, "6001 - 7000 Hrs"));
        alHours.add(new Hours(9, "7001 - 8000 Hrs"));
        alHours.add(new Hours(10, "8001 - 9000 Hrs"));
        alHours.add(new Hours(11, "9001 - 10000 Hrs"));
        alHours.add(new Hours(12, "above 10000 Hrs"));
        alHours.add(new Hours(13, "Not Available Hrs"));
        initHourList();
    }
    private void initHourList() {
        Timber.e("onChanged: " + alHours.toString());
        try {
            binding.llHour.removeAllViews();
            for (int i = 0; i < alHours.size(); i = i + 2) {
                LinearLayout linearLayout = new LinearLayout(RentTractorHour.this);
                linearLayout.setWeightSum(2);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (int j = 0; j <= 1 && i + j < alHours.size(); j++) {
                    ModelRowBinding rowBinding=ModelRowBinding.inflate(getLayoutInflater(),linearLayout,false);
                    final Hours hours = alHours.get(i + j);
                    rowBinding. tvModelName.setText(hours.getName());
                    if (!pref.getRentHour().isEmpty()) {
                        if (pref.getRentHour().equalsIgnoreCase(hours.getName())) {
                            rowBinding.   ll.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector_red));
                        }
                    }
                    rowBinding.getRoot().setOnClickListener(v -> {
                        rowBinding.  ll.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector_red));
                        pref.setRentHour(hours.getName());
                        initHourList();
                        searchHourData(hours.getName());
                    });
                    linearLayout.addView(rowBinding.getRoot());
                }
                binding.llHour.addView(linearLayout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void searchHourData(String Hours) {
        String hours = Hours;
        if (!Hours.isEmpty()) {
            //  pref.setRentHour(Hours);
            Intent intent = new Intent(this, RentTractorImplementActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
        Intent intent = new Intent(RentTractorHour.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Check", "1");
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!pref.getRentHour().isEmpty()) {
            pref.removeKey("RENTHOUR");
        }
    }
}
