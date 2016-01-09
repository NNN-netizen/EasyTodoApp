package com.example.easytodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by AL_META on 12/07/2015.
 */

public class TodoListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";


    private RecyclerView mTodoRecyclerView;
    private TodoAdapter mTodoAdapter;

    private LinearLayout mEmptyViewLinearLayout;
    private Button mNewTodoButton;

    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        mTodoRecyclerView = (RecyclerView) view.findViewById(R.id.todo_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mTodoRecyclerView.setLayoutManager(linearLayoutManager);

        mEmptyViewLinearLayout = (LinearLayout) view.findViewById(R.id.empty_view);
        mNewTodoButton = (Button) view.findViewById(R.id.new_todo_button);
        mNewTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTodo();
            }
        });

    return view;

}

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    private void updateUI() {

        TodoLab todoLab = TodoLab.getInstance(getContext());
        List<Todo> todos = todoLab.getTodos();

        if (mTodoAdapter == null) {
            mTodoAdapter = new TodoAdapter(todos);
            mTodoRecyclerView.setAdapter(mTodoAdapter);
        } else {
//
            mTodoAdapter.setTodos(todos);
            mTodoAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
        int todoCount = TodoLab.getInstance(getContext()).getTodos().size();
        mEmptyViewLinearLayout.setVisibility(todoCount == 0 ? View.VISIBLE : View.GONE);
    }

private class TodoHolder extends RecyclerView.ViewHolder {

    private Todo mTodo;
    //        public TextView mTitleTextView;
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private CheckBox mSolvedCheckBox;

    public TodoHolder(final View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TodoPagerActivity.newIntent(getContext(), mTodo.getID());
                startActivity(intent);
            }
        });

        mTitleTextView = (TextView)
                itemView.findViewById(R.id.list_item_todo_title_text_view);
        mDateTextView = (TextView)
                itemView.findViewById(R.id.list_item_todo_date_text_view);
        mSolvedCheckBox = (CheckBox)
                itemView.findViewById(R.id.list_item_todo_solved_check_box);

    }

    public void bindTodo(Todo todo) {
        mTodo = todo;
        mTitleTextView.setText(mTodo.getTitle());
        mSolvedCheckBox.setChecked(mTodo.isSolved());
        mDateTextView.setText(mTodo.getFormattedDate());
    }
}

private class TodoAdapter extends RecyclerView.Adapter<TodoHolder> {

    private List<Todo> mTodos;

    public TodoAdapter(List<Todo> todos) {
        mTodos = todos;
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View view = layoutInflater.inflate(R.layout.list_item_todo, parent, false);

        return new TodoHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoHolder holder, int position) {
        Todo todo = mTodos.get(position);
        holder.bindTodo(todo);
    }

    @Override
    public int getItemCount() {
        return mTodos.size();
    }

    public void setTodos(List<Todo> todos) {
        mTodos = todos;
    }
}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_todo_list, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            menuItem.setTitle(R.string.hide_subtitle);
        } else {
            menuItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_todo:
                newTodo();
                return true;
            case R.id.menu_item_show_subtitle:        // Now that updateSubtitle is defined, We call the
                mSubtitleVisible = !mSubtitleVisible; // method when the user presses on the new action item.
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override  //The subtitle should appear as expected in the recreated view
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }


    private void updateSubtitle() {
        TodoLab todoLab = TodoLab.getInstance(getContext());
        int todoCount = todoLab.getTodos().size();

        String subtitle;

        if (todoCount == 1) {
            subtitle = getString(R.string.subtitle_singular_format);
        } else {
            subtitle = getString(R.string.subtitle_plural_format, todoCount);
        }

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(subtitle);

    }

    private void newTodo() {
        Todo todo = new Todo();
        TodoLab.getInstance(getContext()).addTodo(todo);
        Intent intent = TodoPagerActivity.newIntent(getContext(), todo.getID());
        startActivity(intent);
    }
}
