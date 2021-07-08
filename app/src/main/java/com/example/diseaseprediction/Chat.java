package com.example.diseaseprediction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.adapter.ChatAdapter;
import com.example.diseaseprediction.listener.MyClickListener;
import com.example.diseaseprediction.model.Result;
import com.example.diseaseprediction.model.TextClassificationClient;
import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.Disease;
import com.example.diseaseprediction.object.Message;
import com.example.diseaseprediction.object.Prediction;
import com.example.diseaseprediction.object.RecommendSymptom;
import com.example.diseaseprediction.object.Session;
import com.example.diseaseprediction.object.Symptom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    private static final String LOG_TAG = "Chat Activity";
    private static final int REQUEST_CODE_SPEECH = 10;

    private DatabaseReference mRef;
    private DatabaseReference mRef2;
    private FirebaseUser fUser;
    private PopupMenu pm;

    private ChatAdapter chatAdapter;
    private List<Message> mMessage;
    private List<Symptom> mSymptom;
    private String receiverID, sessionID;

    private RelativeLayout chat_send_message_layout;
    private RecyclerView chat_recycler_view;
    private TextView chat_toolbar_txt_name;
    private ImageView chat_toolbar_img_pre, chat_img_send, chat_toolbar_img_hamburger, chat_img_mic;
    private CircleImageView chat_toolbar_img_avatar;
    private EditText chat_txt_enter_mess;
    private String allMess = "";
    private TextClassificationClient client;
    private Handler handler;
    private boolean checkClickPredict = false;
    private boolean checkStartMessage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        client = new TextClassificationClient(getApplicationContext());

        handler = new Handler();

        //Find view
        getViews();

        // Initialize
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        //Set toolbar chat
        setToolbarChat();

        //Set UI by role
        setUIByAccountType();

        // get symptom
        getSymptomFirebase();

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
            //Set icon mic visibility when empty edit text
            chat_txt_enter_mess.addTextChangedListener(updateIconOnWriteWatcher());

            //send message
            chat_img_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = chat_txt_enter_mess.getText().toString();
                    try {
                        if (!receiverID.equals(Constants.CHATBOT_ID)) {
                            chatWithDoctor(msg);
                        } else {
                            nextChat(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "Exception when talking with chatbot ");
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

    @Override
    public void onBackPressed() {
        if (receiverID.equals(Constants.CHATBOT_ID)) {
            System.out.println("session la" + sessionID);
            mRef = FirebaseDatabase.getInstance().getReference("Session").child(sessionID);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Session ss = snapshot.getValue(Session.class);
                    if (ss.getStatus() != 0) {
                        dialogConfirm(sessionID);
                    } else {
                        Chat.super.onBackPressed();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Hide keyboard
     *
     * @param activity Current activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isAcceptingText()) {
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(),
                        0
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "hideSoftKeyboard()");
        }
    }

    /**
     * Get all views in layout
     */
    private void getViews() {
        try {
            chat_txt_enter_mess = findViewById(R.id.chat_txt_enter_mess);
            chat_img_send = findViewById(R.id.chat_img_send);
            chat_img_mic = findViewById(R.id.chat_img_mic);
            chat_recycler_view = findViewById(R.id.chat_recycler_view);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "getViews()");
        }
    }

    /**
     * Chat with chat bot
     * Then create new prediction
     *
     * @param msg message
     */
    private void chatWithChatbot(String msg) {
        if (!msg.equals("")) {
            // User request
            //user chat vs user
            if (receiverID.equals(Constants.CHATBOT_ID)) {
                try {
                    // user chat
                    Message message = new Message("", fUser.getUid(), Constants.CHATBOT_ID
                            , getString(R.string.chatbox_button_predict), new Date(), sessionID, 1);
                    setMessageFirebase(message);
                    //chatbot chat
                    String token = client.tokenize1(msg);
                    List<Result> results = client.classify(token);
                    //get symptom user input
                    String result = getString(R.string.default_chatbot_symptom);
                    List<String> tokenList = Arrays.asList(token.split(" "));
                    //search binary symptom
                    int mid = mSymptom.size()/2;
                    for (String tk : tokenList) {
                        tk = tk.replace("_", " ");
                        boolean check = false;
                        if(mSymptom.get(mid).getName().equals(tk)){
                            result += tk + ", ";
                        }else{
                            for (int i = 0; i < mid; i++) {
                                if(mSymptom.get(i).getName().equals(tk)){
                                    result += tk + ", ";
                                    check = true;
                                }
                            }
                            if(check == false){
                                for (int i = mid + 1; i < mSymptom.size(); i++) {
                                    if(mSymptom.get(i).getName().equals(tk)){
                                        result += tk + ", ";
                                    }
                                }
                            }
                        }
                    }
                    //print symptom user input
                    result = result.substring(0, result.length() - 2);
                    Message mess = new Message("", Constants.CHATBOT_ID, fUser.getUid(),
                            result, new Date(), sessionID, 1);
                    setMessageFirebase(mess);
                    //get disease from model
                    String temp = getString(R.string.default_chatbot_disease_list);
                    for (int i = 0; i < 4; i++) {
                        if(i==3){
                            temp+= results.get(i).getTitle() +" "+String.format("%.2f", results.get(i).getConfidence()* 100) + "%";
                        }else{
                            temp+= results.get(i).getTitle() +" "+String.format("%.2f", results.get(i).getConfidence()* 100) + "%" +"\n";
                        }
                     }
                    //print disease list
                    Message diseaseList = new Message("", Constants.CHATBOT_ID, fUser.getUid(),
                            temp, new Date(),
                            sessionID, 1);
                    setMessageFirebase(diseaseList);
                    //print ""benh cua ban la"
                    Message message1 = new Message("", Constants.CHATBOT_ID,
                            fUser.getUid(), getString(R.string.default_chatbot_disease), new Date(), sessionID, 1);
                    setMessageFirebase(message1);
                    chat_txt_enter_mess.setText("");
                    //print disease
                    Message message2 = new Message("", Constants.CHATBOT_ID, fUser.getUid(),
                        results.get(0).getTitle() + " " + results.get(0).getConfidence() * 100 + "%", new Date(),
                        sessionID, 1);
                    setMessageFirebase(message2);
                    getDiseaseByNameFirebase(results.get(0).getTitle(), fUser.getUid());
                    chat_txt_enter_mess.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Exception when talking with chatbot ");
                }
            }
        }
    }

    public void chatWithDoctor(String msg) {
        try {
            if (!msg.equals("")) {
                //user chat vs user
                Message message = new Message("", fUser.getUid(), receiverID, msg
                        , new Date(), sessionID, 1);
                setMessageFirebase(message);
                chat_txt_enter_mess.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "chatWithDoctor()");
        }
    }

    /**
     * Get user name, avatar and chat messages of user
     */
    public void getUserChatData() {
        try {
            mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(receiverID);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Account receiver = snapshot.getValue(Account.class);
                    if (receiver != null) {
                        chat_toolbar_txt_name.setText(receiver.getName());
                        Glide.with(Chat.this).load(receiver.getImage()).into(chat_toolbar_img_avatar);
                        getMessagesFirebase(fUser.getUid(), receiverID);
                    } else {
                        Log.d(LOG_TAG, "Cannot get account info");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "getUserChatData()");
        }
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
                try {
                    // If input empty
                    if (chat_txt_enter_mess.getText().toString().equals("")) {
                        chat_img_send.setVisibility(View.GONE);
                        chat_img_mic.setVisibility(View.VISIBLE);
                    } else {
                        chat_img_send.setVisibility(View.VISIBLE);
                        chat_img_mic.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "updateIconOnWriteWatcher()");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    /**
     * Set up chat toolbar
     */
    private void setToolbarChat() {
        try {
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
                        case R.id.chat_menu_end_session:
                            endSession(sessionID);
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
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "setToolbarChat()");
        }
    }

    /**
     * Set new message to Firebase
     *
     * @param msg massage that added to firebase
     */
    private void setMessageFirebase(Message msg) {
        try {
            mRef = FirebaseDatabase.getInstance().getReference("Message/" + sessionID);
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
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "setMessageFirebase()");
        }
    }

    /**
     * End current session
     *
     * @param currentSession Current session
     */
    private void endSession(String currentSession) {
        try {
            mRef = FirebaseDatabase.getInstance().getReference("Session").child(currentSession);
            mRef.child("status").setValue(0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "endSession()");
        }
    }

    /**
     * Check if the chat session has ended
     * If not, then set chat input layout VISIBLE
     */
    private void checkSessionStatus() {
        try {
            mRef = FirebaseDatabase.getInstance().getReference("Session");
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        String sessionStatus =
                                Objects.requireNonNull(snapshot.child(sessionID).child("status").getValue()).toString();
                        int status = Integer.parseInt(sessionStatus);
                        if (status == 0) {
                            chat_send_message_layout = findViewById(R.id.chat_send_message_layout);
                            chat_send_message_layout.setVisibility(View.GONE);
                            Toast.makeText(Chat.this, getString(R.string.defaut_session_ended), Toast.LENGTH_SHORT).show();
                        } else if (status == 1) {
                            chat_send_message_layout = findViewById(R.id.chat_send_message_layout);
                            chat_send_message_layout.setVisibility(View.VISIBLE);
                        }
                    } catch (NullPointerException e) {
                        Log.d(LOG_TAG, "Session status null");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            //Hide keyboard
            hideSoftKeyboard(Chat.this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "checkSessionStatus()");
        }
    }

    /**
     * Get chat messages of current user with another user from Firebase and load it to recycler view
     *
     * @param currentUserID ID of active user
     * @param receiverID    ID of receiver user
     */
    private void getMessagesFirebase(String currentUserID, String receiverID) {
        try {
            mMessage = new ArrayList<>();
            mRef = FirebaseDatabase.getInstance().getReference("Message/" + sessionID);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mMessage.clear();
                    for (DataSnapshot sn : snapshot.getChildren()) {
                        Message msg = sn.getValue(Message.class);

                        if (msg != null && msg.getReceiverID() != null) {
//                        if (msg.getReceiverID().equals(currentUserID) && msg.getSenderID().equals(receiverID)
//                                && msg.getSessionID().equals(sessionID) || msg.getReceiverID().equals(receiverID)
//                                && msg.getSenderID().equals(currentUserID)
//                                && msg.getSessionID().equals(sessionID)) {
//
//                        }
                            mMessage.add(msg);
                            if (msg.getStatus() == 3) {
                                if (checkStartMessage) {
                                    mRef2 = FirebaseDatabase.getInstance().getReference("Message/" + sessionID);
                                    mRef2.child(msg.getMessageID()).child("status").setValue(4);
                                }
                                if (checkClickPredict) {
                                    mRef2 = FirebaseDatabase.getInstance().getReference("Message/" + sessionID);
                                    mRef2.child(msg.getMessageID()).child("status").setValue(4);
                                }
                            }
                            chatAdapter = new ChatAdapter(Chat.this, mMessage);
                            chat_recycler_view.setAdapter(chatAdapter);

                            chatAdapter.setPredictButtonListener(new MyClickListener() {
                                @Override
                                public void onPredict(View button, int position) {
                                    chatWithChatbot(allMess);
                                    mRef2 = FirebaseDatabase.getInstance().getReference("Message/" + sessionID);
                                    mRef2.child(msg.getMessageID()).child("status").setValue(4);
                                    checkClickPredict = true;
//                                getPredict();

                                }
                            });
                        }
                    }
                    checkStartMessage = false;
                    checkClickPredict = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "getMessagesFirebase()");
        }
    }

    /**
     * Get diseaseID by name
     * And create prediction
     *
     * @param disease Disease Name
     * @param uId     Current user ID
     */
    private void getDiseaseByNameFirebase(String disease, String uId) {
        try {
            mRef2 = FirebaseDatabase.getInstance().getReference("Disease");
            Query disQuery = mRef2.orderByChild("name").equalTo(disease);
            disQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot sn : snapshot.getChildren()) {
                            Disease d = sn.getValue(Disease.class);
                            Prediction pre = new Prediction("0", uId, "Default",
                                    sessionID, "Default",
                                    d.getDiseaseID(), "Default", new Date(), new Date(),
                                    d.getSpecializationID(), 0);
                            createPrediction(pre);
                        }
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "Not found disease in database", e);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "getDiseaseByNameFirebase()");
        }
    }

    /**
     * Set up element base on Account Type
     */
    private void setUIByAccountType() {
        try {
            mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(fUser.getUid());
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        String accountType = Objects.requireNonNull(snapshot.child("typeID").getValue()).toString();
                        if (accountType.equals("0")) {
                            chat_toolbar_img_hamburger.setVisibility(View.VISIBLE);
                        } else {
                            chat_toolbar_img_hamburger.setVisibility(View.GONE);
                        }
                    } catch (NullPointerException e) {
                        Log.d(LOG_TAG, "Account type null", e);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "setUIByAccountType()");
        }
    }

    /**
     * Call default Speech-to-text handler of the phone. Permission checking is included
     */
    public void getSpeechInput() {
        try {
            // Check microphone access permission
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                // Call Voice input
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi");
                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, getString(R.string.default_speech_support_device), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                // ask for the permission.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_CODE_SPEECH);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "getSpeechInput()");
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

    /**
     * Create recommend symptom
     *
     * @param rcs RecommendSymptom
     */
    private void createRecommendSymptom(RecommendSymptom rcs) {
        mRef = FirebaseDatabase.getInstance().getReference("RecommendSymptom");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mRef.child(rcs.getMessageID()).setValue(rcs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "onStart");
        handler.post(
                () -> {
                    client.load();
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "onStop");
        handler.post(
                () -> {
                    client.unload();
                });
    }

    /**
     * Create new prediction
     * And end session
     *
     * @param pre Prediction object
     */
    private void createPrediction(Prediction pre) {
        try {
            mRef2 = FirebaseDatabase.getInstance().getReference("Prediction");
            mRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        pre.setPredictionID(mRef2.push().getKey());
                        mRef2.child(pre.getPredictionID()).setValue(pre, new DatabaseReference.CompletionListener() {
                            @Override
                            //If new prediction are created
                            //Then end the current session of chat
                            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                                endSession(sessionID);
                                checkSessionStatus();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "Not found disease in database", e);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "createPrediction()");
        }
    }
//    /**
//     * check symptom by name
//     *
//     * @param listSymp
//     * @param myCallback
//     */
//    private void checkSymptomByNameFirebase(List<String> listSymp , MyCallback myCallback) {
//        mRef2 = FirebaseDatabase.getInstance().getReference("Symptom");
//        Query disQuery = mRef2.orderByChild("name").equalTo(listSymp.get(0));
//        disQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                try {
//                        if(snapshot.exists()){
//                            System.out.println("da vao day roi");
//                            myCallback.onCallback("true");
//
//                        }
//                } catch (Exception e) {
//                    Log.d(LOG_TAG, "Not found disease in database", e);
//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//    }
//    public interface MyCallback {
//        void onCallback(String value);
//    }

    /**
     * Get symptom in firebase
     */
    private void getSymptomFirebase() {
        try {
            mSymptom = new ArrayList<>();
            mRef = FirebaseDatabase.getInstance().getReference("Symptom");
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mSymptom.clear();
                    for (DataSnapshot sn : snapshot.getChildren()) {
                        Symptom msg = sn.getValue(Symptom.class);
                        mSymptom.add(msg);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "getSymptomFirebase()");
        }
    }


    /**
     * Create dialog confirm
     */
    private void dialogConfirm(String sessionID) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.default_dialog_end_session_prediction);
            builder.setPositiveButton(getString(R.string.dialog_confirm_change_account_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                    endSession(sessionID);
                }
            });
            builder.setNegativeButton(getString(R.string.dialog_confirm_change_account_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Do nothing
                }
            });
            builder.create();
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "dialogConfirm()");
        }
    }


    /**
     * @param msg
     */
    public void nextChat(String msg) {
        try {
            if (!msg.equals("")) {
                Message message = new Message("", fUser.getUid(), Constants.CHATBOT_ID
                        , msg, new Date(), sessionID, 1);
                setMessageFirebase(message);
                //Chatbot chat
                Message message1 = new Message("", Constants.CHATBOT_ID,
                        fUser.getUid(), getString(R.string.default_chatbot_continue_symptom), new Date(), sessionID, 3);
                setMessageFirebase(message1);
                chat_txt_enter_mess.setText("");
                allMess += msg + " ";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "nextChat()");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiverID.equals(Constants.CHATBOT_ID)) {
            endSession(sessionID);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiverID.equals(Constants.CHATBOT_ID)) {
            endSession(sessionID);

        }
    }
}