package com.kopotron.bafvideoplayer;

import java.io.File;

public class VideoData {
    public VideoData() {} // Required no-arg constructor
    private String title;
    private String videoPath;
    private File videoFile;
    public VideoData(String title, String videoPath, File videoFile){
        this.title=title;
        this.videoPath=videoPath;
        this.videoFile=videoFile;
    }

    public String getTitle(){return title;}
    public String getVideoPath(){return  videoPath;}
    public File getVideoFile(){return videoFile;}
    public void setTitle(String title){this.title=title;}
    public void setVideoPath(String videoPath){this.videoPath=videoPath;}
    public void setVideoFile(File videoFile){this.videoFile=videoFile;}
}
