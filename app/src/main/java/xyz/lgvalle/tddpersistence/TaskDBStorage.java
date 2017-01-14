package xyz.lgvalle.tddpersistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import xyz.lgvalle.tddpersistence.db.TaskReaderContract;
import xyz.lgvalle.tddpersistence.db.TaskReaderDbHelper;

import static xyz.lgvalle.tddpersistence.db.TaskReaderContract.*;
import static xyz.lgvalle.tddpersistence.db.TaskReaderContract.TaskEntry.*;

/**
 * Created by lgvalle on 08/01/2017.
 */

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

        long newRowId = db.insertOrThrow(TABLE_NAME, null, values);

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

    @NonNull
    private ContentValues toContentValues(TaskDBModel taskDBModel) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskDBModel.getName());
        values.put(COLUMN_TASK_EXPIRATION, taskDBModel.getExpiration());
        return values;
    }

    @Override
    public List<TaskDBModel> findAllExpiredBy(long expirationDate) {
        SQLiteDatabase db = openDB();

        String[] projection = {
                COLUMN_TASK_NAME,
                COLUMN_TASK_EXPIRATION
        };

        String selection = COLUMN_TASK_EXPIRATION + " < ?";
        String[] selectionArgs = {String.valueOf(expirationDate)};


        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null
        );

        List<TaskDBModel> tasks = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_NAME));
            long expiration = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TASK_EXPIRATION));
            tasks.add(new TaskDBModel(name, expiration));
        }
        cursor.close();
        closeDB();

        return tasks;
    }

    @Override
    public TaskDBModel findByName(String taskName) {
        SQLiteDatabase db = openDB();

        String[] projection = {
                COLUMN_TASK_NAME,
                COLUMN_TASK_EXPIRATION
        };

        String selection = COLUMN_TASK_NAME + " == ?";
        String[] selectionArgs = {taskName};


        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
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
            task = new TaskDBModel(name, expiration);
        }
        cursor.close();
        closeDB();

        return task;
    }
}
