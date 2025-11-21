package com.khetigaadi.pick_image;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
//import com.khetigaadi.pick_image.databinding.ActivityGalleryImagesBinding;
//import com.khetigaadi.R;
//import com.khetigaadi.databinding.ActivityGalleryImagesBinding;
//import com.khetigaadi.pick_image.databinding.ActivityGalleryImagesBinding;
import com.khetigaadi.pick_image.databinding.ActivityGalleryImagesBinding;
import java.util.ArrayList;
public class GalleryVideoActivity extends AppCompatActivity {
    public static final String VIDEO_RESULT_KEY = "videoUri";
    private static final int REQUEST_PERMISSIONS = 76;
    private static final int REQUEST_TAKE_VIDEO = 44;
    private static final int PICK_VIDEO = 3;
    ArrayList<Video> videos = new ArrayList<>();
    private Uri file;
    private ActivityGalleryImagesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityGalleryImagesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        EdgeToEdge.enable(this);
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
        videos.add(new Video());
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
        initToolbar();
        checkPermission();
    }
    private void initToolbar()
    {
        Toolbar toolbar = binding.getRoot().findViewById (R.id.toolbar);  // findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle(getString(R.string.video));
    }
    private void takeVideo()
    {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,30);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        file = FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName() + ".provider", ImageUtils.getVideoOutputMediaFile(this));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,file);
        startActivityForResult(intent,REQUEST_TAKE_VIDEO);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_TAKE_VIDEO) {
            if(resultCode == Activity.RESULT_OK) {
                if (file != null) {
                    Intent intent = new Intent();
                    intent.setData(file);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        }
        else if(requestCode == PICK_VIDEO) {
            if(resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent();
                intent.setData(data.getData());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }
    private void checkPermission()
    {
        /*RUN TIME PERMISSIONS*/
        if((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if((ActivityCompat.shouldShowRequestPermissionRationale(GalleryVideoActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(GalleryVideoActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(GalleryVideoActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSIONS);
            }
        } else {
            fetchVideo();
        }
    }
    @SuppressLint("Range")
    public void fetchVideo()
    {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name, column_id, thum;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA,MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media._ID,MediaStore.Video.Thumbnails.DATA};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        String duration = Integer.toString(30 * 1000);
        cursor = getApplicationContext().getContentResolver().query(uri,projection,MediaStore.Video.VideoColumns.DURATION + " < ?",new String[]{duration},orderBy + " DESC");
        while(cursor.moveToNext()) {
            Video video = new Video();
            video.setUri(ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))));
            videos.add(video);
        }
        VideoAdapter imageAdapter = new VideoAdapter(GalleryVideoActivity.this,videos);
        binding.mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        binding.mRecyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnClickListener((item,position) -> {
            if (position == 0) {
                takeVideo();
            } else if (position == 1) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("video/*");
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,30);
                //noinspection deprecation
                startActivityForResult(intent,PICK_VIDEO);
            } else {
                Intent intent = new Intent();
                intent.setData(item.getUri());
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        cursor.close();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        fetchVideo();
                    } else {
                        Toast.makeText(GalleryVideoActivity.this, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            }
            case 0: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
            }
        }
    }
}
