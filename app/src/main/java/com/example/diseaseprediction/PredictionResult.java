package com.example.diseaseprediction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diseaseprediction.object.Advise;
import com.example.diseaseprediction.object.DiseaseAdvise;
import com.example.diseaseprediction.object.Prediction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PredictionResult extends AppCompatActivity {

    private DatabaseReference mRef;
    private DatabaseReference mRef2;
    private FirebaseUser fUser;
    private Intent intent;

    private ArrayAdapter<String> adviseAdapter;
    private Prediction mPrediction;

    private TextView prediction_txt_disease_result, prediction_txt_disease_description_result,
            prediction_txt_status;
    private ImageView prediction_img_status, prediction_toolbar_img_pre;
    private ListView prediction_listview_advice_result;
    private Button prediction_btn_contact_doctor, prediction_btn_back;

    /**
     * Set height of listview manual
     *
     * @param listView ListView
     */
    public static void setListViewHeightBasedOnChildren(final ListView listView) {
        listView.post(new Runnable() {
            @Override
            public void run() {
                ListAdapter listAdapter = listView.getAdapter();
                if (listAdapter == null) {
                    return;
                }
                int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
                int listWidth = listView.getMeasuredWidth();
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    View listItem = listAdapter.getView(i, null, listView);
                    listItem.measure(
                            View.MeasureSpec.makeMeasureSpec(listWidth, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    totalHeight += listItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)));
                listView.setLayoutParams(params);
                listView.requestLayout();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediciton_result);
        //Get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        //Find view
        findView();

        prediction_toolbar_img_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        prediction_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Get receiver id
        intent = getIntent();
        mPrediction = (Prediction) intent.getSerializableExtra("mPrediction");

//        Prediction pm = new Prediction("idpre1", "kwTsRETY26NguL5v5ffyg3argSG3", "GFoBDpKPUealixZAOQvfoJhiiSE2",
//                "-MdGsAcm_EPiCRhw3PWS", "1", "Default", new Date(), new Date(), 0);

//        mRef = FirebaseDatabase.getInstance().getReference("Prediction").child("idpre1");
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Prediction pr = snapshot.getValue(Prediction.class);
//                getDataToUI(pr);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    /**
     * Find view by ID
     */
    private void findView() {
        prediction_txt_status = findViewById(R.id.prediction_txt_status);
        prediction_txt_disease_result = findViewById(R.id.prediction_txt_disease_result);
        prediction_txt_disease_description_result = findViewById(R.id.prediction_txt_disease_description_result);
        prediction_listview_advice_result = findViewById(R.id.prediction_listview_advice_result);
        prediction_img_status = findViewById(R.id.prediction_img_status);
        prediction_toolbar_img_pre = findViewById(R.id.prediction_toolbar_img_pre);
        prediction_btn_contact_doctor = findViewById(R.id.prediction_btn_contact_doctor);
        prediction_btn_back = findViewById(R.id.prediction_btn_back);
    }

    /**
     * Load data to UI
     *
     * @param mPrediction
     */
    private void getDataToUI(Prediction mPrediction) {
        mRef = FirebaseDatabase.getInstance().getReference("Prediction");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sn : snapshot.getChildren()) {
                    Prediction pr = sn.getValue(Prediction.class);
                    if (pr.getPredictionID().equals(mPrediction.getPredictionID())) {
                        if (pr.getStatus() == 0) {
                            prediction_btn_contact_doctor.setEnabled(false);
                            prediction_txt_status.setText(getString(R.string.prediction_txt_status_pending));
                            prediction_txt_status.setTextColor(Color.parseColor("#ff9931"));
                            prediction_img_status.setImageResource(R.drawable.ic_pending);
                        } else if (pr.getStatus() == 1) {
                            prediction_btn_contact_doctor.setEnabled(true);
                            prediction_txt_status.setText(getString(R.string.prediction_txt_status_correct));
                            prediction_txt_status.setTextColor(Color.parseColor("#3bbf45"));
                            prediction_img_status.setImageResource(R.drawable.ic_correct);
                        } else if (pr.getStatus() == 2) {
                            prediction_btn_contact_doctor.setEnabled(true);
                            prediction_txt_status.setText(getString(R.string.prediction_txt_status_incorrect));
                            prediction_txt_status.setTextColor(Color.parseColor("#ff2b55"));
                            prediction_img_status.setImageResource(R.drawable.ic_incorrect);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Get disease
        mRef = FirebaseDatabase.getInstance().getReference("Disease").child(mPrediction.getDiseaseID());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prediction_txt_disease_result.setText(snapshot.child("name").getValue().toString());
                prediction_txt_disease_description_result.setText(snapshot.child("description").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getAdviseList(mPrediction.getDiseaseID());
    }

    private void getAdviseList(String diseaseID) {
        ArrayList<String> tempAdvise = new ArrayList<>();
        List<String> mAdvise = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference("DiseaseAdvise");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Loop and get all value that equal to disease ID
                for (DataSnapshot sn : snapshot.getChildren()) {
                    DiseaseAdvise da = sn.getValue(DiseaseAdvise.class);
                    if (da.getDiseaseID().equals(diseaseID)) {
                        //Add adviseID to temp array
                        tempAdvise.add(da.getAdviseID());
                    }
                }

                //Get name of advise
                mRef2 = FirebaseDatabase.getInstance().getReference("Advise");
                mRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot sn : snapshot.getChildren()) {
                            for (String id : tempAdvise) {
                                Advise av = sn.getValue(Advise.class);
                                if (id.equals(av.getAdviseID())) {
                                    //Add symptom to list symptom (Object)
                                    mAdvise.add(av.getDescription());
                                }
                            }
                        }
                        setListViewHeightBasedOnChildren(prediction_listview_advice_result);
                        adviseAdapter = new ArrayAdapter<String>(PredictionResult.this, android.R.layout.simple_list_item_1, mAdvise);
                        prediction_listview_advice_result.setAdapter(adviseAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}