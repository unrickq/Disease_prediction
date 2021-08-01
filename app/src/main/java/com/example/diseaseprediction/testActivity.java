package com.example.diseaseprediction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diseaseprediction.firebase.FirebaseConstants;
import com.example.diseaseprediction.object.Medicine;
import com.example.diseaseprediction.object.PredictionMedicine;
import com.example.diseaseprediction.object.Symptom;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class testActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH = 10;
    FirebaseUser firebaseUser;
    DatabaseReference mRef;
    LinearLayout layoutList;
    Button buttonAdd, submit;

    private ArrayList<Medicine> loadMedicineList = new ArrayList<>();
    private ArrayList<PredictionMedicine> predictionMedicineList = new ArrayList<>();
    private String predictionID = "-Mf8Z9n6PEIjCph3O4YB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        layoutList = findViewById(R.id.layout_list);
//        buttonAdd = findViewById(R.id.button);
//        submit = findViewById(R.id.submit);
//
//        GetPredictionMedicineToUI(predictionID);
//        buttonAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Create default prediction medicine layout
//                getMedicineAndDosageToUI(AppConstants.MEDICINE_OTHER_ID, "1 Viên");
//            }
//        });
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //SavePredictionMedicineList();
//            }
//        });

    }

//    private void SavePredictionMedicineList() {
//        //Check valid data of UI (Is empty or not)
//        if (checkMedicineLayout() == 0) {
//            //Check predictionMedicineList is empty or not
//            if (predictionMedicineList.size() == 0) {
//                Toast.makeText(testActivity.this, "Add prediction First!", Toast.LENGTH_SHORT).show();
//            } else {
//                removeAllPredictionMedicine(predictionID);
//                for (int i = 0; i < predictionMedicineList.size(); i++) {
//                    PredictionMedicine tmp = new PredictionMedicine(predictionID,
//                            predictionMedicineList.get(i).getMedicineID(),
//                            predictionMedicineList.get(i).getDosage(),
//                            predictionMedicineList.get(i).getNotes(), 1);
//                    addPredictionMedicine(tmp);
//                }
//                Toast.makeText(testActivity.this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    /**
//     * Load prediction medicine to UI
//     *
//     * @param predictionID prediction ID
//     */
//    private void GetPredictionMedicineToUI(String predictionID) {
//        try {
//            Query QGetPredictionMedicine = FirebaseDatabase.getInstance()
//                    .getReference(FirebaseConstants.FIREBASE_TABLE_PREDICTION_MEDICINE)
//                    .orderByChild("predictionID").equalTo(predictionID);
//            QGetPredictionMedicine.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                    for (DataSnapshot sn : snapshot.getChildren()) {
//                        PredictionMedicine pm = sn.getValue(PredictionMedicine.class);
//                        //
//                        getMedicineAndDosageToUI(pm.getMedicineID(), pm.getDosage());
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
////            Log.d(LOG_TAG, "checkPredictionStatus()");
//        }
//    }
//
//    private void getMedicineAndDosageToUI(String medicineID, String dosage) {
//        try {
//            Query QGetMedicine = FirebaseDatabase.getInstance()
//                    .getReference(FirebaseConstants.FIREBASE_TABLE_MEDICINE)
//                    .orderByChild("medicineID").equalTo(medicineID);
//            QGetMedicine.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                    for (DataSnapshot sn : snapshot.getChildren()) {
//                        Medicine m = sn.getValue(Medicine.class);
//                        addView(m, dosage);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            //Log.d(LOG_TAG, "checkPredictionStatus()");
//        }
//    }
//
//
//    private void addView(Medicine setMedicine, String dosage) {
//        //Clear medicine array
//        loadMedicineList.clear();
//        //Find view of layout
//        final View item_add_medicine
//                = getLayoutInflater().inflate(R.layout.item_medicine_add, null, false);
//        TextInputLayout item_add_medicine_editText_order_layout
//                = item_add_medicine.findViewById(R.id.item_add_medicine_editText_order_layout);
//        TextInputLayout item_add_medicine_editText_dosage_layout
//                = item_add_medicine.findViewById(R.id.item_add_medicine_editText_dosage_layout);
//        AutoCompleteTextView item_add_medicine_autoComplete
//                = item_add_medicine.findViewById(R.id.item_add_medicine_autoComplete);
//        ImageView item_add_medicine_btn_delete
//                = item_add_medicine.findViewById(R.id.item_add_medicine_btn_delete);
//        TextView hiddenMedicineValue
//                = item_add_medicine.findViewById(R.id.hiddenMedicineValue);
//
//        //Load list Medicine
//        mRef = FirebaseDatabase.getInstance().getReference(FirebaseConstants.FIREBASE_TABLE_MEDICINE);
//        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                for (DataSnapshot sn : snapshot.getChildren()) {
//                    Medicine medicine = sn.getValue(Medicine.class);
//                    loadMedicineList.add(medicine);
//                }
//
//                ArrayAdapter arrayAdapter = new ArrayAdapter(testActivity.this, android.R.layout.simple_spinner_item, loadMedicineList);
//                item_add_medicine_autoComplete.setAdapter(arrayAdapter);
//
//                //Handle event item click AutoCompleteTextView
//                item_add_medicine_autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Medicine tmpM = (Medicine) adapterView.getItemAtPosition(i);
//                        hiddenMedicineValue.setText(tmpM.getMedicineID());
//                        if (item_add_medicine_autoComplete.getText().toString().equals(AppConstants.MEDICINE_OTHER_NAME)) {
//                            item_add_medicine_editText_order_layout.setVisibility(View.VISIBLE);
//                        } else {
//                            item_add_medicine_editText_order_layout.setVisibility(View.GONE);
//                        }
//                    }
//                });
//
//                //Set data to UI
//                item_add_medicine_autoComplete.setText(setMedicine.getName());
//                arrayAdapter.getFilter().filter(null);
//                //IF value equal to MEDICINE_OTHER_NAME. Then set visible editText
//                if (item_add_medicine_autoComplete.getText().toString().equals(AppConstants.MEDICINE_OTHER_NAME)) {
//                    item_add_medicine_editText_order_layout.setVisibility(View.VISIBLE);
//                } else {
//                    item_add_medicine_editText_order_layout.setVisibility(View.GONE);
//                }
//                hiddenMedicineValue.setText(setMedicine.getMedicineID());
//                item_add_medicine_editText_dosage_layout.getEditText().setText(dosage);
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//
//        //Button X
//        item_add_medicine_btn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeView(item_add_medicine);
//            }
//        });
//        //Add layout
//        layoutList.addView(item_add_medicine);
//    }

    //Check data on medicine layout
//    private int checkMedicineLayout() {
//        //List prediction medicine
//        predictionMedicineList.clear();
//        int checkValid = 0;
//        String otherMedicine = "";
//
//        //Loop all layout
//        for (int i = 0; i < layoutList.getChildCount(); i++) {
//            PredictionMedicine prm = new PredictionMedicine(predictionID, "Default", "Default", "Default", 1);
//            //Get view on each layout
//            View item_add_medicine = layoutList.getChildAt(i);
//            //Find UI
//            TextInputLayout item_add_medicine_editText_dosage_layout
//                    = item_add_medicine.findViewById(R.id.item_add_medicine_editText_dosage_layout);
//            TextInputLayout item_add_medicine_editText_order_layout
//                    = item_add_medicine.findViewById(R.id.item_add_medicine_editText_order_layout);
//            AutoCompleteTextView item_add_medicine_autoComplete
//                    = item_add_medicine.findViewById(R.id.item_add_medicine_autoComplete);
//            TextView hiddenMedicineValue
//                    = item_add_medicine.findViewById(R.id.hiddenMedicineValue);
//
//            //Check valid on each layout
//            //If AutoCompleteTextView is other medicine
//            if (item_add_medicine_autoComplete.getText().toString().equals(AppConstants.MEDICINE_OTHER_NAME)) {
//                otherMedicine = item_add_medicine_editText_order_layout.getEditText().getText().toString();
//                //Check if other_medicine_editText empty
//                if (!otherMedicine.trim().isEmpty()) {
//                    //Check editText dosage
//                    if (!item_add_medicine_editText_dosage_layout.getEditText().getText().toString().equals("")) {
//                        prm.setDosage(item_add_medicine_editText_dosage_layout.getEditText().getText().toString());
//                        prm.setMedicineID(hiddenMedicineValue.getText().toString());
//                        // !!!!! Add note that other medicine name !!!!
//                        // !!!!!
//                    }
//                } else {
//                    //Other medicine empty
//                    item_add_medicine_editText_order_layout.setError(getString(R.string.error_field_empty));
//                    checkValid++;
//                    break;
//                }
//            }
//            //Check dosage empty
//            if (!item_add_medicine_editText_dosage_layout.getEditText().getText().toString().equals("")) {
//                prm.setDosage(item_add_medicine_editText_dosage_layout.getEditText().getText().toString());
//                prm.setMedicineID(hiddenMedicineValue.getText().toString());
//            } else {
//                item_add_medicine_editText_dosage_layout.setError("Please fill this");
//                checkValid++;
//                break;
//            }
//            //Add all prediction medicine to list
//            predictionMedicineList.add(prm);
//        }
//
//        return checkValid;
//    }

    private void removeView(View view) {
        layoutList.removeView(view);
    }

    /**
     * Add prediction Medicine
     *
     * @param predictionMedicine predictionMedicine
     */
    private void addPredictionMedicine(PredictionMedicine predictionMedicine) {
        try {
            mRef = FirebaseDatabase.getInstance().getReference(FirebaseConstants.FIREBASE_TABLE_PREDICTION_MEDICINE);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    mRef.child(mRef.push().getKey())
                            .setValue(predictionMedicine);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
//            Log.d(LOG_TAG, "checkPredictionStatus()");
        }
    }

    /**
     * Remove all predictionMedicine by predictionID
     *
     * @param predictionID prediction ID
     */
    private void removeAllPredictionMedicine(String predictionID) {
        try {
            Query QGetMedicine = FirebaseDatabase.getInstance()
                    .getReference(FirebaseConstants.FIREBASE_TABLE_PREDICTION_MEDICINE)
                    .orderByChild("predictionID").equalTo(predictionID);
            QGetMedicine.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot sn : snapshot.getChildren()) {
                        sn.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
//            Log.d(LOG_TAG, "setMessageFirebase()");
        }
    }
//        myRef = FirebaseDatabase.getInstance().getReference("Session");
//        Query a2 = FirebaseDatabase.getInstance().getReference("PredictionSymptom")
//                .child("ID tu push").setValue(object cua prediction symptom).
//        //Tìm tất cả các Node có status = 0
//        Query a2 = FirebaseDatabase.getInstance().getReference("Session")
//                .orderByChild("status")
//                .equalTo(0);
//        a2.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    Session ss = ds.getValue(Session.class);
//                    System.out.println("quang " + ss.getSessionID());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQUEST_CODE_SPEECH:
//                if (resultCode == RESULT_OK && data != null) {
//                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    txt.setText(result.get(0));
//                }
//                break;
//        }
//    }

//    void addDataSpecialization(DoctorSpecialization sp){
//        myRef = FirebaseDatabase.getInstance().getReference("Specialization");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                sp.setSpecializationID(myRef.push().getKey());
//                myRef.child(sp.getSpecializationID()).setValue(sp);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//        void addDataDiseaseAdvise(DiseaseAdvise da){
//        myRef = FirebaseDatabase.getInstance().getReference("DiseaseAdvise");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                myRef.child(myRef.push().getKey()).setValue(da);
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//    }
////
//    void addDataAdvise(Advise ad){
//        myRef = FirebaseDatabase.getInstance().getReference("Advise");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
////                ad.setAdviseID(myRef.push().getKey());
//                myRef.child(ad.getAdviseID()).setValue(ad);
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    void addDataMedicine(Medicine md){
//        mRef = FirebaseDatabase.getInstance().getReference("Medicine");
//        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                md.setMedicineID(mRef.push().getKey());
//                mRef.child(md.getMedicineID()).setValue(md);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    void addDataDisease(Disease ds){
//        myRef = FirebaseDatabase.getInstance().getReference("Disease");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                //                ds.setDiseaseID(myRef.push().getKey());
//                myRef.child(ds.getDiseaseID()).setValue(ds);
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//    }

    //
    void addDataSymptom(Symptom sm) {
        mRef = FirebaseDatabase.getInstance().getReference("Symptom");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                sm.setSymptomsID(myRef.push().getKey());
                mRef.child(sm.getSymptomID()).setValue(sm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    addDataDiseaseAdvise(new DiseaseAdvise("1",  "1", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("1",  "2", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("1",  "3", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("1",  "4", 1));
//
//    addDataDiseaseAdvise(new DiseaseAdvise("2",  "12", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("2",  "14", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("2",  "20", 1));
//
//    addDataDiseaseAdvise(new DiseaseAdvise("3",  "14", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("3",  "17", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("3",  "20", 1));
//
//    addDataDiseaseAdvise(new DiseaseAdvise("4",  "3", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("4",  "4", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("4",  "13", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("4",  "19", 1));
//
//    addDataDiseaseAdvise(new DiseaseAdvise("5",  "13", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("5",  "18", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("5",  "2", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("5",  "3", 1));
//
//    addDataDiseaseAdvise(new DiseaseAdvise("6",  "3", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("6",  "5", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("6",  "2", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("6",  "20", 1));
//
//    addDataDiseaseAdvise(new DiseaseAdvise("7",  "3", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("7",  "12", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("7",  "16", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("7",  "20", 1));
//
//    addDataDiseaseAdvise(new DiseaseAdvise("8",  "4", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("8",  "3", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("8",  "5", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("8",  "7", 1));
//
//    addDataDiseaseAdvise(new DiseaseAdvise("9",  "13", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("9",  "18", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("9",  "2", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("9",  "3", 1));
//
//    addDataDiseaseAdvise(new DiseaseAdvise("10",  "10", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("10",  "7", 1));
//    addDataDiseaseAdvise(new DiseaseAdvise("10",  "3", 1));

//        addDataAdvise(new Advise("1", "che miệng trong lúc ho", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("2", "tham khảo ý kiến bác sĩ", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("3", "uống thuốc theo hướng dẫn của bác sĩ", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("4", "nghỉ ngơi điều độ", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("5", "ăn uống lành mạnh, khoa học", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("6", "theo dõi tình hình sức khỏe bản thân", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("7", "không uống rượu, bia", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("8", "tập luyện thể thao", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("9", "không hút thuốc lá", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("10", "ăn uống lành mạnh", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("11", "thường xuyên rữa tay", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("12", "tránh ăn đồ ăn có nhiều chất béo, cay, nóng...", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("13", "đến bệnh viện gần nhất để kiểm tra (đây là bệnh có tỉ lệ tử vong cao)", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("14", "tránh bị muỗi đốt", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("15", "uống đồ uống giàu vitamin c", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("16", "tránh đồ ăn, thức uống lạnh", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("17", "thực hiện các biện pháp giảm sốt", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("18", "đến ngay bệnh viện để kiểm tra tình trạng bệnh", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("19", "tránh tiếp xúc trực tiếp với mọi người xung quanh", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("20", "uống nước nhiều nước", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("21", "sử dụng các biện pháp giảm nhiệt độ", new Date(), new Date(), 1));

//        addDataSymptom(new Symptom("1",  "ho mãn tính",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("2",  "ho",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("3",  "sốt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("4",  "ho ra máu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("5",  "sụt cân",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("6",  "buồn nôn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("7",  "nhức đầu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("8",  "đau họng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("9",  "sổ mũi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("10",  "ho khan",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("11",  "khó thở",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("12",  "thở nhanh",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("13",  "mệt mỏi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("14",  "mất vị giác",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("15",  "mất khứ giác",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("16",  "nước tiểu đậm",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("17",  "tiêu chảy",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("18",  "vàng da",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("19",  "buồn nôn ói mửa",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("20",  "đau tinh hoàn",  "Default", new Date(),  new Date(), 1));
//addDataDisease(new Disease("99999", "-Mct1vQY8dBaxrRr1eog", "Default", "Default", new Date(), new Date(), 1));
//        addDataDisease(new Disease("1", "-Mct1vQY8dBaxrRr1eog", "Bệnh sốt rét", "", new Date(), new Date(), 1));
//        addDataDisease(new Disease("2", "-Mct1vQWhFAEtX8TjL_D", "viêm gan A", "Viêm gan A là một bệnh nhiễm trùng gan rất dễ lây lan do vi rút viêm gan A gây ra. Virus này là một trong số các loại virus viêm gan gây viêm và ảnh hưởng đến khả năng hoạt động của gan.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("3", "-Mct1vQY8dBaxrRr1eog", "Cảm lạnh thông thường", "Cảm lạnh thông thường là một bệnh nhiễm trùng do vi-rút ở mũi và cổ họng (đường hô hấp trên). Nó thường vô hại, mặc dù nó có thể không cảm thấy như vậy. Nhiều loại vi-rút có thể gây ra cảm lạnh thông thường.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("4", "-Mct1vQWhFAEtX8TjL_D", "Bệnh viêm gan B", "Viêm gan B là một bệnh nhiễm trùng ở gan của bạn. Nó có thể gây sẹo nội tạng, suy gan và ung thư. Nó có thể gây tử vong nếu không được điều trị. Nó lây lan khi mọi người tiếp xúc với máu, vết loét hở hoặc chất dịch cơ thể của người có vi rút viêm gan B.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("5", "-Mct1vQWhFAEtX8TjL_D", "Viêm gan C", "Viêm gan do siêu vi viêm gan C (HCV), thường lây lan qua truyền máu (hiếm gặp), chạy thận nhân tạo và dùng kim tiêm. Những tổn thương mà viêm gan C gây ra cho gan có thể dẫn đến xơ gan và các biến chứng của nó cũng như ung thư.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("6", "-Mct1vQWhFAEtX8TjL_D", "Viêm gan E", "Một dạng viêm gan hiếm gặp do nhiễm vi rút viêm gan E (HEV). Nó lây truyền qua thức ăn hoặc đồ uống do người bị nhiễm bệnh cầm nắm hoặc qua nguồn cung cấp nước bị nhiễm bệnh ở những khu vực mà phân có thể ngấm vào nước. Viêm gan E không gây ra bệnh gan mãn tính.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("7", "-Mct1vQWhFAEtX8TjL_D", "Viêm gan siêu vi D", "Viêm gan D, còn được gọi là virus viêm gan delta, là một bệnh nhiễm trùng khiến gan bị viêm. Vết sưng này có thể làm suy giảm chức năng gan và gây ra các vấn đề về gan lâu dài, bao gồm cả sẹo gan và ung thư. Tình trạng này do vi rút viêm gan D (HDV) gây ra.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("8", "-Mct1vPzocbo9S4P_RZr", "Viêm phổi", "Viêm phổi là tình trạng nhiễm trùng ở một hoặc cả hai phổi. Vi khuẩn, vi rút và nấm gây ra nó. Nhiễm trùng gây viêm các túi khí trong phổi của bạn, được gọi là phế nang. Các phế nang chứa đầy dịch hoặc mủ, gây khó thở.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("9", "-Mct1vPzocbo9S4P_RZr", "Bệnh lao", "Bệnh lao (TB) là một bệnh truyền nhiễm thường do vi khuẩn Mycobacterium tuberculosis (MTB) gây ra. Bệnh lao thường ảnh hưởng đến phổi, nhưng cũng có thể ảnh hưởng đến các bộ phận khác của cơ thể. Hầu hết các trường hợp nhiễm trùng không có triệu chứng, trong trường hợp đó, nó được gọi là bệnh lao tiềm ẩn.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("10","-Mct1vPzocbo9S4P_RZr", "Covid 19", "COVID-19 (từ tiếng Anh: coronavirus disease 2019 nghĩa là bệnh virus corona 2019)là một bệnh đường hô hấp cấp tính truyền nhiễm gây ra bởi chủng virus corona SARS-CoV-2. Bệnh được phát hiện lần đầu tiên trong đại dịch COVID-19 năm 2019–2020.", new Date(), new Date(), 1));

    //adivse of 9 disease
//        addDataAdvise(new Advise("id", "Tham khảo ý kiến bệnh viện gần nhất", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "tránh thức ăn nhiều dầu mỡ", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "tránh thức ăn không chay", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "giữ muỗi ra ngoài", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "uống đồ uống giàu vitamin c", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "tham khảo ý kiến bác sĩ", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "che miệng", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "rửa tay qua", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "lấy hơi", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "tiêm chủng", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "nghỉ ngơi", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "thuốc", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "tránh đồ ăn cay béo", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "tránh đồ ăn lạnh", new Date(), new Date(), 1));
//        addDataAdvise(new Advise("id", "giữ sốt", new Date(), new Date(), 1));

    //9 disease
//        addDataDisease(new Disease("id", "Bệnh sốt rét", "Một bệnh truyền nhiễm do ký sinh trùng đơn bào thuộc họ Plasmodium gây ra, có thể lây truyền qua vết đốt của muỗi Anopheles hoặc do kim tiêm bị ô nhiễm hoặc truyền máu. Sốt rét Falciparum là loại gây tử vong cao nhất.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "viêm gan A", "Viêm gan A là một bệnh nhiễm trùng gan rất dễ lây lan do vi rút viêm gan A gây ra. Virus này là một trong số các loại virus viêm gan gây viêm và ảnh hưởng đến khả năng hoạt động của gan.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Cảm lạnh thông thường", "Cảm lạnh thông thường là một bệnh nhiễm trùng do vi-rút ở mũi và cổ họng (đường hô hấp trên). Nó thường vô hại, mặc dù nó có thể không cảm thấy như vậy. Nhiều loại vi-rút có thể gây ra cảm lạnh thông thường.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Bệnh viêm gan B", "Viêm gan B là một bệnh nhiễm trùng ở gan của bạn. Nó có thể gây sẹo nội tạng, suy gan và ung thư. Nó có thể gây tử vong nếu không được điều trị. Nó lây lan khi mọi người tiếp xúc với máu, vết loét hở hoặc chất dịch cơ thể của người có vi rút viêm gan B.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Viêm gan C", "Viêm gan do siêu vi viêm gan C (HCV), thường lây lan qua truyền máu (hiếm gặp), chạy thận nhân tạo và dùng kim tiêm. Những tổn thương mà viêm gan C gây ra cho gan có thể dẫn đến xơ gan và các biến chứng của nó cũng như ung thư.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Viêm gan E", "Một dạng viêm gan hiếm gặp do nhiễm vi rút viêm gan E (HEV). Nó lây truyền qua thức ăn hoặc đồ uống do người bị nhiễm bệnh cầm nắm hoặc qua nguồn cung cấp nước bị nhiễm bệnh ở những khu vực mà phân có thể ngấm vào nước. Viêm gan E không gây ra bệnh gan mãn tính.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Viêm gan siêu vi D", "Viêm gan D, còn được gọi là virus viêm gan delta, là một bệnh nhiễm trùng khiến gan bị viêm. Vết sưng này có thể làm suy giảm chức năng gan và gây ra các vấn đề về gan lâu dài, bao gồm cả sẹo gan và ung thư. Tình trạng này do vi rút viêm gan D (HDV) gây ra.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Viêm phổi", "Viêm phổi là tình trạng nhiễm trùng ở một hoặc cả hai phổi. Vi khuẩn, vi rút và nấm gây ra nó. Nhiễm trùng gây viêm các túi khí trong phổi của bạn, được gọi là phế nang. Các phế nang chứa đầy dịch hoặc mủ, gây khó thở.", new Date(), new Date(), 1));
//        addDataDisease(new Disease("id", "Bệnh lao", "Bệnh lao (TB) là một bệnh truyền nhiễm thường do vi khuẩn Mycobacterium tuberculosis (MTB) gây ra. Bệnh lao thường ảnh hưởng đến phổi, nhưng cũng có thể ảnh hưởng đến các bộ phận khác của cơ thể. Hầu hết các trường hợp nhiễm trùng không có triệu chứng, trong trường hợp đó, nó được gọi là bệnh lao tiềm ẩn.", new Date(), new Date(), 1));

//        //Full symptom
//        addDataSymptom(new Symptom("id",  "ngứa",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "phát ban da",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nổi nốt trên da",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "hắt hơi liên tục",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "rùng mình",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "ớn lạnh",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau khớp",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau bụng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "acid dạ dày",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "vết loét trên lưỡi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mỏi cơ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nôn mửa",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tiểu rát",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đi tiểu nhỏ giọt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mệt mỏi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tăng cân",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "thở nhanh",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "bàn tay và bàn chân lạnh",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nhanh chóng thay đổi tâm trạng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "giảm cân",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "bồn chồn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mơ màng và buồn ngủ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đàm trong cổ họng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tăng đường huyết",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "ho",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sốt cao",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mắt trũng sâu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "khó thở",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đổ mồ hôi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mất nước",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "khó tiêu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau đầu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "da hơi vàng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "Nước tiểu đậm",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "buồn nôn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "ăn mất ngon",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau sau mắt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau lưng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "táo bón",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau bụng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "bệnh tiêu chảy",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sốt nhẹ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nước tiểu vàng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "vàng mắt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "suy gan cấp tính",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tình trạng quá tải chất lỏng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sưng bụng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sưng hạch bạch huyết",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "khó chịu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "blurred and distorted vision",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đờm dãi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "viêm họng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đỏ mắt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "áp lực xoang",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sổ mũi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tắc nghẽn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tức ngực",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "yếu tay chân",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nhịp tim nhanh",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau khi đi tiểu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau ở vùng hậu môn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "phân có máu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "kích ứng ở hậu môn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau cổ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "chóng mặt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "chuột rút",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "bầm tím",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "béo phì",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sưng chân",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mạch máu sưng lên",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mặt và mắt sưng húp",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tuyến giáp mở rộng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "móng tay dễ gãy",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tứ chi sưng tấy",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đói bụng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "extra marital contacts",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "khô và ngứa môi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nói lắp",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau đầu gối",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau khớp háng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "yếu cơ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "cổ cứng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "sưng khớp",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "cử động cứng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "spinning movements",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mất thăng bằng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "loạng choạng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau ở một bên cơ thể",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "không cảm nhận được mùi vị",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "khó chịu bàng quang",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mùi hôi của nước tiểu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "cảm giác mắc tiểu liên tục",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "passage of gases",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "ngứa nội tạng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "toxic look (typhos)",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "Phiền muộn",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "cáu gắt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau cơ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "altered sensorium",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đốm đỏ trên cơ thể",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau bụng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "kinh nguyệt bất thường",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "dischromic patches",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "chảy nước mắt",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tăng khẩu vị",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đa niệu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "gia đình có người nhiễm",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đờm nhầy",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đờm rỉ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "thiếu tập trung",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "rối loạn thị giác",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "nhận truyền máu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tiêm thuốc không vô trùng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "hôn mê",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "chảy máu dạ dày",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "chướng bụng",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "từng uống rượu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "fluid overload",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đờm trong máu",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "tĩnh mạch nổi rõ trên bắp chân",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau ngực",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau khi đi bộ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mụn đầy mủ",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mụn đầu đen",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "hối hả",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "lột da",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "silver like dusting",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "vết lõm nhỏ trên móng tay",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "móng tay bị viêm",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "mụn rộp",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "đau đỏ quanh mũi",  "Default", new Date(),  new Date(), 1));
//        addDataSymptom(new Symptom("id",  "rỉ nước vàng",  "Default", new Date(),  new Date(), 1));


//        addDataMedicine(new Medicine("id", "name",  "description",  "manufacturer",  "content", new Date(), new Date(), 1));
}