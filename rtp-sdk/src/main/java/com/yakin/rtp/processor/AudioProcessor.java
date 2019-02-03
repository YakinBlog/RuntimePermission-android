package com.yakin.rtp.processor;

import android.Manifest;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;

import com.yakin.rtp.IRTPGrantHandler;
import com.yakin.rtp.Permission;

import java.util.Arrays;

public class AudioProcessor implements IProcessor {

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    private static final int MAX_SEQ = 10; // 尝试读取数据的次数
    private static final int SAMPLE_RATE = 4000; // 使用最小采样

    private static int sBufferSize;

    @Override
    public void runPermission(final IRTPGrantHandler callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AudioRecord audioRecord = createAudioRecord();
                if(audioRecord == null || audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
                    callback(false, callback);
                    return;
                }
                try {
                    audioRecord.startRecording();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                    audioRecord.release();
                    callback(false, callback);
                    return;
                }
                callback(checkStream(audioRecord), callback);
                audioRecord.release();
            }
        }).start();
    }

    private void callback(final boolean granted, final IRTPGrantHandler callback) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if(callback != null) {
                    if(granted) {
                        callback.onPermissionGranted();
                    } else {
                        Permission permission = new Permission(Manifest.permission.RECORD_AUDIO, false);
                        callback.onPermissionDenied(new Permission[] { permission });
                    }
                }
            }
        });
    }

    private AudioRecord createAudioRecord() {
        try {
            sBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_8BIT);
            return new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_8BIT, sBufferSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkStream(AudioRecord audioRecord) {
        byte[] readBuffer = new byte[sBufferSize];
        int readSize, sequence = 0;
        while(sequence++ < MAX_SEQ) {
            readSize = audioRecord.read(readBuffer, 0, sBufferSize);
            if(readSize > AudioRecord.SUCCESS) {
                Arrays.fill(readBuffer, (byte) 0);
                return true;
            }
        }
        Arrays.fill(readBuffer, (byte) 0);
        return false;
    }
}
