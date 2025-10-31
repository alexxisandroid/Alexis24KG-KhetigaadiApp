package com.khetigaadi.user.profile;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.WebViewActivity;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.model.agri.Crop;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityProfileBinding;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;
import com.khetigaadi.user.model.UserResponse;
import com.khetigaadi.user.retrofit.UserAPIs;
import com.khetigaadi.util.NumberFormatting;
import org.jetbrains.annotations.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
@AndroidEntryPoint
public class ProfileActivity extends BaseActivity {
    private static final int HIDE_MENU = 1;
    private User userLoggedIn;
    private boolean isAWardEnable;
    private boolean isBiographyEnable;
    private String paramUserId;
    private User secondUser;
    private int mState = 0;
    @Inject
    SharedPreferenceUtils pref;
    ActivityProfileBinding binding;
    @Inject
    SessionManager sessionManager;
    @Inject
    UserAPIs userAPIs;
    private final io.reactivex.rxjava3.disposables.CompositeDisposable disposable = new io.reactivex.rxjava3.disposables.CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        findViewById();
        initToolbar();
        binding.tvDeleteAccount.setPaintFlags(binding.tvDeleteAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        userLoggedIn = sessionManager.getUser();
        binding.tvDeleteAccount.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, WebViewActivity.class);
            intent.putExtra(WebViewActivity.TITLE, "Terms and Conditions");
            intent.putExtra(WebViewActivity.URL, "https://khetigaadi.com/delete-user/user/en");
            startActivity(intent);
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        handleDeepLinking();
        initListener();
    }
    private void handleDeepLinking() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            if (getIntent().getAction() != null && getIntent().getAction().equalsIgnoreCase(Intent.ACTION_VIEW)) {
                paramUserId = intent.getData().toString().split("profile/")[1];
            } else {
                paramUserId = getIntent().getExtras().getString("userId");
            }
            if (paramUserId != null)
                getProfile(paramUserId);
        }
        Log.d("vcbcvbvcbcvbcvb====",""+paramUserId);
    }
    private void hideShowView(String userId) {
        if (userLoggedIn.getId().equals(userId)) {
            binding.tvCommentsByMe.setText(getString(R.string.my_posts));
            binding.inUserCredit.getRoot().setVisibility(View.GONE);
            binding.inUserCreditLarge.getRoot().setVisibility(View.VISIBLE);
            binding.btnFollow.setVisibility(View.GONE);
            binding.ivEditBiography.setVisibility(View.VISIBLE);
            binding.ivAwards.setVisibility(View.VISIBLE);
        } else {
            binding.inUserCredit.getRoot().setVisibility(View.VISIBLE);
            binding.inUserCreditLarge.getRoot().setVisibility(View.GONE);
            binding.tvCommentsByMe.setText(String.format("Post By %s", secondUser.getFname()));
            binding.btnFollow.setVisibility(View.GONE);
            binding.ivEditBiography.setVisibility(View.GONE);
            binding.ivAwards.setVisibility(View.GONE);
            mState = HIDE_MENU;
            invalidateOptionsMenu();
        }
        binding.ivLikeBottom.setOnClickListener(v -> callLikeApi(binding.ivLikeBottom, secondUser));
        binding.ivShareBottom.setOnClickListener(v -> {
            callSharePostApi(secondUser.getId());
            sharePost(secondUser.getId());
        });
        binding.tvCommentsByMe.setOnClickListener(v -> {

        });
    }
    private void sharePost(String secondUserId) {
        String sharePostText = "https://www.khetigaadi.com/khetiguru/profile/" + secondUserId;
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, sharePostText);
        try {
            startActivity(Intent.createChooser(intent, "Select an action"));
        } catch (ActivityNotFoundException ex) {
            // (handle error)
        }
    }
    private void callSharePostApi(String secondUserId) {
        shareStateChange();
        Map<String, String> params = new HashMap<>();
        try {
            params.put("to_user_id", secondUserId);
            userAPIs.shareUser(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void shareStateChange() {
        secondUser.setShareCount((int) (secondUser.getShareCount() + 1));
        binding.inUserCredit.tvShares.setText(String.valueOf(secondUser.getShareCount()));
    }
    private void callLikeApi(ImageView ivLikeBottom,User secondUser) {
        likeStateChange(ivLikeBottom);
//        disposable.add(userAPIs.likeUser(secondUser)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
//                    @Override
//                    public void onSuccess(@NonNull MyResponse response) {
//                        if (response.isError()) {
//                            likeStateChange(ivLikeBottom);
//                            Toast.makeText(ProfileActivity.this,response.getMsg().get(0),Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                        likeStateChange(ivLikeBottom);
//                    }
//                }));
        disposable.add(userAPIs.likeUser(secondUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new io.reactivex.rxjava3.observers.DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NotNull MyResponse safetyTipsResp) {
                        if (!safetyTipsResp.isError()) {
                            likeStateChange(ivLikeBottom);
                            Toast.makeText(ProfileActivity.this,safetyTipsResp.getMsg().get(0),Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        likeStateChange(ivLikeBottom);
                    }
                }));
    }
    private void likeStateChange(ImageView ivLikeBottom) {
        secondUser.setLikeCount((int) (secondUser.isLiked() ? secondUser.getLikeCount() - 1 : secondUser.getLikeCount() + 1));
        secondUser.setLiked(!secondUser.isLiked());
        binding.inUserCredit.tvLikes.setText(String.valueOf(secondUser.getLikeCount()));
        binding.inUserCreditLarge.tvLikes.setText(String.valueOf(secondUser.getLikeCount()));
        if (secondUser.isLiked()) {
            ivLikeBottom.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.like_green));
        } else {
            ivLikeBottom.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.like));
        }
    }
    private void initListener() {
        binding.btnFollow.setOnClickListener(v -> callFollowApi((Button) v));
        binding.ivEditBiography.setOnClickListener(v -> {
            if (isBiographyEnable) {
                if (binding.etBiagraphy.getText().toString().length() > 0) {
                    updateBiographyEnable(binding.etBiagraphy.getText().toString());
                } else {
                    Toast.makeText(ProfileActivity.this, getString(R.string.please_enter_biography), Toast.LENGTH_SHORT).show();
                }
                isBiographyEnable = false;
            }
            else {
                binding.etBiagraphy.setEnabled(true);
                binding.ivEditBiography.setImageResource(R.drawable.baseline_send_black_24);
                binding.etBiagraphy.setSelection(binding.etBiagraphy.getText().toString().length());
                binding.etBiagraphy.requestFocus();
                isBiographyEnable = true;
            }
        });
        binding.ivAwards.setOnClickListener(v -> {
            if (isAWardEnable) {
                if (binding.etAwards.getText().toString().length() > 0) {
                    updateAwards(binding.etAwards.getText().toString());
                } else {
                    Toast.makeText(ProfileActivity.this, getString(R.string.please_enter_award), Toast.LENGTH_SHORT).show();
                }
                isAWardEnable = false;
            } else {
                binding.etAwards.setEnabled(true);
                binding.ivAwards.setImageResource(R.drawable.baseline_send_black_24);
                binding.etAwards.requestFocus();
                binding.etAwards.setSelection(binding.etAwards.getText().toString().length());
                isAWardEnable = true;
            }
        });
    }
    private void callFollowApi(Button btnFollow) {
        followStateChange(btnFollow);
      try {
          Map<String,String> params = new HashMap<>();
          params.put("user_id", paramUserId);

          disposable.add(userAPIs.followUser(params)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribeWith(new io.reactivex.rxjava3.observers.DisposableSingleObserver<MyResponse>() {
                      @Override
                      public void onSuccess(@NotNull MyResponse response) {
                          try {
                              if (response.isError()) {
                                  followStateChange(btnFollow);
                                  for (int i = 0; i < response.getMsg().size(); i++) {
                                      Toast.makeText(ProfileActivity.this, response.getMsg().get(i), Toast.LENGTH_LONG).show();
                                  }
                              }
                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      }

                      @Override
                      public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                          followStateChange(btnFollow);
                          Toast.makeText(ProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                      }
                  }));
      }
      catch (Exception e){
          Log.d("vcbcb",""+e.getMessage());
        }
    }
    private void followStateChange(Button btnFollow) {
        if (secondUser != null) {
            secondUser.setFollowerCount((int) (secondUser.isFollowerStatus() ? secondUser.getFollowerCount() - 1 : secondUser.getFollowerCount() + 1));
            secondUser.setFollowerStatus(!secondUser.isFollowerStatus());
            binding.inUserCredit.tvFollowers.setText(NumberFormatting.format(secondUser.getFollowerCount()));
            if (secondUser.isFollowerStatus()) {
                btnFollow.setText(getString(R.string.following));
            } else {
                btnFollow.setText(getString(R.string.follow));
            }
        }
        binding.btnFollow.setVisibility(View.GONE);
    }
    private void updateBiographyEnable(String biography) {
       // showProgressDialog(getString(R.string.loading));
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        userLoggedIn.getUserDetail().setBiography(biography);

        disposable.add(userAPIs.updateUser(userLoggedIn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new io.reactivex.rxjava3.observers.DisposableSingleObserver<UserResponse>() {
                    @Override
                    public void onSuccess(@NotNull UserResponse userResponse) {

                        try {
                            binding.llProgresss.llProgress.setVisibility(View.GONE);
                            if (!userResponse.isError()) {
                                binding.etBiagraphy.setEnabled(false);
                                userLoggedIn.getUserDetail().setBiography(biography);
                                updateUser(userLoggedIn);
                                binding.ivEditBiography.setImageResource(R.drawable.ic_edit);
                            } else {
                                for (int i = 0; i < userResponse.getMsg().size(); i++) {
                                    Toast.makeText((Context) ProfileActivity.this, ""+userResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        catch(Exception e) {
                            binding.llProgresss.llProgress.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        dismissProgressDialog();
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this,""+getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                }));
    }
    private void updateAwards(String awards) {
       // showProgressDialog(getString(R.string.loading));
        userLoggedIn.getUserDetail().setAwards(awards);
        disposable.add(userAPIs.updateUser(userLoggedIn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new io.reactivex.rxjava3.observers.DisposableSingleObserver<UserResponse>() {
                    @Override
                    public void onSuccess(@NotNull UserResponse userResponse) {
                        dismissProgressDialog();
                        try {
                            if (!userResponse.isError()) {
                                binding.etAwards.setEnabled(false);
                                userLoggedIn.getUserDetail().setAwards(awards);
                                updateUser(userLoggedIn);
                                binding.ivAwards.setImageResource(R.drawable.ic_edit);
                            } else {
                                for (int i = 0; i < userResponse.getMsg().size(); i++) {
                                    Toast.makeText((Context) ProfileActivity.this, ""+ userResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(ProfileActivity.this,getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                    }
                }));
    }
    private void updateUser(User userLoggedIn) {
        sessionManager.setUser(userLoggedIn);
    }
    private void getProfile(String paramUserId) {
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
      //  binding.nsvParent.setVisibility(View.GONE);
//        disposable.add(userAPIs.getUser(paramUserId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<UserResponse>() {
//                    @Override
//                    public void onSuccess(@NonNull UserResponse userResponse) {
//                        binding.llProgresss.llProgress.setVisibility(View.GONE);
//                        binding.nsvParent.setVisibility(View.VISIBLE);
//                        if (!userResponse.isError()) {
//                            secondUser = userResponse.getUser();
//                            if (userLoggedIn.getId().equals(paramUserId)) {
//                                sessionManager.setUser(userResponse.getUser());
//                                sessionManager.setUserOthers(userResponse.getOthers());
//                            }
//                            hideShowView(ProfileActivity.this.paramUserId);
//                            initUI(secondUser);
//                        }
//                    }
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        binding.llProgresss.llProgress.setVisibility(View.GONE);
//                        binding.nsvParent.setVisibility(View.VISIBLE);
//                        Toast.makeText(ProfileActivity.this, getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                }));

        disposable.add(userAPIs.getUser(paramUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new io.reactivex.rxjava3.observers.DisposableSingleObserver<UserResponse>() {
                    @Override
                    public void onSuccess(@NotNull UserResponse userResponse) {
                        try {
                            if (userResponse.isError()) {
                                binding.llProgresss.llProgress.setVisibility(View.GONE);
                                binding.nsvParent.setVisibility(View.VISIBLE);
                                if (!userResponse.isError()) {
                                    secondUser = userResponse.getUser();
                                    if (userLoggedIn.getId().equals(paramUserId)) {
                                        sessionManager.setUser(userResponse.getUser());
                                        sessionManager.setUserOthers(userResponse.getOthers());
                                    }
                                    hideShowView(ProfileActivity.this.paramUserId);
                                    initUI(secondUser);
                                }
                            }
                        }
                        catch(Exception e) {
                            binding.llProgresss.llProgress.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                        binding.nsvParent.setVisibility(View.VISIBLE);
                        Toast.makeText(ProfileActivity.this, getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }));
    }
    private void initUI(User user) {
        try {
            binding.llProgresss.llProgress.setVisibility(View.GONE);
            Glide.with(ProfileActivity.this).load(Constants.decodeURL(sessionManager.getUserOthers().getUserPath() + user.getImage())).into(binding.civProfile);

            if(user.getUserDetail().getType() != null) {
                if(user.getUserDetail().getType().getUserLevelName() != null && !user.getUserDetail().getType().getUserLevelName().equals("null")) {
                    binding.tvUserType.setText(user.getUserDetail().getType().getUserLevelName());
                }
            }
            binding.tvUserName.setText(String.format("%s %s", user.getFname(),user.getLname()));
            binding.tvAddress.setText(String.format("%s, %s", user.getState().getName(),user.getCity().getName()));

           // SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String dateInString = user.getRegisteredDate();
            Log.d("cxvcvxvxvxcv564545vx====",""+dateInString+"=="+user.getUserDetail().getType());

           Log.d("dvfdzsfsfsf====",""+user.getUserDetail().getType().getUserLevelName());
            SimpleDateFormat formatterOut = new SimpleDateFormat("dd-MM-yyyy");

            try {
                Date date = formatter.parse(dateInString);
                System.out.println(date);
                //System.out.println(formatterOut.format(date));
                assert date != null;
                String dtt = formatterOut.format(date);
               binding.tvRegisteredOn.setText(String.format("%s %s", getString(R.string.registered_on),dtt));
            }
            catch(ParseException e) {
                e.printStackTrace();
            }
           // binding.tvRegisteredOn.setText(String.format("%s %s", getString(R.string.registered_on),DateUtils.convertDateToDate(user.getRegisteredDate(),DateUtils.DateFormat.yyyy_MM_dd_T_HH_mm_ss_SSS_Z,DateUtils.DateFormat.MMM_dd_yyyy)));
            if(user.isFollowerStatus()) {
                binding.btnFollow.setText(getString(R.string.following));
            }
            else {
                binding.btnFollow.setText(getString(R.string.follow));
            }
            binding.inUserCreditLarge.tvFollowers.setText(String.valueOf(user.getFollowerCount()));
            binding.inUserCreditLarge.tvFollowing.setText(String.valueOf(user.getFollowingCount()));
            binding.inUserCreditLarge.tvLikes.setText(String.valueOf(user.getLikeCount()));
            binding.inUserCreditLarge.tvPosts.setText(String.valueOf(user.getPostCount()));
            binding.inUserCreditLarge.tvComments.setText(String.valueOf(user.getCommentCount()));
            binding.inUserCreditLarge.tvShares.setText(String.valueOf(user.getShareCount()));
            initCropView(binding.llCrops, user.getUserDetail().getCrops());
            binding.inUserCredit.tvFollowers.setText(NumberFormatting.format(user.getFollowerCount()));
            binding.inUserCredit.tvFollowing.setText(NumberFormatting.format(user.getFollowingCount()));
            binding.inUserCredit.tvLikes.setText(NumberFormatting.format(user.getLikeCount()));
            if(user.isLiked()) {
                binding.ivLikeBottom.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.like_green));
            }
            else {
                binding.ivLikeBottom.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.like));
            }
            binding.inUserCredit.tvPosts.setText(NumberFormatting.format(user.getPostCount()));
            binding.inUserCredit.tvComments.setText(NumberFormatting.format(user.getCommentCount()));
            binding.inUserCredit.tvShares.setText(NumberFormatting.format(user.getShareCount()));
            if(user.getUserDetail().getBiography() != null) {
                binding.etBiagraphy.setText(user.getUserDetail().getBiography());
            }
            if(user.getUserDetail().getAwards() != null) {
                binding.etAwards.setText(user.getUserDetail().getAwards());
            }
        }
        catch(Exception e) {
            binding.llProgresss.llProgress.setVisibility(View.GONE);
            Log.d("xcvcvxv3452345cxvxvxv====",""+e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
    private void initCropView(@NonNull LinearLayout llCrops,List<Crop> crops) {
        llCrops.removeAllViews();
        binding.llProgresss.llProgress.setVisibility(View.GONE);
        if(crops != null) {
            for(int i = 0; i < crops.size(); i++) {
                TextView rowLayout = (TextView) getLayoutInflater().inflate(R.layout.crop_profile_item, llCrops, false);
                rowLayout.setText(crops.get(i).getName());
                llCrops.addView(rowLayout);
            }
            llCrops.setVisibility(View.VISIBLE);
            binding.ivCrop.setVisibility(View.VISIBLE);
        }
        else {
            llCrops.setVisibility(View.GONE);
            binding.ivCrop.setVisibility(View.GONE);
        }
    }
    private void findViewById() {
        Glide.with(ProfileActivity.this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
    }
    @Override
    public boolean onCreateOptionsMenu(@androidx.annotation.NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        if(mState == HIDE_MENU) {
            for(int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() == R.id.menu_edit) {
            addSomething();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (paramUserId != null && paramUserId.equals(userLoggedIn.getId())) {
            userLoggedIn = sessionManager.getUser();
            initUI(userLoggedIn);
        }
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }
    private void addSomething() {
        startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
    }
}
