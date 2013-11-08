package org.where2pair;

import android.text.format.Time;

class AndroidTimeProvider implements TimeProvider {

    @Override
    public SimpleTime getCurrentTime() {
        Time now = new Time();
        now.setToNow();
        return new SimpleTime(now.hour, now.minute);
    }
}
