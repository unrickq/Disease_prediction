package com.example.diseaseprediction.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.Chat;
import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.adapter.ConsultationAdapter;
import com.example.diseaseprediction.adapter.PredictionAdapter;
import com.example.diseaseprediction.object.ConsultationList;
import com.example.diseaseprediction.object.Message;
import com.example.diseaseprediction.object.Session;
import com.example.diseaseprediction.ui.about.AboutFragment;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

    private String  sessionID;
    private final String CHATBOT_ID ="1Gc2soWrtWa36H9i00G7elMsyNG3";
    private ConsultationList consultationList;
    private TextView home_txt_prediction_see_more, home_txt_consultation_see_more;
    private SearchView home_search_view;
    private NavigationView navigationView;
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
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        //set toolbar
        ((MainActivity) getActivity()).setActionBarTitle("");
        ((MainActivity) getActivity()).setIconToolbar();
        container.removeAllViews();
        //find view
        findView(view);

        home_search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSession();
            }
        });
        home_txt_prediction_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(2).setChecked(true);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new PredictionListFragment()).commit();
            }
        });
        home_txt_consultation_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(3).setChecked(true);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ConsultationListFragment()).commit();

            }
        });

        return view;
    }

    private void findView(View view) {
        navigationView = getActivity().findViewById(R.id.nav_view);
        home_txt_prediction_see_more = view.findViewById(R.id.home_txt_prediction_see_more);
        home_txt_consultation_see_more = view.findViewById(R.id.home_txt_consultation_see_more);
        home_search_view = view.findViewById(R.id.home_search_view);
    }

    private void createSession(){
        mRef = FirebaseDatabase.getInstance().getReference("ConsultationList");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ConsultationList csl = new ConsultationList();
                for (DataSnapshot sn : snapshot.getChildren()) {
                    csl = sn.getValue(ConsultationList.class);
                    if (csl.getmAccountID().equals(fUser.getUid())
                            && csl.getReceiverID().equals(CHATBOT_ID)) {
                        //Get consultation of mAccount and there account
                        consultationList = csl;
                    }
                }

                //If consultation list is null
                //Then create new session, consultation
                if (consultationList == null) {
                    mRef = FirebaseDatabase.getInstance().getReference("Session");
                    sessionID = mRef.push().getKey();
                    mRef.child(sessionID).setValue(new Session(sessionID, 1));
                    //Create new consultation list
                    mRef = FirebaseDatabase.getInstance().getReference("ConsultationList");
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mRef.push().setValue(new ConsultationList(fUser.getUid()
                                    , CHATBOT_ID, sessionID));
                            //Send session id
                            Intent i = new Intent(getActivity(), Chat.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("receiverID",CHATBOT_ID);
                            i.putExtra("sessionID",sessionID);
                            i.putExtra("isChatBot",true);
                            getContext().startActivity(i);

                            //Send message started
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Message");
                            Message msg = new Message(reference.push().getKey(), fUser.getUid()
                                    , CHATBOT_ID, "Hello all! Let's started!"
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
                                mRef.child(sessionID).setValue(new Session(sessionID, 1));
                                //Create new consultation list
                                mRef = FirebaseDatabase.getInstance().getReference("ConsultationList");
                                mRef.push().setValue(new ConsultationList(fUser.getUid()
                                        , CHATBOT_ID, sessionID));

                                //Send message started
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Message");
                                Message msg = new Message(reference.push().getKey(), fUser.getUid()
                                        , CHATBOT_ID, "Hello all! Let's started!"
                                        , new Date(), sessionID, 1);
                                reference.child(msg.getMessageID()).setValue(msg);
                            }
                            //Send session id
                            Intent i = new Intent(getActivity(), Chat.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("receiverID",CHATBOT_ID);
                            i.putExtra("sessionID",sessionID);
                            i.putExtra("isChatBot",true);
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
}