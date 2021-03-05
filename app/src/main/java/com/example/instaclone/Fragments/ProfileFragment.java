package com.example.instaclone.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instaclone.Adapters.ProfileRecycleAdapter;
import com.example.instaclone.EditProfileActivity;
import com.example.instaclone.R;
import com.example.instaclone.SignActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> userEmailFromFB;
    ArrayList<String> userCaptionFromFB;
    ArrayList<String> userImageFromFB;
    ProfileRecycleAdapter profileRecycleAdapter;
    ImageView profilePhoto;
    Button signOutButton,editProfileButton;
    TextView bioText;
    TextView emailText2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userCaptionFromFB = new ArrayList<>();
        userEmailFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        profileInfo();
        getDataFromProfile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profileRecycleAdapter = new ProfileRecycleAdapter(userEmailFromFB,userImageFromFB,userCaptionFromFB);
        recyclerView.setAdapter(profileRecycleAdapter);

        emailText2 = view.findViewById(R.id.emailTextSearched);
        profilePhoto = view.findViewById(R.id.profilePhoto);
        signOutButton = view.findViewById(R.id.signOutButton);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        bioText = view.findViewById(R.id.bioTextSearched);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();

                Intent intentToSignUp = new Intent(getActivity(), SignActivity.class);
                intentToSignUp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentToSignUp);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToEditProfile = new Intent(getActivity(), EditProfileActivity.class);
                intentToEditProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentToEditProfile);
            }
        });
    }

    public void profileInfo(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String currentUserEmail = user.getEmail();
        DocumentReference documentReference = firebaseFirestore.collection("UserProfile").document(currentUserEmail);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists())
                    {
                        Map<String, Object> data = snapshot.getData();

                        String bio = (String) data.get("biography");
                        String email = (String) data.get("useremail");
                        String downloadUrl = (String) data.get("downloadurl");

                        emailText2.setText(email.toString());
                        bioText.setText(bio.toString());
                        Picasso.get().load(downloadUrl).into(profilePhoto);

                    }
                }
            }
        });
    }

    public void getDataFromProfile(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String currentUserEmail = user.getEmail();
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        collectionReference.whereEqualTo("useremail",currentUserEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapShot : value.getDocuments()) {
                        Map<String, Object> data = snapShot.getData();

                        String caption = (String) data.get("caption");
                        String userEmail = (String) data.get("useremail");
                        String downloadUrl = (String) data.get("downloadurl");


                        userEmailFromFB.add(userEmail);
                        userCaptionFromFB.add(caption);
                        userImageFromFB.add(downloadUrl);

                        profileRecycleAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}