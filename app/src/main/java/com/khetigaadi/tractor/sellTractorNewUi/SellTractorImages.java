package com.khetigaadi.tractor.sellTractorNewUi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
//import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.khetigaadi.R;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.databinding.FragmentTractorImagesBinding;
import com.khetigaadi.pick_image.CropParam;
import com.khetigaadi.pick_image.GalleryImagesActivity;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.util.ImageUtils;
//import com.watermark.androidwm_light.WatermarkBuilder;
//import com.watermark.androidwm_light.bean.WatermarkText;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class SellTractorImages extends BaseFragment {
  //  private static final String TAG = SellTractorImages.class.getSimpleName();
    int imageIndex;
    private SellViewModel viewModel;
    @Inject
    KhetiAPIs khetiAPIs;
    private FragmentTractorImagesBinding binding;
    private FragmentActivity activity;
    @SuppressWarnings("CallToPrintStackTrace")
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        try {
                            Glide.with(activity).asBitmap().load(data.getData()).into(new CustomTarget<Bitmap>() {
                                @SuppressLint("LogNotTimber")
                                @Override
                                public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                                    //resource = addWaterMark(resource);
                                    try {
                                        handleImageResult(ImageUtils.getUriFromBitmap(activity, resource));
                                    }
                                    catch(IOException e) {
                                        Log.d("dfsdsdsfdsfs===", e.getLocalizedMessage());
                                        //noinspection CallToPrintStackTrace
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
                                }
                                @Override
                                public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                    handleImageResult(data.getData());
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(activity,getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    public static Fragment getInstance() {
        return new SellTractorImages();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentTractorImagesBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(activity).get(SellViewModel.class);
        binding.ivFirst.setOnClickListener(v -> {
            try {
                imageIndex = 0;
                displayImageSelectionDialog();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });
        binding.ivSecond.setOnClickListener(v -> {
            try {
                imageIndex = 1;
                displayImageSelectionDialog();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });
        binding.ivThird.setOnClickListener(v -> {
            try {
                imageIndex = 2;
                displayImageSelectionDialog();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });
        binding.ivFourth.setOnClickListener(v -> {
            try {
                imageIndex = 3;
                displayImageSelectionDialog();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });
        binding.ivFifth.setOnClickListener(v -> {
            try {
                imageIndex = 4;
                displayImageSelectionDialog();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });
        binding.ivSixth.setOnClickListener(v -> {
            try {
                imageIndex = 5;
                displayImageSelectionDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.btnImageNext.setOnClickListener(v -> {
            //Temp commented "If" condition
            if (viewModel.getImages().getRightImage() != null && viewModel.getImages().getFrontSideImage() != null) {
                ((SellTractorActivity) activity).callFragments(4);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.please_add_two_images), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    private void initUI() {
        Glide.with(activity)
                .load(viewModel.getImages().getRightImage() != null ? viewModel.getImages().getRightImage() : "")
                .error(R.drawable.ic_camara)
                .into(binding.ivFirst);
        Glide.with(activity)
                .load(viewModel.getImages().getFrontSideImage() != null ? viewModel.getImages().getFrontSideImage() : "")
                .error(R.drawable.ic_camara)
                .into(binding.ivSecond);
        Glide.with(activity)
                .load(viewModel.getImages().getLeftImage() != null ? viewModel.getImages().getLeftImage() : "")
                .error(R.drawable.ic_camara)
                .into(binding.ivThird);
        Glide.with(activity)
                .load(viewModel.getImages().getBackSideImage() != null ? viewModel.getImages().getBackSideImage() : "")
                .error(R.drawable.ic_camara)
                .into(binding.ivFourth);
        Glide.with(activity)
                .load(viewModel.getImages().getFrontTyreImage() != null ? viewModel.getImages().getFrontTyreImage() : "")
                .error(R.drawable.ic_camara)
                .into(binding.ivFifth);
        Glide.with(activity)
                .load(viewModel.getImages().getBackTyreImage() != null ? viewModel.getImages().getBackTyreImage() : "")
                .error(R.drawable.ic_camara)
                .into(binding.ivSixth);
    }
    private void displayImageSelectionDialog() {
        CropParam cropParams = new CropParam();
        cropParams.setCropImageName(System.currentTimeMillis() + ".png");
        Intent intent = new Intent(activity, GalleryImagesActivity.class);
        intent.putExtra("cropParams", cropParams);
        galleryActivityResultLauncher.launch(intent);
    }
//    private Bitmap addWaterMark(Bitmap backgroundBitmap) {
//        double  positionX = 0.4, positionY = 0.8;
//        boolean landscape = backgroundBitmap.getWidth() > backgroundBitmap.getHeight();
//        if (landscape) {
//            backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap,960,640,true);
//        } else {
//            backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap,640,960,true);
//        }
//        return WatermarkBuilder
//                .create(activity,backgroundBitmap)
//                .loadWatermarkText(new WatermarkText(getString(R.string.app_name))
//                        .setPositionX(positionX)
//                        .setPositionY(positionY)
//                        .setTextColor(Color.WHITE)
//                        .setTextAlpha(400)
//                        .setTextSize(10))
//                .setTileMode(false) // select different drawing mode.
//                .getWatermark()
//                .getOutputImage();
//    }
    private void handleImageResult(Uri uriFromBitmap) {
        if (imageIndex == 0) {
            binding.ivFirst.setImageURI(uriFromBitmap);
            viewModel.setImage1(uriFromBitmap);
        } else if (imageIndex == 1) {
            binding.ivSecond.setImageURI(uriFromBitmap);
            viewModel.setImage2(uriFromBitmap);
        } else if (imageIndex == 2) {
            binding.ivThird.setImageURI(uriFromBitmap);
            viewModel.setImage3(uriFromBitmap);
        } else if (imageIndex == 3) {
            binding.ivFourth.setImageURI(uriFromBitmap);
            viewModel.setImage4(uriFromBitmap);
        } else if (imageIndex == 4) {
            binding.ivFifth.setImageURI(uriFromBitmap);
            viewModel.setImage5(uriFromBitmap);
        } else if (imageIndex == 5) {
            binding.ivSixth.setImageURI(uriFromBitmap);
            viewModel.setImage6(uriFromBitmap);
        }
    }
    @SuppressLint("LogNotTimber")
    @Override
    public void onResume() {
        super.onResume();
        ((SellTractorActivity) activity).iconColorChange(3);
        initUI();
        Log.d("callicon====","");
    }
}
