package com.khetigaadi.pick_image;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.khetigaadi.pick_image.databinding.ActivityGalleryImagesBinding;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

import timber.log.Timber;
public class GalleryImagesActivity extends AppCompatActivity {
    public static final int CAMERA_PERMISSION_REQ = 45;
    private static final String TAG = "GalleryImagesActivity";
    private static final int REQUEST_TAKE_PHOTO = 65;
    private static final int PICK_IMAGE = 99;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.png";
    final String[] columns = {MediaStore.Images.Media._ID};
    final String orderBy = MediaStore.Images.Media._ID + " DESC";
    private Uri file;
    private ActivityGalleryImagesBinding binding;
    private CropParam cropParams;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityGalleryImagesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);
        if (!checkPermissionIsGranted()) {
            requestPermission();
        } else {
            getImg();
        }
        cropParams = getIntent().getParcelableExtra("cropParams");
        if (cropParams == null)
            cropParams = new CropParam();
        initToolbar();
    }
    private void requestPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQ);
        }
    }
    private boolean checkPermissionIsGranted()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        }
    }
    private void initToolbar() {
        Toolbar toolbar = binding.getRoot().findViewById(R.id.toolbars).findViewById(R.id.toolbar);     //(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle(getString(R.string.gallery));
    }
    @SuppressLint("Range")
    public void getImg()
    {
        try {
            ArrayList<Uri> galleryImages = new ArrayList<>();
            galleryImages.add(Uri.parse("dfgd"));
            galleryImages.add(Uri.parse("dsfdds"));
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns,null,null,orderBy);
            for (int i = 0; i < 22; i++) {
                try {
                    assert cursor != null;
                    if (i < cursor.getCount()) {
                        cursor.moveToPosition(i);
                        galleryImages.add(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ImageAdapter imageAdapter = new ImageAdapter(GalleryImagesActivity.this, galleryImages);
            binding.mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            binding.mRecyclerView.setAdapter(imageAdapter);
            imageAdapter.setOnClickListener((path, position) -> {
                if (position == 0) {
                    takePicture();
                }
                else if (position == 1) {
                    Intent pickIntent = new Intent();
                    pickIntent.setAction(Intent.ACTION_PICK);
                    pickIntent.setType("image/*");
                    startActivityForResult(pickIntent, PICK_IMAGE);
                }
                else {
                    if (path != null) {
                        startCrop(path);

                    }
                }
            });
            if (cursor != null)
                cursor.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == CAMERA_PERMISSION_REQ) {
            boolean granted = false;
            for (int result:grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    granted = true;
                } else {
                    granted = false;
                    break;
                }
            }
            if (granted) {
                getImg();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO: {
                if (resultCode == Activity.RESULT_OK) {
                    if (file != null) {
                        try {
                            startCrop(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            }
            case PICK_IMAGE: {
                if (resultCode == Activity.RESULT_OK) {
                    startCrop((Uri) data.getData());
                }
                break;
            }
            case UCrop.REQUEST_CROP: {
                if (resultCode == Activity.RESULT_OK) {
                    final Uri resultUri = UCrop.getOutput(data);
                    handleCropResult(resultUri);
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                    handleCropError(cropError);
                }
                break;
            }
        }
    }
    private void handleCropResult(Uri resultUri) {
        if (resultUri != null) {
            try {
                Intent intent = new Intent();
                intent.setData(resultUri);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(GalleryImagesActivity.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }
    private void handleCropError(Throwable cropError) {
        if (cropError != null) {
            Timber.e(cropError, "handleCropError: ");
            Toast.makeText(GalleryImagesActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(GalleryImagesActivity.this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }
    private void startCrop(@NonNull Uri uri) {
        try {
            UCrop.Options option = new UCrop.Options();
            if (cropParams.getRounded()) {
                option.setCircleDimmedLayer(cropParams.getRounded());
            } else {
                option.setShowCropFrame(false);
            }
            option.setShowCropGrid(false);
            option.setCompressionQuality(60);

            //noinspection deprecation
            option.setToolbarColor(getResources().getColor(R.color.white));
            StringBuilder destinationFileName = new StringBuilder();
            if (cropParams.getCropImageName() == null) {
                destinationFileName.append(SAMPLE_CROPPED_IMAGE_NAME);
            } else {
                destinationFileName.append(cropParams.getCropImageName());
            }
//            destinationFileName.append(".png");
            UCrop.of(uri, Uri.fromFile(new File(this.getCacheDir(), String.valueOf(destinationFileName))))
                    .withOptions(option)
                    .withAspectRatio(cropParams.getRatioX(), cropParams.getRatioY())
                    .start(GalleryImagesActivity.this, UCrop.REQUEST_CROP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", ImageUtils.geImageOutputMediaFile(this));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }
}
