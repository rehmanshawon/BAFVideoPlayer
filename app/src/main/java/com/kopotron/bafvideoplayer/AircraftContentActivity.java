package com.kopotron.bafvideoplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import static com.kopotron.bafvideoplayer.Utilities.loadMovieList;

public class AircraftContentActivity extends BaseActivity {

    private static final String TAG = "AircraftContentActivity";
    private CardView cardViewVideos = null;
    private CardView cardViewImages = null;
    private String aircraftName;
    private List<String> movieList;

    private static Context context;
    private static Intent videoIntent;
    @Override
    protected void baseOnCreate(@Nullable Bundle savedInstanceState) {
       // super.onCreate(savedInstanceState);
        this.context = getApplicationContext();
        setContentView(R.layout.activity_aircraft_content);
        Log.d(TAG,"onCreate: started.");
        cardViewVideos = findViewById(R.id.video_card);
        cardViewImages = findViewById(R.id.image_card);
        movieList = loadMovieList(context);

        Intent intent = getIntent();
        aircraftName = intent.getStringExtra("aircraft");
        cardViewVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVideoListActivity();
            }
        });

        cardViewImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageListActivity();
            }
        });
        prepareVideoPlayerListIntent();
    }

    private void prepareVideoPlayerListIntent(){
        videoIntent = new Intent(context, VideoPlayerPlayList.class);
        videoIntent.putStringArrayListExtra("videoList", (ArrayList<String>) movieList);
        videoIntent.putExtra("index",0);
    }

    public void startVideoListActivity(){
        Intent intent= new Intent(this,VideoListActivity.class);
        intent.putExtra("aircraft",aircraftName );
        startActivity(intent);
    }
    public void  startImageListActivity(){
        Intent intent = new Intent(this,ImageListActivity.class);
        intent.putExtra("aircraft",aircraftName );
        startActivity(intent);
    }

    @Override
    protected void reactToIdleState() {
        context.startActivity(videoIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void baseOnResume() {
//       // super.baseOnResume();
//    }
//
//    @Override
//    protected void baseOnUserInteraction() {
//       // super.baseOnUserInteraction();
//    }
}
