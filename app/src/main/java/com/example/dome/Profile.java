package com.example.dome;

import static com.example.dome.App.ME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Profile extends AppCompatActivity {

    Button logout;
    ImageButton back;
    TextView Name, Email;

    @SuppressLint({"ClickableViewAccessibility", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = findViewById(R.id.bt_logout_Profile);
        Name = findViewById(R.id.name);
        back = findViewById(R.id.bt_back_Profile);
        Email = findViewById(R.id.email);

        Name.setText(String.format("Name: %s", ME.getfName()));
        Email.setText(String.format("Email: %s", ME.geteMail()));

        //back to homescreen
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this,Home_Activity.class));
                finishAffinity();
            }
        });

        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Profile.this);  //Creates a Pop-up Dialog
                alertDialogBuilder.setTitle("Confirm Logout?");
                alertDialogBuilder.setIcon(R.drawable.news_icon);
                alertDialogBuilder.setMessage("Do you really want to Logout?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Profile.this, Sign_IN.class);
                        startActivity(intent);
                        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember","false");
                        editor.apply();
                        finish();
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Profile.this, "Exit cancelled", Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alertDialog=alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }
}