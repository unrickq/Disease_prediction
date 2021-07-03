package com.example.diseaseprediction.ui.account;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.diseaseprediction.MainActivity;
import com.example.diseaseprediction.R;
import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.DoctorInfo;
import com.example.diseaseprediction.object.DoctorSpecialization;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

  private static final String TAG = "AccountFragment";
  private final int PICK_IMAGE_REQUEST = 71;

  //Firebase
  private StorageReference sRef;
  private DatabaseReference mRef;
  private FirebaseUser fUser;

  private Account mAccount;
  private DoctorInfo mDoctor;
  private DoctorSpecialization ds;
  private Uri imgPath;
  private ArrayAdapter<DoctorSpecialization> specializationAdapter;
  private ArrayList<DoctorSpecialization> specialization;
  private ArrayAdapter genderAdapter;

  private TextInputLayout account_txt_title_name, account_txt_title_gender, account_txt_title_phone,
      account_txt_title_email, account_txt_title_address,
      account_doctor_txt_title_experience, account_doctor_txt_title_description,
      account_doctor_txt_title_specialization;
  private AutoCompleteTextView account_spinner_gender, account_doctor_spinner_specialization;
  //    private TextView nav_header_txt_acc_name, nav_header_txt_acc_phone;
  private TextView account_img_upload, account_txt_name, account_txt_gender, account_txt_phone, account_txt_email,
      account_txt_address, account_doctor_txt_specialization, account_doctor_txt_experience,
      account_doctor_txt_description;

  private LinearLayout account_layout_doctor;
  private Button accout_btn_edit, accout_btn_edit_done;
  private CircleImageView account_img_avatar, nav_header_avatar;


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
    account_img_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

//                dialogConfirmUploadImg();
      }
    });

    account_img_upload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
      }
    });

    return view;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //Open file image mobile
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
        && data != null && data.getData() != null) {
      imgPath = data.getData();
      uploadImage();
    }
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
      if (account_layout_doctor.getVisibility() == View.VISIBLE) {
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
    builder.setPositiveButton(getString(R.string.dialog_confirm_change_account_yes),
        new DialogInterface.OnClickListener() {
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
   * Create dialog confirm
   * Dialog show when data on change
   */
  private void dialogConfirmUploadImg() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage(getString(R.string.dialog_confirm_change_img));
    builder.setPositiveButton(getString(R.string.dialog_confirm_change_account_yes),
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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
  private void findViews(View view) {
    account_layout_doctor = view.findViewById(R.id.account_layout_doctor);
    //Button
    accout_btn_edit = view.findViewById(R.id.account_btn_edit);
    accout_btn_edit_done = view.findViewById(R.id.account_btn_edit_done);

    //Find view
    account_txt_name = view.findViewById(R.id.account_txt_name);
    account_txt_title_name = view.findViewById(R.id.account_txt_title_name);
    account_txt_gender = view.findViewById(R.id.account_txt_gender);
    account_txt_title_gender = view.findViewById(R.id.account_txt_title_gender);
    account_txt_phone = view.findViewById(R.id.account_txt_phone);
    account_txt_title_phone = view.findViewById(R.id.account_txt_title_phone);
    account_txt_email = view.findViewById(R.id.account_txt_email);
    account_txt_title_email = view.findViewById(R.id.account_txt_title_email);
    account_txt_address = view.findViewById(R.id.account_txt_address);
    account_txt_title_address = view.findViewById(R.id.account_txt_title_address);
    account_img_avatar = view.findViewById(R.id.account_img_avatar);
    account_img_upload = view.findViewById(R.id.account_img_upload);

    account_doctor_txt_experience = view.findViewById(R.id.account_doctor_txt_experience);
    account_doctor_txt_title_experience = view.findViewById(R.id.account_doctor_txt_title_experience);
    account_doctor_txt_description = view.findViewById(R.id.account_doctor_txt_description);
    account_doctor_txt_title_description = view.findViewById(R.id.account_doctor_txt_title_description);
    account_doctor_txt_specialization = view.findViewById(R.id.account_doctor_txt_specialization);
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
    mAccount = new Account();
    mRef = FirebaseDatabase.getInstance().getReference("Accounts").child(fUser.getUid());
    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        mAccount = snapshot.getValue(Account.class);

        // user log in with phone => can edit mail
        if (mAccount.getTypeLogin() == 0) {
          account_txt_phone.setVisibility(View.GONE);
          account_txt_title_phone.setVisibility(View.VISIBLE);

          account_txt_email.setVisibility(View.GONE);
          account_txt_title_email.setVisibility(View.VISIBLE);
          account_txt_title_email.setEnabled(true);
        } else {
          account_txt_email.setVisibility(View.GONE);
          account_txt_title_email.setVisibility(View.VISIBLE);

          account_txt_phone.setVisibility(View.GONE);
          account_txt_title_phone.setVisibility(View.VISIBLE);
          account_txt_title_phone.setEnabled(true);
        }
      }

      @Override
      public void onCancelled(@NonNull @NotNull DatabaseError error) {

      }
    });
    // Hide TextViews
    account_txt_name.setVisibility(View.GONE);
    account_txt_gender.setVisibility(View.GONE);
    account_txt_address.setVisibility(View.GONE);

    account_doctor_txt_experience.setVisibility(View.GONE);
    account_doctor_txt_description.setVisibility(View.GONE);
    account_doctor_txt_specialization.setVisibility(View.GONE);

    // Expose edit texts
    account_txt_title_name.setVisibility(View.VISIBLE);
    account_txt_title_gender.setVisibility(View.VISIBLE);
    account_txt_title_address.setVisibility(View.VISIBLE);

    account_doctor_txt_title_experience.setVisibility(View.VISIBLE);
    account_doctor_txt_title_description.setVisibility(View.VISIBLE);
    account_doctor_txt_title_specialization.setVisibility(View.VISIBLE);

    account_img_upload.setVisibility(View.VISIBLE);

    //Enable Edit text
    account_txt_title_name.setEnabled(true);
    account_txt_title_gender.setEnabled(true);
    account_txt_title_address.setEnabled(true);

    account_doctor_txt_title_experience.setEnabled(true);
    account_doctor_txt_title_description.setEnabled(true);
    account_doctor_txt_title_specialization.setEnabled(true);
  }

  /**
   * Disable edit
   */
  private void disableEdit() {
    // Expose TextViews
    account_txt_name.setVisibility(View.VISIBLE);
    account_txt_gender.setVisibility(View.VISIBLE);
    account_txt_address.setVisibility(View.VISIBLE);
    account_txt_phone.setVisibility(View.VISIBLE);
    account_txt_email.setVisibility(View.VISIBLE);

    account_doctor_txt_experience.setVisibility(View.VISIBLE);
    account_doctor_txt_description.setVisibility(View.VISIBLE);
    account_doctor_txt_specialization.setVisibility(View.VISIBLE);

    // Hide edit texts
    account_txt_title_name.setVisibility(View.GONE);
    account_txt_title_gender.setVisibility(View.GONE);
    account_txt_title_address.setVisibility(View.GONE);
    account_txt_title_phone.setVisibility(View.GONE);
    account_txt_title_email.setVisibility(View.GONE);

    account_doctor_txt_title_experience.setVisibility(View.GONE);
    account_doctor_txt_title_description.setVisibility(View.GONE);
    account_doctor_txt_title_specialization.setVisibility(View.GONE);
    account_img_upload.setVisibility(View.GONE);

    // disable edit text
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
              account_txt_name.setText(Html.fromHtml(getString(R.string.account_txt_title_name_format, name),
                  FROM_HTML_MODE_LEGACY));
              account_txt_title_name.getEditText().setText(name);
            }

            //Set gender
            int gender = mAccount.getGender();
            String genderStr = account_spinner_gender.getAdapter().getItem(gender).toString();
            account_txt_gender.setText(Html.fromHtml(getString(R.string.account_txt_title_gender_format, genderStr),
                FROM_HTML_MODE_LEGACY));
            account_spinner_gender.setText(genderStr);
            genderAdapter.getFilter().filter(null);

//            if (mAccount.getGender() == 0) {
//              account_txt_gender.setText(account_spinner_gender.getAdapter().getItem(0).toString());
//              account_spinner_gender.setText(account_spinner_gender.getAdapter().getItem(0).toString());
//              genderAdapter.getFilter().filter(null);
//            } else if (mAccount.getGender() == 1) {
//              account_spinner_gender.setText(account_spinner_gender.getAdapter().getItem(1).toString());
//              genderAdapter.getFilter().filter(null);
//            }

            //Set phone
            if (!mAccount.getPhone().equals("Default")) {
              String phone = mAccount.getPhone();
              account_txt_phone.setText(Html.fromHtml(getString(R.string.account_txt_title_phone_format, phone),
                  FROM_HTML_MODE_LEGACY));
              account_txt_title_phone.getEditText().setText(phone);
            }

            //Set email
            if (!mAccount.getEmail().equals("Default")) {
              String email = mAccount.getEmail();
              account_txt_email.setText(Html.fromHtml(getString(R.string.account_txt_title_email_format, email),
                  FROM_HTML_MODE_LEGACY));
              account_txt_title_email.getEditText().setText(email);
            }

            //Set address
            if (!mAccount.getAddress().equals("Default")) {
              String address = mAccount.getAddress();
              account_txt_address.setText(Html.fromHtml(getString(R.string.account_txt_title_address_format, address),
                      FROM_HTML_MODE_LEGACY));
              account_txt_title_address.getEditText().setText(address);
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
      //Set nav bar
//            nav_header_txt_acc_name = getActivity().findViewById(R.id.nav_header_txt_acc_name);
//            nav_header_txt_acc_name.setText(account_txt_title_name.getEditText().getText().toString());
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
      //Set nav bar
//            nav_header_txt_acc_phone = getActivity().findViewById(R.id.nav_header_txt_acc_phone);
//            nav_header_txt_acc_phone.setText(account_txt_title_phone.getEditText().getText().toString());
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

    if (account_layout_doctor.getVisibility() == View.VISIBLE) {
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
                      account_doctor_txt_specialization.setText(Html.fromHtml(getString(R.string.account_info_doctor_specialization_format, specialization),
                          FROM_HTML_MODE_LEGACY));
                      account_doctor_spinner_specialization.setText(specialization);
                    }
                    specialization.add(ds);
                  } catch (NullPointerException e) {
                    Log.d(TAG, "Account. loadDataOfDoctor", e);
                  }
                }
                //Set spinner
                specializationAdapter = new ArrayAdapter<DoctorSpecialization>(getContext(),
                    R.layout.support_simple_spinner_dropdown_item, specialization);
                account_doctor_spinner_specialization.setAdapter(specializationAdapter);
              }

              @Override
              public void onCancelled(@NonNull @NotNull DatabaseError error) {

              }

            });


            if (mDoctor.getExperience() != -1) {
              String experience = String.valueOf(mDoctor.getExperience());
              account_doctor_txt_experience.setText(Html.fromHtml(getString(R.string.account_info_doctor_experience_format, experience),
                  FROM_HTML_MODE_LEGACY));
              account_doctor_txt_title_experience.getEditText().setText(experience);
            }

            if (!mDoctor.getShortDescription().equals("Default")) {
              String description = mDoctor.getShortDescription();
              account_doctor_txt_description.setText(Html.fromHtml(getString(R.string.account_info_doctor_description_format, description),
                  FROM_HTML_MODE_LEGACY));
              account_doctor_txt_title_description.getEditText().setText(description);
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
            try {
              assert ds != null;
              if (ds.getName().equals(account_doctor_spinner_specialization.getText().toString())) {
                mRef = FirebaseDatabase.getInstance().getReference("DoctorInfo");
                mRef.child(fUser.getUid()).child("specializationID").setValue(ds.getSpecializationID());
              }
            } catch (NullPointerException e) {
              Log.d(TAG, "Account. saveDataOfDoctor", e);
            }


          }
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }

      });
    }

  }

  /**
   * Upload file img to storage firebase
   */
  private void uploadImage() {
    if (imgPath != null) {
      final ProgressDialog progressDialog = new ProgressDialog(getContext());
      progressDialog.setTitle(getString(R.string.upload_img_waiting));
      progressDialog.show();

      //Get reference "images" in storage firebase
      sRef = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
      sRef.putFile(imgPath)
          .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              progressDialog.dismiss();
              Toast.makeText(getContext(), getString(R.string.upload_img_done), Toast.LENGTH_SHORT).show();
              //Get url of image in storage firebase
              sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                  mRef = FirebaseDatabase.getInstance().getReference("Accounts");
                  mRef.child(fUser.getUid()).child("image").setValue(uri.toString());
                  Glide.with(AccountFragment.this).load(uri.toString()).into(account_img_avatar);
                  //Set nav bar
                  nav_header_avatar = getActivity().findViewById(R.id.nav_header_avatar);
                  Glide.with(AccountFragment.this).load(uri.toString()).into(nav_header_avatar);
                }
              });
            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              progressDialog.dismiss();
              Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          })
          .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
              double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                  .getTotalByteCount());
              progressDialog.setMessage("Uploaded " + (int) progress + "%");
            }
          });
    }
  }
}