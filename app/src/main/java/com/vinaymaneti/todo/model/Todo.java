package com.vinaymaneti.todo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vinaymaneti on 9/21/16.
 */

public class Todo implements Parcelable {
    private String title, notes, dateTimeRemainder;
    private boolean isChecked;
    private int id;
    private int priorityStatus;

    public Todo() {

    }

    public Todo(boolean isChecked, String title, String notes, String dateTimeRemainder, int priorityStatus) {
        this.isChecked = isChecked;
        this.title = title;
        this.notes = notes;
        this.dateTimeRemainder = dateTimeRemainder;
        this.priorityStatus = priorityStatus;

    }

    private Todo(Parcel in) {
        id = in.readInt();
        title = in.readString();
        notes = in.readString();
        isChecked = in.readByte() != 0; //isChecked = (in.readInt() == 0) ? false : true;
        dateTimeRemainder = in.readString();
        priorityStatus = in.readInt();
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    public Todo(int id, String stringTitle, String stringNotes, boolean checkboxStatus, String dateTimeRemainder, int priorityStatus) {
        this.id = id;
        this.title = stringTitle;
        this.notes = stringNotes;
        this.isChecked = checkboxStatus;
        this.dateTimeRemainder = dateTimeRemainder;
        this.priorityStatus = priorityStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateTimeRemainder() {
        return dateTimeRemainder;
    }

    public void setDateTimeRemainder(String dateTimeRemainder) {
        this.dateTimeRemainder = dateTimeRemainder;
    }

    public int getPriorityStatus() {
        return priorityStatus;
    }

    public void setPriorityStatus(int priorityStatus) {
        this.priorityStatus = priorityStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(notes);
        dest.writeInt(isChecked ? 1 : 0);
        dest.writeString(dateTimeRemainder);
        dest.writeInt(priorityStatus);
    }
}
