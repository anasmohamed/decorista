package com.decorista.anas.decorista;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Image implements Serializable {
    private String link;
    private int likes;







    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
