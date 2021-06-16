package com.example.diseaseprediction.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.Chat;
import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.adapter.ConsultationAdapter;
import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.ConsultationList;
import com.example.diseaseprediction.object.Message;
import com.example.diseaseprediction.object.Session;
import com.example.diseaseprediction.ui.consultation.ConsultationListFragment;
import com.example.diseaseprediction.ui.prediction.PredictionListFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private String sessionID;
    private final String CHATBOT_ID = "GFoBDpKPUealixZAOQvfoJhiiSE2";
    private List<ConsultationList> consultationLists;
    private ConsultationAdapter consultationAdapter;

    private ConsultationList consultationList;
    private TextView home_txt_prediction_see_more, home_txt_consultation_see_more,
            home_txt_title, home_doctor_txt_title, home_txt_prediction_title, home_doctor_txt_prediction_title,
            home_txt_consultation_title, home_doctor_txt_consultation_title;
    private SearchView home_search_view;
    private NavigationView navigationView;
    private RecyclerView home_recycler_view_consultation;
    private RecyclerView consultation_list_recycler_view_main;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                createSession(fUser.getUid(),CHATBOT_ID);
            }
        });

        //See more prediction clicked
        home_txt_prediction_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(2).setChecked(true);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new PredictionListFragment()).commit();
            }
        });

        //See more consultation clicked
        home_txt_consultation_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(3).setChecked(true);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ConsultationListFragment()).commit();

            }
        });

        //Create recycler
        home_recycler_view_consultation = view.findViewById(R.id.home_recycler_view_consultation);
        home_recycler_view_consultation.setHasFixedSize(true);
        home_recycler_view_consultation.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        consultationLists = new ArrayList<>();
        //Load list consultation to recycler
        loadConsultationList();

        return view;
    }

    /**
     * Find view by ID
     * @param view view
     */
    private void findView(View view) {
        navigationView = getActivity().findViewById(R.id.nav_view);
        home_txt_prediction_see_more = view.findViewById(R.id.home_txt_prediction_see_more);
        home_txt_consultation_see_more = view.findViewById(R.id.home_txt_consultation_see_more);
        home_search_view = view.findViewById(R.id.home_search_view);

        home_txt_title = view.findViewById(R.id.home_txt_title);
        home_doctor_txt_title = view.findViewById(R.id.home_doctor_txt_title);
        home_txt_prediction_title = view.findViewById(R.id.home_txt_prediction_title);
        home_doctor_txt_prediction_title = view.findViewById(R.id.home_doctor_txt_prediction_title);
        home_txt_consultation_title = view.findViewById(R.id.home_txt_consultation_title);
        home_doctor_txt_consultation_title = view.findViewById(R.id.home_doctor_txt_consultation_title);
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
//                    if (csl.getAccountOne().equals(accountIDOne)
//                            && csl.getAccountTwo().equals(accountIDTwo) || csl.getAccountOne().equals(accountIDTwo)
//                            && csl.getAccountTwo().equals(accountIDOne)) {
//                        consultationList = csl;
//                    }

                    //Check to find consultation is exist or not
                    if (csl.getAccountTwo().equals(accountIDTwo) || csl.getAccountOne().equals(accountIDTwo)) {
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
                            i.putExtra("isChatBot", true);
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
                                if (ss.getSessionID().equals(consultationList.getSessionID())) {
                                    if (ss.getStatus() == 1) {
                                        sessionID = ss.getSessionID();
                                    }
                                }
                            }
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
                            i.putExtra("isChatBot", true);
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
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("ConsultationList");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                consultationLists.clear();
                for (DataSnapshot sh : snapshot.getChildren()) {
                    ConsultationList cls = sh.getValue(ConsultationList.class);
                    assert cls != null;
                    if (cls.getAccountOne().equals(firebaseUser.getUid()) || cls.getAccountTwo().equals(firebaseUser.getUid())) {
                        consultationLists.add(cls);
                        Collections.reverse(consultationLists);
                    }
                    consultationAdapter = new ConsultationAdapter(getActivity().getApplicationContext(), consultationLists);
                    home_recycler_view_consultation.setAdapter(consultationAdapter);
                }
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
        home_txt_title.setVisibility(View.GONE);
        home_txt_prediction_title.setVisibility(View.GONE);
        home_txt_consultation_title.setVisibility(View.GONE);

        home_doctor_txt_title.setVisibility(View.VISIBLE);
        home_doctor_txt_prediction_title.setVisibility(View.VISIBLE);
        home_doctor_txt_consultation_title.setVisibility(View.VISIBLE);
    }

    /**
     * UI of user role
     */
    private void setUIPatient() {
        home_doctor_txt_title.setVisibility(View.GONE);
        home_doctor_txt_prediction_title.setVisibility(View.GONE);
        home_doctor_txt_consultation_title.setVisibility(View.GONE);

        home_txt_title.setVisibility(View.VISIBLE);
        home_txt_prediction_title.setVisibility(View.VISIBLE);
        home_txt_consultation_title.setVisibility(View.VISIBLE);
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
                    if (snapshot.child("typeID").getValue().toString().equals("0")) {
                        setUIDoctor();
                    } else {
                        setUIPatient();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}