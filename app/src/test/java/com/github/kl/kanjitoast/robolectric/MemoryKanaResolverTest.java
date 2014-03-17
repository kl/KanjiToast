package com.github.kl.kanjitoast.robolectric;

import android.content.res.Resources;
import android.test.mock.MockContext;

import com.github.kl.kanjitoast.resolver.MemoryKanaResolver;
import com.github.kl.kanjitoast.resolver.PreconditionChecker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class MemoryKanaResolverTest {

    private static final String[] KANA_ARRAY = new String[] {
        "あ:a",
        "う:u",
        "え:e",
        "ヨ:yo",
        "ラ:ra",
        "リ:ri",
        "ぎゃ:gya",
        "チャ:cha"
    };

    private MemoryKanaResolver kanaResolver;

    @Before
    public void setup() {

        // Workaround for https://code.google.com/p/dexmaker/issues/detail?id=2
        //System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        Resources stubResources = mock(Resources.class);
        when(stubResources.getStringArray(anyInt())).thenReturn(KANA_ARRAY);

        MockContext stubContext = mock(MockContext.class);
        when(stubContext.getResources()).thenReturn(stubResources);

        PreconditionChecker stubPreconditionChecker = mock(PreconditionChecker.class);
        when(stubPreconditionChecker.check(anyString())).thenReturn(true);

        kanaResolver = new MemoryKanaResolver(stubContext, stubPreconditionChecker);
    }

    @Test
    public void returnsKeywordWhenValidKana() {
        assertEquals("a", kanaResolver.resolve("あ"));
        assertEquals("yo", kanaResolver.resolve("ヨ"));
        assertEquals("gya", kanaResolver.resolve("ぎゃ"));
        assertEquals("cha", kanaResolver.resolve("チャ"));
    }

    @Test
    public void testReturnsNullWhenInvalidKana() {
        assertNull(kanaResolver.resolve(null));
        assertNull(kanaResolver.resolve(""));
        assertNull(kanaResolver.resolve("a"));
        assertNull(kanaResolver.resolve("九"));
    }
}

/*
   private static final String[] KANA_ARRAY = new String[] {
        "あ:a",
        "う:u",
        "え:e",
        "ヨ:yo",
        "ラ:ra",
        "リ:ri",
        "ぎゃ:gya",
        "チャ:cha"
    };

    private MemoryKanaResolver kanaResolver;

    @Override
    public void setUp() throws Exception {
        super.setUp();


        Resources stubResources = mock(Resources.class);
        when(stubResources.getStringArray(anyInt())).thenReturn(KANA_ARRAY);

        MockContext stubContext = mock(MockContext.class);
        when(stubContext.getResources()).thenReturn(stubResources);

        PreconditionChecker stubPreconditionChecker = mock(PreconditionChecker.class);
        when(stubPreconditionChecker.check(anyString())).thenReturn(true);

    }

    public void testReturnsKeywordWhenValidKana() {
       assertEquals("a", kanaResolver.resolve("あ"));
       assertEquals("yo", kanaResolver.resolve("ヨ"));
       assertEquals("gya", kanaResolver.resolve("ぎゃ"));
       assertEquals("cha", kanaResolver.resolve("チャ"));
    }

    public void testReturnsNullWhenInvalidKana() {
        assertNull(kanaResolver.resolve(null));
        assertNull(kanaResolver.resolve(""));
        assertNull(kanaResolver.resolve("a"));
        assertNull(kanaResolver.resolve("九"));
    }
 */
