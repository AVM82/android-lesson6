package org.avm.lesson6.model;

import android.os.Handler;
import android.os.SystemClock;

public class CountdownTimer {
    public interface OnTickListener {
        void OnSecondTick(long millis);
    }

    private Handler handler;
    private Runnable ticker;
    private OnTickListener onTickListener;

    public void setOnClockTickListener(OnTickListener onClockListener) {
        this.onTickListener = onClockListener;
    }

    public void start(long activationTime) {
        this.handler = new Handler();
        this.ticker = () -> {
            onTickListener.OnSecondTick(activationTime);
            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);
            handler.postAtTime(ticker, next);
        };
        this.ticker.run();

    }
    public void stop() {
        if (this.handler != null) {
            this.handler.removeCallbacks(this.ticker);
        }

    }
}
