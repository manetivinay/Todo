package com.vinaymaneti.todo.listener;

import android.view.View;

import com.vinaymaneti.todo.model.Todo;

import java.util.List;

/**
 * Created by vinaymaneti on 9/21/16.
 */

public interface ClickListener {
    void onClicked(View view, int position, List<Todo> todoList);

    // i'm not using as of now
    void onClick(View view, int position);

    void onCheckBoxSelected(View view, int position, boolean isChecked);

    void onLongClick(View view, int position);
}
