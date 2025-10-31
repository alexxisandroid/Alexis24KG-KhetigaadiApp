package com.khetigaadi.commons.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import com.khetigaadi.R;
import com.khetigaadi.agri.activity.BrandSelectionActivity;
//import com.khetigaadi.base.model.Brand;

import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.base.room.repository.utils.Status;
import com.khetigaadi.commons.adapter.BrandAdapter;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.implement.newImplement.NewImplementsList;
import com.khetigaadi.room.repository.BrandRepository;
import com.khetigaadi.tractor.newTractor.NewTractorsListFragment;
import com.khetigaadi.tractor.newTractor.model.Filter;
import java.util.Collections;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class HomeTractorBrandsFragment extends BaseFragment {
    private static final String TAG = "HomeTractorBrandsFragment";
    private RecycleListBinding binding;
    @Inject
    BrandRepository brandRepository;
    private FragmentActivity mActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = RecycleListBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.filterIcon.setVisibility(View.GONE);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            initList(getArguments().getInt(BrandSelectionActivity.BRAND_TYPE, BrandType.NEW_TRACTOR));
        }
    }
    private void initList(@BrandType int type) {
        BrandAdapter adapter = new BrandAdapter(this.requireContext(), binding.myRecyclerView);
        binding.myRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 4));
        binding.myRecyclerView.setAdapter(adapter);
        switch (type) {
            case BrandType.NEW_TRACTOR: {
                adapter.setOnItemClickListener((position, v) -> callNewTractorListPage(adapter.getItem(position)));
                brandRepository.getNewTractorBrands().observe(getViewLifecycleOwner(),brands -> {
                    if (brands.size()>0) {
                        if (brands!= null && brands.size() != 0)
                            adapter.updateItems(brands.subList(0, 16));
                    }
                });
                break;
            }
            case BrandType.IMPLEMENT: {
                adapter.setOnItemClickListener((position, v) -> callImplementList(adapter.getItem(position)));
                brandRepository.getImplementBrands().observe(getViewLifecycleOwner(), brands -> {
                    if (brands.size()>0) {
                        if (Objects.requireNonNull(brands).size() != 0)
                            adapter.updateItems(brands.subList(0, 16));
                    }
                });
                break;
            }
            case BrandType.OLD_TRACTOR:
                break;
        }
    }
    private void callImplementList(Brand brand) {
        try {
            Filter filter = new Filter();
            filter.setBrands(Collections.singletonList(brand));
            FragmentManager mFragmentManager = mActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            FrameLayout contentView = (FrameLayout) mActivity.findViewById(R.id.containerView);
            fragmentTransaction.replace(contentView.getId(), NewImplementsList.getInstance(filter)).addToBackStack(TAG).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void callNewTractorListPage(Brand currentTractor) {
        Filter newTractorListParam = new Filter();
        newTractorListParam.setBrands(Collections.singletonList(currentTractor));
        FragmentManager mFragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        FrameLayout contentView = (FrameLayout) mActivity.findViewById(R.id.containerView);
        fragmentTransaction.replace(contentView.getId(), NewTractorsListFragment.getInstance(newTractorListParam)).addToBackStack(TAG).commit();
    }
    public static HomeTractorBrandsFragment newInstance(@BrandType int type) {
        HomeTractorBrandsFragment fragment = new HomeTractorBrandsFragment();
        Bundle args = new Bundle();
        args.putInt(BrandSelectionActivity.BRAND_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }
}