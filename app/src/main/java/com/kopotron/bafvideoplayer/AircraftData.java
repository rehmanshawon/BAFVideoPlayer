package com.kopotron.bafvideoplayer;

import java.io.File;

public class AircraftData {
    public AircraftData() {} // Required no-arg constructor
    private String title;
    private File aircraftImage;
    private AircraftDirectories aircraftDirectories;
    private Boolean deletable=false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getAircraftImage() {
        return aircraftImage;
    }
    public AircraftDirectories getAircraftDirectories(){return aircraftDirectories;}

    public Boolean getDeletable() {
        return deletable;
    }

    public void setAircraftImage(File imageFile) {
        this.aircraftImage = imageFile;
    }
    public void setAircraftDirectories(AircraftDirectories aircraftDirectories){this.aircraftDirectories=aircraftDirectories;}

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }

    public AircraftData(String title, File imageFile, AircraftDirectories aircraftDirectories, Boolean deletable) {
        this.title = title;
        this.aircraftImage = imageFile;
        this.aircraftDirectories=aircraftDirectories;
        this.deletable=deletable;
    }
}
