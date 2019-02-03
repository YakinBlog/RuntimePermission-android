package com.yakin.rtp;

import android.app.Activity;

public class RTPManager implements IRTPHandler {

    private IRTPHandler mHandler;

    private RTPManager(Activity activity) {
        mHandler = new RTPHandlerImpl(activity);
    }

    public static IRTPHandler get(Activity activity) {
        return new RTPManager(activity);
    }

    @Override
    public boolean isGranted(String permission) {
        return mHandler.isGranted(permission);
    }

    @Override
    public boolean isRevoked(String permission) {
        return mHandler.isRevoked(permission);
    }

    @Override
    public void requestPermission(String permission, IRTPGrantHandler handler) {
        mHandler.requestPermission(permission, handler);
    }

    @Override
    public void requestPermissions(String[] permissions) {
        mHandler.requestPermissions(permissions);
    }

    @Override
    public void requestPermissions(String[] permissions, IRTPGrantHandler handler) {
        mHandler.requestPermissions(permissions, handler);
    }

    @Override
    public void runAudioPermission(IRTPGrantHandler handler) {
        mHandler.runAudioPermission(handler);
    }
}
