package com.khetigaadi.tractor.sellTractorNewUi;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.khetigaadi.R;
import com.khetigaadi.databinding.ActivitySellTractorBinding;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class SellTractorActivity extends AppCompatActivity {
    private ActivitySellTractorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        EdgeToEdge.enable(this);

        binding = ActivitySellTractorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar,(v,insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());  //.systemBars()
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
      EdgeToEdge.enable(this);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbar.setTitle(getString(R.string.sellTractor));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.sellTractor));
    }
    public void iconColorChange(int i) {
        switch (i) {
            case 1: {
                binding.tab.tvBrand.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvModel.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvImage.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvYear.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvMonth.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvHours.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvDocument.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvCondition.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvPrice.setTextColor(getResources().getColor(R.color.black));
                break;
            }
            case 2: {

                binding.tab.tvBrand.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvModel.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvImage.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvYear.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvMonth.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvHours.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvDocument.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvCondition.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvPrice.setTextColor(getResources().getColor(R.color.black));
                break;
            }
            case 3: {

                binding.tab.tvBrand.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvModel.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvImage.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvYear.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvMonth.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvHours.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvDocument.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvCondition.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvPrice.setTextColor(getResources().getColor(R.color.black));
                break;
            }
            case 4: {

                binding.tab.tvBrand.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvModel.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvImage.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvYear.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvMonth.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvHours.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvDocument.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvCondition.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvPrice.setTextColor(getResources().getColor(R.color.black));
                break;
            }
            case 5:

                binding.tab.tvBrand.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvModel.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvImage.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvYear.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvMonth.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvHours.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvDocument.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvCondition.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvPrice.setTextColor(getResources().getColor(R.color.black));
                break;
            case 6: {

                binding.tab.tvBrand.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvModel.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvImage.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvYear.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvMonth.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvHours.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvDocument.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvCondition.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvPrice.setTextColor(getResources().getColor(R.color.black));
                break;
            }
            case 7: {
                binding.tab.tvBrand.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvModel.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvImage.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvYear.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvMonth.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvHours.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvDocument.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvCondition.setTextColor(getResources().getColor(R.color.black));
                binding.tab.tvPrice.setTextColor(getResources().getColor(R.color.black));
                break;
            }
            case 8: {

                binding.tab.tvBrand.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvModel.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvImage.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvYear.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvMonth.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvHours.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvDocument.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvCondition.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvPrice.setTextColor(getResources().getColor(R.color.black));
                break;
            }
            case 9: {
                binding.tab.tvBrand.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvModel.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvImage.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvYear.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvMonth.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvHours.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvDocument.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvCondition.setTextColor(getResources().getColor(R.color.red));
                binding.tab.tvPrice.setTextColor(getResources().getColor(R.color.red));
                break;
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.close_button_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_close) {
            showAlertDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAlertDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.are_you_sure_you_want_quit))
                .setNegativeButton(getString(R.string.no),(dialog,which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes),(dialog,which) -> {
                    dialog.cancel();
                    finish();
                }).show();
    }
    public void callFragments(int i) {
        Fragment fragment;
        switch (i) {
            case 2:
                fragment = SellTractorModel.getInstance();
                break;
            case 3:
                fragment = SellTractorImages.getInstance();
                break;
            case 4:
                fragment = SellTractorYear.getInstance();
                break;
            case 5:
                fragment = SellTractorMonth.getInstance();
                break;
            case 6:
                fragment = SellTractorHour.getInstance();
                break;
            case 7:
                fragment = SellTractorDocument.getInstance();
                break;
            case 8:
                fragment = SellTractorCondition.getInstance();
                break;
            case 9:
                fragment = SellTractorPrice.getInstance();
                break;
            default:
                fragment = SellTractorBrand.getInstance();
        }
        if (i > 4)
            binding.tab.sscroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        else
            binding.tab.sscroll.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(binding.fragmentContainerView.getId(), fragment)
                .addToBackStack(i + "")
                .commit();
    }
}