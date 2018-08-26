package org.avm.lesson6.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import org.avm.lesson6.model.DrinkRealmObject;
import org.avm.lesson6.model.db.DataBaseRealm;
import org.avm.lesson6.model.db.IDataBaseManager;

import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class RestoreAfterRebootJobIntentService extends JobIntentService {
    private static final int UNIQUE_JOB_ID = 999;

    public static void enqueueWork(Context context, Intent intent) {
        Timber.d("Start job for RestoreAfterReboot.");
        enqueueWork(context, RestoreAfterRebootJobIntentService.class, UNIQUE_JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("The onCreate() handler was called");

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        IDataBaseManager dataBaseManager = initDataBase();
        RealmResults<DrinkRealmObject> activeDrinks = dataBaseManager.getActiveDrinks();
        long activeDrinkTime;
        if(activeDrinks.size() > 0) {
            activeDrinkTime = activeDrinks.get(0).getTimeLastStart();
            Timber.d("ScheduledNotification %s", activeDrinks);
            NotificationBroadcastReceiver.scheduledNotification(getBaseContext(), activeDrinkTime);
        }

        dataBaseManager.close();
        stopSelf();
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
