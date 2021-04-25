package com.example.androidtema2.models;

public class Album extends Element {

    private int id;
    private final String body;

    public Album(int id, String body) {
        super(ElementType.ALBUM);
        this.id = id;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

}
