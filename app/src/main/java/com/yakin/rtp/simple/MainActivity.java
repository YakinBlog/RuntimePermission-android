package com.yakin.rtp.simple;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yakin.rtp.IRTPGrantCallback;
import com.yakin.rtp.RTPManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RTPManager.requestPermissions(MainActivity.this, Manifest.permission.RECORD_AUDIO, new IRTPGrantCallback() {

                    @Override
                    public void onPermissionGranted() {
                        Log.d("--RTP--", "onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied() {
                        Log.d("--RTP--", "onPermissionDenied");
                    }
                });
            }
        });

        findViewById(R.id.file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RTPManager.requestPermissions(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new IRTPGrantCallback() {

                    @Override
                    public void onPermissionGranted() {
                        Log.d("--RTP--", "onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied() {
                        Log.d("--RTP--", "onPermissionDenied");
                    }
                });
            }
        });
        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RTPManager.requestPermissions(MainActivity.this, Manifest.permission.CAMERA, new IRTPGrantCallback() {

                    @Override
                    public void onPermissionGranted() {
                        Log.d("--RTP--", "onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied() {
                        Log.d("--RTP--", "onPermissionDenied");
                    }
                });
            }
        });
    }
}
