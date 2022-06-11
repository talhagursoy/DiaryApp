package com.example.mydiary;

import androidx.cardview.widget.CardView;

import com.example.mydiary.Models.Diary;

public interface DiaryClickListener{
    void onClick(Diary diary);
    void onLongClick(Diary diary, CardView cardView);
}
