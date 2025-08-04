package com.kopotron.bafvideoplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Utilities {

    public static List<String> getFileListFromDirectory(String folderPath, String[] fileType){
        List<String> filenames = new ArrayList<String>();
        try {
            File dir = new File(folderPath);
            if (dir.exists()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        for (String extension: fileType) {
                            if (files[i].getName().toLowerCase().endsWith(extension)) {
                                filenames.add(files[i].getName());
                            }
                        }
                    }

                }
            }
        }catch (Exception e){
            Log.d("file copy",e.toString() );
            e.printStackTrace();
        }

        return  filenames;
    }

    public static List<String> getFilePathListFromDirectory(String folderPath, String[] fileType){
        List<String> filePaths = new ArrayList<String>();
        try {
            File dir = new File(folderPath);
            if (dir.exists()) {
                File[] files = dir.listFiles();
                Log.d("getFilePathList",files.toString());
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        for (String extension: fileType) {
                            if (files[i].getName().toLowerCase().endsWith(extension)) {
                                filePaths.add(files[i].getAbsolutePath());
                            }
                        }
                    }

                }
            }
        }catch (Exception e){
            Log.d("file copy",e.toString() );
            e.printStackTrace();
        }

        return  filePaths;
    }
    public static void saveAircraftData(Context context, ArrayList<AircraftData> aircraftDataArrayList){
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
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
        // below line is to apply changes
        // and save data in shared prefs.
        editor.commit();
    }

    public static  ArrayList<AircraftData> loadAircraftData(Context context){
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
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
        ArrayList<AircraftData> aircraftDataArrayList = gson.fromJson(json, type);
        if (aircraftDataArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            aircraftDataArrayList = new ArrayList<>();
        }


        return aircraftDataArrayList;
    }

    public static ArrayList<String> rescanMovies(ArrayList<AircraftData> aircraftDataArrayList){
        List<String> movieList = new ArrayList<>();
        for(int i=0;i<aircraftDataArrayList.size();i++)
        {
            String mpath = aircraftDataArrayList.get(i).getAircraftDirectories().getMovieDirectoryPath();
            // Log.d("Movie directory",mpath);
            String[] videoFileExtension = new String[] {
                    "mp4",
            };
            List mpathList = getFilePathListFromDirectory(mpath, videoFileExtension);
            //Log.d("movie file paths",mpathList.toString());
            if(mpathList.size()>0)
                movieList.addAll(mpathList);
        }
        return (ArrayList<String>) movieList;
    }

    public static ArrayList<String> rescanImages(ArrayList<AircraftData> aircraftDataArrayList){
        List<String> imageList = new ArrayList<>();
        for(int i=0;i<aircraftDataArrayList.size();i++)
        {
            String ppath = aircraftDataArrayList.get(i).getAircraftDirectories().getPictureDirectoryPath();
            // Log.d("Picture file paths",ppath);
            String[] imageFileExtension = new String[] {
                    "jpg",
                    "png",
                    "gif",
                    "jpeg"
            };
            List ppathList = getFilePathListFromDirectory(ppath, imageFileExtension);
            //Log.d("Picture file paths",ppathList.toString());
            if(ppathList.size()>0)
                imageList.addAll(ppathList);
        }
        return (ArrayList<String>) imageList;
    }



    public static  ArrayList<AircraftDirectories> loadAircraftDirectories(Context context){
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        //SharedPreferences.Editor.
        // creating a variable for gson.
        Gson gson = new Gson();
        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("aircraftdirectories", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<AircraftDirectories>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        ArrayList<AircraftDirectories> aircraftDirectoriesArrayList = gson.fromJson(json, type);
        return aircraftDirectoriesArrayList;
    }

    public static void saveAircraftDirectories(Context context, ArrayList<AircraftDirectories> aircraftDirectoriesArrayList){
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        //sharedPreferences.edit().clear().commit();
        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();
        // getting data from gson and storing it in a string.
        String json = gson.toJson(aircraftDirectoriesArrayList);
        editor.putString("aircraftdirectories", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();
    }

    public static void saveMovieList(Context context, ArrayList<String> movieList) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);

        //sharedPreferences.edit().clear().commit();
        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(movieList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("movieList", json);


        // below line is to apply changes
        // and save data in shared prefs.
        editor.commit();

        // after saving data we are displaying a toast message.
        Toast.makeText(context, "Saved Movie List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }

    public static void saveImageList(Context context, ArrayList<String> imageList) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);

        //sharedPreferences.edit().clear().commit();
        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(imageList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("imageList", json);


        // below line is to apply changes
        // and save data in shared prefs.
        editor.commit();

        // after saving data we are displaying a toast message.
        Toast.makeText(context, "Saved Image List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<String> loadMovieList(Context context) {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        //SharedPreferences.Editor.
        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("movieList", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        ArrayList<String > movieList = gson.fromJson(json, type);
        if (movieList == null) {
            movieList = new ArrayList<>(); // Return empty list instead of null
        }
        return  movieList;
    }

    public static ArrayList<String> loadImageList(Context context) {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        //SharedPreferences.Editor.
        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("imageList", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        ArrayList<String > imageList = gson.fromJson(json, type);
        if (imageList == null) {
            imageList = new ArrayList<>(); // Return empty list instead of null
        }
        return  imageList;
    }

//    public static ArrayList<String> loadAircraftImageList(Context context, ) {
//        // method to load arraylist from shared prefs
//        // initializing our shared prefs with name as
//        // shared preferences.
//        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
//        //SharedPreferences.Editor.
//        // creating a variable for gson.
//        Gson gson = new Gson();
//
//        // below line is to get to string present from our
//        // shared prefs if not present setting it as null.
//        String json = sharedPreferences.getString("imageList", null);
//
//        // below line is to get the type of our array list.
//        Type type = new TypeToken<ArrayList<String>>() {}.getType();
//
//        // in below line we are getting data from gson
//        // and saving it to our array list
//        ArrayList<String > imageList = gson.fromJson(json, type);
//        return  imageList;
//    }

    public static void saveUserInfo(Context context,UserInfo userInfo){
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);

        //sharedPreferences.edit().clear().commit();
        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(userInfo);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("userInfo", json);


        // below line is to apply changes
        // and save data in shared prefs.
        editor.commit();
    }

    public static  UserInfo loadUserInfo(Context context){
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        //SharedPreferences.Editor.
        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("userInfo", null);

        // below line is to get the type of our data.
        Type type = new TypeToken<UserInfo>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        UserInfo userInfo = gson.fromJson(json, type);
        if (userInfo == null) {
            // if the array list is empty
            // creating a new array list.
            userInfo = new UserInfo("admin","123456",false);
        }
        return  userInfo;
    }

   public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
}
