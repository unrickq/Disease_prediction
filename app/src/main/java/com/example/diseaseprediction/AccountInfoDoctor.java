package com.example.diseaseprediction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.diseaseprediction.object.DoctorInfo;
import com.example.diseaseprediction.object.DoctorSpecialization;
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

public class AccountInfoDoctor extends AppCompatActivity {
    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private ArrayAdapter<DoctorSpecialization> specializationAdapter;
    private ArrayList<DoctorSpecialization> specialization;
    private DoctorInfo mDoctor;
    private DoctorSpecialization ds;

    private TextInputLayout account_info_doctor_txt_title_experience, account_info_doctor_txt_title_description, account_info_doctor_txt_title_specialization;
    private Button account_info_doctor_btn_edit_done;
    private AutoCompleteTextView account_info_doctor_spinner_specialization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info_doctor);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        setView();
        loadData();

        account_info_doctor_btn_edit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check field is empty or not
                if (checkEmpty() == 0) {
                    dialogConfirm();
                }
            }
        });

    }

    //Find view by ID
    private void setView() {
        //Button
        account_info_doctor_btn_edit_done = findViewById(R.id.account_info_doctor_btn_edit_done);

        //Find view
        account_info_doctor_txt_title_experience = findViewById(R.id.account_info_doctor_txt_title_experience);
        account_info_doctor_txt_title_description = findViewById(R.id.account_info_doctor_txt_title_description);
        account_info_doctor_txt_title_specialization = findViewById(R.id.account_info_doctor_txt_title_specialization);

        //Add event to clear error message
        account_info_doctor_txt_title_specialization.getEditText().addTextChangedListener(clearErrorOnTyping(account_info_doctor_txt_title_specialization));
        account_info_doctor_txt_title_description.getEditText().addTextChangedListener(clearErrorOnTyping(account_info_doctor_txt_title_description));
        account_info_doctor_txt_title_experience.getEditText().addTextChangedListener(clearErrorOnTyping(account_info_doctor_txt_title_experience));

        //Spinner
        account_info_doctor_spinner_specialization = findViewById(R.id.account_info_doctor_spinner_specialization);
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
     * Show error message when field is empty
     */
    private void showErrorMess() {
        if (account_info_doctor_txt_title_experience.getEditText().getText().toString().isEmpty()) {
            account_info_doctor_txt_title_experience.setError(getString(R.string.default_empty_experience));
        }
        if (account_info_doctor_txt_title_description.getEditText().getText().toString().isEmpty()) {
            account_info_doctor_txt_title_description.setError(getString(R.string.default_empty_description));
        }
        if (account_info_doctor_txt_title_specialization.getEditText().getText().toString().isEmpty()) {
            account_info_doctor_txt_title_specialization.setError(getString(R.string.default_empty_specialization));
        }

    }

    /**
     * Check empty edit text and spinner
     * Error: 1 | Normal: 0
     *
     * @return
     */
    private int checkEmpty() {
        if (!account_info_doctor_txt_title_experience.getEditText().getText().toString().isEmpty() &&
                !account_info_doctor_txt_title_description.getEditText().getText().toString().isEmpty() &&
                !account_info_doctor_txt_title_specialization.getEditText().getText().toString().isEmpty()) {
            return 0;
        } else {
            showErrorMess();
            return 1;
        }
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
                Intent intent = new Intent(AccountInfoDoctor.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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

    /**
     * Load all data of current account
     */
    private void loadData() {
        //get user by id
        mDoctor = new DoctorInfo();
        mRef = FirebaseDatabase.getInstance().getReference("DoctorInfo");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check user exist in firebase
                //if exist then set UI
                System.out.println(fUser.getUid());
                if (snapshot.hasChild(fUser.getUid())) {
                    mDoctor = snapshot.child(fUser.getUid()).getValue(DoctorInfo.class);

                    //Set spinner
                    specialization = new ArrayList<DoctorSpecialization>();
                    mRef = FirebaseDatabase.getInstance().getReference("Specialization");
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot sh : snapshot.getChildren()) {
                                ds = sh.getValue(DoctorSpecialization.class);
                                assert ds != null;
                                if (mDoctor.getSpecializationID().equals(ds.getSpecializationID())) {
                                    account_info_doctor_spinner_specialization.setText(ds.getName());
                                }
                                specialization.add(ds);
                            }
                            //Set spinner
                            specializationAdapter = new ArrayAdapter<DoctorSpecialization>(AccountInfoDoctor.this, R.layout.support_simple_spinner_dropdown_item, specialization);
                            account_info_doctor_spinner_specialization.setAdapter(specializationAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }

                    });


                    if (mDoctor.getExperience() != -1) {
                        account_info_doctor_txt_title_experience.getEditText().setText(String.valueOf(mDoctor.getExperience()));
                    }

                    if (!mDoctor.getShortDescription().equals("Default")) {
                        account_info_doctor_txt_title_description.getEditText().setText(mDoctor.getShortDescription());
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
     * Save data to firebase
     */
    private void saveData() {
        mRef = FirebaseDatabase.getInstance().getReference("DoctorInfo");

        if (account_info_doctor_txt_title_experience.getEditText().getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("experience").setValue(-1);
        } else {
            mRef.child(fUser.getUid()).child("experience").setValue(Double.valueOf(account_info_doctor_txt_title_experience.getEditText().getText().toString()));
        }

        if (account_info_doctor_txt_title_description.getEditText().getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("shortDescription").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("shortDescription").setValue(account_info_doctor_txt_title_description.getEditText().getText().toString());
        }

        if (account_info_doctor_spinner_specialization.getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("specializationID").setValue("Default");
        } else {
            mRef = FirebaseDatabase.getInstance().getReference("Specialization");
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot sh : snapshot.getChildren()) {
                        ds = sh.getValue(DoctorSpecialization.class);
                        assert ds != null;
                        if (ds.getName().equals(account_info_doctor_spinner_specialization.getText().toString())) {
                            mRef = FirebaseDatabase.getInstance().getReference("DoctorInfo");
                            mRef.child(fUser.getUid()).child("specializationID").setValue(ds.getSpecializationID());
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }

            });
        }

    }
}