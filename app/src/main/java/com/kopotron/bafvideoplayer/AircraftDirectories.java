package com.kopotron.bafvideoplayer;

import java.io.File;

public class AircraftDirectories {
    private String movieDirectoryPath;
    private File movieDirectory;
    private String pictureDirectoryPath;
    private File pictureDirectory;


    public String getMovieDirectoryPath(){
        return movieDirectoryPath;
    }
    public File getMovieDirectory(){return movieDirectory;}
    public File getPictureDirectory(){return pictureDirectory;}
    public String getPictureDirectoryPath(){
        return pictureDirectoryPath;
    }

    public void setMovieDirectoryPath(String movieDirectoryPath){
        this.movieDirectoryPath = movieDirectoryPath;
    }
    public void setMovieDirectory(File movieDirectory){
        this.movieDirectory=movieDirectory;
    }

    public  void  setPictureDirectoryPath(String pictureDirectoryPath){
        this.pictureDirectoryPath = pictureDirectoryPath;
    }
    public void setPictureDirectory(File pictureDirectory){
        this.pictureDirectory=pictureDirectory;
    }

    public AircraftDirectories(String movieDirectoryPath,String pictureDirectoryPath,File movieDirectory, File pictureDirectory){
        this.pictureDirectoryPath = pictureDirectoryPath;
        this.movieDirectoryPath = movieDirectoryPath;
        this.pictureDirectory=pictureDirectory;
        this.movieDirectory=movieDirectory;
    }
}
