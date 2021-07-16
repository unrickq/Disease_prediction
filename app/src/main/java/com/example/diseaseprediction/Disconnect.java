package com.example.diseaseprediction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

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

}
