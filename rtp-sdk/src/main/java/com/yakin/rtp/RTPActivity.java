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

    private final static String REQUEST_PERMISSION = "permission";

    private final static int PERMISSION = 999;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String permission = intent.getStringExtra(REQUEST_PERMISSION);
        if(!checkPermission(permission)) {
            requestPermissions(permission);
        } else if(Manifest.permission.RECORD_AUDIO.equals(permission)) {
            AudioProcessor.checkPermission(InnerCallback);
        } else {
            InnerCallback.onPermissionGranted();
        }
    }

    private void requestPermissions(String permission) {
        ActivityCompat.requestPermissions(this,new String[]{ permission }, PERMISSION);
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
        context.startActivity(intent);
    }
}
