package com.example.diseaseprediction.ui.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.Account;
import com.google.android.gms.common.api.internal.LifecycleFragment;
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
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Account mAccount;
    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private Button accout_btn_edit, accout_btn_edit_done;
    private EditText account_edit_txt_name, account_edit_txt_phone, account_edit_txt_email, account_edit_txt_address;
    private TextView account_txt_name, account_txt_phone, account_txt_email, account_txt_address;
    private CircleImageView account_img_avatar;
    private Spinner account_spinner_gender;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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

        //Click button edit
        accout_btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditVisibility();
                accout_btn_edit.setVisibility(View.GONE);
                accout_btn_edit_done.setVisibility(View.VISIBLE);
            }
        });
        //If edit done
        accout_btn_edit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogConfirm();
            }
        });

        return view;
    }

    //Create dialog confirm
    private void dialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.dialog_confirm_change_account));
        builder.setPositiveButton(getString(R.string.dialog_confirm_change_account_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateValue();
                //Set UI
                setViewVisibility();
                accout_btn_edit_done.setVisibility(View.GONE);
                accout_btn_edit.setVisibility(View.VISIBLE);
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

    //Find view by ID
    private void findView(View view) {
        //Button
        accout_btn_edit = view.findViewById(R.id.accout_btn_edit);
        accout_btn_edit_done = view.findViewById(R.id.accout_btn_edit_done);
        //Edit text
        account_edit_txt_name = view.findViewById(R.id.account_edit_txt_name);
        account_edit_txt_phone = view.findViewById(R.id.account_edit_txt_phone);
        account_edit_txt_email = view.findViewById(R.id.account_edit_txt_email);
        account_edit_txt_address = view.findViewById(R.id.account_edit_txt_address);
        //Text view
        account_txt_name = view.findViewById(R.id.account_txt_name);
        account_txt_phone = view.findViewById(R.id.account_txt_phone);
        account_txt_email = view.findViewById(R.id.account_txt_email);
        account_txt_address = view.findViewById(R.id.account_txt_address);
        account_img_avatar = view.findViewById(R.id.account_img_avatar);
        //Spinner
        account_spinner_gender = view.findViewById(R.id.account_spinner_gender);
        ArrayList<String> gender = new ArrayList<String>();
        gender.add(getString(R.string.default_choose_gender));
        gender.add(getString(R.string.default_gender_male));
        gender.add(getString(R.string.default_gender_female));
        ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, gender);
        account_spinner_gender.setAdapter(genderAdapter);
        account_spinner_gender.setEnabled(false);
    }

    //Set edit text visible
    private void setEditVisibility() {
        //Edit text
        account_edit_txt_name.setVisibility(View.VISIBLE);
        account_edit_txt_phone.setVisibility(View.VISIBLE);
        account_edit_txt_email.setVisibility(View.VISIBLE);
        account_edit_txt_address.setVisibility(View.VISIBLE);
        account_spinner_gender.setEnabled(true);
        //Text view
        account_txt_name.setVisibility(View.GONE);
        account_txt_phone.setVisibility(View.GONE);
        account_txt_email.setVisibility(View.GONE);
        account_txt_address.setVisibility(View.GONE);
    }

    //Set view visible
    private void setViewVisibility() {
        //Edit text
        account_edit_txt_name.setVisibility(View.GONE);
        account_edit_txt_phone.setVisibility(View.GONE);
        account_edit_txt_email.setVisibility(View.GONE);
        account_edit_txt_address.setVisibility(View.GONE);
        account_spinner_gender.setEnabled(false);
        //Text view
        account_txt_name.setVisibility(View.VISIBLE);
        account_txt_phone.setVisibility(View.VISIBLE);
        account_txt_email.setVisibility(View.VISIBLE);
        account_txt_address.setVisibility(View.VISIBLE);
    }

    //Get data for UI
    private void getDataForUI() {
        //Set spinner
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
                        account_txt_name.setText(mAccount.getName());
                        account_edit_txt_name.setText(mAccount.getName());
                    } else {
                        account_txt_name.setText(getString(R.string.default_empty_name));
                        account_edit_txt_name.setHint(getString(R.string.default_empty_name));
                    }

                    //Set gender
                    if (mAccount.getGender() == 1) {
                        account_spinner_gender.setSelection(1);
                    } else if (mAccount.getGender() == 2) {
                        account_spinner_gender.setSelection(2);
                    }else{
                        account_spinner_gender.setSelection(0);
                    }

                    //Set phone
                    if (!mAccount.getPhone().equals("Default")) {
                        account_txt_phone.setText(mAccount.getPhone());
                        account_edit_txt_phone.setText(mAccount.getPhone());
                    } else {
                        account_txt_phone.setText(getString(R.string.default_empty_phone));
                        account_edit_txt_phone.setHint(getString(R.string.default_empty_phone));
                    }

                    //Set email
                    if (!mAccount.getEmail().equals("Default")) {
                        account_txt_email.setText(mAccount.getEmail());
                        account_edit_txt_email.setText(mAccount.getEmail());
                    } else {
                        account_txt_email.setText(getString(R.string.default_empty_email));
                        account_edit_txt_email.setHint(getString(R.string.default_empty_email));
                    }

                    //Set address
                    if (!mAccount.getAddress().equals("Default")) {
                        account_txt_address.setText(mAccount.getAddress());
                        account_edit_txt_address.setText(mAccount.getAddress());
                    } else {
                        account_txt_address.setText(getString(R.string.default_empty_address));
                        account_edit_txt_address.setHint(getString(R.string.default_empty_address));
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

    //Update value to database
    private void updateValue() {
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        //Name
        if (account_edit_txt_name.getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("name").setValue("Default");
            account_txt_name.setText(getString(R.string.default_empty_name));
        } else {
            mRef.child(fUser.getUid()).child("name").setValue(account_edit_txt_name.getText().toString());
            account_txt_name.setText(account_edit_txt_name.getText().toString());
        }

        //Gender
        if (account_spinner_gender.getSelectedItemPosition() == 1) {
            mRef.child(fUser.getUid()).child("gender").setValue(1);
        } else if (account_spinner_gender.getSelectedItemPosition() == 2) {
            mRef.child(fUser.getUid()).child("gender").setValue(2);
        } else{
            mRef.child(fUser.getUid()).child("gender").setValue(-1);
        }

        //Phone
        if (account_edit_txt_phone.getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("phone").setValue("Default");
            account_txt_phone.setText(getString(R.string.default_empty_phone));
        } else {
            mRef.child(fUser.getUid()).child("phone").setValue(account_edit_txt_phone.getText().toString());
            account_txt_phone.setText(account_edit_txt_phone.getText().toString());
        }

        //email
        if (account_edit_txt_email.getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("email").setValue("Default");
            account_txt_email.setText(getString(R.string.default_empty_email));
        } else {
            mRef.child(fUser.getUid()).child("email").setValue(account_edit_txt_email.getText().toString());
            account_txt_email.setText(account_edit_txt_email.getText().toString());
        }

        //address
        if (account_edit_txt_address.getText().toString().equals("")) {
            mRef.child(fUser.getUid()).child("address").setValue("Default");
            account_txt_address.setText(getString(R.string.default_empty_address));
        } else {
            mRef.child(fUser.getUid()).child("address").setValue(account_edit_txt_address.getText().toString());
            account_txt_address.setText(account_edit_txt_address.getText().toString());
        }
    }
}