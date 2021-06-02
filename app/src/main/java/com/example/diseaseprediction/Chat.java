package com.example.diseaseprediction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.adapter.ChatAdapter;
import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.Message;
import com.example.diseaseprediction.object.Session;
import com.example.diseaseprediction.ui.account.AccountFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;
    private Intent intent;

    private ChatAdapter chatAdapter;
    private List<Message> mMessage;

    private Session currentSession ;

    private RecyclerView chat_recycler_view;
    private String receiverID,sessionID;
    private TextView chat_toolbar_txt_name;
    private ImageView chat_toolbar_img_pre,chat_img_send,chat_toolbar_img_hamburger;
    private CircleImageView chat_toolbar_img_avatar;
    private EditText chat_txt_enter_mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Set toolbar chat
        setToolbarChat();
        //Find view
        findView();
        //Set recycler view
        chat_recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        chat_recycler_view.setLayoutManager(linearLayoutManager);

        //Get receiver id
        intent = getIntent();
        receiverID =  intent.getStringExtra("receiverID");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Accounts").child(receiverID);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Account receiver =snapshot.getValue(Account.class);
                chat_toolbar_txt_name.setText(receiver.getName());
                Glide.with(Chat.this).load(receiver.getImage()).into(chat_toolbar_img_avatar);
                ReadMessage(firebaseUser.getUid(), receiverID);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chat_img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = chat_txt_enter_mess.getText().toString();
                if (!msg.equals("")){
                    Message message = new Message("", firebaseUser.getUid(), receiverID, msg
                            , new Date(), "", 1);
                    SendMessage(message);
                    chat_txt_enter_mess.getText().clear();
                }
            }
        });
    }

    private void findView(){
        chat_txt_enter_mess = findViewById(R.id.chat_txt_enter_mess);
        chat_img_send = findViewById(R.id.chat_img_send);
        chat_recycler_view = findViewById(R.id.chat_recycler_view);
    }

    //Set toolbar
    private void setToolbarChat(){
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.chat_toolbar);
        View view = getSupportActionBar().getCustomView();
        chat_toolbar_img_avatar = view.findViewById(R.id.chat_toolbar_img_avatar);
        chat_toolbar_txt_name = view.findViewById(R.id.chat_toolbar_txt_name);
        chat_toolbar_img_pre = view.findViewById(R.id.chat_toolbar_img_pre);
        chat_toolbar_img_hamburger = view.findViewById(R.id.chat_toolbar_img_hamburger);
        chat_toolbar_img_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Set popup menu
        PopupMenu pm = new PopupMenu(this, chat_toolbar_img_hamburger);
        pm.getMenuInflater().inflate(R.menu.chat_menu, pm.getMenu());
//        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                switch (menuItem.getItemId()){
//                    case R.id.chat_menu_view_info:
//                        System.out.println("chat_menu_view_info");
//                        return true;
//                    case R.id.chat_menu_end_session:
//                        System.out.println("chat_menu_end_session");
//                        return true;
//                }
//                return true;
//            }
//        });
        pm.show();
    }

    private void SendMessage(Message msg){
        sessionID = "default";
        myRef = FirebaseDatabase.getInstance().getReference("Session");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Session ss = new Session();
                for (DataSnapshot sn : snapshot.getChildren()){
                    ss = sn.getValue(Session.class);
                    if (ss.getStatus()==1){
                        sessionID = ss.getSessionID();
                    }
                }
                if (sessionID.equals("default")){
                    sessionID = myRef.push().getKey();
                    myRef.child(sessionID).setValue(new Session(sessionID,1));
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Message");
                //Set message id, and session id
                msg.setMessageID(reference.push().getKey());
                msg.setSessionID(sessionID);
                reference.child(msg.getMessageID()).setValue(msg);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Read message and load it to recycler view
    private void ReadMessage(String mID, String uID){
        mMessage = new ArrayList<>();
        myRef = FirebaseDatabase.getInstance().getReference("Message");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMessage.clear();
                for (DataSnapshot sn : snapshot.getChildren()){
                    Message msg = sn.getValue(Message.class);
                    if (msg.getReceiverID().equals(mID) && msg.getSenderID().equals(uID)||msg.getReceiverID().equals(uID)
                            && msg.getSenderID().equals(mID)){
                        mMessage.add(msg);
                    }
                    chatAdapter = new ChatAdapter(Chat.this, mMessage);
                    chat_recycler_view.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}