package com.example.diseaseprediction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.Advise;
import com.example.diseaseprediction.object.ConsultationList;
import com.example.diseaseprediction.object.Disease;
import com.example.diseaseprediction.object.DiseaseAdvise;
import com.example.diseaseprediction.object.Message;
import com.example.diseaseprediction.object.Prediction;
import com.example.diseaseprediction.object.Session;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PredictionResult extends AppCompatActivity {
    private static final String TAG = "PredictionResult";
    private DatabaseReference mRef;
    private DatabaseReference mRef2;
    private FirebaseUser fUser;
    private Intent intent;

    private ArrayAdapter<String> adviseAdapter;
    private Prediction mPrediction;
    private ConsultationList consultationList;
    private String sessionID;

    private TextView prediction_txt_disease_result, prediction_txt_disease_description_result, prediction_listview_advice_result,
            prediction_txt_status, prediction_txt_contact_doctor_click, prediction_txt_disease_title;
    private ImageView prediction_img_status, prediction_toolbar_img_pre;
    private LinearLayout prediction_layout_contact_doctor;
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

        //Get receiver id
        intent = getIntent();
        mPrediction = intent.getParcelableExtra("mPrediction");
        getDataToUI(mPrediction);

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
        prediction_txt_contact_doctor_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef = FirebaseDatabase.getInstance().getReference("Prediction").child(mPrediction.getPredictionID());
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            String doctorID = Objects.requireNonNull(snapshot.child("doctorID").getValue()).toString();
                            if (!doctorID.equals("Default")) {
                                createSessionWithCDoctor(doctorID);
                            }
                        } catch (NullPointerException e) {
                            Log.d(TAG, "prediction_txt_contact_doctor_click", e);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });
    }

    /**
     * Find view by ID
     */
    private void findView() {
        try {
            prediction_txt_status = findViewById(R.id.prediction_txt_status);
            prediction_txt_disease_title = findViewById(R.id.prediction_txt_disease_title);
            prediction_txt_disease_result = findViewById(R.id.prediction_txt_disease_result);
            prediction_txt_disease_description_result = findViewById(R.id.prediction_txt_disease_description_result);
            prediction_listview_advice_result = findViewById(R.id.prediction_listview_advice_result);
            prediction_img_status = findViewById(R.id.prediction_img_status);
            prediction_toolbar_img_pre = findViewById(R.id.prediction_toolbar_img_pre);
            prediction_txt_contact_doctor_click = findViewById(R.id.prediction_txt_contact_doctor_click);
            prediction_btn_back = findViewById(R.id.prediction_btn_back);
            prediction_layout_contact_doctor = findViewById(R.id.prediction_layout_contact_doctor);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "findView()");
        }
    }

    /**
     * Load data to UI
     *
     * @param mPrediction current prediction
     */
    private void getDataToUI(Prediction mPrediction) {
        try {
            mRef = FirebaseDatabase.getInstance().getReference("Prediction").child(mPrediction.getPredictionID());
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Prediction pr = snapshot.getValue(Prediction.class);
                    try {
                        if (pr.getPredictionID().equals(mPrediction.getPredictionID())) {
                            if (pr.getStatus() == 0) {
                                prediction_layout_contact_doctor.setVisibility(View.GONE);
                                prediction_txt_status.setText(getString(R.string.prediction_txt_status_pending));
                                prediction_txt_status.setTextColor(getResources().getColor(R.color.text_warning));
                                prediction_img_status.setImageResource(R.drawable.ic_prediction_status_pending);
                            } else if (pr.getStatus() == 1) {
                                prediction_layout_contact_doctor.setVisibility(View.VISIBLE);
                                prediction_txt_status.setText(getString(R.string.prediction_txt_status_correct));
                                prediction_txt_status.setTextColor(getResources().getColor(R.color.text_success));
                                prediction_img_status.setImageResource(R.drawable.ic_correct);
                            } else if (pr.getStatus() == 2) {
                                prediction_layout_contact_doctor.setVisibility(View.VISIBLE);
                                prediction_txt_status.setText(getString(R.string.prediction_txt_status_correct));
//                            prediction_txt_status.setTextColor(Color.parseColor("#ff2b55"));
                                prediction_txt_status.setTextColor(getResources().getColor(R.color.text_success));
//                            prediction_img_status.setImageResource(R.drawable.ic_incorrect);
                                prediction_img_status.setImageResource(R.drawable.ic_correct);
                            }
                        }
                    } catch (NullPointerException e) {
                        Log.d(TAG, "PredictionResult. getDataToUI", e);
                    }

                    //Get disease
                    mRef2 = FirebaseDatabase.getInstance().getReference("Disease").child(mPrediction.getDiseaseID());
                    mRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Disease disease = snapshot.getValue(Disease.class);
                            // prediction correct
                            prediction_txt_disease_result.setText(disease.getName());
                            prediction_txt_disease_description_result.setText(disease.getDescription());
                            // prediction incorrect
                            if (pr.getStatus() == 2) {
                                prediction_txt_disease_title.setText(getString(R.string.prediction_txt_disease_title).concat(" (Disease" +
                                        " updated)"));
                                // doctor select unknown disease
                                if (pr.getDiseaseID().equals(Constants.DISEASE_OTHER_ID)) {
                                    String unknownDisease = pr.getNotes();
                                    prediction_txt_disease_result.setText(unknownDisease);
                                }
                            }
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

            //Hide 'contact doctor' option if user is a doctor
            mRef = FirebaseDatabase.getInstance().getReference("Accounts");
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(fUser.getUid())) {
                        Account mAccount = snapshot.child(fUser.getUid()).getValue(Account.class);
                        try {
                            if (mAccount.getTypeID() == 0) {
                                prediction_layout_contact_doctor.setVisibility(View.GONE);
                            }
                        } catch (NullPointerException e) {
                            Log.e(TAG, "getDataToUI: TypeID Null", e);
                            Toast.makeText(PredictionResult.this, getString(R.string.error_unknown_contactDev), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            getAdviseList(mPrediction.getDiseaseID());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getDataToUI()");
        }
    }

    /**
     * Get list advise by disease ID
     *
     * @param diseaseID disease ID
     */
    private void getAdviseList(String diseaseID) {
        try {
            ArrayList<String> tempAdvise = new ArrayList<>();
            List<String> mAdvise = new ArrayList<>();
            mRef = FirebaseDatabase.getInstance().getReference("DiseaseAdvise");
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Loop and get all value that equal to disease ID
                    for (DataSnapshot sn : snapshot.getChildren()) {
                        DiseaseAdvise da = sn.getValue(DiseaseAdvise.class);
                        try {
                            if (da.getDiseaseID().equals(diseaseID)) {
                                //Add adviseID to temp array
                                tempAdvise.add(da.getAdviseID());
                            }
                        } catch (NullPointerException e) {
                            Log.d(TAG, "PredictionResult. getAdviseList", e);
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
                                    try {
                                        if (id.equals(av.getAdviseID())) {
                                            //Add symptom to list symptom (Object)
                                            mAdvise.add(av.getDescription());
                                        }
                                    } catch (NullPointerException e) {
                                        Log.d(TAG, "PredictionResult. getAdviseList", e);
                                    }
                                }
                            }

                            String finalText = "";
                            // iterate and concatenate string, then displays advise from adviseList to TextView
                            if (mAdvise.size() != 1) {
                                for (int i = 0; i < mAdvise.size(); i++) {
                                    finalText = finalText.concat("- " + mAdvise.get(i) + "\n");
                                }
                            } else {
                                finalText = mAdvise.get(0);
                            }
                            prediction_listview_advice_result.setText(finalText);
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
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getAdviseList()");
        }
    }

    /**
     * Create new chat session
     * accountIDOne is sender
     * accountIDTwo is receiver
     * CHATBOT_ID is receiver (account two in consultation)
     */
//    private void createSession(String accountIDOne, String accountIDTwo) {
//        try {
//            //Get consultation list of two account
//            mRef = FirebaseDatabase.getInstance().getReference("ConsultationList");
//            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    ConsultationList csl = new ConsultationList();
//                    for (DataSnapshot sn : snapshot.getChildren()) {
//                        csl = sn.getValue(ConsultationList.class);
//                        try {
//                            //Check to find consultation is exist or not
//                            if ((csl.getAccountOne().equals(accountIDOne) && csl.getAccountTwo().equals(accountIDTwo))
//                                    || (csl.getAccountOne().equals(accountIDTwo) && csl.getAccountTwo().equals(accountIDOne))) {
//                                consultationList = csl;
//                            }
//                        } catch (NullPointerException e) {
//                            Log.d(TAG, "createSession", e);
//                        }
//                    }
//
//                    //If consultation is null
//                    //Then create new session, consultation
//                    if (consultationList == null) {
//                        mRef = FirebaseDatabase.getInstance().getReference("Session");
//                        sessionID = mRef.push().getKey();
//                        mRef.child(sessionID).setValue(new Session(sessionID, new Date(), new Date(), 1));
//                        //Create new consultation list
//                        mRef = FirebaseDatabase.getInstance().getReference("ConsultationList");
//                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                mRef.push().setValue(new ConsultationList(accountIDOne
//                                        , accountIDTwo, sessionID));
//                                //Update doctor session of prediction
//                                updateDoctorSessionInPrediction(mPrediction.getPredictionID(), sessionID);
//                                //Send session id
//                                Intent i = new Intent(PredictionResult.this, Chat.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                i.putExtra("receiverID", accountIDTwo);
//                                i.putExtra("sessionID", sessionID);
//                                startActivity(i);
//                                //Send message started
//                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(
//                                    "Message/" + sessionID);
//                                Message msg = new Message(reference.push().getKey(), accountIDTwo
//                                    , accountIDOne, getString(R.string.default_chatbot_hello)
//                                    , new Date(), sessionID, 1);
//                                reference.child(msg.getMessageID()).setValue(msg);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                    //If consultation is not null
//                    //Then find the last session,
//                    //if it equal to 1, then it is current session,
//                    //if not, then create new session and new consultation
//                    else {
//                        sessionID = "default";
//                        mRef = FirebaseDatabase.getInstance().getReference("Session");
//                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                Session ss = new Session();
//                                for (DataSnapshot sn : snapshot.getChildren()) {
//                                    ss = sn.getValue(Session.class);
//                                    //Get current session of two account
//                                    if (ss.getSessionID().equals(consultationList.getSessionID())) {
//                                        if (ss.getStatus() == 1) {
//                                            sessionID = ss.getSessionID();
//                                        }
//                                    }
//                                }
//                                //If session is null
//                                if (sessionID.equals("default")) {
//                                    sessionID = mRef.push().getKey();
//                                    mRef.child(sessionID).setValue(new Session(sessionID, new Date(), new Date(), 1));
//                                    //Create new consultation list
//                                    mRef = FirebaseDatabase.getInstance().getReference("ConsultationList");
//                                    mRef.push().setValue(new ConsultationList(accountIDOne
//                                            , accountIDTwo, sessionID));
//
//                                    //Send message started
//                                    DatabaseReference reference =
//                                        FirebaseDatabase.getInstance().getReference().child("Message/" + sessionID);
//                                    Message msg = new Message(reference.push().getKey(), accountIDTwo
//                                        , accountIDOne, getString(R.string.default_chatbot_hello)
//                                        , new Date(), sessionID, 1);
//                                    reference.child(msg.getMessageID()).setValue(msg);
//                                }
//                                //Update doctor session of prediction
//                                updateDoctorSessionInPrediction(mPrediction.getPredictionID(), sessionID);
//                                //Send session id
//                                Intent i = new Intent(PredictionResult.this, Chat.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                i.putExtra("receiverID", accountIDTwo);
//                                i.putExtra("sessionID", sessionID);
//                                startActivity(i);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d(TAG, "createSession()");
//        }
//    }

    /**
     * Create new chat session
     * accountIDOne is sender
     * accountIDTwo is receiver
     */
    private void createSessionWithCDoctor(String doctorID) {
        mRef = FirebaseDatabase.getInstance().getReference("Prediction").child(mPrediction.getPredictionID());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Prediction newPrediction = snapshot.getValue(Prediction.class);
                //Prediction has no session with doctor -> create new session and send welcome msg then open Chat activity
                if (newPrediction.getDoctorSessionID().equals("Default")) {
                    mRef = FirebaseDatabase.getInstance().getReference("Session");
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean isFound = false;

                            // Create new Session
                            mRef = FirebaseDatabase.getInstance().getReference("Session");
                            sessionID = mRef.push().getKey();
                            Session session = new Session(sessionID, new Date(), new Date(), 1);
                            session.setAccOneAndAccTwo(fUser.getUid(), doctorID);
                            mRef.child(sessionID).setValue(session);

                            //Send message started
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Message/" + sessionID);

                            Message msg = new Message(reference.push().getKey(), doctorID, getString(R.string.default_chatbot_hello)
                                    , new Date(), sessionID, 1);
                            reference.child(msg.getMessageID()).setValue(msg);
                            //Update doctor session of prediction
                            updateDoctorSessionInPrediction(newPrediction.getPredictionID(), sessionID);
                            // Start Chat activity
                            Intent i = new Intent(PredictionResult.this, Chat.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("receiverID", doctorID);
                            i.putExtra("sessionID", sessionID);
                            PredictionResult.this.startActivity(i);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    //Send session id
                    Intent i = new Intent(PredictionResult.this, Chat.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("receiverID", doctorID);
                    i.putExtra("sessionID", newPrediction.getDoctorSessionID());
                    PredictionResult.this.startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }


    private void updateDoctorSessionInPrediction(String predictionID, String doctorSession) {
        try {
            mRef = FirebaseDatabase.getInstance().getReference("Prediction");
            mRef.child(predictionID).child("doctorSessionID").setValue(doctorSession);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "updateDoctorSessionInPrediction()");
        }
    }
}