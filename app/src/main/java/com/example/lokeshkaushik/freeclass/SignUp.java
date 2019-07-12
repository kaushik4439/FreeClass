package com.example.lokeshkaushik.freeclass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText mUserName;
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mSignUpButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mref;
    private Context context;
    private Button mSignInBotton;
    private ProgressDialog progressDialog;
    private Switch mSwitch;
    private boolean isSwitchOn = false;
    FirebaseUser firebaseUser;
    private static final String TAG = "SignUp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mref = firebaseDatabase.getReference();

        progressDialog = new ProgressDialog(this);
        context = SignUp.this;
        mUserName = (EditText) findViewById(R.id.username);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mSignUpButton = (Button) findViewById(R.id.email_sign_in_button);
        mSignInBotton = (Button) findViewById(R.id.sign_in_button);
        mSwitch = (Switch) findViewById(R.id.switch1);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerUser();


            }
        });
        mSignInBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    isSwitchOn = true;
                }
                else
                    isSwitchOn = false;
            }
        });
    }

    public void registerUser(){

        final String userName = mUserName.getText().toString().trim();
        final String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            mEmailView.setError("Email cannot be empty");
            return;


        }
        if (TextUtils.isEmpty(password)){
            mPasswordView.setError("Password cannot be empty");
            return;

        }
        progressDialog.setMessage("Registering User....");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()){
                            UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                            builder.setDisplayName(userName);
                            firebaseUser = mAuth.getCurrentUser();
                            saveUserInformation(userName,email,isSwitchOn);
                            progressDialog.dismiss();
                            Toast.makeText(context,"Successfully Registered", Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(SignUp.this, LoginActivity.class);
                            //startActivity(intent);


                        }

                        if(!task.isSuccessful()){
                            //display some message here
                            progressDialog.dismiss();
                            Toast.makeText(context,"Registration Error", Toast.LENGTH_LONG).show();
                        }

                    }
                });


    }
    public void saveUserInformation(String userName,String email,boolean isSwitchOn){

        String userId = firebaseUser.getUid();

        User user = new User(userId,userName,email,isSwitchOn);
        mref.child("users").child(userId).setValue(user);


    }
}
