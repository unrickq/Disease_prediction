package com.example.diseaseprediction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diseaseprediction.object.Account;
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


public class AccountInfo extends AppCompatActivity {

    private static final String TAG = "AccountInfo";

    private Account mAccount;
    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private TextInputLayout account_info_txt_title_name, account_info_txt_title_gender, account_info_txt_title_phone,
        account_info_txt_title_email, account_info_txt_title_address;
    private Button account_info_btn_edit_done;
    private AutoCompleteTextView account_info_spinner_gender;
    private ArrayAdapter genderAdapter;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        //Get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        //getting mobile number from the previous activity
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra(Login.INTENT_MOBILE);
        Log.d(TAG, "Phone:" + phoneNumber);

        //Find view
        setView();

        //Load data
        loadData();

        //Save button
        account_info_btn_edit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check field is empty or not
                if (checkEmpty()) {
                    dialogConfirm();
                }
            }
        });
    }

    //Find view by ID
    private void setView() {
        //Button
        account_info_btn_edit_done = findViewById(R.id.account_info_btn_edit_done);

        //Find view
        account_info_txt_title_name = findViewById(R.id.account_info_txt_title_name);
        account_info_txt_title_gender = findViewById(R.id.account_info_txt_title_gender);
        account_info_txt_title_phone = findViewById(R.id.account_info_txt_title_phone);
        account_info_txt_title_email = findViewById(R.id.account_info_txt_title_email);
        account_info_txt_title_address = findViewById(R.id.account_info_txt_title_address);

        //Add event to clear error message
        account_info_txt_title_name.getEditText().addTextChangedListener(clearErrorOnTyping(account_info_txt_title_name));
        account_info_txt_title_gender.getEditText().addTextChangedListener(clearErrorOnTyping(account_info_txt_title_gender));
        account_info_txt_title_phone.getEditText().addTextChangedListener(clearErrorOnTyping(account_info_txt_title_phone));
        account_info_txt_title_email.getEditText().addTextChangedListener(clearErrorOnTyping(account_info_txt_title_email));
        account_info_txt_title_address.getEditText().addTextChangedListener(clearErrorOnTyping(account_info_txt_title_address));

        //Spinner
        account_info_spinner_gender = findViewById(R.id.account_info_spinner_gender);
        ArrayList<String> gender = new ArrayList<String>();
        gender.add(getString(R.string.default_gender_male));
        gender.add(getString(R.string.default_gender_female));
        gender.add("Other");
        genderAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, gender);
        account_info_spinner_gender.setAdapter(genderAdapter);

        // If previous activity send phone number => display on edit text
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            account_info_txt_title_phone.getEditText().setText(phoneNumber);
        }
    }

    /**
     * Load all data of current account
     */
    private void loadData() {
        //get user by id
        mAccount = new Account();
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check user exist in firebase
                //if exist then set UI
                if (snapshot.hasChild(fUser.getUid())) {
                    mAccount = snapshot.child(fUser.getUid()).getValue(Account.class);
                    //Set name
                    if (!mAccount.getName().equals("Default")) {
                        account_info_txt_title_name.getEditText().setText(mAccount.getName());
                    }

                    //Set gender
                    if (mAccount.getGender() == 0) {
                        account_info_spinner_gender.setText(account_info_spinner_gender.getAdapter().getItem(0).toString());
                        genderAdapter.getFilter().filter(null);
                    } else if (mAccount.getGender() == 1) {
                        account_info_spinner_gender.setText(account_info_spinner_gender.getAdapter().getItem(1).toString());
                        genderAdapter.getFilter().filter(null);
                    } else if (mAccount.getGender() == 2) {
                        account_info_spinner_gender.setText(account_info_spinner_gender.getAdapter().getItem(2).toString());
                        genderAdapter.getFilter().filter(null);
                    }

                    //Set phone
                    if (!mAccount.getPhone().equals("Default")) {
                        account_info_txt_title_phone.getEditText().setText(mAccount.getPhone());
                    }

                    //Set email
                    if (!mAccount.getEmail().equals("Default")) {
                        account_info_txt_title_email.getEditText().setText(mAccount.getEmail());
                    }

                    //Set address
                    if (!mAccount.getAddress().equals("Default")) {
                        account_info_txt_title_address.getEditText().setText(mAccount.getAddress());
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

    /**
     * Check empty edit text and spinner
     *
     * @return true if all input valid
     */
    private boolean checkEmpty() {
        boolean isValid = true;
        try {
            if (account_info_txt_title_name.getEditText().getText().toString().trim().isEmpty()) {
                account_info_txt_title_name.setError(getString(R.string.default_empty_name));
                isValid = false;
            }
            if (account_info_spinner_gender.getText().toString().trim().isEmpty()) {
                account_info_txt_title_gender.setError(getString(R.string.default_empty_gender));
                isValid = false;
            }
            if (account_info_txt_title_phone.getEditText().getText().toString().trim().isEmpty()) {
                account_info_txt_title_phone.setError(getString(R.string.default_empty_phone));
                isValid = false;
            }
            if (account_info_txt_title_email.getEditText().getText().toString().trim().isEmpty()) {
                account_info_txt_title_email.setError(getString(R.string.default_empty_email));
                isValid = false;
            }
            if (account_info_txt_title_address.getEditText().getText().toString().trim().isEmpty()) {
                account_info_txt_title_address.setError(getString(R.string.default_empty_address));
                isValid = false;
            }

        } catch (NullPointerException e) {
            Log.e(TAG, "An EditText is null", e);
            Toast.makeText(this, getText(R.string.error_unknown_contactDev), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    /**
     * Create dialog confirm
     */
    private void dialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_confirm_change_account));
        builder.setPositiveButton(getString(R.string.dialog_confirm_change_account_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveData();
                Intent intent = new Intent(AccountInfo.this, MainActivity.class);
                startActivity(intent);
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

    /**
     * Create a {@link TextWatcher} to clear {@link TextInputLayout} error notification
     *
     * @return a {@link TextWatcher}
     */
    private TextWatcher clearErrorOnTyping(TextInputLayout layout) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    /**
     * Save data to firebase
     */
    private void saveData() {
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        //Name
        if (account_info_txt_title_name.getEditText().getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("name").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("name").setValue(account_info_txt_title_name.getEditText().getText().toString());
        }

        //Gender
        if (account_info_spinner_gender.getText().toString().equals(account_info_spinner_gender.getAdapter().getItem(0).toString())) {
            mRef.child(fUser.getUid()).child("gender").setValue(0);
        } else if (account_info_spinner_gender.getText().toString().equals(account_info_spinner_gender.getAdapter().getItem(1).toString())) {
            mRef.child(fUser.getUid()).child("gender").setValue(1);
        } else if (account_info_spinner_gender.getText().toString().equals(account_info_spinner_gender.getAdapter().getItem(2).toString())) {
            mRef.child(fUser.getUid()).child("gender").setValue(2);
        } else {
            mRef.child(fUser.getUid()).child("gender").setValue(-1);
        }

        //Phone
        if (account_info_txt_title_phone.getEditText().getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("phone").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("phone").setValue(account_info_txt_title_phone.getEditText().getText().toString());
        }

        //email
        if (account_info_txt_title_email.getEditText().getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("email").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("email").setValue(account_info_txt_title_email.getEditText().getText().toString());
        }

        //address
        if (account_info_txt_title_address.getEditText().getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("address").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("address").setValue(account_info_txt_title_address.getEditText().getText().toString());
        }
    }
}