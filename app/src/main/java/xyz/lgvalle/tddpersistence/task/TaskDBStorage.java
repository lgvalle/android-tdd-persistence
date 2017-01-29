package xyz.lgvalle.tddpersistence.task;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import xyz.lgvalle.tddpersistence.db.TaskReaderDbHelper;

import static xyz.lgvalle.tddpersistence.db.TaskReaderContract.TaskEntry.*;


public class TaskDBStorage implements TaskStorage {


    private final TaskReaderDbHelper taskReaderDbHelper;

    public TaskDBStorage(TaskReaderDbHelper taskReaderDbHelper) {
        this.taskReaderDbHelper = taskReaderDbHelper;
    }

    private SQLiteDatabase openDB() {
        return taskReaderDbHelper.getWritableDatabase();
    }

    @Override
    public void insert(TaskDBModel taskDBModel) {
        SQLiteDatabase db = openDB();

        ContentValues values = toContentValues(taskDBModel);

        db.insertOrThrow(TABLE_NAME, null, values);

        closeDB();
    }

    @Override
    public void delete(TaskDBModel taskDBModel) {
        SQLiteDatabase db = openDB();

        String selection = COLUMN_TASK_NAME + " LIKE ?";
        String[] selectionArgs = {taskDBModel.getName()};
        db.delete(TABLE_NAME, selection, selectionArgs);

        closeDB();
    }

    private void closeDB() {
        taskReaderDbHelper.close();
    }

    private ContentValues toContentValues(TaskDBModel taskDBModel) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskDBModel.getName());
        values.put(COLUMN_TASK_EXPIRATION, taskDBModel.getExpiration());
        values.put(COLUMN_TASK_LIST, taskDBModel.getListName());
        return values;
    }

    @Override
    public List<TaskDBModel> findAllExpiredBy(long expirationDate) {
        SQLiteDatabase db = openDB();

        String selection = COLUMN_TASK_EXPIRATION + " < ?";
        String[] selectionArgs = {String.valueOf(expirationDate)};


        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                dbProjection(),                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null
        );

        return toTaskDBModels(cursor);
    }

    private String[] dbProjection() {
        return new String[]{
                COLUMN_TASK_NAME,
                COLUMN_TASK_EXPIRATION,
                COLUMN_TASK_LIST
        };
    }

    private List<TaskDBModel> toTaskDBModels(Cursor cursor) {
        List<TaskDBModel> tasks = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_NAME));
            long expiration = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TASK_EXPIRATION));
            String listName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_LIST));
            tasks.add(new TaskDBModel(name, expiration, listName));
        }
        cursor.close();
        closeDB();
        return tasks;
    }

    @Override
    public List<TaskDBModel> findAll() {
        SQLiteDatabase db = openDB();

        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                dbProjection(),                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null
        );

        return toTaskDBModels(cursor);
    }

    @Override
    public TaskDBModel findByName(String taskName) {
        SQLiteDatabase db = openDB();

        String selection = COLUMN_TASK_NAME + " == ?";
        String[] selectionArgs = {taskName};


        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                dbProjection(),                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null
        );

        TaskDBModel task = null;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_NAME));
            long expiration = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TASK_EXPIRATION));
            String listName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_LIST));
            task = new TaskDBModel(name, expiration, listName);
        }
        cursor.close();
        closeDB();

        return task;
    }
}
