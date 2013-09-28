package org.where2pair;

import android.app.Application;

import roboguice.RoboGuice;


public class Where2PairApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE, RoboGuice.newDefaultRoboModule(this), new Where2PairModule(this));
    }
}
