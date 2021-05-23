package com.example.diseaseprediction.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.diseaseprediction.object.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AccountModel {
    private Account mAccount;
    private FirebaseUser mUser;
    private DatabaseReference mRef;
    private List<Account> mListAccount;

    public AccountModel() {

    }

    public List<Account> getAllAccount(){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        mListAccount = new ArrayList<>();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListAccount.clear();
                for (DataSnapshot mSnapshot: snapshot.getChildren()){
                    Account account = mSnapshot.getValue(Account.class);
                    assert account!=null;
                    if (!account.getAccountId().equals(mUser.getUid())){
                        mListAccount.add(account);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return mListAccount;
    }

    public Account getAccountById(String accountID){
        mAccount = new Account();
        mListAccount = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check user exist in firebase
                if (snapshot.hasChild(accountID)){
                    mAccount = snapshot.child(accountID).getValue(Account.class);
                    mAccount = getData(snapshot,accountID);
                }else{
                    System.out.println("k tim ra");
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        System.out.println("ben ngoai: "+ mAccount.getName());
        return mAccount;
    }


    public Account getData(DataSnapshot snapshot,String accountID){
        mAccount = snapshot.child(accountID).getValue(Account.class);
        System.out.println("lay gia tri "+mAccount.getName());
        return mAccount;
    }
}
