package com.yakin.rtp;

import android.Manifest;
import android.os.Build;

public class RTPUtil {

    static int sID = 0x00000000;
    static synchronized int generateID() {
        // synchronized ensure method is thread-safe
        // requestCode should be less than 0xffff，because of FragmentActivity use the low 16 bits
        return (sID++) % 0xffff;
    }

    static boolean isGE_M() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    static final String[] EXCLUDE_LIST = new String[] {
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.REQUEST_DELETE_PACKAGES
    };
    static boolean isExcluded(String permission) {
        for (String exclude : EXCLUDE_LIST) {
            if(exclude.equals(permission)) {
                return true;
            }
        }
        return false;
    }
}
