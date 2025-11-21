package com.khetigaadi.language;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.databinding.ChangeLanguageBinding;
import com.khetigaadi.user.LoginActivity;
import com.khetigaadi.user.SessionManager;

import java.util.Objects;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class SelectLanguage extends BaseActivity {
    private String languageToLoad;
    @Inject
    SessionManager session;
    private LocaleManager localManager;
    private ChangeLanguageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ChangeLanguageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        ViewCompat.setOnApplyWindowInsetsListener(view,(v,insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//       });
//        EdgeToEdge.enable(this);
        Log.d("xcvvxvxcvcxv342","");
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.select_language));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.select_language));
        binding.toolbars.toolbar.setLogo(null);
        localManager = new LocaleManager(session);
        String selectedLanguage = localManager.getLanguage();
        findViewById();
        switch (selectedLanguage) {
            case "hi": {
                binding.rbHindi.setChecked(true);
                break;
            }
            case "mr": {
                binding.rbMarathi.setChecked(true);
                break;
            }
            case "kn": {
                binding.rbKannada.setChecked(true);
                break;
            }
            case "te": {
                binding.rbTelugu.setChecked(true);
                break;
            }
            case "ta": {
                binding.rbTamil.setChecked(true);
                break;
            }
            case "gu": {
                binding.rbGujarati.setChecked(true);
                break;
            }
            case "pa": {
                binding.rbPunjabi.setChecked(true);
                break;
            }
            case "bn": {
                binding.rbBangali.setChecked(true);
                break;
            }
            case "ml": {
                binding.rbMalayalam.setChecked(true);
                break;
            }
            default: {
                binding.rbEnglish.setChecked(true);
                break;
            }
        }
    }
    @SuppressLint("RestrictedApi")
    private void findViewById() {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                         ContextCompat.getColor(this,R.color.white)
                        ,ContextCompat.getColor(this,R.color.navy_blue)
                });
        binding.rbEnglish.setSupportButtonTintList(colorStateList);
        binding.rbHindi.setSupportButtonTintList(colorStateList);
        binding.rbMarathi.setSupportButtonTintList(colorStateList);
        binding.rbKannada.setSupportButtonTintList(colorStateList);
        binding.rbTelugu.setSupportButtonTintList(colorStateList);
        binding.rbTamil.setSupportButtonTintList(colorStateList);
        binding.rbGujarati.setSupportButtonTintList(colorStateList);
        binding.rbPunjabi.setSupportButtonTintList(colorStateList);
        binding.rbBangali.setSupportButtonTintList(colorStateList);
        binding.rbMalayalam.setSupportButtonTintList(colorStateList);
        binding.btnDone.setOnClickListener(v -> {
            //language
            if (binding.rgLanguages.getCheckedRadioButtonId() == R.id.rbHindi) {
                languageToLoad = "hi";
            } else if (binding.rgLanguages.getCheckedRadioButtonId() == R.id.rbMarathi) {
                languageToLoad = "mr";
            } else if (binding.rgLanguages.getCheckedRadioButtonId() == R.id.rbKannada) {
                languageToLoad = "kn";
            } else if (binding.rgLanguages.getCheckedRadioButtonId() == R.id.rbTelugu) {
                languageToLoad = "te";
            } else if (binding.rgLanguages.getCheckedRadioButtonId() == R.id.rbTamil) {
                languageToLoad = "ta";
            } else if (binding.rgLanguages.getCheckedRadioButtonId() == R.id.rbGujarati) {
                languageToLoad = "gu";
            } else if (binding.rgLanguages.getCheckedRadioButtonId() == R.id.rbPunjabi) {
                languageToLoad = "pa";
            } else if (binding.rgLanguages.getCheckedRadioButtonId() == R.id.rbBangali) {
                languageToLoad = "bn";
            } else if (binding.rgLanguages.getCheckedRadioButtonId() == R.id.rbMalayalam) {
                languageToLoad = "ml";
            } else {
                languageToLoad = "en";
            }
            localManager.setNewLocale(SelectLanguage.this, languageToLoad);
            if (session.isLoggedIn()) {
                Intent intent = new Intent(SelectLanguage.this, MainActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SelectLanguage.this, LoginActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                startActivity(intent);
                finish();
            }
        });
        Log.d("xcvvxvxcvcxvx","");
    }
    //for disable baseActivity menu
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Do Nothing
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Do Nothing
        return false;
    }
}
