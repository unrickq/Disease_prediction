package com.example.diseaseprediction.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.R;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.ViewHolder> {
    @NonNull
    @Override
    public PredictionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prediction_item, parent, false);
        return new PredictionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
