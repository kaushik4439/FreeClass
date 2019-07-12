package com.example.lokeshkaushik.freeclass;

import java.util.ArrayList;

/**
 * Created by Lokesh Kaushik on 03-Jan-17.
 */

public class Course {


    private String courseKey;
    private String courseName;
    private String department;
    private String facultyName;
    private String description;


    public Course() {
    }

    public Course(String courseKey,String courseName, String department, String facultyName, String description) {

        this.courseKey = courseKey;
        this.courseName = courseName;
        this.department = department;
        this.facultyName = facultyName;
        this.description = description;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDepartment() {
        return department;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getDescription() {
        return description;
    }

    public String getCourseKey() {
        return courseKey;
    }


}
