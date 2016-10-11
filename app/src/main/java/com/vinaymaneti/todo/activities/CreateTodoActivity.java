package com.vinaymaneti.todo.activities;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.vinaymaneti.todo.R;
import com.vinaymaneti.todo.db.DatabaseHandler;
import com.vinaymaneti.todo.fragments.DatePickerFragment;
import com.vinaymaneti.todo.fragments.TimePickerFragment;
import com.vinaymaneti.todo.misc.PriorityStatusCode;
import com.vinaymaneti.todo.misc.Util;
import com.vinaymaneti.todo.model.Todo;

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
    private AppCompatImageView remainderImageView;
    private Toolbar createToolbar;
    private FloatingActionButton fab;
    int databaseId;
    String mDate, mHours, mMinutes, formatTime;
    PriorityStatusCode priorityStatusCode = null;
    int priorityCode = 0;
    String lastDateTimeValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_edit_layout);
        initUI();
        initToolbar();
        mDatabaseHandler = new DatabaseHandler(this);
        onClickRemainderButton();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getSupportActionBar().setTitle(R.string.tool_bar_edit_title);
            existingDataEditFunctionality(fab, extras);
        } else {
            getSupportActionBar().setTitle(R.string.tool_bar_create_title);
            createNewFunctionality(fab);
        }
    }

    private void onClickRemainderButton() {
        remainderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemainder(mDatabaseHandler);
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(createToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initUI() {
        createToolbar = (Toolbar) findViewById(R.id.createEditToolBar);
        mDateAndTime = (AppCompatTextView) findViewById(R.id.date_display);
        mDateAndTime.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.icon_clock, 0, 0, 0);

        titleTv = (AppCompatEditText) findViewById(R.id.etTodoTitle);
        descriptionTv = (AppCompatEditText) findViewById(R.id.descriptionTv);
        fab = (FloatingActionButton) findViewById(R.id.createFab);
        remainderImageView = (AppCompatImageView) findViewById(R.id.remainderIv);
    }

    private void createNewFunctionality(FloatingActionButton fab) {
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
                todoList.setDateTimeRemainder(getDateTime(mDate, formatTime));
                todoList.setPriorityStatus(getPriorityCode());
                mDatabaseHandler.addTodo(new Todo(todoList.isChecked(), todoList.getTitle(), todoList.getNotes(), todoList.getDateTimeRemainder(), todoList.getPriorityStatus()));
                Intent createIntent = new Intent(CreateTodoActivity.this, MainActivity.class);
                startActivity(createIntent);
            }
        });
    }

    private void existingDataEditFunctionality(final FloatingActionButton fab, Bundle extras) {
        boolean status = false;
        databaseId = extras.getInt(MainActivity.KEY_DATABASE_ID);
        final Todo databaseHandlerTodo = mDatabaseHandler.getTodo(databaseId);
        if (databaseId >= 0 && databaseHandlerTodo != null) {
            status = databaseHandlerTodo.isChecked();
            titleTv.setText(databaseHandlerTodo.getTitle());
            titleTv.setSelection(titleTv.getText().length());
            descriptionTv.setText(databaseHandlerTodo.getNotes());
            descriptionTv.setSelection(descriptionTv.getText().length());
            if (databaseHandlerTodo.getDateTimeRemainder() != null) {
                mDateAndTime.setVisibility(View.VISIBLE);
                String displayDateTImeValue = "   " + databaseHandlerTodo.getDateTimeRemainder();
                mDateAndTime.setText(displayDateTImeValue);
            }
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
                if (lastDateTimeValue == null)
                    if (!mDateAndTime.getText().toString().isEmpty())
                        todoUpdateValues.setDateTimeRemainder(mDateAndTime.getText().toString());
                    else
                        todoUpdateValues.setDateTimeRemainder(null);
                else
                    todoUpdateValues.setDateTimeRemainder(lastDateTimeValue);


//                if (mDate == null) {
////                    todoUpdateValues.setDateTimeRemainder(databaseHandlerTodo.getDateTimeRemainder());
//                    todoUpdateValues.setDateTimeRemainder(mDateAndTime.getText().toString());
//                } else {
//                    todoUpdateValues.setDateTimeRemainder(getDateTime(mDate, formatTime));
//                }


                if (getPriorityCode() == 0) {
                    todoUpdateValues.setPriorityStatus(databaseHandlerTodo.getPriorityStatus());
                } else
                    todoUpdateValues.setPriorityStatus(getPriorityCode());

                mDatabaseHandler.updateTodoList(new Todo(databaseId, todoUpdateValues.getTitle(), todoUpdateValues.getNotes(), todoUpdateValues.isChecked(), todoUpdateValues.getDateTimeRemainder(), todoUpdateValues.getPriorityStatus()));
                Intent createIntent = new Intent(CreateTodoActivity.this, MainActivity.class);
                startActivity(createIntent);
                finish();
            }
        });
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

    private void showRemainder(final DatabaseHandler databaseHandler) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);
        dateSelector = (AppCompatTextView) dialogView.findViewById(R.id.dateSelection);
        timeSelection = (AppCompatTextView) dialogView.findViewById(R.id.timeSelection);
        if (databaseId > 0) {
            getDateTimeValueFromDB(databaseHandler);
        } else {
            initialiseDateTimeValuesFromDialog();
        }

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
        builder.setPositiveButton(R.string.c_OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                displayDateTimeAtBottomAfterSelection();
            }
        });

        builder.setNegativeButton(R.string.c_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onTapDialogDismiss(dialog, databaseHandler);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void onTapDialogDismiss(DialogInterface dialog, DatabaseHandler databaseHandler) {
        Todo todo = databaseHandler.getTodo(databaseId);

        if (todo == null)
            return;


        if (todo.getDateTimeRemainder() != null)
            mDateAndTime.setVisibility(View.VISIBLE);
        else
            mDateAndTime.setVisibility(View.GONE);

        mDate = null;
        formatTime = null;
        mHours = null;
        mMinutes = null;
        lastDateTimeValue = null;
        dialog.dismiss();
    }

    private void displayDateTimeAtBottomAfterSelection() {

        if (mDate != null && formatTime != null) {
            mDateAndTime.setVisibility(View.VISIBLE);
            mDateAndTime.setText(" " + mDate + " " + formatTime);
            lastDateTimeValue = " " + mDate + " " + formatTime;
        } else if (mDate != null && formatTime == null) {
            mDateAndTime.setVisibility(View.VISIBLE);
            if (timeSelection.getText().toString().equals("Select Time")) {
                mDateAndTime.setText(" " + mDate);
                lastDateTimeValue = " " + mDate;
            } else {
                mDateAndTime.setText(" " + mDate + " " + timeSelection.getText().toString());
                lastDateTimeValue = " " + mDate + " " + timeSelection.getText().toString();
            }
        } else if (mDate == null && dateSelector.getText().toString().equals("Select Date")) {
            Toast.makeText(CreateTodoActivity.this, "Please select Date", Toast.LENGTH_LONG).show();
            mDateAndTime.setVisibility(View.GONE);
            mDateAndTime.setText(null);
        } else if (mDate != null && formatTime == null) {
            mDateAndTime.setVisibility(View.VISIBLE);
            mDateAndTime.setText(" " + mDate);
            lastDateTimeValue = " " + mDate;
        } else if (mDate != null && timeSelection.getText().toString() != null) {
            mDateAndTime.setVisibility(View.VISIBLE);
            mDateAndTime.setText(" " + mDate + " " + timeSelection.getText().toString());
            lastDateTimeValue = " " + mDate + " " + timeSelection.getText().toString();
        } else if (mDate == null && formatTime != null) {
            mDateAndTime.setVisibility(View.VISIBLE);
            mDateAndTime.setText(" " + dateSelector.getText().toString() + " " + formatTime);
            lastDateTimeValue = " " + dateSelector.getText().toString() + " " + formatTime;
        } else if (dateSelector.getText().toString() != null && timeSelection.getText().toString() != null) {
            mDateAndTime.setVisibility(View.VISIBLE);
            mDateAndTime.setText(" " + dateSelector.getText().toString() + " " + timeSelection.getText().toString());
            lastDateTimeValue = " " + dateSelector.getText().toString() + " " + timeSelection.getText().toString();
        } else if (mDate == null && dateSelector.getText().toString() != null) {
            mDateAndTime.setVisibility(View.VISIBLE);
            mDateAndTime.setText(" " + dateSelector.getText().toString());
            lastDateTimeValue = " " + dateSelector.getText().toString();
        } else
            mDateAndTime.setVisibility(View.GONE);


//        if (dateSelector.getText().toString() != null) {
//            mDateAndTime.setVisibility(View.VISIBLE);
//            if (dateSelector.getText().toString() != null && formatTime != null) {
//                mDateAndTime.setText(" " + dateSelector.getText().toString() + " " + formatTime);
//            } else if (formatTime == null && mDate != null) {
//                mDateAndTime.setText(" " + mDate);
//            } else if ((dateSelector.getText().toString() == null && mDate == null) || formatTime != null) {
//                Toast.makeText(CreateTodoActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
//                mDateAndTime.setVisibility(View.GONE);
//            } else if (dateSelector.getText().toString() != null && timeSelection.getText().toString() != null) {
//                mDateAndTime.setText("   " + dateSelector.getText().toString() + " " + timeSelection.getText().toString());
//            } else {
//                mDateAndTime.setText(" " + mDate + " " + formatTime);
//            }
//        } else {
////            if (mDate == null && formatTime != null) {
////                timeSelection.setText("");
////                mDateAndTime.setVisibility(View.GONE);
////            }
//            mDateAndTime.setVisibility(View.GONE);
//        }
//
//        if (mDate == null && formatTime != null) {
//            if (dateSelector.getText().toString() != null) {
//                mDateAndTime.setVisibility(View.VISIBLE);
//                mDateAndTime.setText(" " + dateSelector.getText().toString() + " " + formatTime);
//            }
//        }
//
//        if (dateSelector.getText().toString().equals("Select Date")) {
//            Toast.makeText(CreateTodoActivity.this, "Please select Date", Toast.LENGTH_LONG).show();
//            mDateAndTime.setVisibility(View.GONE);
//            mDateAndTime.setText("");
//        }
//
//
//        if (formatTime != null) {
//            if (mDate != null || !dateSelector.getText().toString().equals("Select Date"))
//                timeSelection.setText(formatTime);
//        }

    }

    private void initialiseDateTimeValuesFromDialog() {
        if (mDate != null)
            dateSelector.setText(mDate);
        else
            dateSelector.setText(R.string.select_date);

        if (formatTime != null)
            timeSelection.setText(formatTime);
        else
            timeSelection.setText(R.string.select_time);
    }

    private void getDateTimeValueFromDB(DatabaseHandler databaseHandler) {
        Todo todo = databaseHandler.getTodo(databaseId);
        if (todo == null)
            return;
        String dateTimeRemainder = todo.getDateTimeRemainder();
        if (dateTimeRemainder != null) {
            String[] dateTimeSeparation = dateTimeRemainder.trim().split("\\s+");
            String dateSeparationValue = dateTimeSeparation[0];
            String timeSeparationValue;
            if (dateTimeSeparation.length == 1)
                timeSeparationValue = getString(R.string.select_time);
            else
                timeSeparationValue = dateTimeSeparation[1];

            if (dateSeparationValue != null)
                dateSelector.setText(dateSeparationValue);
            else
                dateSelector.setText(R.string.select_date);

            if (timeSeparationValue != null)
                timeSelection.setText(timeSeparationValue);
            else
                timeSelection.setText(R.string.select_time);
        } else {
            dateSelector.setText(R.string.select_date);
            timeSelection.setText(R.string.select_time);
        }
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

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.highChoice:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                priorityStatusCode = PriorityStatusCode.HIGH;
                break;
            case R.id.mediumChoice:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                priorityStatusCode = PriorityStatusCode.MEDIUM;
                break;
            case R.id.lowChoice:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                priorityStatusCode = PriorityStatusCode.LOW;
                break;
        }

        priorityCode = priorityStatusCode.getCode();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (databaseId > 0) {
            Todo databaseHandlerTodo = mDatabaseHandler.getTodo(databaseId);
            if (databaseId > 0 && databaseHandlerTodo != null) {
                int priorityStatus = databaseHandlerTodo.getPriorityStatus();
                if (priorityStatus > 0 && priorityStatus < 4) {
                    String priorityStatusString = Util.getPriorityString(priorityStatus);
                    MenuItem highMenuItem = menu.findItem(R.id.highChoice);
                    MenuItem mediumMenuItem = menu.findItem(R.id.mediumChoice);
                    MenuItem lowMenuItem = menu.findItem(R.id.lowChoice);
                    if (priorityStatusString.equals(PriorityStatusCode.HIGH.getPriorityStatus())) {
                        highMenuItem.setChecked(true);
                    } else if (priorityStatusString.equals(PriorityStatusCode.MEDIUM.getPriorityStatus())) {
                        mediumMenuItem.setChecked(true);
                    } else if (priorityStatusString.equals(PriorityStatusCode.LOW.getPriorityStatus())) {
                        lowMenuItem.setChecked(true);
                    } else {
                        highMenuItem.setChecked(false);
                        mediumMenuItem.setChecked(false);
                        lowMenuItem.setChecked(false);
                    }
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public int getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(int priorityCode) {
        this.priorityCode = priorityCode;
    }

    @Override
    public void onCompleteDateSelected(String date) {
        this.mDate = date;
        if (mDate != null) {
            dateSelector.setText(mDate);
        } else {
            dateSelector.setText(R.string.select_date);
        }
    }

    @Override
    public void onCompleteTimeSelected(String hours, String minutes) {
        this.mHours = hours;
        this.mMinutes = minutes;

        if (mHours != null && mMinutes != null) {
            formatTime = String.format(Locale.ENGLISH, "%02d:%02d", Integer.parseInt(mHours), Integer.parseInt(mMinutes));
            timeSelection.setText(formatTime);
        } else
            timeSelection.setText(R.string.select_time);
    }

    private String getDateTime(String date, String hours) {
        if (date == null && hours != null)
            return null;

        if (hours == null) {
            return date;
        } else {
            return date + " " + hours;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_main, menu);
        return true;
    }

}
