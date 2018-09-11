package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements RecyclerViewAdapter.ItemClickListener{

    private static final int NUM_COLUM =2;
    public String result;
    private OkHttpClient okHttpClient;
    private ArrayList<Movie> All_movies;
    public String JsonData;
    public static TextView no_internet;
    private Context mContext;

    public static String name;
    private String base_url = "https://api.themoviedb.org/3/discover/movie?";
    private String api_key = BuildConfig.ApiKey;
    private String url = base_url+api_key;
    private String str_result;
    private JsonHandler JSONobj;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu , menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.highest_rated:

             try {
                 url = "https://api.themoviedb.org/3/movie/top_rated?" + api_key;

                 str_result = new HttpFetchData().execute(url).get();

                 Log.i("MainActivity", str_result);
                 JSONobj = new JsonHandler(str_result);

                 All_movies = JSONobj.move_data();


                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                     All_movies.sort(new Comparator<Movie>() {
                         @Override
                         public int compare(Movie movie, Movie t1) {
                             return movie.compareTo(t1);
                         }
                     });
                 }


                 initRecyclerView(All_movies);
             }catch (Exception e){
                 e.printStackTrace();
             }

                return true;

                case R.id.popular_sort:

                    try {


                        url = "https://api.themoviedb.org/3/movie/top_rated?" + api_key;

                        str_result = new HttpFetchData().execute(url).get();

                        Log.i("MainActivity", str_result);
                        JSONobj = new JsonHandler(str_result);

                        All_movies = JSONobj.move_data();


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            All_movies.sort(new Comparator<Movie>() {
                                @Override
                                public int compare(Movie movie, Movie t1) {
                                    return movie.compareToPopular(t1);
                                }
                            });
                        }


                        url = "https://api.themoviedb.org/3/movie/popular?" + api_key;

                        initRecyclerView(All_movies);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
         }

            return true;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //var initilaize

        no_internet = (TextView) findViewById(R.id.no_internet);

        if(isOnline()) {
            try {

                url = "https://api.themoviedb.org/3/discover/movie?" + api_key;

                str_result = new HttpFetchData().execute(url).get();

                Log.i("MainActivity", str_result);
                JSONobj = new JsonHandler(str_result);

                All_movies = JSONobj.move_data();
                initRecyclerView(All_movies);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            no_internet.setVisibility(TextView.VISIBLE);
            no_internet.setText(getString(R.string.no_internet_msg));
        }
    }



    void initRecyclerView(ArrayList<Movie>moviesList ){


       RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        RecyclerViewAdapter adapter =
                new RecyclerViewAdapter(this, moviesList , this );

        StaggeredGridLayoutManager layoutManager =
        new StaggeredGridLayoutManager(NUM_COLUM , LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(int clicked_position) {

    }


    class HttpFetchData extends AsyncTask<String,String,String>{
        String jsonText;



        @Override
        protected String doInBackground(String... strings) {
            url = strings[0];
            Log.i("MainActivity", url );
            okHttp getData = new okHttp();
            try {
                String data = getData.run(url);
                result = data;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {


        }

    }

}
