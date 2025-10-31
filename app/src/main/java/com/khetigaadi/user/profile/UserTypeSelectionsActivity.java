package com.khetigaadi.user.profile;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityUserTypeSelectionsBinding;
import com.khetigaadi.user.adapter.UserTypeSelectionAdapter;
import com.khetigaadi.user.model.UserTypes;
import com.khetigaadi.user.retrofit.UserAPIs;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
@AndroidEntryPoint
public class UserTypeSelectionsActivity extends BaseActivity {
    public static final String SELECTED_USER_TYPE = "selectedUserType";
    private UserTypeSelectionAdapter mAdapter;
    @Inject
    UserAPIs userAPIs;
    @Inject
    CompositeDisposable disposable;
   // private ActivityUserTypeSelectionBinding binding;
    private ActivityUserTypeSelectionsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = ActivityUserTypeSelectionsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        findViewById();
        initToolbar();
        initView();
        if(Constants.isConnectionAvailable(this)) {
            getUserTypes();
        } else {
            noInternetConnection(binding.parent);
        }
    }
    private void getUserTypes() {
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        disposable.add(userAPIs.getUserType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserTypes>() {
                    @Override
                    public void onSuccess(@NonNull UserTypes userTypes) {
                        try {
                            if (!userTypes.isError()) {
                                mAdapter.setData(userTypes.getData());
                                binding.edSearch.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void onTextChanged(CharSequence s,int start,int before,int count) {
                                        mAdapter.getFilter().filter(s.toString());
                                    }
                                    @Override
                                    public void beforeTextChanged(CharSequence s,int start,int count,int after) {

                                    }
                                    @Override
                                    public void afterTextChanged(Editable s) {
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(UserTypeSelectionsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
                }));
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.what_kind_of_user_are_you));
    }
    private void findViewById() {
        Glide.with(UserTypeSelectionsActivity.this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
    }
    private void initView() {
        mAdapter = new UserTypeSelectionAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(UserTypeSelectionsActivity.this);
        binding.mRecyclerView.setHasFixedSize(true);
        binding.mRecyclerView.setLayoutManager(manager);
        binding.mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(brand -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SELECTED_USER_TYPE,brand);
            setResult(RESULT_OK,returnIntent);
            finish();
        });
        binding.llButton.setVisibility(View.GONE);
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams)binding.mRecyclerView.getLayoutParams();
        marginLayoutParams.setMargins(0,0,0,0);
        binding.mRecyclerView.setLayoutParams(marginLayoutParams);
    }
    @Override
    protected void onPause() {
        super.onPause();
        hideSoftKeyboard(UserTypeSelectionsActivity.this);
    }
}
