package com.carcontroller.Retrofit.Model;

/**
 * Created by Hudeya on 8/11/2017.
 */

public class AudioPost {
    private String description;

    public AudioPost(String description){
        this.setDescription(description);
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
