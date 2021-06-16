package com.example.diseaseprediction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.object.Prediction;
import com.example.diseaseprediction.ui.account.AccountFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PredicitonResult extends AppCompatActivity {

    private DatabaseReference mRef;
    private FirebaseUser fUser;
    private Intent intent;
    private Prediction mPrediction;

    private TextView prediction_txt_acc_name, prediction_txt_acc_gender, prediction_txt_acc_phone,
            prediction_txt_disease_result, prediction_txt_disease_description_result,
            prediction_txt_symptom_result, prediction_txt_medicine_result, prediction_txt_advice_result;
    private CircleImageView prediction_img_avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediciton_result);
        //Get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        //Find view
        findView();

        //Get receiver id
        intent = getIntent();
        mPrediction = (Prediction) intent.getSerializableExtra("mPrediction");

        getDataToUI(mPrediction);

    }

    /**
     * Find view by ID
     */
    private void findView() {
        prediction_txt_acc_name = findViewById(R.id.prediction_txt_acc_name);
        prediction_txt_acc_gender = findViewById(R.id.prediction_txt_acc_gender);
        prediction_txt_acc_phone = findViewById(R.id.prediction_txt_acc_phone);
        prediction_txt_disease_result = findViewById(R.id.prediction_txt_disease_result);
        prediction_txt_disease_description_result = findViewById(R.id.prediction_txt_disease_description_result);
        prediction_txt_symptom_result = findViewById(R.id.prediction_txt_symptom_result);
        prediction_txt_medicine_result = findViewById(R.id.prediction_txt_medicine_result);
        prediction_txt_advice_result = findViewById(R.id.prediction_txt_advice_result);
        prediction_img_avatar = findViewById(R.id.prediction_img_avatar);
    }

    /**
     * Load data to UI
     * @param mPrediction
     */
    private void getDataToUI(Prediction mPrediction){
        getAccInformation(mPrediction);
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

        //Get symptom
        mRef = FirebaseDatabase.getInstance().getReference("Symptom").child(mPrediction.getSymptomsID());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prediction_txt_symptom_result.setText(snapshot.child("name").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Get medicine
        mRef = FirebaseDatabase.getInstance().getReference("Medicine").child(mPrediction.getMedicineID());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prediction_txt_medicine_result.setText(snapshot.child("name").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Get advise
        mRef = FirebaseDatabase.getInstance().getReference("Advise").child(mPrediction.getMedicineID());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prediction_txt_advice_result.setText(snapshot.child("description").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * Get account information and load it to UI
     * @param mPrediction
     */
    private void getAccInformation(Prediction mPrediction){
        mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(mPrediction.getPatientID());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prediction_txt_acc_name.setText(snapshot.child("name").getValue().toString());
                prediction_txt_acc_phone.setText(snapshot.child("phone").getValue().toString());

                if (snapshot.child("gender").getValue().toString().equals("1")){
                    prediction_txt_acc_gender.setText(getString(R.string.default_gender_male));
                }else if (snapshot.child("gender").getValue().toString().equals("2")){
                    prediction_txt_acc_gender.setText(getString(R.string.default_gender_female));
                } else if (snapshot.child("gender").getValue().toString().equals("-1")) {
                    prediction_txt_acc_gender.setText(getString(R.string.default_choose_gender));
                }

                //Set image
                if (!snapshot.child("image").getValue().toString().equals("Default")) {
                    Glide.with(PredicitonResult.this).load(snapshot.child("image").getValue().toString())
                            .into(prediction_img_avatar);
                } else {
                    Glide.with(PredicitonResult.this).load(R.drawable.background_avatar).into(prediction_img_avatar);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}