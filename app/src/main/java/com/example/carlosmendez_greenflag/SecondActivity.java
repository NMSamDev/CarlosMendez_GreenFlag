package com.example.carlosmendez_greenflag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecondActivity extends AppCompatActivity {

    ImageButton btnBack;
    ImageButton btnNext;
    EditText etEmail;
    EditText etPasswordCreate;
    EditText etPasswordRepeat;
    TextView tvErrorEmail;
    TextView tvErrorPassword;

    int tintColorUnavailable = Color.argb(80, 0, 0, 0);
    int tintColorDefault = Color.argb(0, 0, 0, 0);

    public static final String EMAIL_STRING = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    public static final String PASSWORD_STRING = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d\\-\\]\\[\\\\{}/;:=<>_+.,#@!%*$?&]{8,30}$";
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(EMAIL_STRING, Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile(PASSWORD_STRING);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        /**
         * Find Views
         */
        // Button
        btnBack = findViewById(R.id.goto_main);
        btnNext = findViewById(R.id.btn_next);

        // EditText
        etEmail = findViewById(R.id.et_email);
        etPasswordCreate = findViewById(R.id.et_password1);
        etPasswordRepeat = findViewById(R.id.et_password2);

        // TextView
        tvErrorEmail = findViewById(R.id.tv_error_email);
        tvErrorPassword = findViewById(R.id.tv_error_password);

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        btnNext.setOnClickListener(view -> {
            if(validateEmail() && validatePassword())
            {
                registerAccount();
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                notEmpty();
            }
        });

        etPasswordCreate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                notEmpty();
            }
        });

        etPasswordRepeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                notEmpty();
            }
        });
    }

    void notEmpty(){
        if(etEmail.getText().toString().length() > 0){
            if(etPasswordCreate.getText().toString().length() > 0){
                if(etPasswordRepeat.getText().toString().length() > 0){
                    btnNext.setEnabled(true);
                    btnNext.setColorFilter(tintColorDefault);
                    return;
                }
            }
        }
        btnNext.setEnabled(false);
        btnNext.setColorFilter(tintColorUnavailable);
    }

    boolean validateEmail(){
        boolean result;
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(etEmail.getText().toString());
        result = matcher.find();

        if(result){
            etEmail.setBackground(getDrawable(R.drawable.borderline_gw));
            etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getDrawable(R.drawable.tick),null);
            tvErrorEmail.setVisibility(View.GONE);
        }
        else{
            etEmail.setBackground(getDrawable(R.drawable.borderline_rw));
            etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null,null);
            tvErrorEmail.setVisibility(View.VISIBLE);
        }

        return result;
    }

    void registerAccount(){
        Toast toast = new Toast(this);
        toast.setText("Next");
        toast.show();
    }

    boolean validatePassword(){
        boolean result;

        Matcher matcher = VALID_PASSWORD_REGEX.matcher(etPasswordCreate.getText().toString());
        result = matcher.find();

        if(result){
            etPasswordCreate.setBackground(getDrawable(R.drawable.borderline_gw));
            etPasswordCreate.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getDrawable(R.drawable.tick),null);
            tvErrorPassword.setVisibility(View.GONE);

            if(etPasswordRepeat.getText().toString().equals(etPasswordCreate.getText().toString())){
                etPasswordRepeat.setBackground(getDrawable(R.drawable.borderline_gw));
                tvErrorPassword.setVisibility(View.GONE);
            }
            else {
                etPasswordRepeat.setBackground(getDrawable(R.drawable.borderline_rw));
                tvErrorPassword.setText(getString(R.string.error_password2));
                tvErrorPassword.setVisibility(View.VISIBLE);
                result = false;
            }
        }
        else{
            etPasswordCreate.setBackground(getDrawable(R.drawable.borderline_rw));
            etPasswordCreate.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null,null);
            tvErrorPassword.setText(getString(R.string.error_password1));
            tvErrorPassword.setVisibility(View.VISIBLE);
        }

        return result;
    }

}