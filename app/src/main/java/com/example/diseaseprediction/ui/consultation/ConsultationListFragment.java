package com.example.diseaseprediction.ui.consultation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.adapter.ConsultationAdapter;
import com.example.diseaseprediction.object.ConsultationList;
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
 * Use the {@link ConsultationListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultationListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseUser fUser;
    DatabaseReference mRef;

    private RecyclerView consultation_list_recycler_view_main;
    private ConsultationAdapter consultationAdapter;
    private List<ConsultationList> consultationLists;

    public ConsultationListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsultationListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultationListFragment newInstance(String param1, String param2) {
        ConsultationListFragment fragment = new ConsultationListFragment();
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
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.menu_consultationList));
        ((MainActivity) getActivity()).setIconToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Remove all view current in activity
        container.removeAllViews();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consultation_list, container, false);

        //Create recycler
        consultation_list_recycler_view_main = view.findViewById(R.id.consultation_list_recycler_view_main);
        consultation_list_recycler_view_main.setHasFixedSize(true);
        consultation_list_recycler_view_main.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        consultationLists = new ArrayList<>();
        //Load user to recycler
        ReadUsers();

        return view;
    }

    private void ReadUsers(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("ConsultationList");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sh: snapshot.getChildren()){
                    ConsultationList cls = sh.getValue(ConsultationList.class);
                    assert cls!=null;
                    if (cls.getAccountOne().equals(firebaseUser.getUid())||cls.getAccountTwo().equals(firebaseUser.getUid())){
                        consultationLists.add(cls);
                    }
                    consultationAdapter = new ConsultationAdapter(getActivity().getApplicationContext(),consultationLists);
                    consultation_list_recycler_view_main.setAdapter(consultationAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}