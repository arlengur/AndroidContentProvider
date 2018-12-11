package ru.arlen.androidsqllite.dblayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DATABASE_NAME";
    public static final int DB_VERSION = 6;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db);
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL("create table NOTE(id integer primary key, title text, content text)");
        db.execSQL("insert into NOTE(id, title, content) values (1, 'first', 'first text')");
        db.execSQL("insert into NOTE(id, title, content) values (2, 'second', 'second text')");
        db.execSQL("create table PROPS(id integer primary key, size text, color text)");
        db.execSQL("insert into PROPS(id, size, color) values (1, '12', 'WHITE')");
    }

    private void deleteTables(SQLiteDatabase db) {
        db.execSQL("drop table if exists NOTE");
        db.execSQL("drop table if exists PROPS");
    }
}
