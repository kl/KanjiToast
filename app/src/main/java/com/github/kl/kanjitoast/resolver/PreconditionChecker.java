package com.github.kl.kanjitoast.resolver;

public interface PreconditionChecker {

    /**
     * Should return true if text could be a kanji or a kana. For example, if the text is
     * more than one character long, a kana or kanji lookup should not be attempted and this
     * method should return false.
     * @param text the text to check
     * @return whether the text is eligible for lookup or not.
     */
    public boolean check(String text);
}
