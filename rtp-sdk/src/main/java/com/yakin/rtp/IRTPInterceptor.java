package com.yakin.rtp;

interface IRTPInterceptor {

    boolean checkPermissions();

    void requestPermissions();
}
