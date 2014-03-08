package com.github.kl.kanjitoast.service;

import android.app.Service;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IBinder;

import com.github.kl.kanjitoast.PreferenceHelper;
import com.github.kl.kanjitoast.resolver.DatabaseKanjiResolver;
import com.github.kl.kanjitoast.database.KanjiOpenHelper;
import com.github.kl.kanjitoast.resolver.KanaPreconditionChecker;
import com.github.kl.kanjitoast.resolver.KanaResolver;
import com.github.kl.kanjitoast.resolver.KanjiPreconditionChecker;
import com.github.kl.kanjitoast.resolver.KanjiResolver;
import com.github.kl.kanjitoast.resolver.MemoryKanaResolver;
import com.github.kl.kanjitoast.resolver.PreconditionChecker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KanjiLookupService extends Service implements ClipboardManager.OnPrimaryClipChangedListener {

    private KanjiResolver kanjiResolver;
    private KanaResolver kanaResolver;
    private Toaster toaster;
    private ExecutorService executor;
    private ClipboardManager clipboard;

    @Override
    public void onCreate() {
        kanjiResolver = makeDatabaseKanjiResolver();
        kanaResolver = new MemoryKanaResolver(this, new KanaPreconditionChecker());

        toaster  = new Toaster(this);
        executor = Executors.newSingleThreadExecutor();

        clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(this);
    }

    private KanjiResolver makeDatabaseKanjiResolver() {
        PreconditionChecker checker = new KanjiPreconditionChecker();
        SQLiteOpenHelper openHelper = new KanjiOpenHelper(this);

        return new DatabaseKanjiResolver(openHelper.getReadableDatabase(), checker);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public void onDestroy() {
        clipboard.removePrimaryClipChangedListener(this);
        executor.shutdown();
    }

    @Override
    public void onPrimaryClipChanged() {

        if (clipboardContainsText()) {
            String clipboardText = getClipboardText();
            if (clipboardText != null) runResolverTask(clipboardText);
        }
    }

    // When using a SingleThreadExecutor tasks are guaranteed to run sequentially.
    private void runResolverTask(final String text) {
        executor.execute(new Runnable() {

            @Override
            public void run() {
                String keyword = kanjiResolver.resolve(text);

                if (keyword == null && isKanaEnabled())
                    keyword = kanaResolver.resolve(text);

                if (keyword != null)
                    toaster.display(keyword, PreferenceHelper.getInstance().getToastLength());
            }
        });
    }

    @SuppressWarnings("ConstantConditions") // clipboard cannot be null.
    private boolean clipboardContainsText() {
        return clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
    }

    @SuppressWarnings("ConstantConditions") // clipboard cannot be null.
    private String getClipboardText() {
        return (String)clipboard.getPrimaryClip().getItemAt(0).getText();
    }

    private boolean isKanaEnabled() {
        return PreferenceHelper.getInstance().isKanaEnabled();
    }
}


















