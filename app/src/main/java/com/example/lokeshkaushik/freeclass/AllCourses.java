package com.example.lokeshkaushik.freeclass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AllCourses extends AppCompatActivity implements  LayoutAdapter.ClickListener {
    private ArrayList<Course> allCourses;
    private ArrayList<Course> registeredCourses;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerView;
    private LayoutAdapter layoutAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String currentUserName;
    private DatabaseReference mref;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);
        allCourses = new ArrayList<Course>();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mref = firebaseDatabase.getReference();
        user = mAuth.getCurrentUser();
        registeredCourses = new ArrayList<Course>();

        layoutAdapter = new LayoutAdapter(this,allCourses);
        layoutAdapter.setClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.allCourseList);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(layoutAdapter);

        //registeredCourses.clear();
        allCourses.clear();
        getAllCourses();
       // layoutAdapter.notifyDataSetChanged();


    }

    public void getAllCourses(){
        mref.child("courses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getValue()!= null){

                    Course course = dataSnapshot.getValue(Course.class);
                    allCourses.add(course);

                }

                layoutAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Course course = dataSnapshot.getValue(Course.class);
                allCourses.remove(course);
                layoutAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void registerInNewCourse(final int position) {


        mref.child("students").child(user.getUid()).child(allCourses.get(position).getCourseKey()).setValue(true);
        Toast.makeText(this,"Successfully Enrolled in "+allCourses.get(position).getCourseName(), Toast.LENGTH_LONG).show();



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void itemClicked(View view, final int position) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AllCourses.this);
        View mView = getLayoutInflater().inflate(R.layout.register_dialog,null);
        Button mRegisterButton = (Button) mView.findViewById(R.id.dialog_register);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerInNewCourse(position);
                dialog.dismiss();
            }
        });

    }
}
