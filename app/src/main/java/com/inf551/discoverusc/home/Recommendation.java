package com.inf551.discoverusc.home;

/**
 * Created by liwanjin on 17/10/20.
 */

public class Recommendation {
    private String title,description, image, username; //same with the Keynmae in Firebase

    //Command + N
    public Recommendation(){
        //without this app may crush
    }

    public Recommendation(String title, String description, String image, String username) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
