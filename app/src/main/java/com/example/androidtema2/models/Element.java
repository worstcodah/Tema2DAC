package com.example.androidtema2.models;

public class Element {
    public ElementType getElement() {
        return type;
    }

    ElementType type;

    public Element(ElementType type) {
        this.type = type;
    }
}
