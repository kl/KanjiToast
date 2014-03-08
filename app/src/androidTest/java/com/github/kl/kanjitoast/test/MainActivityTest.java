package com.github.kl.kanjitoast.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityUnitTestCase;

import com.github.kl.kanjitoast.R;
import com.github.kl.kanjitoast.service.KanjiLookupService;
import com.github.kl.kanjitoast.ui.MainActivity;
import com.github.kl.kanjitoast.PreferenceHelper;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

    private static PreferenceHelper prefHelper;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        prefHelper = PreferenceHelper.getInstance();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        resetSharedPreferences();
    }

    public void testStartsServiceWhenCreatedIfServicePreferenceEnabled() {
        prefHelper.setServiceEnabled(true);

        IntentMockContext context = new IntentMockContext(getInstrumentation().getTargetContext());
        setActivityContext(context);

        startActivity(new Intent(Intent.ACTION_MAIN), null, null);

        Intent intent = context.capturedIntent;
        assertNotNull("capturedIntent should not be null", intent);
        assertEquals(KanjiLookupService.class.getName(), intent.getComponent().getClassName());
    }

    public void testDoesNotStartServiceWhenCreatedIfServicePreferenceDisabled() {
        prefHelper.setServiceEnabled(false);

        IntentMockContext context = new IntentMockContext(getInstrumentation().getTargetContext());
        setActivityContext(context);

        startActivity(new Intent(Intent.ACTION_MAIN), null, null);

        assertNull("Expected capturedIntent to be null", context.capturedIntent);
    }

    // Helper mock context
    private class IntentMockContext extends ContextWrapper {

        Intent capturedIntent;

        public IntentMockContext(Context base) {
            super(base);
        }

        @Override
        public ComponentName startService(Intent serviceIntent) {
            capturedIntent = serviceIntent;
            return null;
        }
    }

    private void resetSharedPreferences() {
        Context targetContext = getInstrumentation().getTargetContext();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(targetContext);
        sharedPrefs.edit().clear().commit();   // needs to be done before default values can be reset.
        PreferenceManager.setDefaultValues(targetContext, R.xml.preferences, true);
    }
}














