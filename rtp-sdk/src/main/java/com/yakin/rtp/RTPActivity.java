package com.yakin.rtp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.yakin.rtp.processor.AudioProcessor;

public class RTPActivity extends Activity {

    private final static String REQUEST_CODE = "permission_code";
    private final static String REQUEST_PERMISSION = "permission";

    private final static int PERMISSION = 100;
    private final static int PERMISSIONS = 101;

    private static IRTPGrantCallback sCallback;

    private IRTPGrantCallback InnerCallback = new IRTPGrantCallback() {
        @Override
        public void onPermissionGranted() {
            finish();
            if(sCallback != null) {
                sCallback.onPermissionGranted();
            }
        }

        @Override
        public void onPermissionDenied() {
            finish();
            if(sCallback != null) {
                sCallback.onPermissionDenied();
            }
        }
    };

    private String[] mPermissions;
    private int mIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int code = intent.getIntExtra(REQUEST_CODE, -1);
        if(code == PERMISSION) {
            String permission = intent.getStringExtra(REQUEST_PERMISSION);
            if (!checkPermission(permission)) {
                requestPermission(PERMISSION, permission);
            } else if (Manifest.permission.RECORD_AUDIO.equals(permission)) {
                AudioProcessor.checkPermission(InnerCallback);
            } else {
                InnerCallback.onPermissionGranted();
            }
            return;
        } else if(code == PERMISSIONS) {
            mPermissions = intent.getStringArrayExtra(REQUEST_PERMISSION);
            loopCheckPermissions();
            return;
        }
        finish();
    }

    private void loopCheckPermissions() {
        if(mPermissions == null || mIndex >= mPermissions.length) {
            finish();
        } else if (!checkPermission(mPermissions[mIndex])) {
            requestPermission(PERMISSIONS, mPermissions[mIndex++]);
        } else {
            mIndex ++;
            loopCheckPermissions();
        }
    }

    private void requestPermission(int code, String permission) {
        ActivityCompat.requestPermissions(this,new String[]{ permission }, code);
    }

    private boolean checkPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION) {
            if(grantResults.length == 0 || permissions.length == 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                InnerCallback.onPermissionDenied();
            } else if(Manifest.permission.RECORD_AUDIO.equals(permissions[0])) {
                AudioProcessor.checkPermission(InnerCallback);
            } else {
                InnerCallback.onPermissionGranted();
            }
        } else if(requestCode == PERMISSIONS) {
            loopCheckPermissions();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    static void startActivity(Context context, String permission, IRTPGrantCallback callback) {
        sCallback = callback;
        Intent intent = new Intent(context, RTPActivity.class);
        intent.putExtra(REQUEST_PERMISSION, permission);
        intent.putExtra(REQUEST_CODE, PERMISSION);
        context.startActivity(intent);
    }

    static void startActivity(Context context, String[] permissions) {
        Intent intent = new Intent(context, RTPActivity.class);
        intent.putExtra(REQUEST_PERMISSION, permissions);
        intent.putExtra(REQUEST_CODE, PERMISSIONS);
        context.startActivity(intent);
    }
}
