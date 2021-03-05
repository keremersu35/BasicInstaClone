package com.example.instaclone.Fragments;

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
import android.widget.EditText;

import com.example.instaclone.Adapters.SearchRecycleAdapter;
import com.example.instaclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class SearchFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    EditText searchText;
    Button searchButton;
    ArrayList<String> searchBiography;
    ArrayList<String> searchProfilePhoto;
    ArrayList<String> searchEmail;
    SearchRecycleAdapter SearchRecycleAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchButton = view.findViewById(R.id.searchButton);
        searchText = view.findViewById(R.id.searchText);

        searchBiography = new ArrayList<>();
        searchProfilePhoto = new ArrayList<>();
        searchEmail = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        RecyclerView recyclerView3 = view.findViewById(R.id.recyclerSearch);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext()));
        SearchRecycleAdapter = new SearchRecycleAdapter(searchEmail, searchProfilePhoto, searchBiography);
        recyclerView3.setAdapter(SearchRecycleAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = searchText.getText().toString();
                searchProfile(mail);
            }
        });
    }

    public void searchProfile(String mail) {

        DocumentReference documentReference = firebaseFirestore.collection("UserProfile").document(mail);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    searchEmail.clear();
                    searchProfilePhoto.clear();
                    searchBiography.clear();
                    if (snapshot.exists()) {
                        Map<String, Object> data = snapshot.getData();

                        String bio = (String) data.get("biography");
                        String email = (String) data.get("useremail");
                        String downloadUrl = (String) data.get("downloadurl");

                        searchEmail.add(email);
                        searchBiography.add(bio);
                        searchProfilePhoto.add(downloadUrl);

                        SearchRecycleAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}