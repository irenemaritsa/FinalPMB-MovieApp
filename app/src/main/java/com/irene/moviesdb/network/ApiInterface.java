package com.irene.moviesdb.network;

import com.irene.moviesdb.network.movies.Movie;
import com.irene.moviesdb.network.movies.MovieCastsOfPersonResponse;
import com.irene.moviesdb.network.movies.MovieCreditsResponse;
import com.irene.moviesdb.network.movies.NowShowingMoviesResponse;
import com.irene.moviesdb.network.movies.PopularMoviesResponse;
import com.irene.moviesdb.network.movies.SimilarMoviesResponse;
import com.irene.moviesdb.network.movies.TopRatedMoviesResponse;
import com.irene.moviesdb.network.movies.UpcomingMoviesResponse;
import com.irene.moviesdb.network.people.Person;
import com.irene.moviesdb.network.videos.VideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by irene.
 */

public interface ApiInterface {

    //MOVIES

    @GET("movie/now_playing")
    Call<NowShowingMoviesResponse> getNowShowingMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/popular")
    Call<PopularMoviesResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/upcoming")
    Call<UpcomingMoviesResponse> getUpcomingMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/top_rated")
    Call<TopRatedMoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideosResponse> getMovieVideos(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Call<MovieCreditsResponse> getMovieCredits(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/similar")
    Call<SimilarMoviesResponse> getSimilarMovies(@Path("id") Integer movieId, @Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("genre/movie/list")
    Call<com.irene.moviesdb.network.movies.GenresList> getMovieGenresList(@Query("api_key") String apiKey);

    //PERSON

    @GET("person/{id}")
    Call<Person> getPersonDetails(@Path("id") Integer personId, @Query("api_key") String apiKey);

    @GET("person/{id}/movie_credits")
    Call<MovieCastsOfPersonResponse> getMovieCastsOfPerson(@Path("id") Integer personId, @Query("api_key") String apiKey);


}
