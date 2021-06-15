package com.example.diseaseprediction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.core.content.ContextCompat;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

  private static final String LOG_TAG = "Chat";
  private static final int REQUEST_CODE_SPEECH = 10;

  private DatabaseReference mRef;
  private FirebaseUser fUser;
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

  private SpeechRecognizer speechRecognizer;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    //Find view
    getViews();

    // Initialize
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
    Intent intent = getIntent();
    receiverID = intent.getStringExtra("receiverID");
    sessionID = intent.getStringExtra("sessionID");

    //Check receiver and session
    //Then load all message
    if (receiverID != null && sessionID != null) {
      getUserChatData();
      //Edit text input
      chat_txt_enter_mess.addTextChangedListener(updateIconOnWriteWatcher());

      //send message
      chat_img_send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          String msg = chat_txt_enter_mess.getText().toString();
          if (!msg.equals("")) {
            Message message = new Message("", fUser.getUid(), receiverID, msg
                , new Date(), sessionID, 1);
            setMessage(message);
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
      checkSessionStatus();
    } else {
      Toast toast = Toast.makeText(this, R.string.exception_chat_load_user, Toast.LENGTH_SHORT);
      toast.show();
    }

  }

  /**
   * Get all views in layout
   */
  private void getViews() {
    chat_txt_enter_mess = findViewById(R.id.chat_txt_enter_mess);
    chat_img_send = findViewById(R.id.chat_img_send);
    chat_img_mic = findViewById(R.id.chat_img_mic);
    chat_recycler_view = findViewById(R.id.chat_recycler_view);
  }

  /**
   * Set up chat toolbar
   */
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
            checkSessionStatus();
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

  /**
   * Get user name, avatar and chat messages of user
   */
  public void getUserChatData() {
    mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(receiverID);
    mRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        Account receiver = snapshot.getValue(Account.class);
        if (receiver != null) {
          chat_toolbar_txt_name.setText(receiver.getName());
          Glide.with(Chat.this).load(receiver.getImage()).into(chat_toolbar_img_avatar);

          GetUserMessages(fUser.getUid(), receiverID);
        } else {
          Log.d(LOG_TAG, "Cannot get account info");
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  /**
   * Create a text watcher to update chat icon on typing
   *
   * @return a {@link TextWatcher} object
   */
  private TextWatcher updateIconOnWriteWatcher() {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      //Show icon mic or send depend on edit text
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // If input empty
        if (chat_txt_enter_mess.getText().toString().equals("")) {
          chat_img_send.setVisibility(View.GONE);
          chat_img_mic.setVisibility(View.VISIBLE);
        } else {
          chat_img_send.setVisibility(View.VISIBLE);
          chat_img_mic.setVisibility(View.GONE);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    };
  }


  /**
   * Set new message to Firebase
   *
   * @param msg massage to set
   */
  private void setMessage(Message msg) {
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

  /**
   * Check if the chat session has ended
   */
  private void checkSessionStatus() {
    mRef = FirebaseDatabase.getInstance().getReference("Session");
    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        String sessionStatus = (String) snapshot.child(sessionID).child("status").getValue();
        if (sessionStatus != null) {
          int status = Integer.parseInt(sessionStatus);
          if (status == 0) {
            chat_send_message_layout = findViewById(R.id.chat_send_message_layout);
            chat_send_message_layout.setVisibility(View.GONE);
          }
        } else {
          Log.d(LOG_TAG, "Status is null!");
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
      }
    });
  }

  /**
   * Get chat messages of current user with another user from Firebase and load it to recycler view
   *
   * @param currentUserID ID of active user
   * @param receiverID    ID of receiver user
   */
  private void GetUserMessages(String currentUserID, String receiverID) {
    mMessage = new ArrayList<>();
    mRef = FirebaseDatabase.getInstance().getReference("Message");
    mRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot sn : snapshot.getChildren()) {
          Message msg = sn.getValue(Message.class);

          if (msg != null && msg.getReceiverID() != null) {
            if (msg.getReceiverID().equals(currentUserID) && msg.getSenderID().equals(receiverID)
                && msg.getSessionID().equals(sessionID) || msg.getReceiverID().equals(receiverID)
                && msg.getSenderID().equals(currentUserID)
                && msg.getSessionID().equals(sessionID)) {
              mMessage.add(msg);
            }
            chatAdapter = new ChatAdapter(Chat.this, mMessage);
            chat_recycler_view.setAdapter(chatAdapter);
          }
        }
        mMessage.clear();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  /**
   * Set up element base on Account Type
   */
  private void setUIByAccountType() {
    mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(fUser.getUid());
    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        String accountType = (String) snapshot.child("typeID").getValue();
        if (accountType != null) {
          if (accountType.equals("0")) {
            Menu m = pm.getMenu();
            m.getItem(0).setTitle(getString(R.string.chat_menu_doctor_view_info));
          } else {
            Menu m = pm.getMenu();
            m.getItem(1).setVisible(false);
          }
        } else {
          Log.d(LOG_TAG, "Account type is null");
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  /**
   * Call default Speech-to-text handler of the phone. Permission checking is included
   */
  public void getSpeechInput() {
    // Check microphone access permission
    if (ContextCompat.checkSelfPermission(
        this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
      // Call Voice input
      Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
      intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
      intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi");

      startActivityForResult(intent, REQUEST_CODE_SPEECH);
    } else {
      // ask for the permission.
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(
            new String[]{Manifest.permission.RECORD_AUDIO},
            REQUEST_CODE_SPEECH);
      }
    }

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // Receive speech-to-text result
    if (requestCode == REQUEST_CODE_SPEECH) {
      if (resultCode == RESULT_OK && data != null) {
        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        chat_txt_enter_mess.setText(result.get(0));
      }
    }
  }

}