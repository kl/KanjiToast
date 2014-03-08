package com.github.kl.kanjitoast.resolver;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.kl.kanjitoast.resolver.KanjiResolver;
import com.github.kl.kanjitoast.resolver.PreconditionChecker;

import static com.github.kl.kanjitoast.database.KanjiDatabaseContract.KANJI_COLUMN_NAME;
import static com.github.kl.kanjitoast.database.KanjiDatabaseContract.KEYWORD_COLUMN_NAME;
import static com.github.kl.kanjitoast.database.KanjiDatabaseContract.TABLE_NAME;

public class DatabaseKanjiResolver implements KanjiResolver {

    private PreconditionChecker checker;
    private SQLiteDatabase database;

    public DatabaseKanjiResolver(SQLiteDatabase database, PreconditionChecker checker) {
        this.checker = checker;
        this.database = database;
    }

    // Note: this method is synchronized which may reduce concurrency but it is desirable in
    // this case because 1) it guarantees thread safety and 2) we don't actually want to have
    // multiple threads executing this method concurrently.
    @Override
    public synchronized String resolve(String text) {
        if (!checker.check(text)) return null;
        return query(text);
    }

    private String query(String text) {

        Cursor cursor = database.query(
                TABLE_NAME,                                       // table
                new String[] { KEYWORD_COLUMN_NAME },             // columns to return
                KANJI_COLUMN_NAME + " = " + makeQuerySafe(text),  // where clause
                null,                                             // selectionArgs
                null,                                             // group by
                null,                                             // having
                null,                                             // order by
                "1");                                             // limit

        if (cursor.moveToFirst()) {
            return cursor.getString(0);  // The column index is 0 because only the keyword is returned.
        } else {
            return null;                 // There was no keyword for the text.
        }
    }

    private String makeQuerySafe(String text) {
        return "'" + text + "'";
    }
}


















