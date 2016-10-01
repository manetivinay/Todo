package com.vinaymaneti.todo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.vinaymaneti.todo.db.DatabaseHandler;
import com.vinaymaneti.todo.R;
import com.vinaymaneti.todo.model.Todo;
import com.vinaymaneti.todo.misc.Util;

public class DetailedActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 12;
    private RelativeLayout mDetailedRelativeLayout;
    private AppCompatTextView mDetailedTitleTextView;
    private AppCompatTextView mDetailedNoteTextView;
    private Toolbar mDetailedToolbar;
    private FloatingActionButton mDetailEditButton;
    private DatabaseHandler mDatabaseHandler;
    private AppCompatImageView mDeleteTodo;
    private AppCompatTextView mDetailedDateTimeView;
    private AppCompatTextView mPriorityTextView;
    private AppCompatTextView mRemainderTextView;
    private AppCompatTextView mPriorityStatusValue;
    private View centreVerticalLine;

    int database_id;
    Todo mDatabaseHandlerTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_activity);
        mDatabaseHandler = new DatabaseHandler(this);
        initUi();
        initToolbar();
        initFloatActionButton();
        onClickEditButton();
        onClickDelete();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            database_id = bundle.getInt(MainActivity.KEY_DATABASE_ID);
            mDatabaseHandlerTodo = mDatabaseHandler.getTodo(database_id);
        }

        if (database_id >= 0 && mDatabaseHandlerTodo != null) {
            mDetailedTitleTextView.setText(mDatabaseHandlerTodo.getTitle());
            mDetailedNoteTextView.setText(mDatabaseHandlerTodo.getNotes());
            if (mDatabaseHandlerTodo.getDateTimeRemainder() != null) {
                centreVerticalLine.setVisibility(View.VISIBLE);
                mRemainderTextView.setVisibility(View.VISIBLE);
                mDetailedDateTimeView.setVisibility(View.VISIBLE);
                mDetailedDateTimeView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.icon_clock, 0, 0, 0);
                String dateTimeValue = "   " + mDatabaseHandlerTodo.getDateTimeRemainder();
                mDetailedDateTimeView.setText(dateTimeValue);
            }

            if (mDatabaseHandlerTodo.getPriorityStatus() > 0 && mDatabaseHandlerTodo.getPriorityStatus() < 4) {
                centreVerticalLine.setVisibility(View.VISIBLE);
                mPriorityTextView.setVisibility(View.VISIBLE);
                mPriorityStatusValue.setVisibility(View.VISIBLE);
                mPriorityStatusValue.setText(Util.getPriorityString(mDatabaseHandlerTodo.getPriorityStatus()).toUpperCase());
            }
        }
    }

    private void onClickDelete() {
        mDeleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeletingTodoTask();
            }
        });
    }


    private void showDeletingTodoTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.delete_title_text);
        builder.setMessage(R.string.delete_message_text);

        String positiveText = getString(R.string.ok);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Todo deleteTask = mDatabaseHandler.getTodo(database_id);
                mDatabaseHandler.deleteTodoList(deleteTask);
                startActivityForResult(new Intent(DetailedActivity.this, MainActivity.class), REQUEST_CODE);
            }
        });

        String negativeText = getString(R.string.cancel);
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onClickEditButton() {
        mDetailEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(DetailedActivity.this, CreateTodoActivity.class);
                intent.putExtra(MainActivity.KEY_DATABASE_ID, database_id);
                startActivity(intent);
            }
        });
    }

    private void initFloatActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.editFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(DetailedActivity.this, CreateTodoActivity.class);
                createIntent.putExtra(MainActivity.KEY_DATABASE_ID, database_id);
                startActivity(createIntent);
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(mDetailedToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initUi() {
        mDetailedToolbar = (Toolbar) findViewById(R.id.detailToolBar);
        mDetailedRelativeLayout = (RelativeLayout) findViewById(R.id.detailedRelativeLayout);
        mDetailedTitleTextView = (AppCompatTextView) findViewById(R.id.detailedTitleTv);
        mDetailedNoteTextView = (AppCompatTextView) findViewById(R.id.detailedNoteTv);
        mDetailEditButton = (FloatingActionButton) findViewById(R.id.editFab);
        mDeleteTodo = (AppCompatImageView) findViewById(R.id.deleteTodo);
        mDetailedDateTimeView = (AppCompatTextView) findViewById(R.id.detailed_date_display);
        mPriorityTextView = (AppCompatTextView) findViewById(R.id.priorityTextView);
        mRemainderTextView = (AppCompatTextView) findViewById(R.id.remainderTextView);
        mPriorityStatusValue = (AppCompatTextView) findViewById(R.id.priorityStatus);
        centreVerticalLine = findViewById(R.id.centerShim);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        setResult(RESULT_OK);
        super.finish();
    }
}
