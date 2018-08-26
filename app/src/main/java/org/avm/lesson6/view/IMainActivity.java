package org.avm.lesson6.view;

import android.content.Context;

import io.realm.Realm;

public interface IMainActivity {
    Realm getRealm();
    void updateTimer(long timeInMillis);
    Context getContext();
}