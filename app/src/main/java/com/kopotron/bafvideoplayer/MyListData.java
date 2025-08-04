package com.kopotron.bafvideoplayer;

public class MyListData {
    public MyListData() {} // Required no-arg constructor
    private String description;
    private String imgPath;
    public MyListData(String description, String imgPath) {
        this.description = description;
        this.imgPath = imgPath;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImgPath() {
        return imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
