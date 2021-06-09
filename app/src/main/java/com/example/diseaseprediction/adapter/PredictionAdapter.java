package com.example.diseaseprediction.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.PredicitonResult;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.Disease;
import com.example.diseaseprediction.object.Message;
import com.example.diseaseprediction.object.Prediction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.ViewHolder> {
    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private Context mContext;
    private List<Prediction> mPredictions;
    private SimpleDateFormat sdf;

    public PredictionAdapter(Context context, List<Prediction> mPredictions) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        this.mContext = context;
        this.mPredictions = mPredictions;
    }

    @NonNull
    @Override
    public PredictionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_prediction, parent, false);
        return new PredictionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PredictionAdapter.ViewHolder holder, int position) {
        Prediction prediction = mPredictions.get(position);
        getDataToUI(prediction, holder.item_prediction_txt_month, holder.item_prediction_txt_day
                , holder.item_prediction_txt_disease, holder.item_prediction_txt_status);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, PredicitonResult.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("mPrediction", (Parcelable) prediction);
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPredictions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_prediction_txt_month, item_prediction_txt_day, item_prediction_txt_disease, item_prediction_txt_status;

        public ViewHolder(@NonNull View view) {
            super(view);
            item_prediction_txt_month = view.findViewById(R.id.item_prediction_txt_month);
            item_prediction_txt_day = view.findViewById(R.id.item_prediction_txt_day);
            item_prediction_txt_disease = view.findViewById(R.id.item_prediction_txt_disease);
            item_prediction_txt_status = view.findViewById(R.id.item_prediction_txt_status);
        }

    }

    public void getDataToUI(Prediction prediction, TextView item_prediction_txt_month, TextView item_prediction_txt_day
            , TextView item_prediction_txt_disease, TextView item_prediction_txt_status) {
        //Get disease
        mRef = FirebaseDatabase.getInstance().getReference("Disease").child(prediction.getDiseaseID());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                item_prediction_txt_disease.setText(snapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Get date and status
        mRef = FirebaseDatabase.getInstance().getReference("Prediction").child(prediction.getPredictionID());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Prediction pd = snapshot.getValue(Prediction.class);
                sdf = new SimpleDateFormat("MMM");
                String month = sdf.format(pd.getDateCreate().getTime());
                item_prediction_txt_month.setText(month);
                sdf = new SimpleDateFormat("dd");
                String day = sdf.format(pd.getDateCreate().getTime());
                item_prediction_txt_day.setText(day);
                if (pd.getStatus() == 0) {
                    item_prediction_txt_status.setText(R.string.prediction_adapter_waiting_confirm);
                } else if (pd.getStatus() == 1) {
                    item_prediction_txt_status.setText(R.string.prediction_adapter_confirmed);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
