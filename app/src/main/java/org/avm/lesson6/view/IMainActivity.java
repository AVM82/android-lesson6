package org.avm.lesson6.view;

import android.content.Context;

public interface IMainActivity {

    void updateTimer(long timeInMillis);

    Context getContext();
}