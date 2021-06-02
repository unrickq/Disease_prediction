package com.example.diseaseprediction.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.Message;
import com.example.diseaseprediction.ui.account.AccountFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultationAdapter extends RecyclerView.Adapter<ConsultationAdapter.ViewHolder>{

    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private Context mContext;
    private List<Account> mUser;
    private String latestMessage;
    private String latestTime;

    public ConsultationAdapter(Context context, List<Account> mUser) {
        this.mContext = context;
        this.mUser = mUser;
    }

    /**
     * Required method for creating the viewholder objects.
     *
     * @param parent   The ViewGroup into which the new View will be added
     *                 after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly created ViewHolder.
     */
    @Override
    public ConsultationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home, parent, false);
        return new ConsultationAdapter.ViewHolder(view);
    }

    /**
     * Required method that binds the data to the viewholder.
     *
     * @param holder   The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(final ConsultationAdapter.ViewHolder holder, int position) {
        Account users = mUser.get(position);
        holder.item_consultation_txt_name.setText(users.getName());
        //getLatestMessageAndTime(users.getAccountId(),holder.item_consultation_txt_message,holder.item_consultation_txt_time);
        Glide.with(mContext).load(users.getImage()).into(holder.item_consultation_img_main);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(context, chat.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.putExtra("userid",users.getId());
//                context.startActivity(i);
            }
        });
    }

    /**
     * Required method for determining the size of the data set.
     *
     * @return Size of the data set.
     */
    @Override
    public int getItemCount() {
        return mUser.size();
    }

    /**
     * ViewHolder class that represents each row of data in the RecyclerView.
     */
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

    public void getLatestMessageAndTime(String uID, TextView item_consultation_txt_message, TextView item_consultation_txt_time){
        latestMessage = "";
        latestTime = "";
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Message");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sn : snapshot.getChildren()){
                    Message msg = sn.getValue(Message.class);
                    if (msg.getReceiverID().equals(fUser) && msg.getSenderID().equals(uID)
                            ||msg.getReceiverID().equals(uID)
                            && msg.getSenderID().equals(fUser)){
                        latestMessage = msg.getMessage();
                        //latestTime = msg.getDateSend().toString();
                    }
                    item_consultation_txt_message.setText(latestMessage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
