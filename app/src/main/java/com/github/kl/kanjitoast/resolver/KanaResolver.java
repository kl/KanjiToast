package com.github.kl.kanjitoast.resolver;

public interface KanaResolver {

    /**
     * Returns the romaji for the kana (hiragana or katakana).
     * @param text the kana to look up
     * @return the romaji for the kana
     */
    String resolve(String text);
}
