package com.yakin.rtp;

interface IRTPProxyHandler {

    IRTPInterceptor getIntercepotor();

    void requestPermissions();
}
