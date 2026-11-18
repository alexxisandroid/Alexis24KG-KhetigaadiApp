package com.khetigaadi.tractor.rent.rentTractorNewUi;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
//import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowCompat;
//import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.activities.ModelSelectionActivity;
import com.khetigaadi.databinding.ModelRowBinding;
import com.khetigaadi.databinding.RentTractorModelBinding;
import com.khetigaadi.room.repository.ModelRepository;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@SuppressWarnings({"deprecation", "CallToPrintStackTrace"})
@AndroidEntryPoint
public class RentTractorModel extends BaseActivity {
 //   private static final String TAG = RentTractorModel.class.getSimpleName();
    @Inject
    ModelRepository modelRepository;
//    private EditText etSellTractorModel;
    int model_id = 0;
    private Brand brand;
    @Inject
    SharedPreferenceUtils pref;
    private RentTractorModelBinding binding;
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = RentTractorModelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setSupportActionBar(binding.toolbarI.toolbar);
        binding.toolbarI.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbarI.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.toolbarI.toolbar.setTitle(getString(R.string.rentTractor));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.rentTractor));
        if(getIntent().getExtras() != null) {
            //noinspection deprecation
            brand = getIntent().getExtras().getParcelable("brand");
        }
        iconColorChange();
        binding.etSellTractorModel.requestFocus();
        binding.etSellTractorModel.setOnClickListener(v -> {
            Intent intent = new Intent(RentTractorModel.this,ModelSelectionActivity.class);
            intent.putExtra(ModelSelectionActivity.SELECTED_BRAND,brand);
            startActivityForResult(intent,ModelSelectionActivity.MODEL_SELECTION_REQ);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.close_button_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.menu_close) {
            showAlerDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAlerDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.are_you_sure_you_want_quit))
                .setNegativeButton(getString(R.string.no),(dialog,which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes),(dialog,which) -> {
                    dialog.cancel();
                    addSomething();
                }).show();
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
       try {
           if (requestCode == ModelSelectionActivity.MODEL_SELECTION_REQ) {
               if (resultCode == RESULT_OK) {
                   Model selectedModel = data.getParcelableExtra(ModelSelectionActivity.SELECTED_MODEL);
                   if (selectedModel != null) {
                       binding.etSellTractorModel.setText(selectedModel.getName());
                       model_id = selectedModel.getId();
                       searchModelData(model_id);
                   }
               }
           }
       }
       catch (Exception e) {
           //noinspection CallToPrintStackTrace
           e.printStackTrace();
       }
    }
    @Override
    public void onStart() {
        super.onStart();
        initList();
    }
    private void iconColorChange() {
        binding.tabModel.tvBrand.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabModel.tvModel.setTextColor(ContextCompat.getColor(this, R.color.red));
    }
    private void initList() {
     try {
         modelRepository.getNewTractorModels(brand.getId()).observe(this,listResource -> {
             List<Model> brandParents;
             if(!listResource.isEmpty()) {
                brandParents = listResource;
                 try {
                     binding.llModel.removeAllViews();
                   //  if (brandParents != null) {
                         for(int i = 0; i < brandParents.size(); i = i + 2) {
                             LinearLayout linearLayout = new LinearLayout(RentTractorModel.this);
                             linearLayout.setWeightSum(2);
                             linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                             for(int j = 0; j <= 1 && i + j < brandParents.size(); j++) {
                                 ModelRowBinding rowBinding = ModelRowBinding.inflate(getLayoutInflater(), linearLayout,false);
                                 final Model model = brandParents.get(i + j);
                                 rowBinding.tvModelName.setText(model.getName());
                                 if(!pref.getRentModelId().isEmpty()) {
                                     if(pref.getRentModelId().equalsIgnoreCase(String.valueOf(model.getId()))) {
                                         rowBinding.ll.setBackground(ContextCompat.getDrawable(this,R.drawable.button_selector_red));
                                     }
                                 }
                                 rowBinding.getRoot().setOnClickListener(v -> {
                                     binding.etSellTractorModel.setText(model.getName());
                                     rowBinding.ll.setBackground(ContextCompat.getDrawable(this,R.drawable.button_selector_red));
                                     pref.setRentModelId(String.valueOf(model.getId()));
                                     initList();
                                     searchModelData(model.getId());
                                 });
                                 linearLayout.addView(rowBinding.getRoot());
                             }
                             binding.llModel.addView(linearLayout);
                         }
                   //  }
                 }
                 catch(Exception e) {
                     e.printStackTrace();
                 }
             }
         });
     }
     catch(Exception e) {
         //noinspection CallToPrintStackTrace
         e.printStackTrace();
     }
    }
    private void searchModelData(int modelId) {
       // int modelno = modelId;
        if(modelId != 0) {
            pref.setRentModelId(String.valueOf(modelId));
            Intent intent = new Intent(this,RentTractorYear.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
    private void addSomething() {
      try {
          if(!pref.getRentModelId().isEmpty()) {
              pref.removeKey("RENTMODELID");
          }
          if(!pref.getRentBrandId().isEmpty()) {
              pref.removeKey("RENTBRANDID");
          }
          Intent intent = new Intent(RentTractorModel.this,MainActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("Check","1");
          startActivity(intent);
          finish();
      }
      catch(Exception e) {
          //noinspection CallToPrintStackTrace
          e.printStackTrace();
      }
    }
    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!pref.getRentModelId().isEmpty()) {
            pref.removeKey("RENTMODELID");
        }
    }
}
