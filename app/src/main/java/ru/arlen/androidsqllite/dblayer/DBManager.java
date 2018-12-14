package ru.arlen.androidsqllite.dblayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import ru.arlen.androidsqllite.model.Note;
import ru.arlen.androidsqllite.model.Props;

import java.util.*;

import static ru.arlen.androidsqllite.dblayer.DBHelper.NOTE_TABLE;

public class DBManager {
    public static final String TITLE = "TITLE";
    private static final String CONTENT = "CONTENT";
    private static final String ID = "ID";
    private static final String[] NOTE_COLUMNS = {ID, TITLE, CONTENT};
    public static final String SIZE = "SIZE";
    public static final String COLOR = "COLOR";
    private DBHelper dbHelper;


    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public Note getNote(String title) {
        SQLiteDatabase db = null;
        Note note = new Note();
        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            note = getNote(db, title);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.w("SQLiteException: ", e.getMessage());
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
        return note;
    }

    public Props getProps() {
        SQLiteDatabase db = null;
        Props props = new Props();
        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            props = getProps(db);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.w("SQLiteException: ", e.getMessage());
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
        return props;
    }

    public List<String> getNotes() {
        SQLiteDatabase db = null;
        List<String> notes = new ArrayList<>();
        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            notes = getNotes(db);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.w("SQLiteException: ", e.getMessage());
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
        return notes;
    }

    public void updateNote(Note note) {
        updateNote(getCV(note), note.getId());
    }

    public int updateNote(ContentValues values, String id) {
        SQLiteDatabase db = null;
        int rowsUpdated = 0;
        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            rowsUpdated = updateNote(db, values, id);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.w("SQLiteException: ", e.getMessage());
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
        return rowsUpdated;
    }

    public int deleteNote(String id) {
        SQLiteDatabase db = null;
        int rowsDelted = 0;
        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            rowsDelted = deleteNote(db, id);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.w("SQLiteException: ", e.getMessage());
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
        return rowsDelted;
    }

    public void addNote(String title, String content) {
        ContentValues values = getCV(new Note(title, content));
        addNote(values);
    }

    public long addNote(ContentValues values) {
        SQLiteDatabase db = null;
        long id = -1;
        try {
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            id = insertNote(db, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.w("SQLiteException: ", e.getMessage());
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
        return id;
    }

    public void updateProps(Props props) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = getCV(props);
            db.beginTransaction();
            updateProps(db, values, props.getId());
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.w("SQLiteException: ", e.getMessage());
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
    }

    private ContentValues getCV(Props props) {
        ContentValues values = new ContentValues();
        values.put(SIZE, props.getSize());
        values.put(COLOR, props.getColor());
        return values;
    }

    private ContentValues getCV(Note note) {
        ContentValues values = new ContentValues();
        values.put(TITLE, note.getTitle());
        values.put(CONTENT, note.getContent());
        return values;
    }

    private Note getNote(SQLiteDatabase db, String title) {
        Note note = new Note();
        Cursor cursor = db
                .query(NOTE_TABLE, NOTE_COLUMNS, "TITLE = ?", new String[]{title}, null, null, null);
        if (cursor.moveToFirst()) {
            note.setId(Integer.toString(cursor.getInt(0)));
            note.setTitle(cursor.getString(1));
            note.setContent(cursor.getString(2));
        }
        cursor.close();
        return note;
    }

    private List<String> getNotes(SQLiteDatabase db) {
        List<String> notes = new ArrayList<>();
        Cursor cursor = db.rawQuery("select TITLE from NOTE", null);
        if (cursor.moveToFirst()) {
            do {
                notes.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    private Props getProps(SQLiteDatabase db) {
        Props props = new Props();
        Cursor cursor = db.rawQuery("select * from PROPS", null);

        if (cursor.moveToFirst()) {
            do {
                props.setId(Integer.toString(cursor.getInt(0)));
                props.setSize(cursor.getString(1));
                props.setColor(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return props;
    }

    private long insertNote(SQLiteDatabase db, ContentValues values) {
        return db.insert(NOTE_TABLE, null, values);
    }

    private int updateNote(SQLiteDatabase db, ContentValues values, String id) {
        return db.update(NOTE_TABLE, values, "ID = ?", new String[]{id});
    }

    private int deleteNote(SQLiteDatabase db, String id) {
        return db.delete(NOTE_TABLE, "ID = ?", new String[]{id});
    }

    private void updateProps(SQLiteDatabase db, ContentValues values, String id) {
        db.update("PROPS", values, "ID = ?", new String[]{id});
    }
}
