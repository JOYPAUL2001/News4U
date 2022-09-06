package com.example.dome;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetpass extends AppCompatActivity {

    FirebaseAuth fAuth;
    EditText email;
    Button resetpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);

        fAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.etMailForgetPass);
        resetpass = findViewById(R.id.btForgetPass);

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailnew = email.getText().toString().trim();

                if (emailnew.isEmpty()){
                    email.setError("Field can't be empty");
                    email.requestFocus();
                    return;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailnew).matches()){
                    email.setError("Please enter a valid Email id");
                    email.requestFocus();
                    return;
                }
                else{
                    fAuth.sendPasswordResetEmail(emailnew)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    try{
                                        if (task.isSuccessful()){
                                            Toast.makeText(forgetpass.this, "Password Reset email sent!", Toast.LENGTH_SHORT).show();

                                        }
                                        else{
                                            Toast.makeText(forgetpass.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        Log.d(TAG,"Email sent");
                                        return;
                                    }
                                }
                            });
                }
            }
        });
    }
}