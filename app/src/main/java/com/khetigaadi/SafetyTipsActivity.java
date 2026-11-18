package com.khetigaadi;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
//import android.text.Spannable;
//import android.text.Spanned;
import android.text.Spanned;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.khetigaadi.databinding.ActivitySafetyTipsBinding;
import com.khetigaadi.model.SafetyTipsResp;
import com.khetigaadi.retrofit.KhetiAPIs;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@SuppressWarnings("DeprecatedIsStillUsed")
@AndroidEntryPoint
public class SafetyTipsActivity extends AppCompatActivity {
    private ActivitySafetyTipsBinding binding;
    @Inject
    KhetiAPIs khetiAPIs;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySafetyTipsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        EdgeToEdge.enable(this);
//        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(view,(v,insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        initToolbar();
        Glide.with(SafetyTipsActivity.this).load(R.drawable.loader_white).into(binding.llProgresss.ivLoader);
        getTips();
    }
    @Deprecated
    public static Spanned getHtmlText(String html) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        }
        else {
            return Html.fromHtml(html);
        }
    }
    private void getTips() {
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        disposable.add(khetiAPIs.getSafetyTips()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SafetyTipsResp>() {
                    @Override
                    public void onSuccess(@NotNull SafetyTipsResp safetyTipsResp) {
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                        try {
                           // Spanned dfdg;
                           // dfdg = Html.fromHtml(safetyTipsResp.getData().getContent());

                           // binding.tvTips.setText(Html.fromHtml(safetyTipsResp.getData().getContent()));

                            binding.tvTips.setText(getHtmlText(safetyTipsResp.getData().getContent()));
                        }
                        catch(Exception e) {
                            //noinspection CallToPrintStackTrace
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
                }));
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        //binding.cvnmjn.findViewById(R.id.acgdxfbgv).findViewById(R.id.toolbar).findViewById(R.id.toolbar).setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        //noinspection deprecation
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.safety_tips));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.safety_tips));
    }
//    public void upDate(){
//        val appUpdateManager = AppUpdateManagerFactory.create(this)
//
//        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
//
//        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
//                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//
//                appUpdateManager.startUpdateFlowForResult(
//                        appUpdateInfo,
//                        AppUpdateType.IMMEDIATE,
//                        this,
//                        REQUEST_CODE_UPDATE
//                )
//            }
//        }
//
//    }
}
