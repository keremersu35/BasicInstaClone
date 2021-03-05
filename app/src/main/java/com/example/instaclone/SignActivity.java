package com.example.instaclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignActivity extends AppCompatActivity {

    public static FirebaseAuth firebaseAuth;
    EditText emailText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){

            Intent intent = new Intent(SignActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void signInClicked(View view){

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if(email.equals("") || password.equals(""))
            Toast.makeText(SignActivity.this,"Please Enter your Email and Password!!",Toast.LENGTH_SHORT).show();
        else{
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Intent intent = new Intent(SignActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();

            }
        });
        }
    }

    public void signUpClicked(View view){

        Intent intent = new Intent(SignActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}