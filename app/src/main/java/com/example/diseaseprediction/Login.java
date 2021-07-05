package com.example.diseaseprediction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.DoctorInfo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Login extends AppCompatActivity {

  private TextInputLayout phoneInputLayout;
  private ImageView loginButton;
  private TextView login_txt_title, login_txt_by_type;
  private Button login_btn_login_by_google;

  private static final String TAG = "GoogleActivity";
  public static final String INTENT_MOBILE = "mobile";

  private static final int RC_SIGN_IN = 9001;
  private GoogleSignInClient mGoogleSignInClient;
  private DatabaseReference mRef;

  private int type = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_login);
    Intent intent = getIntent();
    type = intent.getIntExtra("type", 1);

    initUIElements();

    //Set login by type
    login_txt_by_type.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //Send type account 0:doctor | 1:user
        Intent i = new Intent(Login.this, Login.class);
        if (type == 0) {
          i.putExtra("type", 1);
        } else {
          i.putExtra("type", 0);
        }
        Login.this.startActivity(i);
      }
    });

    // Set up UI elements
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (checkPhoneNumberInput()) {
          signInWithPhone();
        }
      }
    });

    phoneInputLayout.getEditText().addTextChangedListener(clearErrorOnTyping());

    // Configure Google Sign In
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    login_btn_login_by_google.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        signInWithFirebase();
      }
    });


  }

  /**
   * Get view
   */
  private void initUIElements() {
    login_btn_login_by_google = findViewById(R.id.login_btn_login_by_google);
    phoneInputLayout = findViewById(R.id.login_eTxt_user_layout);
    loginButton = findViewById(R.id.login_img_next);
    login_txt_title = findViewById(R.id.login_txt_title);
    login_txt_by_type = findViewById(R.id.login_txt_by_type);
    if (type == 0) {
      login_txt_title.setText(getString(R.string.login_txt_title_doctor));
      login_txt_by_type.setText(getString(R.string.login_txt_by_type));
    } else {
      login_txt_title.setText(getString(R.string.login_txt_title));
      login_txt_by_type.setText(getString(R.string.login_txt_by_type_doctor));
    }
  }

  /**
   * on_start check user is logged in or not
   */
  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser == null) {
      //Clear current google account
      mGoogleSignInClient.revokeAccess();
    } else {
      Intent intent = new Intent(Login.this, MainActivity.class);
      startActivity(intent);
      finish();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = task.getResult(ApiException.class);
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        firebaseAuthWithGoogle(account.getIdToken());
      } catch (ApiException e) {
        // Google Sign In failed, update UI appropriately
        Log.w(TAG, "Google sign in failed", e);
      }
    }
  }

  /**
   * Auth_with_google
   *
   * @param idToken
   */
  private void firebaseAuthWithGoogle(String idToken) {
    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              // Sign in success, update UI with the signed-in user's information
              Log.d(TAG, "signInWithCredential:success");
              FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
              updateUI(user);
            } else {
              // If sign in fails, display a message to the user.
              Log.w(TAG, "signInWithCredential:failure", task.getException());
              updateUI(null);
            }
          }
        });
  }

  /**
   * Open Google sign in intent
   */
  private void signInWithFirebase() {
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  /**
   * Go to next screen
   *
   * @param user current user
   */
  private void updateUI(FirebaseUser user) {
    //If current user isn't null, then create new account
    if (user != null) {
      CreateNewAccount(user);
    } else {
      Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
    }
  }

  /**
   * Create new account in firebase
   *
   * @param user current user
   */
  private void CreateNewAccount(FirebaseUser user) {
    mRef = FirebaseDatabase.getInstance().getReference().child("Accounts");
    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        //check user exist in firebase
        if (!snapshot.hasChild(user.getUid())) {
          //Set default data
          int gender = -1;
          String phone = "Default";
          String address = "Default";
          String email = "Default";
          String name = "Default";
          String imgURL = "Default";

          //Get data of current user (data is exist if login by google)
          if (user.getDisplayName() != null) {
            name = user.getDisplayName();
          }
          if (user.getPhoneNumber() != null) {
            phone = user.getPhoneNumber();
          }
          if (user.getEmail() != null) {
            email = user.getEmail();
          }
          if (user.getPhotoUrl() != null) {
            imgURL = user.getPhotoUrl().toString();
          }

          //Create new account
          Account account = new Account(user.getUid(), type, 1, phone, name, gender, address, email, imgURL
              , new Date(), new Date(), 1);
          //Save new account to firebase
          //If write data success, start new activity
          mRef.child(user.getUid()).setValue(account, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error,
                                   @NonNull DatabaseReference ref) {
              //If account type is doctor, then create new doctor information
              if (account.getTypeID() == 0) {
                DoctorInfo doctorInfo = new DoctorInfo(user.getUid(), "Default", "Default", -1, new Date(),
                    new Date(), 1);
                mRef = FirebaseDatabase.getInstance().getReference().child("DoctorInfo");
                mRef.child(user.getUid()).setValue(doctorInfo);
              }
              //Go to account information activity
              Intent intent = new Intent(Login.this, MainActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(intent);
              finish();
            }
          });
        } else {
          //Account existed
          Intent intent = new Intent(Login.this, MainActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
          finish();
        }
      }

      @Override
      public void onCancelled(@NonNull @NotNull DatabaseError error) {

      }
    });
  }

  /**
   * Check phone number input
   */
  private boolean checkPhoneNumberInput() {
    // Input empty
    String phoneNumber = phoneInputLayout.getEditText().getText().toString();
    // input field empty
    if (phoneNumber.isEmpty()) {
      phoneInputLayout.setError(getString(R.string.error_field_empty));
      return false;
    } else if (phoneNumber.length() != 10) { // phone number length greater than 10
      phoneInputLayout.setError(getString(R.string.error_login_phone_too_long));
      return false;
    } else {
      return true;
    }
  }

  /**
   * Create a {@link TextWatcher} to clear {@link TextInputLayout} error notification
   *
   * @return a {@link TextWatcher}
   */
  private TextWatcher clearErrorOnTyping() {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        phoneInputLayout.setError(null);
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    };
  }

  private void signInWithPhone() {
    // Check input
    if (checkPhoneNumberInput()) {
      String phone = phoneInputLayout.getEditText().getText().toString();
      Intent intent = new Intent(Login.this, CodeVerify.class);
      intent.putExtra(INTENT_MOBILE, addCountryCode(phone));
      intent.putExtra("type", type);
      startActivity(intent);
    }
  }

  /**
   * Add country code to phone number
   *
   * @param phone raw phone number
   * @return phone number with country code
   */
  private String addCountryCode(String phone) {
    // if user input 0123 -> +84 123
    if (phone.startsWith("0")) {
      return "+84" + phone.substring(1);
    } else {
      return "+84" + phone;
    }
  }

}