package com.example.diseaseprediction.ui.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.Account;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";

    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private Account mAccount;
    private DoctorInfo mDoctor;
    private DoctorSpecialization ds;
    private ArrayAdapter<DoctorSpecialization> specializationAdapter;
    private ArrayList<DoctorSpecialization> specialization;
    private ArrayAdapter genderAdapter;

    private TextInputLayout account_txt_title_name, account_txt_title_gender, account_txt_title_phone, account_txt_title_email, account_txt_title_address,
            account_doctor_txt_title_experience, account_doctor_txt_title_description, account_doctor_txt_title_specialization;
    private AutoCompleteTextView account_spinner_gender, account_doctor_spinner_specialization;

    private LinearLayout account_layout_doctor;
    private Button accout_btn_edit, accout_btn_edit_done;
    private CircleImageView account_img_avatar;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set toolbar
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.menu_account));
        ((MainActivity) getActivity()).setIconToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_account, container, false);
        //Get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        //Find view by id
        findView(view);
        //get data and load it to UI
        getDataForUI();
        //Set disable edit
        disableEdit();
        //Click button edit
        accout_btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEdit();
                accout_btn_edit_done.setVisibility(View.VISIBLE);
                accout_btn_edit.setVisibility(View.GONE);
            }
        });
        //If edit done
        accout_btn_edit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmpty()) {
                    dialogConfirm();
                }
            }
        });

        return view;
    }

    /**
     * Check empty edit text and spinner
     *
     * @return true if all inputs valid
     */
    private boolean checkEmpty() {
        boolean isValid = true;
        try {
            if (account_txt_title_name.getEditText().getText().toString().trim().isEmpty()) {
                account_txt_title_name.setError(getString(R.string.default_empty_name));
                isValid = false;
            }
            if (account_spinner_gender.getText().toString().trim().isEmpty()) {
                account_spinner_gender.setError(getString(R.string.default_empty_gender));
                isValid = false;
            }
            if (account_txt_title_phone.getEditText().getText().toString().trim().isEmpty()) {
                account_txt_title_phone.setError(getString(R.string.default_empty_phone));
                isValid = false;
            }
            if (account_txt_title_email.getEditText().getText().toString().trim().isEmpty()) {
                account_txt_title_email.setError(getString(R.string.default_empty_email));
                isValid = false;
            }
            if (account_txt_title_address.getEditText().getText().toString().trim().isEmpty()) {
                account_txt_title_address.setError(getString(R.string.default_empty_address));
                isValid = false;
            }
            //If account type is doctor
            if (account_layout_doctor.getVisibility()==View.VISIBLE){
                if (account_doctor_txt_title_experience.getEditText().getText().toString().trim().isEmpty()) {
                    account_doctor_txt_title_experience.setError(getString(R.string.default_empty_experience));
                    isValid = false;
                }
                if (account_doctor_txt_title_description.getEditText().getText().toString().trim().isEmpty()) {
                    account_doctor_txt_title_description.setError(getString(R.string.default_empty_description));
                    isValid = false;
                }
                if (account_doctor_txt_title_specialization.getEditText().getText().toString().trim().isEmpty()) {
                    account_doctor_txt_title_specialization.setError(getString(R.string.default_empty_specialization));
                    isValid = false;
                }
            }

        } catch (NullPointerException e) {
            Log.e(TAG, "An EditText is null", e);
            Toast.makeText(getContext(), getText(R.string.error_unknown_contactDev), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    /**
     * Create dialog confirm
     * Dialog show when data on change
     */
    private void dialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.dialog_confirm_change_account));
        builder.setPositiveButton(getString(R.string.dialog_confirm_change_account_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateValue();
                //Set UI
                accout_btn_edit_done.setVisibility(View.GONE);
                accout_btn_edit.setVisibility(View.VISIBLE);
                disableEdit();
            }
        });
        builder.setNegativeButton(getString(R.string.dialog_confirm_change_account_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();
    }

    /**
     * Find view by ID
     *
     * @param view View of current activity
     */
    private void findView(View view) {
        account_layout_doctor = view.findViewById(R.id.account_layout_doctor);
        //Button
        accout_btn_edit = view.findViewById(R.id.accout_btn_edit);
        accout_btn_edit_done = view.findViewById(R.id.accout_btn_edit_done);

        //Find view
        account_txt_title_name = view.findViewById(R.id.account_txt_title_name);
        account_txt_title_gender = view.findViewById(R.id.account_txt_title_gender);
        account_txt_title_phone = view.findViewById(R.id.account_txt_title_phone);
        account_txt_title_email = view.findViewById(R.id.account_txt_title_email);
        account_txt_title_address = view.findViewById(R.id.account_txt_title_address);
        account_img_avatar = view.findViewById(R.id.account_img_avatar);

        account_doctor_txt_title_experience = view.findViewById(R.id.account_doctor_txt_title_experience);
        account_doctor_txt_title_description = view.findViewById(R.id.account_doctor_txt_title_description);
        account_doctor_txt_title_specialization = view.findViewById(R.id.account_doctor_txt_title_specialization);
        account_doctor_spinner_specialization = view.findViewById(R.id.account_doctor_spinner_specialization);

        //Set event for layout
        account_txt_title_name.getEditText().addTextChangedListener(clearErrorOnTyping(account_txt_title_name));
        account_txt_title_gender.getEditText().addTextChangedListener(clearErrorOnTyping(account_txt_title_gender));
        account_txt_title_phone.getEditText().addTextChangedListener(clearErrorOnTyping(account_txt_title_phone));
        account_txt_title_email.getEditText().addTextChangedListener(clearErrorOnTyping(account_txt_title_email));
        account_txt_title_address.getEditText().addTextChangedListener(clearErrorOnTyping(account_txt_title_address));

        account_doctor_txt_title_experience.getEditText().addTextChangedListener(clearErrorOnTyping(account_doctor_txt_title_experience));
        account_doctor_txt_title_description.getEditText().addTextChangedListener(clearErrorOnTyping(account_doctor_txt_title_description));
        account_doctor_txt_title_specialization.getEditText().addTextChangedListener(clearErrorOnTyping(account_doctor_txt_title_specialization));

        //Spinner
        account_spinner_gender = view.findViewById(R.id.account_spinner_gender);
        //Set data for spinner
        ArrayList<String> gender = new ArrayList<String>();
        gender.add(getString(R.string.default_gender_male));
        gender.add(getString(R.string.default_gender_female));
        genderAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, gender);
        account_spinner_gender.setAdapter(genderAdapter);
    }

    /**
     * Enable edit
     */
    private void enableEdit() {
        //Edit text
        account_txt_title_name.setEnabled(true);
        account_txt_title_gender.setEnabled(true);
        account_txt_title_phone.setEnabled(true);
        account_txt_title_email.setEnabled(true);
        account_txt_title_address.setEnabled(true);

        account_doctor_txt_title_experience.setEnabled(true);
        account_doctor_txt_title_description.setEnabled(true);
        account_doctor_txt_title_specialization.setEnabled(true);
    }

    /**
     * Disable edit
     */
    private void disableEdit() {
        account_txt_title_name.setEnabled(false);
        account_txt_title_gender.setEnabled(false);
        account_txt_title_phone.setEnabled(false);
        account_txt_title_email.setEnabled(false);
        account_txt_title_address.setEnabled(false);

        account_doctor_txt_title_experience.setEnabled(false);
        account_doctor_txt_title_description.setEnabled(false);
        account_doctor_txt_title_specialization.setEnabled(false);
    }

    /**
     * Get data for UI
     */
    private void getDataForUI() {
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
                    //Set Doctor information by type
                    if (mAccount.getTypeID()==0){
                        account_layout_doctor.setVisibility(View.VISIBLE);
                        loadDataOfDoctor();
                    }else{
                        account_layout_doctor.setVisibility(View.GONE);
                    }
                    //Set name
                    if (!mAccount.getName().equals("Default")) {
                        account_txt_title_name.getEditText().setText(mAccount.getName());
                    }

                    //Set gender
                    if (mAccount.getGender() == 0) {
                        account_spinner_gender.setText(account_spinner_gender.getAdapter().getItem(0).toString());
                        genderAdapter.getFilter().filter(null);
                    } else if (mAccount.getGender() == 1) {
                        account_spinner_gender.setText(account_spinner_gender.getAdapter().getItem(1).toString());
                        genderAdapter.getFilter().filter(null);
                    }

                    //Set phone
                    if (!mAccount.getPhone().equals("Default")) {
                        account_txt_title_phone.getEditText().setText(mAccount.getPhone());
                    }

                    //Set email
                    if (!mAccount.getEmail().equals("Default")) {
                        account_txt_title_email.getEditText().setText(mAccount.getEmail());
                    }

                    //Set address
                    if (!mAccount.getAddress().equals("Default")) {
                        account_txt_title_address.getEditText().setText(mAccount.getAddress());
                    }

                    //Set image
                    if (!mAccount.getImage().equals("Default")) {
                        Glide.with(AccountFragment.this).load(mAccount.getImage()).into(account_img_avatar);
                    } else {
                        Glide.with(AccountFragment.this).load(R.drawable.background_avatar).into(account_img_avatar);
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
     * Update value to database
     * Then update all text view
     */
    private void updateValue() {
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        //Name
        if (account_txt_title_name.getEditText().getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("name").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("name").setValue(account_txt_title_name.getEditText().getText().toString());
        }

        //Gender
        if (account_spinner_gender.getText().toString().equals(account_spinner_gender.getAdapter().getItem(0).toString())) {
            mRef.child(fUser.getUid()).child("gender").setValue(0);
        } else if (account_spinner_gender.getText().toString().equals(account_spinner_gender.getAdapter().getItem(1).toString())) {
            mRef.child(fUser.getUid()).child("gender").setValue(1);
        } else {
            mRef.child(fUser.getUid()).child("gender").setValue(-1);
        }

        //Phone
        if (account_txt_title_phone.getEditText().getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("phone").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("phone").setValue(account_txt_title_phone.getEditText().getText().toString());
        }

        //email
        if (account_txt_title_email.getEditText().getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("email").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("email").setValue(account_txt_title_email.getEditText().getText().toString());
        }

        //address
        if (account_txt_title_address.getEditText().getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("address").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("address").setValue(account_txt_title_address.getEditText().getText().toString());
        }

        if(account_layout_doctor.getVisibility()==View.VISIBLE){
            saveDataOfDoctor();
        }
    }

    /**
     * Load all data of current account
     */
    private void loadDataOfDoctor() {
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
                                    account_doctor_spinner_specialization.setText(ds.getName());
                                }
                                specialization.add(ds);
                            }
                            //Set spinner
                            specializationAdapter = new ArrayAdapter<DoctorSpecialization>(getContext(), R.layout.support_simple_spinner_dropdown_item, specialization);
                            account_doctor_spinner_specialization.setAdapter(specializationAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }

                    });


                    if (mDoctor.getExperience() != -1) {
                        account_doctor_txt_title_experience.getEditText().setText(String.valueOf(mDoctor.getExperience()));
                    }

                    if (!mDoctor.getShortDescription().equals("Default")) {
                        account_doctor_txt_title_description.getEditText().setText(mDoctor.getShortDescription());
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
    private void saveDataOfDoctor() {
        mRef = FirebaseDatabase.getInstance().getReference("DoctorInfo");

        if (account_doctor_txt_title_experience.getEditText().getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("experience").setValue(-1);
        } else {
            mRef.child(fUser.getUid()).child("experience").setValue(Double.valueOf(account_doctor_txt_title_experience.getEditText().getText().toString()));
        }

        if (account_doctor_txt_title_description.getEditText().getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("shortDescription").setValue("Default");
        } else {
            mRef.child(fUser.getUid()).child("shortDescription").setValue(account_doctor_txt_title_description.getEditText().getText().toString());
        }

        if (account_doctor_spinner_specialization.getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("specializationID").setValue("Default");
        } else {
            mRef = FirebaseDatabase.getInstance().getReference("Specialization");
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot sh : snapshot.getChildren()) {
                        ds = sh.getValue(DoctorSpecialization.class);
                        assert ds != null;
                        if (ds.getName().equals(account_doctor_spinner_specialization.getText().toString())) {
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