package com.example.diseaseprediction.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.adapter.ConsultationAdapter;
import com.example.diseaseprediction.adapter.PredictionAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    List<String> testData = new ArrayList<>();
    private RecyclerView recyclerView;
    private ConsultationAdapter consultationAdapter ;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        container.removeAllViews();
        //set toolbar
        ((MainActivity) getActivity()).setActionBarTitle("");
        ((MainActivity) getActivity()).setIconToolbar();
        container.removeAllViews();

        testData.add("TEST 1");
        testData.add("TEST 2");
        testData.add("TEST 3");


//        recyclerView = root.findViewById(R.id.recycler_view_consultation);
//        consultationAdapter = new ConsultationAdapter(testData);
//        recyclerView.setAdapter(consultationAdapter);



        return root;
    }

}