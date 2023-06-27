package com.petry;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<Diary> diaryList = new ArrayList<>();
    private StorageReference mStorageRef;
    private Context context;

    public AlbumAdapter() {}

    public AlbumAdapter(List<Diary> diaryList) {
        this.diaryList = diaryList;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_album, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        context = holder.itemView.getContext();
        String url = diaryList.get(position).getdImgUrl();
        Glide.with(context).load(url).into(holder.a_img);
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView a_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            a_img = itemView.findViewById(R.id.a_img);

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

    public void setOnItemClickListener(DiaryAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    private DiaryAdapter.OnItemClickListener itemClickListener;
}
