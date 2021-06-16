package com.example.diseaseprediction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>
{
    private FirebaseUser fUser;

    private Context mContext;
    private List<Message> mMessage;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private String imgURL;


    public ChatAdapter(@NonNull Context context, List<Message> mMessage) {
        this.mContext = context;
        this.mMessage = mMessage;
        this.imgURL = imgURL;
    }

    @NotNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Get view type. Right layout if sender, left layout if receiver
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_right, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_left, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        //Get message
        Message msg = mMessage.get(position);
        holder.item_chat_show_message.setText(msg.getMessage());

        //Custom date under message
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String shortTimeStr = sdf.format(msg.getDateSend().getTime());
        holder.item_chat_time.setText(shortTimeStr);

        //On clicked in message then show time send
        holder.item_chat_show_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.item_chat_time.getVisibility() == View.VISIBLE){
                    holder.item_chat_time.setVisibility(View.GONE);
                }else{
                    holder.item_chat_time.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView item_chat_show_message,item_chat_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Find view
            item_chat_show_message = itemView.findViewById(R.id.item_chat_show_message);
            item_chat_time = itemView.findViewById(R.id.item_chat_time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //Get type depend on user
        //Right if sender, Left if receiver
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mMessage.get(position).getSenderID().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
