package com.yakin.rtp;

public interface IRTPHandler {

    boolean isGranted(String permission);

    boolean isRevoked(String permission);

    void requestPermission(String permission, IRTPGrantHandler handler);

    void requestPermissions(String[] permissions);

    void requestPermissions(String[] permissions, IRTPGrantHandler handler);

    void runAudioPermission(IRTPGrantHandler handler);
}
