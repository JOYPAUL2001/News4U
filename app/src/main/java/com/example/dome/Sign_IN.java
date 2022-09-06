package com.example.dome;

import static com.example.dome.App.ME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Arrays;

public class Sign_IN extends AppCompatActivity {

    EditText  mPassSignIn,mMailSignIn;
    Button mSignUp,mSignIn,mForgetpass;
    CheckBox mCheckbox;

    boolean passwordVisible;

    FirebaseAuth fAuth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


   mPassSignIn = findViewById(R.id.etPassSignIn);
   mMailSignIn = findViewById(R.id.etMailSignIn);
   mSignUp = findViewById(R.id.bt_signup_SignIn);
   mSignIn = findViewById(R.id.bt_signin_SignIn);
   mCheckbox = findViewById(R.id.bt_checkBoxSignIn);
   mForgetpass = findViewById(R.id.bt_forgetPassSignIn);

        fAuth = FirebaseAuth.getInstance();

        //checkbox
        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()){
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                }
                else if (!compoundButton.isChecked()){
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                }
            }
        });
//forget password
        mForgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_IN.this,forgetpass.class);
                startActivity(intent);
                Animatoo.animateShrink(Sign_IN.this);
            }
        });



   // Function to go Sign_up activity
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Sign_UP.class));
            }
        });
        // Requirements of SignIn button

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtext = mMailSignIn.getText().toString().trim();
                String passwordtext = mPassSignIn.getText().toString().trim();


                if(TextUtils.isEmpty(emailtext)){
                    mMailSignIn.setError("Mail is required");
                    return;
                }
                if(TextUtils.isEmpty(passwordtext)){
                    mPassSignIn.setError("Password is required");
                    return;
                }

                if(passwordtext.length()<=6){
                    mPassSignIn.setError("Password must be more than 6 characters");
                    return;
                }


                //Authentication

                fAuth.signInWithEmailAndPassword(emailtext,passwordtext).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
                            if (task.isSuccessful()){

                                FirebaseUser user = fAuth.getCurrentUser();
                                if (user.isEmailVerified()){
                                    FirebaseFirestore.getInstance().collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                ME = task.getResult().toObject(User.class);
                                                Intent intent2 = new Intent(getApplicationContext(),Home_Activity.class);
                                                startActivity(intent2);
                                                Animatoo.animateFade(Sign_IN.this);
                                                finishAffinity();
                                            } else {
                                                Toast.makeText(Sign_IN.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    user.sendEmailVerification();
                                    Toast.makeText(getApplicationContext(), "Check your email to verify your account and Login again", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Failed to Login! Please check your credentials", Toast.LENGTH_SHORT).show();
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
        mPassSignIn.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= mPassSignIn.getRight() - mPassSignIn.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = mPassSignIn.getSelectionEnd();
                    //Handles Multiple option popups
                    if (passwordVisible) {
                        //set drawable image here
                        mPassSignIn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0);
                        //for hide password
                        mPassSignIn.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        //set drawable image here
                        mPassSignIn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility, 0);
                        //for show password
                        mPassSignIn.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    mPassSignIn.setLongClickable(false); //Handles Multiple option popups
                    mPassSignIn.setSelection(selection);
                    return true;
                }
            }
            return false;
        });





    }

    //Internt Connectivity checker
//    private boolean isConnected() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
//            return true;
//
//        DynamicToast.makeError(getApplicationContext(), "You're not connected to Internet!").show();
//        return false;
//    }
}