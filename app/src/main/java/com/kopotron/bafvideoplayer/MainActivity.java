package com.kopotron.bafvideoplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.kopotron.bafvideoplayer.Utilities.loadAircraftData;
import static com.kopotron.bafvideoplayer.Utilities.loadUserInfo;
import static com.kopotron.bafvideoplayer.Utilities.rescanImages;
import static com.kopotron.bafvideoplayer.Utilities.rescanMovies;
import static com.kopotron.bafvideoplayer.Utilities.saveUserInfo;

public class MainActivity extends BaseActivity {
    // This button is placed in main activity layout.
    private Button navigateToAircraft = null;
    // This listview is just under above button.
    private ListView userDataListView = null;
    // Below edittext and button are all exist in the popup dialog view.
    private View popupInputDialogView = null;
    // Contains user name data.
    private EditText userNameEditText = null;
    // Contains password data.
    private EditText passwordEditText = null;
    // Contains email data.
    private EditText emailEditText = null;
    // Click this button in popup dialog to save user input data in above three edittext.
    private Button saveUserDataButton = null;
    // Click this button to cancel edit user data.
    private Button cancelUserDataButton = null;

    FloatingActionButton floatingActionButtonMain=null;
    private static Context context;
    private static final String TAG ="Permission" ;
    private ArrayList<Integer> listPermission;// add all permission in list which you used in manifes.xml

    ArrayList<AircraftData> aircraftDataArrayList;
   // AircraftViewAdapter aircraftViewAdapter=null;
    private static Intent videoIntent;
    private static Intent imageIntent;
    private List<String> movieList;
    private List<String> pictureList;

    @Override
    protected void baseOnCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButtonMain=findViewById(R.id.FAB_MAIN);
        isStoragePermissionGranted();
        this.context = getApplicationContext();
        // Only save default user if none exists
        UserInfo existing = loadUserInfo(context);
        if (existing == null) {
            UserInfo userInfo = new UserInfo("admin", "123456", false);
            saveUserInfo(context, userInfo);
        }

       // openAircraftListactivity();
       //initMainActivityControls();

        // When click the open input popup dialog button.
//        navigateToAircraft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openAircraftListactivity();
//            }
//        });
        floatingActionButtonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAircraftListactivity();
            }
        });
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
       // openInputPopupDialogButton.setVisibility(View.INVISIBLE);
        prepareVideoPlayerListIntent();
        prepareImagePlayerListIntent();
       // aircraftViewAdapter.notifyDataSetChanged();
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


        Log.d("resume","Main Activity");
    }

    public void openAircraftListactivity(){
        Intent intent = new Intent(this, AircraftListActivity.class);
        startActivity(intent);
    }
    /* Initialize main activity ui controls ( button and listview ). */


    public void populateWithVideos(String folderPath){
        File dir = new File(folderPath);
        String pattern = ".mp4";
        List<String> filenames = new ArrayList<String>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(pattern)) {
                    // Do what ever u want, add the path of the video to the list
                    filenames.add(files[i].getName());
                }
            }

        }
        if(filenames.size()>0)
        {
            MyListData[] myListData = new MyListData[filenames.size()];
            for(int i=0;i<filenames.size();i++) {
                String nameOnly= filenames.get(i).split("\\.")[0];
                myListData[i]=new MyListData(nameOnly, folderPath+"/"+filenames.get(i));
            }
//            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//            MyListAdapter adapter = new MyListAdapter(myListData);
//            GridLayoutManager layoutManager=new GridLayoutManager(this,3);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setAdapter(adapter);
        }

    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    //Options Menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.system_menu, menu);
//        inflater.inflate(R.menu.options_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//
//            case R.id.action_about:
//            case R.id.about_item:
//                try {
//                    showDialog("Copyright (c) 2021 Bangladesh Air Force.\nbaf.mil.bd","App Copyright");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.action_developer:
//            case R.id.developer_item:
//                try {
//                    showDialog("Kopotron Corporation.\nserver.kopotron.com","App Developer");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void showDialog(final String message, final String Title) throws Exception {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(Title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
            }
        });
        
        builder.show();
    }



    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }
    public static Context getAppContext() {
        return MainActivity.context;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 9999:
                if(data==null) break;
                Uri treeUri = data.getData();
                String path = FileUtil.getFullPathFromTreeUri(treeUri,this);
                populateWithVideos(path);
                final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  

                break;
        }
    }

}

final class FileUtil {

    private static final String PRIMARY_VOLUME_NAME = "primary";

    @Nullable
    public static String getFullPathFromTreeUri(@Nullable final Uri treeUri, Context con) {
        if (treeUri == null) return null;
        String volumePath = getVolumePath(getVolumeIdFromTreeUri(treeUri),con);
        if (volumePath == null) return File.separator;
        if (volumePath.endsWith(File.separator))
            volumePath = volumePath.substring(0, volumePath.length() - 1);

        String documentPath = getDocumentPathFromTreeUri(treeUri);
        if (documentPath.endsWith(File.separator))
            documentPath = documentPath.substring(0, documentPath.length() - 1);

        if (documentPath.length() > 0) {
            if (documentPath.startsWith(File.separator))
                return volumePath + documentPath;
            else
                return volumePath + File.separator + documentPath;
        }
        else return volumePath;
    }


    private static String getVolumePath(final String volumeId, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return getVolumePathForAndroid11AndAbove(volumeId, context);
        else 
            return getVolumePathBeforeAndroid11(volumeId, context);
    }


    private static String getVolumePathBeforeAndroid11(final String volumeId, Context context){
        try {
            StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getUuid = storageVolumeClazz.getMethod("getUuid");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
            Object result = getVolumeList.invoke(mStorageManager);

            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String uuid = (String) getUuid.invoke(storageVolumeElement);
                Boolean primary = (Boolean) isPrimary.invoke(storageVolumeElement);

                if (primary && PRIMARY_VOLUME_NAME.equals(volumeId))    // primary volume?
                    return (String) getPath.invoke(storageVolumeElement);

                if (uuid != null && uuid.equals(volumeId))    // other volumes?
                    return (String) getPath.invoke(storageVolumeElement);
            }
            // not found.
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.R)
    private static String getVolumePathForAndroid11AndAbove(final String volumeId, Context context) {
        try {
            StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            List<StorageVolume> storageVolumes = mStorageManager.getStorageVolumes();
            for (StorageVolume storageVolume : storageVolumes) {
                // primary volume?
                if (storageVolume.isPrimary() && PRIMARY_VOLUME_NAME.equals(volumeId))
                    return storageVolume.getDirectory().getPath();

                // other volumes?
                String uuid = storageVolume.getUuid();
                if (uuid != null && uuid.equals(volumeId))
                    return storageVolume.getDirectory().getPath();

            }
            // not found.
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String getVolumeIdFromTreeUri(final Uri treeUri) {
        final String docId = DocumentsContract.getTreeDocumentId(treeUri);
        final String[] split = docId.split(":");
        if (split.length > 0) return split[0];
        else return null;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String getDocumentPathFromTreeUri(final Uri treeUri) {
        final String docId = DocumentsContract.getTreeDocumentId(treeUri);
        final String[] split = docId.split(":");
        if ((split.length >= 2) && (split[1] != null)) return split[1];
        else return File.separator;
    }
}