package com.example.dome;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance(); //initialize Firebase Auth

        FirebaseUser currentUser = fAuth.getCurrentUser(); //Get the current user

        if (currentUser == null)

            SendUserToLoginActivity(); //If the user has not logged in, send them to On-Boarding Activity

        else {
            //If user was logged in last time
            if (currentUser.isEmailVerified()) {
                FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            App.ME = task.getResult().toObject(User.class);
                            Intent loginIntent = new Intent(MainActivity.this, Home_Activity.class);
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginIntent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                new Handler().postDelayed(() -> {
                    Intent loginIntent;
                    loginIntent = new Intent(MainActivity.this, Sign_IN.class); //If the user email is not verified
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                    finish();
                }, 3000);
            }
        }
    }
    private void SendUserToLoginActivity() {
        new Handler().postDelayed(() -> {
            Intent loginIntent = new Intent(MainActivity.this, Sign_IN.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
        }, 3000);
    }
}