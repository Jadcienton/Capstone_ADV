package com.example.alejandro.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class RecyclerViewAdapterComments extends RecyclerView.Adapter<RecyclerViewAdapterComments.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterComm";
    private ArrayList<Comment> comments;
    private Context context;

    public RecyclerViewAdapterComments(Context context, ArrayList<Comment> comments) {
        this.comments = comments;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listcomments, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.date.setText(comments.get(position).getDate());
        holder.type.setText(comments.get(position).getType());
        holder.user.setText(comments.get(position).getUser());
        holder.comment.setText(comments.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView date,type,user,comment;
        LinearLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_comment);
            type = itemView.findViewById(R.id.type_comment);
            user = itemView.findViewById(R.id.user_comment);
            comment = itemView.findViewById(R.id.text_comment);
            parentLayout = itemView.findViewById(R.id.parent_layout_comment);
        }
    }
}
