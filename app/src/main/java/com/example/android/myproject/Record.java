package com.example.android.myproject;

public class Record {
    private String description,title,Userkey,id,key;

    private String image;
    public Record(){}

    public Record(String description, String image, String title,String Userkey,String id) {
        this.description = description;
        this.image = image;
        this.title = title;
        this.Userkey = Userkey;
        this.id = id;
    }

    public Record(String description, String title, String Userkey,String id) {
        this.description = description;
        this.title = title;
        this.Userkey = Userkey;
        this.id = id;
}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserkey() {
        return Userkey;
    }

    public void setUserkey(String userkey) {
        Userkey = userkey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
