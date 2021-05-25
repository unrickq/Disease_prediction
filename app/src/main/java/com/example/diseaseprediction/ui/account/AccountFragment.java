package com.example.diseaseprediction.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private TextView account_txt_name, account_txt_gender, account_txt_phone, account_txt_email, account_txt_address;
    private CircleImageView account_img_avatar;
    private Account mAccount;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


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
        getUI(view);
        return view;
    }

    private void getUI(View view) {
        //get user by id
        mAccount = new Account();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Accounts");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check user exist in firebase
                //if exist then set UI
                if (snapshot.hasChild(user.getUid())) {
                    mAccount = snapshot.child(user.getUid()).getValue(Account.class);
                    String gender = getString(R.string.default_empty_gender);
                    String phone = getString(R.string.default_empty_phone);
                    String address = getString(R.string.default_empty_address);
                    String email = getString(R.string.default_empty_address);
                    String name = getString(R.string.default_empty_address);
                    String imgURL = "";

                    //Set text navigation header
                    account_txt_name = view.findViewById(R.id.account_txt_name);
                    account_txt_gender = view.findViewById(R.id.account_txt_gender);
                    account_txt_phone = view.findViewById(R.id.account_txt_phone);
                    account_txt_email = view.findViewById(R.id.account_txt_email);
                    account_txt_address = view.findViewById(R.id.account_txt_address);
                    account_img_avatar = view.findViewById(R.id.account_img_avatar);

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
                    //set image
                    if (!mAccount.getImage().equals("Default")){
                        Glide.with(AccountFragment.this).load(mAccount.getImage()).into(account_img_avatar);
                    }else{
                        Glide.with(AccountFragment.this).load(R.drawable.background_avatar).into(account_img_avatar);
                    }

                } else {
                    System.out.println("k tim ra");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}