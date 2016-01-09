package com.example.easytodoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.easytodoapp.database.TodoBaseHelper;
import com.example.easytodoapp.database.TodoCursorWrapper;
import com.example.easytodoapp.database.TodoDbSchema.TodoTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by AL_META on 12/07/2015.
 */

public class TodoLab {

    private static TodoLab sTodoLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private TodoLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TodoBaseHelper(mContext)
                .getWritableDatabase();
    }

    public static TodoLab getInstance(Context context) {
        if (sTodoLab == null) {
            synchronized (TodoLab.class) {
                if (sTodoLab == null) {
                    sTodoLab = new TodoLab(context);
                }
            }
        }
        return sTodoLab;
    }

    public void addTodo(Todo todo) {
        ContentValues values = getContentValues(todo);
        mDatabase.insert(TodoTable.TABLE_NAME, null, values);
    }

    public void deleteTodo(Todo todo) {
        mDatabase.delete(TodoTable.TABLE_NAME, TodoTable.Cols.UUID + " = ? ",
                new String[]{todo.getID().toString()});
    }

    public List<Todo> getTodos () {
        List<Todo> todos = new ArrayList<>();
        TodoCursorWrapper cursor = queryTodos(null, null);

        try {
            while (cursor.moveToNext()) {
                todos.add(cursor.getTodo());
            }
        } finally {
            cursor.close();
        }

        return todos;
    }

    public Todo getTodo (UUID id) {
        TodoCursorWrapper todoCursorWrapper = queryTodos(
                TodoTable.Cols.UUID + " = ? ", new String[]{id.toString()});

        try {
            if (todoCursorWrapper.getCount() == 0) {
                return null;
            }

            todoCursorWrapper.moveToFirst();
            return todoCursorWrapper.getTodo();
        } finally {
            todoCursorWrapper.close();
        }
    }

    public void updateTodo(Todo todo) {
        String uuidString = todo.getID().toString();
        ContentValues values = getContentValues(todo);
        mDatabase.update(TodoTable.TABLE_NAME, values,
                TodoTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private static ContentValues getContentValues(Todo todo) {
        ContentValues values = new ContentValues();
        values.put(TodoTable.Cols.UUID, todo.getID().toString());
        values.put(TodoTable.Cols.TITLE, todo.getTitle());
        values.put(TodoTable.Cols.DATE, todo.getDate().getTime());
        values.put(TodoTable.Cols.SOLVED, todo.isSolved() ? 1 : 0);
        values.put(TodoTable.Cols.CONTACT, todo.getContact());
        //Next, write to the new column in TodoLab.getContentValues( Todo ).

        return values;
    }

    private TodoCursorWrapper queryTodos(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TodoTable.TABLE_NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
//        return cursor;
        return new TodoCursorWrapper(cursor);
    }
}