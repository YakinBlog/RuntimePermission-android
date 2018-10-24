package com.yakin.rtp;

import android.os.Build;

public class RTPUtil {

    public static boolean printLog = false;

    private static int sID = 0x00000000;
    static synchronized int generateID() {
        // synchronized ensure method is thread-safe
        // requestCode should be less than 0xffff，because of FragmentActivity use the low 16 bits
        return (sID++) % 0xffff;
    }

    static int generateStringArrayHashCode(String[] array) {
        if (array != null && array.length > 0) {
            int result = 1;
            int prime = 97;
            for (int i = 0; i < array.length; i ++) {
                result = prime * result + ((array[i] == null) ? 0 : array[i].hashCode());
            }
            return result;
        }
        return -1;
    }

    static boolean isGE_M() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
