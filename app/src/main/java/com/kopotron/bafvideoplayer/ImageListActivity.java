package com.kopotron.bafvideoplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import static com.kopotron.bafvideoplayer.Utilities.loadImageList;
import static com.kopotron.bafvideoplayer.Utilities.loadMovieList;
import static com.kopotron.bafvideoplayer.Utilities.loadUserInfo;
import static com.kopotron.bafvideoplayer.Utilities.rescanImages;
import static com.kopotron.bafvideoplayer.Utilities.rescanMovies;
import static com.kopotron.bafvideoplayer.Utilities.saveImageList;
import static com.kopotron.bafvideoplayer.Utilities.saveMovieList;
import static com.kopotron.bafvideoplayer.Utilities.saveUserInfo;

public class ImageListActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ArrayList<ImageData> imageDataArrayList;
    private FloatingActionButton fabAddImage = null;
    private ImageViewAdapter imageViewAdapter = null;
    public static final int PICKFILE_RESULT_CODE = 1;
    private Uri fileUri;
    private String filePath;
    private String fileName;
    private static Context context;
    private List<String > imageFileList;
    private File imageFolder;
    private String imageFolderPath;
    private  String aircraftName;
    private List<String> movieList;
    private List<String> pictureList;
    private static Intent imageIntent;
    private static Intent videoIntent;
    ArrayList<AircraftData> aircraftDataArrayList;
    @Override
    protected void baseOnCreate(@Nullable Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        this.context = getApplicationContext();
        setContentView(R.layout.activity_image_list);
        movieList = loadMovieList(context);
        pictureList = loadImageList(context);
        Intent intent = getIntent();
        aircraftName = intent.getStringExtra("aircraft");
        recyclerView = findViewById(R.id.RV_IMAGES);
        imageFolder=makeDirectory(context, Environment.DIRECTORY_PICTURES,getString(R.string.app_name)+"/"+aircraftName);
        imageFolderPath=imageFolder.getAbsolutePath();
         String[] imageFileExtension = new String[] {
                "jpg",
                "png",
                "gif",
                "jpeg"
        };
        imageFileList=getFileListFromDirectory(imageFolderPath,imageFileExtension);
        imageDataArrayList = new ArrayList<>();
        if(imageFileList.size()>0)
        {
            for(int i=0;i<imageFileList.size();i++) {
                String fileNameWithOutExt = FilenameUtils.removeExtension(imageFileList.get(i));
                File imageFile=new File(imageFolderPath+"/"+imageFileList.get(i));
                imageDataArrayList.add(new ImageData(fileNameWithOutExt, imageFolderPath+"/"+imageFileList.get(i),imageFile));
            }
        }
        imageViewAdapter = new ImageViewAdapter(imageDataArrayList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(imageViewAdapter);
        UserInfo userInfo=loadUserInfo(context);
        if(userInfo.getLoggedIn())
        initializeAddImageControl();
        prepareImagePlayerListIntent();
    }

    @Override
    protected void baseOnResume() {
        aircraftDataArrayList=new ArrayList<>();
        movieList = new ArrayList<>();
        pictureList = new ArrayList<>();
        aircraftDataArrayList=loadAircraftData(context);
        movieList=rescanMovies(aircraftDataArrayList);
        pictureList=rescanImages(aircraftDataArrayList);
        saveMovieList(context, (ArrayList<String>) movieList);
        saveImageList(context, (ArrayList<String>) pictureList);
        UserInfo userInfo=loadUserInfo(context);
        if(userInfo.getLoggedIn())
            initializeAddImageControl();

        Log.d("resume","ImageListActivity");
    }
    private void initializeAddImageControl() {
        if (fabAddImage == null) {
            fabAddImage = findViewById(R.id.FAB_IMAGES);
            fabAddImage.setVisibility(View.VISIBLE);
            fabAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // videoDataArrayList.add(new VideoData("video01",filePath));
                    Intent chooseFile = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
//                    chooseFile.setType("*/*");
//                    chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/png"});
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                }
            });
        }
    }

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
                    File targetLocation= makeDirectory(context,Environment.DIRECTORY_PICTURES,getString(R.string.app_name)+"/"+aircraftName);
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
                        imageDataArrayList.add(new ImageData(fileNameWithOutExt, targetFileName,targetFile));
                        imageViewAdapter.notifyDataSetChanged();
                        Log.d("copy","copy done");
                    }catch (IOException e) {
                        Log.d("copy error", e.getMessage());
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

//    private void prepareImagePlayerListIntent(){
//        imageIntent = new Intent(context, ImagePlayerPlayList.class);
//        imageIntent.putStringArrayListExtra("imageList", (ArrayList<String>) pictureList);
//        imageIntent.putExtra("index",0);
//    }
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
        if(fabAddImage!=null) {
            fabAddImage.setVisibility(View.INVISIBLE);
            imageViewAdapter.notifyDataSetChanged();
        }
        prepareVideoPlayerListIntent();
        prepareImagePlayerListIntent();
        if(pictureList.size()>0) {
            context.startActivity(imageIntent);
        }
        else{
            if(pictureList.size()>0)
            {
                context.startActivity(videoIntent);
            }
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
                Log.d("folder",folder.toString() );

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

}
