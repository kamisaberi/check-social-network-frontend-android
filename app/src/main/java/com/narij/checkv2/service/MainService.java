package com.narij.checkv2.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.narij.checkv2.env.Globals;

public class MainService extends Service {
    public MainService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        EasyPermissions.getInstance().requestPermissions(this, this);

        Log.d(Globals.LOG_TAG, "SEEEEERRRRRRRRRVVVVIIIIICCCCEEEE");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
