package com.irene.moviesdb.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irene.moviesdb.R;
import com.irene.moviesdb.adapters.MovieBriefsSmallAdapter;
import com.irene.moviesdb.network.ApiClient;
import com.irene.moviesdb.network.ApiInterface;
import com.irene.moviesdb.network.movies.MovieBrief;
import com.irene.moviesdb.network.movies.NowShowingMoviesResponse;
import com.irene.moviesdb.network.movies.PopularMoviesResponse;
import com.irene.moviesdb.network.movies.TopRatedMoviesResponse;
import com.irene.moviesdb.network.movies.UpcomingMoviesResponse;
import com.irene.moviesdb.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentViewAllMovies extends Fragment {

    private RecyclerView mRecyclerView;
    private List<MovieBrief> mMovies;
    private MovieBriefsSmallAdapter mMoviesAdapter;

    private int mMovieType;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    private Call<NowShowingMoviesResponse> mNowShowingMoviesCall;
    private Call<PopularMoviesResponse> mPopularMoviesCall;
    private Call<UpcomingMoviesResponse> mUpcomingMoviesCall;
    private Call<TopRatedMoviesResponse> mTopRatedMoviesCall;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_movies, container, false);
        mMovieType = getArguments().getInt("moviesType");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_view_all);
        mMovies = new ArrayList<>();
        mMoviesAdapter = new MovieBriefsSmallAdapter(getActivity(), mMovies);
        mRecyclerView.setAdapter(mMoviesAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // irene : use Paging Library
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    loadMovies(mMovieType);
                    loading = true;
                }

            }
        });

        loadMovies(mMovieType);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mMoviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mNowShowingMoviesCall != null) mNowShowingMoviesCall.cancel();
        if (mPopularMoviesCall != null) mPopularMoviesCall.cancel();
        if (mUpcomingMoviesCall != null) mUpcomingMoviesCall.cancel();
        if (mTopRatedMoviesCall != null) mTopRatedMoviesCall.cancel();
    }

    private void loadMovies(int movieType) {
        if (pagesOver) return;

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        mSmoothProgressBar.progressiveStart();

        switch (movieType) {
            case Constants.NOW_SHOWING_MOVIES_TYPE:
                mNowShowingMoviesCall = apiService.getNowShowingMovies((Constants.APIKEY), presentPage, "US");
                mNowShowingMoviesCall.enqueue(new Callback<NowShowingMoviesResponse>() {
                    @Override
                    public void onResponse(Call<NowShowingMoviesResponse> call, Response<NowShowingMoviesResponse> response) {
                        if (!response.isSuccessful()) {
                            mNowShowingMoviesCall = call.clone();
                            mNowShowingMoviesCall.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

//                        mSmoothProgressBar.progressiveStop();
                        for (MovieBrief movieBrief : response.body().getResults()) {
                            if (movieBrief != null && movieBrief.getTitle() != null && movieBrief.getPosterPath() != null)
                                mMovies.add(movieBrief);
                        }
                        mMoviesAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }

                    @Override
                    public void onFailure(Call<NowShowingMoviesResponse> call, Throwable t) {

                    }
                });
                break;
            case Constants.POPULAR_MOVIES_TYPE:
                mPopularMoviesCall = apiService.getPopularMovies((Constants.APIKEY), presentPage, "US");
                mPopularMoviesCall.enqueue(new Callback<PopularMoviesResponse>() {
                    @Override
                    public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                        if (!response.isSuccessful()) {
                            mPopularMoviesCall = call.clone();
                            mPopularMoviesCall.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

//                        mSmoothProgressBar.progressiveStop();
                        for (MovieBrief movieBrief : response.body().getResults()) {
                            if (movieBrief != null && movieBrief.getTitle() != null && movieBrief.getPosterPath() != null)
                                mMovies.add(movieBrief);
                        }
                        mMoviesAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }

                    @Override
                    public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {

                    }
                });
                break;
            case Constants.UPCOMING_MOVIES_TYPE:
                mUpcomingMoviesCall = apiService.getUpcomingMovies((Constants.APIKEY), presentPage, "US");
                mUpcomingMoviesCall.enqueue(new Callback<UpcomingMoviesResponse>() {
                    @Override
                    public void onResponse(Call<UpcomingMoviesResponse> call, Response<UpcomingMoviesResponse> response) {
                        if (!response.isSuccessful()) {
                            mUpcomingMoviesCall = call.clone();
                            mUpcomingMoviesCall.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

//                        mSmoothProgressBar.progressiveStop();
                        for (MovieBrief movieBrief : response.body().getResults()) {
                            if (movieBrief != null && movieBrief.getTitle() != null && movieBrief.getPosterPath() != null)
                                mMovies.add(movieBrief);
                        }
                        mMoviesAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }

                    @Override
                    public void onFailure(Call<UpcomingMoviesResponse> call, Throwable t) {

                    }
                });
                break;
            case Constants.TOP_RATED_MOVIES_TYPE:
                mTopRatedMoviesCall = apiService.getTopRatedMovies((Constants.APIKEY), presentPage, "US");
                mTopRatedMoviesCall.enqueue(new Callback<TopRatedMoviesResponse>() {
                    @Override
                    public void onResponse(Call<TopRatedMoviesResponse> call, Response<TopRatedMoviesResponse> response) {
                        if (!response.isSuccessful()) {
                            mTopRatedMoviesCall = call.clone();
                            mTopRatedMoviesCall.enqueue(this);
                            return;
                        }

                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;

//                        mSmoothProgressBar.progressiveStop();
                        for (MovieBrief movieBrief : response.body().getResults()) {
                            if (movieBrief != null && movieBrief.getTitle() != null && movieBrief.getPosterPath() != null)
                                mMovies.add(movieBrief);
                        }
                        mMoviesAdapter.notifyDataSetChanged();
                        if (response.body().getPage() == response.body().getTotalPages())
                            pagesOver = true;
                        else
                            presentPage++;
                    }

                    @Override
                    public void onFailure(Call<TopRatedMoviesResponse> call, Throwable t) {

                    }
                });
                break;
        }
    }



}
