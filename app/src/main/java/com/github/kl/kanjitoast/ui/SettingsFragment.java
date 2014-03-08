package com.github.kl.kanjitoast.ui;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.view.View;

import com.github.kl.kanjitoast.PreferenceHelper;
import com.github.kl.kanjitoast.R;

import org.jraf.android.backport.switchwidget.SwitchPreference;

public class SettingsFragment extends PreferenceFragment {

    private String serviceKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        serviceKey = getString(R.string.pref_key_run_service);
    }

    @Override
    public void onResume() {
        super.onResume();
        SwitchPreference s = (SwitchPreference)getPreferenceManager().findPreference(serviceKey);
        s.setChecked(PreferenceHelper.getInstance().isServiceEnabled());
    }
}
