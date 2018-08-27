package org.avm.lesson6.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.avm.lesson6.Util;

import timber.log.Timber;

public class NotificationBroadcastReceiver extends BroadcastReceiver{

    private static final int REQUEST_CODE = 777;
    public static final int MESSAGE_FREQUENCY_MINUTES = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("Start job for notification.");
        NotificationJobIntentService.enqueueWork(context, intent);

    }

    public static void scheduledNotification(Context context, long startTimeInMillis){

        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = createPendingIntent(context);
        if (alarmManager != null) {
            long when = getNextAlarmInMillis(startTimeInMillis);
            int SDK_INT = Build.VERSION.SDK_INT;
            if (SDK_INT < Build.VERSION_CODES.KITKAT)
                alarmManager.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
            else if (Build.VERSION_CODES.KITKAT <= SDK_INT && SDK_INT < Build.VERSION_CODES.M)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, when, pendingIntent);
            else if (SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, when, pendingIntent);
            }
            Timber.d("A notification has been successfully installed");
        } else {
            Timber.d("Failed scheduled notification");
        }
    }

    private static PendingIntent createPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static void unscheduledNotification(Context context){
        AlarmManager alarmManager  = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(createPendingIntent(context));
            Timber.d("A notification has been successfully stopped");
        } else {
            Timber.d("Failed canceled notification");
        }
    }

    private static long getNextAlarmInMillis(long startTimeInMillis) {
        return startTimeInMillis + Util.convertMinToMillis(MESSAGE_FREQUENCY_MINUTES);
    }
}
