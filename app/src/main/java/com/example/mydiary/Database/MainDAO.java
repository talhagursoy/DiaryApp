package com.example.mydiary.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mydiary.Models.Diary;

import java.util.List;

@Dao
public interface MainDAO {
    @Insert(onConflict = REPLACE)
    void insert(Diary diary);
    @Query("SELECT * FROM diary ORDER BY id DESC")
    List<Diary> getAll();
    @Query("UPDATE diary SET title = :title, diary = :diary WHERE ID = :id")
    void update(int id, String title, String diary);
    @Delete
    void delete(Diary diary);
    @Query("UPDATE diary SET pinned = :pin  WHERE ID =:id")
    void pin(int id, boolean pin);
}
