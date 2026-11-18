package com.khetigaadi.util;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.graphics.Color;
//noinspection ExifInterface
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;

import androidx.core.content.FileProvider;

import com.khetigaadi.R;
//import com.watermark.androidwm_light.WatermarkBuilder;
//import com.watermark.androidwm_light.bean.WatermarkText;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ImageUtils {
    public static File getImageOutputCacheFile(Context context) {
        File mediaStorageDir = new File(context.getCacheDir(), context.getString(R.string.app_name));
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }
    public static Uri getUriFromBitmap(Context context,@NotNull Bitmap resource) throws IOException {
        File shareImage = getImageOutputCacheFile(context);
        FileOutputStream stream = new FileOutputStream(shareImage);
        resource.compress(Bitmap.CompressFormat.PNG,100,stream);
        stream.close();
        return Uri.fromFile(shareImage);
    }
    public static Uri getUriFromBitmapForShare(@NotNull Context context,@NotNull Bitmap resource) throws IOException {
        File shareImage = com.khetigaadi.pick_image.ImageUtils.geImageOutputMediaFile(context);
        FileOutputStream stream = new FileOutputStream(shareImage);
        resource.compress(Bitmap.CompressFormat.PNG, 100,stream);
        stream.close();
        if(shareImage != null) {
            return FileProvider.getUriForFile(context.getApplicationContext(),context.getApplicationContext().getPackageName() + ".provider", shareImage);
        }
        return null;
    }

//    public static Bitmap convert(String base64Str) throws IllegalArgumentException {
//        byte[] decodedBytes = Base64.decode(
//                base64Str.substring(base64Str.indexOf(",") + 1),
//                Base64.DEFAULT
//        );
//        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//    }

//    public static String convert(Bitmap bitmap) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 40, outputStream);
//        return "data:image/png;base64," + Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
//    }


//    public static Bitmap addWaterMark(Context context, Bitmap backgroundBitmap) {
//        double positionX = 0.4, positionY = 0.8;
//        boolean landscape = backgroundBitmap.getWidth() > backgroundBitmap.getHeight();
//        if (landscape) {
//            backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, 1080, 800, true);
//        } else {
//            backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, 800, 1080, true);
//        }
//        return WatermarkBuilder
//                .create(context, backgroundBitmap)
//                .loadWatermarkText(new WatermarkText("KhetiGaadi")
//                        .setPositionX(positionX)
//                        .setPositionY(positionY)
//                        .setTextColor(Color.WHITE)
//                        .setTextAlpha(400)
//                        .setTextSize(22))
//                .setTileMode(false) // select different drawing mode.
//                .getWatermark()
//                .getOutputImage();
//    }
    /**
     * This method is responsible for solving the rotation issue if exist. Also scale the images to
     * 1024x1024 resolution
     *
     * @param context       The current context
     * @param selectedImage The Image URI
     * @return Bitmap image results
     * //@throws IOException
     */
//    public static Bitmap handleSamplingAndRotationBitmap(Context context,Uri selectedImage)
//            throws IOException {
//        int MAX_HEIGHT = 1024;
//        int MAX_WIDTH = 1024;
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
//        BitmapFactory.decodeStream(imageStream, null, options);
//        imageStream.close();
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        imageStream = context.getContentResolver().openInputStream(selectedImage);
//        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);
//        img = rotateImageIfRequired(context, img, selectedImage);
//        return img;
//    }


    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = Math.min(heightRatio, widthRatio);

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23) {
            assert input != null;
            ei = new ExifInterface(input);
        }
        else
            ei = new ExifInterface(Objects.requireNonNull(selectedImage.getPath()));

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        return switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270);
            default -> img;
        };
    }
}
