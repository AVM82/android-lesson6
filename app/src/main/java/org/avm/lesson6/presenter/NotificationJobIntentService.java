package org.avm.lesson6.presenter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;

import org.avm.lesson6.R;
import org.avm.lesson6.model.NotificationBroadcastReceiver;
import org.avm.lesson6.model.db.DataBaseRealm;
import org.avm.lesson6.model.db.IDataBaseManager;
import org.avm.lesson6.view.MainActivity;

import io.realm.Realm;
import timber.log.Timber;

public class NotificationJobIntentService extends JobIntentService {
    private static final int UNIQUE_JOB_ID = 777;
    public static final int NOTIFICATION_ID = 333;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, NotificationJobIntentService.class, UNIQUE_JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("The onCreate() handler was called");

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Timber.d("The onHandleWork() handler was called");
        IDataBaseManager dataBaseManager = initDataBase();
        String drinkName = dataBaseManager.getActiveDrinks().get(0).getName();
        dataBaseManager.setActiveDrink(drinkName, System.currentTimeMillis());
        sendNotification(drinkName);
        NotificationBroadcastReceiver.scheduledNotification(getBaseContext());
        dataBaseManager.close();
        stopSelf();
    }

    private void sendNotification(String drinkName) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0,
                intent, 0);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_start)
                        .setContentTitle("Time to pause")
                        .setContentText("It's time for a glass of " + drinkName)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
        Notification notification = builder.build();
        notification.number = 3;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Timber.d("The onStartCommand() handler was called");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("The onDestroy() handler was called");
    }

    private IDataBaseManager initDataBase() {
        Realm.init(getApplicationContext());
        return new DataBaseRealm();
    }

}
