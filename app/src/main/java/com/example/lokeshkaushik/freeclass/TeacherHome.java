package com.example.lokeshkaushik.freeclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeacherHome extends AppCompatActivity {

    private Button btnCourse;
    private Button btnNewCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        btnCourse = (Button) findViewById(R.id.btn_courses);
        btnNewCourse = (Button) findViewById(R.id.btn_new_course);

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
