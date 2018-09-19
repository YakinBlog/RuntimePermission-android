package com.yakin.rtp;

public interface IRTPGrantCallback {

    void onPermissionGranted(String permission);
    void onPermissionDenied(String permission);
}
