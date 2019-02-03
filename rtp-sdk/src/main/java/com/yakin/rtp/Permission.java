package com.yakin.rtp;

public class Permission {

    public final String name;
    public final boolean shouldShowRequestPermissionRationale;

    public Permission(String name, boolean shouldShowRequestPermissionRationale) {
        this.name = name;
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }

    @Override
    public String toString() {
        return "name:" + name + "[" + shouldShowRequestPermissionRationale + "]";
    }
}
