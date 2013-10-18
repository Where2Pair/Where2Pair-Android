package org.where2pair;

import android.os.Vibrator;

import org.where2pair.presentation.DeviceVibrator;

class AndroidDeviceVibrator implements DeviceVibrator {

    private Vibrator vibrator;

    AndroidDeviceVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    @Override
    public void vibrate(long millis) {
        vibrator.vibrate(millis);
    }
}
