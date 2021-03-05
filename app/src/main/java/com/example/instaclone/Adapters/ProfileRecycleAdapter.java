package com.example.instaclone.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instaclone.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileRecycleAdapter extends RecyclerView.Adapter<ProfileRecycleAdapter.PostHolder>{

    private ArrayList<String> userEmailList;
    private  ArrayList<String> userImageList;
    private ArrayList<String> userCaptionList;

    public ProfileRecycleAdapter(ArrayList<String> userEmailList, ArrayList<String> userImageList, ArrayList<String> userCaptionList) {
        this.userEmailList = userEmailList;
        this.userImageList = userImageList;
        this.userCaptionList = userCaptionList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_view,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.userEmail.setText(userEmailList.get(position));
        holder.captionText.setText(userCaptionList.get(position));
        Picasso.get().load(userImageList.get(position)).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return userEmailList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView captionText,userEmail;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.recycle_Image);
            userEmail = itemView.findViewById(R.id.recycle_userEmailText);
            captionText = itemView.findViewById(R.id.recycle_captionText);
        }
    }
}
