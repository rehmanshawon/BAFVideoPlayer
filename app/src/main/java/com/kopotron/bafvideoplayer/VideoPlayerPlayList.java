package com.kopotron.bafvideoplayer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.kopotron.bafvideoplayer.Utilities.loadAircraftData;
import static com.kopotron.bafvideoplayer.Utilities.loadImageList;
import static com.kopotron.bafvideoplayer.Utilities.rescanImages;

public class VideoPlayerPlayList extends AppCompatActivity {
    private FloatingActionButton goToAircraftList=null;
    ArrayList<AircraftData> aircraftDataArrayList;
    VideoView videoView;
    private int index = 0;
    private static Intent imageIntent;
    private static Context context;
    private List<String> imageList;
    private List<String> pictureList;
    private MediaPlayer mMediaPlayer;
    private int old_duration=0;

    private int pausedTime=0;

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000; //Delay for 1 seconds.  One second = 1000 milliseconds.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getApplicationContext();
        setContentView(R.layout.activity_video_player);
        goToAircraftList=findViewById(R.id.FAB_GOTO_ACLIST);
        goToAircraftList.setVisibility(View.INVISIBLE);
        imageList = loadImageList(context);
        // create the get Intent object
        Intent intent = getIntent();
        List<String> videoPath = intent.getStringArrayListExtra("videoList");
        index=intent.getIntExtra("index",0);
        ActionBar actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        videoView =(VideoView)findViewById(R.id.videoView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //Set MediaController  to enable play, pause, forward, etc options.
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        //Starting VideView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(videoPath.get(index)));
        videoView.requestFocus();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        prepareImagePlayerListIntent();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer=mp;
                videoView.start();
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //VideoPlayer.super.onTouchEvent(event);
                goToAircraftList.setVisibility(goToAircraftList.getVisibility()==View.VISIBLE?View.INVISIBLE:View.VISIBLE);
                getSupportActionBar().show();
                return false;
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                //mp.release();
                index++;
                if (index == videoPath.size()) {
                    index = 0;
                    if(imageList.size()>0)
                    {
                        context.startActivity(imageIntent);
                        finish();
                    }

                }
                videoView.setVideoURI(Uri.parse(videoPath.get(index)));

            }
        });
        goToAircraftList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = getIntent();
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(context, AircraftListActivity.class);
                //intent.putExtra("loggedIn", "true");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        imageList = loadImageList(context);
        //start handler as activity become visible
        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                //if()
                int duration = videoView.getCurrentPosition();
                if (old_duration == duration ) {
                   // Log.d("VideoPlayer","Paused");
                    pausedTime++;
                    if(pausedTime==10) {
                        mMediaPlayer.start();
                        pausedTime=0;
                    }
                }
                old_duration = duration;
                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

// If onPause() is not included the threads will double up when you
// reload the activity

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    private void prepareImagePlayerListIntent(){
        imageIntent = new Intent(context, ImagePlayerPlayList.class);
        aircraftDataArrayList=loadAircraftData(context);
        imageList=rescanImages(aircraftDataArrayList);
        imageIntent.putStringArrayListExtra("imageList", (ArrayList<String>) imageList);
        imageIntent.putExtra("index",0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
