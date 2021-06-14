package com.example.diseaseprediction;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.adapter.ChatAdapter;
import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH=10;

    private DatabaseReference mRef;
    private FirebaseUser fUser;
    private Intent intent;
    private PopupMenu pm;

    private ChatAdapter chatAdapter;
    private List<Message> mMessage;
    private String receiverID, sessionID;

    private RelativeLayout chat_send_message_layout;
    private RecyclerView chat_recycler_view;
    private TextView chat_toolbar_txt_name;
    private ImageView chat_toolbar_img_pre, chat_img_send, chat_toolbar_img_hamburger, chat_img_mic;
    private CircleImageView chat_toolbar_img_avatar;
    private EditText chat_txt_enter_mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Find view
        findView();

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        //Set toolbar chat
        setToolbarChat();

        //Set UI by role
        setUIByAccountType();

        //Set recycler view
        chat_recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        chat_recycler_view.setLayoutManager(linearLayoutManager);

        //Get receiver id
        intent = getIntent();
        receiverID = intent.getStringExtra("receiverID");
        sessionID = intent.getStringExtra("sessionID");

        //Check receiver and session
        //Then load all message
        if (!receiverID.equals(null) && !sessionID.equals(null)) {
            mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(receiverID);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Account receiver = snapshot.getValue(Account.class);
                    chat_toolbar_txt_name.setText(receiver.getName());
                    Glide.with(Chat.this).load(receiver.getImage()).into(chat_toolbar_img_avatar);
                    ReadMessage(fUser.getUid(), receiverID);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //Edit text input
            chat_txt_enter_mess.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                //Show icon mic or send depend on edit text
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (chat_txt_enter_mess.getText().toString().equals("")){
                        chat_img_send.setVisibility(View.GONE);
                        chat_img_mic.setVisibility(View.VISIBLE);
                    }else{
                        chat_img_send.setVisibility(View.VISIBLE);
                        chat_img_mic.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            //send message
            chat_img_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = chat_txt_enter_mess.getText().toString();
                    if (!msg.equals("")) {
                        Message message = new Message("", fUser.getUid(), receiverID, msg
                                , new Date(), sessionID, 1);
                        SendMessage(message);
                        chat_txt_enter_mess.getText().clear();
                    }
                }
            });

            //Input Voice
            chat_img_mic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSpeechInput();
                }
            });

            //Check is current session is end or not
            CheckSessionIsEndOrNot();
        } else {
            Toast toast = Toast.makeText(this, R.string.exception_chat_load_user, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private void findView() {
        chat_txt_enter_mess = findViewById(R.id.chat_txt_enter_mess);
        chat_img_send = findViewById(R.id.chat_img_send);
        chat_img_mic = findViewById(R.id.chat_img_mic);
        chat_recycler_view = findViewById(R.id.chat_recycler_view);
    }

    //Set toolbar
    private void setToolbarChat() {
        chat_toolbar_img_avatar = findViewById(R.id.chat_toolbar_img_avatar);
        chat_toolbar_txt_name = findViewById(R.id.chat_toolbar_txt_name);
        chat_toolbar_img_pre = findViewById(R.id.chat_toolbar_img_pre);
        chat_toolbar_img_hamburger = findViewById(R.id.chat_toolbar_img_hamburger);

        //Back button
        chat_toolbar_img_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Create popup menu
        pm = new PopupMenu(Chat.this, chat_toolbar_img_hamburger);
        pm.getMenuInflater().inflate(R.menu.chat_menu, pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.chat_menu_view_info:
                        System.out.println("chat_menu_view_info");
                        return true;
                    case R.id.chat_menu_end_session:
                        System.out.println("chat_menu_end_session");
                        endSession();
                        CheckSessionIsEndOrNot();
                        return true;
                }
                return true;
            }
        });

        //Show popup menu when clicked on img_hamburger
        chat_toolbar_img_hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set popup menu
                pm.show();
            }
        });
    }

    private void SendMessage(Message msg) {
        mRef = FirebaseDatabase.getInstance().getReference("Message");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msg.setMessageID(mRef.push().getKey());
                mRef.child(msg.getMessageID()).setValue(msg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void endSession() {
        mRef = FirebaseDatabase.getInstance().getReference("Session").child(sessionID);
        mRef.child("status").setValue(0);
    }

    private void CheckSessionIsEndOrNot() {
        mRef = FirebaseDatabase.getInstance().getReference("Session");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int status = Integer.valueOf(snapshot.child(sessionID).child("status").getValue().toString());
                if (status == 0) {
                    chat_send_message_layout = findViewById(R.id.chat_send_message_layout);
                    chat_send_message_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Read message and load it to recycler view
    private void ReadMessage(String mID, String uID) {
        mMessage = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference("Message");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMessage.clear();
                for (DataSnapshot sn : snapshot.getChildren()) {
                    Message msg = sn.getValue(Message.class);
                    if (msg.getReceiverID().equals(mID) && msg.getSenderID().equals(uID)
                            && msg.getSessionID().equals(sessionID)
                            || msg.getReceiverID().equals(uID)
                            && msg.getSenderID().equals(mID)
                            && msg.getSessionID().equals(sessionID)) {
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

    private void setUIByAccountType() {
        mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(fUser.getUid());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("typeID").getValue().toString().equals("0")) {
                    Menu m = pm.getMenu();
                    m.getItem(0).setTitle(getString(R.string.chat_menu_doctor_view_info));
                } else {
                    Menu m = pm.getMenu();
                    m.getItem(1).setVisible(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Get text by speech
    public void getSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SPEECH);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    //Receive text when speech
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    chat_txt_enter_mess.setText(result.get(0));
                }
                break;
        }
    }


}