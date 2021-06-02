package com.example.diseaseprediction.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.Chat;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.Account;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class testAdapter extends RecyclerView.Adapter<testAdapter.ViewHolder>{

    private Context context;
    private List<Account> mUser;

    public testAdapter(@NonNull Context context, List<Account> mUser) {
        this.context = context;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_test,parent,false);

        return new testAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account users = mUser.get(position);
        holder.userName.setText(users.getName());

        holder.img.setImageResource(R.mipmap.ic_launcher);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Chat.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("receiverID",users.getAccountId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.textView2);
            img = itemView.findViewById(R.id.imageView);

        }
    }
}
