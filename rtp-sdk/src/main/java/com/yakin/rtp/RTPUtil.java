package com.yakin.rtp;

import android.Manifest;

public class RTPUtil {

    private static String[] GRANTED = new String[] {
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.REQUEST_DELETE_PACKAGES
    };

    public static boolean ifGranted(String permission) {
        for (int i = 0; i < GRANTED.length; i ++) {
            if(GRANTED[i].equals(permission)) {
                return true;
            }
        }
        return false;
    }
}
