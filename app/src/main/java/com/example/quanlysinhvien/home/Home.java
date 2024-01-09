package com.example.quanlysinhvien.home;

public class Home {
    private int images;
    private String name_manager;

    public Home(int images, String name_manager) {
        this.images = images;
        this.name_manager = name_manager;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public String getName_manager() {
        return name_manager;
    }

    public void setName_manager(String name_manager) {
        this.name_manager = name_manager;
    }
}
