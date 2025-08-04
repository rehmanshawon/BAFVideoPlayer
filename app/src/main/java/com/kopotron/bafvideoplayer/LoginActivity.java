package com.kopotron.bafvideoplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.kopotron.bafvideoplayer.Utilities.loadUserInfo;
import static com.kopotron.bafvideoplayer.Utilities.saveUserInfo;

public class LoginActivity extends AppCompatActivity {
    Button buttonLogin,btnChange;
    EditText editTextName, editTextPassword;
    UserInfo userInfo;
    private static Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.context = getApplicationContext();
        buttonLogin = (Button)findViewById(R.id.BT_LOGIN);
        editTextName = (EditText)findViewById(R.id.ET_NAME);
        editTextPassword = (EditText)findViewById(R.id.ET_PASSWORD);
        btnChange=findViewById(R.id.BT_CHANGE);

       // userInfo=new UserInfo("admin","admin",false);
       // saveUserInfo(context,userInfo);
        userInfo=loadUserInfo(context);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextName.getText().toString().equals(userInfo.getUserName()) &&
                        editTextPassword.getText().toString().equals(userInfo.getPassword())) {
                    userInfo.setLoggedIn(true);
                    saveUserInfo(context,userInfo);
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AircraftListActivity.class);
                   // intent.putExtra("loggedIn", "true");
                    startActivity(intent);
                    finish();
                }else{
                   // Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show());
            }
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ActivityChangePassword.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
