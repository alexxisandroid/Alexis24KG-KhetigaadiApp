package com.khetigaadi.implement.compare;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.khetigaadi.R;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.CompareImpBinding;
import com.khetigaadi.databinding.ImpCompareRowBinding;
import com.khetigaadi.implement.compare.model.CompImpReq;
import com.khetigaadi.implement.compare.model.CompImpResp;
import com.khetigaadi.implement.compare.model.ImplResult;
import com.khetigaadi.retrofit.KhetiAPIs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class CompareImp extends Fragment {
    private final String TAG = CompareImp.class.getSimpleName();
    private FragmentActivity activity;
    private String value = "";
    @Inject
    Tracker mTracker;
    @Inject
    SharedPreferenceUtils pref;
    private ArrayList<ImplResult> arrayListResult;
    private CompareImpBinding binding;
    @Inject
    KhetiAPIs khetiAPIs;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            value = bundle.getString("value");
        }
    }
    @NotNull
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = CompareImpBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        arrayListResult = new ArrayList<>();
        getCompareImpDetails();
        return view;
    }
    private void getCompareImpDetails() {
        binding.llProgress.setVisibility(View.VISIBLE);
        khetiAPIs.getCompareImp(new Gson().fromJson(value,CompImpReq.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<>() {
                    @Override
                    public void onSuccess(@NonNull CompImpResp myResponse) {
                        binding.llProgress.setVisibility(View.GONE);
                        try {
                            if(!myResponse.isError()) {
                                //Google analytic code
                                Timber.i("Setting screen name: %s", TAG);
                                mTracker.setScreenName(TAG + " " + myResponse.getData().getFirstImplement().get(0).getImplementName());
                                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                                binding.tvTractor1Name.setText(myResponse.getData().getFirstImplement().get(0).getImplementName());
                                String firstImplImage = myResponse.getData().getFirstImplement().get(0).getImplementImage().getImplementImage();
                                Glide.with(CompareImp.this)
                                        .load(Constants.decodeURL(myResponse.getOthers().getImplementImagePath() + firstImplImage))
                                        .placeholder(R.mipmap.no_image)
                                        .error(R.mipmap.no_image)
                                        .into(binding.ivNewTractor1Image);
                                binding.tvTractor2Name.setText(myResponse.getData().getSecondImplement().get(0).getImplementName());
                                String secondImplImage = myResponse.getData().getSecondImplement().get(0).getImplementImage().getImplementImage();
                                Glide.with(CompareImp.this)
                                        .load(Constants.decodeURL(myResponse.getOthers().getImplementImagePath() + secondImplImage))
                                        .placeholder(R.mipmap.no_image)
                                        .error(R.mipmap.no_image)
                                        .into(binding.ivNewTractor2Image);
                                arrayListResult.clear();
                                for (int i = 0; i < myResponse.getData().getImplementSpecLevel().size(); i++) {
                                    boolean isFirstDataEmpty = true;
                                    boolean isSecondDataEmpty = true;
                                    ImplResult implResult = new ImplResult();
                                    implResult.setId(myResponse.getData().getImplementSpecLevel().get(i).getImplementSpecId());
                                    implResult.setLabel(myResponse.getData().getImplementSpecLevel().get(i).getImplementSpecName());
                                    for (int j = 0; j < myResponse.getData().getFirstImplement().get(0).getImplementSpecData().size(); j++) {
                                        if (myResponse.getData().getImplementSpecLevel().get(i).getImplementSpecId() == myResponse.getData().getFirstImplement().get(0).getImplementSpecData().get(j).getImplementSpecId()) {
                                            implResult.setLeftSideValue(myResponse.getData().getFirstImplement().get(0).getImplementSpecData().get(j).getSpecData());
                                            isFirstDataEmpty = false;
                                        }
                                    }
                                    for (int j = 0; j < myResponse.getData().getSecondImplement().get(0).getImplementSpecData().size(); j++) {
                                        if (myResponse.getData().getImplementSpecLevel().get(i).getImplementSpecId() == myResponse.getData().getSecondImplement().get(0).getImplementSpecData().get(j).getImplementSpecId()) {
                                            implResult.setRightSideValue(myResponse.getData().getSecondImplement().get(0).getImplementSpecData().get(j).getSpecData());
                                            isSecondDataEmpty = false;
                                        }
                                    }
                                    if (isFirstDataEmpty) {
                                        implResult.setLeftSideValue("");
                                    }
                                    if (isSecondDataEmpty) {
                                        implResult.setRightSideValue("");
                                    }
                                    arrayListResult.add(implResult);
                                }
                                int len = arrayListResult.size();
                                for (int i = 0; i < len; i++) {
                                    ImpCompareRowBinding hiddenInfo = ImpCompareRowBinding.inflate(LayoutInflater.from(activity), binding.llMainDetails, false);
                                    hiddenInfo.tvLabel.setText(arrayListResult.get(i).getLabel());
                                    if (binding.llMainDetails.getChildCount() == 0)
                                        hiddenInfo.vLine.setVisibility(View.GONE);
                                    else
                                        hiddenInfo.vLine.setVisibility(View.VISIBLE);
                                    if (arrayListResult.get(i).getLeftSideValue().equals("")) {
                                        hiddenInfo.tvValue1.setText("-");
                                    } else {
                                        hiddenInfo.tvValue1.setText(arrayListResult.get(i).getLeftSideValue());
                                    }
                                    if (arrayListResult.get(i).getRightSideValue().equals("")) {
                                        hiddenInfo.tvValue2.setText("-");
                                    } else {
                                        hiddenInfo.tvValue2.setText(arrayListResult.get(i).getRightSideValue());
                                    }
                                    binding.llMainDetails.addView(hiddenInfo.getRoot());
                                }
                            } else {
                                for (int j = 0; j < myResponse.getMsg().size(); j++) {
                                    Toast.makeText(activity, myResponse.getMsg().get(j), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("Error: %s", e.getMessage());
                        binding.llProgress.setVisibility(View.GONE);
                    }
                });
    }
}
