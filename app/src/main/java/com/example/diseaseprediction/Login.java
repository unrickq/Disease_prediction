package com.example.diseaseprediction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diseaseprediction.object.Account;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mRef;

    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Button login_btn_login_by_google = findViewById(R.id.login_btn_login_by_google);
        login_btn_login_by_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
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
     * Open google sign in intent
     */
    private void signIn() {
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
            Toast.makeText(this, "Login failed", Toast.LENGTH_LONG);
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
                    Account account = new Account(user.getUid(), 1, phone, name, gender, address, email, imgURL
                            , new Date(), new Date(), 1);
                    //Save new account to firebase
                    //If write data success, start new activity
                    mRef.child(user.getUid()).setValue(account, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                            //Go to account information activity
                            Intent intent = new Intent(Login.this, AccountInfo.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    //Account existed
                    checkDataOfAccount(user);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    /**
     * Check data of current account
     * If there exists default data, go to account information activity
     * If data is full, go to main activity
     *
     * @param user current user
     */
    private void checkDataOfAccount(FirebaseUser user) {
        mAccount = new Account();
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check user exist in firebase
                //if exist then set UI
                if (snapshot.hasChild(user.getUid())) {
                    mAccount = snapshot.child(user.getUid()).getValue(Account.class);
                    if (mAccount.getName().equals("Default") || mAccount.getGender() == -1 ||
                            mAccount.getPhone().equals("Default") || mAccount.getEmail().equals("Default") ||
                            mAccount.getAddress().equals("Default")) {
                        Intent intent = new Intent(Login.this, AccountInfo.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    //IF can't find any data
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}