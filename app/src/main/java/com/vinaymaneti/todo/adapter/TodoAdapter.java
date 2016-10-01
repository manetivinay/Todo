package com.vinaymaneti.todo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vinaymaneti.todo.db.DatabaseHandler;
import com.vinaymaneti.todo.R;
import com.vinaymaneti.todo.misc.Util;
import com.vinaymaneti.todo.listener.ClickListener;
import com.vinaymaneti.todo.model.Todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vinaymaneti on 9/21/16.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<Todo> mTodoList;
    private ClickListener mClickListener;
    private ArrayList<Integer> checkedValues = new ArrayList<>();
    private Context context;

    public TodoAdapter(List<Todo> todoList, ClickListener clickListener) {
        mTodoList = todoList;
        mClickListener = clickListener;
        for (int i = 0; i < mTodoList.size(); i++) {
            Todo todo = mTodoList.get(i);
            if (todo.isChecked()) {
                checkedValues.add(i);
            }
        }
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_row, parent, false);
        context = parent.getContext();
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, final int position) {
        Todo todo = mTodoList.get(position);
        holder.mAppCompatCheckBox.setChecked(todo.isChecked());
        holder.mTitle.setText(todo.getTitle());
        int priorityStatus = todo.getPriorityStatus();
        if (priorityStatus > 0 && priorityStatus < 4) {
            holder.mPriorityLabel.setVisibility(View.VISIBLE);
            holder.mPriorityLabel.setText(Util.getPriorityString(priorityStatus).toUpperCase());
        }
    }

    private ArrayList<Integer> getCheckedValues() {
        return checkedValues;
    }

    private void setCheckedValues(ArrayList<Integer> checkedValues) {
        this.checkedValues = checkedValues;
    }


    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void notifyDeleteItemToAdapter() {
        DatabaseHandler databaseHandler;
        for (int i = getCheckedValues().size(); i > 0; i--) {
            databaseHandler = new DatabaseHandler(context);
            if (getCheckedValues().get(0) >= 0) {
                Todo needToDelete = mTodoList.get(getCheckedValues().get(0));
                mTodoList.remove(getCheckedValues().get(0));
                notifyItemRemoved(getCheckedValues().get(0));
                checkedValues.remove(checkedValues.get(0));
                if (needToDelete != null) {
                    databaseHandler.deleteTodoList(needToDelete);
                    notifyDataSetChanged();
                    mTodoList = databaseHandler.getAllTodoList();
                }
                if (checkedValues.size() >= 1)
                    decrementList(checkedValues);
            }
        }
        checkedValues.clear();
    }

    private ArrayList<Integer> decrementList(ArrayList<Integer> position) {
        for (int i = 0; i < position.size(); i++) {
            int decrement = position.get(i);
            decrement--;
            position.set(i, decrement);
            setCheckedValues(position);
        }

        return position;
    }

    public List<Todo> getTodoList() {
        return mTodoList;
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mRelativeLayout;
        AppCompatCheckBox mAppCompatCheckBox;
        TextView mTitle;
        AppCompatTextView mPriorityLabel;


        TodoViewHolder(final View itemView) {
            super(itemView);
            mAppCompatCheckBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
            mTitle = (TextView) itemView.findViewById(R.id.title_tv);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.individualListRow);
            mPriorityLabel = (AppCompatTextView) itemView.findViewById(R.id.priorityLabel);

            mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onClicked(v, getAdapterPosition(), mTodoList);
                }
            });

            //checkbox
            mAppCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mClickListener != null) {
                        if (buttonView.isChecked()) {
                            buttonView.setChecked(true);
                            mClickListener.onCheckBoxSelected(buttonView, getAdapterPosition(), isChecked);
                            int i = getAdapterPosition();
                            if (!checkedValues.contains(i))
                                checkedValues.add(i);
                            Collections.sort(checkedValues);
                            mTitle.setPaintFlags(mTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mTitle.getContext(), R.color.colorDivider));
                        } else {
                            buttonView.setChecked(false);
                            int unCheckValuePosition;
                            unCheckValuePosition = checkedValues.indexOf(getAdapterPosition());
                            if (unCheckValuePosition > 0)
                                checkedValues.remove(unCheckValuePosition);
                            mClickListener.onCheckBoxSelected(buttonView, getAdapterPosition(), isChecked);
                            mTitle.setPaintFlags(mTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mTitle.getContext(), R.color.colorIcons));
                        }
                    }
                }
            });
        }
    }

}
