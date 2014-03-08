package com.github.kl.kanjitoast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.kl.kanjitoast.service.KanjiLookupService;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_BOOT_COMPLETED)) {

            PreferenceHelper prefs = PreferenceHelper.getInstance();
            if (prefs.isStartOnBootEnabled()) {
                context.startService(new Intent(context, KanjiLookupService.class));
                prefs.setServiceEnabled(true);
            }
        }
    }
}
