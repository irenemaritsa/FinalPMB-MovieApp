package com.irene.moviesdb.network.movies;

import com.google.gson.annotations.SerializedName;

/**
 * Created by irene .
 */

public class Genre {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String genreName;

    public Genre(Integer id, String genreName) {
        this.id = id;
        this.genreName = genreName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
