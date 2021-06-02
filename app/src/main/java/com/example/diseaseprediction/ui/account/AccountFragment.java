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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

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

    private int statusEditTextChange = 0;

    private Button accout_btn_edit, accout_btn_edit_done;
    private EditText account_edit_txt_name, account_edit_txt_gender, account_edit_txt_phone, account_edit_txt_email, account_edit_txt_address;
    private TextView account_txt_name, account_txt_gender, account_txt_phone, account_txt_email, account_txt_address;
    private CircleImageView account_img_avatar;

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
        //Set change event on edit text
        onTextEditChange();

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
                if (statusEditTextChange ==1){
                    //Button confirm "yes"
                    dialogConfirm();
                }else{
                    //Button confirm "no"
                }
            }
        });

        return view;
    }

    //Create dialog confirm
    private void dialogConfirm(){
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
        account_edit_txt_gender = view.findViewById(R.id.account_edit_txt_gender);
        account_edit_txt_phone = view.findViewById(R.id.account_edit_txt_phone);
        account_edit_txt_email = view.findViewById(R.id.account_edit_txt_email);
        account_edit_txt_address = view.findViewById(R.id.account_edit_txt_address);
        //Text view
        account_txt_name = view.findViewById(R.id.account_txt_name);
        account_txt_gender = view.findViewById(R.id.account_txt_gender);
        account_txt_phone = view.findViewById(R.id.account_txt_phone);
        account_txt_email = view.findViewById(R.id.account_txt_email);
        account_txt_address = view.findViewById(R.id.account_txt_address);
        account_img_avatar = view.findViewById(R.id.account_img_avatar);
    }

    //Set edit text visible
    private void setEditVisibility(){
        //Edit text
        account_edit_txt_name.setVisibility(View.VISIBLE);
        account_edit_txt_gender.setVisibility(View.VISIBLE);
        account_edit_txt_phone.setVisibility(View.VISIBLE);
        account_edit_txt_email.setVisibility(View.VISIBLE);
        account_edit_txt_address.setVisibility(View.VISIBLE);
        //Text view
        account_txt_name.setVisibility(View.GONE);
        account_txt_gender.setVisibility(View.GONE);
        account_txt_phone.setVisibility(View.GONE);
        account_txt_email.setVisibility(View.GONE);
        account_txt_address.setVisibility(View.GONE);
    }

    //Set view visible
    private void setViewVisibility(){
        //Edit text
        account_edit_txt_name.setVisibility(View.GONE);
        account_edit_txt_gender.setVisibility(View.GONE);
        account_edit_txt_phone.setVisibility(View.GONE);
        account_edit_txt_email.setVisibility(View.GONE);
        account_edit_txt_address.setVisibility(View.GONE);
        //Text view
        account_txt_name.setVisibility(View.VISIBLE);
        account_txt_gender.setVisibility(View.VISIBLE);
        account_txt_phone.setVisibility(View.VISIBLE);
        account_txt_email.setVisibility(View.VISIBLE);
        account_txt_address.setVisibility(View.VISIBLE);
    }

    //Set event on edit text
    private void onTextEditChange(){
        account_edit_txt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()!=i2){
                    statusEditTextChange = 1;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                account_txt_name.setText(account_edit_txt_name.getText().toString());

            }
        });
        account_edit_txt_gender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()!=i2){
                    statusEditTextChange = 1;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                account_txt_gender.setText(account_edit_txt_gender.getText().toString());

            }
        });
        account_edit_txt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()!=i2){
                    statusEditTextChange = 1;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                account_txt_phone.setText(account_edit_txt_phone.getText().toString());

            }
        });
        account_edit_txt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()!=i2){
                    statusEditTextChange = 1;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                account_txt_email.setText(account_edit_txt_email.getText().toString());

            }
        });
        account_edit_txt_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()!=i2){
                    statusEditTextChange = 1;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                account_txt_address.setText(account_edit_txt_address.getText().toString());

            }
        });;
    }

    //Get data for UI
    private void getDataForUI() {
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
                    String gender = getString(R.string.default_empty_gender);
                    String phone = getString(R.string.default_empty_phone);
                    String address = getString(R.string.default_empty_address);
                    String email = getString(R.string.default_empty_address);
                    String name = getString(R.string.default_empty_address);
                    String imgURL = "";

                    if (!mAccount.getName().equals("Default")) {
                        name = mAccount.getName();
                    }
                    if (mAccount.getGender() != -1) {
                        if (mAccount.getGender() == 1) {
                            gender = getString(R.string.default_gender_male);
                        } else {
                            gender = getString(R.string.default_gender_female);
                        }
                    }
                    if (!mAccount.getPhone().equals("Default")) {
                        phone = mAccount.getPhone();
                    }
                    if (!mAccount.getEmail().equals("Default")) {
                        email = mAccount.getEmail();
                    }
                    if (!mAccount.getAddress().equals("Default")) {
                        address = mAccount.getAddress();
                    }

                    account_txt_name.setText(name);
                    account_txt_gender.setText(gender);
                    account_txt_phone.setText(phone);
                    account_txt_email.setText(email);
                    account_txt_address.setText(address);

                    account_edit_txt_name.setText(name);
                    account_edit_txt_gender.setText(gender);
                    account_edit_txt_phone.setText(phone);
                    account_edit_txt_email.setText(email);
                    account_edit_txt_address.setText(address);

                    //set image
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
    private void updateValue(){
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");

        if (account_txt_name.getText().toString().equals(getString(R.string.default_empty_name))) {
            mRef.child(fUser.getUid()).child("name").setValue("Default");
        }else{
            mRef.child(fUser.getUid()).child("name").setValue(account_txt_name.getText().toString());
        }

        //Gender
        if (account_txt_gender.getText().toString().equals(getString(R.string.default_empty_gender))) {
            mRef.child(fUser.getUid()).child("gender").setValue(-1);
        }else{
            mRef.child(fUser.getUid()).child("gender").setValue(account_txt_gender.getText().toString());
        }

        //Phone
        if (account_txt_phone.getText().toString().equals(getString(R.string.default_empty_phone))) {
            mRef.child(fUser.getUid()).child("phone").setValue("Default");
        }else{
            mRef.child(fUser.getUid()).child("phone").setValue(account_txt_phone.getText().toString());
        }

        //email
        if (account_txt_email.getText().toString().equals(getString(R.string.default_empty_email))) {
            mRef.child(fUser.getUid()).child("email").setValue("Default");
        }else{
            mRef.child(fUser.getUid()).child("email").setValue(account_txt_email.getText().toString());
        }

        //address
        if (account_txt_address.getText().toString().equals(getString(R.string.default_empty_address))) {
            mRef.child(fUser.getUid()).child("address").setValue("Default");
        }else{
            mRef.child(fUser.getUid()).child("address").setValue(account_txt_address.getText().toString());
        }
    }
}