package com.kopotron.bafvideoplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.kopotron.bafvideoplayer.Utilities.loadAircraftData;
import static com.kopotron.bafvideoplayer.Utilities.loadMovieList;
import static com.kopotron.bafvideoplayer.Utilities.rescanMovies;

public class ImagePlayerPlayList extends AppCompatActivity {
    // initialize variables
    ImageButton btPrevious,btNext;
    ImageSwitcher imageSwitcher;
    private static Context context;
    private static Intent videoIntent;
    ArrayList<AircraftData> aircraftDataArrayList;
    List<String> imageList;
    private List<String> movieList;
    int count=0;
    int currentIndex=0;
    FloatingActionButton floatingActionButtonGoHome=null;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000; //Delay for 1 seconds.  One second = 1000 milliseconds.
    int second=10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);
        this.context = getApplicationContext();
        movieList = loadMovieList(context);
        // assign variables
        btPrevious=findViewById(R.id.bt_previous);
        btNext=findViewById(R.id.bt_next);
        imageSwitcher=findViewById(R.id.image_switcher);
        floatingActionButtonGoHome=findViewById(R.id.FAB_GO_HOME);
        floatingActionButtonGoHome.setVisibility(View.INVISIBLE);
        prepareVideoPlayerListIntent();
        Intent intent = getIntent();
        imageList = intent.getStringArrayListExtra("imageList");
        count=imageList.size();
        Log.d("image list",imageList.toString());
        currentIndex=intent.getIntExtra("index",0);

        // implement the ViewFactory interface and implement
        // unimplemented method that returns an imageView
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory(){
            @Override
            public View makeView() {
                ImageView imageView= new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                imageView.setLayoutParams(new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.FILL_PARENT
//                        ,ViewGroup.LayoutParams.FILL_PARENT
//                ));
                // returning imageview
                return imageView;
            }
        });

        imageSwitcher.setImageURI(Uri.parse(imageList.get(0)));
        // set on click listener on left button
        btPrevious.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting animation to swipe image from right to left
                imageSwitcher.setInAnimation(ImagePlayerPlayList.this,R.anim.from_right);
                imageSwitcher.setOutAnimation(ImagePlayerPlayList.this,R.anim.to_left);
                --currentIndex;
                second = 10;
                // condition
                if(currentIndex<0)
                    currentIndex=imageList.size()-1;
                imageSwitcher.setImageURI(Uri.parse((imageList.get(currentIndex))));
            }
        });

        // set on click listener on right(next) button
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting animation to swipe image from left to right
                imageSwitcher.setInAnimation(ImagePlayerPlayList.this,R.anim.from_left);
                imageSwitcher.setOutAnimation(ImagePlayerPlayList.this,R.anim.to_right);
                currentIndex++;
                second = 10;
                // condition
                if(currentIndex==count) {
                    currentIndex = 0;
                }
                imageSwitcher.setImageURI(Uri.parse(imageList.get(currentIndex)));
            }
        });
        floatingActionButtonGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AircraftListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButtonGoHome.setVisibility(floatingActionButtonGoHome.getVisibility()==View.VISIBLE?View.INVISIBLE:View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        second=10;
       // getSupportActionBar().show();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        movieList = loadMovieList(context);
        //start handler as activity become visible
        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                Log.d("time", String.valueOf(second));
                second--;
                if(second==0) {
                    if(currentIndex==count-1)
                    {
                        Log.d("currentIndex", String.valueOf(currentIndex));
                        currentIndex = 0;
                        if(movieList.size()>0) {
                            context.startActivity(videoIntent);
                            finish();
                        }
                    }
                    btNext.performClick();
                    second = 10;
                }
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

    private void prepareVideoPlayerListIntent(){
        videoIntent = new Intent(context, VideoPlayerPlayList.class);
        aircraftDataArrayList=loadAircraftData(context);
        movieList=rescanMovies(aircraftDataArrayList);
        videoIntent.putStringArrayListExtra("videoList", (ArrayList<String>) movieList);
        videoIntent.putExtra("index",0);
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
}

