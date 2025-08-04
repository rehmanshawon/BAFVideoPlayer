package com.kopotron.bafvideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.kopotron.bafvideoplayer.Utilities.loadUserInfo;
import static com.kopotron.bafvideoplayer.Utilities.saveUserInfo;

public class BaseActivity extends AppCompatActivity {

    private Handler mHandler;
    private final long TIMEOUT = 30000;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            reactToIdleState();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mHandler = new Handler();
//        mHandler.postDelayed(mRunnable, TIMEOUT);
        baseOnCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, TIMEOUT);
        super.onResume();
        baseOnResume();
    }

    @Override
    protected void onPause() {
        mHandler.removeCallbacks(mRunnable);
        super.onPause();
    }

    protected  void  baseOnResume(){

    }

    protected void baseOnCreate(Bundle savedInstanceState) {

    }

    protected void reactToIdleState() {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        resetHandler();
        return super.onTouchEvent(event);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        resetHandler();
    }

    private void resetHandler() {
        Log.d("baseActivity","touched");
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, TIMEOUT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.system_menu, menu);
        inflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_login:
            case R.id.menu_login:
                try {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    //showDialog("Copyright (c) 2021 Bangladesh Air Force.\nbaf.mil.bd","App Copyright");
                    Log.d("Login","Login as Admin");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.action_logout:
            case R.id.menu_logout:
                UserInfo userInfo=loadUserInfo(this);
                userInfo.setLoggedIn(false);
                saveUserInfo(this,userInfo);
                finish();
                startActivity(getIntent());
                return true;
            case R.id.action_about:
            case R.id.about_item:
                try {
                    //showDialog("Copyright (c) 2021 Bangladesh Air Force.\nbaf.mil.bd","App Copyright");
                    Log.d("About","Copyright (c) 2021 Bangladesh Air Force.\nbaf.mil.bd");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.action_developer:
            case R.id.developer_item:
                try {
                    // showDialog("Kopotron Corporation.\nserver.kopotron.com","App Developer");
                    Log.d("Developer","Rehman Shawon");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
