package com.github.kl.kanjitoast;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashSet;

public final class PreferenceHelper {

    public interface ServiceStateListener {
        public void onServiceStart();
        public void onServiceStop();
    }

    //
    // Singleton initialization:
    //

    private static PreferenceHelper instance;

    public static void initInstance(Context context) {
        if (instance != null) throw new IllegalStateException("initInstance has already been called");
        instance = new PreferenceHelper(context);
    }

    public static PreferenceHelper getInstance() {
        if (instance == null) throw new IllegalStateException("initInstance must be called before getInstance");
        return instance;
    }

    //
    // Implementation:
    //

    private Context context;
    private SharedPreferences prefs;
    private Collection<ServiceStateListener> serviceStateListeners;

    // A reference to the SharedPreferencesListener must be kept.
    // See http://stackoverflow.com/questions/2542938
    private SharedPreferencesListener sharedPrefListener;

    // Cached for performance
    private String prefToastLengthKey;
    private String prefToastLengthDefault;
    private String prefKanaEnabledKey;
    private String prefServiceEnabledKey;
    private String prefStartOnBootKey;

    private PreferenceHelper(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefListener = new SharedPreferencesListener();
        prefs.registerOnSharedPreferenceChangeListener(sharedPrefListener);
        serviceStateListeners = new HashSet<>();

        initCachedFields();
    }

    private void initCachedFields() {
        prefToastLengthKey     = context.getString(R.string.pref_key_keyword_length);
        prefToastLengthDefault = context.getString(R.string.pref_default_keyword_length);
        prefKanaEnabledKey     = context.getString(R.string.pref_key_kana_enabled);
        prefServiceEnabledKey  = context.getString(R.string.pref_key_run_service);
        prefStartOnBootKey     = context.getString(R.string.pref_key_start_service_boot);
    }

    public void registerServiceStateListener(ServiceStateListener listener) {
        serviceStateListeners.add(listener);
    }

    public void removeServiceStateListener(ServiceStateListener listener) {
        serviceStateListeners.remove(listener);
    }

    public boolean isKanaEnabled() {
        return prefs.getBoolean(prefKanaEnabledKey, false);
    }

    public boolean isServiceEnabled() {
        return prefs.getBoolean(prefServiceEnabledKey, true);
    }

    public boolean isStartOnBootEnabled() {
        return prefs.getBoolean(prefStartOnBootKey, false);
    }

    public int getToastLength() {
        String value = prefs.getString(prefToastLengthKey, prefToastLengthDefault);

        if (value.equalsIgnoreCase("long")) {
            return Toast.LENGTH_LONG;
        } else {
            return Toast.LENGTH_SHORT;
        }
    }

    public void setServiceEnabled(boolean enabled) {
        prefs.edit().putBoolean(prefServiceEnabledKey, enabled).commit();
    }

    private class SharedPreferencesListener implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (!key.equals(prefServiceEnabledKey)) return;

            for (ServiceStateListener listener : serviceStateListeners) {
                if (prefs.getBoolean(prefServiceEnabledKey, true)) {
                    listener.onServiceStart();
                } else {
                    listener.onServiceStop();
                }
            }
        }
    }
}



























