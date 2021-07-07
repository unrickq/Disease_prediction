package com.example.diseaseprediction.ui.prediction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.Login;
import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.adapter.PredictionAdapter;
import com.example.diseaseprediction.object.Prediction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PredictionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PredictionListFragment extends Fragment {
  private static final String TAG = "PredictionListFragment";

  FirebaseUser fUser;
  DatabaseReference mRef;

  private RecyclerView prediction_list_recycler_view_main;
  private List<Prediction> mPredictionList;
  private PredictionAdapter patientPredictionAdapter;
  private TextView prediction_list_txt_title;


  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public PredictionListFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment PredictionListFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static PredictionListFragment newInstance(String param1, String param2) {
    PredictionListFragment fragment = new PredictionListFragment();
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
    ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.menu_predictionList));
    ((MainActivity) getActivity()).setIconToolbar();

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    container.removeAllViews();
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_prediction_list, container, false);

    //Get current user
    fUser = FirebaseAuth.getInstance().getCurrentUser();

    prediction_list_txt_title = view.findViewById(R.id.prediction_list_txt_title);
    prediction_list_recycler_view_main = view.findViewById(R.id.prediction_list_recycler_view_main);
    prediction_list_recycler_view_main.setHasFixedSize(true);
    prediction_list_recycler_view_main.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

    //Load UI
    loadUIByType();

    return view;
  }

  /**
   * Load UI by account type
   */
  private void loadUIByType() {
    mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(fUser.getUid());
    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        try {
          if (snapshot.child("typeID").getValue().toString().equals("0")) {
            loadAllPredictionOfAccount(0);
          } else {
            loadAllPredictionOfAccount(1);
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

  /**
   * Load prediction by type account
   * Load list prediction of patient if account type is 1
   * Load list prediction that confirmed by account doctor if account type is 0
   *
   * @param typeAcc type of account. 0: doctor | 1: patient
   */
  private void loadAllPredictionOfAccount(int typeAcc) {
    mPredictionList = new ArrayList<>();
    Query predictionByDateUpdate = FirebaseDatabase.getInstance().getReference("Prediction").orderByChild(
        "dateUpdate/time");
    predictionByDateUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        mPredictionList.clear();
        for (DataSnapshot sh : snapshot.getChildren()) {
          Prediction pr = sh.getValue(Prediction.class);
          //If patientId equal with current accountID
          try {
            //Doctor type
            if (typeAcc == 0) {
              if (pr.getDoctorID().equals(fUser.getUid())) {
                mPredictionList.add(pr);
              }
            }
            //Patient type
            else if (typeAcc == 1) {
              if (pr.getPatientID().equals(fUser.getUid())) {
                mPredictionList.add(pr);
              }
            }
          } catch (NullPointerException e) {
            Log.d(TAG, "PredictionListFragment. ID null", e);
          }
        }
        //goToScreen 1: prediction result screen
        if (mPredictionList.size() > 0) {
          prediction_list_txt_title.setVisibility(View.GONE);
          //Reverse list index to get latest prediction
          Collections.reverse(mPredictionList);
          patientPredictionAdapter = new PredictionAdapter(getActivity().getApplicationContext(),
                  mPredictionList, 1, mPredictionList.size());
          prediction_list_recycler_view_main.setAdapter(patientPredictionAdapter);
        } else {
          prediction_list_txt_title.setVisibility(View.VISIBLE);
        }

      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }
}