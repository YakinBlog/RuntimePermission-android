package com.yakin.rtp;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.pm.PackageManager;

import com.yakin.rtp.processor.AudioProcessor;
import com.yakin.rtp.processor.IProcessor;

import java.util.ArrayList;
import java.util.List;

class RTPHandlerImpl implements IRTPInternalHandler, IRTPGrantHandler {

    private String FRAGMENT_TAG = "RTP";

    private RTPFragment mFragment;

    private IRTPGrantHandler mGrantHandler;
    private List<Permission> mDenyList;

    public RTPHandlerImpl(Activity activity) {
        mFragment = findRTPFragment(activity);
        boolean needNewInstance = mFragment == null;
        if (needNewInstance) {
            mFragment = new RTPFragment();
            mFragment.setInternalHandler(this);
            FragmentManager fragmentMgr = activity.getFragmentManager();
            fragmentMgr.beginTransaction()
                    .add(mFragment, FRAGMENT_TAG)
                    .commitAllowingStateLoss();
            fragmentMgr.executePendingTransactions();
        }
    }

    private RTPFragment findRTPFragment(Activity activity) {
        return (RTPFragment) activity.getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    @Override
    public boolean isGranted(String permission) {
        return mFragment.isGranted(permission);
    }

    @Override
    public boolean isRevoked(String permission) {
        return mFragment.isRevoked(permission);
    }

    @Override
    public void requestPermission(String permission, IRTPGrantHandler handler) {
        requestPermissions(new String[]{ permission }, handler);
    }

    @Override
    public void requestPermissions(String[] permissions) {
        requestPermissions(permissions, null);
    }

    @Override
    public void requestPermissions(String[] permissions, IRTPGrantHandler handler) {
        mGrantHandler = handler;
        mDenyList = new ArrayList<>();
        List<String> unrequestedList = new ArrayList<>();
        for (String permission : permissions) {
            if(isGranted(permission)) {
                continue;
            }
            if (isRevoked(permission)) {
                mDenyList.add(new Permission(permission, false));
                continue;
            }

            unrequestedList.add(permission);
        }

        if (!unrequestedList.isEmpty()) {
            String[] unrequestedArray = unrequestedList.toArray(new String[unrequestedList.size()]);
            mFragment.requestPermissions(unrequestedArray);
            unrequestedList.clear();
        } else if(!mDenyList.isEmpty()) {
            onPermissionDenied(mDenyList.toArray(new Permission[mDenyList.size()]));
            mDenyList.clear();
        } else {
            onPermissionGranted();
        }
    }

    @Override
    public void runAudioPermission(final IRTPGrantHandler handler) {
        requestPermission(Manifest.permission.RECORD_AUDIO, new IRTPGrantHandler() {
            @Override
            public void onPermissionGranted() {
                IProcessor processor = new AudioProcessor();
                processor.runPermission(handler);
            }

            @Override
            public void onPermissionDenied(Permission[] permissions) {
                if(handler != null) {
                    handler.onPermissionDenied(permissions);
                }
            }
        });
    }

    @Override
    public void handlePermissionsResult(String[] permissions, int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
        for (int i = 0, size = permissions.length; i < size; i++) {
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                mDenyList.add(new Permission(permissions[i], shouldShowRequestPermissionRationale[i]));
            }
        }
        if(!mDenyList.isEmpty()) {
            onPermissionDenied(mDenyList.toArray(new Permission[mDenyList.size()]));
            mDenyList.clear();
        } else {
            onPermissionGranted();
        }
    }

    @Override
    public void onPermissionGranted() {
        if(mGrantHandler != null) {
            mGrantHandler.onPermissionGranted();
        }
    }

    @Override
    public void onPermissionDenied(Permission[] permissions) {
        if(mGrantHandler != null) {
            mGrantHandler.onPermissionDenied(permissions);
        }
    }
}
