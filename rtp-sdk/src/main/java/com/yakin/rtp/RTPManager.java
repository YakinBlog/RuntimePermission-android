package com.yakin.rtp;

import android.content.Context;

public class RTPManager {

    public static void requestPermissions(Context context, String[] permissions) {
        RTPActivity.startActivity(context, permissions);
    }

    public static void requestPermission(Context context, String permission, IRTPGrantCallback callback) {
        RTPActivity.startActivity(context, permission, callback);
    }
}
