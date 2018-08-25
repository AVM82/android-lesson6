package org.avm.lesson6.view;

import io.realm.Realm;

public interface IMainActivity {
    Realm getRealm();
    void updateTimer(long timeInMillis);
}