package com.kopotron.bafvideoplayer;

import java.io.File;

public class ImageData {
    public ImageData() {} // Required no-arg constructor
    private String title;
    private String imagePath;
    private File imageFile;
    public ImageData(String title, String imagePath, File imageFile){
        this.title=title;
        this.imagePath=imagePath;
        this.imageFile=imageFile;
    }

    public String getTitle(){return title;}
    public String getImagePath(){return  imagePath;}
    public File getImageFile(){return imageFile;}
    public void setTitle(String title){this.title=title;}
    public void setImagePath(String imagePath){this.imagePath=imagePath;}
    public void setImageFile(File imageFile){this.imageFile=imageFile;}
}
