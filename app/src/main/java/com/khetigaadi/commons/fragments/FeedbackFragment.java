package com.khetigaadi.commons.fragments;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.R;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.commons.activities.FeedBackActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.FragmentFeedbackBinding;
import com.khetigaadi.retrofit.KhetiAPIs;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
/**
 * Created by Irfan on 11-Jun-16.
 */
@AndroidEntryPoint
public class FeedbackFragment extends BaseFragment {
    private FragmentActivity activity;
    private final String TAG = "FeedBack";
    @Inject
    Tracker mTracker;
    private FragmentFeedbackBinding binding;
    @Inject
    CompositeDisposable composite;
    @Inject
    KhetiAPIs khetiAPIs;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        findViewById();
        initListener();
        binding.riSubmit.setOnRippleCompleteListener(rippleView -> {
            if (validation()) {
                if (Constants.isConnectionAvailable(activity)) {
                    submitForm();
                }
            }
        });
        binding.llRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=";
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("market://details?id=com.khetigaadi"));
                    startActivity(i);
                }
                catch(ActivityNotFoundException e) {
                    e.printStackTrace();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL + "com.khetigaadi")));
                }
            }
        });
        binding.tvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=";
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("market://details?id=com.khetigaadi"));
                    startActivity(i);
                }
                catch(ActivityNotFoundException e) {
                    e.printStackTrace();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL + "com.khetigaadi")));
                }
            }
        });
        return view;
    }
    private void initListener() {
        ArrayAdapter adapter = new ArrayAdapter(activity, R.layout.state_list_item, getResources().getStringArray(R.array.feedbackType));
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.actvFeedbackType.setAdapter(adapter);
    }
    private void submitForm() {
        binding.llProgress.setVisibility(View.VISIBLE);
        Map<String, Object> params = new HashMap<>();
        params.put("feedback_type", binding.actvFeedbackType.toString());
        params.put("message", binding.tvComments.getText().toString());
        composite.add(khetiAPIs.feedback(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse response) {
                        try {
                            if (!response.isError()) {
                                Toast.makeText(activity, response.getMsg().get(0), Toast.LENGTH_LONG).show();
                                ((FeedBackActivity) activity).onBackPressed();
                            } else {
                                Toast.makeText(activity, response.getMsg().get(0), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }
    private boolean validation() {
        if (TextUtils.isEmpty(binding.actvFeedbackType.getText())) {
            Toast.makeText(activity, getString(R.string.please_select_type), Toast.LENGTH_SHORT).show();
            return false;
        }
        return validateComment();
    }
    private void findViewById() {
        binding.tvComments.addTextChangedListener(new MyTextWatcher(binding.tvComments));
    }
    @Override
    public void onResume() {
        super.onResume();
        Timber.i("Setting screen name: %s", TAG);
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            if (view.getId() == R.id.tvComments) {
                validateComment();
            }
        }

    }
    private boolean validateComment() {
        String comm = binding.tvComments.getText().toString().trim();
        if (comm.isEmpty()) {
            binding.inputLayoutComment.setError(getString(R.string.err_msg_comment));
            requestFocus(binding.tvComments);
            return false;
        } else if (comm.length() < 20) {
            binding.inputLayoutComment.setError(getString(R.string.please_enter_at_least_characters_in_comment));
            requestFocus(binding.tvComments);
            return false;
        } else {
            binding.inputLayoutComment.setErrorEnabled(false);
        }
        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
