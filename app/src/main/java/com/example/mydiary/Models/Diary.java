package com.example.mydiary.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "diary")
public class Diary implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int ID =0;
    @ColumnInfo(name = "title")
    String title = "";
    @ColumnInfo(name = "diary")
    String diary = "";
    @ColumnInfo(name = "date")
    String date = "";
    @ColumnInfo(name = "pinned")
    boolean pinned = false;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiary() {
        return diary;
    }

    public void setDiary(String diary) {
        this.diary = diary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
