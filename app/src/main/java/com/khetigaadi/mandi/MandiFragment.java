package com.khetigaadi.mandi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.khetigaadi.base.model.State;
import com.khetigaadi.commons.activities.StateSelectionActivity;
import com.khetigaadi.databinding.RecycleListBinding;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MandiFragment extends Fragment {

    private MandiViewModel viewModel;
    private RecycleListBinding binding;
    private FragmentActivity activity;
    private MandiPriceListAdapter adapter;

    public static MandiFragment newInstance() {
        return new MandiFragment();
    }

    ActivityResultLauncher<Intent> stateSectionResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        State selectedState = data.getParcelableExtra(StateSelectionActivity.SELECTED_STATE);
                        if (selectedState != null) {
                            viewModel.setState(selectedState);
                            getList(0);
                        }
                    }
                }
            });

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecycleListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MandiViewModel.class);
        adapter = new MandiPriceListAdapter(getActivity(), binding.myRecyclerView);
        binding.myRecyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this::getList);
        getList(0);
        binding.addIcon.setVisibility(View.GONE);
        binding.filterIcon.setOnClickListener(v -> stateSectionResultLauncher.launch(new Intent(activity, StateSelectionActivity.class)));
    }

    private void getList(int page) {
        viewModel.getMandiPrice(page).observe(getViewLifecycleOwner(), dealerListRespStateData -> {
            switch (dealerListRespStateData.getStatus()) {
                case LOADING:
                    if (page == 0)
                        binding.llProgress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.llProgress.setVisibility(View.GONE);
                    adapter.setTotPages(dealerListRespStateData.getData() != null ? dealerListRespStateData.getData().getTotal() / 10 : 0);
                    if (page == 0) {
                        adapter.getList().clear();
                        adapter.notifyDataSetChanged();
                    }
                    adapter.addItems(dealerListRespStateData.getData().getRecords(), page);
                    break;
                case ERROR:
                    Toast.makeText(activity, dealerListRespStateData.getError(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}