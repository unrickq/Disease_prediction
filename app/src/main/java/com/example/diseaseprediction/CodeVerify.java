package com.example.diseaseprediction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diseaseprediction.object.Account;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class CodeVerify extends AppCompatActivity {

    private Account mAccount;
    private DatabaseReference mRef;
    private FirebaseUser fUser;

  private TextInputLayout code_verify_edit_txt_code_layout;
  private ImageView code_verify_img_next;
  private ImageView code_verify_img_back;
  private TextView code_verify_re_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_code_verify);

      findView();

      // Edit Text
      code_verify_edit_txt_code_layout.getEditText().addTextChangedListener(clearErrorOnTyping());

      //Next button
      code_verify_img_next.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (checkCodeInput()) {
            Intent intent = new Intent(CodeVerify.this, MainActivity.class);
            startActivity(intent);
          }
        }
      });

      //Back button
      code_verify_img_back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          onBackPressed();
        }
      });

      //Resend code
      code_verify_re_send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
      });
    }

    /**
     * Find view by ID
     */
    private void findView() {
      code_verify_img_next = findViewById(R.id.code_verify_img_next);
      code_verify_img_back = findViewById(R.id.code_verify_img_back);
      code_verify_edit_txt_code_layout = findViewById(R.id.code_verify_edit_txt_code_layout);
      code_verify_re_send = findViewById(R.id.code_verify_re_send);
    }

  /**
   * Check empty edit text and spinner
   * Error: 1 | Normal: 0
   * @return
   */
//    private int checkEmpty() {
//        if (!code_verify_edit_txt_code.getText().toString().equals("")) {
//            //Default color
//            code_verify_edit_txt_code.setHintTextColor(Color.rgb(128, 128, 128));
//            return 0;
//        } else {
//            //Set hint error message
//            code_verify_edit_txt_code.setHint(getString(R.string.default_empty_code));
//            //Set color warning
//            code_verify_edit_txt_code.setHintTextColor(Color.RED);
//            return 1;
//        }
//    }

  /**
   * Check phone number input
   */
  private boolean checkCodeInput() {
    // Check input empty
    if (code_verify_edit_txt_code_layout.getEditText().getText().toString().isEmpty()) {
      code_verify_edit_txt_code_layout.setError(getString(R.string.error_field_empty));
      return false;
    }
    // TODO: Check code mismatch
//        else if ()

    // Verify code correct
    else {
      return true;
    }
  }

  /**
   * Create a {@link TextWatcher} to clear {@link TextInputLayout} error notification
   *
   * @return a {@link TextWatcher}
   */
  private TextWatcher clearErrorOnTyping() {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        code_verify_edit_txt_code_layout.setError(null);
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    };
  }
}