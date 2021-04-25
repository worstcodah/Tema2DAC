package com.example.androidtema2.models;

public class Image extends Element {
    private int id;
    private String title;
    private final String url;
    private String thumbnailUrl;

    public Image(int id, String title, String url, String thumbnailUrl) {
        super(ElementType.IMAGE);
        this.id = id;
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

}
