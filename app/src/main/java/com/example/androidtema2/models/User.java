package com.example.androidtema2.models;

import androidx.annotation.Nullable;

public class User extends Element {
    private int id;
    private String name;
    private final String username;
    private final String email;
    private boolean expandPosts;

    public User(int id, String name, String username, String email) {
        super(ElementType.USER);
        this.id = id;
        this.email = email;
        this.name = name;
        this.username = username;
        expandPosts = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean hasExpandedPosts() {
        return expandPosts;
    }

    public void setExpandedPosts(boolean expandPosts) {
        this.expandPosts = expandPosts;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        assert obj != null;
        if (!(obj instanceof User))
            return false;
        return this.id == ((User) obj).id;
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
