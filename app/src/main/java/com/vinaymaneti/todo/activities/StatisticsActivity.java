package com.vinaymaneti.todo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vinaymaneti.todo.db.DatabaseHandler;
import com.vinaymaneti.todo.R;

public class StatisticsActivity extends AppCompatActivity {

    private DatabaseHandler mDatabaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        mDatabaseHandler = new DatabaseHandler(this);
        Toolbar statisticsToolbar = (Toolbar) findViewById(R.id.statisticsToolbar);
        AppCompatTextView completedTasks = (AppCompatTextView) findViewById(R.id.completeTaskLabel);
        AppCompatTextView totalNumberOfTasks = (AppCompatTextView) findViewById(R.id.totalTaskLabel);
        AppCompatTextView activeNumberOfTasks = (AppCompatTextView) findViewById(R.id.activeTaskLabel);
        setSupportActionBar(statisticsToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        int totalTasks = mDatabaseHandler.getTodoListCount();
        int doneTasks = mDatabaseHandler.getTodoListCompletedCount();

        if (totalTasks > 0 || doneTasks > 0) {
            totalNumberOfTasks.setText(String.valueOf(totalTasks));
            completedTasks.setText(String.valueOf(doneTasks));
            int inCompletedTasks = totalTasks - doneTasks;
            activeNumberOfTasks.setText(String.valueOf(inCompletedTasks));
        } else {
            totalNumberOfTasks.setText("0");
            completedTasks.setText("0");
            activeNumberOfTasks.setText("0");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
