package com.example.lokeshkaushik.freeclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewCourse extends AppCompatActivity {

    private EditText courseName;
    private EditText departName;
    private EditText description;
    private String facultyName;
    private Button btnSubmit;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String caller;
    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("New Course");
        courseName = (EditText) findViewById(R.id.courseName);
        departName = (EditText) findViewById(R.id.department);
        description = (EditText) findViewById(R.id.description);
        btnSubmit = (Button) findViewById(R.id.course_submit);
        progressDialog = new ProgressDialog(this);
        caller = getIntent().getStringExtra("caller");

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mref =firebaseDatabase.getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

            }
        };


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewCourse();
            }
        });

    }


    public void registerNewCourse(){

        final String course = courseName.getText().toString().trim();
        final String departmentName = departName.getText().toString().trim();
        String courseDescription = description.getText().toString().trim();

        if (TextUtils.isEmpty(course)){
            courseName.setError("Course Name cannot be empty");
            return;


        }
        if (TextUtils.isEmpty(departmentName)){
            departName.setError("Department Name cannot be empty");
            return;

        }
        progressDialog.setMessage("Saving New Course....");
        progressDialog.show();




        mref.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                facultyName = user.getUserName();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        String courseKey = mref.child("courses").push().getKey();

        Course mCourse  = new Course(courseKey,course,departmentName,facultyName,courseDescription);
        mref.child("courses").child(courseKey).setValue(mCourse);
        mref.child("students").child(user.getUid()).child(courseKey).setValue(true);
        progressDialog.dismiss();
        Toast.makeText(this,"course Successfully added", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Class callerClass = Class.forName(caller);
            if (callerClass == TeacherHome.class){
                Intent intent = new Intent(this, TeacherHome.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
