package com.yakin.rtp;

public interface IRTPGrantHandler {

    void onPermissionGranted();

    void onPermissionDenied(Permission[] permissions);
}
