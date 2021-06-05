package com.example.diseaseprediction.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.Chat;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.ConsultationList;
import com.example.diseaseprediction.object.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultationAdapter extends RecyclerView.Adapter<ConsultationAdapter.ViewHolder>{
    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private Context mContext;
    private String latestMessage, latestTime;
    private List<ConsultationList> mConsultationList;

    public ConsultationAdapter(Context context, List<ConsultationList> mConsultationList) {
        //Get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        this.mContext = context;
        this.mConsultationList = mConsultationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_consultation,parent,false);
        return new ConsultationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ConsultationAdapter.ViewHolder holder, int position) {
        ConsultationList consultation = mConsultationList.get(position);
        //Get username depend on current account
        if(consultation.getAccountOne().equals(fUser.getUid())){
            getUserName(consultation.getAccountTwo(),holder.item_consultation_txt_name,holder.item_consultation_img_main);
        }else{
            getUserName(consultation.getAccountOne(),holder.item_consultation_txt_name,holder.item_consultation_img_main);
        }
        //Get messeage
        getLatestMessageAndTime(consultation.getSessionID(),holder.item_consultation_txt_message, holder.item_consultation_txt_time);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Chat.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //Open chat depend on current account
                if(consultation.getAccountOne().equals(fUser.getUid())){
                    i.putExtra("receiverID",consultation.getAccountTwo());
                }else{
                    i.putExtra("receiverID",consultation.getAccountOne());
                }
                i.putExtra("sessionID",consultation.getSessionID());
                i.putExtra("isChatBot",false);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mConsultationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_consultation_txt_name,item_consultation_txt_message,item_consultation_txt_time;
        public CircleImageView item_consultation_img_main;

        public ViewHolder(View view) {
            super(view);
            item_consultation_txt_name = view.findViewById(R.id.item_consultation_txt_name);
            item_consultation_txt_message = view.findViewById(R.id.item_consultation_txt_message);
            item_consultation_txt_time = view.findViewById(R.id.item_consultation_txt_time);
            item_consultation_img_main = view.findViewById(R.id.item_consultation_img_main);
        }
    }

    //Get user by user ID
    public void getUserName(String userId,TextView item_consultation_txt_name, CircleImageView item_consultation_img_main){
        mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(userId);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                item_consultation_txt_name.setText(snapshot.child("name").getValue().toString());
                Glide.with(mContext).load(snapshot.child("image").getValue().toString()).into(item_consultation_img_main);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Get message by session ID
    public void getLatestMessageAndTime(String sessionID, TextView item_consultation_txt_message, TextView item_consultation_txt_time){
        latestMessage = "";
        mRef = FirebaseDatabase.getInstance().getReference("Message");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sn : snapshot.getChildren()){
                    Message msg = sn.getValue(Message.class);
                    if (msg.getSessionID().equals(sessionID)){
                        latestMessage = msg.getMessage();
                        latestTime = msg.getDateSend().getHours() +":"+ msg.getDateSend().getMinutes();
                    }
                    item_consultation_txt_message.setText(latestMessage);
                    item_consultation_txt_time.setText(latestTime);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
