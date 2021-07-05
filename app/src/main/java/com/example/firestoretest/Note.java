package com.example.firestoretest;
/* Assignment: final
Campus: Ashdod
Author: Matan Tal, ID: 201492881
*/
import androidx.annotation.NonNull;

public class Note {
    private String title;
    private String description;
    private int priority;

    public Note() {
        //empty constructor needed
    }

    @NonNull
    @Override
    public String toString() {
        return "Title: " + getTitle() + "Description " + getDescription() +
                " Priority: " + getPriority();
    }

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}