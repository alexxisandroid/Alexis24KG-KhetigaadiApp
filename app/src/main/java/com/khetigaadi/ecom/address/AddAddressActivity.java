package com.khetigaadi.ecom.address;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.khetigaadi.R;
import com.khetigaadi.base.model.City;
import com.khetigaadi.base.model.State;
import com.khetigaadi.commons.activities.CitySelectionActivity;
import com.khetigaadi.commons.activities.StateSelectionActivity;
import com.khetigaadi.databinding.ActivityAddAddressBinding;
import com.khetigaadi.shop.def.AddressType;
import com.khetigaadi.shop.model.Address;
import com.khetigaadi.util.Utils;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class AddAddressActivity extends AppCompatActivity  {
    private static final String TAG = "AddAddressActivity";
    public static final String SELECTED_ADDRESS = "selectedAddress";
    private ActivityAddAddressBinding binding;
    private AddAddressViewModel viewModel;
    private State selectedState;
    private City selectedCity;
    ActivityResultLauncher<Intent> stateSectionResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        selectedState = data.getParcelableExtra(StateSelectionActivity.SELECTED_STATE);
                        if (selectedState != null) {
                            binding.etState.setText(selectedState.getName());
                            binding.etCity.setText("");
                            selectedCity = null;
                        }
                    }
                }
            });
    ActivityResultLauncher<Intent> citySectionResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        selectedCity = data.getParcelableExtra(CitySelectionActivity.SELECTED_CITY);
                        if (selectedCity != null)
                            binding.etCity.setText(selectedCity.getName());
                    }
                }
            });
    private Address address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);
        viewModel = new ViewModelProvider(this).get(AddAddressViewModel.class);
        initToolbar();
        binding.etState.setOnClickListener(v -> stateSectionResultLauncher.launch(new Intent(AddAddressActivity.this, StateSelectionActivity.class)));
        binding.etCity.setOnClickListener(v -> {
            if (selectedState != null) {
                Intent intent = new Intent(AddAddressActivity.this, CitySelectionActivity.class);
                intent.putExtra("state", selectedState);
                citySectionResultLauncher.launch(intent);
            } else {
                Toast.makeText(AddAddressActivity.this, getString(R.string.please_select_state), Toast.LENGTH_SHORT).show();
            }
        });
        initUI(viewModel.getAddress());
        binding.btnSaveAndContinue.setOnClickListener(v -> {
            if (validate()) {
                address.setFirstName(binding.etFname.getText().toString());
                address.setLastName(binding.etLastName.getText().toString());
                address.setAddress(binding.etAddress.getText().toString());
                address.setState(selectedState);
                address.setCity(selectedCity);
                address.setPinCode(binding.etPincode.getText().toString());
                address.setMobile(binding.etMobile.getText().toString());
                address.setType(binding.radioHome.isChecked() ? AddressType.ADDRESS_HOME : AddressType.ADDRESS_OFFICE);
                viewModel.addUpdateAddress(address).observe(this, addressRespStateData -> {
                    switch (addressRespStateData.getStatus()) {
                        case LOADING: {
                            binding.btnSaveAndContinue.setEnabled(false);
                            binding.btnCancel.setEnabled(false);
                            break;
                        }
                        case SUCCESS:
                            binding.btnSaveAndContinue.setEnabled(true);
                            binding.btnCancel.setEnabled(true);
                            Intent returnIntent = new Intent();
                            if (addressRespStateData.getData() != null) {
                                //  returnIntent.putExtra(SELECTED_ADDRESS,addressRespStateData.getData().getData());
                                returnIntent.putExtra(SELECTED_ADDRESS,""+addressRespStateData.getData());
                                setResult(RESULT_OK,returnIntent);
                                finish();
                            }
                            break;
                        case ERROR:
                            binding.btnSaveAndContinue.setEnabled(true);
                            binding.btnCancel.setEnabled(true);
                            Toast.makeText(this,addressRespStateData.getError(),Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
            }
        });
        binding.btnCancel.setOnClickListener(v -> onBackPressed());
        binding.toolbarTitle.setOnClickListener(v -> finish());
    }
    private void initUI(Address address) {
        this.address = address;
        binding.etFname.setText(address.getFirstName());
        binding.etLastName.setText(address.getLastName());
        binding.etAddress.setText(address.getAddress());
        if (address.getState() != null) {
            selectedState = address.getState();
            binding.etState.setText(selectedState.getName());
        }
        if (address.getCity() != null) {
            selectedCity = address.getCity();
            binding.etCity.setText(selectedCity.getName());
        }
        binding.etPincode.setText(address.getPinCode());
        binding.etMobile.setText(address.getMobile());
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbarI.toolbar);
        binding.toolbarI.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbarI.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbarI.toolbar.setTitle(getString(R.string.add_address));
    }
    private boolean validate() {
        if (!validateFName()) {
            Toast.makeText(this, getString(R.string.invalidName), Toast.LENGTH_SHORT).show();
            binding.tilFirstName.setError(getString(R.string.invalidName));
        } else if (!validateLName()) {
            binding.tilLastName.setError(getString(R.string.invalidName));
        } else if (!validateAddress()) {
        } else if (selectedState == null || selectedState.getName().equals("")) {
            Toast.makeText(this, getString(R.string.please_select_state), Toast.LENGTH_SHORT).show();
        } else if (selectedCity == null || selectedCity.getName().equals("")) {
            Toast.makeText(this, getString(R.string.please_select_city), Toast.LENGTH_SHORT).show();
        } else if (!validatePinCode()) {
        } else if (!validateMobile()) {
        } else {
            binding.tilFirstName.setErrorEnabled(false);
            binding.tilFirstName.setErrorEnabled(false);
            binding.tilAddress.setErrorEnabled(false);
            binding.tilPinCode.setErrorEnabled(false);
            binding.tilMobile.setErrorEnabled(false);
            return true;
        }
        return false;
    }
    private boolean validateMobile() {
        String mobile = binding.etMobile.getText().toString().trim();
        if (mobile.isEmpty() || !Utils.isValidPhone(mobile)) {
            binding.tilMobile.setError(getString(R.string.err_msg_mobile));
            requestFocus(binding.etMobile);
            return false;
        } else {
            binding.tilMobile.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validatePinCode() {
        String name = binding.etPincode.getText().toString().trim();
        if (name.isEmpty() || name.length() < 6) {
            binding.tilPinCode.setError(getString(R.string.err_msg_pincode));
            requestFocus(binding.etPincode);
            return false;
        } else {
            binding.tilPinCode.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateAddress() {
        String name = binding.etAddress.getText().toString().trim();
        if (name.isEmpty()) {
            binding.tilAddress.setError(getString(R.string.err_msg_addres));
            requestFocus(binding.etAddress);
            return false;
        } else {
            binding.tilAddress.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateLName() {
        String name = binding.etLastName.getText().toString().trim();
        if (name.isEmpty() || name.length() < 3) {
            binding.tilLastName.setError(getString(R.string.err_msg_f_name));
            requestFocus(binding.etLastName);
            return false;
        } else {
            binding.tilLastName.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateFName() {
        String name = binding.etFname.getText().toString().trim();
        if (name.isEmpty() || name.length() < 3) {
            binding.tilFirstName.setError(getString(R.string.err_msg_f_name));
            requestFocus(binding.etFname);
            return false;
        } else {
            binding.tilFirstName.setErrorEnabled(false);
        }
        return true;
    }
    private void requestFocus(EditText etFname) {
    }
}