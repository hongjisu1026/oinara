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

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private List<PetProfile> petProfileList = new ArrayList<>();
    private FirebaseStorage storage;
    private Context context;

    public ProfileAdapter() {
    }

    public ProfileAdapter(List<PetProfile> petProfileList) {
        this.petProfileList = petProfileList;
        storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.main_profile, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.main_name.setText(petProfileList.get(position).getPetName());
        holder.main_sex.setText(petProfileList.get(position).getPetSex());
        holder.main_birth.setText(petProfileList.get(position).getPetBirth());
        holder.main_img.setImageResource(R.drawable.no_profile);

        context = holder.itemView.getContext();
        String url = petProfileList.get(position).getProfileImgUrl();
        Glide.with(context).load(url).into(holder.main_img);

    }

    @Override
    public int getItemCount() {
        return petProfileList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView main_img;
        public TextView main_name;
        public TextView main_sex;
        public TextView main_birth;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            main_img = itemView.findViewById(R.id.main_img);
            main_name = itemView.findViewById(R.id.main_name);
            main_sex = itemView.findViewById(R.id.main_sex);
            main_birth = itemView.findViewById(R.id.main_birth);
        }
    }
}
