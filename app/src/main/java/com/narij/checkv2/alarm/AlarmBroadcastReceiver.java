package com.narij.checkv2.alarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.narij.checkv2.R;
import com.narij.checkv2.firebase.util.NotificationUtils;

import androidx.core.app.NotificationCompat;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "my_app")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm Manager")
                .setContentText("Hora de tomar seus comprimidos.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager am = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int id = (int) (System.currentTimeMillis() / 1000);

        am.notify(id, mBuilder.build());

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();

//        PackageManager pm = context.getPackageManager();
//        Intent launchIntent = pm.getLaunchIntentForPackage("com.example.helloworld");
//        launchIntent.putExtra("some_data", "value");
//        context.startActivity(launchIntent);

        if (NotificationUtils.isAppIsInBackground(context)) {

            Intent startIntent = context
                    .getPackageManager()
                    .getLaunchIntentForPackage(context.getPackageName());

            startIntent.setFlags(
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            );
            context.startActivity(startIntent);

        }

    }
}
