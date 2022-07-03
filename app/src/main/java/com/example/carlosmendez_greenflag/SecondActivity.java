package com.example.carlosmendez_greenflag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    boolean validEmail;
    boolean validPassword;

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
            if(validEmail && validPassword)
            {
                registerAccount();
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                validEmail = validateEmail();
                notEmpty();
            }
        });
        etPasswordCreate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                notEmpty();
            }
        });

        etPasswordRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                validPassword = validatePassword();
                notEmpty();
            }
        });
    }

    void notEmpty(){
        if(etEmail.getText().toString().length() > 0){
            if(etPasswordCreate.getText().toString().length() > 0){
                if(etPasswordRepeat.getText().toString().length() > 0){
                    btnNext.setEnabled(true);
                    btnNext.animate();
                    btnNext.setColorFilter(tintColorDefault);
                    return;
                }
            }
        }
        btnNext.setEnabled(false);
        btnNext.setColorFilter(tintColorUnavailable);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("EMAIL_KEY", etEmail.getText().toString());
        outState.putString("PASSWORDC_KEY", etPasswordCreate.getText().toString());
        outState.putString("PASSWORDR_KEY", etPasswordRepeat.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        etEmail.setText(savedInstanceState.getString("EMAIL_KEY"));
        etPasswordCreate.setText(savedInstanceState.getString("PASSWORDC_KEY"));
        etPasswordRepeat.setText(savedInstanceState.getString("PASSWORDR_KEY"));
    }

    void registerAccount(){
        UserModel userModel = null;
        try {
            userModel = new UserModel(-1, etEmail.getText().toString(), etPasswordCreate.getText().toString());
        }
        catch (Exception e){
            Toast.makeText(SecondActivity.this, "User cannot be created", Toast.LENGTH_SHORT).show();
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(SecondActivity.this, "greenFlag.db", null, 1);
        boolean success = databaseHelper.addUser(userModel);

        if(success){
            Toast.makeText(SecondActivity.this, "A new user has been created" + success , Toast.LENGTH_SHORT).show();
        }
        else{
            etEmail.setBackground(getDrawable(R.drawable.borderline_rw));
            etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null,null);
            tvErrorEmail.setText(getString(R.string.error_email1));
            tvErrorEmail.setVisibility(View.VISIBLE);
        }

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
            tvErrorEmail.setText(getString(R.string.error_email2));
            tvErrorEmail.setVisibility(View.VISIBLE);
        }

        return result;
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
                etPasswordRepeat.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getDrawable(R.drawable.tick),null);
                tvErrorPassword.setVisibility(View.GONE);
            }
            else {
                etPasswordRepeat.setBackground(getDrawable(R.drawable.borderline_rw));
                etPasswordRepeat.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null,null);
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