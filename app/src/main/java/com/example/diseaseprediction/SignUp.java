package com.example.diseaseprediction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.diseaseprediction.object.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SignUp extends AppCompatActivity {

    private Account mAccount;
    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private EditText sign_up_edit_txt_name,sign_up_edit_txt_phone, sign_up_edit_txt_email, sign_up_edit_txt_address;
    private Button sign_up_btn_edit_done;
    private Spinner sign_up_spinner_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findView();
        loadData();
        sign_up_btn_edit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmpty() == 0) {
                    dialogConfirm();
                }
            }
        });
    }

    //Find view by ID
    private void findView() {
        //Button
        sign_up_btn_edit_done = findViewById(R.id.sign_up_btn_edit_done);
        //Edit text
        sign_up_edit_txt_name = findViewById(R.id.sign_up_edit_txt_name);
        sign_up_edit_txt_phone = findViewById(R.id.sign_up_edit_txt_phone);
        sign_up_edit_txt_email = findViewById(R.id.sign_up_edit_txt_email);
        sign_up_edit_txt_address = findViewById(R.id.sign_up_edit_txt_address);
        //Spinner
        sign_up_spinner_gender = findViewById(R.id.sign_up_spinner_gender);
        ArrayList<String> gender = new ArrayList<String>();
        gender.add(getString(R.string.default_choose_gender));
        gender.add(getString(R.string.default_gender_male));
        gender.add(getString(R.string.default_gender_female));
        ArrayAdapter genderAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, gender);
        sign_up_spinner_gender.setAdapter(genderAdapter);
    }

    //Load data to UI if login by google
    private void loadData(){
        //get user by id
        mAccount = new Account();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
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
                        sign_up_edit_txt_name.setText(mAccount.getName());
                    } else {
                        sign_up_edit_txt_name.setHint(getString(R.string.default_empty_name));
                    }

                    //Set gender
                    if (mAccount.getGender() == 1) {
                        sign_up_spinner_gender.setSelection(1);
                    } else if (mAccount.getGender() == 2) {
                        sign_up_spinner_gender.setSelection(2);
                    } else {
                        sign_up_spinner_gender.setSelection(0);
                    }

                    //Set phone
                    if (!mAccount.getPhone().equals("Default")) {
                        sign_up_edit_txt_phone.setText(mAccount.getPhone());
                    } else {
                        sign_up_edit_txt_phone.setHint(getString(R.string.default_empty_phone));
                    }

                    //Set email
                    if (!mAccount.getEmail().equals("Default")) {
                        sign_up_edit_txt_email.setText(mAccount.getEmail());
                    } else {
                        sign_up_edit_txt_email.setHint(getString(R.string.default_empty_email));
                    }

                    //Set address
                    if (!mAccount.getAddress().equals("Default")) {
                        sign_up_edit_txt_address.setText(mAccount.getAddress());
                    } else {
                        sign_up_edit_txt_address.setHint(getString(R.string.default_empty_address));
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

    //Check empty edit text and spinner
    //Error: 1 | Normal: 0
    private int checkEmpty() {
        if (!sign_up_edit_txt_name.getText().toString().equals("") &&
                !sign_up_edit_txt_phone.getText().toString().equals("") &&
                !sign_up_edit_txt_email.getText().toString().equals("") &&
                !sign_up_edit_txt_address.getText().toString().equals("") &&
                sign_up_spinner_gender.getSelectedItemPosition() != 0) {
            if (sign_up_spinner_gender.getSelectedItemPosition() != 0) {
                //Clear error icon
                ((TextView) sign_up_spinner_gender.getSelectedView()).setError(null);
            }
            //Default color
            sign_up_edit_txt_name.setHintTextColor(Color.rgb(128, 128, 128));
            sign_up_edit_txt_phone.setHintTextColor(Color.rgb(128, 128, 128));
            sign_up_edit_txt_email.setHintTextColor(Color.rgb(128, 128, 128));
            sign_up_edit_txt_address.setHintTextColor(Color.rgb(128, 128, 128));
            return 0;
        } else {
            if (sign_up_spinner_gender.getSelectedItemPosition() == 0) {
                //Clear error icon
                ((TextView) sign_up_spinner_gender.getSelectedView()).setError("Error message");
            }
            //Set hint error message
            sign_up_edit_txt_name.setHint(getString(R.string.default_empty_name));
            sign_up_edit_txt_phone.setHint(getString(R.string.default_empty_phone));
            sign_up_edit_txt_email.setHint(getString(R.string.default_empty_email));
            sign_up_edit_txt_address.setHint(getString(R.string.default_empty_address));
            //Set color warning
            sign_up_edit_txt_name.setHintTextColor(Color.RED);
            sign_up_edit_txt_phone.setHintTextColor(Color.RED);
            sign_up_edit_txt_email.setHintTextColor(Color.RED);
            sign_up_edit_txt_address.setHintTextColor(Color.RED);
            return 1;
        }
    }

    //Create dialog confirm
    private void dialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_confirm_change_account));
        builder.setPositiveButton(getString(R.string.dialog_confirm_change_account_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setValue();
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(getString(R.string.dialog_confirm_change_account_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create();
        builder.show();
    }

    //Set value to database
    private void setValue() {
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        //Name
        if (sign_up_edit_txt_name.getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("name").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("name").setValue(sign_up_edit_txt_name.getText().toString());
        }

        //Gender
        if (sign_up_spinner_gender.getSelectedItemPosition() == 1) {
            mRef.child(fUser.getUid()).child("gender").setValue(1);
        } else if (sign_up_spinner_gender.getSelectedItemPosition() == 2) {
            mRef.child(fUser.getUid()).child("gender").setValue(2);
        } else {
            mRef.child(fUser.getUid()).child("gender").setValue(-1);
        }

        //Phone
        if (sign_up_edit_txt_phone.getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("phone").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("phone").setValue(sign_up_edit_txt_phone.getText().toString());
        }

        //email
        if (sign_up_edit_txt_email.getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("email").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("email").setValue(sign_up_edit_txt_email.getText().toString());
        }

        //address
        if (sign_up_edit_txt_address.getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("address").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("address").setValue(sign_up_edit_txt_address.getText().toString());
        }
    }
}