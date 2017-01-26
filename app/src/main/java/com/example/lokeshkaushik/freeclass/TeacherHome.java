package com.example.lokeshkaushik.freeclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class TeacherHome extends AppCompatActivity {

    private ImageButton btnCourse;
    private ImageButton btnNewCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        btnCourse = (ImageButton) findViewById(R.id.btn_courses);
        btnNewCourse = (ImageButton) findViewById(R.id.btn_new_course);

        btnNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherHome.this, NewCourse.class);
                intent.putExtra("caller", "TeacherHome");
                startActivity(intent);
            }
        });

        btnCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherHome.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
