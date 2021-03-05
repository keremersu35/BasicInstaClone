package com.example.instaclone.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instaclone.R;
import com.example.instaclone.SearchedProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchRecycleAdapter extends RecyclerView.Adapter<SearchRecycleAdapter.PostHolder> {

    private ArrayList<String> userEmailList;
    private  ArrayList<String> userProfilePhoto;
    private ArrayList<String> userBiography;

    public SearchRecycleAdapter(ArrayList<String> userEmailList, ArrayList<String> userImageList, ArrayList<String> userCaptionList) {
        this.userEmailList = userEmailList;
        this.userProfilePhoto = userImageList;
        this.userBiography = userCaptionList;
    }

    @NonNull
    @Override
    public SearchRecycleAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_search_row,parent,false);

        return new SearchRecycleAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecycleAdapter.PostHolder holder, int position) {
        holder.userEmail.setText(userEmailList.get(position));
        //holder.SearchBioText.setText(userBiography.get(position));
        Picasso.get().load(userProfilePhoto.get(position)).into(holder.searchProfileImage);
    }

    @Override
    public int getItemCount() {
       return userEmailList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        ImageView searchProfileImage;
        TextView SearchBioText,userEmail;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            searchProfileImage = itemView.findViewById(R.id.searchedProfilePhoto);
            userEmail = itemView.findViewById(R.id.searchedProfileText);
            SearchBioText = itemView.findViewById(R.id.recycle_captionText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), SearchedProfileActivity.class);
                    intent.putExtra("searchedProfile",userEmail.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }
        }
    }

