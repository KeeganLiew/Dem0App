package com.keegan.experiment.utilities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.keegan.experiment.Global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by keegan on 21/01/16.
 */
public class DisplayPictureUtil {

    private static final String TAG = "DisplayPictureUtil";

    public static Intent performCrop(String picUri, int size) {
        try {
            //Start Crop Activity
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            Log.d(TAG, "size: " + size);
            cropIntent.putExtra("outputX", size);
            cropIntent.putExtra("outputY", size);

            cropIntent.putExtra("scale", true);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            return cropIntent;
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            return null;
        }
    }

    public static Bitmap performCircleCrop(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public static String saveToInternalStorage(ContextWrapper cw, Bitmap bitmapImage, String fileName) {
        // path to /data/data/yourapp/app_data/imageDir
        // Create imageDir
        File directory = cw.getDir(Global.profileImgDirName, Context.MODE_PRIVATE);
        File mypath = new File(directory, fileName);
        Log.d(TAG, "saving as: " + mypath.getAbsolutePath());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            Log.d(TAG, "bitmapImage null?: " + bitmapImage);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static void deleteImageFromStorage(String path, String fileName) {
        try {
            File f = new File(path, fileName);
            Log.d(TAG, "delete image as: " + f.getAbsolutePath());
            if (f.exists()) {
                boolean deleted = f.delete();
                Log.d(TAG, f.getAbsolutePath() + " is deleted? " + deleted);
            } else {
                Log.d(TAG, "No profile picture saved");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean loadImageFromStorage(ImageView nav_display_picture, String path, String fileName) {
        try {
            File f = new File(path, fileName);
            Log.d(TAG, "Loading image as: " + f.getAbsolutePath());
            if (f.exists()) {
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                nav_display_picture.setImageBitmap(b);
                return true;
            } else {
                Log.d(TAG, "No profile picture saved");
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap getDisplayPictureFromStorage(String path, String fileName) {
        Bitmap b = null;
        try {
            File f = new File(path, fileName);
            Log.d(TAG, "getting display pic as: " + f.getAbsolutePath());
            if (f.exists()) {
                b = BitmapFactory.decodeStream(new FileInputStream(f));
            } else {
                Log.d(TAG, "No profile picture saved");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static void backUpDisplayPictureFromStorage(ContextWrapper cw) {
        try {
            File directory = cw.getDir(Global.profileImgDirName, Context.MODE_PRIVATE);
            File f = new File(directory, Global.profilePicImgName);
            Log.d(TAG, "Backing as: " + f.getAbsolutePath());
            if (f.exists()) {
                Bitmap bitmapImage = getDisplayPictureFromStorage(directory.getPath(), Global.profilePicImgName);
                if (bitmapImage != null) {
                    saveToInternalStorage(cw, bitmapImage, Global.prevProfileImgName);
                    Log.d(TAG, "Backed up profile pic");
                } else {
                    Log.d(TAG, "Error getting profile picture");
                }
            } else {
                Log.d(TAG, "No profile picture saved");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
