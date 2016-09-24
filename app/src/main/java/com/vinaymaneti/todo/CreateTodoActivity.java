package com.vinaymaneti.todo;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.Locale;

public class CreateTodoActivity extends AppCompatActivity
        implements DatePickerFragment.onDatePickerSelectionListener,
        TimePickerFragment.onTimePickerSelectionListener {
    public static final String TIME_PICKER = "TimePicker";
    public static final String DATE_PICKER = "DatePicker";
    private AppCompatEditText titleTv;
    private AppCompatEditText descriptionTv;
    private DatabaseHandler mDatabaseHandler;
    private AppCompatTextView mDateAndTime;
    private AppCompatTextView dateSelector;
    private AppCompatTextView timeSelection;

    int databaseId;
    String mDate, mHours, mMinutes, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_edit_layout);

        mDatabaseHandler = new DatabaseHandler(this);
        Toolbar createToolbar = (Toolbar) findViewById(R.id.createEditToolBar);
        setSupportActionBar(createToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDateAndTime = (AppCompatTextView) findViewById(R.id.date_display);
        mDateAndTime.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.icon_clock, 0, 0, 0);

        titleTv = (AppCompatEditText) findViewById(R.id.etTodoTitle);
        descriptionTv = (AppCompatEditText) findViewById(R.id.descriptionTv);
        AppCompatImageView remainderImageView = (AppCompatImageView) findViewById(R.id.remainderIv);
        remainderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemainder();
            }
        });
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.createFab);

        boolean status = false;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            databaseId = extras.getInt(MainActivity.KEY_DATABASE_ID);
            Todo databaseHandlerTodo = mDatabaseHandler.getTodo(databaseId);
            if (databaseId >= 0 && databaseHandlerTodo != null) {
                status = databaseHandlerTodo.isChecked();
                titleTv.setText(databaseHandlerTodo.getTitle());
                titleTv.setSelection(titleTv.getText().length());
                descriptionTv.setText(databaseHandlerTodo.getNotes());
                descriptionTv.setSelection(descriptionTv.getText().length());
            }

            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icn_tick_mark_1));
            final boolean finalStatus = status;
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!validateTitle() || !validateNotes()) {
                        return;
                    }

                    fab.setEnabled(false);
                    Todo todoUpdateValues = new Todo();
                    todoUpdateValues.setTitle(titleTv.getText().toString());
                    todoUpdateValues.setNotes(descriptionTv.getText().toString());
                    todoUpdateValues.setChecked(finalStatus);
                    mDatabaseHandler.updateTodoList(new Todo(databaseId, todoUpdateValues.getTitle(), todoUpdateValues.getNotes(), todoUpdateValues.isChecked()));
                    Intent createIntent = new Intent(CreateTodoActivity.this, MainActivity.class);
                    startActivity(createIntent);
                    finish();
                }
            });

        } else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!validateTitle() || !validateNotes()) {
                        return;
                    }
                    Todo todoList = new Todo();
                    todoList.setTitle(titleTv.getText().toString());
                    todoList.setNotes(descriptionTv.getText().toString());
                    todoList.setChecked(false);
                    mDatabaseHandler.addTodo(new Todo(todoList.isChecked(), todoList.getTitle(), todoList.getNotes()));
                    Intent createIntent = new Intent(CreateTodoActivity.this, MainActivity.class);
                    startActivity(createIntent);
                }
            });
        }


    }

    private boolean validateTitle() {
        boolean value = true;

        String mTitle = titleTv.getText().toString();

        if (mTitle.isEmpty() || mTitle.trim().length() <= 0) {
            Snackbar.make(titleTv, R.string.title_empty, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            value = false;
        }

        return value;
    }

    private boolean validateNotes() {
        boolean value = true;

        String mNotes = descriptionTv.getText().toString();

        if (mNotes.isEmpty() || mNotes.trim().length() <= 0) {
            Snackbar.make(titleTv, R.string.notes_empty, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            value = false;
        }

        return value;
    }

    private void showRemainder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);
        dateSelector = (AppCompatTextView) dialogView.findViewById(R.id.dateSelection);
        timeSelection = (AppCompatTextView) dialogView.findViewById(R.id.timeSelection);
        if (mDate != null)
            dateSelector.setText(mDate);
        else
            dateSelector.setText(R.string.select_date);

        if (time != null)
            timeSelection.setText(time);
        else
            timeSelection.setText(R.string.select_time);


        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        timeSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        builder.setTitle(R.string.add_remainder);
        builder.setPositiveButton(R.string.c_string, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton(R.string.c_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showTimePickerDialog(View v) {
        DialogFragment timeDialogFragment = new TimePickerFragment();
        timeDialogFragment.show(getFragmentManager(), TIME_PICKER);
    }

    private void showDatePickerDialog(View v) {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), DATE_PICKER);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCompleteDateSelected(String date) {
        this.mDate = date;
        if (mDate != null) {
            mDateAndTime.setVisibility(View.VISIBLE);
            mDateAndTime.setText("  " + mDate + " ");
            dateSelector.setText(mDate);
        } else {
            mDateAndTime.setText(R.string.select_date);
            mDateAndTime.setVisibility(View.GONE);
        }

    }

    @Override
    public void onCompleteTimeSelected(String hours, String minutes) {
        this.mHours = hours;
        this.mMinutes = minutes;
        String time = String.format(Locale.ENGLISH, "%02d:%02d", Integer.parseInt(mHours), Integer.parseInt(mMinutes));
        if (time != null) {
            mDateAndTime.append(time);
            timeSelection.setText(time);
        } else
            mDateAndTime.setText(R.string.select_time);
    }

}
