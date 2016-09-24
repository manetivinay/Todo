package com.vinaymaneti.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_DATABASE_ID = "database_id";
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private TodoAdapter mTodoAdapter;
    private List<Todo> mTodoList = new ArrayList<>();
    private DatabaseHandler mDatabaseHandler;
    private AppCompatTextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDatabaseHandler = new DatabaseHandler(this);
        initNavigationDrawer();
        initRecyclerView();
        initFloatActionButton();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyTextView = (AppCompatTextView) findViewById(R.id.emptyTextView);
        mTodoList = mDatabaseHandler.getAllTodolist();
        if (mTodoList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }

        int total = mDatabaseHandler.getTodoListCount();
        Log.d("total", String.valueOf(total));
        mTodoAdapter = new TodoAdapter(mTodoList, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Todo todo = mTodoList.get(position);
                int id = todo.getId();
                Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
                intent.putExtra(KEY_DATABASE_ID, id);
                startActivity(intent);
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
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DivideritemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mTodoAdapter);
        mTodoAdapter.notifyDataSetChanged();

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
}
