package com.github.kl.kanjitoast.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.common.base.Preconditions;

public class Toaster {

    private Context context;
    private Handler uiThreadHandler;

    public Toaster(Context context) {
        Preconditions.checkNotNull(context);

        this.context = context;
        uiThreadHandler = new Handler(Looper.getMainLooper());
    }

    public void display(final String text, final int toastLength) {
        uiThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context, text, toastLength).show();
            }
        });
    }
}
