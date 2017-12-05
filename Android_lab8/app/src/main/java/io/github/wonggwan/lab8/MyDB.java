package io.github.wonggwan.lab8;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wonggwan on 2017/12/4.
 */

public class MyDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "MyDB.db";
    private static final String TABLE_NAME = "Info";
    private static final int DB_VERSION = 1;

    public MyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDB(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE if not exists "
                + TABLE_NAME
                + "(name TEXT PRIMARY KEY,"
                + "birth TEXT,"
                + "gift TEXT)";
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }
        catch (SQLException e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
