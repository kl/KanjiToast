package com.github.kl.kanjitoast.resolver;

import android.content.Context;

import com.github.kl.kanjitoast.R;
import com.github.kl.kanjitoast.resolver.KanaResolver;
import com.github.kl.kanjitoast.resolver.PreconditionChecker;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class MemoryKanaResolver implements KanaResolver {

    private static final Map<String, String> kanaMap = new HashMap<>();

    private PreconditionChecker checker;

    public MemoryKanaResolver(Context context, PreconditionChecker checker) {
        this.checker = checker;

        synchronized (kanaMap) {
            if (kanaMap.isEmpty()) {
                String[] data = context.getResources().getStringArray(R.array.kana_data);
                initializeKanaMap(data);
            }
        }
    }

    @Override
    public String resolve(String text) {
        if (!checker.check(text)) return null;
        return kanaMap.get(text);
    }

    private void initializeKanaMap(String[] data) {
        for (String entry : data) {
            String[] parts = entry.split(":");
            kanaMap.put(parts[0], parts[1]);
        }
    }
}
