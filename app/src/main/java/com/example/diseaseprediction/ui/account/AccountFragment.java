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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";
    private Account mAccount;
    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private TextInputLayout account_txt_title_name, account_txt_title_gender, account_txt_title_phone, account_txt_title_email, account_txt_title_address;
    private AutoCompleteTextView account_spinner_gender;
    ArrayAdapter genderAdapter;

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
     * @return true if all input valid
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
     * @param view
     */
    private void findView(View view) {
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

        //Set event for layout
        account_txt_title_name.getEditText().addTextChangedListener(clearErrorOnTyping(account_txt_title_name));
        account_txt_title_gender.getEditText().addTextChangedListener(clearErrorOnTyping(account_txt_title_gender));
        account_txt_title_phone.getEditText().addTextChangedListener(clearErrorOnTyping(account_txt_title_phone));
        account_txt_title_email.getEditText().addTextChangedListener(clearErrorOnTyping(account_txt_title_email));
        account_txt_title_address.getEditText().addTextChangedListener(clearErrorOnTyping(account_txt_title_address));

        //Spinner
        account_spinner_gender = view.findViewById(R.id.account_spinner_gender);
        //Set data for spinner
        ArrayList<String> gender = new ArrayList<String>();
        gender.add(getString(R.string.default_gender_male));
        gender.add(getString(R.string.default_gender_female));
        gender.add(getString(R.string.default_gender_other));
        genderAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, gender);
        account_spinner_gender.setAdapter(genderAdapter);
    }

    /**
     * Enable edit
     */
    private void enableEdit() {
        //Edit text
        account_txt_title_name.getEditText().setEnabled(true);
        account_txt_title_gender.getEditText().setEnabled(true);
        account_txt_title_phone.getEditText().setEnabled(true);
        account_txt_title_email.getEditText().setEnabled(true);
        account_txt_title_address.getEditText().setEnabled(true);
    }

    /**
     * Disable edit
     */
    private void disableEdit() {
        account_txt_title_name.getEditText().setEnabled(false);
        account_txt_title_gender.getEditText().setEnabled(false);
        account_txt_title_phone.getEditText().setEnabled(false);
        account_txt_title_email.getEditText().setEnabled(false);
        account_txt_title_address.getEditText().setEnabled(false);
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
                    } else if (mAccount.getGender() == 2) {
                        account_spinner_gender.setText(account_spinner_gender.getAdapter().getItem(2).toString());
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
        } else if (account_spinner_gender.getText().toString().equals(account_spinner_gender.getAdapter().getItem(2).toString())) {
            mRef.child(fUser.getUid()).child("gender").setValue(2);
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
    }
}