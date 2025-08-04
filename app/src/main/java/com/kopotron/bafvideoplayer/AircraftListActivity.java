package com.kopotron.bafvideoplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.kopotron.bafvideoplayer.Utilities.loadAircraftData;
import static com.kopotron.bafvideoplayer.Utilities.loadUserInfo;
import static com.kopotron.bafvideoplayer.Utilities.rescanImages;
import static com.kopotron.bafvideoplayer.Utilities.rescanMovies;
import static com.kopotron.bafvideoplayer.Utilities.saveAircraftData;
import static com.kopotron.bafvideoplayer.Utilities.saveImageList;
import static com.kopotron.bafvideoplayer.Utilities.saveMovieList;
import static com.kopotron.bafvideoplayer.Utilities.saveUserInfo;

public class AircraftListActivity extends BaseActivity {
     RecyclerView recyclerView;
     ArrayList<AircraftData> aircraftDataArrayList;
     ArrayList<AircraftDirectories> aircraftDirectoriesArrayList;
     AircraftViewAdapter aircraftViewAdapter=null;

    // This button is placed in main activity layout.
    private FloatingActionButton openInputPopupDialogButton = null;
    // Below edittext and button are all exist in the popup dialog view.
    private View popupInputDialogView = null;
    // Click this button in popup dialog to save user input data in above three edittext.
    private Button saveAircraftButton = null;
    // Click this button to cancel edit user data.
    private Button cancelAircraftButton = null;
    private Button pictureAircraftButton=null;
    // Contains user name data.
    private EditText aircraftNameEditText = null;
    private EditText aircraftImageFile = null;
    private static Context context;
    private static Intent videoIntent;
    private static Intent imageIntent;
    private List<String> movieList;
    private List<String> pictureList;
    public static final int PICKFILE_RESULT_CODE = 1;
    private  String aircraftName;
    File aircraftImage;
   // SharedPreferences sharedPreferences;

   // Handler handler;
   // Runnable r;

    @Override
    protected void baseOnCreate(Bundle savedInstanceState) {
      //  super.onCreate(savedInstanceState);
        this.context = getApplicationContext();
        setContentView(R.layout.activity_aircraft_list);
       // handler = new Handler();
        recyclerView=findViewById(R.id.idAircraftRV);
        // created new array list..

        aircraftDataArrayList=new ArrayList<AircraftData>();
        aircraftDirectoriesArrayList = new ArrayList<>();

        movieList = new ArrayList<>();
        pictureList = new ArrayList<>();
        aircraftDataArrayList=loadAircraftData(context);

        if(aircraftDataArrayList.size()>0)
        {
            movieList=rescanMovies(aircraftDataArrayList);
            pictureList=rescanImages(aircraftDataArrayList);
            saveMovieList(context, (ArrayList<String>) movieList);
            saveImageList(context, (ArrayList<String>) pictureList);
        }


        // added data from arraylist to adapter class.
         aircraftViewAdapter=new AircraftViewAdapter(aircraftDataArrayList,this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(aircraftViewAdapter);

        // create admin control button
        initAdminActivityControls();
        // set it to visible if roll is admin
//        Intent intent = getIntent();
//        String loggedIn=intent.getStringExtra("loggedIn");
        UserInfo userInfo=loadUserInfo(context);
        if(userInfo.getLoggedIn())
            openInputPopupDialogButton.setVisibility(View.VISIBLE);

//        if(loggedIn!=null && loggedIn.equals("true"))
//        {
//            openInputPopupDialogButton.setVisibility(View.VISIBLE);
//        }
    }

    private void prepareVideoPlayerListIntent(){
            videoIntent = new Intent(context, VideoPlayerPlayList.class);
            aircraftDataArrayList=loadAircraftData(context);
            movieList=rescanMovies(aircraftDataArrayList);
            videoIntent.putStringArrayListExtra("videoList", (ArrayList<String>) movieList);
            videoIntent.putExtra("index",0);
    }
    private void prepareImagePlayerListIntent(){
        imageIntent = new Intent(context, ImagePlayerPlayList.class);
        aircraftDataArrayList=loadAircraftData(context);
        pictureList=rescanImages(aircraftDataArrayList);
        imageIntent.putStringArrayListExtra("imageList", (ArrayList<String>) pictureList);
        imageIntent.putExtra("index",0);
    }

    @Override
    protected void reactToIdleState() {
        UserInfo userInfo=loadUserInfo(context);
        userInfo.setLoggedIn(false);
        saveUserInfo(context,userInfo);
        openInputPopupDialogButton.setVisibility(View.INVISIBLE);
        prepareVideoPlayerListIntent();
        prepareImagePlayerListIntent();
        aircraftViewAdapter.notifyDataSetChanged();
        if(movieList.size()>0) {
            context.startActivity(videoIntent);
        }
        else{
            if(pictureList.size()>0)
            {
               context.startActivity(imageIntent);
            }
        }
        //Toast.makeText(context, "user is inactive from last 10 seconds",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void baseOnResume() {

       aircraftDataArrayList=loadAircraftData(context);
        aircraftViewAdapter=new AircraftViewAdapter(aircraftDataArrayList,context);
        recyclerView.setAdapter(aircraftViewAdapter);
        if(aircraftDataArrayList.size()>0) {
            movieList = rescanMovies(aircraftDataArrayList);
            pictureList = rescanImages(aircraftDataArrayList);
            saveMovieList(context, (ArrayList<String>) movieList);
            saveImageList(context, (ArrayList<String>) pictureList);
        }
        UserInfo userInfo=loadUserInfo(context);
        if(userInfo.getLoggedIn())
            openInputPopupDialogButton.setVisibility(View.VISIBLE);
        else
            openInputPopupDialogButton.setVisibility(View.INVISIBLE);

        Log.d("resume","AircraftListActivity");
    }

    /* Initialize main activity ui controls ( button and listview ). */
    private void initAdminActivityControls()
    {
        if(openInputPopupDialogButton == null)
        {
            openInputPopupDialogButton = findViewById(R.id.open_modal_bottom_sheet);
            // When click the open input popup dialog button.
            openInputPopupDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Create a AlertDialog Builder.
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AircraftListActivity.this);
                    // Set title, icon, can not cancel properties.
                    alertDialogBuilder.setTitle("Create New Aircraft Directory: ");
                    alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
                    alertDialogBuilder.setCancelable(false);

                    // Init popup dialog view and it's ui controls.
                    initPopupViewControls();

                    // Set the inflated layout view object to the AlertDialog builder.
                    alertDialogBuilder.setView(popupInputDialogView);

                    // Create AlertDialog and show.
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    aircraftNameEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        aircraftName=s.toString();
                        //Log.d(aircraftName,aircraftName);
                        }
                    });
                    // When user click the save user data button in the popup dialog.
                    saveAircraftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // Get aircraft name from popup dialog editeext.
                             aircraftName = aircraftNameEditText.getText().toString();
                            if(aircraftName!="" && aircraftImage!=null)
                            {

                                File aircraftContentPathMovies= makeDirectory(context, Environment.DIRECTORY_MOVIES,getString(R.string.app_name)+"/"+aircraftName);
                                File aircraftContentPathPictures= makeDirectory(context, Environment.DIRECTORY_PICTURES,getString(R.string.app_name)+"/"+aircraftName);
                                aircraftDirectoriesArrayList.add(new AircraftDirectories(aircraftContentPathMovies.toString(), aircraftContentPathPictures.toString(),aircraftContentPathMovies,aircraftContentPathPictures));
                                aircraftDataArrayList.add(new AircraftData(aircraftName,aircraftImage,aircraftDirectoriesArrayList.get(aircraftDirectoriesArrayList.size()-1),false));
                               // saveData();
                                //Log.d("aircraft items",aircraftDataArrayList.toString());
                                saveAircraftData(context,aircraftDataArrayList);
                                aircraftViewAdapter=new AircraftViewAdapter(aircraftDataArrayList,context);
                                recyclerView.setAdapter(aircraftViewAdapter);
                                //aircraftViewAdapter.notifyDataSetChanged();
                                //aircraftViewAdapter.notifyItemChanged(aircraftDataArrayList.size()-1);
                                //recyclerView.invalidate();
                                alertDialog.cancel();
                            }

                        }
                    });

                    cancelAircraftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.cancel();
                        }
                    });

                    pictureAircraftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent chooseFile = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                           // alertDialog.cancel();
                        }
                    });
                }
            });
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    Uri fileUri = data.getData();
                    String filePath = fileUri.getPath();
                    Cursor returnCursor = getContentResolver().query(fileUri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    returnCursor.moveToFirst();
                    String fileName = returnCursor.getString(nameIndex);
                    File targetLocation= makeDirectory(context,Environment.DIRECTORY_PICTURES,getString(R.string.app_name)+"/"+aircraftName+"/thumbnails");
                    aircraftImage=new File(targetLocation.getAbsolutePath()+"/"+fileName);
                    String targetFileName=targetLocation.getAbsolutePath()+"/"+fileName;
                    try {
                        ContentResolver cr = getContentResolver();
                        InputStream in = cr.openInputStream(fileUri);
                        OutputStream out = new FileOutputStream(aircraftImage);

                        // Copy the bits from instream to outstream
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                        String fileNameWithOutExt = FilenameUtils.removeExtension(fileName);
                        aircraftImageFile.setText(fileNameWithOutExt);
                        Log.d("copy","copy done");
                    }catch (IOException e) {
                        Log.d("copy error", e.getMessage());
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private File makeDirectory(Context ctx, String type, String folderName){
        try{
            File folder = new File(ctx.getExternalFilesDir(type),folderName);
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
                Log.d("file copy","inside mkdirs" );
            }
            if (success) {

                Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show();
                Log.d("file copy","done mkdir" );
                return folder;
            } else {
                Toast.makeText(this,"folder_failed",Toast.LENGTH_SHORT).show();
                Log.d("file copy","failed mkdirs" );
                System.out.println(folder.getAbsolutePath());
                return  null;
            }
        }catch (Exception e){
            Toast.makeText(this,"exception",Toast.LENGTH_SHORT).show();
            Log.d("file copy",e.toString() );
            e.printStackTrace();
        }
        return null;
    }
    /* Initialize popup dialog view and ui controls in the popup dialog. */
    private void initPopupViewControls()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);

        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.showcase_name_dialog, null);

        // Get user input edittext and button ui controls in the popup dialog.
        aircraftNameEditText = (EditText) popupInputDialogView.findViewById(R.id.aircraftName);
        saveAircraftButton = popupInputDialogView.findViewById(R.id.button_save_user_data);
        cancelAircraftButton = popupInputDialogView.findViewById(R.id.button_cancel_user_data);
        pictureAircraftButton=popupInputDialogView.findViewById(R.id.BTN_PICK_IMAGE);
        aircraftImageFile=popupInputDialogView.findViewById(R.id.aircraftPicture);
    }

    private void loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        //SharedPreferences.Editor.
        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("aircrafts", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<AircraftData>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        aircraftDataArrayList = gson.fromJson(json, type);

        json = sharedPreferences.getString("aircraftdirectories", null);
        type = new TypeToken<ArrayList<AircraftDirectories>>() {}.getType();
        aircraftDirectoriesArrayList = gson.fromJson(json, type);
        Log.d("directories",aircraftDirectoriesArrayList.toString());
        // checking below if the array list is empty or not
        if (aircraftDataArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            aircraftDataArrayList = new ArrayList<>();
        }

        if (aircraftDirectoriesArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            aircraftDirectoriesArrayList = new ArrayList<>();
        }
    }

    private void saveData() {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        //sharedPreferences.edit().clear().commit();
        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(aircraftDataArrayList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("aircrafts", json);

        json = gson.toJson(aircraftDirectoriesArrayList);
        editor.putString("aircraftdirectories", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        // after saving data we are displaying a toast message.
        Toast.makeText(this, "Saved Aircraft and directories Lists Shared preferences. ", Toast.LENGTH_SHORT).show();
    }



}
