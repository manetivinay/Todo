package com.vinaymaneti.todo;

import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vinaymaneti on 9/21/16.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<Todo> mTodoList;
    private ClickListener mClickListener;


    public TodoAdapter(List<Todo> todoList, ClickListener clickListener) {
        mTodoList = todoList;
        mClickListener = clickListener;

    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_row, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, final int position) {
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
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.individualListRow);

            mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onClick(v, getAdapterPosition());
                }
            });

            mAppCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mClickListener.onCheckBoxSelected(buttonView, getAdapterPosition(), isChecked);
                    if (isChecked) {
                        mTitle.setPaintFlags(mTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mTitle.getContext(), R.color.colorDivider));
                    } else {
                        mTitle.setPaintFlags(mTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mTitle.getContext(), R.color.colorIcons));
                    }
                }
            });
        }
    }
}
