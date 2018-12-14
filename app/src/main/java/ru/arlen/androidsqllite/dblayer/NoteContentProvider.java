package ru.arlen.androidsqllite.dblayer;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import static ru.arlen.androidsqllite.dblayer.DBHelper.*;

public class NoteContentProvider extends ContentProvider {
    private static final String AUTHORITY = "ru.arlen.note";
    private static final String NOTE_PATH = "notes";

    public static final Uri NOTE_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTE_PATH);

    // Типы данных
    // набор строк
    private static final String NOTE_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + NOTE_PATH;

    // одна строка
    private static final String NOTE_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + NOTE_PATH;

    // UriMatcher
    // общий Uri
    private static final int URI_NOTES = 1;

    // Uri с указанным ID
    private static final int URI_NOTES_ID = 2;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH, URI_NOTES);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH + "/#", URI_NOTES_ID);
    }

    private DBHelper dbHelper;
    private DBManager dbManager;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        dbManager = new DBManager(getContext());
        return true;
    }

    /**
     * projection - столбцы
     * selection - условие
     * selectionArgs - аргументы для условия
     * sortOrder – сортировка
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTES: // общий Uri
                // если сортировка не указана, ставим свою - по заголовку
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = NOTE_TITLE + " ASC";
                }
                break;
            case URI_NOTES_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = NOTE_ID + " = " + id;
                } else {
                    selection = selection + " AND " + NOTE_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(NOTE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), NOTE_URI);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTES: // общий Uri
                long rowID = dbManager.addNote(values);
                Uri resultUri = ContentUris.withAppendedId(NOTE_URI, rowID);
                getContext().getContentResolver().notifyChange(NOTE_URI, null);
                return resultUri;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTES_ID: // общий Uri
                int rowsDeleted = dbManager.deleteNote(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(NOTE_URI, null);
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTES_ID: // общий Uri
                int rowsUpdated = dbManager.updateNote(values, uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(NOTE_URI, null);
                return rowsUpdated;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:
                return NOTE_TYPE;
            case URI_NOTES_ID:
                return NOTE_ITEM_TYPE;
        }
        return null;
    }
}
