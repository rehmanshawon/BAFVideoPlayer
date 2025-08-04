package com.kopotron.bafvideoplayer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.kopotron.bafvideoplayer.Utilities.getFileListFromDirectory;
import static com.kopotron.bafvideoplayer.Utilities.loadAircraftData;
import static com.kopotron.bafvideoplayer.Utilities.loadUserInfo;
import static com.kopotron.bafvideoplayer.Utilities.rescanImages;
import static com.kopotron.bafvideoplayer.Utilities.rescanMovies;
import static com.kopotron.bafvideoplayer.Utilities.saveImageList;
import static com.kopotron.bafvideoplayer.Utilities.saveMovieList;
import static com.kopotron.bafvideoplayer.Utilities.saveUserInfo;

public class VideoListActivity extends BaseActivity {
     RecyclerView recyclerView;
    private ArrayList<VideoData> videoDataArrayList;
    private FloatingActionButton fabAddVideo = null;
     VideoViewAdapter videoViewAdapter = null;
    public static final int PICKFILE_RESULT_CODE = 1;
    private Uri fileUri;
    private String filePath;
    private String fileName;
    private static Context context;
    private List<String > movieFileList;
    private File movieFolder;
    private String movieFolderPath;
    private  String aircraftName;
     ArrayList<AircraftData> aircraftDataArrayList;
    private List<String> movieList;
    private List<String> pictureList;
    private static Intent videoIntent;
    private static Intent imageIntent;

    @Override
    protected void baseOnCreate(@Nullable Bundle savedInstanceState) {
       // super.onCreate(savedInstanceState);
        this.context = getApplicationContext();
        setContentView(R.layout.activity_video_list);
        aircraftDataArrayList=loadAircraftData(context);
        movieList=rescanMovies(aircraftDataArrayList);
        pictureList=rescanImages(aircraftDataArrayList);
        saveMovieList(context, (ArrayList<String>) movieList);
        saveImageList(context, (ArrayList<String>) pictureList);
        Intent intent = getIntent();
        aircraftName = intent.getStringExtra("aircraft");
        recyclerView = findViewById(R.id.RV_VIDEOS);
        movieFolder=makeDirectory(context,Environment.DIRECTORY_MOVIES,getString(R.string.app_name)+"/"+aircraftName);
        movieFolderPath=movieFolder.getAbsolutePath();
        String[] videoFileExtension = new String[] {
                "mp4",
        };
        movieFileList=getFileListFromDirectory(movieFolderPath,videoFileExtension);
        Log.d("moviefiles",movieFileList.toString());
        videoDataArrayList = new ArrayList<>();
        if(movieFileList.size()>0)
        {
            for(int i=0;i<movieFileList.size();i++) {
                String fileNameWithOutExt = FilenameUtils.removeExtension(movieFileList.get(i));
                File movieFile=new File(movieFolderPath+"/"+movieFileList.get(i));
                videoDataArrayList.add(new VideoData(fileNameWithOutExt, movieFolderPath+"/"+movieFileList.get(i),movieFile));
            }
        }

        videoViewAdapter = new VideoViewAdapter(videoDataArrayList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(videoViewAdapter);

        UserInfo userInfo=loadUserInfo(context);
        if(userInfo.getLoggedIn())
            initializeAddVideoControl();

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
    private void initializeAddVideoControl() {
        if (fabAddVideo == null) {
            fabAddVideo = findViewById(R.id.FAB_VIDEOS);
            fabAddVideo.setVisibility(View.VISIBLE);
            fabAddVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // videoDataArrayList.add(new VideoData("video01",filePath));
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                    chooseFile.setType("*/*");
                    chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/mp4"});
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                }
            });
        }
    }

    @Override
    protected void reactToIdleState() {
        UserInfo userInfo=loadUserInfo(context);
        userInfo.setLoggedIn(false);
        saveUserInfo(context,userInfo);
        if(fabAddVideo!=null) {
            fabAddVideo.setVisibility(View.INVISIBLE);
            videoViewAdapter.notifyDataSetChanged();
        }
        prepareVideoPlayerListIntent();
        prepareImagePlayerListIntent();
        if(movieList.size()>0) {
            context.startActivity(videoIntent);
        }
        else{
            if(pictureList.size()>0)
            {
                context.startActivity(imageIntent);
            }
        }

    }

    @Override
    protected void baseOnResume() {
        aircraftDataArrayList=new ArrayList<>();
        movieList = new ArrayList<>();
        pictureList = new ArrayList<>();
        aircraftDataArrayList=loadAircraftData(context);
//        aircraftViewAdapter=new AircraftViewAdapter(aircraftDataArrayList,context);
//        recyclerView.setAdapter(aircraftViewAdapter);
        movieList=rescanMovies(aircraftDataArrayList);
        pictureList=rescanImages(aircraftDataArrayList);
        saveMovieList(context, (ArrayList<String>) movieList);
        saveImageList(context, (ArrayList<String>) pictureList);

//        movieFolder=makeDirectory(context,Environment.DIRECTORY_MOVIES,getString(R.string.app_name)+"/"+"AN-24b");
//        movieFolderPath=movieFolder.getAbsolutePath();
//        String[] videoFileExtension = new String[] {
//                "mp4",
//        };
//        movieFileList=getFileListFromDirectory(movieFolderPath,videoFileExtension);
//        videoDataArrayList = new ArrayList<>();
//        if(movieFileList.size()>0)
//        {
//            for(int i=0;i<movieFileList.size();i++) {
//                String fileNameWithOutExt = FilenameUtils.removeExtension(movieFileList.get(i));
//                File movieFile=new File(movieFileList.get(i));
//                videoDataArrayList.add(new VideoData(fileNameWithOutExt, movieFolderPath+"/"+movieFileList.get(i),movieFile));
//            }
//        }
//
//        videoViewAdapter = new VideoViewAdapter(videoDataArrayList, this);
//        recyclerView.setAdapter(videoViewAdapter);
//        videoViewAdapter.notifyDataSetChanged();
        UserInfo userInfo=loadUserInfo(context);
        if(userInfo.getLoggedIn())
            initializeAddVideoControl();

        Log.d("resume","VideoListActivity");
    }
//
//    @Override
//    protected void baseOnUserInteraction() {
//        // super.baseOnUserInteraction();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    filePath = fileUri.getPath();
                    //String mimeType = getContentResolver().getType(fileUri);
                    Cursor returnCursor =
                            getContentResolver().query(fileUri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    //int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();
                    fileName = returnCursor.getString(nameIndex);
                    File targetLocation= makeDirectory(context,Environment.DIRECTORY_MOVIES,getString(R.string.app_name)+"/"+aircraftName);
                    File targetFile=new File(targetLocation.getAbsolutePath()+"/"+fileName);
                    String targetFileName=targetLocation.getAbsolutePath()+"/"+fileName;
                    Log.d("file path", targetFile.getAbsolutePath());
                    try {
                        ContentResolver cr = getContentResolver();
                        InputStream in = cr.openInputStream(fileUri);
                        OutputStream out = new FileOutputStream(targetFile);

                        // Copy the bits from instream to outstream
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                        String fileNameWithOutExt = FilenameUtils.removeExtension(fileName);
                        videoDataArrayList.add(new VideoData(fileNameWithOutExt, targetFileName,targetFile));
                        videoViewAdapter.notifyDataSetChanged();
                        Log.d("copy","copy done");
                    }catch (IOException e) {
                        Log.d("copy error", e.getMessage());
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    private void saveFile(Uri sourceUri, String fileName, String mimeType) throws IOException {
        ContentValues values = new ContentValues();
        Uri destinationUri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
            if (fileName.endsWith(".mp4")) {
                File dest = makeDirectory(context,Environment.DIRECTORY_MOVIES,getString(R.string.app_name));
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/"+ getString(R.string.app_name));
                //destinationUri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                //destinationUri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                //dest.
                Log.d("file copy:123", String.valueOf(MediaStore.Video.Media.EXTERNAL_CONTENT_URI));
            } else {
                makeDirectory(context,Environment.DIRECTORY_PICTURES,getString(R.string.app_name));
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/"+ getString(R.string.app_name));
                destinationUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Log.d("file copy", String.valueOf(MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
            }
//            Log.d("file copy","before streaming" );
//            InputStream inputStream = context.getContentResolver().openInputStream(sourceUri);
//            OutputStream outputStream = context.getContentResolver().openOutputStream(destinationUri);
//            Log.d("file copy","before copy!" );
//            IOUtils.copy(inputStream, outputStream);
//            Log.d("file copy","The File has been saved!" );
            //Toast.makeText(context, "The File has been saved!", Toast.LENGTH_SHORT).show();

        }
    }

    private File makeDirectory(Context ctx,String type, String folderName){
        try{
            File folder = new File(ctx.getExternalFilesDir(type),folderName);
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
                //Log.d("file copy","inside mkdirs" );
            }
            if (success) {
                //Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show();
                //Log.d("file copy","done mkdir" );
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

//    private List<String> getFileListFromDirectory(String folderPath, String fileType){
//        File dir = new File(folderPath);
//        String pattern = ".mp4";
//        List<String> filenames = new ArrayList<String>();
//        File[] files = dir.listFiles();
//        if (files != null) {
//            for (int i = 0; i < files.length; i++) {
//                if (files[i].getName().endsWith(pattern)) {
//                    // Do what ever u want, add the path of the video to the list
//                    filenames.add(files[i].getName());
//                }
//            }
//
//        }
//        return  filenames;
//    }
}
