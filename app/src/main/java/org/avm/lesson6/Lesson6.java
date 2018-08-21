package org.avm.lesson6;

import android.app.Application;

import timber.log.Timber;

public class Lesson6 extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
            /*Logging to the file*/
            Timber.plant(new FileLoggingTree());
        }
        Timber.d("[Start application]");
    }
}
