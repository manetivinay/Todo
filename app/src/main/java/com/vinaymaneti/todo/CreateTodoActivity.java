package com.vinaymaneti.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class CreateTodoActivity extends AppCompatActivity {
    public static final String NEW_DATA = "newData";
    private Toolbar mCreateToolbar;
    private AppCompatEditText titleTv;
    private AppCompatEditText descriptionTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_edit_layout);

        mCreateToolbar = (Toolbar) findViewById(R.id.createEditToolBar);
        setSupportActionBar(mCreateToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleTv = (AppCompatEditText) findViewById(R.id.etTodoTitle);
        descriptionTv = (AppCompatEditText) findViewById(R.id.descriptionTv);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.createFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Todo todoList = new Todo();
                todoList.setChecked(false);
                todoList.setTitle(titleTv.getText().toString());
                todoList.setNotes(descriptionTv.getText().toString());
                Intent createIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(NEW_DATA, todoList);
                createIntent.putExtras(bundle);
                setResult(RESULT_OK, createIntent);
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
