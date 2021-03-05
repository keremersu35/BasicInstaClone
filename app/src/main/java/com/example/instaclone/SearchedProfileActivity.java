package com.example.instaclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instaclone.Adapters.ProfileRecycleAdapter;
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

public class SearchedProfileActivity extends AppCompatActivity {

    ImageView profilePhotoSearched;
    TextView emailTextSearched,bioTextSearched;
    RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> userEmailFromFB;
    ArrayList<String> userCaptionFromFB;
    ArrayList<String> userImageFromFB;
    ProfileRecycleAdapter profileRecycleAdapter;
    String searchedProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_profile);

        profilePhotoSearched = findViewById(R.id.profilePhotoSearched);
        emailTextSearched = findViewById(R.id.emailTextSearched);
        bioTextSearched = findViewById(R.id.bioTextSearched);

        userCaptionFromFB = new ArrayList<>();
        userEmailFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        searchedProfile = getIntent().getStringExtra("searchedProfile");

        profileInfo(searchedProfile);
        getDataFromProfile(searchedProfile);

        RecyclerView recyclerView = findViewById(R.id.recyclerView3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileRecycleAdapter = new ProfileRecycleAdapter(userEmailFromFB,userImageFromFB,userCaptionFromFB);
        recyclerView.setAdapter(profileRecycleAdapter);

    }

    public void profileInfo(String searchedProfile){

        DocumentReference documentReference = firebaseFirestore.collection("UserProfile").document(searchedProfile);
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

                        emailTextSearched.setText(email.toString());
                        bioTextSearched.setText(bio.toString());
                        Picasso.get().load(downloadUrl).into(profilePhotoSearched);

                    }
                }
            }
        });
    }

    public void getDataFromProfile(String searchedProfile){


        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        collectionReference.whereEqualTo("useremail",searchedProfile).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(SearchedProfileActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
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