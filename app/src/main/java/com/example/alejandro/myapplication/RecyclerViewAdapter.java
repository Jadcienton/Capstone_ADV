package com.example.alejandro.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Sisda> mSisdas;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<Sisda> mSisdas) {
        this.mSisdas = mSisdas;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.sisda.setText(mSisdas.get(position).getmSisdas());
        holder.priority.setText(mSisdas.get(position).getmPriority());
        holder.address.setText(mSisdas.get(position).getmAddresses());
        holder.level.setText(mSisdas.get(position).getmLevel());
        holder.progress.setText(mSisdas.get(position).getmProgresses());
        holder.delay.setText(mSisdas.get(position).getmDelay());
        holder.time.setText(mSisdas.get(position).getmTime());
        if(mSisdas.get(position).getmTime().equals("1:00:00") && mSisdas.get(position).getmDelay().equals("A Tiempo"))
            holder.delay.setBackgroundResource(R.drawable.bg_progress_caution);
        else if (mSisdas.get(position).getmDelay().equals("Retrasado"))
            holder.delay.setBackgroundResource(R.drawable.bg_progress_warning);
        else if (mSisdas.get(position).getmDelay().equals("A Tiempo"))
            holder.delay.setBackgroundResource(R.drawable.bg_progress_good);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: "+ mSisdas.get(position));
                openSisdaDetail();
            }
        });
    }
    public void openSisdaDetail(){
        Intent intent = new Intent(mContext,EventDetail.class);
        mContext.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return mSisdas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView sisda;
        TextView priority;
        TextView address;
        TextView level;
        TextView progress;
        TextView delay;
        TextView time;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            sisda = itemView.findViewById(R.id.sisda);
            priority = itemView.findViewById(R.id.priority);
            address = itemView.findViewById(R.id.address);
            level = itemView.findViewById(R.id.level);
            progress = itemView.findViewById(R.id.progress);
            delay = itemView.findViewById(R.id.delay);
            time = itemView.findViewById(R.id.time);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
