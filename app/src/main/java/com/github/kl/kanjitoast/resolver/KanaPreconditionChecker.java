package com.github.kl.kanjitoast.resolver;

import com.google.common.base.CharMatcher;

public class KanaPreconditionChecker implements PreconditionChecker {

    @Override
    public boolean check(String text) {
        if (text == null) return false;
        return lengthCheck(text) && notSingleWidthCheck(text);
    }

    private boolean lengthCheck(String text) {
        return text.length() == 1 || text.length() == 2;
    }

    private boolean notSingleWidthCheck(String text) {
        return CharMatcher.SINGLE_WIDTH.matchesNoneOf(text);
    }
}
