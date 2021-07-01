package com.example.diseaseprediction.ui.predictionListConfirm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.adapter.PredictionAdapter;
import com.example.diseaseprediction.object.DoctorInfo;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PredictionListConfirm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PredictionListConfirm extends Fragment {
    private static final String TAG = "PredictionListConfirm";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseUser fUser;
    DatabaseReference mRef;
    private RecyclerView prediction_list_confirm_recycler_view_main;
    private List<Prediction> mPredictionListDoctor;
    private PredictionAdapter doctorPredictionPendingListAdapter;
    private DoctorInfo mDoctor;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PredictionListConfirm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment predictionListConfirm.
     */
    // TODO: Rename and change types and number of parameters
    public static PredictionListConfirm newInstance(String param1, String param2) {
        PredictionListConfirm fragment = new PredictionListConfirm();
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
        //Set toolbar
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.home_doctor_all_prediction_txt_title));
        ((MainActivity) getActivity()).setIconToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prediction_list_confirm, container, false);

        //Get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        prediction_list_confirm_recycler_view_main = view.findViewById(R.id.prediction_list_confirm_recycler_view_main);
        prediction_list_confirm_recycler_view_main.setHasFixedSize(true);
        prediction_list_confirm_recycler_view_main.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        //Load UI
        loadAllPredictionPending();
        return view;
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
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                mPredictionListDoctor, 0, mPredictionListDoctor.size());
                        prediction_list_confirm_recycler_view_main.setAdapter(doctorPredictionPendingListAdapter);
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