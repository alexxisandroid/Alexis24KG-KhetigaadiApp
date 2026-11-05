package com.khetigaadi.commons.activities;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.khetigaadi.R;
import com.khetigaadi.base.model.City;
import com.khetigaadi.commons.MyLocation;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.CustomCityMenuLayoutBinding;
import com.khetigaadi.databinding.GetCityDialogBinding;
import com.khetigaadi.language.LocaleManager;
import com.khetigaadi.room.repository.CityRepository;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.inject.Inject;
import timber.log.Timber;
public abstract class BaseActivity extends AppCompatActivity {
    private static final int REQUEST_ACCESS_PERMISSION = 54;
    protected static final int REQUEST_CHECK_SETTINGS = 85;
    private static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    private GoogleApiClient mGoogleApiClient;
    private List<City> cityList = new ArrayList<>();
    @Inject
    SessionManager session;
    protected User user;
    @Inject
    Gson gson;
    @Inject
    SharedPreferenceUtils pref;
    private Boolean cityValid = false;
    private String cityNameGeo;
    private String firstUpdate;
    @Inject
    CityRepository cityRepository;
    private GetCityDialogBinding cityDialogBinding;
    private ProgressDialog progressDialog;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    public BaseActivity() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       try {
          // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
           //noinspection deprecation
           mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
           if (session.isLoggedIn())
               user = session.getUser();
           requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted -> {
               if (!isGranted) {
                   showAlertForNotification();
               }
           });
          // EdgeToEdge.enable(this);
       }
       catch(Exception e) {
           e.printStackTrace();
       }
    }

    private void showAlertForNotification() {
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setMessage(getResources().getString(R.string.mandatory_permission_access_required))
                .setNegativeButton("Don’t Enable", (dialog, which) -> dialog.cancel())
                .setPositiveButton("Yes, Enable", (dialog, which) -> {
                    try {
                        Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
                        intent.putExtra("android.provider.extra.APP_PACKAGE",getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                    dialog.cancel();
                }).show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
    }
    protected void showProgressDialog(String message) {
       try {
           if (progressDialog == null) {
               progressDialog = new ProgressDialog(getApplicationContext());
           }
           Log.d("xcvcvxvxvxvcxvx2211bb====", "" + progressDialog + "==" + getApplicationContext());
           if (!progressDialog.isShowing()) {
               progressDialog = new ProgressDialog(getApplicationContext());
               progressDialog.setMessage(message);
               //progressDialog.show();

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       progressDialog.show();
                   }
               });
           }
       }
       catch (Exception e) {
           e.printStackTrace();
       }
     }
    protected void showProgressDialog(String message,Context context) {
       try {
           if (progressDialog == null) {
               progressDialog = new ProgressDialog(context);
           }
           Log.d("xcvcvxvxvxvcxvx2211bb====", "" + progressDialog + "==" + getApplicationContext() + "==" + context);
           if (!progressDialog.isShowing()) {
               progressDialog = new ProgressDialog(context);
               progressDialog.setMessage(message);
               //progressDialog.show();

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       progressDialog.show();
                   }
               });
           }
       }
       catch (Exception e){
           e.printStackTrace();
       }
    }
    //showOrUpdateProgressDialog
     protected void showOrUpdateProgressDialog(String message) {
       try {
           if (progressDialog == null) {
               progressDialog = new ProgressDialog(getApplicationContext());
           }
           if (!progressDialog.isShowing()) {
               progressDialog = new ProgressDialog(getApplicationContext());
               progressDialog.setMessage(message);
               //progressDialog.show();

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       progressDialog.show();
                   }
               });
           }
       }
       catch (Exception e) {
           e.printStackTrace();
       }
     }
     protected void dismissProgressDialog()
     {
         try {
             if (progressDialog != null && progressDialog.isShowing()) {
                 progressDialog.dismiss();
             }
         } catch(Exception e) {
             e.printStackTrace();
         }
     }
     @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    public void setupTouchUIToDismissKeyboard(View view) {
     try {
         setupTouchUIToDismissKeyboard(view,(v,event) -> {
             hideSoftKeyboard(BaseActivity.this);
             return false;
         }, -1);
     }
     catch(Exception e){
         e.printStackTrace();
     }
    }
    public void showSoftKeyboard() {
       try {
           InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
           View v = getCurrentFocus();
           if (v != null)
               imm.showSoftInput(v, 0);
       }
       catch(Exception e){
           e.printStackTrace();
       }
    }
    public static void setupTouchUIToDismissKeyboard(View view,View.OnTouchListener onTouchListener,final Integer... exceptIDs) {
       try {
           List<Integer> ids = new ArrayList<>();
           if (exceptIDs != null)
               ids = Arrays.asList(exceptIDs);

           //Set up touch listener for non-text box views to hide keyboard.
           if (!(view instanceof EditText)) {
               if (!ids.isEmpty() && ids.contains(view.getId())) {
                   return;
               }
               view.setOnTouchListener(onTouchListener);
           }

           //If a layout container, iterate over children and seed recursion.
           if (view instanceof ViewGroup) {
               for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                   View innerView = ((ViewGroup) view).getChildAt(i);
                   setupTouchUIToDismissKeyboard(innerView, onTouchListener, exceptIDs);
               }
           }
       }
       catch(Exception e){
           e.printStackTrace();
       }
    }
    public static void hideSoftKeyboard(Activity activity) {
       try {
           InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
           if(inputMethodManager == null)
               return;
           if(activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null)
               inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
       }
       catch(Exception e) {
           e.printStackTrace();
       }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
      try {
          SessionManager sessionManager = new SessionManager(newBase);
          super.attachBaseContext(new LocaleManager(sessionManager).setLocale(newBase));
          Timber.d("attachBaseContext");
      }
      catch (Exception e) {
          e.printStackTrace();
      }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String @NotNull [] permissions,int @NotNull [] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == REQUEST_ACCESS_PERMISSION) {
            this.getPackageManager();
            if((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if(checkGooglePlayServices()) {
                    createLocationRequestAndSetCity();
                }
            }
            else {
                Timber.d("Call Permission Not Granted");
            }
            notificationPermission();
        }
    }
    protected void requestFocus(View view) {
        if(view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    protected boolean checkLocationPermission() {
        boolean permissionGranted = ActivityCompat.checkSelfPermission(BaseActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
       try {
           if(permissionGranted) {
               notificationPermission();
               if(checkGooglePlayServices()) {
                   //prepare connection request
                   return true;
               }
           }
           else {
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_PERMISSION);
           }
       }
       catch(Exception e) {
           e.printStackTrace();
       }
        return permissionGranted;
    }
    private boolean checkGooglePlayServices() {
        int checkGooglePlayServices = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(BaseActivity.this);
       try {
           if(checkGooglePlayServices != ConnectionResult.SUCCESS) {
               /*
                * Google Play Services is missing or update is required
                *  return code could be
                * SUCCESS,
                * SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
                * SERVICE_DISABLED, SERVICE_INVALID.
                */
               Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(BaseActivity.this, checkGooglePlayServices, REQUEST_CODE_RECOVER_PLAY_SERVICES);
               if (errorDialog != null)
                   errorDialog.show();
               return false;
           }
       }
       catch(Exception e) {
           e.printStackTrace();
       }
        return true;
    }
//    protected void notificationPermission() {
//        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
//           try {
//               if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                   requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
//               }
//               else {
//                   showAlertForNotification();
//               }
//           }
//           catch(Exception e){
//               e.printStackTrace();
//           }
//        }
//    }

    protected void notificationPermission() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            try {
                try {
                    if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                        } else {
                            showAlertForNotification();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void createLocationRequest(Context context) {
        try {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(result1 -> {
                final Status status = result1.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        createLocationRequestAndSetCity();
                        Timber.i("All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Timber.i("Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(BaseActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Timber.i("PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Timber.i("Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void showGetCityDialog() {
        Log.d("cvxxvxvxvcxvxv===","");
       try {
           final Dialog dialog = new Dialog(BaseActivity.this);
           dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
           dialog.setCancelable(false);
           dialog.setCanceledOnTouchOutside(false);
           cityDialogBinding = GetCityDialogBinding.inflate(getLayoutInflater());
           dialog.setContentView(cityDialogBinding.getRoot());
           //start geo location functionality
           if(checkLocationPermission()) {
               createLocationRequest(BaseActivity.this);
           }
           getAllCity();
           cityDialogBinding.btnConfirm.setOnClickListener(v -> {
               try {
                   if(!cityDialogBinding.tvCity.getText().toString().trim().isEmpty()) {
                       if(cityList != null) {
                           List<City> cityItem = getCityItem(cityList, cityDialogBinding.tvCity.getText().toString());
                           if(cityItem.size() != 0) {
                               hideSoftKeyboard(this);
                               session.setUserLocation(cityItem.get(0));
                               cityValid = true;
                               refreshActionBarMenu();
                           }
                       }
                       dialog.dismiss();
                       if(!cityValid) {
                           Toast.makeText(BaseActivity.this,getString(R.string.please_select_correct_city), Toast.LENGTH_SHORT).show();
                       }
                   }
                   else {
                       Toast.makeText(BaseActivity.this,getString(R.string.please_select_city), Toast.LENGTH_SHORT).show();
                   }
               }
               catch(Exception e) {
                   e.printStackTrace();
               }
           });

           cityDialogBinding.tvCity.setOnTouchListener((v, event) -> {
               final int DRAWABLE_RIGHT = 2;

               if (event.getAction() == MotionEvent.ACTION_UP) {
                   if (event.getRawX() >= (cityDialogBinding.tvCity.getRight() - cityDialogBinding.tvCity.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                       Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                       startActivity(viewIntent);
                       return true;
                   }
               }
               return false;
           });
           if(Constants.isConnectionAvailable(this)) {
               if(!isDestroyed() && !isFinishing())
                   dialog.show();
           }
           else {
               Toast.makeText(this,getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show();
           }
       }
       catch(Exception e) {
           e.printStackTrace();
       }
    }
    private List<City> getCityItem(List<City> cityList,String cityName) {
        return cityList.stream().filter(city -> city.getName().equals(cityName)).collect(Collectors.toList());
    }
    public void refreshActionBarMenu() {
        this.invalidateOptionsMenu();
    }
    protected void createLocationRequestAndSetCity() {
        if(!new MyLocation().getLocation(this,new MyLocation.LocationResult() {
            @Override
            public void gotLocation(@NotNull Location mLastLocation) {
                try {
                    cityNameGeo = getCityFromGEOCode(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    if(cityNameGeo != null) {
                        if(cityDialogBinding.tvCity != null) {
                            cityDialogBinding.tvCity.setText(cityNameGeo);
                        }
                        refreshActionBarMenu();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })) {
            String cityName = null;
            if (user != null && user.getCity() != null) {
                cityName = user.getCity().getName();
            }
            if (cityName != null) {
                if (cityDialogBinding != null) {
                    cityDialogBinding.tvCity.setText(cityName);
                    cityDialogBinding.tvCity.setSelection(cityDialogBinding.tvCity.getText().length());
                }
                refreshActionBarMenu();
            }
        }
    }
    private String getCityFromGEOCode(double latitude,double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            return addresses.get(0).getSubAdminArea();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void getAllCity() {
        cityRepository.getCities().observe(this, listResource -> {
           // if (listResource.getStatus() == com.khetigaadi.base.room.repository.utils.Status.SUCCESS) {
                if (listResource.size()>0) {
                cityList = listResource;
                ArrayAdapter<City> adapter = new ArrayAdapter<>(BaseActivity.this,R.layout.spinner_row,cityList);
                cityDialogBinding.tvCity.setThreshold(1);
                cityDialogBinding.tvCity.setAdapter(adapter);
                cityDialogBinding.tvCity.setOnItemClickListener((arg0, arg1, position, arg3) -> {
                    cityDialogBinding.tvCity.setText(arg0.getItemAtPosition(position).toString());
                    cityDialogBinding.tvCity.setTag((City) arg0.getItemAtPosition(position));
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem saveMenu = menu.findItem(R.id.menuCity);
        if (saveMenu != null) {
            CustomCityMenuLayoutBinding binding = CustomCityMenuLayoutBinding.inflate(getLayoutInflater());
            LinearLayout saveLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.custom_city_menu_layout,null);
            saveMenu.setActionView(binding.getRoot());
            saveMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            City userLocation = session.getUserLocation();
            if(userLocation != null) {
                binding.tvCity.setText(userLocation.getName());

               // binding.tvCity.setVisibility(View.GONE);
               // binding.tvCity.setTextColor(Color.WHITE);
                Log.d("xvcvxcv===",""+userLocation.getName());
                saveLayout.setOnClickListener(v -> showGetCityDialog());
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuCity) {
            showGetCityDialog();
            return true;
        } else if (item.getItemId() == R.id.search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_RECOVER_PLAY_SERVICES: {
                if (resultCode == RESULT_OK) {
                    // Make sure the app is not already connected or attempting to connect
                    if (!mGoogleApiClient.isConnecting() &&
                            !mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.connect();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Google Play Services must be installed.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        createLocationRequestAndSetCity();
                        Timber.i("User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
                        Timber.i("User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }


    public void noInternetConnection(View view) {
        try {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),getResources().getString(R.string.no_internet_connection),Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.action_settings),v -> startActivityForResult(new Intent(Settings.ACTION_SETTINGS),0));
            snackbar.setActionTextColor(ContextCompat.getColor(this,R.color.white));
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(this,R.color.red));//change Snackbar's background color;
            TextView textView = snackbarView.findViewById(R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(this,R.color.white));//change Snackbar's text color;
            snackbar.show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


}


