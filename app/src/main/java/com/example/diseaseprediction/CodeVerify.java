package com.example.diseaseprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diseaseprediction.object.Account;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class CodeVerify extends AppCompatActivity {

    private Account mAccount;
    private DatabaseReference mRef;
    private FirebaseUser fUser;

    private EditText code_verify_edit_txt_code;
    private ImageView code_verify_img_next;
    private TextView code_verify_re_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verify);
        findView();

        code_verify_img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmpty() == 0) {
                    Intent intent = new Intent(CodeVerify.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        code_verify_re_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //Find view by ID
    private void findView() {
        code_verify_img_next = findViewById(R.id.code_verify_img_next);
        code_verify_edit_txt_code = findViewById(R.id.code_verify_edit_txt_code);
        code_verify_re_send = findViewById(R.id.code_verify_re_send);
    }

    //Check empty edit text and spinner
    //Error: 1 | Normal: 0
    private int checkEmpty() {
        if (!code_verify_edit_txt_code.getText().toString().equals("")) {
            //Default color
            code_verify_edit_txt_code.setHintTextColor(Color.rgb(128, 128, 128));
            return 0;
        } else {
            //Set hint error message
            code_verify_edit_txt_code.setHint(getString(R.string.default_empty_code));
            //Set color warning
            code_verify_edit_txt_code.setHintTextColor(Color.RED);
            return 1;
        }
    }
}