package com.github.kl.kanjitoast.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.InstrumentationTestCase;

import com.github.kl.kanjitoast.resolver.DatabaseKanjiResolver;
import com.github.kl.kanjitoast.resolver.PreconditionChecker;

import static com.github.kl.kanjitoast.database.KanjiDatabaseContract.KANJI_COLUMN_NAME;
import static com.github.kl.kanjitoast.database.KanjiDatabaseContract.KEYWORD_COLUMN_NAME;
import static com.github.kl.kanjitoast.database.KanjiDatabaseContract.TABLE_NAME;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DatabaseKanjiResolverTest extends InstrumentationTestCase {

    DatabaseKanjiResolver resolver;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        PreconditionChecker mockPreconditionChecker = mock(PreconditionChecker.class);
        when(mockPreconditionChecker.check(anyString())).thenReturn(true);

        resolver = new DatabaseKanjiResolver(createTestDatabase(), mockPreconditionChecker);
    }

    public void testReturnsKeywordWhenValidKanji() {
        assertEquals("scaffold", resolver.resolve("桟"));
        assertEquals("coin",     resolver.resolve("銭"));
        assertEquals("shallow",  resolver.resolve("浅"));
    }

    public void testReturnsNulldWhenInvalidKanji() {
        assertNull(resolver.resolve(null));
        assertNull(resolver.resolve(""));
        assertNull(resolver.resolve("ヨ"));
        assertNull(resolver.resolve("桟銭浅"));
    }

    // Creates an in-memory SQLiteDatabase
    private SQLiteDatabase createTestDatabase() {
        Context context = getInstrumentation().getTargetContext();

        return new SQLiteOpenHelper(context, null, null, 1) {

            @Override
            public void onCreate(SQLiteDatabase db) {

                db.execSQL(
                    "create table " + TABLE_NAME + " (" +
                        KANJI_COLUMN_NAME   + " text primary key," +
                        KEYWORD_COLUMN_NAME + " text not null" +
                    ");"
                );

                db.execSQL("insert into " + TABLE_NAME + " values('桟', 'scaffold')");
                db.execSQL("insert into " + TABLE_NAME + " values('銭', 'coin')");
                db.execSQL("insert into " + TABLE_NAME + " values('浅', 'shallow')");
            }

            @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

        }.getReadableDatabase();
    }
}






















