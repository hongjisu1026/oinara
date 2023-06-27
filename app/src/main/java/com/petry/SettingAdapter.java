package com.petry;

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

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {
    private List<PetProfile> list = new ArrayList<>();
    private StorageReference mStorageRef;
    private Context context;

    public SettingAdapter() {}
    public SettingAdapter(List<PetProfile> list) {
        this.list = list;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_setting, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        context = holder.itemView.getContext();
        String url = list.get(position).getProfileImgUrl();
        Glide.with(context).load(url).into(holder.s_img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView s_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            s_img = itemView.findViewById(R.id.s_img);
        }
    }
}
