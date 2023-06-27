package com.petry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {

    private List<Diary> diaryList = new ArrayList<>();
    private StorageReference mStorageRef;
    private Context context;

    public DiaryAdapter() {
    }

    public DiaryAdapter(List<Diary> diaryList) {
        this.diaryList = diaryList;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.diary_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.d_title.setText(diaryList.get(position).getdTitle());
        holder.d_content.setText(diaryList.get(position).getdContent());
        holder.d_date.setText(diaryList.get(position).getdUploadDate());

        context = holder.itemView.getContext();
        String url = diaryList.get(position).getdImgUrl();
        Glide.with(context).load(url).into(holder.d_img);

    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView d_img;
        public TextView d_title;
        private TextView d_content;
        private TextView d_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            d_img = itemView.findViewById(R.id.d_img);
            d_title = itemView.findViewById(R.id.d_title);
            d_content = itemView.findViewById(R.id.d_content);
            d_date = itemView.findViewById(R.id.d_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClicked(view, pos);
                    }
                }
            });
        }
    }

    //리사이클러뷰 아이템 클릭 이벤트 처리
    public interface OnItemClickListener {
        void onItemClicked(View view, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    private OnItemClickListener itemClickListener;


}
