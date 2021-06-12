package com.irene.moviesdb.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.irene.moviesdb.R;
import com.irene.moviesdb.fragments.FavouriteMoviesFragment;
import com.irene.moviesdb.fragments.FragmentViewAllMovies;
import com.irene.moviesdb.fragments.MoviesFragment;
import com.irene.moviesdb.utils.Constants;
import com.irene.moviesdb.utils.NetworkConnection;

public class MainActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    private boolean doubleBackToExitPressedOnce;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (sharedPreferences.getBoolean(Constants.FIRST_TIME_LAUNCH, true)) {
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putBoolean(Constants.FIRST_TIME_LAUNCH, false);
            sharedPreferencesEditor.apply();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bnv_main);
        bottomNav.setOnNavigationItemSelectedListener(this);
        setSelectedItem(bottomNav);
        toolbar.setTitle("\t" + R.string.app_name);
//        getActionBar().setDisplayShowHomeEnabled(true);
//        getActionBar().setDisplayUseLogoEnabled(true);
        toolbar.setLogo(R.drawable.ic__home_24);
        setFragment(new MoviesFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        int sortBy = 0;
        switch (menuItem.getItemId()) {
            case R.id.item_home:
                setTitle(R.string.app_name);
                toolbar.setLogo(R.drawable.ic__home_24);
                sortBy = Constants.NOW_SHOWING_MOVIES_TYPE;
                fragment = new MoviesFragment();
                break;
            case R.id.item_airing_today:
                setTitle("\t" + "Now Playing");
                toolbar.setLogo(R.drawable.ic_airing_today_white);
                sortBy = Constants.NOW_SHOWING_MOVIES_TYPE;
                fragment = new FragmentViewAllMovies();
                break;
            case R.id.item_popular:
                setTitle("\t" + "Popular");
                toolbar.setLogo(R.drawable.ic_popular_white);
                sortBy = Constants.POPULAR_MOVIES_TYPE;
                fragment = new FragmentViewAllMovies();
                break;
            case R.id.item_top_rated:
                setTitle("\t" + "Upcoming");
                toolbar.setLogo(R.drawable.ic_update_24);
                sortBy = Constants.UPCOMING_MOVIES_TYPE;
                fragment = new FragmentViewAllMovies();
                break;
            case R.id.item_favorite:
                setTitle("\t" + "Favorite");
                toolbar.setLogo(R.drawable.ic_favorite_white);
                fragment = new FavouriteMoviesFragment();
                break;
        }
        if (fragment != null) {
            // Method that handle which data to show base on @sortBy params
            startFragment(fragment, sortBy);
            return true;
        }
        return false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final SearchView searchView = (SearchView) item.getActionView();
                searchView.setQueryHint(getResources().getString(R.string.search_movies_tv_shows_people));

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (!NetworkConnection.isConnected(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, R.string.no_network, Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        intent.putExtra(Constants.QUERY, query);
                        startActivity(intent);
                        item.collapseActionView();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
                return true;
            case R.id.item_language_setting:
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void setActionBar(String title, int logo) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("\t" + title);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setLogo(logo);
        }
    }

    private void setSelectedItem(BottomNavigationView bottomNavigationView) {
        if (getIntent().getStringExtra("SELECTED_FRAGMENT") != null) {
            switch (getIntent().getStringExtra("SELECTED_FRAGMENT")) {
                case "home":
                    bottomNavigationView.setSelectedItemId(R.id.item_airing_today);
                    break;
                case "airing_today":
                    bottomNavigationView.setSelectedItemId(R.id.item_airing_today);
                    break;
                case "popular":
                    bottomNavigationView.setSelectedItemId(R.id.item_popular);
                    break;
                case "top_rated":
                    bottomNavigationView.setSelectedItemId(R.id.item_top_rated);
                    break;
                case "favorite":
                    bottomNavigationView.setSelectedItemId(R.id.item_favorite);
                    break;
            }
        } else {
            bottomNavigationView.setSelectedItemId(R.id.item_home);
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_fragment_container, fragment);
        fragmentTransaction.commit();
    }
    private void startFragment(Fragment fragment, int bundle) {
        if (bundle != 0) {
            Bundle sortBy = new Bundle();
            sortBy.putInt("moviesType", bundle);
            fragment.setArguments(sortBy);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_activity_fragment_container, fragment)
                .commit();
    }
}
