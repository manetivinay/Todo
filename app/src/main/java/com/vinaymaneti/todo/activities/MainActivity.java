package com.vinaymaneti.todo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vinaymaneti.todo.listener.ClickListener;
import com.vinaymaneti.todo.db.DatabaseHandler;
import com.vinaymaneti.todo.misc.DivideritemDecoration;
import com.vinaymaneti.todo.R;
import com.vinaymaneti.todo.model.Todo;
import com.vinaymaneti.todo.adapter.TodoAdapter;
import com.vinaymaneti.todo.app.TodoApplication;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ClickListener {
    public static final String KEY_DATABASE_ID = "database_id";
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private TodoAdapter mTodoAdapter;
    private List<Todo> mTodoList = new ArrayList<>();
    private DatabaseHandler mDatabaseHandler;
    RecyclerView recyclerView;
    AppCompatTextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        if (!((TodoApplication) getApplicationContext()).toasted) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, R.string.welcome_app_message, Snackbar.LENGTH_LONG);

            snackbar.show();
            ((TodoApplication) getApplicationContext()).toasted = true;
        }

        mDatabaseHandler = new DatabaseHandler(this);
        initNavigationDrawer();
        initRecyclerView();
        initFloatActionButton();
        initFilterIv();
    }

    private void initFilterIv() {
        AppCompatImageView filterIv = (AppCompatImageView) findViewById(R.id.filterIV);
        filterIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertToDeleteAllMarkedTask();
            }
        });
    }

    private void showAlertToDeleteAllMarkedTask() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.delete_all_marked_title_text);
        builder.setMessage(R.string.delete_message_text);

        String positiveText = getString(R.string.ok);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTodoAdapter.notifyDeleteItemToAdapter();
                checkShowEmptyView(mTodoAdapter.getTodoList());
                Snackbar snackbar = Snackbar
                        .make(mToolbar, R.string.deleted_marked_task_message, Snackbar.LENGTH_LONG);
                snackbar.show();
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

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyTextView = (AppCompatTextView) findViewById(R.id.emptyTextView);
        mTodoList = mDatabaseHandler.getAllTodoList();
        checkShowEmptyView(mTodoList);

        mTodoAdapter = new TodoAdapter(mTodoList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DivideritemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mTodoAdapter);
        mTodoAdapter.notifyDataSetChanged();

    }

    private void checkShowEmptyView(List<Todo> todoList) {
        if (todoList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DetailedActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            mTodoAdapter.notifyDataSetChanged();
        }
    }

    private void initFloatActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(MainActivity.this, CreateTodoActivity.class);
                startActivity(createIntent);
            }
        });
    }

    private void initNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.todoList:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.statistics:
                        Intent statisticsIntent = new Intent(MainActivity.this, StatisticsActivity.class);
                        startActivity(statisticsIntent);
                        mDrawerLayout.closeDrawers();
                        break;
                }

                return true;
            }
        });

        View header = navigationView.getHeaderView(0);
        TextView appNameTv = (TextView) header.findViewById(R.id.app_name_tv);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @Override
    public void onClicked(View view, int position, List<Todo> todoList) {
        //Check if an item was deleted, but the user clicked it before the UI removed it
        if (position != RecyclerView.NO_POSITION) {
            int databaseId = todoList.get(position).getId();
            if (databaseId > 0) {
                Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
                intent.putExtra(KEY_DATABASE_ID, databaseId);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onCheckBoxSelected(View view, int position, boolean isChecked) {
        Todo todo = mTodoList.get(position);
        int id = todo.getId();
        todo.setChecked(isChecked);
        mDatabaseHandler.updateCheckboxStatus(todo);
    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
