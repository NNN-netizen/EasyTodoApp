package com.example.easytodoapp;

import android.support.v4.app.Fragment;

/**
 * Created by AL_META on 12/07/2015.
 */

public class TodoListActivity extends SingleFragmentActivity {

        @Override
        protected Fragment createFragment() {
            return new TodoListFragment();
        }
    }


