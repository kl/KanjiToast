package com.github.kl.kanjitoast.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.kl.kanjitoast.R;

public class KanjiOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "heisig_keywords.db";
    private static final int DATABASE_VERSION = 3;
    private static final String RESOURCE_SEPARATOR = ":";

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table " + KanjiDatabaseContract.TABLE_NAME + " (" +
                    KanjiDatabaseContract.KANJI_COLUMN_NAME   + " text primary key," +
                    KanjiDatabaseContract.KEYWORD_COLUMN_NAME + " text not null" +
            ");";

    private Context context;

    public KanjiOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        seedDatabase(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + KanjiDatabaseContract.TABLE_NAME);
        onCreate(db);
    }

    // Get the string resource array and seed database.
    private void seedDatabase(SQLiteDatabase database) {

        String[] data = context.getResources().getStringArray(R.array.heisig_data);

        for (String line : data) {
            String[] parts = line.split(RESOURCE_SEPARATOR);

            ContentValues values = new ContentValues();
            values.put(KanjiDatabaseContract.KANJI_COLUMN_NAME, parts[0]);
            values.put(KanjiDatabaseContract.KEYWORD_COLUMN_NAME, parts[1]);

            long insertId = database.insert(KanjiDatabaseContract.TABLE_NAME, null, values);

            // This *should* never happen.
            if (insertId == -1)
                throw new RuntimeException("Error seeding the database");
        }
    }
}

