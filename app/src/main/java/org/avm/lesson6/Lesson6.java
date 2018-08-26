package org.avm.lesson6;

import android.app.Application;

import io.realm.Realm;
import timber.log.Timber;

public class Lesson6 extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());

        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
            /*Logging to the file*/
            Timber.plant(new FileLoggingTree());
        }
        Timber.d("[Start application]");
    }
}
