package com.example.multiplechoiceexam.Models;

public class TabItem {
    private String title;
    private int iconResId;

    public TabItem(String title, int iconResId){
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}