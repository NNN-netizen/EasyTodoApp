package com.example.easytodoapp;

import android.support.v4.app.Fragment;

import java.util.Date;

/**
 * Created by AL_META on 12/07/2015.
 */

public class TimePickerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        Date date = (Date) getIntent().getSerializableExtra(TodoFragment.EXTRA_DATE);

        return TimePickerFragment.newInstance(date);
    }
}

