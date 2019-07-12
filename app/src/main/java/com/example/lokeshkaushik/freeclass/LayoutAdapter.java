package com.example.lokeshkaushik.freeclass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lokesh Kaushik on 04-Jan-17.
 */

public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.CourseViewHolder>{


    private  LayoutInflater inflater;
    private ArrayList<Course> courseList;
    private ClickListener clickListener;

    public LayoutAdapter(Context context,ArrayList<Course>courseList) {
        inflater = LayoutInflater.from(context);
        this.courseList = courseList;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.courselayout,null);
        CourseViewHolder holder = new CourseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        if (holder!= null){
            holder.courseName.setText(courseList.get(position).getCourseName());
            holder.facultyName.setText(courseList.get(position).getFacultyName());
            holder.departmentName.setText(courseList.get(position).getDepartment());
            holder.description.setText(courseList.get(position).getDescription());
        }


    }

    public void setClickListener(ClickListener clicklistener){
        this.clickListener = clicklistener;
    }
    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{



        TextView courseName;
        TextView facultyName;
        TextView departmentName;
        TextView description;
        public CourseViewHolder(View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.courseName);
            facultyName = (TextView) itemView.findViewById(R.id.facultyName);
            departmentName = (TextView) itemView.findViewById(R.id.department);
            description = (TextView) itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            if (clickListener!= null){
                clickListener.itemClicked(view,getAdapterPosition());
            }
        }
    }
    public interface ClickListener{
        public void itemClicked(View view, int position);

    }
}