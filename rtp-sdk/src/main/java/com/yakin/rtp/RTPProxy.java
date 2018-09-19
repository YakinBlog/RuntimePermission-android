package com.yakin.rtp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.yakin.rtp.processor.AudioProcessor;

import java.lang.ref.WeakReference;

public class RTPProxy {

    private final static int PERMISSION = 9999;

    private WeakReference<Activity> mContextWR;
    private IRTPGrantCallback mCallback;
    private String[] mPermissions;
    private String mPermission;
    private int mIndex;

    void requestPermissions(Activity context, String[] permissions, IRTPGrantCallback callback) {
        mIndex = 0;
        mCallback = callback;
        mPermissions = permissions;
        mContextWR = new WeakReference<>(context);
        loopRequestPermission();
    }

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == PERMISSION) {
            if(mCallback != null) {
                if(grantResults.length == 0 || permissions.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    mCallback.onPermissionDenied(mPermission);
                } else if(Manifest.permission.RECORD_AUDIO.equals(permissions[0])) {
                    AudioProcessor.checkPermission(mCallback);
                } else {
                    mCallback.onPermissionGranted(mPermission);
                }
            }
            loopRequestPermission();
        }
    }

    private void loopRequestPermission() {
        if(mContextWR != null && mContextWR.get() != null &&
                mPermissions != null && mIndex < mPermissions.length) {
            mPermission = mPermissions[mIndex ++];
            if (needRTP()) {
                if(RTPUtil.ifGranted(mPermission) && mCallback != null) {
                    mCallback.onPermissionGranted(mPermission);
                } else if(checkAndRequestPermission(mContextWR.get(), mPermission)) {
                    return;
                } else if(Manifest.permission.RECORD_AUDIO.equals(mPermission)) {
                    AudioProcessor.checkPermission(mCallback);
                } else if(mCallback != null) {
                    mCallback.onPermissionGranted(mPermission);
                }
            } else if(mCallback != null) {
                if(Manifest.permission.RECORD_AUDIO.equals(mPermission)) {
                    AudioProcessor.checkPermission(mCallback);
                } else {
                    mCallback.onPermissionGranted(mPermission);
                }
            }
            loopRequestPermission();
        }
    }

    private boolean needRTP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private boolean checkPermission(Activity context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkAndRequestPermission(Activity context, String permission) {
        if(!checkPermission(context, permission)) {
            ActivityCompat.requestPermissions(context, new String[]{ permission }, PERMISSION);
            return true;
        }
        return false;
    }
}
