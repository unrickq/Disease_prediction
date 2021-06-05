package com.example.diseaseprediction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.diseaseprediction.adapter.testAdapter;
import com.example.diseaseprediction.object.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class test extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference myRef;

    private RecyclerView recyclerView;
    private testAdapter userAdapter;
    private List<Account> mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mUser = new ArrayList<>();

        ReadUsers();
    }

    private void ReadUsers(){
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        myRef = FirebaseDatabase.getInstance().getReference("Accounts");
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mUser.clear();
//                int cout = 0;
//                for (DataSnapshot sh: snapshot.getChildren()){
//                    Account users = sh.getValue(Account.class);
//
//                    assert users!=null;
//                    if (!users.getAccountId().equals(firebaseUser.getUid())){
//                        mUser.add(users);
//                    }
//
//                    userAdapter = new testAdapter(getApplicationContext(),mUser);
//                    recyclerView.setAdapter(userAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}