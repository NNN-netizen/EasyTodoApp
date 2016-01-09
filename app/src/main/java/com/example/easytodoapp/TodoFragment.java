package com.example.easytodoapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by AL_META on 12/07/2015.
 */

public class TodoFragment extends Fragment {

    private static final String ARG_TODO_ID = "todo_id";

    //Added a constant for the DatePickerFragment’s tag
    private static final String DIALOG_DATE = "DialogDate";

    //Added a constant for the DatePickerFragment’s tag
    private static final String DIALOG_TIME = "DialogTime";

    public static final String EXTRA_DATE = "date";

    private static final int REQUEST_CONTACT = 1;

    private static final int REQUEST_DATE = 0;

    private Todo mTodo;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private Button mContactButton;
    private Button mReportButton;
    private Button mDialButton;


    public static TodoFragment newInstance(UUID todoId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TODO_ID, todoId);

        TodoFragment fragment = new TodoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID todoId = (UUID) getArguments().getSerializable(ARG_TODO_ID);

        mTodo = TodoLab.getInstance(getContext()).getTodo(todoId);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        mTitleField = (EditText) view.findViewById(R.id.todo_title);
        mTitleField.setText(mTodo.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTodo.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mDateButton = (Button) view.findViewById(R.id.todo_date);

        updateDate();

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE) {
                    FragmentManager fragmentManager = getFragmentManager();
                    DatePickerFragment datePickerFragment =
                            DatePickerFragment.newInstance(mTodo.getDate());
                    datePickerFragment.setTargetFragment(TodoFragment.this, REQUEST_DATE);
                    datePickerFragment.show(fragmentManager, DIALOG_DATE);
                } else if (getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_PORTRAIT) {
                    Intent intent = new Intent(TodoFragment.this.getActivity(),
                            DatePickerActivity.class);
                    intent.putExtra(EXTRA_DATE, mTodo.getDate());
                    startActivityForResult(intent, REQUEST_DATE);
                }

            }
        });

        mTimeButton = (Button) view.findViewById(R.id.todo_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE) {
                    FragmentManager fragmentManager = getFragmentManager();
                    TimePickerFragment timePickerFragment =
                            TimePickerFragment.newInstance(mTodo.getDate());
                    timePickerFragment.setTargetFragment(TodoFragment.this, REQUEST_DATE);
                    timePickerFragment.show(fragmentManager, DIALOG_TIME);
                } else if (getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_PORTRAIT) {
                    Intent intent = new Intent(TodoFragment.this.getActivity(),
                            TimePickerActivity.class);
                    intent.putExtra(EXTRA_DATE, mTodo.getDate());
                    startActivityForResult(intent, REQUEST_DATE);
                }
            }
        });

        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.todo_solved);
        mSolvedCheckBox.setChecked(mTodo.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the Todos solved/done property
                mTodo.setSolved(isChecked);
            }
        });

        mReportButton = (Button) view.findViewById(R.id.todo_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareCompat.IntentBuilder intentBuilder =
                        ShareCompat.IntentBuilder.from(getActivity());
                Intent intent = intentBuilder
                        .setType("text/plain")
                        .setText(getTodoReport())
                        .setSubject(getString(R.string.todo_report_contact))
                        .setChooserTitle(getString(R.string.send_report))
                        .createChooserIntent();
                startActivity(intent);
            }
        });

        final Intent pickIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);

        mContactButton = (Button) view.findViewById(R.id.todo_contact);
        mContactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickIntent, REQUEST_CONTACT);
            }
        });

        if (mTodo.getContact() != null) {
            mContactButton.setText(mTodo.getContact());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickIntent,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mContactButton.setEnabled(false);
        }

        return view;

    }


    @Override
    public void onPause() {
        super.onPause();

        TodoLab.getInstance(getContext()).updateTodo(mTodo);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(PickerDialogFragment.EXTRA_DATE);
            mTodo.setDate(date);
            updateDate();
            updateTime();

        } else if (requestCode == REQUEST_CONTACT) {

            Uri contactUri = data.getData();

            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = getActivity().getContentResolver().query(contactUri, queryFields,
                    null, null, null);

            if (cursor != null) {
                try {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        String contact = cursor.getString(
                                cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        mTodo.setContact(contact);
                        mContactButton.setText(mTodo.getContact());
                    }
                } finally {
                    cursor.close();
                }

            }
        }
    }


    private void updateDate() {
        mDateButton.setText(mTodo.getFormattedDate());
    }

    private void updateTime() {
        mTimeButton.setText(mTodo.getFormattedTime());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_todo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_todo:
                if (mTodo != null) {
                    TodoLab.getInstance(getContext()).deleteTodo(mTodo);
                    getActivity().finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getTodoReport() {
        String solvedString = null;
        if (mTodo.isSolved()) {
            solvedString = getString(R.string.todo_report_solved);
        } else {
            solvedString = getString(R.string.todo_report_unsolved);
        }


        String dateFormat = "EEE, MMM dd";
        String dateString = new SimpleDateFormat(dateFormat, Locale.US)
                .format(mTodo.getDate());


        String contact = mTodo.getContact();
        if (contact == null) {
            contact = getString(R.string.todo_report_no_contact);
        } else {
            contact = getString(R.string.todo_report_contact, contact);
        }

        return getString(R.string.todo_report, mTodo.getTitle(), dateString, solvedString, contact);
    }
}





