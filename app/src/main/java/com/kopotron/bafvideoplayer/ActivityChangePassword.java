package com.kopotron.bafvideoplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.kopotron.bafvideoplayer.Utilities.loadUserInfo;
import static com.kopotron.bafvideoplayer.Utilities.saveUserInfo;

public class ActivityChangePassword extends AppCompatActivity {
    Button buttonSave;
    EditText editTextCurrentPass, editTextNewPass;
    TextView textViewCurrent, textViewNew;
    UserInfo userInfo;
    private static Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);
        this.context = getApplicationContext();
        buttonSave=findViewById(R.id.BT_SAVE_PASS);
        editTextCurrentPass=findViewById(R.id.ET_C_PASS);
        editTextNewPass=findViewById(R.id.ET_N_PASS);
        textViewCurrent=findViewById(R.id.TV_C_PASS);
        textViewNew=findViewById(R.id.TV_N_PASS);

        UserInfo userInfo=loadUserInfo(context);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextCurrentPass.getText().toString().equals(userInfo.getPassword())){
                    if(editTextNewPass.getText().toString().length()>4)
                    {
                        userInfo.setPassword(editTextNewPass.getText().toString());
                        userInfo.setLoggedIn(false);
                        saveUserInfo(context,userInfo);
                        Intent intent=new Intent(context,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        textViewNew.setText("New Password length must be 5 characters at least!");
                    }
                }
                else {
                    textViewCurrent.setText("Current Password is wrong!");
                }
            }
        });
        editTextCurrentPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewCurrent.setText("Type Current Password");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewNew.setText("Type New Password");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
