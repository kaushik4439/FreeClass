
package com.example.lokeshkaushik.freeclass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LayoutAdapter.ClickListener {


    private ArrayList<Course> courseList ;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mref;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private RecyclerView recyclerView;
    private LayoutAdapter layoutAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Boolean isFaculty;
    private FirebaseUser user;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Your Courses");
        actionBar.setDisplayHomeAsUpEnabled(true);
        courseList = new ArrayList<Course>();
        layoutAdapter = new LayoutAdapter(this,courseList);
        layoutAdapter.setClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(layoutAdapter);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mref = firebaseDatabase.getReference();
        user = mAuth.getCurrentUser();
        getRegisteredCourseList();
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_button);

        mref.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                isFaculty = user.isFaculty();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFaculty){
                    Intent intent = new Intent(MainActivity.this, NewCourse.class);
                    intent.putExtra("caller", MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, AllCourses.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    public void getRegisteredCourseList() {

        mref.child("students").orderByKey().equalTo(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot!= null){
                    for (DataSnapshot child: dataSnapshot.getChildren()){
                        String courseKey = child.getKey();
                        mref.child("courses").orderByKey().equalTo(courseKey).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Course course = dataSnapshot.getValue(Course.class);
                                courseList.add(course);
                                layoutAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isFaculty){
            Intent intent = new Intent(this, TeacherHome.class);
            startActivity(intent);
            finish();
        }else {
            moveTaskToBack(true);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            if (isFaculty){
                Intent intent = new Intent(this, TeacherHome.class);
                startActivity(intent);
                finish();
            }else {
                moveTaskToBack(true);
            }
        }
        if (item.getItemId()== R.id.action_profile){
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void itemClicked(View view, int position) {
        String courseKey = courseList.get(position).getCourseKey();

        Intent intent = new Intent(this, FacultyCourseContent.class);
        intent.putExtra("courseId",courseKey);
        intent.putExtra("courseName",courseList.get(position).getCourseName());
        startActivity(intent);
        finish();


    }
}
