package com.example.alejandro.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Sisda> mSisdas;
    private Context mContext;
    private String type;
    public RecyclerViewAdapter(Context mContext, ArrayList<Sisda> mSisdas,String type) {
        this.mSisdas = mSisdas;
        this.mContext = mContext;
        this.type = type; //register or detail
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
        holder.sisda.setText(mSisdas.get(position).getSisda());
        holder.priority.setText(mSisdas.get(position).getPriority());
        holder.address.setText(mSisdas.get(position).getAddress() + " " + mSisdas.get(position).getNumber());
        holder.level.setText(mSisdas.get(position).getLevel());
        holder.progress.setText(mSisdas.get(position).getStatus()); //PROGRESO
        holder.delay.setText(mSisdas.get(position).getDelay());
        holder.time.setText(mSisdas.get(position).getTime());
        holder.start.setText("Generado el: "+mSisdas.get(position).getDateCreation()); //AGREGADO
        if(mSisdas.get(position).getDelay().equals("A Tiempo A")) {
            holder.delay.setText("A Tiempo");
            holder.delay.setBackgroundResource(R.drawable.bg_progress_caution);
        }else if (mSisdas.get(position).getDelay().equals("Retrasado")) {
            holder.delay.setBackgroundResource(R.drawable.bg_progress_warning);
        }else if (mSisdas.get(position).getDelay().equals("A Tiempo"))
            holder.delay.setBackgroundResource(R.drawable.bg_progress_good);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: "+ mSisdas.get(position).getSisda());
                if (type.equals("detail"))
                openSisdaDetail(mSisdas.get(position)/*mSisdas.get(position).getmSisdas()*/);
                else if (type.equals("register"))
                openSisdaRegister(mSisdas.get(position)/*mSisdas.get(position).getmSisdas()*/);
            }
        });
    }


    public void openSisdaDetail(Sisda sisdaObject){
        Gson gson = new Gson();
        String sisdaData = gson.toJson(sisdaObject);
        Intent intent = new Intent(mContext,EventDetail.class);
        Bundle miBundle = new Bundle();
        miBundle.putString("detailSisda",sisdaData);
        intent.putExtras(miBundle);
        mContext.startActivity(intent);

    }
    public void openSisdaRegister(Sisda sisdaObject){
        Gson gson = new Gson();
        String sisdaData = gson.toJson(sisdaObject);
        Intent intent = new Intent(mContext,EventRegistration.class);
        Bundle miBundle = new Bundle();
        miBundle.putString("detailSisda",sisdaData);
        intent.putExtras(miBundle);
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
        TextView start;
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
            start = itemView.findViewById(R.id.init_time); //AGREGADO
        }
    }
}