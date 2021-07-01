package com.example.diseaseprediction;

import android.content.DialogInterface;
import android.content.Intent;
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

  //
  ArrayList<Disease> diseasesList = new ArrayList<>();
  private Prediction prediction;
  private int predictionStatus;
  private Disease selectedDisease;


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

  /**
   * Set up necessary elements for the UI
   */
  private void setUpUI() {
    getViews();

    getDiseasesList();

    loadDiseaseData();

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
        String note = "";
        String diseaseID = selectedDisease.getDiseaseID();
        // Check if doctor select other disease option
        if (diseaseID.equals(Constants.DISEASE_OTHER_ID)) {
          // get disease name
          note = prediction_confirm_disease_other.getText().toString();
          // check if disease name empty
          if (!note.trim().isEmpty()) {
            savePrediction(fUser.getUid(), note);
          } else {
            prediction_confirm_disease_other_layout.setError(getString(R.string.error_field_empty));
          }
        } else { // doctor select known diseases
          savePrediction(fUser.getUid(), selectedDisease.getName());
        }
      }
    });
    // Event for back button
    prediction_confirm_toolbar_img_pre.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
  }

  /**
   * Get UI elements on View
   */
  private void getViews() {

    prediction_confirm_toolbar_img_pre = findViewById(R.id.prediction_confirm_toolbar_img_pre);

    prediction_confirm_txt_disease_description_result =
        findViewById(R.id.prediction_confirm_txt_disease_description_result);
    prediction_confirm_txt_disease_prediction_result =
        findViewById(R.id.prediction_confirm_txt_disease_prediction_result);

    prediction_confirm_disease_select_layout = findViewById(R.id.prediction_confirm_disease_select_layout);
    prediction_confirm_disease_select = findViewById(R.id.prediction_confirm_disease_select);

    prediction_confirm_disease_other_layout = findViewById(R.id.prediction_confirm_disease_other_layout);
    prediction_confirm_disease_other = findViewById(R.id.prediction_confirm_disease_other);

    prediction_confirm_prediction_wrong_btn = findViewById(R.id.prediction_confirm_prediction_wrong_btn);
    prediction_confirm_prediction_correct_btn = findViewById(R.id.prediction_confirm_prediction_correct_btn);
    prediction_confirm_prediction_confirm_btn = findViewById(R.id.prediction_confirm_prediction_confirm_btn);
  }

  /**
   * Get disease list from Firebase and load it into the hidden {@link AutoCompleteTextView}
   */
  private void getDiseasesList() {
    mRef = FirebaseDatabase.getInstance().getReference("Disease");
    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
        // Get diseases and add to list
        for (DataSnapshot sn : snapshot.getChildren()) {
          Disease disease = sn.getValue(Disease.class);
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
            if (selectedDisease.getDiseaseID().equals(Constants.DISEASE_OTHER_ID)) {
              prediction_confirm_disease_other_layout.setVisibility(View.VISIBLE);
            } else {
              prediction_confirm_disease_other_layout.setVisibility(View.GONE);
            }
          }
        });
      }

      @Override
      public void onCancelled(@NonNull @NotNull DatabaseError error) {

      }
    });


  }

  /**
   * Load diseases name in to the Prediction Result Textview from Firebase and set default selection of hidden
   * {@link AutoCompleteTextView}
   */
  private void loadDiseaseData() {
    String diseaseID = prediction.getDiseaseID();

    mRef = FirebaseDatabase.getInstance().getReference("Disease").child(diseaseID);
    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
        Disease disease = snapshot.getValue(Disease.class);
        // Set disease name
        prediction_confirm_txt_disease_prediction_result.setText(disease.getName());
        // Iterate through disease list and set default disease
        for (int i = 0; i < diseasesList.size(); i++) {
          Disease item = diseasesList.get(i);
          if (disease.getName().equals(item.getName())) {
            prediction_confirm_disease_select.setListSelection(i);
            diseaseAdapter.getFilter().filter(null);
          }
        }
      }

      @Override
      public void onCancelled(@NonNull @NotNull DatabaseError error) {

      }
    });
  }

  /**
   * Load Patient's symptoms from the chat between patient and the chat bot from Firebase.
   * After successfully loaded, these symptoms will be loaded on the TextView
   */
  private void loadPatientDescription() {
    ArrayList<Message> messagesList = new ArrayList<>();

    String sessionID = prediction.getSessionID();
    // get messages in session between user and chat bot
    mRef = FirebaseDatabase.getInstance().getReference("Message");
    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

        for (DataSnapshot sn : snapshot.getChildren()) {
          Message message = sn.getValue(Message.class);
          try {
            // Get messages that belong to sessionID and was sent to chat bot i.e sender is patient
            if (message.getSessionID().equals(sessionID) && message.getReceiverID().equals(Constants.CHATBOT_ID)) {
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
  }

  /**
   * Display messages from {@code messagesList} to UI
   *
   * @param messagesList an {@link ArrayList} that store {@link Message}
   */
  private void displayMessagesList(ArrayList<Message> messagesList) {
    String finalText = "";

    // iterate and concatenate string, then displays messages from messagesList to TextView
    for (Message message : messagesList) {
      finalText = finalText.concat("- " + message.getMessage() + "\n");
    }

    prediction_confirm_txt_disease_description_result.setText(finalText);
  }

  /**
   * Display confirm dialog when user click on buttons.<br/>  If user select positive button, depend on the dialog
   * type, the following action will be made:
   * <ol>
   * <li>Type 0: The method will be called </li>
   * <li>Type 1: the method {@link PredictionConfirm#savePrediction} will be called</li>
   * </ol>
   *
   * @param type type of dialog. 0 is for incorrect prediction, 1 is correct prediction
   */
  private void displayConfirmDialog(int type) {
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
          savePrediction(doctorID);
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

  }

  /**
   * Change UI elements for Incorrect mode
   */
  private void changeUIIncorrect() {
    // display
    prediction_confirm_disease_select_layout.setVisibility(View.VISIBLE);
    prediction_confirm_prediction_confirm_btn.setVisibility(View.VISIBLE);
    // hide
    prediction_confirm_prediction_correct_btn.setVisibility(View.GONE);
    prediction_confirm_prediction_wrong_btn.setVisibility(View.GONE);
    // set text
    prediction_confirm_txt_disease_prediction_result.setText(R.string.prediction_confirm_txt_disease_prediction_title);
  }


  /**
   * Save prediction data. When prediction successfully saved, a {@link PredictionConfirm#displayThanksDialog()} will
   * be called
   *
   * @param doctorID    doctor ID
   * @param diseaseName disease name
   */
  private void savePrediction(String doctorID, String diseaseName) {
    // check if prediction status still equal to 0 i.e "waiting for confirmation"
    if (predictionStatus == 0) {
      mRef = FirebaseDatabase.getInstance().getReference("Prediction").child(prediction.getPredictionID());
      mRef.child("doctorID").setValue(doctorID);
      // if prediction incorrect
      if (!diseaseName.isEmpty()) {
        mRef.child("note").setValue(diseaseName);
        mRef.child("status").setValue(2); // prediction incorrect
      } else {
        mRef.child("status").setValue(1); // prediction correct
      }
      mRef.child("dateUpdate").setValue(new Date());

      displayThanksDialog();
    }
  }

  /**
   * Save prediction (for correct case)
   *
   * @param doctorID doctor ID
   */
  private void savePrediction(String doctorID) {
    savePrediction(doctorID, "");
  }

  /**
   * Check for prediction status constantly. If the status changed to 1 and prediction doctorID is not the current
   * user, display a dialog and send user back to Home
   */
  private void checkPredictionStatus() {
    mRef = FirebaseDatabase.getInstance().getReference("Prediction").child(prediction.getPredictionID());
    mRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
        Prediction prediction = snapshot.getValue(Prediction.class);
        // if prediction is confirmed but not by current doctor
        try {
          if (prediction.getStatus() != 0 && !prediction.getDoctorID().equals(fUser.getUid())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PredictionConfirm.this);
            builder.setMessage(R.string.prediction_confirm_dialog_prediction_status_invallid);
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
  }

  /**
   * A Thank you dialog to thanks doctor when they confirm the prediction. When doctor press on positive button, they
   * will be navigate to Home
   */
  private void displayThanksDialog() {
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
  }

}