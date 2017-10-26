package com.app.nirogstreet.model;

/**
 * Created by Preeti on 24-10-2017.
 */
public class AskQuestionImages {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public AskQuestionImages(String images,boolean isServerImage,String id) {
        this.images = images;
        this.isServerImage=isServerImage;
        this.id=id;
    }

    private String images;

    public boolean isServerImage() {
        return isServerImage;
    }

    public void setServerImage(boolean serverImage) {
        isServerImage = serverImage;
    }

    private boolean isServerImage=false;
}
