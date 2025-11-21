package com.khetigaadi.tractor.newTractor;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.tabs.TabLayoutMediator;
import com.khetigaadi.R;
import com.khetigaadi.base.util.StateData;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.databinding.NewTractorDetailsBinding;
import com.khetigaadi.def.PriceType;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class NewTractorDetails extends BaseActivity {
    public static final String newTractorId = "tractorId";
    private NewTractorDetailsBinding binding;
    private NewTractorViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = NewTractorDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);

        viewModel = new ViewModelProvider(this).get(NewTractorViewModel.class);
        readDeepLinkData();
        toolbarInit();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new NewTractorDetailsOverview());
        fragmentList.add(new NewTractorDetailsSpecFragment());
        fragmentList.add(new NewTractorDetailsApplicationsList());
        fragmentList.add(new NewTractorDetailsPicture());
        fragmentList.add(getDealerDetails());
        NewTractorDetailsAdapter imagesAdapter = new NewTractorDetailsAdapter(this, fragmentList);
        binding.viewPager2.setAdapter(imagesAdapter);
        List<String> items = new ArrayList<>();
        items.add(getString(R.string.overview));
        items.add(getString(R.string.specification));
        items.add(getString(R.string.applications));
        items.add(getString(R.string.picture));
        items.add(getString(R.string.dealers));
        new TabLayoutMediator(binding.tabs, binding.viewPager2,
                (tab, position) -> tab.setText(items.get(position))
        ).attach();
        viewModel.getTractors().observe(this, newTractorOverviewRespStateData -> {
            if (newTractorOverviewRespStateData.getStatus() == StateData.Status.SUCCESS) {
                if (newTractorOverviewRespStateData.getData() != null) {
                    NewTractor tractor = newTractorOverviewRespStateData.getData().getData().get(0);
                    if (tractor != null) {
                        binding.btnRequestForDemo.setOnClickListener(view1 -> {
                            Intent intent = new Intent(this, NewTractorRequestForDemo.class);
                            intent.putExtra("id", tractor.getId());
                            intent.putExtra("name", tractor.getName());
                            intent.putExtra("image", tractor.getImage()!=null?newTractorOverviewRespStateData.getData().getOthers().getTractorImagePath() + tractor.getImage().getTractor_img_name():"");
                            intent.putExtra("demoType", PriceType.TRACTOR);
                            startActivity(intent);
                        });
                        binding.btnOnRoadPrice.setOnClickListener(view1 -> {
                            Intent intent = new Intent(this, NewTrGetOnRoadPriceActivity.class);
                            intent.putExtra("id", tractor.getId());
                            intent.putExtra("name", tractor.getName());
                            intent.putExtra("image", newTractorOverviewRespStateData.getData().getOthers().getTractorImagePath() + tractor.getImage().getTractor_img_name());
                            intent.putExtra("priceType", PriceType.TRACTOR);
                            startActivity(intent);
                        });
                    }
                }
            }
        });
    }
    public NewTractorDetailsBinding getBinding() {
        return binding;
    }
    private void toolbarInit() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.new_tractor));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.new_tractor));
        viewModel.getTractors().observe(this,newTractorOverviewRespStateData -> {
            if (newTractorOverviewRespStateData != null)
                if (newTractorOverviewRespStateData.getStatus() == StateData.Status.SUCCESS)
                    if (newTractorOverviewRespStateData.getData() != null) {
                        binding.toolbars.toolbar.setTitle(newTractorOverviewRespStateData.getData().getData().get(0).getName());
                    }

        });
    }
    private void readDeepLinkData() {
        if(getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_VIEW))
        {
            try {
                Uri uri = getIntent().getData();
                String[] segments = uri.getPath().split("/");
                viewModel.setTractorId(segments[segments.length - 2]);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        if(viewModel.getTractor() != null)
            returnIntent.putExtra("isFavorites",viewModel.getTractor().isBookmark());
        returnIntent.putExtra("position",viewModel.getPosition());
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }
    private NewTractorDetailsDealerList getDealerDetails() {
        return new NewTractorDetailsDealerList();
    }
}
