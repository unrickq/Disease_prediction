package com.example.diseaseprediction.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.PredictionConfirm;
import com.example.diseaseprediction.PredictionResult;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.Prediction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.ViewHolder> {
    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private Context mContext;
    private List<Prediction> mPredictions;
    private SimpleDateFormat sdf;
    private int goToScreen;

    /**
     * @param context
     * @param mPredictions
     * @param goToScreen   0: Screen doctor confirm | 1: Screen prediction result
     * @param sizeLoad
     */
    public PredictionAdapter(Context context, List<Prediction> mPredictions, int goToScreen, int sizeLoad) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        this.mContext = context;
        this.goToScreen = goToScreen;

        //Limit row load
        this.mPredictions = new ArrayList<>();
        if (sizeLoad >= mPredictions.size()) {
            this.mPredictions = mPredictions;
        } else {
            for (int i = 0; i < sizeLoad; i++) {
                this.mPredictions.add(mPredictions.get(i));
            }
        }
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
                if (goToScreen == 0) {
                    //Doctor confirm prediction
                    Intent i = new Intent(mContext, PredictionConfirm.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("mPrediction", prediction);
                    mContext.startActivity(i);
                } else if (goToScreen == 1) {
                    //Patient prediction
                    Intent i = new Intent(mContext, PredictionResult.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("mPrediction", prediction);
                    mContext.startActivity(i);
                }
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
        mRef.addValueEventListener(new ValueEventListener() {
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
                    item_prediction_txt_status.setText(R.string.prediction_adapter_confirmed_correct);
                } else if (pd.getStatus() == 2) {
                    item_prediction_txt_status.setText(R.string.prediction_txt_status_incorrect);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
