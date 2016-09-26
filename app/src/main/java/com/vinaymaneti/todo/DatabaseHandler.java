package com.vinaymaneti.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinaymaneti on 9/22/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    //All static variables
    //Data base version
    private static final int DATABASE_VERSION = 2;  // here I upgrade the database version from 1 to 2

    // Database name
    private static final String DATABASE_NAME = "todoListOrganiser";

    //Table name
    public static final String TABLE_NAME = "todoList";

    //TO-DO List Table column names
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_STATUS = "status";
    public static final String KEY_DATE_TIME_REMAINDER = "date_time_remainder";
    private static final String DATABASE_ALTER_DATE_TIME_REMAINDER = "ALTER TABLE "
            + TABLE_NAME + " ADD COLUMN "
            + KEY_DATE_TIME_REMAINDER + " DATETIME;";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_LIST_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_NOTES + " TEXT,"
                + KEY_STATUS + " INTEGER DEFAULT 0,"
                + KEY_DATE_TIME_REMAINDER
                + " DATETIME" + ")";
        db.execSQL(CREATE_TODO_LIST_TABLE);
    }

    //update database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        //Drop older table if exited
//        db.execSQL("DROP TABLE IF EXITS " + TABLE_NAME);
//
//        //create tables again
//        onCreate(db);
        if (oldVersion < 2) {
            db.execSQL(DATABASE_ALTER_DATE_TIME_REMAINDER);
        }
    }

    // All CRUD operation (create , read, update and delete)

    //Adding an new task-item(#todo) to db
    public void addTodo(Todo todo) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, todo.getTitle());
        values.put(KEY_NOTES, todo.getNotes());
        values.put(KEY_STATUS, todo.isChecked());
        values.put(KEY_DATE_TIME_REMAINDER, todo.getDateTimeRemainder());

        Log.d("Data Insertion ::--", values.toString());
        //Insert new row
        database.insert(TABLE_NAME, null, values);
        database.close();

    }

    //getting single task-item from db
    public Todo getTodo(int id) {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_NAME,
                new String[]{
                        KEY_ID,
                        KEY_TITLE,
                        KEY_NOTES,
                        KEY_STATUS,
                        KEY_DATE_TIME_REMAINDER},
                KEY_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);

        if (cursor != null)
            cursor.moveToFirst();
        boolean status;
        if (cursor != null && cursor.getString(3) != null && Integer.parseInt(cursor.getString(3)) == 0) {
            status = false;
        } else {
            status = true;
        }

        Todo todo = new Todo(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), status, cursor.getString(4));

        //return todo
        return todo;
    }

    //Get all task -item list from db
    public List<Todo> getAllTodoList() {
        List<Todo> todoList = new ArrayList<>();
        //select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY ID  DESC ";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        //loop through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(Integer.parseInt(cursor.getString(0)));
                todo.setTitle(cursor.getString(1));
                todo.setNotes(cursor.getString(2));
                todo.setChecked(isStatus(Integer.parseInt(cursor.getString(3))));
                todo.setDateTimeRemainder(cursor.getString(4));
                todoList.add(todo);
            } while (cursor.moveToNext());
        }
        // return all task item list
        return todoList;
    }

    private boolean isStatus(int i) {
//        boolean status;
//        if (i == 0)
//            status = false;
//        else
//            status = true;
//        return status;
        return i != 0;
    }

    //Get all task- item list count
    public int getTodoListCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        //it will return total number of items
        return count;
    }

    public int getTodoListCompletedCount() {
        String completedTaskQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_STATUS + " == 1;";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(completedTaskQuery, null);
        int completedAllTasksCount = cursor.getCount();
        cursor.close();

        return completedAllTasksCount;
    }

    public List<Todo> getAllCompletedTasks() {
        List<Todo> allCompletedTasks = new ArrayList<>();

        //select all marked as completed task
        String completedQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_STATUS + " == 1;";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(completedQuery, null);

        //loop through all the row which are completed
        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(Integer.parseInt(cursor.getString(0)));
                todo.setTitle(cursor.getString(1));
                todo.setNotes(cursor.getString(2));
                todo.setChecked(isStatus(Integer.parseInt(cursor.getString(3))));
                todo.setDateTimeRemainder(cursor.getString(4));
                allCompletedTasks.add(todo);
            } while (cursor.moveToNext());
        }
        //return all the completed tasks
        return allCompletedTasks;
    }


    //update single task item
    public int updateTodoList(Todo todo) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, todo.getTitle());
        values.put(KEY_NOTES, todo.getNotes());
        values.put(KEY_STATUS, todo.isChecked());
        values.put(KEY_DATE_TIME_REMAINDER, todo.getDateTimeRemainder());

        //updating row
        return database.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]{String.valueOf(todo.getId())});
    }

    public int updateCheckboxStatus(Todo todo) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, todo.isChecked());

        //Updating check box status value
        return database.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]{String.valueOf(todo.getId())});
    }

    //deleting a single task - item from db
    public void deleteTodoList(Todo todo) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(todo.getId())});
        database.close();
    }

}
