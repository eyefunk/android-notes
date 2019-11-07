package com.example.assignment2;


import java.io.Serializable;
import java.io.SerializablePermission;

public class Note implements Serializable {
    private String title;
    private String note;
    private long date;

    public Note(String title, String note, long date) {
        this.title = title;
        this.note = note;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public long getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", note='" + note + '\'' +
                ", date=" + date +
                '}';
    }
}
