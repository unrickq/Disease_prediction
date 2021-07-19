package com.example.diseaseprediction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diseaseprediction.firebase.FirebaseConstants;
import com.example.diseaseprediction.listener.NetworkChangeListener;
import com.example.diseaseprediction.object.Disease;
import com.example.diseaseprediction.object.Message;
import com.example.diseaseprediction.object.Prediction;
import com.google.android.material.textfield.TextInputLayout;
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

public class PredictionConfirm extends AppCompatActivity {

  private static final String LOG_TAG = "Prediction Confirm";
  //Internet connection
  private NetworkChangeListener networkChangeListener = new NetworkChangeListener();
  //
  ArrayList<Disease> diseasesList = new ArrayList<>();
  // Firebase
  private DatabaseReference mRef;
  private FirebaseUser fUser;
  // Layout
  private ImageView prediction_confirm_toolbar_img_pre;
  private TextView prediction_confirm_txt_disease_description_result;
  private TextView prediction_confirm_txt_disease_prediction_result;
  private TextInputLayout prediction_confirm_disease_select_layout, prediction_confirm_disease_other_layout;
  private AutoCompleteTextView prediction_confirm_disease_select;
  private EditText prediction_confirm_disease_other;
  private Button prediction_confirm_prediction_wrong_btn, prediction_confirm_prediction_correct_btn,
          prediction_confirm_prediction_confirm_btn;
  private ArrayAdapter diseaseAdapter;
  private Prediction prediction;
  private int predictionStatus;
  private Disease selectedDisease; // currently selected disease in combo box


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_confirm);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get prediction ID from Intent
        Intent intent = getIntent();
        prediction = intent.getParcelableExtra("mPrediction");

        setUpUI();

    checkPredictionStatus();

  }

  @Override
  protected void onStart() {
    //Check internet connected or not
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    registerReceiver(networkChangeListener, filter);
    super.onStart();
  }

  @Override
  protected void onStop() {
    //Check internet connected or not
    unregisterReceiver(networkChangeListener);
    super.onStop();
  }

    /**
     * Set up necessary elements for the UI
     */
    private void setUpUI() {
        try {
            getViews();

            getDiseasesListAndLoadUI();

            loadPatientDescription();

            // Event for Incorrect button
            prediction_confirm_prediction_wrong_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayConfirmDialog(0);
                }
            });
            // Event for correct button
            prediction_confirm_prediction_correct_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayConfirmDialog(1);
                }
            });
            // Event for confirm button
            prediction_confirm_prediction_confirm_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleSaveData();
                }
            });
            // Event for back button
            prediction_confirm_toolbar_img_pre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "setUpUI()");
        }

    }

    /**
     * Get UI elements on View
     */
    private void getViews() {
        try {
            prediction_confirm_toolbar_img_pre = findViewById(R.id.prediction_confirm_toolbar_img_pre);

            prediction_confirm_txt_disease_description_result =
                findViewById(R.id.prediction_confirm_txt_patient_description_txt);
            prediction_confirm_txt_disease_prediction_result =
                findViewById(R.id.prediction_confirm_txt_disease_prediction_disease_name);

      prediction_confirm_disease_select_layout = findViewById(R.id.prediction_confirm_disease_select_layout);
      prediction_confirm_disease_select = findViewById(R.id.prediction_confirm_disease_select);

      prediction_confirm_disease_other_layout = findViewById(R.id.prediction_confirm_disease_other_layout);
      prediction_confirm_disease_other = findViewById(R.id.prediction_confirm_disease_other);

      prediction_confirm_prediction_wrong_btn = findViewById(R.id.prediction_confirm_prediction_wrong_btn);
      prediction_confirm_prediction_correct_btn = findViewById(R.id.prediction_confirm_prediction_correct_btn);
      prediction_confirm_prediction_confirm_btn = findViewById(R.id.prediction_confirm_prediction_confirm_btn);
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(LOG_TAG, "getViews()");
    }

  }

  /**
   * Get disease list from Firebase and create adapter for {@link AutoCompleteTextView}. Then, load diseases name
   * into the Prediction Result Textview from Firebase and set default selection of hidden
   * {@link AutoCompleteTextView}
   */
  private void getDiseasesListAndLoadUI() {
    try {
      mRef = FirebaseDatabase.getInstance().getReference(FirebaseConstants.FIREBASE_TABLE_DISEASE);
      mRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
          // Get diseases and add to list
          for (DataSnapshot sn : snapshot.getChildren()) {
            Disease disease = sn.getValue(Disease.class);
            if (disease.getDiseaseID().equals(prediction.getDiseaseID())) {
              selectedDisease = disease;
            }
            diseasesList.add(disease);
          }

          // create new ArrayAdapter
          diseaseAdapter = new ArrayAdapter(PredictionConfirm.this, R.layout.support_simple_spinner_dropdown_item,
                  diseasesList);
          prediction_confirm_disease_select.setAdapter(diseaseAdapter);

          // Set onItemClickListener to check for 'other disease', then set visibility of input layout
          prediction_confirm_disease_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              selectedDisease = (Disease) parent.getItemAtPosition(position);
              // selected disease is 'Other disease'
              if (selectedDisease.getDiseaseID().equals(AppConstants.DISEASE_OTHER_ID)) {
                prediction_confirm_disease_other_layout.setVisibility(View.VISIBLE);
              } else {
                prediction_confirm_disease_other_layout.setVisibility(View.GONE);
              }
            }
          });

          // Set disease name
          prediction_confirm_txt_disease_prediction_result.setText(selectedDisease.getName());
          prediction_confirm_disease_select.setText(selectedDisease.getName());
          diseaseAdapter.getFilter().filter(null);

        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(LOG_TAG, "getDiseasesListAndLoadUI()");
    }
  }

  /**
   * Load Patient's symptoms from the chat between patient and the chat bot from Firebase.
   * After successfully loaded, these symptoms will be loaded on the TextView
   */
  private void loadPatientDescription() {
    try {
      ArrayList<Message> messagesList = new ArrayList<>();

      String sessionID = prediction.getSessionID();
      // get messages in session between user and chat bot
      mRef = FirebaseDatabase.getInstance().getReference(FirebaseConstants.FIREBASE_TABLE_MESSAGE + "/" + sessionID);
      mRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

          for (DataSnapshot sn : snapshot.getChildren()) {
            Message message = sn.getValue(Message.class);
            try {
              // Get messages that was sent by patient
              if (message.getSenderID().equals(prediction.getPatientID())) {
                messagesList.add(message);
            }
          } catch (NullPointerException e) {
            Log.e(LOG_TAG, "loadPatientDescription: Null pointer", e);
            Toast.makeText(PredictionConfirm.this, getString(R.string.error_unknown_contactDev), Toast.LENGTH_SHORT).show();
          }
          }
          displayMessagesList(messagesList);
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(LOG_TAG, "loadPatientDescription()");
    }
  }

  /**
   * Display messages from {@code messagesList} to UI
   *
   * @param messagesList an {@link ArrayList} that store {@link Message}
   */
  private void displayMessagesList(ArrayList<Message> messagesList) {
    try {
      String finalText = "";

      // iterate and concatenate string, then displays messages from messagesList to TextView
      for (Message message : messagesList) {
        finalText = finalText.concat("- " + message.getMessage() + "\n");
      }

      prediction_confirm_txt_disease_description_result.setText(finalText);
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(LOG_TAG, "displayMessagesList()");
    }
  }

  /**
   * Display confirm dialog when user click on buttons.<br/>  If user select positive button, depend on the dialog
   * type, the following action will be made:
   * <ol>
   * <li>Type 0: The method  {@link PredictionConfirm#changeUIIncorrect()} will be called </li>
   * <li>Type 1: the method {@link PredictionConfirm#savePrediction} will be called</li>
   * </ol>
   *
   * @param type type of dialog. 0 is for incorrect prediction, 1 is correct prediction
   */
  private void displayConfirmDialog(int type) {
    try {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);

      // User select Incorrect btn
      if (type == 0) {
        // Set content
        builder.setMessage(R.string.prediction_confirm_dialog_incorrect);

        // Set button
        builder.setPositiveButton(R.string.dialog_button_yes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            changeUIIncorrect();
          }
        });

        builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            // do nothing
          }
        });
        builder.create().show();
      } else if (type == 1) { // User select Correct btn
        // Set content
        builder.setMessage(R.string.prediction_confirm_dialog_correct);

        // Set button
        builder.setPositiveButton(R.string.dialog_button_yes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            // Save prediction with new doctorID and updated status
            String doctorID = fUser.getUid();
            savePrediction(doctorID, 0);
          }
        });

        builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            // do nothing
          }
        });

        builder.create().show();
      } else { // Wrong argument pass in
        Log.w(LOG_TAG, "displayConfirmDialog: invalid 'type' argument!");
      }
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(LOG_TAG, "displayConfirmDialog()");
    }
  }

  /**
   * Change UI elements for Incorrect mode
   */
  private void changeUIIncorrect() {
    try {
      // display
      prediction_confirm_disease_select_layout.setVisibility(View.VISIBLE);
      prediction_confirm_prediction_confirm_btn.setVisibility(View.VISIBLE);
      // hide
      prediction_confirm_prediction_correct_btn.setVisibility(View.GONE);
      prediction_confirm_prediction_wrong_btn.setVisibility(View.GONE);
      // set text
      prediction_confirm_txt_disease_prediction_result.setText(R.string.prediction_confirm_txt_disease_prediction_title);
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(LOG_TAG, "changeUIIncorrect()");
    }
  }

    /**
     * This method is built for Confirm button. It will check and save updated data to Firebase
     */
    private void handleSaveData() {
        String note = "";
        String diseaseID = selectedDisease.getDiseaseID();
        // Check if doctor select other disease option
        if (diseaseID.equals(AppConstants.DISEASE_OTHER_ID)) {
            // get disease name
            note = prediction_confirm_disease_other.getText().toString();
            // check if disease name empty
            if (!note.trim().isEmpty()) {
                savePrediction(fUser.getUid(), 1);
            } else {
                prediction_confirm_disease_other_layout.setError(getString(R.string.error_field_empty));
            }
        } else { // doctor select known diseases
            savePrediction(fUser.getUid(), 1);
        }
    }


  /**
   * Save prediction data. When prediction successfully saved, a {@link PredictionConfirm#displayThanksDialog()} will
   * be called
   *
   * @param doctorID doctor ID
   * @param type     type to save. <ul>
   *                 <li>0: correct</li>
   *                 <li>1: incorrect</li>
   *                 </ul>
   */
  private void savePrediction(String doctorID, int type) {
    try {
      // check if prediction status still equal to 0 i.e "waiting for confirmation"
      if (predictionStatus == 0) {
        mRef = FirebaseDatabase.getInstance().getReference(FirebaseConstants.FIREBASE_TABLE_PREDICTION).child(prediction.getPredictionID());
        mRef.child("doctorID").setValue(doctorID);
        // if prediction correct
        if (type == 0) {
          mRef.child("status").setValue(1); // prediction correct
        } else if (type == 1) { // prediction incorrect
          String diseaseName = prediction_confirm_disease_other.getText().toString();
          mRef.child("diseaseID").setValue(selectedDisease.getDiseaseID()); //set correct disease ID
          // if doctor select unknown disease -> disease name not empty
          if (!diseaseName.isEmpty()) {
            mRef.child("notes").setValue(diseaseName);
          }
          mRef.child("status").setValue(2); // prediction incorrect
        }
      }

      mRef.child("dateUpdate").setValue(new Date());

      displayThanksDialog();
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(LOG_TAG, "savePrediction()");
    }

  }

  /**
   * Check for prediction status constantly. If the status changed to 1 and prediction doctorID is not the current
   * user, display a dialog and send user back to Home
   */
  private void checkPredictionStatus() {
    try {
      mRef = FirebaseDatabase.getInstance().getReference(FirebaseConstants.FIREBASE_TABLE_PREDICTION).child(prediction.getPredictionID());
      mRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
          Prediction prediction = snapshot.getValue(Prediction.class);
          // if prediction is confirmed but not by current doctor
          try {
            if (prediction.getStatus() != 0 && !prediction.getDoctorID().equals(fUser.getUid())) {
              AlertDialog.Builder builder = new AlertDialog.Builder(PredictionConfirm.this);
              builder.setMessage(R.string.prediction_confirm_dialog_prediction_status_invalid);
              builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  // redirect to Home
                  Intent intent = new Intent(PredictionConfirm.this, MainActivity.class);
                  startActivity(intent);
                }
              });
              builder.create().show();
            }
          } catch (NullPointerException e) {
            Log.e(LOG_TAG, "checkPredictionStatus: Param null", e);
            Toast.makeText(PredictionConfirm.this, getString(R.string.error_unknown_contactDev), Toast.LENGTH_LONG).show();
          }
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(LOG_TAG, "checkPredictionStatus()");
    }
  }

  /**
   * A Thank you dialog to thanks doctor when they confirm the prediction. When doctor press on positive button, they
   * will be navigate to Home
   */
  private void displayThanksDialog() {
    try {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage(R.string.prediction_confirm_dialog_thankyou);

      builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          // Navigation to Home
          Intent intent = new Intent(PredictionConfirm.this, MainActivity.class);
          startActivity(intent);
        }
      });

      builder.create().show();
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(LOG_TAG, "displayThanksDialog()");
    }
  }

}