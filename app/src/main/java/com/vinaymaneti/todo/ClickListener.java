package com.vinaymaneti.todo;

import android.view.View;

/**
 * Created by vinaymaneti on 9/21/16.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
