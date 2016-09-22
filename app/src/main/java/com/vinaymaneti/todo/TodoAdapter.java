package com.vinaymaneti.todo;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinaymaneti on 9/21/16.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<Todo> mTodoList;

    public TodoAdapter(List<Todo> todoList) {
        mTodoList = todoList;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_row, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        Todo todo = mTodoList.get(position);
        holder.mAppCompatCheckBox.setChecked(todo.isChecked());
        holder.mTitle.setText(todo.getTitle());

    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mRelativeLayout;
        public AppCompatCheckBox mAppCompatCheckBox;
        public TextView mTitle;

        public TodoViewHolder(View itemView) {
            super(itemView);
            mAppCompatCheckBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
            mTitle = (TextView) itemView.findViewById(R.id.title_tv);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.recycler_view);
        }
    }
}
