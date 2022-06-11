package com.example.mydiary;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mydiary.Adapters.DiaryListAdapter;
import com.example.mydiary.Database.RoomDB;
import com.example.mydiary.Models.Diary;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    RecyclerView recyclerView;
    DiaryListAdapter diaryListAdapter;
    List<Diary> diaries = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;
    Diary selectedDiary;
    SearchView searchView_home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_home);
        database = RoomDB.getInstance(this);
        fab_add = findViewById(R.id.fab_add);
        searchView_home = findViewById(R.id.searchView_home);

        diaries = database.mainDAO().getAll();

        updateRecycler(diaries);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                startActivityForResult(intent, 101);

            }
        });
        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<Diary> filteredList = new ArrayList<>();
        for(Diary singleDiary : diaries){
            if(singleDiary.getTitle().toLowerCase().contains(newText.toLowerCase())
        || singleDiary.getDiary().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(singleDiary);
            }
        }
        diaryListAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101){
            if(resultCode== Activity.RESULT_OK){
                Diary new_diary = (Diary) data.getSerializableExtra("diary");
                database.mainDAO().insert(new_diary);
                diaries.clear();
                diaries.addAll(database.mainDAO().getAll());
                diaryListAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode==102){
            if(resultCode==Activity.RESULT_OK){
                Diary new_diary = (Diary) data.getSerializableExtra("diary");
                database.mainDAO().update(new_diary.getID(), new_diary.getTitle(), new_diary.getDiary());
                diaries.clear();
                diaries.addAll(database.mainDAO().getAll());
                diaryListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Diary> diaries) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        diaryListAdapter = new DiaryListAdapter(MainActivity.this, diaries, diaryClickListener);
        recyclerView.setAdapter(diaryListAdapter);
    }
    private final DiaryClickListener diaryClickListener = new DiaryClickListener() {
        @Override
        public void onClick(Diary diary) {
            Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
            intent.putExtra("old_diary", diary);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Diary diary, CardView cardView) {
            selectedDiary = new Diary();
            selectedDiary = diary;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
                if(selectedDiary.isPinned()){
                    database.mainDAO().pin(selectedDiary.getID(), false);
                    Toast.makeText(MainActivity.this, "Removed Favourite Mark!", Toast.LENGTH_SHORT).show();
                }
                else{
                    database.mainDAO().pin(selectedDiary.getID(), true);
                    Toast.makeText(MainActivity.this, "Marked as Favourite!", Toast.LENGTH_SHORT).show();
                }
                diaries.clear();
                diaries.addAll(database.mainDAO().getAll());
                diaryListAdapter.notifyDataSetChanged();
                return true;
            case R.id.delete:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                database.mainDAO().delete(selectedDiary);
                                diaries.remove(selectedDiary);
                                diaryListAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            case R.id.share:
                String text = selectedDiary.getTitle();
                String text1 = selectedDiary.getDiary();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, text +"\n"+ text1);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            default:
                return false;
        }
    }
}