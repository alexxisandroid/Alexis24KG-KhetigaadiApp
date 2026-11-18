package com.khetigaadi.user.profile;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.agri.activity.BrandSelectionActivity;
import com.khetigaadi.agri.activity.CropSelectionsActivity;
import com.khetigaadi.base.WebViewActivity;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.base.model.City;
import com.khetigaadi.base.model.State;
import com.khetigaadi.base.model.Taluka;
import com.khetigaadi.base.model.agri.Crop;
import com.khetigaadi.base.util.StateData;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.activities.CitySelectionActivity;
import com.khetigaadi.commons.activities.StateSelectionActivity;
import com.khetigaadi.commons.activities.TalukaSelectionActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityEditProfileBinding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.pick_image.CropParam;
import com.khetigaadi.pick_image.GalleryImagesActivity;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;
import com.khetigaadi.user.model.UserType;
import com.khetigaadi.user.retrofit.UserAPIs;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@SuppressWarnings({"deprecation", "CallToPrintStackTrace"})
@AndroidEntryPoint
public class EditProfileActivity extends BaseActivity {
    private static final String TAG = "EditProfileActivity";
    public static final String IS_COMPULSORY = "isCompulsory";
    //Here I am changed Crop to crops
    private ArrayList<Crop> selectedCrops;
    private Brand selectedBrand;
    private UserType selectedUserType;
    //private UserDetail userDetail;
    private User user;
    private boolean isProfileImageChange;
    private boolean isCompulsory;
    private boolean doubleBackToExitPressedOnce;
    private State selectedState;
    private City selectedCity;
    @Inject
    SessionManager session;
    @Inject
    UserAPIs userAPIs;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    handleCropResult(data != null ? data.getData() : null);
                }
            });
    private Uri profileImage;
    private EditProfileViewModel viewModel;
    private ActivityEditProfileBinding binding;
    private Taluka selectedTaluka;
    private String selectedGender = null;
    private long selectedDOB = 0;
    @SuppressWarnings("unchecked")
    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        ViewCompat.setOnApplyWindowInsetsListener(view,(v,insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        setContentView(view);
       // EdgeToEdge.enable(this);
        viewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        if(getIntent().getExtras() != null)
            isCompulsory = getIntent().getExtras().getBoolean(IS_COMPULSORY);
        user = session.getUser();
        binding.etDOB.setOnClickListener(view12 -> {
           Log.d("cxvcxvxvxc===","");
           binding.tilDOB.setErrorEnabled(false);

            CalendarConstraints.Builder calenderConstraints = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now());
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setCalendarConstraints(calenderConstraints.build())
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDOB = selection;
               //binding.etDOB.setText(DateUtils.getDate(selection,DateUtils.DateFormat.dd_mm_yyyy));
               // @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
                long dateInString = selection;
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterOut = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    // Date date = formatter.parse(dateInString);
                    //System.out.println(date);
                    //System.out.println(formatterOut.format(date));
                    Date date = new Date(dateInString);
                    //assert date != null;
                    String dtt = formatterOut.format(date);
                    binding.etDOB.setText(dtt);
                }
                catch(Exception e) {
                    //noinspection CallToPrintStackTrace
                    e.printStackTrace();
                }
//               @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy MMM dd HH:mm:ss Z");
//                String dateInString = ""+selection;
//                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterOut = new SimpleDateFormat("dd MMM yyyy");
//                try {
//                    Date date = formatter.parse(dateInString);
//                    System.out.println(date);
//                    //System.out.println(formatterOut.format(date));
//                    assert date != null;
//                    String dtt = formatterOut.format(date);
//                    binding.etDOB.setText(dtt);
//                }
//                catch(ParseException e) {
//                    e.printStackTrace();
//                }
            });
            datePicker.show(getSupportFragmentManager(),TAG);
        });
        List<String> genders = new ArrayList<>();
        genders.add("Select Gender");
        genders.add("Male");
        genders.add("Female");
        genders.add("Other");
        @SuppressWarnings("rawtypes") ArrayAdapter genderAdapter = new ArrayAdapter(EditProfileActivity.this,android.R.layout.simple_dropdown_item_1line,android.R.id.text1,genders);
        binding.actGender.setAdapter(genderAdapter);
//        binding.actGender.setOnItemClickListener((parent,view1,position,id) -> {
//        binding.tilGender.setErrorEnabled(false);
//        selectedGender = getGender(genderAdapter.getItem(position).toString());
//         Log.d("cvxbcxcxvcxv====",""+selectedGender);
//        });

      //  binding.actGender.setOnItemSelectedListener();

        binding.actGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView,View view,int position,long id) {
                Object item = adapterView.getItemAtPosition(position);
                if(item != null) {
                    binding.tilGender.setErrorEnabled(false);
                    selectedGender = getGender(Objects.requireNonNull(genderAdapter.getItem(position)).toString());
                    // Toast.makeText(EditProfileActivity.this,item.toString()+"=="+selectedGender,Toast.LENGTH_SHORT).show();
                }
               // Toast.makeText(EditProfileActivity.this, "Selected",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });

        initUI();
        initToolbar();
        getProfile();
        binding.tvTermsAndConditionLink.setOnClickListener(view1 -> {
            Intent i = new Intent(EditProfileActivity.this,WebViewActivity.class);
            i.putExtra(WebViewActivity.URL,"https://khetigaadi.com/terms-and-conditions/en");
            i.putExtra(WebViewActivity.TITLE,getString(R.string.terms_and_conditions));
            startActivity(i);
        });
        binding.cbTerms.setOnClickListener(view1 -> {
            Intent i = new Intent(EditProfileActivity.this,WebViewActivity.class);
            i.putExtra(WebViewActivity.URL,"https://khetigaadi.com/terms-and-conditions/en");
            i.putExtra(WebViewActivity.TITLE,getString(R.string.terms_and_conditions));
          //  startActivity(i);
        });
        binding.cardView.setBackgroundColor(Color.WHITE);
    }
    private String getGender(String gender) {
        if(gender.equalsIgnoreCase("male"))
            return "m";
        else if(gender.equalsIgnoreCase("female"))
            return "f";
        else
            return "o";
    }
    private void getProfile() {
        viewModel.getProfile().observe(this, userStateData -> {
            if (userStateData.getStatus() == StateData.Status.ERROR) {
                Toast.makeText(EditProfileActivity.this,userStateData.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressLint("LogNotTimber")
    private void initUI() {
        if (isCompulsory)
            hideShowFields();
        initViewWithUserData();
        binding.ivImage.setOnClickListener(v -> {
            CropParam cropParams = new CropParam();
            cropParams.setCropImageName(System.currentTimeMillis() + ".png");
            cropParams.setRounded(true);
            Intent intent = new Intent(EditProfileActivity.this,GalleryImagesActivity.class);
            intent.putExtra("cropParams",cropParams);
            galleryActivityResultLauncher.launch(intent);
        });
        binding.ivEditImage.setOnClickListener(v -> binding.ivImage.callOnClick());
        binding.rbYes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.etBrand.setVisibility(View.VISIBLE);
                binding.tvOwnedBrand.setVisibility(View.VISIBLE);
            } else {
                binding.etBrand.setVisibility(View.GONE);
                binding.tvOwnedBrand.setVisibility(View.GONE);
            }
        });
        ActivityResultLauncher<Intent> userTypeResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        selectedUserType = data != null ? data.getParcelableExtra(UserTypeSelectionsActivity.SELECTED_USER_TYPE) : null;
                        if (selectedUserType != null) {
                            binding.etUserType.setText(selectedUserType.getUserLevelName());
                        }
                    }
                });
        binding.etUserType.requestFocus();
        binding.etUserType.setOnClickListener(v -> userTypeResultLauncher.launch(new Intent(EditProfileActivity.this,UserTypeSelectionsActivity.class)));
        ActivityResultLauncher<Intent> cropSectionResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        selectedCrops = data != null ? data.getParcelableArrayListExtra(CropSelectionsActivity.SELECTED_CROPS) : null;
                        if (selectedCrops != null) {
                            binding.etCrops.setText(getSelectedCropsString(selectedCrops));
                        }
                    }
                });
        binding.etCrops.setOnClickListener(v -> {
           ///// binding.tilCrops.setErrorEnabled(false);
            Intent intent = new Intent(EditProfileActivity.this,CropSelectionsActivity.class);
            intent.putExtra("crops", selectedCrops);
            cropSectionResultLauncher.launch(intent);
        });
        ActivityResultLauncher<Intent> brandSectionResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        selectedBrand = data != null ? data.getParcelableExtra(BrandSelectionActivity.SELECTED_BRAND) : null;
                        if(selectedBrand != null) {
                            binding.etBrand.setText(selectedBrand.getName());
                        }
                    }
                });
        binding.etBrand.requestFocus();
        binding.etBrand.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this,BrandSelectionActivity.class);
            intent.putExtra(BrandSelectionActivity.BRAND_TYPE,BrandType.OLD_TRACTOR);
            brandSectionResultLauncher.launch(intent);
        });
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
        binding.etState.requestFocus();
        binding.etState.setOnClickListener(v -> stateSectionResultLauncher.launch(new Intent(EditProfileActivity.this,StateSelectionActivity.class)));
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
        binding.etCity.requestFocus();
        binding.etCity.setOnClickListener(v -> {
            if (selectedState != null) {
                Intent intent = new Intent(EditProfileActivity.this,CitySelectionActivity.class);
                intent.putExtra("state", selectedState);
                citySectionResultLauncher.launch(intent);
            } else {
                Toast.makeText(EditProfileActivity.this, getString(R.string.please_select_state),Toast.LENGTH_SHORT).show();
            }
        });
        ActivityResultLauncher<Intent> talukaSectionResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            selectedTaluka = data.getParcelableExtra(TalukaSelectionActivity.SELECTED_TALUKA);
                            if (selectedTaluka != null)
                                binding.etTaluka.setText(selectedTaluka.getName());
                        }
                    }
                });
        binding.etTaluka.setOnClickListener(v -> {
            if (selectedCity != null) {
                Intent intent = new Intent(EditProfileActivity.this,TalukaSelectionActivity.class);
                intent.putExtra("city",selectedCity);
                talukaSectionResultLauncher.launch(intent);
            } else {
                Toast.makeText(EditProfileActivity.this,getString(R.string.please_select_state),Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnUpdate.setOnClickListener(v -> {
            if (validate()) {
                callUpdateProfile();
            }
            Log.d("vxcvxcvxcv===",""+validate());
        });
        binding.etState.setLongClickable(false);
        binding.etState.setTextIsSelectable(false);
        binding.etCity.setLongClickable(false);
        binding.etCity.setTextIsSelectable(false);
    }
    private void hideShowFields() {
        binding.inputLayoutEmail.setVisibility(View.GONE);
        binding.tilUserType.setVisibility(View.GONE);
        binding.tilBiography.setVisibility(View.GONE);
        binding.tilAwards.setVisibility(View.GONE);
        binding.tilCrops.setVisibility(View.GONE);
        binding.tilLand.setVisibility(View.GONE);
        binding.tvAreYouOwner.setVisibility(View.GONE);
        binding.rgTractorOwner.setVisibility(View.GONE);
        binding.tvOwnedBrand.setVisibility(View.GONE);
        binding.etBrand.setVisibility(View.GONE);
        binding.tvSmartPhone.setVisibility(View.GONE);
        binding.rgMobile.setVisibility(View.GONE);
    }
    @SuppressLint("LogNotTimber")
    private void initViewWithUserData() {
        viewModel.getUser().observe(this,user1 -> {
            if(user1 != null) {
                if(user1.getImage() != null)
                    Glide.with(EditProfileActivity.this)
                            .load(Constants.decodeURL(session.getUserOthers().getUserPath()+user1.getImage()))
                            .error(R.drawable.camera)
                            .placeholder(R.drawable.camera)
                            .into(binding.ivImage);
                binding.etFname.setText(user1.getFname());
                binding.etLastName.setText(user1.getLname());

                if(user.getUserDetail().getDob() != null && !user.getUserDetail().getDob().isEmpty()) {
                    selectedDOB = 0L; // DateUtils.getTimestamp(user.getUserDetail().getDob(),DateUtils.DateFormat.yyyy_MM_dd);
                    //binding.etDOB.setText(DateUtils.convertDateToDate(user.getUserDetail().getDob(),DateUtils.DateFormat.yyyy_MM_dd,DateUtils.DateFormat.dd_mm_yyyy));
                   // @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
                    String dateInString = user.getUserDetail().getDob();
                    Log.d("vbbcbcbc87===", dateInString);
                   // @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterOut = new SimpleDateFormat("dd-MM-yyyy");
                    try {
//                        Date date = formatter.parse(dateInString);
//                        System.out.println(date);
//                        //System.out.println(formatterOut.format(date));
//                        assert date != null;
//                        String dtt = formatterOut.format(date);
                        selectedDOB = 1L;
                        binding.etDOB.setText(dateInString);
                    }
                    catch(Exception e)
                    {
                        Log.d("cvbcbcvbcv===", Objects.requireNonNull(e.getLocalizedMessage()));
                        e.printStackTrace();
                    }
                }
                if(user.getUserDetail().getGender() != null && !user.getUserDetail().getGender().isEmpty()) {
                    selectedGender = user.getUserDetail().getGender();
                    Log.d("fgdsgdsgfgfg===", selectedGender);
                    if(user.getUserDetail().getGender().equalsIgnoreCase("m")) {
                      //  binding.actGender.setText(binding.actGender.getAdapter().getItem(0).toString(),false);
                        binding.actGender.setSelection(1);
                    } else if(user.getUserDetail().getGender().equalsIgnoreCase("f"))
                       //  binding.actGender.setText(binding.actGender.getAdapter().getItem(1).toString(),false);
                        binding.actGender.setSelection(2);
                    else
                        binding.actGender.setSelection(3);
                        //binding.actGender.setText(binding.actGender.getAdapter().getItem(2).toString(),false);
                }
                if(user1.getUserDetail().getCrops() != null) {
                    binding.etCrops.setText(getSelectedCropsString(user1.getUserDetail().getCrops()));
                    selectedCrops = (ArrayList<Crop>) user1.getUserDetail().getCrops();
                }
                if(user1.getUserDetail() != null) {
                    if(user1.getUserDetail().getType() != null) {
                        binding.etUserType.setText(user1.getUserDetail().getType().getUserLevelName());
                    }
                    binding.etLand.setText(user1.getUserDetail().getLand());
                    binding.etBiography.setText(user1.getUserDetail().getBiography());
                    binding.etAwards.setText(user1.getUserDetail().getAwards());

                    if(user1.getUserDetail().getIs_Owner() != null)
                        if(user1.getUserDetail().getIs_Owner().equalsIgnoreCase("0")) {
                            binding.rbNo.setChecked(true);
                            binding.tvOwnedBrand.setVisibility(View.GONE);
                            binding.etBrand.setVisibility(View.GONE);
                        }
                    else {
                            binding.rbYes.setChecked(true);
                            binding.tvOwnedBrand.setVisibility(View.VISIBLE);
                            binding.etBrand.setVisibility(View.VISIBLE);
                            if(user1.getUserDetail().getBrand() != null) {
                                binding.etBrand.setText(user1.getUserDetail().getBrand().getName());
                            }
                        }
                    if(user1.getUserDetail().getIs_smartphone().equalsIgnoreCase("0")) {
                        binding.rbSmartPhoneNo.setChecked(true);
                    }
                    else {
                        binding.rbSmartPhoneYes.setChecked(true);
                    }
                }
                if(user1.getState() != null) {
                    binding.etState.setText(user1.getState().getName());
                    selectedState = new State(user1.getState().getId(),user1.getState().getName());
                }
                if(user1.getCity() != null) {
                    binding.etCity.setText(user1.getCity().getName());
                    selectedCity = new City(user1.getCity().getId(),user1.getCity().getName());
                }
                if(user1.getTaluka() != null) {
                    binding.etTaluka.setText(user1.getTaluka().getName());
                    selectedTaluka = user1.getTaluka();
                }
                binding.inputMobile.setText(user1.getMobile());
                binding.inputEmail.setText(user1.getEmail());
                Log.d("xcvvxvcxvcxvx1234====", selectedTaluka+"=="+user1+"=="+user);
            }
        });
    }
    @SuppressLint("LogNotTimber")
    private void callUpdateProfile() {
        user.setFname(Objects.requireNonNull(binding.etFname.getText()).toString());
        user.setLname(Objects.requireNonNull(binding.etLastName.getText()).toString());
        user.setEmail(Objects.requireNonNull(binding.inputEmail.getText()).toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            if(!Objects.requireNonNull(binding.etDOB.getText()).isEmpty()) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateInString = binding.etDOB.getText().toString();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatterOut = new SimpleDateFormat("dd-MM-yyyy");
                try {
                   // Date date = formatter.parse(dateInString);
                  //  System.out.println(date);
                    //System.out.println(formatterOut.format(date));
                  //  assert date != null;
                  //  String dtt = formatterOut.format(date);
                    // binding.tvRegisteredOn.setText(String.format("%s %s", getString(R.string.registered_on),dtt));
                    user.getUserDetail().setDob(dateInString);
                    Log.d("vbbcbcbc4==",user.getUserDetail().getDob()+"=="+dateInString);
                }
                catch(Exception e) {
                    Log.d("cvbcbcvbcvb===",Objects.requireNonNull(e.getLocalizedMessage()));
                    e.printStackTrace();
                }
            }
        }

        // user.getUserDetail().setDob(DateUtils.getDate(selectedDOB,DateUtils.DateFormat.yyyy_MM_dd));
        user.getUserDetail().setGender(selectedGender);
        if (!isCompulsory) {
            if (selectedUserType != null)
                user.getUserDetail().setType(selectedUserType);
            if (!Objects.requireNonNull(binding.etBiography.getText()).toString().isEmpty()) {
                user.getUserDetail().setBiography(binding.etBiography.getText().toString());
            }
            if (!Objects.requireNonNull(binding.etAwards.getText()).toString().isEmpty()) {
                user.getUserDetail().setAwards(binding.etAwards.getText().toString());
            }
            if (!Objects.requireNonNull(binding.etLand.getText()).toString().isEmpty()) {
                user.getUserDetail().setLand(binding.etLand.getText().toString());
            }
            if (selectedCrops != null)
                user.getUserDetail().setCrops(selectedCrops);
            if (binding.rbYes.isChecked()) {
                user.getUserDetail().setIs_Owner("1");
                if (selectedBrand != null)
                    user.getUserDetail().setBrand(selectedBrand);
            }
            if (binding.rbNo.isChecked())
                user.getUserDetail().setIs_Owner("0");
            if (binding.rbSmartPhoneYes.isChecked()) {
                user.getUserDetail().setIs_smartphone("1");
            } else {
                user.getUserDetail().setIs_smartphone("0");
            }
        }
        user.setState(selectedState);
        user.setCity(selectedCity);
        user.setTaluka(selectedTaluka);

        Log.d("vbbcbcbc==",user.getUserDetail().getDob()+"=="+selectedTaluka);

        viewModel.updateUser(user).observe(this,userUpdate -> {
            Log.d("vbbcbcbc465746==",""+userUpdate.getStatus());
            switch (userUpdate.getStatus()) {
                case LOADING:
                    showProgressDialog(getString(R.string.loading),EditProfileActivity.this);
                    break;
                case SUCCESS:
                    if(isProfileImageChange) {
                        uploadProfileImage();
                    }
                    else {
                        dismissProgressDialog();
                        if(userUpdate.getData() != null) {
                            Toast.makeText(EditProfileActivity.this,userUpdate.getData().getMsg().get(0),Toast.LENGTH_LONG).show();
                        }
                        if(isCompulsory) {
                            startActivity(new Intent(EditProfileActivity.this,MainActivity.class));
                        }
                        finish();
                    }
                    dismissProgressDialog();
                    break;
                case ERROR:
                    Toast.makeText(EditProfileActivity.this,userUpdate.getError(),Toast.LENGTH_SHORT).show();
                    dismissProgressDialog();
                    break;
            }
        });
    }
    private boolean validate() {
        if(!validateFName()) {
            Toast.makeText(this,getString(R.string.invalidName),Toast.LENGTH_SHORT).show();
            binding.tilFirstName.setError(getString(R.string.invalidName));
        } else if(!validateLName()) {
            binding.tilLastName.setError(getString(R.string.invalidName));
        } else if(selectedDOB == 0) {
            binding.tilDOB.setError(getString(R.string.please_select_date_of_birth));
        } else if(selectedGender == null ) {
            binding.tilGender.setError(getString(R.string.please_select_gender));
        } else if(selectedState == null || selectedState.getName().isEmpty()) {
            Toast.makeText(this, getString(R.string.please_select_state), Toast.LENGTH_SHORT).show();
        } else if(selectedCity == null || selectedCity.getName().isEmpty()) {
            Toast.makeText(this, getString(R.string.please_select_city),Toast.LENGTH_SHORT).show();
        } else if(selectedTaluka == null || TextUtils.isEmpty(selectedTaluka.getName()) || Objects.requireNonNull(binding.etTaluka.getText()).toString().isEmpty()) {
            Toast.makeText(this,getString(R.string.please_select_taluka),Toast.LENGTH_SHORT).show();
        } else if(!binding.cbTerms.isChecked()) {
            Toast.makeText(this,getString(R.string.please_check_agree_button),Toast.LENGTH_SHORT).show();
        } else {
           // binding.tilFirstName.setErrorEnabled(false);
          //  binding.tilFirstName.setErrorEnabled(false);
           ///// binding.tilDOB.setErrorEnabled(false);
            return true;
        }
        return false;
    }
//    private boolean validate2() {
//        if (!validateFName()) {
//            Toast.makeText(this,getString(R.string.invalidName),Toast.LENGTH_SHORT).show();
//            binding.tilFirstName.setError(getString(R.string.invalidName));
//        } else if (!validateLName()) {
//            binding.tilLastName.setError(getString(R.string.invalidName));
//        }
//        else if (selectedState == null || selectedState.getName().isEmpty()) {
//            Toast.makeText(this,getString(R.string.please_select_state),Toast.LENGTH_SHORT).show();
//        } else if (selectedCity == null || selectedCity.getName().isEmpty()) {
//            Toast.makeText(this,getString(R.string.please_select_city),Toast.LENGTH_SHORT).show();
//        } else if (!binding.cbTerms.isChecked()) {
//            Toast.makeText(this,getString(R.string.please_check_agree_button),Toast.LENGTH_SHORT).show();
//        } else {
//            binding.tilFirstName.setErrorEnabled(false);
//            //binding.tilFirstName.setErrorEnabled(false);
//            return true;
//        }
//        return false;
//    }

    public boolean validateFName() {
        String name = Objects.requireNonNull(binding.etFname.getText()).toString().trim();
        if (name.isEmpty()) {
            binding.tilFirstName.setError(getString(R.string.err_msg_f_name));
            requestFocus(binding.etFname);
            return false;
        } else {
            binding.tilFirstName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLName() {
        String name = Objects.requireNonNull(binding.etLastName.getText()).toString().trim();
        if(name.isEmpty()) {
            binding.tilLastName.setError(getString(R.string.err_msg_f_name));
            requestFocus(binding.etLastName);
            return false;
        }
        else {
           binding.tilLastName.setErrorEnabled(false);
        }
        return true;
    }

    private void initToolbar() {
        // setSupportActionBar(binding.toolbar);
       // binding.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
       // binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbar.setTitle(getString(R.string.edit_profile));
      //  Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.edit_profile));
      //  getSupportActionBar().setTitle(getString(R.string.edit_profile));
    }
    private void handleCropResult(Uri resultUri) {
        if(resultUri != null) {
            isProfileImageChange = true;
            profileImage = resultUri;
            Glide.with(this)
                    .load(resultUri)
                    .error(R.drawable.camera)
                    .into(binding.ivImage);
        }
        else {
            Toast.makeText(EditProfileActivity.this,R.string.toast_cannot_retrieve_cropped_image,Toast.LENGTH_SHORT).show();
        }
    }
    @NonNull
    private String getSelectedCropsString(List<Crop> selectedCrops) {
        try {
            StringBuilder cropName = new StringBuilder();
            for(int i = 0; i < selectedCrops.size(); i++) {
                cropName.append(selectedCrops.get(i).getName());
                if(i != selectedCrops.size() - 1) {
                    cropName.append(", ");
                }
            }
            return cropName.toString();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private void uploadProfileImage() {
       try {
           viewModel.uploadProfileImage(profileImage).observe(this, updateProfileImageRespStateData -> {
               switch (updateProfileImageRespStateData.getStatus()) {
                   case LOADING:
                       showOrUpdateProgressDialog("Wait image uploading!");
                       break;
                   case SUCCESS:
                       if (updateProfileImageRespStateData.getData() != null) {
                           Toast.makeText(getApplicationContext(), String.valueOf(updateProfileImageRespStateData.getData().getMsg().get(0)), Toast.LENGTH_SHORT).show();
                       }
                       if (isCompulsory) {
                           startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                       }
                       finish();
                       dismissProgressDialog();
                       break;
                   case ERROR:
                       Toast.makeText(EditProfileActivity.this, updateProfileImageRespStateData.getError(), Toast.LENGTH_SHORT).show();
                       dismissProgressDialog();
                       break;
               }
           });
       }
       catch (Exception e) {
           //noinspection CallToPrintStackTrace
           e.printStackTrace();
       }
    }
    /** @noinspection deprecation  */
    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        //Here Pranav change the code !isCompalsory to isCompalsory
        super.onBackPressed();
        if(!isCompulsory) {
            finish();
        }
        else {
            showMsg();
        }
    }
    private void showMsg() {
        try {
            if(!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                new MaterialAlertDialogBuilder(this)
                        .setTitle(getResources().getString(R.string.really_exit))
                        .setMessage(getString(R.string.are_you_sure_you_want_to_exit))
                        .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> finish()).show();
                new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false,2000);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
