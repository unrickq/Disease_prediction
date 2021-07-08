package com.example.diseaseprediction.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.Constants;
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

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.ViewHolder> {
  private DatabaseReference mRef;
  private FirebaseUser fUser;

  private Context mContext;
  private List<Prediction> mPredictions;
  private SimpleDateFormat sdf;
  private int goToScreen;
  private static final String LOG_TAG = "PredictionAdapter";
  private ArrayList<Integer> indexList;

  /**
   * @param context      Context
   * @param mPredictions List of predictions
   * @param goToScreen   0: Screen doctor confirm | 1: Screen prediction result
   * @param sizeLoad     size of prediction list
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
    // find indexes of prediction that start a new day
    indexList = findIndex(mPredictions);
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
    getDataToUI(prediction, holder.item_prediction_txt_disease, holder.item_prediction_txt_status);

    // set default layout
    holder.prediction_item_divider.setVisibility(View.VISIBLE);
    holder.item_prediction_txt_month_day.setVisibility(View.GONE);

    // Display Date
    for (Integer i : indexList) {
      // Set Date label or prediction
      if (position == i) {
        holder.item_prediction_txt_month_day.setVisibility(View.VISIBLE);
        // if prediction was created today
        if (DateUtils.isSameDay(prediction.getDateCreate(), new Date())) {
          holder.item_prediction_txt_month_day.setText(R.string.prediction_txt_today);
        } else {
          sdf = new SimpleDateFormat("MMM");
          String month = sdf.format(prediction.getDateCreate().getTime());
          sdf = new SimpleDateFormat("dd");
          String day = sdf.format(prediction.getDateCreate().getTime());
          holder.item_prediction_txt_month_day.setText(month + ", " + day);
        }
        break;
      }
      // if this prediction is the last item of the day -> remove divider
      if (position == i - 1) {
        holder.prediction_item_divider.setVisibility(View.GONE);
      }
    }
    // if this prediction is the last item of the list -> remove divider
    if (position == mPredictions.size() - 1) {
      holder.prediction_item_divider.setVisibility(View.GONE);
    }


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
    public LinearLayout item_prediction_img_main;
    public TextView item_prediction_txt_month_day, item_prediction_txt_disease, item_prediction_txt_status;
    public View prediction_item_divider;

    public ViewHolder(@NonNull View view) {
      super(view);
      item_prediction_txt_month_day = view.findViewById(R.id.item_prediction_txt_month_day);
      item_prediction_txt_disease = view.findViewById(R.id.item_prediction_txt_disease);
      item_prediction_txt_status = view.findViewById(R.id.item_prediction_txt_status);
      prediction_item_divider = view.findViewById(R.id.prediction_item_divider);
    }

  }

  /**
   * Get data from firebase and set to UI
   *
   * @param prediction                  current prediction
   * @param item_prediction_txt_disease TextView that displays disease name
   * @param item_prediction_txt_status  TextView that displays prediction status
   */
  public void getDataToUI(Prediction prediction, TextView item_prediction_txt_disease,
                          TextView item_prediction_txt_status) {
    //Get disease
    mRef = FirebaseDatabase.getInstance().getReference("Disease").child(prediction.getDiseaseID());
    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (prediction.getDiseaseID().equals(Constants.DISEASE_OTHER_ID)) {
          String unknownDisease = prediction.getNotes();
          item_prediction_txt_disease.setText(unknownDisease);
        } else {
          item_prediction_txt_disease.setText(snapshot.child("name").getValue().toString());
        }
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

        if (pd.getStatus() == 0) {
          item_prediction_txt_status.setText(R.string.prediction_adapter_waiting_confirm);
          item_prediction_txt_status.setTextColor(mContext.getResources().getColor(R.color.text_warning));
        } else if (pd.getStatus() == 1) {
          item_prediction_txt_status.setText(R.string.prediction_adapter_confirmed_correct);
          item_prediction_txt_status.setTextColor(mContext.getResources().getColor(R.color.text_success));
        } else if (pd.getStatus() == 2) {
          item_prediction_txt_status.setText(R.string.prediction_txt_status_incorrect);
          item_prediction_txt_status.setTextColor(mContext.getResources().getColor(R.color.text_success));
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  /**
   * Find indexes of Predictions in prediction list that start a new day
   *
   * @param mPredictions list of prediction
   * @return array that store indexes of predictions in prediction list
   */
  private ArrayList<Integer> findIndex(List<Prediction> mPredictions) {
    ArrayList<Integer> result = new ArrayList<>();
    Date currentDate = new Date(2000, 2, 11);
    for (int i = 0; i < mPredictions.size(); i++) {
      Prediction prediction = mPredictions.get(i);
      // Date not the same
      if (!DateUtils.isSameDay(prediction.getDateCreate(), currentDate)) {
        currentDate = prediction.getDateCreate();
        result.add(i);
      }
    }
    return result;
  }
}
