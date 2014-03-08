package com.github.kl.kanjitoast.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.kl.kanjitoast.service.KanjiLookupService;
import com.github.kl.kanjitoast.PreferenceHelper;
import com.github.kl.kanjitoast.R;

public class MainActivity extends Activity implements PreferenceHelper.ServiceStateListener {

    public static final String LOG_TAG = "kanjitoast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            PreferenceHelper.getInstance().registerServiceStateListener(this);
            initializeComponents();
        }
    }

    private void initializeComponents() {
        createSettingsFragment();
        if (PreferenceHelper.getInstance().isServiceEnabled()) {
            startLookupService();
        }
    }

    private void createSettingsFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();
    }

    private void startLookupService() {
        Intent intent = new Intent(this, KanjiLookupService.class);
        startService(intent);
    }

    private void stopLookupService() {
        Intent intent = new Intent(this, KanjiLookupService.class);
        stopService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            showEditDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showEditDialog() {
        AboutDialogFragment about = new AboutDialogFragment();
        about.show(getFragmentManager(), "about_dialog_fragment");
    }

    @Override
    public void onServiceStart() {
        startLookupService();
        Toast.makeText(this, getString(R.string.service_started), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceStop() {
        stopLookupService();
        Toast.makeText(this, getString(R.string.service_stopped), Toast.LENGTH_SHORT).show();
    }
}





