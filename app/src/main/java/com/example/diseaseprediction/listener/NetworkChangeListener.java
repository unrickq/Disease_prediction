package com.example.diseaseprediction.listener;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.example.diseaseprediction.Common;
import com.example.diseaseprediction.R;

public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!Common.isConnectNetwork(context)){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_disconnect = LayoutInflater.from(context).inflate(R.layout.disconnect_internet,null);
            AlertDialog alertDialog = builder.create();
            builder.setCancelable(true);
            alertDialog.show();
            alertDialog.getWindow().setGravity(Gravity.CENTER);
        }
    }
}
