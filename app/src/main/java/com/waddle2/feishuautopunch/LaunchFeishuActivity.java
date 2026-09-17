package com.waddle2.feishuautopunch;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class LaunchFeishuActivity extends Activity {
    private boolean launched = false;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        if (android.os.Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }
        setContentView(new TextView(this));
        int slot = getIntent().getIntExtra("slot", -1);
        if (slot >= 0) {
            android.content.SharedPreferences p = getSharedPreferences(AlarmReceiver.PREF, 0);
            int h = p.getInt("h" + slot, -1), m = p.getInt("m" + slot, -1);
            if (h >= 0) AlarmReceiver.scheduleDaily(this, slot, h, m);
        }

        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (km != null && km.isKeyguardLocked() && android.os.Build.VERSION.SDK_INT >= 26) {
            km.requestDismissKeyguard(this, new KeyguardManager.KeyguardDismissCallback() {
                @Override public void onDismissSucceeded() { launchOnce(); }
                @Override public void onDismissCancelled() { launchOnce(); }
                @Override public void onDismissError() { launchOnce(); }
            });
        }
        handler.postDelayed(this::launchOnce, 1200);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(this::launchOnce, 500);
    }

    private void launchOnce() {
        if (launched || isFinishing()) return;
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (km != null && km.isKeyguardLocked()) {
            // Do not close the bridge while the lock screen is still present.
            handler.postDelayed(this::launchOnce, 500);
            return;
        }
        launched = true;
        AlarmReceiver.launchFeishu(this, "定时");
        handler.postDelayed(() -> finishAndRemoveTask(), 1200);
    }
}
