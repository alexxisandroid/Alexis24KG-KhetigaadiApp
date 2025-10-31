package com.khetigaadi.commons.fragments;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.base.model.Banner;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.base.model.State;
import com.khetigaadi.commons.NewsAdapter;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.SearchActivity;
import com.khetigaadi.commons.activities.VideosActivity;
import com.khetigaadi.commons.activities.YouTubePlayerActivity;
import com.khetigaadi.commons.adapter.BrandAdapter;
import com.khetigaadi.commons.adapter.StateHomeListAdapter;
import com.khetigaadi.commons.adapter.TestimonialListAdapter;
import com.khetigaadi.commons.adapter.TwoBannerSlideAdapter;
import com.khetigaadi.commons.utils.CirclePagerIndicatorDecoration;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.commons.utils.HomeListResp;
import com.khetigaadi.databinding.HomeBinding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.def.CallFrom;
import com.khetigaadi.def.PriceType;
import com.khetigaadi.ecom.EcomHomeActivity;
import com.khetigaadi.ecom.adapter.CategoriesAdapter;
import com.khetigaadi.ecom.list.ProductListActivity;
import com.khetigaadi.ecom.list.ProductListFilterActivity;
import com.khetigaadi.harvester.HarvesterDetailsActivity;
import com.khetigaadi.harvester.HarvesterListAdapter;
import com.khetigaadi.harvester.model.Harvester;
import com.khetigaadi.implement.ImpsAdapter;
import com.khetigaadi.implement.compare.CompareImp;
import com.khetigaadi.implement.compare.adapter.CompareImplementTractorAdapter;
import com.khetigaadi.implement.compare.util.PopularImpComp;
import com.khetigaadi.implement.newImplement.NewImpOverviewDetails;
import com.khetigaadi.implement.newImplement.NewImplementsList;
import com.khetigaadi.implement.newImplement.SearchNewImplementsFilter;
import com.khetigaadi.isOwner.IsOwnerTractorBrand;
import com.khetigaadi.news.activity.NewsDetailsActivity;
import com.khetigaadi.news.fragments.NewsListFragment;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.BrandRepository;
import com.khetigaadi.room.repository.CategoryRepository;
import com.khetigaadi.room.repository.RecentRepo;
import com.khetigaadi.room.repository.StateRepository;
import com.khetigaadi.shop.model.Category;
import com.khetigaadi.tractor.PopularTractorAdapter;
import com.khetigaadi.tractor.compare.ComparePopularTractorAdapter;
import com.khetigaadi.tractor.compare.CompareTractorActivity;
import com.khetigaadi.tractor.compare.model.CompareTractorParam;
import com.khetigaadi.tractor.compare.util.Tractors;
import com.khetigaadi.tractor.newTractor.NewTrGetOnRoadPriceActivity;
import com.khetigaadi.tractor.newTractor.NewTractorDetails;
import com.khetigaadi.tractor.newTractor.NewTractorsListFragment;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.Tractor;
import com.khetigaadi.tractor.newTractor.search.SearchNewTractorFilter;
import com.khetigaadi.tractor.usedTractor.GetUsedTrSellerDetails;
import com.khetigaadi.tractor.usedTractor.UsedTractorDetailActivity;
import com.khetigaadi.tractor.usedTractor.UsedTractorHomeListAdapter;
import com.khetigaadi.tractor.usedTractor.UsedTractorsListFragment;
import com.khetigaadi.tractor.usedTractor.search.SearchUsedTractorFilter;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;
import com.khetigaadi.user.profile.EditProfileActivity;
import com.khetigaadi.youtube.VideoHomeListAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final Object LOADING = "loading";
    private List<Banner> bannerList;
    private boolean stopSliding = false;
    private Handler handler;
    private Runnable animateViewPager;
    private FragmentActivity activity;
    private FragmentManager mFragmentManager;
    private ProgressDialog pDialog;
    private HomeListResp homeList;
    CompositeDisposable disposable = new CompositeDisposable();
    @Inject
    BrandRepository brandRepository;
    @Inject
    StateRepository stateRepository;
    @Inject
    CategoryRepository categoryRepository;
    @Inject
    SessionManager sessionManager;
    @Inject
    Tracker mTracker;
    @Inject
    Application application;
    @Inject
    RecentRepo recentRepo;
    @Inject
    User user;
    @Inject
    KhetiAPIs khetiAPIs;
    @Inject
    SharedPreferenceUtils pref;
    private HomeBinding binding;
    private HomeFragmentViewModel viewModel;
    private UsedTractorHomeListAdapter usedTractorListAdapter;
    private NewsAdapter newsAdapter;
    private ImpsAdapter impsAdapter;
    private ComparePopularTractorAdapter compareTractorAdapter;
    private CompareImplementTractorAdapter compareImplementAdapter;
    private HarvesterListAdapter harvesterListAdapter;
    private CategoriesAdapter categoriesAdapter;
    private VideoHomeListAdapter videoAdapter;
    private TestimonialListAdapter testimonialsAdapter;
    private StateHomeListAdapter stateAdapter;
    private List<Banner> otherBannerList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        handler = new Handler(Looper.getMainLooper());
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = HomeBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        ((MainActivity) activity).setToolbarTitle(getString(R.string.app_name));
        binding.btnIsOwner.setOnClickListener(View -> {
            Intent intent = new Intent(activity, IsOwnerTractorBrand.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(false);
        if (!SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.POPULAR_TRACTORS,"").equals("")) {
            try {
                setPopularTractorAndNewsAdapter(new Gson().fromJson(SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.POPULAR_TRACTORS, ""),HomeListResp.class));

                Log.d("bcvxbcb====",""+new Gson().fromJson(SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.POPULAR_TRACTORS, ""),HomeListResp.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        binding.cvNewTractor.setOnClickListener(view1 -> {
            navigateToSearchNewTractor(false);
        });
        binding.cvOldTractor.setOnClickListener(view1 -> {
            mFragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            fragmentTransaction.replace(contentView.getId(), new SearchUsedTractorFilter()).addToBackStack(TAG).commit();
        });
        binding.cvSellTractor.setOnClickListener(v -> ((MainActivity) activity).navigateToSellTractor());
        binding.cvShop.setOnClickListener(v -> startActivity(new Intent(activity,EcomHomeActivity.class)));
        binding.cvHarvester.setOnClickListener(v -> ((MainActivity) activity).navigateToHarvester());
        binding.cvImplements.setOnClickListener(v -> navigateToSearchImplement(false));
        binding.cvVideo.setOnClickListener(view12 -> startActivity(new Intent(activity,VideosActivity.class)));
        binding.cvCompare.setOnClickListener(view13 -> {
            TabCompare compareTractorSelection = new TabCompare();
            mFragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            fragmentTransaction.replace(contentView.getId(), compareTractorSelection).addToBackStack(TAG).commit();
        });
        binding.cvNews.setOnClickListener(v -> ((MainActivity) activity).navigateToNewsFragment());
        binding.tvProfileUpdate.setOnClickListener(v -> startActivity(new Intent(getActivity(), EditProfileActivity.class)));
        binding.tvMainMenuViewMore.setOnClickListener(v -> {
            if (binding.tvMainMenuViewMore.getText().toString().equals(getString(R.string.view_more))) {
                binding.tvMainMenuViewMore.setText(getString(R.string.view_less));
                binding.tvMainMenuViewMore.setCompoundDrawables(null, null, ContextCompat.getDrawable(activity, R.drawable.up_arrow), null);
                binding.cvVideo.setVisibility(View.VISIBLE);
                binding.cvCompare.setVisibility(View.VISIBLE);
//                binding.cvTyre.setVisibility(View.VISIBLE);
                binding.cvNews.setVisibility(View.VISIBLE);
//                binding.cvInsurance.setVisibility(View.VISIBLE);
//                binding.cvFinance.setVisibility(View.VISIBLE);
            } else {
                binding.tvMainMenuViewMore.setText(getString(R.string.view_more));
                binding.tvMainMenuViewMore.setCompoundDrawables(null, null, ContextCompat.getDrawable(activity, R.drawable.ic_down_arrow_black_24dp), null);
                binding.cvVideo.setVisibility(View.GONE);
                binding.cvCompare.setVisibility(View.GONE);
                binding.cvTyre.setVisibility(View.GONE);
                binding.cvNews.setVisibility(View.GONE);
                binding.cvInsurance.setVisibility(View.GONE);
                binding.cvFinance.setVisibility(View.GONE);
            }
        });
        binding.viewVoiceRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SearchActivity.class);
                intent.putExtra(SearchActivity.IS_RECORDING, true);
                startActivity(intent);
            }
        });
        binding.tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, SearchActivity.class));
            }
        });
        if (Constants.isConnectionAvailable(activity)) {
            getPopularTractor();
        }
        else {
            ((MainActivity) activity).noInternetConnection();
        }
        initBrands();
        initTractors();
        initCompare();
        initNews();
        initVideo();
        initTestimonials();
        return view;
    }
    private void initVideo() {
        videoAdapter = new VideoHomeListAdapter(activity, binding.includeVideos.rvList);
        binding.includeVideos.tvLabel.setText(getString(R.string.videos));
        binding.includeVideos.tvViewMore.setText(getString(R.string.show_all_videos));
        binding.includeVideos.tvViewMore.setOnClickListener(v -> startActivity(new Intent(activity,VideosActivity.class)));
        GridLayoutManager layoutManager = new GridLayoutManager(activity,2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position > 0) {
                    return 1;
                }
                return 2;
            }
        });
        binding.includeVideos.rvList.setLayoutManager(layoutManager);
        binding.includeVideos.rvList.setAdapter(videoAdapter);
        videoAdapter.setOnItemClickListener((item, v) -> {
            Intent intent = new Intent(activity, YouTubePlayerActivity.class);
            intent.putExtra("video",item);
            startActivity(intent);
        });
        binding.includeVideos.tvViewMore.setCompoundDrawables(null,null,null,null);
        binding.includeVideos.tvViewMore.setPadding(0,0,0,0);
    }
    private void initHarvester() {
        binding.includeHarvester.tvLabel.setText(getString(R.string.harvester));
        binding.includeHarvester.tvViewMore.setOnClickListener(v -> ((MainActivity) activity).navigateToHarvester());
        if (harvesterListAdapter == null) {
            initHarvesterAdapter();
        }
        binding.includeHarvester.rvList.setAdapter(harvesterListAdapter);
        if (harvesterListAdapter != null && harvesterListAdapter.getItemCount() <= 1) {
            viewModel.getHarvesterList(1).observe(getViewLifecycleOwner(), tractorsResp -> {
                switch (tractorsResp.getStatus()) {
                    case LOADING:
                        binding.includeHarvester.container.setVisibility(View.GONE);
                        break;
                    case SUCCESS:
                        if (tractorsResp.getData() != null) {
                            List<Harvester> list = tractorsResp.getData().getData().getData().subList(0, 4);
                            if (harvesterListAdapter == null) {
                                initCompareTractorAdapter();
                            }
                            harvesterListAdapter.setOthers(tractorsResp.getData().getOthers());
                            harvesterListAdapter.updateItems(list);
                            binding.includeHarvester.container.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            });
        }
    }
    private void initHarvesterAdapter() {
        harvesterListAdapter = new HarvesterListAdapter(activity, binding.includeCompare.rvList, true);
        harvesterListAdapter.setOnItemClickListener((position, v, harvester) -> {
            Intent intent = new Intent(activity, HarvesterDetailsActivity.class);
            intent.putExtra("harvesterId", harvester.getHarvesterId());
            intent.putExtra("position", position);
            startActivity(intent);
        });
    }
    private void initStates() {
        binding.includeStates.tvLabel.setText(getString(R.string.used_tractors_by_state));
        binding.includeStates.tvViewMore.setOnClickListener(v -> navigateToUsedTractorList(null));
        stateAdapter = new StateHomeListAdapter(activity, pref);
        binding.includeStates.rvList.setLayoutManager(new GridLayoutManager(activity, 4));
        binding.includeStates.rvList.setAdapter(stateAdapter);
        stateAdapter.setOnClickListener(this::navigateToUsedTractorList);
        stateRepository.getStatesByPosition().observe(getViewLifecycleOwner(), states -> {
            if (states.size()>0) {
                    if (states != null) {
                        stateAdapter.setData(states);
                    }
                    binding.includeStates.container.setVisibility(View.VISIBLE);
            }
            else
                binding.includeStates.container.setVisibility(View.GONE);
        });
    }
    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.menu_base_home,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
    private void initNews() {
        binding.includeNews.container.setVisibility(View.GONE);
        binding.includeNews.tvLabel.setText(getString(R.string.news));
        newsAdapter = new NewsAdapter(activity, binding.includeNews.rvList);
        binding.includeNews.rvList.setLayoutManager(new LinearLayoutManager(activity));
        binding.includeNews.rvList.setAdapter(newsAdapter);
        newsAdapter.setOnItemClickListener((position,v) -> {
            try {
                Intent i = new Intent(activity,NewsDetailsActivity.class);
                i.putExtra("news_id",newsAdapter.getItem(position).getId());
                startActivity(i);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });
        binding.includeNews.tvViewMore.setOnClickListener(v -> navigateToNewsList());
        binding.includeNews.tvViewMore.setText(getString(R.string.view_all_news));
        binding.includeNews.tvViewMore.setCompoundDrawables(null,null,null,null);
        binding.includeNews.tvViewMore.setPadding(0,0,0,0);
    }
    private void navigateToSearchImplement(boolean forBrand) {
        mFragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
        FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
        fragmentTransaction.replace(contentView.getId(),SearchNewImplementsFilter.getInstance(forBrand)).addToBackStack(TAG).commit();
    }
    private void navigateToNewsList() {
        mFragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
        FrameLayout frameLayout = (FrameLayout) activity.findViewById(R.id.containerView);
        fragmentTransaction.replace(frameLayout.getId(), new NewsListFragment()).addToBackStack(TAG).commit();
    }
    private void navigateToSearchNewTractor(boolean forBrand) {
        try {
            mFragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit, R.anim.pop_enter,R.anim.pop_exit);
            FrameLayout frameLayout = (FrameLayout) activity.findViewById(R.id.containerView);
            fragmentTransaction.replace(frameLayout.getId(), SearchNewTractorFilter.getInstance(forBrand)).addToBackStack(TAG).commit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void navigateToUsedTractorList(State state) {
        Filter filter = new Filter();
        if (state != null)
            filter.setStates(Collections.singletonList(state));
        FragmentManager mFragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
        FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
        fragmentTransaction.replace(contentView.getId(), UsedTractorsListFragment.getInstance(filter)).addToBackStack(TAG).commit();
    }
    private void initTractors() {
      List<String> optionList = new ArrayList<>();
        optionList.add("Tractors");
        optionList.add("Implements");
        optionList.add("Harvester");
        for (int i = 0; i < optionList.size(); i++) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.suggestion_chip,binding.includeTractors.chipGroup,false);
            chip.setText(optionList.get(i));
            chip.setOnCheckedChangeListener((buttonView,isChecked) -> {
                if (isChecked) {
                    Object tag = buttonView.getTag();
                    if ("Tractors".equals(tag)) {
                      showOldTractors();
                 } else if ("Implements".equals(tag)) {
                        showImplements();
                    } else if (tag.equals("Harvester")) {
                        showHarvesters();
                    }
                }

            });
            chip.setTag(optionList.get(i));
            if (i == 0)
                chip.setChecked(true);
            binding.includeTractors.chipGroup.addView(chip);
        }
    }
    private void showHarvesters() {
        binding.includeTractors.tvLabel.setText(getString(R.string.used_harvester));
    }
    private void showImplements() {
        binding.includeTractors.tvLabel.setText(getString(R.string.used_imaplements));
        binding.includeTractors.tvViewMore.setOnClickListener(v -> navigateToSearchImplement(false));
        binding.includeTractors.rvList.setAdapter(impsAdapter);
    }
    private void showOldTractors() {
        binding.includeTractors.tvLabel.setText(getString(R.string.used_tractors));
        binding.includeTractors.tvViewMore.setOnClickListener(v -> navigateToUsedTractorList(null));
        if (usedTractorListAdapter == null) {
            initUsedTractorAdapter();
        }
        binding.includeTractors.rvList.setAdapter(usedTractorListAdapter);
    }
    private void initCompare() {
        List<String> optionList = new ArrayList<>();
        optionList.add("Tractors");
        optionList.add("Implements");
        for (int i = 0; i < optionList.size(); i++) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.suggestion_chip, binding.includeTractors.chipGroup, false);
            chip.setText(optionList.get(i));
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    Object tag = buttonView.getTag();
                    if ("Tractors".equals(tag)) {
                        showTractorCompare();
                    } else if ("Implements".equals(tag)) {
                        showImplementsCompare();
                    }
                }
            });
            chip.setTag(optionList.get(i));
            if (i == 0)
                chip.setChecked(true);
            binding.includeCompare.chipGroup.addView(chip);
        }
    }
    private void showTractorCompare() {
        binding.includeCompare.tvLabel.setText(getString(R.string.compareTractors));
        binding.includeCompare.tvViewMore.setOnClickListener(v -> ((MainActivity) activity).navigateToCompareFragment(CallFrom.NEW_TRACTOR));
        if (compareTractorAdapter == null) {
            initCompareTractorAdapter();
        }
        binding.includeCompare.rvList.setLayoutManager(new LinearLayoutManager(activity));
        binding.includeCompare.rvList.setAdapter(compareTractorAdapter);
    }
    private void showImplementsCompare() {
        binding.includeCompare.tvLabel.setText(getString(R.string.compareImplement));
        binding.includeCompare.tvViewMore.setOnClickListener(v -> ((MainActivity) activity).navigateToCompareFragment(CallFrom.IMPLEMENT));
        if (compareImplementAdapter == null) {
            initCompareImplementAdapter();
        }
        binding.includeCompare.rvList.setLayoutManager(new LinearLayoutManager(activity));
        binding.includeCompare.rvList.setAdapter(compareImplementAdapter);
        if (compareImplementAdapter != null && compareImplementAdapter.getItemCount() <= 1) {
            viewModel.getCompareImplements(1).observe(getViewLifecycleOwner(), tractorsResp -> {
                switch (tractorsResp.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (tractorsResp.getData() != null) {
                            List<PopularImpComp> list = tractorsResp.getData().getData().getImplement().subList(0, 2);
                            if (compareImplementAdapter == null) {
                                initCompareTractorAdapter();
                            }
                            compareImplementAdapter.setOthers(tractorsResp.getData().getOthers());
                            compareImplementAdapter.updateItems(list);
                            binding.includeCompare.container.setVisibility(View.VISIBLE);
                        }
                        break;
                    case ERROR:
                        Toast.makeText(activity, tractorsResp.getError(), Toast.LENGTH_SHORT).show();
                        break;
                }
            });
        }
    }
    private void initCompareImplementAdapter() {
        compareImplementAdapter = new CompareImplementTractorAdapter(activity,binding.includeCompare.rvList);
        compareImplementAdapter.setOnItemClickListener((position,view,popularImpComp) -> {
            try {
                CompareImp fragment = new CompareImp();
                Bundle bundle = new Bundle();
                JSONObject tr1JsonObj = new JSONObject();
                JSONObject tr2JsonObj = new JSONObject();
                tr1JsonObj.put("brand_id", popularImpComp.getFirstImplement().getImplBrandId());
                tr1JsonObj.put("Type_id", popularImpComp.getFirstImplement().getImplTypeId());
                tr1JsonObj.put("model_id", popularImpComp.getFirstImplement().getImplModelId());
                tr2JsonObj.put("brand_id", popularImpComp.getSecondImplement().getImplBrandId());
                tr2JsonObj.put("Type_id", popularImpComp.getSecondImplement().getImplTypeId());
                tr2JsonObj.put("model_id", popularImpComp.getSecondImplement().getImplModelId());
                JSONObject jsonObjectTractorComp = new JSONObject();
                jsonObjectTractorComp.put("first_implement", tr1JsonObj);
                jsonObjectTractorComp.put("secound_implement", tr2JsonObj);
                JSONObject postJsonObject = new JSONObject();
                postJsonObject.put("implements", jsonObjectTractorComp);
                bundle.putString("value", postJsonObject.toString());
                fragment.setArguments(bundle);
                FragmentManager mFragmentManager = activity.getSupportFragmentManager();
                if (!fragment.isAdded()) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
                    fragmentTransaction.replace(contentView.getId(), fragment).addToBackStack(TAG).commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(((MainActivity) activity)).get(HomeFragmentViewModel.class);
        if(pref.getIntValue(Constants.NO_OF_LAUNCHED,0) > 7) {
//            ReviewManager manager = ReviewManagerFactory.create(activity);
//            Task<ReviewInfo> request = manager.requestReviewFlow();
//            request.addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    ReviewInfo reviewInfo = task.getResult();
//                    Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
//                    flow.addOnCompleteListener(task1 -> {
//                    });
//                }
//            });
            pref.setValue(Constants.NO_OF_LAUNCHED,0);
        }
        viewModel.getCompareTractors(1).observe(getViewLifecycleOwner(),tractorsResp -> {
            switch(tractorsResp.getStatus()) {
                case LOADING:
                    binding.includeCompare.container.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    if (tractorsResp.getData() != null) {
                        List<Tractors> list = tractorsResp.getData().getData().getTractors().subList(0, 2);
                        if (compareTractorAdapter == null) {
                            initCompareTractorAdapter();
                        }
                        compareTractorAdapter.setOthers(tractorsResp.getData().getOthers());
                        compareTractorAdapter.updateItems(list);
                        binding.includeCompare.container.setVisibility(View.VISIBLE);
                    }
                    break;
                case ERROR:
                    Toast.makeText(activity,tractorsResp.getError(),Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        viewModel.getUsedTractors(1).observe(getViewLifecycleOwner(),tractorsResp -> {
            switch (tractorsResp.getStatus()) {
                case LOADING:
                    binding.includeTractors.container.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    if (tractorsResp.getData() != null) {
                        if (usedTractorListAdapter == null) {
                            initUsedTractorAdapter();
                        }
                        usedTractorListAdapter.setOthers(tractorsResp.getData().getOthers());
                        if (tractorsResp.getData().getData().getData().size() > 5)
                            usedTractorListAdapter.updateItems(tractorsResp.getData().getData().getData().subList(0,4));
                        else
                            usedTractorListAdapter.updateItems(tractorsResp.getData().getData().getData());
                        binding.includeTractors.container.setVisibility(View.VISIBLE);
                    }
                    break;
                case ERROR:
                    Toast.makeText(activity, tractorsResp.getError(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        viewModel.getVideos(getString(R.string.youtubeChannelID), getString(R.string.google_api_key),null);
        viewModel.videoList.observe(getViewLifecycleOwner(),items -> {
            if (items != null) {
                if(items.size()>6)
                   videoAdapter.addItems(items.subList(0, 5),1);
                else if(items.size()<=6 && items.size()>=1)
                   videoAdapter.addItems(items.subList(0, items.size()-1),1);

                binding.includeVideos.container.setVisibility(View.VISIBLE);
            } else {
                binding.includeVideos.container.setVisibility(View.GONE);
            }
        });
        initStates();
        initHarvester();
        initSmallFarmMachinery();
    }
    private void initSmallFarmMachinery() {
        binding.includeSmallFarmMachinery.tvLabel.setText(getString(R.string.small_farm_machinery));
        binding.includeSmallFarmMachinery.tvViewMore.setVisibility(View.GONE);
        if (categoriesAdapter == null) {
            initSmallFarmAdapter();
        }
        binding.includeSmallFarmMachinery.rvList.setAdapter(categoriesAdapter);
        if (categoriesAdapter != null && categoriesAdapter.getItemCount() <= 1) {
            categoryRepository.getHomeCategories().observe(getViewLifecycleOwner(), listResource -> {
                if(listResource.size()>0) {
                      //  binding.includeSmallFarmMachinery.container.setVisibility(View.GONE);
                      //  break;
                        binding.includeSmallFarmMachinery.container.setVisibility(View.VISIBLE);
                        categoriesAdapter.addItems(listResource,1,sessionManager.getEcomOther());
                }
            });
        }
    }
    private void initSmallFarmAdapter() {
        categoriesAdapter = new CategoriesAdapter(activity,binding.includeTractors.rvList,true);
        categoriesAdapter.setListener((View v,int position,Category category) -> {
            Intent intent = new Intent(activity, ProductListActivity.class);
            com.khetigaadi.shop.model.Filter innerFilter = new com.khetigaadi.shop.model.Filter();
            innerFilter.setCategories(Collections.singletonList(category));
            intent.putExtra(ProductListFilterActivity.FILTER,innerFilter);
            startActivity(intent);
        });
    }
    private void initCompareTractorAdapter() {
        compareTractorAdapter = new ComparePopularTractorAdapter(activity,binding.includeCompare.rvList);
        compareTractorAdapter.setOnItemClickListener((position,v,tractors) -> {
            try {
                Tractor tempTractor1 = new Tractor();
                tempTractor1.setId(tractors.getFirstTractor().getId());
                Tractor tempTractor2 = new Tractor();
                tempTractor2.setId(tractors.getSecondTractor().getId());
                CompareTractorParam tractorParam = new CompareTractorParam();
                tractorParam.setTractors(Arrays.asList(tempTractor1,tempTractor2));
                Intent intent = new Intent(activity,CompareTractorActivity.class);
                intent.putExtra("tractorParam",tractorParam);
                startActivity(intent);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void initUsedTractorAdapter() {
        usedTractorListAdapter = new UsedTractorHomeListAdapter(activity, user.getId(), binding.includeTractors.rvList, true);
        usedTractorListAdapter.setOnItemClickListener((position, v) -> {
            try {
                Intent intent = new Intent(activity, UsedTractorDetailActivity.class);
                intent.putExtra("tractorId", usedTractorListAdapter.getItem(position).getId());
                intent.putExtra("position", position);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        usedTractorListAdapter.setOnSellerDetailsClickListener((position, v) -> {
            try {
                Intent intent = new Intent(activity, GetUsedTrSellerDetails.class);
                intent.putExtra("tractorId", usedTractorListAdapter.getItem(position).getId());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void initBrands() {
        binding.includeBrands.tvLabel.setText(getString(R.string.brands));
        List<String> optionList = new ArrayList<>();
        optionList.add("Tractors");
        optionList.add("Implements");
        for (int i = 0; i < optionList.size(); i++) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.suggestion_chip,binding.includeBrands.chipGroup,false);
            chip.setText(optionList.get(i));
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    Object tag = buttonView.getTag();
                    if ("Tractors".equals(tag)) {
                        showTractorOrImpBrands(BrandType.NEW_TRACTOR);
                    } else if ("Implements".equals(tag)) {
                        showTractorOrImpBrands(BrandType.IMPLEMENT);
                    }
                }
            });
            chip.setTag(optionList.get(i));
            if (i == 0)
                chip.setChecked(true);
            binding.includeBrands.chipGroup.addView(chip);
        }
            /*binding.includeBrands.tvLabel.setOnClickListener(v -> {
                if (binding.includeBrands.tabs.getSelectedTabPosition() == 0) {
                    ((MainActivity) activity).navigateToNewFragment();
                } else if (binding.includeBrands.tabs.getSelectedTabPosition() == 1) {
                    ((MainActivity) activity).navigateToImplementFragment();
                }
            });
            binding.includeBrands.tvSeeMore.setOnClickListener(v -> binding.includeBrands.tvLabel.callOnClick());
            binding.includeBrands.tvLabel.setText(getString(R.string.brand));
            List<String> items = new ArrayList<>();
            items.add(getString(R.string.tractor));
            items.add(getString(R.string.implement));
            binding.includeBrands.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            binding.includeBrands.viewpager2.setCurrentItem(0);
//                        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.new_tractor));
                            break;
                        case 1:
//                        binding.includeBrands.viewpager2.setCurrentItem(1);
                            // ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.new_implement));
                            break;

                        default:
                            binding.includeBrands.viewpager2.setCurrentItem(tab.getPosition());
                            // ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.new_tractor));
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            binding.includeBrands.viewpager2.setAdapter(new HomeBrandsTabAdapter(this, items));
            new TabLayoutMediator(binding.includeBrands.tabs, binding.includeBrands.viewpager2, (tab, position) -> tab.setText(items.get(position))).attach();
            brandRepository.getNewTractorBrands().observe(getViewLifecycleOwner(), listResource -> {
                if (listResource.getStatus() == Status.SUCCESS) {
                    if (listResource.getData() != null && listResource.getData().size() > 0)
                        binding.includeBrands.container.setVisibility(View.VISIBLE);
                    else
                        binding.includeBrands.container.setVisibility(View.GONE);
                }
            });*/
    }
    private void getPopularTractor() {
        disposable.add(khetiAPIs.getHomeList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<HomeListResp>() {
                    @Override
                    public void onSuccess(@NonNull HomeListResp homeListResp) {
                        SharedPreferenceUtils.getInstance(activity).setValue(Constants.POPULAR_TRACTORS, new Gson().toJson(homeListResp));
                        setPopularTractorAndNewsAdapter(homeListResp);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.tag(TAG).e(e, "onError: ");
                    }
                }));
    }
    private void setPopularTractorAndNewsAdapter(HomeListResp homeList) {
        try {
            if (homeList.getHomeData().getTractors() != null) {
                binding.includePopularTractors.container.setVisibility(View.VISIBLE);
                binding.includePopularTractors.tvLabel.setText(getString(R.string.popular_tractor));
                binding.includePopularTractors.tvViewMore.setOnClickListener(v -> {
                    callNewTractorListPage(null);
                });
                PopularTractorAdapter popularTractorAdapter = new PopularTractorAdapter(activity,binding.includePopularTractors.rvList);
                binding.includePopularTractors.rvList.setLayoutManager(new GridLayoutManager(activity,2));
                binding.includePopularTractors.rvList.setAdapter(popularTractorAdapter);
                popularTractorAdapter.setOthers(homeList.getOthers());
                if (homeList.getHomeData().getTractors().size() > 4)
                    popularTractorAdapter.updateItems(homeList.getHomeData().getTractors().subList(0, 4));
                else
                    popularTractorAdapter.updateItems(homeList.getHomeData().getTractors());
                popularTractorAdapter.setOnItemClickListener(new PopularTractorAdapter.Listener() {
                    @Override
                    public void onItemClick(int position, @org.jetbrains.annotations.Nullable View v) {
                        try {
                            Intent intent = new Intent(activity, NewTractorDetails.class);
                            intent.putExtra(NewTractorDetails.newTractorId, homeList.getHomeData().getTractors().get(position).getId());
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onGetRoadPriceClick(int position,@Nullable View v) {
                        Intent intent = new Intent(activity,NewTrGetOnRoadPriceActivity.class);
                        intent.putExtra("id",popularTractorAdapter.getItem(position).getId());
                        intent.putExtra("name",popularTractorAdapter.getItem(position).getName());
                        //intent.putExtra("image", popularTractorAdapter.getOthers().getNewTractorImage() + popularTractorAdapter.getItem(position).getImage().getTractor_img_name());
                        intent.putExtra("image",popularTractorAdapter.getOthers().getNewTractorImage() + popularTractorAdapter.getItem(position).getImage());
                        intent.putExtra("priceType",PriceType.TRACTOR);
                        startActivity(intent);
                    }
                });
            }
            else {
                binding.includePopularTractors.container.setVisibility(View.GONE);
            }
            if (homeList.getHomeData().getImplement() != null) {
                impsAdapter = new ImpsAdapter(activity, binding.includeTractors.rvList, true);
                impsAdapter.setOthers(homeList.getOthers());
                if (homeList.getHomeData().getImplement().size() > 4)
                    impsAdapter.updateItems(homeList.getHomeData().getImplement().subList(0, 4));
                else
                    impsAdapter.updateItems(homeList.getHomeData().getImplement());
                impsAdapter.setOnItemClickListener(new ImpsAdapter.Listener() {
                    @Override
                    public void onItemClick(int position,View v) {
                        try {
                            Intent i = new Intent(activity, NewImpOverviewDetails.class);
                            i.putExtra("brandId", homeList.getHomeData().getImplement().get(position).getBrandId());
                            i.putExtra("typeId", homeList.getHomeData().getImplement().get(position).getImplementTypeId());
                            i.putExtra("modelId", homeList.getHomeData().getImplement().get(position).getImplementModelId());
                            startActivity(i);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onGetPriceClick(int position,View v) {
                        Intent intent = new Intent(activity, NewTrGetOnRoadPriceActivity.class);
                        intent.putExtra("id", impsAdapter.getItem(position).getId());
                        intent.putExtra("name", impsAdapter.getItem(position).getImplementName());
                        intent.putExtra("image", impsAdapter.getOthers().getImplementImagePath()+impsAdapter.getItem(position).getImplementImage().getImage());
                        intent.putExtra("priceType", PriceType.IMPLEMENT);
                        intent.putExtra("toolbarlabel", "Get Price");
                        startActivity(intent);
                    }
                });
            }
            if (homeList.getHomeData().getNews() != null) {
                if (newsAdapter == null)
                    initNews();
                newsAdapter.addItems(homeList.getHomeData().getNews().stream().filter(news -> news.getImage() != null && news.getImage().getImage() != null).collect(Collectors.toList()).subList(0, 4), 1);
                binding.includeNews.container.setVisibility(View.VISIBLE);
            }
            if (homeList.getHomeData().getTestimonials() != null) {
                if (testimonialsAdapter == null)
                    initTestimonials();
                if (testimonialsAdapter.getList() != null && testimonialsAdapter.getList().size() < 1)
                    testimonialsAdapter.addItems(homeList.getHomeData().getTestimonials(), 1, homeList.getOthers());
                binding.includeTestimonials.container.setVisibility(View.VISIBLE);
            } else {
                binding.includeTestimonials.container.setVisibility(View.GONE);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void initTestimonials() {
        binding.includeTestimonials.container.setVisibility(View.GONE);
        binding.includeTestimonials.tvLabel.setText(getString(R.string.farmers_testimonial));
        testimonialsAdapter = new TestimonialListAdapter(activity, binding.includeTestimonials.rvList);
        binding.includeTestimonials.rvList.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.includeTestimonials.rvList.setAdapter(testimonialsAdapter);
        binding.includeTestimonials.tvViewMore.setVisibility(View.GONE);
        binding.includeTestimonials.rvList.addItemDecoration(new CirclePagerIndicatorDecoration());
    }
    public void runnable(final int size) {
        animateViewPager = () -> {
            if (!stopSliding) {
                if (binding.viewPager.getCurrentItem() == size - 1) {
                    binding.viewPager.setCurrentItem(0);
                } else {
                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1,true);
                }
                handler.postDelayed(animateViewPager,Constants.ANIM_VIEWPAGER_DELAY);
            }
        };
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) activity).hideSoftKeyboard();
        if (bannerList == null)
            bannerList = ((MainActivity) activity).allBanner.getBanners().getHome();
        if (bannerList != null && bannerList.size() != 0) {
            configAdapter();
        }
        if (otherBannerList == null)
            otherBannerList = ((MainActivity) activity).allBanner.getBanners().getHomeOther();
        if (otherBannerList != null && otherBannerList.size() != 0) {
            initOtherBanners();
        }

        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        addFabButtonItems();
        /*Given code commented by pranav*/
        if (/*user.getCrops() == null || user.getCrops().size() == 0 ||*/ user.getUserDetail()/*.getBiography()*/ != null /*|| user.getUserDetail().getBiography().equals("")*/) {
            binding.tvProfileUpdate.setVisibility(View.GONE);
        } else {
            binding.tvProfileUpdate.setVisibility(View.VISIBLE);
        }
    }
    private void initOtherBanners() {
        if (otherBannerList != null) {
            if (otherBannerList.size() >= 1) {
                binding.mcvBanner1.setVisibility(View.VISIBLE);
                Glide.with(activity)
                        .load(otherBannerList.get(0).getImageUrl())
                        .into(binding.ivBanner1);

            } else
                binding.mcvBanner1.setVisibility(View.GONE);
            if (otherBannerList.size() >= 2) {
                binding.mcvBanner2.setVisibility(View.VISIBLE);
                Glide.with(activity)
                        .load(otherBannerList.get(1).getImageUrl())
                        .into(binding.ivBanner2);

            } else
                binding.mcvBanner2.setVisibility(View.GONE);
            if (otherBannerList.size() >= 2) {
                binding.mcvBanner3.setVisibility(View.VISIBLE);
                Glide.with(activity)
                        .load(otherBannerList.get(2).getImageUrl())
                        .into(binding.ivBanner3);

            } else
                binding.mcvBanner3.setVisibility(View.GONE);
        }
    }
    private void configAdapter() {
        binding.viewPager.setAdapter(new TwoBannerSlideAdapter(activity,bannerList));
        binding.viewPager.setClipToPadding(false);
       // binding.viewPager.setPadding(0,0,120,0);
        if(binding.viewPager.getCurrentItem()==bannerList.size()-1)
            binding.viewPager.setPadding(0,0,0,0);
      //  else
           // binding.viewPager.setPadding(0,0,120,0);
        binding.indicator.setViewPager(binding.viewPager);
        runnable(bannerList.size());
        //Re-run callback
        handler.postDelayed(animateViewPager,Constants.ANIM_VIEWPAGER_DELAY);
    }
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }
    private void showTractorOrImpBrands(@BrandType int type) {
        BrandAdapter adapter = new BrandAdapter(this.requireContext(),binding.includeBrands.rvList);
        binding.includeBrands.rvList.setLayoutManager(new GridLayoutManager(this.getContext(),4));
        binding.includeBrands.rvList.setAdapter(adapter);
        switch (type) {
            case BrandType.NEW_TRACTOR: {
                adapter.setOnItemClickListener((position,v) -> callNewTractorListPage(adapter.getItem(position)));
                brandRepository.getNewTractorBrands().observe(getViewLifecycleOwner(),brands -> {
                    if (brands.size()>0) {
                        if (brands != null && brands.size() != 0)
                            adapter.updateItems(brands.subList(0, 16));
                    }
                });
                binding.includeBrands.tvViewMore.setOnClickListener(v ->navigateToSearchNewTractor(true));
                break;
            }
            case BrandType.IMPLEMENT: {
                adapter.setOnItemClickListener((position,v) ->callImplementList(adapter.getItem(position)));
                brandRepository.getImplementBrands().observe(getViewLifecycleOwner(),brands -> {
                    if (brands.size()>0) {
                        if (Objects.requireNonNull(brands).size() != 0)
                            adapter.updateItems(brands.subList(0,16));
                    }
                });
                binding.includeBrands.tvViewMore.setOnClickListener(v -> navigateToSearchImplement(true));
                break;
            }
            case BrandType.OLD_TRACTOR:
                break;
        }
    }
    private void callNewTractorListPage(Brand currentTractor) {
        Filter newTractorListParam = new Filter();
        if (currentTractor != null)
            newTractorListParam.setBrands(Collections.singletonList(currentTractor));
        FragmentManager mFragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
        fragmentTransaction.replace(contentView.getId(), NewTractorsListFragment.getInstance(newTractorListParam)).addToBackStack(TAG).commit();
    }
    private void callImplementList(Brand brand) {
        try {
            Filter filter = new Filter();
            filter.setBrands(Collections.singletonList(brand));
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            fragmentTransaction.replace(contentView.getId(), NewImplementsList.getInstance(filter)).addToBackStack(TAG).commit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
