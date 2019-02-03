package com.yakin.rtp;

interface IRTPInternalHandler extends IRTPHandler {

    void handlePermissionsResult(String[] permissions, int[] grantResults, boolean[] shouldShowRequestPermissionRationale);
}
