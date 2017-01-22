package com.example.lokeshkaushik.freeclass;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FacultyCourseContent extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_course_content);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_upload_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FacultyCourseContent.this, CourseContent.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
