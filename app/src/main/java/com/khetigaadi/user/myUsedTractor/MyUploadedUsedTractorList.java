package com.khetigaadi.user.myUsedTractor;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.DeleteTrFeedbackFormDialogBinding;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.UsedTractorRepository;
import com.khetigaadi.tractor.sellTractorNewUi.model.TractorImages;
import com.khetigaadi.tractor.usedTractor.UsedTractorDetailActivity;
import com.khetigaadi.tractor.usedTractor.model.UsedTractorImage;
import com.khetigaadi.user.model.User;
import com.khetigaadi.user.myUsedTractor.model.Filter;
import com.khetigaadi.user.myUsedTractor.model.InterestedBuyersResp;
import com.khetigaadi.user.myUsedTractor.model.MyUploadedTrReq;
import com.khetigaadi.user.myUsedTractor.model.MyUsedTractorResp;
import com.khetigaadi.user.util.MyUsedTractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class MyUploadedUsedTractorList extends Fragment {
    public static final String TAG = "MyUploadedUsedTractorLi";
    private MyUploadedTractorsListAdapter mAdapter;
    private FragmentActivity activity;
    private String filter;
    @Inject
    Tracker mTracker;
    @Inject
    User user;
    @Inject
    UsedTractorRepository usedTractorRepository;
    @Inject
    KhetiAPIs khetiAPIs;
    private RecycleListBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull @NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        binding = RecycleListBinding.inflate(inflater, container, false);
        ((MainActivity) activity).setToolbarTitle(getString(R.string.myUploadedUsedTractor));
        Bundle bundle = getArguments();
        if (bundle != null) {
            filter = bundle.getString("filter");
        }
        Glide.with(activity).load(R.drawable.loader_white).into(binding.ivLoader);
        binding.filterIcon.setVisibility(View.GONE);
        init();
        myUploadedUsedTractorList();
        return binding.getRoot();
    }
    private void init() {
        mAdapter = new MyUploadedTractorsListAdapter(activity, binding.myRecyclerView);
        binding.myRecyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position, v) -> {
            Intent intent = new Intent(activity, UsedTractorDetailActivity.class);
            intent.putExtra("tractorId", mAdapter.getItem(position).getId());
            startActivity(intent);
        });
        mAdapter.setOnInterestedBuyersClickListener((position, v) -> {
            if (mAdapter.getItem(position).isBuyersOpen()) {
                mAdapter.getItem(position).setInterestedBuyers(null);
                mAdapter.getItem(position).setBuyersOpen(false);
                mAdapter.notifyItemChanged(position);
            } else {
                getInterestedBuyers(position,mAdapter.getItem(position).getId());
                mAdapter.getItem(position).setBuyersOpen(true);
            }
        });
        mAdapter.setOnSoldClickListener((position, v) -> changeSoldStatus(mAdapter.getItem(position).getId(),mAdapter.getItem(position).getSoldUnsoldStatus(),position));
        mAdapter.setOnTrashClickListener((position, v) -> showDialog(mAdapter.getItem(position).getId()));
        mAdapter.setOnChatClickListener((position, v) -> {
//                                startActivity(new Intent(activity, LoginActivity.class));
        });
    }
    private void myUploadedUsedTractorList() {
        binding.llProgress.setVisibility(View.VISIBLE);
        MyUploadedTrReq jsonObjectStatus = new MyUploadedTrReq(new Filter(filter));
        khetiAPIs.getMyUploadedTractor(jsonObjectStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<>() {
                    @Override
                    public void onSuccess(@NonNull MyUsedTractorResp myUsedTractorResp) {
                        binding.llProgress.setVisibility(View.GONE);
                        try {
                            if (!myUsedTractorResp.isError()) {
                                List<MyUsedTractor> data = myUsedTractorResp.getData();
                                for (MyUsedTractor myUsedTractor : data) {
                                    if (myUsedTractor.getImage() == null) {
                                        try {
                                            TractorImages usedTractor = usedTractorRepository.getUsedTractor(Integer.parseInt(myUsedTractor.getId()));
                                            if (usedTractor != null) {
                                                UsedTractorImage image = new UsedTractorImage();
                                                image.setUsedTractorImage(usedTractor.getRightImage().toString());
                                                myUsedTractor.setImage(image);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                mAdapter.addItems(myUsedTractorResp.getData(), 1, myUsedTractorResp.getOthers());
                            } else {
                                Toast.makeText(activity,
                                        myUsedTractorResp.getMsg().get(0), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("response Error: %s", e);
                        binding.llProgress.setVisibility(View.GONE);
                    }
                });
    }
    private void showDialog(final String id) {
        // get prompts.xml view
        final Dialog d = new Dialog(activity);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setCancelable(false);
        d.setCanceledOnTouchOutside(false);
        DeleteTrFeedbackFormDialogBinding dialogBinding = DeleteTrFeedbackFormDialogBinding.inflate(getLayoutInflater());
        d.setContentView(dialogBinding.getRoot());
        d.show();
        dialogBinding.btnClose.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            d.dismiss();
        });
        dialogBinding.btnSubmit.setOnClickListener(v -> {
            Constants.hideSoftKeyboard(activity);
            if (!dialogBinding.spReasonType.getSelectedItem().toString().equalsIgnoreCase("Select Reason")) {
                if (dialogBinding.tvReason.getText().toString().length() != 0) {
                    try {
                        Map<String, Object> params = new HashMap<>();
                        params.put("used_tractor_id", id);
                        params.put("delete_request_type_id", dialogBinding.spReasonType.getSelectedItemPosition());
                        params.put("reason", dialogBinding.tvReason.getText().toString().replaceAll(" ", "%20"));
                        khetiAPIs.deleteMyUploadedTractor(params)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DisposableSingleObserver<>() {
                                    @Override
                                    public void onSuccess(@NonNull MyResponse myResponse) {
                                        try {
                                            for (int i = 0; i < myResponse.getMsg().size(); i++) {
                                                Toast.makeText(activity, myResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                            }
                                            if (!myResponse.isError()) {
                                                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                                            }
                                            d.dismiss();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        Timber.e("response Error: %s", e);
                                        binding.llProgress.setVisibility(View.GONE);
                                    }
                                });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(activity, "Please enter Reason", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "Please Select Reason.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getInterestedBuyers(final int position,final String id) {
        khetiAPIs.getInterestedBuyers(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<>() {
                    @Override
                    public void onSuccess(@NonNull InterestedBuyersResp buyersResp) {
                        try {
                            if (!buyersResp.isError()) {
                                mAdapter.getItem(position).setInterestedBuyers(buyersResp.getData());
                                mAdapter.notifyItemChanged(position);
                            } else {
                                for (int i = 0; i < buyersResp.getMsg().size(); i++) {
                                    Toast.makeText(activity, buyersResp.getMsg().get(i), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("response Error: %s", e);
                        binding.llProgress.setVisibility(View.GONE);
                    }
                });
    }
    private void changeSoldStatus(final String id,final String soldStatus,final int position) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("used_tractor_id", Integer.parseInt(id));
            khetiAPIs.changeSoldStatusOfTr(params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<>() {
                        @Override
                        public void onSuccess(@NonNull MyResponse myResponse) {
                            try {
                                if (!myResponse.isError()) {
                                    for (int i = 0; i < myResponse.getMsg().size(); i++) {
                                        if (myResponse.getMsg().get(i).equalsIgnoreCase("Sold Successfully")) {
                                            mAdapter.getItem(position).setSoldUnsoldStatus("1");
                                        } else {
                                            mAdapter.getItem(position).setSoldUnsoldStatus("0");
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < myResponse.getMsg().size(); i++) {
                                        Toast.makeText(activity, myResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                    }
                                }
                                mAdapter.notifyItemChanged(position);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Timber.e("response Error: %s", e);
                            mAdapter.notifyItemChanged(position);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
