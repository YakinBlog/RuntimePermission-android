package com.yakin.rtp.processor;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.yakin.rtp.BuildConfig;
import com.yakin.rtp.IRTPGrantCallback;

import java.util.Arrays;

public class AudioProcessor {

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    private static final int MAX_SEQ = 50; // 尝试读取数据的次数
    private static final int SAMPLE_RATE = 4000; // 使用最小采样

    private static int sBufferSize;

    public static void checkPermission(final IRTPGrantCallback callback) {
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
                    Log.e("--RTP--", "AudioRecord start failed", e);
                }
                if(audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                    callback(false, callback);
                    return;
                }
                callback(checkStream(audioRecord), callback);
                audioRecord.release();
            }
        }).start();
    }

    private static void callback(final boolean granted, final IRTPGrantCallback callback) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if(callback != null) {
                    if(granted) {
                        callback.onPermissionGranted();
                    } else {
                        callback.onPermissionDenied();
                    }
                }
            }
        });
    }

    private static AudioRecord createAudioRecord() {
        try {
            sBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_8BIT);
            return new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_8BIT, sBufferSize);
        } catch (Exception e) {
            Log.e("--RTP--", "Failed to create a audio recorder", e);
        }
        return null;
    }

    private static boolean checkStream(AudioRecord audioRecord) {
        byte[] readBuffer = new byte[sBufferSize];
        int readSize, sequence = 0;
        while(sequence++ < MAX_SEQ) {
            readSize = audioRecord.read(readBuffer, 0, sBufferSize);
            if(BuildConfig.DEBUG) {
                Log.d("--RTP--", "sequence:" + sequence + ", sBufferSize:" + sBufferSize + ", readSize:" + readSize);
            }
            if(readSize > AudioRecord.SUCCESS) {
                Arrays.fill(readBuffer, (byte) 0);
                return true;
            }
        }
        Arrays.fill(readBuffer, (byte) 0);
        return false;
    }
}
