package com.example.lokeshkaushik.freeclass;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FacultyCourseContent extends AppCompatActivity {

    private String courseKey;
    private String courseName;
    private ArrayList<NewPost> postArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mref;
    private RecyclerView recyclerView;
    private CourseContentAdapter courseContentAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_course_content);
        courseKey = getIntent().getStringExtra("courseId");
        courseName = getIntent().getStringExtra("courseName");
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(courseName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_upload_button);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mref = firebaseDatabase.getReference();
        postArrayList = new ArrayList<>();
        courseContentAdapter = new CourseContentAdapter(postArrayList,this);
        recyclerView = (RecyclerView) findViewById(R.id.courseContentCardList);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(courseContentAdapter);
        getCourseContent();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FacultyCourseContent.this, CourseContent.class);
                intent.putExtra("courseId",courseKey);
                intent.putExtra("courseName",courseName);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getCourseContent(){
        mref.child("posts").child(courseKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NewPost post = dataSnapshot.getValue(NewPost.class);
                postArrayList.add(post);
                courseContentAdapter.notifyDataSetChanged();

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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId()==R.id.action_profile){
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
