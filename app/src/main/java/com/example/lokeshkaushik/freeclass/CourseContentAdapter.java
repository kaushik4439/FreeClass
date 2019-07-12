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
 * Created by Lokesh Kaushik on 25-Jan-17.
 */

public class CourseContentAdapter extends RecyclerView.Adapter<CourseContentAdapter.CourseContentHolder>  {
    private LayoutInflater layoutInflater;
    private ArrayList<NewPost> arrayList;
    private Context context;

    public CourseContentAdapter( ArrayList<NewPost> arrayList, Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public CourseContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.course_content_layout,parent,false);
        CourseContentHolder holder = new CourseContentHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(CourseContentHolder holder, int position) {
        if(holder!=null){
            holder.senderName.setText(arrayList.get(position).getSenderName());
            holder.title.setText(arrayList.get(position).getTitle());
            holder.message.setText(arrayList.get(position).getMessage());
            holder.downloadUri.setText(arrayList.get(position).getDownloadUri());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class CourseContentHolder extends RecyclerView.ViewHolder {
        TextView senderName;
        TextView title;
        TextView message;
        TextView downloadUri;
        public CourseContentHolder(View itemView) {
            super(itemView);
            senderName = (TextView) itemView.findViewById(R.id.sender);
            title = (TextView) itemView.findViewById(R.id.post_title);
            message = (TextView) itemView.findViewById(R.id.post_message);
            downloadUri = (TextView) itemView.findViewById(R.id.post_link);
        }


    }
}
