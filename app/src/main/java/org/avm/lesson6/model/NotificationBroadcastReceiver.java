package org.avm.lesson6.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import timber.log.Timber;

public class NotificationBroadcastReceiver extends BroadcastReceiver{

    private static final int MILLISECOND = 1000;
    private static final int SECOND_IN_MINUTE = 60;
    private static final int REQUEST_CODE = 777;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("Send notification");
        Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();

    }

    public static void scheduledNotification(Context context, int frequencyMinutes){
        long when = System.currentTimeMillis() + convertMinToMillis(frequencyMinutes);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = createPendingIntent(context);
        if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, when, pendingIntent);
        } else {
            Timber.d("Failed scheduled notification");
        }
    }

    private static PendingIntent createPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static int convertMinToMillis(int minutes) {
        return minutes * SECOND_IN_MINUTE * MILLISECOND;
    }

    public static void unscheduledNotification(Context context){
        AlarmManager alarmManager  = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(createPendingIntent(context));
        } else {
            Timber.d("Failed canceled notification");
        }
    }
}
