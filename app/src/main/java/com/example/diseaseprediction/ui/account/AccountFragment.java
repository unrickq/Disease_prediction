package com.example.diseaseprediction.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.AccountEdit;
import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.DoctorInfo;
import com.example.diseaseprediction.object.DoctorSpecialization;
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

    //Firebase
    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private Account mAccount;
    private DoctorInfo mDoctor;
    private DoctorSpecialization ds;
    private ArrayAdapter<DoctorSpecialization> specializationAdapter;
    private ArrayList<DoctorSpecialization> specialization;
    private ArrayAdapter genderAdapter;

    private TextView account_txt_name, account_txt_gender, account_txt_phone, account_txt_email,
        account_txt_address, account_doctor_txt_specialization, account_doctor_txt_experience,
        account_doctor_txt_description;

    private AutoCompleteTextView account_spinner_gender, account_doctor_spinner_specialization;

    private LinearLayout account_layout_doctor;
    private Button accout_btn_edit;
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
        findViews(view);
        //get data and load it to UI
        getDataForUI();

        //Click button edit
        accout_btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AccountEdit.class);
                startActivity(intent);

            }
        });
        return view;
    }

    /**
     * Find view by ID
     *
     * @param view View of current activity
     */
    private void findViews(View view) {
        account_layout_doctor = view.findViewById(R.id.account_layout_doctor);
        //Button
        accout_btn_edit = view.findViewById(R.id.account_btn_edit);


        //Find view
        account_txt_name = view.findViewById(R.id.account_txt_name);

        account_txt_gender = view.findViewById(R.id.account_txt_gender);

        account_txt_phone = view.findViewById(R.id.account_txt_phone);

        account_txt_email = view.findViewById(R.id.account_txt_email);

        account_txt_address = view.findViewById(R.id.account_txt_address);

        account_img_avatar = view.findViewById(R.id.account_img_avatar);


        account_doctor_txt_experience = view.findViewById(R.id.account_doctor_txt_experience);

        account_doctor_txt_description = view.findViewById(R.id.account_doctor_txt_description);

        account_doctor_txt_specialization = view.findViewById(R.id.account_doctor_txt_specialization);
        account_doctor_spinner_specialization = view.findViewById(R.id.account_doctor_spinner_specialization);

        //Spinner
        account_spinner_gender = view.findViewById(R.id.account_spinner_gender);
        //Set data for spinner
        ArrayList<String> gender = new ArrayList<String>();
        gender.add(getString(R.string.default_gender_male));
        gender.add(getString(R.string.default_gender_female));
        genderAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, gender);
        account_spinner_gender.setAdapter(genderAdapter);

    }

    /**
     * Get data for UI
     */
    private void getDataForUI() {
        //get user by id
        mAccount = new Account();
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check user exist in firebase
                //if exist then set UI
                if (snapshot.hasChild(fUser.getUid())) {
                    mAccount = snapshot.child(fUser.getUid()).getValue(Account.class);
                    try {
                        //Set Doctor information by type
                        if (mAccount.getTypeID() == 0) {
                            account_layout_doctor.setVisibility(View.VISIBLE);
                            loadDataOfDoctor();
                        } else {
                            account_layout_doctor.setVisibility(View.GONE);
                        }
                        //Set name
                        if (!mAccount.getName().equals("Default")) {
                            String name = mAccount.getName();
                            account_txt_name.setText(Html.fromHtml(getString(R.string.account_txt_title_name_format, name)));
                        }

                        //Set gender
                        int gender = mAccount.getGender();
                        String genderStr = account_spinner_gender.getAdapter().getItem(gender).toString();
                        account_txt_gender.setText(Html.fromHtml(getString(R.string.account_txt_title_gender_format, genderStr)));

                        //Set phone
                        if (!mAccount.getPhone().equals("Default")) {
                            String phone = mAccount.getPhone();
                            account_txt_phone.setText(Html.fromHtml(getString(R.string.account_txt_title_phone_format, phone)));
                        }

                        //Set email
                        if (!mAccount.getEmail().equals("Default")) {
                            String email = mAccount.getEmail();
                            account_txt_email.setText(Html.fromHtml(getString(R.string.account_txt_title_email_format, email)));
                        }

                        //Set address
                        if (!mAccount.getAddress().equals("Default")) {
                            String address = mAccount.getAddress();
                            account_txt_address.setText(Html.fromHtml(getString(R.string.account_txt_title_address_format, address)));
                        }

                        //Set image
                        if (!mAccount.getImage().equals("Default")) {
                            Glide.with(AccountFragment.this).load(mAccount.getImage()).into(account_img_avatar);
                        } else {
                            account_img_avatar.setImageResource(R.mipmap.ic_default_avatar_round);
                        }
                    } catch (NullPointerException e) {
                        Log.d(TAG, "Account. Account ID null", e);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
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
                    try {
                        //Set spinner
                        specialization = new ArrayList<DoctorSpecialization>();
                        mRef = FirebaseDatabase.getInstance().getReference("Specialization");
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot sh : snapshot.getChildren()) {
                                    ds = sh.getValue(DoctorSpecialization.class);
                                    try {
                                        assert ds != null;
                                        if (mDoctor.getSpecializationID().equals(ds.getSpecializationID())) {
                                            String specialization = ds.getName();
                                            account_doctor_txt_specialization.setText(Html.fromHtml(getString(R.string.account_info_doctor_specialization_format, specialization)));
                                            account_doctor_spinner_specialization.setText(specialization);
                                        }
                                        specialization.add(ds);
                                    } catch (NullPointerException e) {
                                        Log.d(TAG, "Account. loadDataOfDoctor", e);
                                    }
                                }
                                //Set spinner
                                specializationAdapter = new ArrayAdapter<DoctorSpecialization>(requireContext(),
                                    R.layout.support_simple_spinner_dropdown_item, specialization);
                                account_doctor_spinner_specialization.setAdapter(specializationAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }

                        });


                        if (mDoctor.getExperience() != -1) {
                            String experience = String.valueOf(mDoctor.getExperience());
                            account_doctor_txt_experience.setText(Html.fromHtml(getString(R.string.account_info_doctor_experience_format, experience)));
                        }

                        if (!mDoctor.getShortDescription().equals("Default")) {
                            String description = mDoctor.getShortDescription();
                            account_doctor_txt_description.setText(Html.fromHtml(getString(R.string.account_info_doctor_description_format, description)));
                        }
                    } catch (NullPointerException e) {
                        Log.d(TAG, "Account. loadDataOfDoctor", e);
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