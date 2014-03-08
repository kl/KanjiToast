package com.github.kl.kanjitoast.test;

import android.test.InstrumentationTestCase;

import com.github.kl.kanjitoast.resolver.KanjiPreconditionChecker;
import com.google.common.collect.Lists;

import java.util.List;

public class KanjiPreconditionCheckerTest extends InstrumentationTestCase {

    // Inputs that should return false
    private static final List<String> shouldFail = Lists.newArrayList("Longer than a single character", "A", "漢字");

    // Inputs that should return true
    private static final List<String> shouldPass = Lists.newArrayList("漢", "字", "本");

    private KanjiPreconditionChecker checker;

    public void setUp() {
        checker = new KanjiPreconditionChecker();
    }

    public void testReturnsFalseWhenPassedNull() {
        assertFalse(checker.check(null));
    }

    public void testReturnsFalseWhenInputShouldFail() {
        for (String input : shouldFail) {
            assertFalse("Expected '" + input + "' to fail", checker.check(input));
        }
    }

    public void testReturnsTrueWhenInputShouldPass() {
        for (String input : shouldPass) {
            assertTrue("Expected '" + input + "' to pass", checker.check(input));
        }
    }
}
