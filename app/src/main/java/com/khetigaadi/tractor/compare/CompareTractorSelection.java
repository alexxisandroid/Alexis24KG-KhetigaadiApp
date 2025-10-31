package com.khetigaadi.tractor.compare;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.R;
//import com.khetigaadi.base.model.Brand;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.base.model.Model;
import com.khetigaadi.databinding.CompareTrSelectionBinding;
import com.khetigaadi.model.LinearLayoutManagerWrapper;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.compare.model.CompareTractorParam;
import com.khetigaadi.tractor.compare.model.CompareTractorsListResp;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
import com.khetigaadi.tractor.newTractor.model.NewTractorResp;
import com.khetigaadi.tractor.newTractor.model.Tractor;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class CompareTractorSelection extends Fragment {
    private static final String TAG = CompareTractorSelection.class.getSimpleName();
    private String value;
    private JSONObject json;
    private FragmentActivity activity;
    //private LinearLayout llPopularTractorList;
    @Inject
    Tracker mTracker;
    @Inject
    KhetiAPIs khetiAPIs;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private ComparePopularTractorAdapter popularTractorAdapter;
    private NewTractor tractor1;
    private NewTractor tractor2;
    private CompareTrSelectionBinding binding;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            value = bundle.getString("value");
        }
        try {
            if (value == null) {
                json = new JSONObject();
            } else {
                json = new JSONObject(value);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("requestJson=" + value);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        Timber.i("Setting screen name: %s", TAG);
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if (getArguments() != null) {
            if (getArguments().getBoolean("isCallFirstTr")) {
                getArguments().putBoolean("isCallFirstTr", false);
                binding.cvTractor1.callOnClick();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CompareTrSelectionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.rvPopularTractors.setNestedScrollingEnabled(false);
        binding.rvPopularTractors.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWrapper(activity, LinearLayoutManager.VERTICAL, false);
        binding.rvPopularTractors.setLayoutManager(mLayoutManager);

        if (value == null) {
            binding.llTractor1.setVisibility(View.GONE);
            binding.llTractor2.setVisibility(View.GONE);
            binding.llTractor1Empty.setVisibility(View.VISIBLE);
            binding.llTractor2Empty.setVisibility(View.VISIBLE);
        } else {
            try {
                if (json.has("tractor1")) {
                    JSONObject tractor1Json = json.getJSONObject("tractor1");
                    if (tractor1Json.has("brandId") && !tractor1Json.isNull("brandId")) {
                        getImageAndHp(tractor1Json.getString("brandId"), tractor1Json.getString("modelId"), 1);
                    }
                    if (tractor1Json.has("brandName") && tractor1Json.has("modelName")) {
                        binding.tvTractor1Name.setText(String.format("%s %s", tractor1Json.getString("brandName"), tractor1Json.getString("modelName")));
                    }
                    binding.llTractor1.setVisibility(View.VISIBLE);
                    binding.llTractor1Empty.setVisibility(View.GONE);
                } else {
                    binding.llTractor1.setVisibility(View.GONE);
                    binding.llTractor1Empty.setVisibility(View.VISIBLE);
                }
                if (json.has("tractor2")) {
                    JSONObject tractor1Json = json.getJSONObject("tractor2");
                    if (tractor1Json.has("brandId") && !tractor1Json.isNull("brandId")) {
                        getImageAndHp(tractor1Json.getString("brandId"), tractor1Json.getString("modelId"), 2);
                    }
                    if (tractor1Json.has("brandName") && tractor1Json.has("modelName")) {
                        binding.tvTractor2Name.setText(String.format("%s %s", tractor1Json.getString("brandName"), tractor1Json.getString("modelName")));
                    }
                    binding.llTractor2.setVisibility(View.VISIBLE);
                    binding.llTractor2Empty.setVisibility(View.GONE);
                } else {
                    binding.llTractor2.setVisibility(View.GONE);
                    binding.llTractor2Empty.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        popularTractorAdapter = new ComparePopularTractorAdapter(activity, binding.rvPopularTractors);
        binding.rvPopularTractors.setAdapter(popularTractorAdapter);
        popularTractorAdapter.setOnItemClickListener((position, v, get) -> {
            try {
                CompareTractorParam tractorParam = new CompareTractorParam();
                List<Tractor> list = new ArrayList<>();
                Tractor tractor = new Tractor();
                tractor.setId(""+popularTractorAdapter.getItem(position).getFirstTractor().getId());
                Tractor tractor1 = new Tractor();
                tractor1.setId(""+popularTractorAdapter.getItem(position).getSecondTractor().getId());
                list.add(tractor);
                list.add(tractor1);
                tractorParam.setTractors(list);
                startCompareActivity(tractorParam);
            } catch (Exception e) {
                e.printStackTrace();
                binding.llProgressComTractors.setVisibility(View.GONE);
            }
        });
        binding.cvTractor1.setOnClickListener(v -> {

            try {
                json.put("tractor1", new JSONObject());
                json.put("selected", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GetBrandName fragment = new GetBrandName();
            Bundle bundle = new Bundle();
            bundle.putString("value", json.toString());
            fragment.setArguments(bundle);
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.enter);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            fragmentTransaction.replace(contentView.getId(), fragment).addToBackStack(TAG).commit();

        });
        binding.cvTractor2.setOnClickListener(v -> {
            try {
                json.put("tractor2", new JSONObject());
                json.put("selected", "2");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GetBrandName fragment = new GetBrandName();
            Bundle bundle = new Bundle();
            bundle.putString("value", json.toString());
            fragment.setArguments(bundle);
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.enter);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            mFragmentManager.popBackStack("GetModelName", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.replace(contentView.getId(), fragment).addToBackStack(TAG).commit();

        });

        binding.btnCompare.setOnRippleCompleteListener(rippleView -> {
            if (tractor1 != null && tractor2 != null) {
                Tractor tempTractor1 = new Tractor();
                tempTractor1.setId(tractor1.getId());
                Tractor tempTractor2 = new Tractor();
                tempTractor2.setId(tractor2.getId());
                CompareTractorParam tractorParam = new CompareTractorParam();
                tractorParam.setTractors(Arrays.asList(tempTractor1, tempTractor2));
                startCompareActivity(tractorParam);

            } else {
                Toast.makeText(activity, getResources().getString(R.string.please_select_tractors_for_compare), Toast.LENGTH_LONG).show();
            }
        });
        getPopularTractorComparison();
        return view;
    }

    private void getPopularTractorComparison() {
        binding.llProgressComTractors.setVisibility(View.VISIBLE);
        disposable.add(khetiAPIs.getCompareTractors(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CompareTractorsListResp>() {
                    @Override
                    public void onSuccess(@NonNull CompareTractorsListResp compareTractorsListResp) {
                        if (!compareTractorsListResp.isError()) {
                            popularTractorAdapter.setOthers(compareTractorsListResp.getOthers());
                            popularTractorAdapter.updateItems(compareTractorsListResp.getData().getTractors());
                            binding.llProgressComTractors.setVisibility(View.GONE);
                            binding.rvPopularTractors.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(activity, compareTractorsListResp.getMsg().get(0), Toast.LENGTH_LONG).show();
                            binding.llProgressComTractors.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                    }
                }));
    }
    private void startCompareActivity(CompareTractorParam tractorParam) {
        Intent intent = new Intent(activity, CompareTractorActivity.class);
        intent.putExtra("tractorParam", tractorParam);
        startActivity(intent);
    }
    private void getImageAndHp(final String brandId, final String modelId, final int i) {
        Brand brand = new Brand();
        brand.setId(Integer.parseInt(brandId));
        Model model = new Model();
        model.setId(Integer.parseInt(modelId));
        Filter filter = new Filter();
        filter.setBrands(Collections.singletonList(brand));
        filter.setModels(Collections.singletonList(model));
        TractorFilter tractorFilter = new TractorFilter();
        tractorFilter.setFilter(filter);
        disposable.add(khetiAPIs.getNewTractors(tractorFilter, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NewTractorResp>() {
                    @Override
                    public void onSuccess(@NonNull NewTractorResp newTractorResp) {
                        try {
                            if (!newTractorResp.isError()) {
                                if (i == 1) {
                                    tractor1 = newTractorResp.getData().getData().get(0);
                                    if (tractor1.getImage() != null) {
                                        Glide.with(activity)
                                                .load(newTractorResp.getOthers().getTractorImagePath() + tractor1.getImage().getTractor_img_name())
                                                .placeholder(R.mipmap.no_image)
                                                .error(R.mipmap.no_image)
                                                .into(binding.image);

                                    }
                                    if (!tractor1.getExactHp().isEmpty()) {
                                        binding.tvHp.setText(tractor1.getExactHp().concat(" " + getResources().getString(R.string.hp)));
                                    }
                                } else {
                                    tractor2 = newTractorResp.getData().getData().get(0);
                                    if (tractor2.getImage() != null) {
                                        Glide.with(activity)
                                                .load(newTractorResp.getOthers().getTractorImagePath() + tractor2.getImage().getTractor_img_name())
                                                .placeholder(R.mipmap.no_image)
                                                .error(R.mipmap.no_image)
                                                .into(binding.image1);

                                    }
                                    if (!tractor2.getExactHp().isEmpty()) {
                                        binding.tvHp1.setText(tractor2.getExactHp().concat(" " + getResources().getString(R.string.hp)));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                    }
                }));
    }
}
