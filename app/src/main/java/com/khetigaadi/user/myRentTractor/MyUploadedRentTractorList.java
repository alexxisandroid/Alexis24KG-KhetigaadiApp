package com.khetigaadi.user.myRentTractor;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.databinding.DeleteTrFeedbackFormDialogBinding;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.rent.RentTractorDetails;
import com.khetigaadi.user.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class MyUploadedRentTractorList extends BaseFragment {
    private MyUploadedRentTractorListAdapter mAdapter;
    private FragmentActivity activity;
    private final String TAG = MyUploadedRentTractorList.class.getSimpleName();
    @Inject
    Tracker mTracker;
    @Inject
    KhetiAPIs khetiAPIs;
    @Inject
    SessionManager sessionManager;
    @Inject
    SharedPreferenceUtils pref;
    private MyRentTractorListViewModel viewModel;
    CompositeDisposable disposable = new CompositeDisposable();
    private RecycleListBinding binding;
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) activity).setToolbarTitle(getString(R.string.myUploadedRentTractor));
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        Timber.i("Setting screen name: %s", TAG);
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        binding = RecycleListBinding.inflate(getLayoutInflater(), container, false);
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        binding.myRecyclerView.setLayoutManager(mLayoutManager);
        binding.filterIcon.setVisibility(View.GONE);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view,@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MyRentTractorListViewModel.class);
        viewModel.getRentTractors().observe(getViewLifecycleOwner(), rentTractorRespStateData -> {
            switch (rentTractorRespStateData.getStatus()) {
                case LOADING:
                    binding.llProgress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.llProgress.setVisibility(View.GONE);
                    if (rentTractorRespStateData.getData() != null) {
                        mAdapter = new MyUploadedRentTractorListAdapter(activity, rentTractorRespStateData.getData().getData(), rentTractorRespStateData.getData().getOthers());
                       binding.myRecyclerView.setAdapter(mAdapter);
                        mAdapter.setOnItemClickListener((position, v) -> {
                            Intent intent = new Intent(activity, RentTractorDetails.class);
                            intent.putExtra("tractorId", mAdapter.getItem(position).getRentTractorId());
                            startActivity(intent);
                        });
                        mAdapter.setOnTrashClickListener((position, v) -> showNewDeleteDialog(mAdapter.getItem(position).getRentTractorId()));
                    }
                    break;
                case ERROR:
                    binding.llProgress.setVisibility(View.GONE);
                    Toast.makeText(activity, rentTractorRespStateData.getError(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
    private void showNewDeleteDialog(final String id) {
        // get prompts.xml view
        final Dialog d = new Dialog(activity);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setCancelable(false);
        d.setCanceledOnTouchOutside(false);
        DeleteTrFeedbackFormDialogBinding dialogBinding = DeleteTrFeedbackFormDialogBinding.inflate(getLayoutInflater());
        d.setContentView(dialogBinding.getRoot());
        d.show();
        // set prompts.xml to be the layout file of the alertdialog builder
        // create an alert dialog
        dialogBinding. btnClose.setOnClickListener(v -> {
            d.dismiss();
            ((MainActivity) activity).hideSoftKeyboard();
        });
        dialogBinding. btnSubmit.setOnClickListener(v -> {
            if (!dialogBinding.spReasonType.getSelectedItem().toString().equalsIgnoreCase("Select Reason")) {
                if (dialogBinding.tvReason.getText().toString().length() != 0) {
                    try {
                        Map<String,Object> params = new HashMap<>();
                        params.put("rent_tractor_id", id);
                        params.put("delete_request_type_id", dialogBinding.spReasonType.getSelectedItemPosition());
                        params.put("reason", dialogBinding.tvReason.getText().toString());

                        disposable.add(khetiAPIs.rentTrDeleteReq(params)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                                    @Override
                                    public void onSuccess(@NonNull MyResponse myResponse) {
                                        try {
                                            if (!myResponse.isError()) {
                                                for (int i = 0; i < myResponse.getMsg().size(); i++) {
                                                    Toast.makeText(activity, myResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                                }
                                                d.dismiss();
                                                ((MainActivity) activity).hideSoftKeyboard();
                                            } else {
                                                for (int i = 0; i < myResponse.getMsg().size(); i++) {
                                                    Toast.makeText(activity, myResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                                }
                                                d.dismiss();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                        Timber.e(e, "onError: ");

                                    }
                                }));
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
}