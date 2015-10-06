package com.cs616.trysqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ian on 15-09-02.
 */
public class NoteTable {

    private static final String TABLE_NAME = "note";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_CATEGORY_ID = "categoryId";

    // DatabaseHandler creation sql statement
    private static final String CREATE_TABLE = "create table " + TABLE_NAME +
            "(_id integer primary key autoincrement, " +
            COLUMN_TITLE + " text not null, " +
            COLUMN_BODY + " text, " +
            COLUMN_CATEGORY_ID + " integer" +
    ");";


    private final DatabaseHandler dbh;
    
 

    public NoteTable(DatabaseHandler databaseHandler) {
        this.dbh = databaseHandler;    
    }

    public String getCreateSQL() {
        return CREATE_TABLE;
    }
    
    public String getTableName() {
        return TABLE_NAME;
    }

    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setTitle(cursor.getString(1));
        note.setBody(cursor.getString(2));
        note.setCategoryId(cursor.getLong(3));
        return note;
    }

    /**
     * Insert the note into the table.
     * @param note The note to be inserted.
     * @postconditions The note's ID field will be set to the value returned by the database.
     */
    public void createNote(Note note) {

        SQLiteDatabase database = dbh.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_BODY, note.getBody());
        values.put(COLUMN_CATEGORY_ID, note.getCategoryId());

        long insertId = database.insertOrThrow(TABLE_NAME, null, values);
        note.setId(insertId);

        database.close();
    }

    /**
     * Read the note specified by id.
     * @param id The ID of the note.
     * @return The note object containing the values from the database.
     */
    public Note readNote(long id) {
        SQLiteDatabase db = dbh.getReadableDatabase();

        String[] projection = new String[] { "_id", COLUMN_TITLE, COLUMN_BODY, COLUMN_CATEGORY_ID};
        Cursor cursor = db.query(TABLE_NAME, projection, "_id =?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = cursorToNote(cursor);
        return note;
    }


    /**
     * Read all categories from the table.
     * @return All rows from the table as a List of note objects.
     */
    public List<Note> getAllNotes() {
        SQLiteDatabase database = dbh.getReadableDatabase();
        List<Note> comments = new ArrayList<>();

        String[] projection = new String[] { "_id", COLUMN_TITLE, COLUMN_BODY, COLUMN_CATEGORY_ID};
        Cursor cursor = database.query(TABLE_NAME, projection, null, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                comments.add(cursorToNote(cursor));
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
        return comments;
    }

    /**
     * Get the number of rows in the table.
     * @return The number of rows in the table.
     */
    public int getNoteCount() {
        return -1;
    }


    /**
     * Update a row in the table.
     * @param note The note object containing updates. The ID field is used to retrieve the correct row.
     * @return ?
     */
    public boolean updateNote(Note note) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    /**
     * Delete a row from the table.
     * @param note The note object containing the row to delete. The ID fields is used to
     */
    public void deleteNote(Note note) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

}
