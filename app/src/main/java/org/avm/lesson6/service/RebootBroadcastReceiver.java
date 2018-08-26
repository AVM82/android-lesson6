package org.avm.lesson6.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

public class RebootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Timber.d("Restore notification");
            RestoreAfterRebootJobIntentService.enqueueWork(context, intent);
        }
    }

}
