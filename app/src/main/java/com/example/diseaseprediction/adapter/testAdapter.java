package com.example.diseaseprediction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.ConsultationList;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class testAdapter extends RecyclerView.Adapter<testAdapter.ViewHolder>{
    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private Context context;
    private List<Account> mUser;

    private Context mContext;
    private String latestMessage;
    private List<ConsultationList> mconsultationList;

    public testAdapter(@NonNull Context context, List<ConsultationList> consultationList) {
        this.context = context;
        this.mconsultationList = consultationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_consultation,parent,false);

        return new testAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Account users = mUser.get(position);
//        holder.userName.setText(users.getName());
//
//        holder.img.setImageResource(R.mipmap.ic_launcher);
//
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, Chat.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.putExtra("receiverID",users.getAccountId());
//                context.startActivity(i);
//            }
//        });
        ConsultationList consultation = mconsultationList.get(position);
        getUserName(consultation.getAccountOne(),holder.item_consultation_txt_name,holder.item_consultation_img_main);
//        getLatestMessageAndTime(consultation.getAccountTwo(),holder.item_consultation_txt_message, holder.item_consultation_txt_time);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mconsultationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView userName;
//        public ImageView img;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.textView2);
//            img = itemView.findViewById(R.id.imageView);
//
//        }
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

    public void getUserName(String userId,TextView item_consultation_txt_name, CircleImageView item_consultation_img_main){
        mRef = FirebaseDatabase.getInstance().getReference("Account").child(userId);
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

//    public void getLatestMessageAndTime(String uID, TextView item_consultation_txt_message, TextView item_consultation_txt_time){
//        latestMessage = "";
//        fUser = FirebaseAuth.getInstance().getCurrentUser();
//        mRef = FirebaseDatabase.getInstance().getReference("Message");
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot sn : snapshot.getChildren()){
//                    Message msg = sn.getValue(Message.class);
//                    if (msg.getReceiverID().equals(fUser) && msg.getSenderID().equals(uID)
//                            ||msg.getReceiverID().equals(uID)
//                            && msg.getSenderID().equals(fUser)){
//                        latestMessage = msg.getMessage();
//                        //latestTime = msg.getDateSend().toString();
//                    }
//                    item_consultation_txt_message.setText(latestMessage);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}
