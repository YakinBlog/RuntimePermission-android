package com.yakin.rtp;

public class RTPProxyHandler implements IRTPProxyHandler, IRTPGrantHandler {

    private IRTPInterceptor mIntercepotor;
    private IRTPGrantHandler mEntity;

    public RTPProxyHandler(IRTPInterceptor interceptor, IRTPGrantHandler entity) {
        this.mIntercepotor = interceptor;
        this.mEntity = entity;
    }

    @Override
    public IRTPInterceptor getIntercepotor() {
        return mIntercepotor;
    }

    @Override
    public void requestPermissions() {
        if(mIntercepotor != null) {
            if(!mIntercepotor.checkPermissions()) {
                mIntercepotor.requestPermissions();
            } else {
                onPermissionGranted();
            }
        }
    }

    @Override
    public void onPermissionGranted() {
        if(mEntity != null) {
            mEntity.onPermissionGranted();
        }
    }

    @Override
    public void onPermissionDenied(String[] permissions) {
        if(mEntity != null) {
            mEntity.onPermissionDenied(permissions);
        }
    }
}
