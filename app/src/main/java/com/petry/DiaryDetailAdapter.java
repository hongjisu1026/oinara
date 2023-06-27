package com.petry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DiaryDetailAdapter extends RecyclerView.Adapter<DiaryDetailAdapter.ViewHolder> {

    private List<Diary> diaryList = new ArrayList<>();
    private StorageReference mStorageRef;
    private Context context;

    public DiaryDetailAdapter() {}

    public DiaryDetailAdapter(List<Diary> diaryList) {
        this.diaryList = diaryList;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_diary, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryDetailAdapter.ViewHolder holder, int position) {

        holder.detail_title.setText(diaryList.get(position).getdTitle());
        holder.detail_content.setText(diaryList.get(position).getdContent());
        holder.detail_date.setText(diaryList.get(position).getdUploadDate());

        context = holder.itemView.getContext();
        String url = diaryList.get(position).getdImgUrl();
        Glide.with(context).load(url).into(holder.detail_img);
    }

    @Override
    public int getItemCount() {
        return  diaryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView detail_img;
        public TextView detail_title;
        public TextView detail_content;
        public TextView detail_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            detail_img = itemView.findViewById(R.id.detail_img);
            detail_title = itemView.findViewById(R.id.detail_title);
            detail_content = itemView.findViewById(R.id.detail_content);
            detail_date = itemView.findViewById(R.id.detail_date);
        }
    }
}
