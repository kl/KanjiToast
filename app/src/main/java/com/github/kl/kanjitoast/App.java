package com.github.kl.kanjitoast;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class App extends Application {

    // Used for Toast debugging:
    private static Context applicationContext;
    private static Handler uiThreadHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();
        uiThreadHandler = new Handler(Looper.getMainLooper());
        initSingletons();
    }

    private void initSingletons() {
        PreferenceHelper.initInstance(getApplicationContext());
    }

    // This method is for temporary debugging only.
    public static void DEBUG(final String message) {
       uiThreadHandler.post(new Runnable() {

           @Override
           public void run() {
               Toast.makeText(applicationContext, "DEBUG: " + message, Toast.LENGTH_LONG).show();
           }
       });
    }
}
