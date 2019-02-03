package com.yakin.rtp.simple;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yakin.rtp.IRTPGrantHandler;
import com.yakin.rtp.IRTPHandler;
import com.yakin.rtp.Permission;
import com.yakin.rtp.RTPManager;

public class MainActivity extends AppCompatActivity {

    private TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultView = (TextView) findViewById(R.id.result);

        final IRTPHandler handler = RTPManager.get(MainActivity.this);
        findViewById(R.id.group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = new String[]{Manifest.permission.INTERNET,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.REQUEST_INSTALL_PACKAGES,
                        Manifest.permission.REQUEST_DELETE_PACKAGES};
                handler.requestPermissions(permissions, new IRTPGrantHandler() {
                    @Override
                    public void onPermissionGranted() {
                        Log.d("--RTP--", "onPermissionGranted");
                        mResultView.setText("ALL:onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied(Permission[] permissions) {
                        Log.d("--RTP--", "onPermissionDenied");
                        StringBuilder strBuilder = new StringBuilder();
                        for (int i = 0; i < permissions.length; i++) {
                            strBuilder.append(permissions[i]).append("\n");
                        }
                        mResultView.setText("ALL:onPermissionDenied:" + strBuilder.toString());
                    }
                });
            }
        });

        findViewById(R.id.internet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.requestPermission(Manifest.permission.INTERNET, new IRTPGrantHandler() {

                    @Override
                    public void onPermissionGranted() {
                        Log.d("--RTP--", "onPermissionGranted");
                        mResultView.setText("INTERNET:onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied(Permission[] permissions) {
                        Log.d("--RTP--", "onPermissionDenied");
                        mResultView.setText("INTERNET:onPermissionDenied");
                    }
                });
            }
        });

        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.requestPermission(Manifest.permission.CHANGE_WIFI_STATE, new IRTPGrantHandler() {

                    @Override
                    public void onPermissionGranted() {
                        Log.d("--RTP--", "onPermissionGranted");
                        mResultView.setText("CHANGE_WIFI_STATE:onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied(Permission[] permissions) {
                        Log.d("--RTP--", "onPermissionDenied");
                        mResultView.setText("CHANGE_WIFI_STATE:onPermissionDenied");
                    }
                });
            }
        });

        findViewById(R.id.file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new IRTPGrantHandler() {

                    @Override
                    public void onPermissionGranted() {
                        Log.d("--RTP--", "onPermissionGranted");
                        mResultView.setText("WRITE_EXTERNAL_STORAGE:onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied(Permission[] permissions) {
                        Log.d("--RTP--", "onPermissionDenied");
                        mResultView.setText("WRITE_EXTERNAL_STORAGE:onPermissionDenied");
                    }
                });
            }
        });

        findViewById(R.id.phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.requestPermission(Manifest.permission.READ_PHONE_STATE, new IRTPGrantHandler() {

                    @Override
                    public void onPermissionGranted() {
                        Log.d("--RTP--", "onPermissionGranted");
                        mResultView.setText("READ_PHONE_STATE:onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied(Permission[] permissions) {
                        Log.d("--RTP--", "onPermissionDenied");
                        mResultView.setText("READ_PHONE_STATE:onPermissionDenied");
                    }
                });
            }
        });

        findViewById(R.id.record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handler.requestPermission(Manifest.permission.RECORD_AUDIO, new IRTPGrantHandler() {
//
//                    @Override
//                    public void onPermissionGranted() {
//                        Log.d("--RTP--", "onPermissionGranted");
//                        mResultView.setText("RECORD_AUDIO:onPermissionGranted");
//                    }
//
//                    @Override
//                    public void onPermissionDenied(Permission[] permissions) {
//                        Log.d("--RTP--", "onPermissionDenied");
//                        mResultView.setText("RECORD_AUDIO:onPermissionDenied");
//                    }
//                });
                handler.runAudioPermission(new IRTPGrantHandler() {

                    @Override
                    public void onPermissionGranted() {
                        Log.d("--RTP--", "onPermissionGranted");
                        mResultView.setText("RECORD_AUDIO:onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied(Permission[] permissions) {
                        Log.d("--RTP--", "onPermissionDenied");
                        mResultView.setText("RECORD_AUDIO:onPermissionDenied");
                    }
                });
            }
        });

        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.requestPermission(Manifest.permission.CAMERA, new IRTPGrantHandler() {

                    @Override
                    public void onPermissionGranted() {
                        Log.d("--RTP--", "onPermissionGranted");
                        mResultView.setText("CAMERA:onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied(Permission[] permissions) {
                        Log.d("--RTP--", "onPermissionDenied");
                        mResultView.setText("CAMERA:onPermissionDenied");
                    }
                });
            }
        });

        findViewById(R.id.install).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                File apkFile = new File("/sdcard/app-debug.apk");
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//                startActivity(intent);
                handler.requestPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES, new IRTPGrantHandler() {

                    @Override
                    public void onPermissionGranted() {
                        Log.d("--RTP--", "onPermissionGranted");
                        mResultView.setText("INSTALL_PACKAGES:onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied(Permission[] permissions) {
                        Log.d("--RTP--", "onPermissionDenied");
                        mResultView.setText("INSTALL_PACKAGES:onPermissionDenied");
                    }
                });
            }
        });

        findViewById(R.id.uninstall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri = Uri.fromParts("package", "com.yakin.rtp.simple", null);
//                Intent intent = new Intent(Intent.ACTION_DELETE, uri);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                handler.requestPermission(Manifest.permission.REQUEST_DELETE_PACKAGES, new IRTPGrantHandler() {

                    @Override
                    public void onPermissionGranted() {
                        Log.d("--RTP--", "onPermissionGranted");
                        mResultView.setText("DELETE_PACKAGES:onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied(Permission[] permissions) {
                        Log.d("--RTP--", "onPermissionDenied");
                        mResultView.setText("DELETE_PACKAGES:onPermissionDenied");
                    }
                });
            }
        });
    }
}
