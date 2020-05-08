package com.narij.checkv2.app;

import android.content.Context;
import android.graphics.Typeface;

import com.narij.checkv2.env.Globals;
import com.narij.checkv2.model.Priority;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();

        Globals.regularRoboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Globals.boldRoboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

        Globals.milliseconds.put("Minute", (long) (60 * 1000));
        Globals.milliseconds.put("Hour", (long) (60 * 60 * 1000));
        Globals.milliseconds.put("Day", (long) (24 * 60 * 60 * 1000));
        Globals.milliseconds.put("Week", (long) (7 * 24 * 60 * 60 * 1000));
        Globals.milliseconds.put("Month", (long) (30 * 24 * 60 * 60 * 1000));
        Globals.milliseconds.put("Year", (long) (365 * 24 * 60 * 60 * 1000));


        Globals.priorities.add(new Priority(1, "Urgent"));
        Globals.priorities.add(new Priority(2, "High"));
        Globals.priorities.add(new Priority(3, "Normal"));
        Globals.priorities.add(new Priority(4, "Low"));

        Globals.friendshipAction.put(0, "send request");
        Globals.friendshipAction.put(3, "break");
        Globals.friendshipAction.put(11, "remove request");
        Globals.friendshipAction.put(12, "accept");
        Globals.friendshipAction.put(21, "accept again");
        Globals.friendshipAction.put(22, "request again");


        Globals.friendshipActionCode.put(0, 1);
        Globals.friendshipActionCode.put(3, 0);
        Globals.friendshipActionCode.put(11, 0);
        Globals.friendshipActionCode.put(12, 3);
        Globals.friendshipActionCode.put(21, 1);
        Globals.friendshipActionCode.put(22, 1);


//        Globals.friendship.put(0, "none");
//        Globals.friendship.put(1, "requested");
//        Globals.friendship.put(2, "rejected");
//        Globals.friendship.put(3, "accepted");

        Globals.friendship.put(0, "send request");
        Globals.friendship.put(1, "remove request");
        Globals.friendship.put(2, "accept again");
        Globals.friendship.put(3, "break");


    }
}
