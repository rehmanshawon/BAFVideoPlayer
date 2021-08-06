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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    private static final String TAG ="Permission" ;
    private ArrayList<Integer> listPermission;// add all permission in list which you used in manifes.xml

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStoragePermissionGranted();
        MainActivity.context = getApplicationContext();
        Button btnFolder= (Button)findViewById(R.id.btnFolder);
        btnFolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // click handling code
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                    Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivityForResult(Intent.createChooser(i, "Select Video Folder"), 9999);
                }
            }
        });
         /*
        Button btnExit= (Button)findViewById(R.id.btnExit);
        btnFolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // click handling code
                //finishAffinity();
                //finishAndRemoveTask();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        //File dir = Environment.getExternalStorageDirectory();
        File dir = new File("/storage/emulated/0/Movies");
        Log.d(TAG, "onCreate: "+dir);
        String pattern = ".mp4";
        //String[] filenames=new String[0];
        List<String> filenames = new ArrayList<String>();
        //List<String> path_vid=new List<String>();
        //
        //dir.
        //File[] files = dir.listFiles(new MediaFileFilter());
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
               // final int x = i;
                //if (listFile[i].isDirectory()) {
                //    walkdir(listFile[i]);
                //} else {
                    if (files[i].getName().endsWith(pattern)) {
                        // Do what ever u want, add the path of the video to the list
                        filenames.add(files[i].getName());
                        Log.d(TAG, "onFile: "+filenames.get(i));
                    }
                }
            }

        //Log.d(TAG, "onCreate: "+files[0]);
        String[] names = files[0].list(
                new FilenameFilter()
                {
                    public boolean accept(File dir, String name)
                    {
                        return name.endsWith(".mp4");
                        // Example
                        // return name.endsWith(".mp3");
                    }
                });

        MyListData[] myListData = new MyListData[filenames.size()];
        //String moviePath=dir.getAbsolutePath()+"/Movies/";
        String moviePath="/storage/emulated/0/Movies/";
        //moviePath="//com.android.externalstorage.documents/tree/primary%3AMovies";
        //Log.d(TAG, moviePath);
        // myListData[0].
        for(int i=0;i<filenames.size();i++) {
            //Log.d(TAG, moviePath+names[i]);
            String nameOnly= filenames.get(i).split("\\.")[0];
            myListData[i]=new MyListData(nameOnly, moviePath+filenames.get(i));
            Log.d(TAG, "onAdd: "+moviePath+filenames.get(i));
            //playDummy(moviePath+names[i]);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, filenames.size()));
        recyclerView.setAdapter(adapter);
        */
    }
    public void populateWithVideos(String folderPath){
        File dir = new File(folderPath);
        String pattern = ".mp4";
        List<String> filenames = new ArrayList<String>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                // final int x = i;
                //if (listFile[i].isDirectory()) {
                //    walkdir(listFile[i]);
                //} else {
                if (files[i].getName().endsWith(pattern)) {
                    // Do what ever u want, add the path of the video to the list
                    filenames.add(files[i].getName());
                   // Log.d(TAG, "onFile: "+filenames.get(i));
                }
            }
        }

        MyListData[] myListData = new MyListData[filenames.size()];
        for(int i=0;i<filenames.size();i++) {
            //Log.d(TAG, moviePath+names[i]);
            String nameOnly= filenames.get(i).split("\\.")[0];
            myListData[i]=new MyListData(nameOnly, folderPath+"/"+filenames.get(i));
            //Log.d(TAG, "onAdd: "+myListData[i].getImgPath());
            //playDummy(moviePath+names[i]);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, filenames.size()));
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //super.onStart();


        //ViewGroup vg = findViewById (R.id.activity_main);
        //vg.invalidate();
        //getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        //Toast.makeText(getApplicationContext(), "Now onStart() calls", Toast.LENGTH_LONG).show(); //onStart Called
    }

    //Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.system_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_set_folder:
               // Toast.makeText(this, "Set Movie Directory", Toast.LENGTH_SHORT).show();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                    Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
                }
                break;
            case R.id.action_about:
                //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                //alertDialogBuilder.setPositiveButton("Hello",
                //        DialogInterface.OnClickListener listener)
                //Toast.makeText(this, "Copyright Information", Toast.LENGTH_SHORT).show();
                try {
                    showDialog("Copyright (c) 2021 Bangladesh Air Force.\nbaf.mil.bd","App Copyright");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.action_developer:
                //Toast.makeText(this, "Developer Information", Toast.LENGTH_SHORT).show();
                try {
                    showDialog("Kopotron Corporation.\nserver.kopotron.com","App Developer");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
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
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 9999:
                Log.v("Test", "Result URI " + data.getData());
                Uri treeUri = data.getData();
                String path = FileUtil.getFullPathFromTreeUri(treeUri,this);
                populateWithVideos(path);
               // Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
               //        DocumentsContract.getTreeDocumentId(uri));
                final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
                Log.v("Is KitKat", String.valueOf(isKitKat));
                Log.d(TAG, "onActivityResult: "+path);
               // String path = getPath(this, docUri);
                break;
        }
    }
/*
    public  void playDummy(String videoPath){
        VideoView videoViewD =(VideoView)findViewById(R.id.videoViewD);

        //Set MediaController  to enable play, pause, forward, etc options.
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoViewD);
        //Starting VideView By Setting MediaController and URI
        videoViewD.setMediaController(mediaController);
        videoViewD.setVideoURI(Uri.parse(videoPath));
        //videoViewD.requestFocus();
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        videoViewD.setVisibility(videoViewD.INVISIBLE);
        videoViewD.start();
        videoViewD.seekTo(2000);
        videoViewD.stopPlayback();
    }
    */
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