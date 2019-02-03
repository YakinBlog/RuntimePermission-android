package com.yakin.rtp;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class RTPFragment extends Fragment {

    private IRTPInternalHandler mHandler;
    private int mRequestCode;

    void setInternalHandler(IRTPInternalHandler handler) {
        mHandler = handler;
    }

    boolean isGranted(String permission) {
        return !RTPUtil.isGE_M() || (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED || RTPUtil.isExcluded(permission));
    }

    boolean isRevoked(String permission) {
        return RTPUtil.isGE_M() && getActivity().getPackageManager().isPermissionRevokedByPolicy(permission, getActivity().getPackageName());
    }

    void requestPermissions(String[] permissions) {
        if(RTPUtil.isGE_M()) {
            mRequestCode = RTPUtil.generateID();
            requestPermissions(permissions, mRequestCode);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(RTPUtil.isGE_M() && requestCode == mRequestCode) {
            boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];

            for (int i = 0; i < permissions.length; i++) {
                shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i]);
            }

            if(mHandler != null) {
                mHandler.handlePermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale);
            }
        }
    }
}
