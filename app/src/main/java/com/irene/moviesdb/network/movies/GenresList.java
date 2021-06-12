package com.irene.moviesdb.network.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by irene.
 */

public class GenresList {

    @SerializedName("genres")
    private List<Genre> genres;

    public GenresList(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
