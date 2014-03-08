package com.github.kl.kanjitoast.resolver;

/**
 *
 */
public interface KanjiResolver {

    /**
     * Returns the keyword for the given text, or null if there is no match.
     * @param text the kanji to look up.
     * @return the Heisig keyword or null if the kanji was not found.
     */
    public String resolve(String text);

}
