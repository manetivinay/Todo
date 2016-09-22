package com.vinaymaneti.todo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String POSITION = "position";
    public static final String LIST_DATA = "list_data";
    public static final String ENTIRE_LIST = "entire_list";
    private static final int REQUEST_CODE = 1;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private TodoAdapter mTodoAdapter;
    private ArrayList<Todo> mTodoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        int total = mTodoList.size();
        initNavigationDrawer();
        initRecyclerView();
        initFloatActionButton();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mTodoAdapter = new TodoAdapter(mTodoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DivideritemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                Todo todo = mTodoList.get(position);
                Toast.makeText(getApplicationContext(), todo.getTitle() + "is selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(POSITION, position);
                bundle.putParcelable(LIST_DATA, todo);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mRecyclerView.setAdapter(mTodoAdapter);

        prepareTodoListData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Todo todo = bundle.getParcelable("newData");
            mTodoList.add(todo);
            mTodoAdapter.notifyDataSetChanged();
        }
    }

    private void prepareTodoListData() {
        Todo todo = new Todo(false, "Mad Max: Fury Road", "Action & Adventure");
        mTodoList.add(todo);

        todo = new Todo(true, "Inside Out", "Animation, Kids & Family");
        mTodoList.add(todo);

        todo = new Todo(true, "The Martian", "Science Fiction & Fantasy");
        mTodoList.add(todo);

        todo = new Todo(false, "Mission: Impossible Rogue Nation", "Action");
        mTodoList.add(todo);

        todo = new Todo(false, "Star Trek", "Science Fiction");
        mTodoList.add(todo);

        todo = new Todo(true, "Inside Out", "Animation, Kids & Family");
        mTodoList.add(todo);

        todo = new Todo(true, "The Martian", "Science Fiction & Fantasy");
        mTodoList.add(todo);

        todo = new Todo(false, "Mission: Impossible Rogue Nation", "Action");
        mTodoList.add(todo);

        todo = new Todo(false, "Star Trek", "Science Fiction");
        mTodoList.add(todo);

        todo = new Todo(true, "Inside Out", "Animation, Kids & Family");
        mTodoList.add(todo);

        todo = new Todo(true, "The Martian", "Science Fiction & Fantasy");
        mTodoList.add(todo);

        todo = new Todo(false, "Mission: Impossible Rogue Nation", "Action");
        mTodoList.add(todo);

        todo = new Todo(false, "Star Trek", "Science Fiction");
        mTodoList.add(todo);

        todo = new Todo(true, "Inside Out", "Animation, Kids & Family");
        mTodoList.add(todo);

        todo = new Todo(true, "The Martian", "Science Fiction & Fantasy");
        mTodoList.add(todo);

        todo = new Todo(false, "Mission: Impossible Rogue Nation", "Action");
        mTodoList.add(todo);

        todo = new Todo(false, "Star Trek", "Science Fiction");
        mTodoList.add(todo);

        mTodoAdapter.notifyDataSetChanged();

    }

    private void initFloatActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent createIntent = new Intent(MainActivity.this, CreateTodoActivity.class);
                createIntent.putParcelableArrayListExtra(ENTIRE_LIST, mTodoList);
                startActivityForResult(createIntent, REQUEST_CODE);
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
        TextView tvTitle = (TextView) header.findViewById(R.id.tv_email);


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
}
