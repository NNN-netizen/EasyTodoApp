package com.example.easytodoapp.database;

/**
 * Created by AL_META on 12/07/2015.
 */

public class TodoDbSchema {

    public static final class TodoTable {

        public static final String TABLE_NAME = "todo";

        public static final class Cols {
            private static final String ID = "_id";
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String CONTACT = "contact";

        }

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Cols.UUID + ", "
                + Cols.TITLE + ", "
                + Cols.DATE + ", "
                + Cols.SOLVED
                + ")";

        public static final String ALTER_TABLE = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN "
                + Cols.CONTACT;
    }
}
