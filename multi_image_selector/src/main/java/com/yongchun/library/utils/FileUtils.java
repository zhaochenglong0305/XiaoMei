package com.yongchun.library.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by dee on 15/11/20.
 */
public class FileUtils {
    public static final String POSTFIX = ".jpg";
    public static final String APP_NAME = "WineSocial";
    public static final String CAMERA_PATH = "/" + APP_NAME + "/CameraImage/";
    public static final String CROP_PATH = "/" + APP_NAME + "/CropImage/";

    public static File createCameraFile(Context context) {
        return createMediaFile(context, CAMERA_PATH);
    }

    public static File createCropFile(Context context) {
        return createMediaFile(context, CROP_PATH);
    }

    private static File createMediaFile(Context context, String parentPath) {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(MEDIA_MOUNTED) ? Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) : getCacheDirectory(context, true);

        File folderDir = new File(rootDir.getAbsolutePath() + parentPath);
        if (!folderDir.exists()) {
            folderDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String fileName = APP_NAME + "_" + timeStamp + "";
        File tmpFile = new File(folderDir, fileName + POSTFIX);
        return tmpFile;
    }

    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) { // (sh)it happens too (Issue #989)
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}
