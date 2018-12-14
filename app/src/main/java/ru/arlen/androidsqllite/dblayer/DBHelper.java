package ru.arlen.androidsqllite.dblayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DATABASE_NAME";
    public static final String NOTE_TABLE = "NOTE";
    public static final String NOTE_ID = "id";
    public static final String NOTE_TITLE = "title";
    public static final String NOTE_CONTENT = "content";
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
        db.execSQL("create table " + NOTE_TABLE + "(" + NOTE_ID + " integer primary key autoincrement, " + NOTE_TITLE + " text, " + NOTE_CONTENT + " text)");
        db.execSQL("insert into NOTE(title, content) values ('first', 'first text')");
        db.execSQL("insert into NOTE(title, content) values ('second', 'second text')");
        db.execSQL("create table PROPS(id integer primary key, size text, color text)");
        db.execSQL("insert into PROPS(id, size, color) values (1, '12', 'BLACK')");
    }

    private void deleteTables(SQLiteDatabase db) {
        db.execSQL("drop table if exists NOTE");
        db.execSQL("drop table if exists PROPS");
    }
}
