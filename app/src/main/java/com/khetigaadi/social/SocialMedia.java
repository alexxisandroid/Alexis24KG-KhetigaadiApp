package com.khetigaadi.social;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.khetigaadi.R;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.SocialMediaBinding;
import java.util.ArrayList;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class SocialMedia extends BaseActivity {
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private static ArrayList<String> socialMediaUrl;
    private SocialMediaFragment facebookFragment;
    private SocialMediaFragment twitterFragment;
    private SocialMediaFragment linkedInFragment;
    private SocialMediaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SocialMediaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initToolbar();
        socialMediaUrl = new ArrayList<>();
        socialMediaUrl.add("https://www.facebook.com/khetigaadi.India/");
        socialMediaUrl.add("https://www.instagram.com/khetigaadi/");
        socialMediaUrl.add("https://www.linkedin.com/in/khetigaadi");
        mFragmentManager = getSupportFragmentManager();
//        callFacebookPage();
        if (getIntent().getExtras() != null) {
            int page = getIntent().getExtras().getInt("page", 1);
            switch (page) {
                case 1: {
                    callFacebookPage();
                    break;
                }
                case 2: {
                    callInstagramPage();
                    break;
                }
                case 3: {
                    callLinkedInPage();
                    break;
                }

                default: {
                    callFacebookPage();
                }
            }
        }
        binding.imgBtnFacebook.setOnClickListener(v -> callFacebookPage());
        binding.imgBtnInstagram.setOnClickListener(v -> callInstagramPage());
        binding.imgBtnLinkedIn.setOnClickListener(v -> callLinkedInPage());
        if (!Constants.isConnectionAvailable(this)) {
            noInternetConnection(binding.parent);
        }
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.social_media));
    }
    private void callLinkedInPage() {
        if (linkedInFragment == null) {
            linkedInFragment = SocialMediaFragment.newInstance(SocialMedia.socialMediaUrl.get(2));
        }
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        mFragmentTransaction.replace(binding.clPageViewer.getId(), linkedInFragment).commit();
    }
    private void callInstagramPage() {
        if (twitterFragment == null) {
            twitterFragment = SocialMediaFragment.newInstance(SocialMedia.socialMediaUrl.get(1));
        }
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        mFragmentTransaction.replace(binding.clPageViewer.getId(), twitterFragment).commit();
    }
    private void callFacebookPage() {
        if (facebookFragment == null) {
            facebookFragment = SocialMediaFragment.newInstance(SocialMedia.socialMediaUrl.get(0));
        }
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        mFragmentTransaction.replace(binding.clPageViewer.getId(), facebookFragment).commit();
    }
}
