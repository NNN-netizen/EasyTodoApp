package com.example.easytodoapp.database;


import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.easytodoapp.Todo;

import com.example.easytodoapp.database.TodoDbSchema.TodoTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by AL_META on 12/07/2015.
 */

public class TodoCursorWrapper extends CursorWrapper {

    public TodoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Todo getTodo() {
        String uuidString = getString(getColumnIndex(TodoTable.Cols.UUID));
        String title = getString(getColumnIndex(TodoTable.Cols.TITLE));
        long date = getLong(getColumnIndex(TodoTable.Cols.DATE));
        int solved = getInt(getColumnIndex(TodoTable.Cols.SOLVED));
        String contact = getString(getColumnIndex(TodoTable.Cols.CONTACT));

        Todo todo = new Todo(UUID.fromString(uuidString));
        todo.setTitle(title);
        todo.setDate(new Date(date));
        todo.setSolved(solved != 0);
        todo.setContact(contact);

        return todo;
    }
}
