package com.yakin.rtp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import com.yakin.rtp.processor.AudioProcessor;
import com.yakin.rtp.processor.IProcessor;

import java.util.ArrayList;
import java.util.List;

public class RTPManager {

    private RTPManager(){ }

    private static class InstanceHolder{
        private static RTPManager sInstance = new RTPManager();
    }

    public static RTPManager getInstance(){
        return InstanceHolder.sInstance;
    }

    private SparseArray<IRTPProxyHandler> mProxyHandlerMap = new SparseArray<>();

    public void requestPermission(Activity context, String permission, IRTPGrantHandler handler) {
        requestPermissions(context, new String[]{ permission }, handler);
    }

    public void requestPermissions(Activity context, String[] permissions, IRTPGrantHandler handler) {
        int requestId = RTPUtil.generateID();
        IRTPInterceptor interceptor = new RTPInterceptor(context, requestId, permissions);
        IRTPProxyHandler proxyHandler = new RTPProxyHandler(interceptor, handler);
        addHandler(requestId, proxyHandler);
        proxyHandler.requestPermissions();
    }

    private boolean isException(String[] permissions, int[] grantResults) {
        return permissions == null || permissions.length == 0 || grantResults == null || grantResults.length == 0;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        List<IRTPProxyHandler> list = getHandlers(requestCode);
        if (list == null || list.isEmpty()) {
            return;
        }
        ArrayList<Integer> removableRequestCodes = new ArrayList<>();
        for (IRTPProxyHandler handler : list) {
            if (handler != null) {
                RTPInterceptor interceptor = (RTPInterceptor) handler.getIntercepotor();
                removableRequestCodes.add(interceptor.getRequestCode());

                // 系统回调异常时，对返回值进行拒绝处理
                if (isException(permissions, grantResults)) {
                    permissions = interceptor.getPermissions();
                    if (permissions != null) {
                        grantResults = new int[permissions.length];
                        for (int i = 0, size = permissions.length; i < size; i++) {
                            grantResults[i] = PackageManager.PERMISSION_DENIED;
                        }
                    }
                }

                // 上一个步骤进行异常补救后，参数还是异常情况，则不再处理
                if (isException(permissions, grantResults)) {
                    continue;
                }

                handleResult((IRTPGrantHandler) handler, permissions, grantResults);
            }
        }

        for (int code : removableRequestCodes) {
            removeHandler(code);
        }
    }

    private void handleResult(IRTPGrantHandler handler, String[] permissions, int[] grantResults) {
        if (handler != null) {
            ArrayList<String> denyList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    denyList.add(permissions[i]);
                }
            }
            if (denyList.isEmpty()) {
                handler.onPermissionGranted();
            } else {
                handler.onPermissionDenied(denyList.toArray(new String[denyList.size()]));
            }
        }
    }

    synchronized void removeHandler(int requestCode) {
        mProxyHandlerMap.remove(requestCode);
    }

    synchronized void addHandler(int requestCode, IRTPProxyHandler handler) {
        mProxyHandlerMap.put(requestCode, handler);
    }

    synchronized List<IRTPProxyHandler> getHandlers(int requestCode) {
        IRTPProxyHandler requestHandler = mProxyHandlerMap.get(requestCode);
        int hashCode = -1;
        if (requestHandler != null) {
            RTPInterceptor interceptor = (RTPInterceptor) requestHandler.getIntercepotor();
            hashCode = interceptor.getPermissionsHashCode();
        }
        if (hashCode != -1) {
            List<IRTPProxyHandler> list = new ArrayList<>();
            int size = mProxyHandlerMap.size();
            for(int i = 0; i < size; i++) {
                IRTPProxyHandler handler = mProxyHandlerMap.valueAt(i);
                if (handler != null) {
                    RTPInterceptor interceptor = (RTPInterceptor) handler.getIntercepotor();
                    if (hashCode == interceptor.getPermissionsHashCode()) {
                        list.add(handler);
                    }
                }
            }
            return list;
        }
        return null;
    }

    public void runAudioPermission(Activity context, final IRTPGrantHandler handler) {
        requestPermission(context, Manifest.permission.RECORD_AUDIO, new IRTPGrantHandler() {
            @Override
            public void onPermissionGranted() {
                IProcessor processor = new AudioProcessor();
                processor.runPermission(handler);
            }

            @Override
            public void onPermissionDenied(String[] permissions) {
                if(handler != null) {
                    handler.onPermissionDenied(permissions);
                }
            }
        });
    }
}
