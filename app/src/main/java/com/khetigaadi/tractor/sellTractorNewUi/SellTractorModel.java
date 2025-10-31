package com.khetigaadi.tractor.sellTractorNewUi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.khetigaadi.R;
import com.khetigaadi.commons.model.ModelResp;
import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.commons.activities.ModelSelectionActivity;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.databinding.FragmentTractorModelBinding;
import com.khetigaadi.databinding.ModelRowBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.ModelRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class SellTractorModel extends BaseFragment {
    @Inject
    ModelRepository modelRepository;
    int model_id = 0;
    private SellViewModel viewModel;
    @Inject
    KhetiAPIs khetiAPIs;
    private FragmentTractorModelBinding binding;
    private FragmentActivity activity;
    List<Model> modelsv = new ArrayList<Model>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    ActivityResultLauncher<Intent> modelSelectResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Model selectedModel = data != null ? data.getParcelableExtra(ModelSelectionActivity.SELECTED_MODEL) : null;
                    if (selectedModel != null) {
                        binding.etSellTractorModel.setText(selectedModel.getName());
                        model_id = selectedModel.getId();
                        searchModelData(selectedModel);
                    }
                }
            });
    public static SellTractorModel getInstance() {
        return new SellTractorModel();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentTractorModelBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider((SellTractorActivity)activity).get(SellViewModel.class);
        binding.etSellTractorModel.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ModelSelectionActivity.class);
            intent.putExtra(ModelSelectionActivity.SELECTED_BRAND, viewModel.getUsedTractor().getBrand());
            modelSelectResultLauncher.launch(intent);
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        initList();
    }
    @Override
    public void onResume() {
        super.onResume();
        ((SellTractorActivity) activity).iconColorChange(2);
    }
    private void initList() {
        if (viewModel.getUsedTractor().getBrand() != null)
            modelRepository.getNewTractorModels(viewModel.getUsedTractor().getBrand().getId()).observe(this, listResource -> {
                if(listResource.size()>0) {
                    List<Model> brandParents = listResource;
                    if(brandParents != null) {
                        Timber.e("onChanged: %s", brandParents.toString());
                        try {
                            binding.llModel.removeAllViews();
                            for (int i = 0; i < brandParents.size(); i = i + 2) {
                                LinearLayout linearLayout = new LinearLayout(activity);
                                linearLayout.setWeightSum(2);
                                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                for (int j = 0; j <= 1 && i + j < brandParents.size(); j++) {
                                    @NonNull ModelRowBinding rowBinding = ModelRowBinding.inflate(getLayoutInflater(), linearLayout, false);
                                    final Model model = brandParents.get(i + j);
                                    rowBinding.tvModelName.setText(model.getName());
                                    if (viewModel.getUsedTractor().getModel() != null && viewModel.getUsedTractor().getModel().equals(model)) {
                                        rowBinding.ll.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_selector_red));
                                    }
                                    rowBinding.getRoot().setOnClickListener(v -> {
                                        binding.etSellTractorModel.setText(model.getName());
                                        model_id = model.getId();
                                        rowBinding.ll.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_selector_red));
                                        initList();
                                        searchModelData(model);
                                    });
                                    linearLayout.addView(rowBinding.getRoot());
                                }
                                binding.llModel.addView(linearLayout);
                            }
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    getModelsList();
                }
            });
    }
    /** @noinspection unchecked*/
    private void searchModelData(Model model) {
        if (model != null) {
            viewModel.getUsedTractor().setModel(model);
            ((SellTractorActivity)activity).callFragments(3);
        }
    }
    public void getModelsList()
    {
        if(modelsv.size()>0)
            return;
        else modelsv.clear();
        Log.d("bnbvnvbvcvbc====",""+viewModel.getUsedTractor().getBrand().getId());
        disposable.add(khetiAPIs.getNewTractorModels(viewModel.getUsedTractor().getBrand().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ModelResp>() {
                    @Override
                    public void onSuccess(@NotNull ModelResp modelsResp) {
                        try {
                            List<Model> models = modelsResp.getData().getData();
                            for(int i=0;i<models.size();i++) {
                                Model model = models.get(i);
                                modelsv.add(model);
                                modelRepository.deleteModel(model);
                                modelRepository.insertModel(model);
                            }
                            if(models != null) {
                                Timber.e("onChanged: %s", models.toString());
                                try {
                                    binding.llModel.removeAllViews();
                                    for (int i = 0; i < models.size(); i = i + 2) {
                                        LinearLayout linearLayout = new LinearLayout(activity);
                                        linearLayout.setWeightSum(2);
                                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                        for(int j = 0; j <= 1 && i + j < models.size(); j++) {
                                            @NonNull ModelRowBinding rowBinding = ModelRowBinding.inflate(getLayoutInflater(),linearLayout,false);
                                            final Model model = models.get(i + j);
                                            rowBinding.tvModelName.setText(model.getName());
                                            if(viewModel.getUsedTractor().getModel() != null && viewModel.getUsedTractor().getModel().equals(model)) {
                                                rowBinding.ll.setBackground(ContextCompat.getDrawable(activity,R.drawable.button_selector_red));
                                            }
                                            rowBinding.getRoot().setOnClickListener(v -> {
                                                binding.etSellTractorModel.setText(model.getName());
                                                model_id = model.getId();
                                                rowBinding.ll.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_selector_red));
                                                initList();
                                                searchModelData(model);
                                            });
                                            linearLayout.addView(rowBinding.getRoot());
                                        }
                                        binding.llModel.addView(linearLayout);
                                    }
                                }
                                catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                    }
                }));
    }
}
