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
import android.view.Menu;
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

    private static final String TAG = "TextClassificationDemo";

    /**
     * Hide keyboard
     *
     * @param activity Current activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

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
                    //model xu ly
                    List<Result> results = client.classify(msg);
                    List<String> token = client.tokenize(msg);
                    //get symptom user input
                    String result = getString(R.string.default_chatbot_symptom);
                    for (Symptom s : mSymptom) {
                        for (String tk : token) {
                            tk = tk.replace("_", " ");
                            if (s.getName().equals(tk)) {
                                result += tk + ", ";
                            }
                        }
                    }
                    //print symptom user input
                    result = result.substring(0, result.length() - 2);
                    Message mess = new Message("", Constants.CHATBOT_ID, fUser.getUid(),
                            result, new Date(), sessionID, 1);
                    setMessageFirebase(mess);
                    //print ""benh cua ban la"
                    Message message1 = new Message("", Constants.CHATBOT_ID,
                            fUser.getUid(), getString(R.string.default_chatbot_disease), new Date(), sessionID, 1);
                    setMessageFirebase(message1);
                    chat_txt_enter_mess.getText().clear();
                    //get disease from model
                    for (Result var : results) {
                        System.out.println(var.getTitle() + " " + var.getConfidence());
                    }
                    //print disease
                    Message message2 = new Message("", Constants.CHATBOT_ID, fUser.getUid(),
                            results.get(0).getTitle() + " " + results.get(0).getConfidence(), new Date(),
                            sessionID, 1);
                    setMessageFirebase(message2);
                    getDiseaseByNameFirebase(results.get(0).getTitle(), fUser.getUid());
                    chat_txt_enter_mess.getText().clear();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Exception when talking with chatbot ");
                }
            }
        }
    }

    public void chatWithDoctor(String msg) {
        if (!msg.equals("")) {
            //user chat vs user
            Message message = new Message("", fUser.getUid(), receiverID, msg
                    , new Date(), sessionID, 1);
            setMessageFirebase(message);
            chat_txt_enter_mess.getText().clear();
        }
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

                    getMessagesFirebase(fUser.getUid(), receiverID);
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
                if (receiverID.equals(Constants.CHATBOT_ID)) {
                    System.out.println("session la" + sessionID);
                    mRef = FirebaseDatabase.getInstance().getReference("Session").child(sessionID);
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Session ss = snapshot.getValue(Session.class);
                            System.out.println("check vl" + ss.getStatus());
                            System.out.println();
                            if (ss.getStatus() != 0) {
                                dialogConfirm(sessionID);
                            } else {
                                onBackPressed();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    onBackPressed();
                }

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
    }

    /**
     * Set new message to Firebase
     *
     * @param msg massage that added to firebase
     */
    private void setMessageFirebase(Message msg) {
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

    /**
     * End current session
     *
     * @param currentSession Current session
     */
    private void endSession(String currentSession) {
        mRef = FirebaseDatabase.getInstance().getReference("Session").child(currentSession);
        mRef.child("status").setValue(0);
    }

    /**
     * Check if the chat session has ended
     * Then set chat input layout GONE
     */
    private void checkSessionStatus() {
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
    }

    /**
     * Get chat messages of current user with another user from Firebase and load it to recycler view
     *
     * @param currentUserID ID of active user
     * @param receiverID    ID of receiver user
     */
    private void getMessagesFirebase(String currentUserID, String receiverID) {
        mMessage = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference("Message");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMessage.clear();
                for (DataSnapshot sn : snapshot.getChildren()) {
                    Message msg = sn.getValue(Message.class);

                    if (msg != null && msg.getReceiverID() != null) {
                        if (msg.getReceiverID().equals(currentUserID) && msg.getSenderID().equals(receiverID)
                                && msg.getSessionID().equals(sessionID) || msg.getReceiverID().equals(receiverID)
                                && msg.getSenderID().equals(currentUserID)
                                && msg.getSessionID().equals(sessionID)) {
                            mMessage.add(msg);
                        }
                        if (msg.getStatus() == 3) {
                            if (checkStartMessage) {
                                mRef2 = FirebaseDatabase.getInstance().getReference("Message");
                                mRef2.child(msg.getMessageID()).child("status").setValue(4);
                            }
                            if (checkClickPredict) {
                                mRef2 = FirebaseDatabase.getInstance().getReference("Message");
                                mRef2.child(msg.getMessageID()).child("status").setValue(4);
                            }
                        }
                        chatAdapter = new ChatAdapter(Chat.this, mMessage);
                        chat_recycler_view.setAdapter(chatAdapter);

                        chatAdapter.setPredictButtonListener(new MyClickListener() {
                            @Override
                            public void onPredict(View button, int position) {
                                chatWithChatbot(allMess);
                                mRef2 = FirebaseDatabase.getInstance().getReference("Message");
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
    }

    /**
     * Get diseaseID by name
     * And create prediction
     *
     * @param disease Disease Name
     * @param uId     Current user ID
     */
    private void getDiseaseByNameFirebase(String disease, String uId) {
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
    }

    /**
     * Set up element base on Account Type
     */
    private void setUIByAccountType() {
        mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(fUser.getUid());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String accountType = Objects.requireNonNull(snapshot.child("typeID").getValue()).toString();
                    Menu m = pm.getMenu();
                    if (accountType.equals("0")) {
                        m.getItem(0).setTitle(getString(R.string.chat_menu_doctor_view_info));
                    } else {
                        m.getItem(1).setVisible(false);
                    }
                } catch (NullPointerException e) {
                    Log.d(LOG_TAG, "Account type null", e);
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
        Log.v(TAG, "onStart");
        handler.post(
                () -> {
                    client.load();
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
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
        mSymptom = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference("Symptom");
        mRef.addValueEventListener(new ValueEventListener() {
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
    }


    /**
     * Create dialog confirm
     */
    private void dialogConfirm(String sessionID) {
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
    }


    public void nextChat(String msg) {
        if (!msg.equals("")) {
            // User request
            //user chat vs user
            // user chat
            Message message = new Message("", fUser.getUid(), Constants.CHATBOT_ID
                    , msg, new Date(), sessionID, 1);
            setMessageFirebase(message);
            //chatbot chat
            Message message1 = new Message("", Constants.CHATBOT_ID,
                    fUser.getUid(), getString(R.string.default_chatbot_continue_symptom), new Date(), sessionID, 3);
            setMessageFirebase(message1);
            System.out.println("MS1" + message1.getMessageID());
            chat_txt_enter_mess.getText().clear();
            allMess += msg + " ";
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