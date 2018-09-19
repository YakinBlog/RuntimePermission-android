package com.yakin.rtp;

import android.app.Activity;

public class RTPManager {

    private RTPManager(){ }

    private static class InstanceHolder{
        private static RTPManager sInstance = new RTPManager();
    }

    public static RTPManager getInstance(){
        return InstanceHolder.sInstance;
    }

    private RTPProxy mProxy = new RTPProxy();

    public void requestPermissions(Activity context, String[] permissions) {
        mProxy.requestPermissions(context, permissions, null);
    }

    public void requestPermission(Activity context, String permission, IRTPGrantCallback callback) {
        mProxy.requestPermissions(context, new String[]{ permission }, callback);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mProxy.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
