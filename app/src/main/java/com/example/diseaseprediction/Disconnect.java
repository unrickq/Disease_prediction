package com.example.diseaseprediction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;

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
            builder.setCancelable(true);
            alertDialog = builder.create();
            alertDialog.show();
        }

    }
    void startDialog_chat(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.disconnect_internet,null));
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
    }


    public void dismissDialog(){
        alertDialog.dismiss();
    }

    public void isConnectNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifiConnect != null && wifiConnect.isConnected()) || (mobileConnect != null && mobileConnect.isConnected()) ){

        }else{
            startDialog_main();
        }
    }
    public void isConnectNetwork_1(){
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){

        }

    }

}
