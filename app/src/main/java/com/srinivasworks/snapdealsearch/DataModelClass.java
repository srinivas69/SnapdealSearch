package com.srinivasworks.snapdealsearch;

/**
 * Created by dell on 24/10/17.
 */

public class DataModelClass {

    private String title;
    private String url;

    public DataModelClass(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
