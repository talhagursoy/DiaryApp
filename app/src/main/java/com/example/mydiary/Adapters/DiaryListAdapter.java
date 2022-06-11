package com.example.mydiary.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.DiaryClickListener;
import com.example.mydiary.Models.Diary;
import com.example.mydiary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiaryListAdapter extends RecyclerView.Adapter<DiaryViewHolder> {
    Context context;
    List<Diary> list;
    DiaryClickListener listener;

    public DiaryListAdapter(Context context, List<Diary> list, DiaryClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiaryViewHolder(LayoutInflater.from(context).inflate(R.layout.diary_list, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);
        holder.textView_diary.setText(list.get(position).getDiary());
        holder.textView_date.setText(list.get(position).getDate());
        holder.textView_date.setSelected(true);
        if(list.get(position).isPinned()){
            holder.imageView_fav.setImageResource(R.drawable.fav);
        }
        else{
            holder.imageView_fav.setImageResource(0);
        }
        int color_code = getRandomColor();
        holder.diary_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code, null));

        holder.diary_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.diary_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.diary_container);
                return true;
            }
        });
    }

    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.purple_200);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.color6);
        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Diary> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }
}

class DiaryViewHolder extends RecyclerView.ViewHolder{

    TextView textView_title,textView_diary,textView_date;
    ImageView imageView_fav;
    CardView diary_container;

    public DiaryViewHolder(@NonNull View itemView) {
        super(itemView);
        textView_title = itemView.findViewById(R.id.textView_title);
        diary_container = itemView.findViewById(R.id.diary_container);
        imageView_fav = itemView.findViewById(R.id.imageView_fav);
        textView_diary = itemView.findViewById(R.id.textView_diary);
        textView_date = itemView.findViewById(R.id.textView_date);
    }
}