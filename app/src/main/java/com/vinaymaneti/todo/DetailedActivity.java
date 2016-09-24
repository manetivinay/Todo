package com.vinaymaneti.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class DetailedActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 12;
    private RelativeLayout mDetailedRelativeLayout;
    private AppCompatTextView mDetailedTitleTextView;
    private AppCompatTextView mDetailedNoteTextView;
    private Toolbar mDetailedToolbar;
    private FloatingActionButton mDetailEditButton;
    private DatabaseHandler mDatabaseHandler;
    private AppCompatImageView mDeleteTodo;

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

        mDeleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo deleteTask = mDatabaseHandler.getTodo(database_id);
                deleteTask.getId();
                mDatabaseHandler.deleteTodoList(deleteTask);
                startActivityForResult(new Intent(DetailedActivity.this, MainActivity.class), REQUEST_CODE);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            database_id = bundle.getInt(MainActivity.KEY_DATABASE_ID);
            mDatabaseHandlerTodo = mDatabaseHandler.getTodo(database_id);
        }

        if (database_id >= 0 && mDatabaseHandlerTodo != null) {
            mDetailedTitleTextView.setText(mDatabaseHandlerTodo.getTitle());
            mDetailedNoteTextView.setText(mDatabaseHandlerTodo.getNotes());
        }
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
        mDeleteTodo = (AppCompatImageView) findViewById(R.id.deleteTodo
        );
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
