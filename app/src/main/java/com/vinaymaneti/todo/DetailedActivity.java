package com.vinaymaneti.todo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class DetailedActivity extends AppCompatActivity {

    private RelativeLayout mDetailedRelativeLayout;
    private AppCompatCheckBox mDetailedCheckbox;
    private AppCompatTextView mDetailedTitleTextView;
    private AppCompatTextView mDetailedNoteTextView;
    private Toolbar mDetailedToolbar;
    private FloatingActionButton mDetailEditButton;

    int position;
    Todo todoValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_activity);

        initUi();
        initToolbar();

        mDetailEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt(MainActivity.POSITION);
            todoValue = bundle.getParcelable(MainActivity.LIST_DATA);
        }

        if (position >= 0 && todoValue != null) {
            mDetailedCheckbox.setChecked(todoValue.isChecked());
            mDetailedTitleTextView.setText(todoValue.getTitle());
            mDetailedNoteTextView.setText(todoValue.getNotes());
        }
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
        mDetailedCheckbox = (AppCompatCheckBox) findViewById(R.id.detailedSelectableCheckbox);
        mDetailedTitleTextView = (AppCompatTextView) findViewById(R.id.detailedTitleTv);
        mDetailedNoteTextView = (AppCompatTextView) findViewById(R.id.detailedNoteTv);
        mDetailEditButton = (FloatingActionButton) findViewById(R.id.editFab);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
