package com.example.diseaseprediction.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.Chat;
import com.example.diseaseprediction.Login;
import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.adapter.ConsultationAdapter;
import com.example.diseaseprediction.adapter.PredictionAdapter;
import com.example.diseaseprediction.object.ConsultationList;
import com.example.diseaseprediction.object.DoctorInfo;
import com.example.diseaseprediction.object.Message;
import com.example.diseaseprediction.object.Prediction;
import com.example.diseaseprediction.object.Session;
import com.example.diseaseprediction.ui.consultation.ConsultationListFragment;
import com.example.diseaseprediction.ui.prediction.PredictionListFragment;
import com.example.diseaseprediction.ui.predictionListConfirm.PredictionListConfirm;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private DatabaseReference mRef;
    private FirebaseUser fUser;
    private DoctorInfo mDoctor;

    private String sessionID;
    private final String CHATBOT_ID = "hmVF1lBCzlddOHl6qFeP0t76iMy1";
    private List<ConsultationList> consultationLists;
    private List<Prediction> mPredictionListDoctor;
    private List<Prediction> mPredictionListPatient;
    private ConsultationAdapter consultationAdapter;
    private PredictionAdapter doctorPredictionPendingListAdapter;
    private PredictionAdapter patientPredictionAdapter;

    private ConsultationList consultationList;
    private TextView home_txt_prediction_see_more, home_txt_consultation_see_more, home_doctor_all_prediction_txt_see_more,
            home_txt_title, home_txt_prediction_title, home_txt_consultation_title;
    private RelativeLayout home_doctor_all_prediction_layout_title;
    private SearchView home_search_view;
    private NavigationView navigationView;
    private RecyclerView home_recycler_view_consultation, home_recycler_view_disease, home_doctor_all_prediction_recycle_view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        //set toolbar
        ((MainActivity) getActivity()).setActionBarTitle("");
        ((MainActivity) getActivity()).setIconToolbar();
        container.removeAllViews();

        //find view
        findView(view);

        //Set UI by role
        setUIByAccountType();

        //Search clicked
        home_search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSession(fUser.getUid(), CHATBOT_ID);
            }
        });

        home_doctor_all_prediction_txt_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(2).setChecked(true);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new PredictionListConfirm()).commit();
            }
        });

        //See more prediction clicked
        home_txt_prediction_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(3).setChecked(true);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new PredictionListFragment()).commit();
            }
        });

        //See more consultation clicked
        home_txt_consultation_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(4).setChecked(true);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ConsultationListFragment()).commit();

            }
        });

        //Load list consultation to recycler
        loadConsultationList();

        return view;
    }

    /**
     * Find view by ID
     *
     * @param view view
     */
    private void findView(View view) {
        navigationView = getActivity().findViewById(R.id.nav_view);
        home_txt_prediction_see_more = view.findViewById(R.id.home_txt_prediction_see_more);
        home_txt_consultation_see_more = view.findViewById(R.id.home_txt_consultation_see_more);
        home_doctor_all_prediction_txt_see_more = view.findViewById(R.id.home_doctor_all_prediction_txt_see_more);
        home_search_view = view.findViewById(R.id.home_search_view);

        home_txt_title = view.findViewById(R.id.home_txt_title);
        home_txt_prediction_title = view.findViewById(R.id.home_txt_prediction_title);
        home_txt_consultation_title = view.findViewById(R.id.home_txt_consultation_title);

        //Create consultation recycle
        home_recycler_view_consultation = view.findViewById(R.id.home_recycler_view_consultation);
        home_recycler_view_consultation.setHasFixedSize(true);
        home_recycler_view_consultation.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        //Create disease recycle
        home_recycler_view_disease = view.findViewById(R.id.home_recycler_view_disease);
        home_recycler_view_disease.setHasFixedSize(true);
        home_recycler_view_disease.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        home_doctor_all_prediction_layout_title = view.findViewById(R.id.home_doctor_all_prediction_layout_title);
        home_doctor_all_prediction_recycle_view = view.findViewById(R.id.home_doctor_all_prediction_recycle_view);
    }

    /**
     * Create new chat session
     * accountIDOne is sender
     * accountIDTwo is receiver
     * CHATBOT_ID is receiver (account two in consultation)
     */
    private void createSession(String accountIDOne, String accountIDTwo) {
        //Get consultation list of two account
        mRef = FirebaseDatabase.getInstance().getReference("ConsultationList");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ConsultationList csl = new ConsultationList();
                for (DataSnapshot sn : snapshot.getChildren()) {
                    csl = sn.getValue(ConsultationList.class);
                    //Check to find consultation is exist or not
                    if ((csl.getAccountOne().equals(accountIDOne) && csl.getAccountTwo().equals(accountIDTwo))
                            || (csl.getAccountOne().equals(accountIDTwo) && csl.getAccountTwo().equals(accountIDOne))) {
                        consultationList = csl;
                    }
                }

                //If consultation is null
                //Then create new session, consultation
                if (consultationList == null) {
                    mRef = FirebaseDatabase.getInstance().getReference("Session");
                    sessionID = mRef.push().getKey();
                    mRef.child(sessionID).setValue(new Session(sessionID, new Date(), new Date(), 1));
                    //Create new consultation list
                    mRef = FirebaseDatabase.getInstance().getReference("ConsultationList");
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mRef.push().setValue(new ConsultationList(accountIDOne
                                    , accountIDTwo, sessionID));
                            //Send session id
                            Intent i = new Intent(getActivity(), Chat.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("receiverID", accountIDTwo);
                            i.putExtra("sessionID", sessionID);
//                            i.putExtra("isChatBot", true);
                            getContext().startActivity(i);

                            //Send message started
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Message");
                            Message msg = new Message(reference.push().getKey(), accountIDOne
                                    , accountIDTwo, "Hello all! Let's started!"
                                    , new Date(), sessionID, 1);
                            reference.child(msg.getMessageID()).setValue(msg);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                //If consultation is not null
                //Then find the last session,
                //if it equal to 1, then it is current session,
                //if not, then create new session and new consultation
                else {
                    sessionID = "default";
                    mRef = FirebaseDatabase.getInstance().getReference("Session");
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Session ss = new Session();
                            for (DataSnapshot sn : snapshot.getChildren()) {
                                ss = sn.getValue(Session.class);
                                //Get current session of two account
                                if (ss.getSessionID().equals(consultationList.getSessionID())) {
                                    if (ss.getStatus() == 1) {
                                        sessionID = ss.getSessionID();
                                    }
                                }
                            }
                            //If session is null
                            if (sessionID.equals("default")) {
                                sessionID = mRef.push().getKey();
                                mRef.child(sessionID).setValue(new Session(sessionID, new Date(), new Date(), 1));
                                //Create new consultation list
                                mRef = FirebaseDatabase.getInstance().getReference("ConsultationList");
                                mRef.push().setValue(new ConsultationList(accountIDOne
                                        , accountIDTwo, sessionID));

                                //Send message started
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Message");
                                Message msg = new Message(reference.push().getKey(), accountIDOne
                                        , accountIDTwo, "Hello all! Let's started!"
                                        , new Date(), sessionID, 1);
                                reference.child(msg.getMessageID()).setValue(msg);
                            }
                            //Send session id
                            Intent i = new Intent(getActivity(), Chat.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("receiverID", accountIDTwo);
                            i.putExtra("sessionID", sessionID);
//                            i.putExtra("isChatBot", true);
                            getContext().startActivity(i);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Load consultation of current account
     */
    private void loadConsultationList() {
        consultationLists = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference("ConsultationList");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                consultationLists.clear();
                for (DataSnapshot sh : snapshot.getChildren()) {
                    ConsultationList cls = sh.getValue(ConsultationList.class);
                    assert cls != null;
                    if (cls.getAccountOne().equals(fUser.getUid()) || cls.getAccountTwo().equals(fUser.getUid())) {
                        consultationLists.add(cls);
                    }
                }
                //Reverse list index to get latest consultation
                Collections.reverse(consultationLists);
                consultationAdapter = new ConsultationAdapter(getActivity().getApplicationContext(), consultationLists, 3);
                home_recycler_view_consultation.setAdapter(consultationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * DOCTOR TYPE
     * Load all prediction pending
     */
    private void loadAllPredictionPending() {
        mPredictionListDoctor = new ArrayList<>();
        //Find specialization id of doctor account
        mRef = FirebaseDatabase.getInstance().getReference("DoctorInfo").child(fUser.getUid());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDoctor = snapshot.getValue(DoctorInfo.class);
                //Go to prediction
                mRef = FirebaseDatabase.getInstance().getReference("Prediction");
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mPredictionListDoctor.clear();
                        for (DataSnapshot sh : snapshot.getChildren()) {
                            Prediction pr = sh.getValue(Prediction.class);
                            assert pr != null;
                            try {
                                if (pr.getStatus() == 0) {
                                    //Check if specializationID in prediction equal with specializationID in doctor account
                                    if (pr.getHiddenSpecializationID().equals(mDoctor.getSpecializationID())) {
                                        //Add to prediction list
                                        mPredictionListDoctor.add(pr);
                                    }
                                }
                            } catch (NullPointerException e) {
                                Log.d(TAG, "Home. Patient ID null", e);
                            }
                        }
                        //Reverse list index to get latest consultation
//                        Collections.reverse(mPredictionListDoctor);
                        //Create adapter
                        //goToScreen 0: doctor confirm screen
                        doctorPredictionPendingListAdapter = new PredictionAdapter(getActivity().getApplicationContext(),
                                mPredictionListDoctor, 0, 3);
                        home_doctor_all_prediction_recycle_view.setAdapter(doctorPredictionPendingListAdapter);
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

    /**
     * Load prediction by type account
     * Load list prediction of patient if account type is 1
     * Load list prediction that confirmed by account doctor if account type is 0
     *
     * @param typeAcc type of account. 0: doctor | 1: patient
     */
    private void loadAllPredictionOfAccount(int typeAcc) {
        mPredictionListPatient = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference("Prediction");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPredictionListPatient.clear();
                for (DataSnapshot sh : snapshot.getChildren()) {
                    Prediction pr = sh.getValue(Prediction.class);
                    //If patientId equal with current accountID
                    try {
                        if (typeAcc == 0) {
                            if (pr.getDoctorID().equals(fUser.getUid())) {
                                mPredictionListPatient.add(pr);
                            }
                        } else if (typeAcc == 1) {
                            if (pr.getPatientID().equals(fUser.getUid())) {
                                mPredictionListPatient.add(pr);
                            }
                        }
                    } catch (NullPointerException e) {
                        Log.d(TAG, "Home. Patient ID null", e);
                    }
                }
                //goToScreen 1: prediction result screen
                patientPredictionAdapter = new PredictionAdapter(getActivity().getApplicationContext(),
                        mPredictionListPatient, 1, 3);
                home_recycler_view_disease.setAdapter(patientPredictionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * UI of doctor role
     */
    private void setUIDoctor() {
        home_txt_title.setText(getString(R.string.home_doctor_txt_title));
        home_txt_prediction_title.setText(getString(R.string.home_doctor_txt_prediction_title));
        home_txt_consultation_title.setText(getString(R.string.home_doctor_txt_consultation_title));

        //All prediction in pending
        home_doctor_all_prediction_layout_title.setVisibility(View.VISIBLE);
        home_doctor_all_prediction_recycle_view.setVisibility(View.VISIBLE);

        home_doctor_all_prediction_recycle_view.setHasFixedSize(true);
        home_doctor_all_prediction_recycle_view.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        loadAllPredictionPending();
        //type 0: doctor
        loadAllPredictionOfAccount(0);
    }

    /**
     * UI of user role
     */
    private void setUIPatient() {
        //Set doctor visibility gone
        home_doctor_all_prediction_layout_title.setVisibility(View.GONE);
        home_doctor_all_prediction_recycle_view.setVisibility(View.GONE);

        //Patient
        home_txt_title.setText(getString(R.string.home_txt_title));
        home_txt_prediction_title.setText(getString(R.string.home_txt_prediction_title));
        home_txt_consultation_title.setText(getString(R.string.home_txt_consultation_title));
        //type 1: patient
        loadAllPredictionOfAccount(1);

    }

    /**
     * Set UI by type of account
     */
    private void setUIByAccountType() {
        //Get disease
        if (!fUser.getUid().equals("")) {
            mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(fUser.getUid());
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.child("typeID").getValue().toString().equals("0")) {
                            setUIDoctor();
                        } else {
                            setUIPatient();
                        }

                    } catch (NullPointerException e) {
                        Log.e(TAG, "setUIByAccountType: TypeID Null", e);
                        Toast.makeText(getContext(), getString(R.string.error_unknown_relogin), Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getContext(), Login.class);
                        startActivity(i);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}