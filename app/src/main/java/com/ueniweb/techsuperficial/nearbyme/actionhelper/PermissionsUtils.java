package com.ueniweb.techsuperficial.nearbyme.actionhelper;

import android.app.Activity;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PermissionsUtils {
    public static boolean checkReadStoragePermission(Activity activity) {
        boolean z = true;
        if (Build.VERSION.SDK_INT < 16) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            z = false;
        }
        if (!z) {
            ActivityCompat.requestPermissions(activity, PermissionsConstant.PERMISSIONS_LOCATION, 2);
        }
        return z;
    }

    public static boolean checkLocationPermission(Activity activity) {
        boolean z = ContextCompat.checkSelfPermission(activity.getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION") == 0;
        if (!z) {
            ActivityCompat.requestPermissions(activity, PermissionsConstant.PERMISSIONS_LOCATION, 1);
        }
        return z;
    }

    public static boolean checkFineLocation(Activity activity) {
        boolean z = ContextCompat.checkSelfPermission(activity.getApplicationContext(), "android.permission.ACCESS_COARSE_LOCATION") == 0;
        if (!z) {
            ActivityCompat.requestPermissions(activity, PermissionsConstant.PERMISSIONS_FINE_LOCATION, 3);
        }
        return z;
    }
}
