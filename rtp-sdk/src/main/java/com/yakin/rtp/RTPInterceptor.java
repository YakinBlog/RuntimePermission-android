package com.yakin.rtp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RTPInterceptor implements IRTPInterceptor {

    // 需要跳过的权限
    private final static String[] GRANTED_LIST = new String[] {
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.REQUEST_DELETE_PACKAGES
    };

    private Activity mContext;
    private int mRequestCode;
    private String[] mPermissions;
    private int mPermissionsHashCode;
    private List<Integer> mNeedRequestIndexList;

    public RTPInterceptor(Activity context, int requestCode, String[] permissions) {
        mContext = context;
        mRequestCode = requestCode;
        mPermissions = permissions;
        mNeedRequestIndexList = new ArrayList<>();
        mPermissionsHashCode = RTPUtil.generateStringArrayHashCode(mPermissions);
    }

    public int getPermissionsHashCode() {
        return mPermissionsHashCode;
    }

    public Integer getRequestCode() {
        return mRequestCode;
    }

    public String[] getPermissions() {
        return mPermissions;
    }

    private String[] getNeedRequestPermissions() {
        String[] permissions = new String[mNeedRequestIndexList.size()];
        for (int i = 0; i < permissions.length; i++) {
            permissions[i] = mPermissions[mNeedRequestIndexList.get(i)];
        }
        return permissions;
    }

    private boolean isGranted(String permission) {
        for (int i = 0; i < GRANTED_LIST.length; i++) {
            if(GRANTED_LIST[i].equals(permission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkPermissions() {
        mNeedRequestIndexList.clear();
        if (!RTPUtil.isGE_M()) {
            return true;
        }
        if (mPermissions != null) {
            for (int i = 0; i < mPermissions.length; i++) {
                if (checkSelfPermission(mContext, mPermissions[i]) != PackageManager.PERMISSION_GRANTED
                        && !isGranted(mPermissions[i])) {
                    mNeedRequestIndexList.add(i);
                }
            }
        }
        boolean permissionsGranted = mNeedRequestIndexList.size() == 0;
        if (permissionsGranted) {
            // do not need to request permission, remove static and dynamic handlers
            RTPManager.getInstance().removeHandler(getRequestCode());
        }
        return permissionsGranted;
    }

    @Override
    public void requestPermissions() {
        if (mNeedRequestIndexList.size() > 0) {
            String[] permissions = getNeedRequestPermissions();
            requestPermissions(mContext, permissions, mRequestCode);
        }
    }

    private final static String METHOD_NAME_REQUEST_PERMISSIONS = "requestPermissions";
    private static Method sRequestPermissionsMethod;

    private synchronized void requestPermissions(Activity context, String[] permissions, int requestCode) {
        try {
            if (sRequestPermissionsMethod == null) {
                Class<?> clz = context.getClass();
                sRequestPermissionsMethod = clz.getMethod(METHOD_NAME_REQUEST_PERMISSIONS, String[].class, int.class);
                sRequestPermissionsMethod.setAccessible(true);
            }

            sRequestPermissionsMethod.invoke(context, new Object[]{ permissions, requestCode });
        } catch (Exception e) {
            Log.e("--RTP--", "Failed to request permissions", e);
        }
    }

    private final static String METHOD_NAME_CHECK_SELF_PERMISSION = "checkSelfPermission";
    private static Method sCheckSelfPermissionMethod;

    private synchronized int checkSelfPermission(Activity context, String permission) {
        int result;
        try {
            if (sCheckSelfPermissionMethod == null) {
                Class<?> clz = context.getClass();
                sCheckSelfPermissionMethod = clz.getMethod(METHOD_NAME_CHECK_SELF_PERMISSION, String.class);
                sCheckSelfPermissionMethod.setAccessible(true);
            }
            result = (Integer) sCheckSelfPermissionMethod.invoke(context, new Object[] {permission});
        } catch (Exception e) {
            Log.e("--RTP--", "Failed to check permission", e);
            return PackageManager.PERMISSION_DENIED;
        }

        return result;
    }
}
