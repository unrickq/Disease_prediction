package com.example.diseaseprediction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.R;

import java.util.ArrayList;
import java.util.List;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.ViewHolder> {
    private AdapterView.OnItemClickListener mlistner;
    private List<String> mList;
    private Context mContext;

    public PredictionAdapter(List<String> predictionlist) {
        this.mList = predictionlist;
    }
    @NonNull
    @Override
    public PredictionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prediction_item, parent, false);
        return new PredictionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PredictionAdapter.ViewHolder holder, int position) {
        //Test data
        String data = mList.get(position);
        //set view
        holder.prediction_disease.setText(data);
        holder.prediction_status.setText("data");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView prediction_disease;
        public TextView prediction_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //System.out.println ("Quang"+ mList.size() );
            prediction_disease = itemView.findViewById(R.id.prediction_disease);
            prediction_status = itemView.findViewById(R.id.prediction_status);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
