package com.example.diseaseprediction;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Disconnect {
    Activity activity;
    AlertDialog alertDialog;

    public Disconnect(Activity activity) {
        this.activity = activity;
    }

    public void startDialog_main(){
        if (!this.activity.isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.disconnect_internet, null));
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void dismissDialog(){
        if(alertDialog != null){
            if(alertDialog.isShowing()){
                alertDialog.dismiss();
            }
        }

    }

    public void isInternetConnect(){
        keepLive();
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    dismissDialog();
                } else {
                    startDialog_main();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /**
     * Keep connect to firebase by using temp reference
     */
    public void keepLive() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference("keepLive");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

}
