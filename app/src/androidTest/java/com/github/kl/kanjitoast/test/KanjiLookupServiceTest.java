package com.github.kl.kanjitoast.test;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.test.RenamingDelegatingContext;
import android.test.ServiceTestCase;

import com.github.kl.kanjitoast.service.KanjiLookupService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class KanjiLookupServiceTest extends ServiceTestCase<KanjiLookupService> {

    private TestContext testContext;

    public KanjiLookupServiceTest() {
        super(KanjiLookupService.class);
    }

    @Override
    public void setUp() throws Exception {
        // Each time setUp is called the service is started but onCreate is NOT called.
        // onCreate is called after startService in a test, see: http://developer.android.com/reference/android/test/ServiceTestCase.html
        super.setUp();

        // Workaround for https://code.google.com/p/dexmaker/issues/detail?id=2
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());

        // Create the TestContext based on getContext() which is a full application context.
        testContext = new TestContext(getContext());
    }

    public void testAddsItselfAsPrimaryClipListenerWhenCreated() {
        setContext(testContext);
        startKanjiLookupService();

        ClipboardManager cm = testContext.mockClipboardManager;
        verify(cm).addPrimaryClipChangedListener(any(KanjiLookupService.class));
    }

    public void testRemovesItselfAsPrimaryClipListenerWhenDestroyed() {
        setContext(testContext);
        startKanjiLookupService();
        shutdownService();

        ClipboardManager cm = testContext.mockClipboardManager;
        verify(cm).removePrimaryClipChangedListener(any(KanjiLookupService.class));
    }

    private void startKanjiLookupService() {
        startService(new Intent(testContext, KanjiLookupService.class));
    }

    //
    // TestContext extends ContextWrapper and wraps a RenamingDelegatingContext which is used
    // so that the KanjiOpenHelper class, which is a dependency of the service, creates a test database.
    //
    private static class TestContext extends ContextWrapper {

        public final ClipboardManager mockClipboardManager = mock(ClipboardManager.class);
        public final Resources mockResources = mock(Resources.class);

        public TestContext(Context base) {
            super(new RenamingDelegatingContext(base, "test"));
        }

        //@Override
        //public Resources getResources() {
        //    return mockResources;
        //}

        @Override
        public Object getSystemService(String name) {
            return mockClipboardManager;
        }
    }
}










