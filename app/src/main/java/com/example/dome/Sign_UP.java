package com.example.dome;

import static com.example.dome.App.ME;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dome.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.HashMap;
import java.util.Map;

public class Sign_UP extends AppCompatActivity {

    public static final String TAG = "TAG";

    EditText mPassSignUp,mNameSignUp,mMailSignUp;
    Button mSignUp,mSignIn;
    boolean passwordVisible;


    FirebaseFirestore fstore;
    String userID;
    FirebaseAuth fAuth;




    ActivitySignUpBinding binding;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        mPassSignUp = findViewById(R.id.etPassSignUp);
        mNameSignUp = findViewById(R.id.etNameSignUp);
        mMailSignUp = findViewById(R.id.etMailSignUp);

        mSignUp = findViewById(R.id.bt_signup_SignUp);
        mSignIn = findViewById(R.id.bt_signin_SignUp);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();





        //Function to go LOG-in Activity

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Sign_IN.class));
            }
        });



        //REQUIREMENTS OF signup btn
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = mNameSignUp.getText().toString().trim();
                String email = mMailSignUp.getText().toString().trim();
                String password = mPassSignUp.getText().toString().trim();


                if(TextUtils.isEmpty(fullName)){
                    mNameSignUp.setError("Name is required");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    mMailSignUp.setError("Mail is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassSignUp.setError("Password is required");
                    return;
                }

                if(password.length()<=6){
                    mPassSignUp.setError("Password must be more than 6 characters");
                    return;
                }
                //Authentication
            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        if (task.isSuccessful()) {




                            Toast.makeText(Sign_UP.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userID);
//                            Map<String, Object> user = new HashMap<>();
//                            user.put("fName", fullName);
//                            user.put("eMail", email);
                            ME = new User(fullName, email);
                            documentReference.set(ME).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: User profile is created for" + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), Sign_IN.class));
                            finishAffinity();
                        } else {
                            Toast.makeText(Sign_UP.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        DynamicToast.makeError(getApplicationContext(), "You're not connected to Internet!").show();
                    }
                }
            });

            }
        });



        // Function to see password and hide password
        mPassSignUp.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= mPassSignUp.getRight() - mPassSignUp.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = mPassSignUp.getSelectionEnd();
                    //Handles Multiple option popups
                    if (passwordVisible) {
                        //set drawable image here
                        mPassSignUp.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0);
                        //for hide password
                        mPassSignUp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        //set drawable image here
                        mPassSignUp.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility, 0);
                        //for show password
                        mPassSignUp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    mPassSignUp.setLongClickable(false); //Handles Multiple option popups
                    mPassSignUp.setSelection(selection);
                    return true;
                }
            }
            return false;
        });




    }
}