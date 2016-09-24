package com.vinaymaneti.todo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vinaymaneti on 9/21/16.
 */

public class Todo implements Parcelable {
    private String title, notes;
    private boolean isChecked;
    private int id;

    public Todo() {

    }

    public Todo(boolean isChecked, String title, String notes) {
        this.isChecked = isChecked;
        this.title = title;
        this.notes = notes;
    }

    protected Todo(Parcel in) {
        id = in.readInt();
        title = in.readString();
        notes = in.readString();
        isChecked = in.readByte() != 0; //isChecked = (in.readInt() == 0) ? false : true;

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

    public Todo(int id, String stringTitle, String stringNotes, boolean checkboxStatus) {
        this.id = id;
        this.title = stringTitle;
        this.notes = stringNotes;
        this.isChecked = checkboxStatus;
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
    }
}
