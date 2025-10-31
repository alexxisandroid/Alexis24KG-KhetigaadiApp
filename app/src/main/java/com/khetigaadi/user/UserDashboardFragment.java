package com.khetigaadi.user;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.UserDashboardBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.user.model.Dashboard;
import com.khetigaadi.user.model.DashboardResp;
import com.khetigaadi.user.myRentTractor.MyUploadedRentTractorList;
import com.khetigaadi.user.myUsedTractor.MyUploadedUsedTractorList;
import com.khetigaadi.user.util.MyValueFormatter;
import org.jetbrains.annotations.NotNull;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class UserDashboardFragment extends BaseFragment {
    private FragmentActivity activity;
    private final String TAG = UserDashboardFragment.class.getSimpleName();
    @Inject
    public Tracker mTracker;
    @Inject
    KhetiAPIs khetiAPIs;
    @Inject
    CompositeDisposable disposable;
    private UserDashboardBinding binding;
    public UserDashboardFragment() {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        //Google analytic code
        Timber.i("Setting screen name: %s", TAG);
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        ((MainActivity) activity).hideSoftKeyboard();
    }
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
    {
        binding = UserDashboardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ((MainActivity) activity).setToolbarTitle(getString(R.string.dashboard));
        if (Constants.isConnectionAvailable(activity)) {
            getData();
        }
        else {
            ((MainActivity) activity).noInternetConnection();
        }
        binding.rvRentTr.setOnRippleCompleteListener(rippleView -> {
            FragmentManager mFragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            fragmentTransaction.replace(contentView.getId(), new MyUploadedRentTractorList()).addToBackStack(TAG).commit();
        });
        binding.rvPendingSell.setOnRippleCompleteListener(rippleView -> callusedTr("pending"));
        binding.rvSoldTractor.setOnRippleCompleteListener(rippleView -> callusedTr("sold"));
        binding.rvUploadedTractor.setOnRippleCompleteListener(rippleView -> callusedTr("all"));
        return view;
    }
    private void callusedTr(String status)
    {
        FragmentManager mFragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
        MyUploadedUsedTractorList fragment = new MyUploadedUsedTractorList();
        Bundle bundle = new Bundle();
        bundle.putString("filter", status);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.containerView,fragment).addToBackStack(TAG).commit();
    }
    private void getData()
    {
        binding.PrPending.setVisibility(View.VISIBLE);
        binding.PrUploadedCount.setVisibility(View.VISIBLE);
        binding.PrSoldCount.setVisibility(View.VISIBLE);
        binding.PrRentTr.setVisibility(View.VISIBLE);
        binding.tvPending.setVisibility(View.GONE);
        binding.tvUploadedCount.setVisibility(View.GONE);
        binding.tvSoldCount.setVisibility(View.GONE);
        binding.tvRentTr.setVisibility(View.GONE);
        disposable.add(khetiAPIs.getDashBoard()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<DashboardResp>() {
                    @Override
                    public void onSuccess(@NonNull DashboardResp response) {
                        binding.PrPending.setVisibility(View.GONE);
                        binding.PrUploadedCount.setVisibility(View.GONE);
                        binding.PrSoldCount.setVisibility(View.GONE);
                        binding.PrRentTr.setVisibility(View.GONE);
                        binding.tvPending.setVisibility(View.VISIBLE);
                        binding.tvUploadedCount.setVisibility(View.VISIBLE);
                        binding.tvSoldCount.setVisibility(View.VISIBLE);
                        binding.tvRentTr.setVisibility(View.VISIBLE);
                        if (!response.isError()) {
                            binding.tvUploadedCount.setText(String.format(Locale.US, "%d", response.getData().getUploadedOldTractorList()));
                            binding.tvSoldCount.setText(String.format(Locale.US, "%d", response.getData().getSoldOldTractor()));
                            binding.tvPending.setText(String.format(Locale.US, "%d", response.getData().getPendingSellOldTractor()));
                            binding.tvRentTr.setText(String.format(Locale.US, "%d", response.getData().getUploadedRentTractor()));
                            getChart(response.getData());
                        } else {
                            Toast.makeText(activity, response.getMsg().get(0), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }));
    }
    private void getChart(Dashboard dashboard)
    {
        binding.userChart.setUsePercentValues(true);
        binding.userChart.setExtraOffsets(5, 10, 5, 5);
        binding.userChart.setDragDecelerationFrictionCoef(0.95f);
        binding.userChart.setCenterText(generateCenterSpannableText());
        binding.userChart.setDrawHoleEnabled(true);
        binding.userChart.setHoleColor(Color.WHITE);
        binding.userChart.setTransparentCircleColor(Color.WHITE);
        binding.userChart.setTransparentCircleAlpha(110);
        binding.userChart.setHoleRadius(48f);
        binding.userChart.setTransparentCircleRadius(50f);
        binding.userChart.setDrawCenterText(true);
        binding.userChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        binding.userChart.setRotationEnabled(true);
        binding.userChart.setHighlightPerTapEnabled(true);
        binding.userChart.getDescription().setEnabled(false);
        // add a selection listener
        setData(dashboard);
        Legend legend = binding.userChart.getLegend();
        legend.setEnabled(false);
    }
    private void setData(Dashboard dashboard)
    {
        ArrayList<PieEntry> yVals1 = new ArrayList<>();
        yVals1.add(new PieEntry(dashboard.getUploadedOldTractorList(),getString(R.string.uploaded_old),1));
        yVals1.add(new PieEntry(dashboard.getSoldOldTractor(),getString(R.string.sold),2));
        yVals1.add(new PieEntry(dashboard.getPendingSellOldTractor(),getString(R.string.pending),3));
//        yVals1.add(new PieEntry(Integer.parseInt(tvRentTr.getText().toString()), getString(R.string.uploaded_rent), 3));
        PieDataSet dataSet = new PieDataSet(yVals1,"");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(activity,R.color.cerulean));
        colors.add(ContextCompat.getColor(activity,R.color.greenHaze));
        colors.add(ContextCompat.getColor(activity,R.color.punch));
        colors.add(ContextCompat.getColor(activity,R.color.buttercup));
        dataSet.setColors(colors);
//        dataSet.setSelectionShift(0f);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueFormatter(new MyValueFormatter(new DecimalFormat(), binding.userChart));
        binding.userChart.setData(data);
        // undo all highlights
        binding.userChart.highlightValues(null);
        binding.userChart.invalidate();
    }
    private SpannableString generateCenterSpannableText()
    {
        SpannableString s = new SpannableString("Old\nTractors");
        s.setSpan(new RelativeSizeSpan(2.7f),0,12,0);
        return s;
    }
}
