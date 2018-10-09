package com.example.android.movieapp.UI;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
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

import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.Data.JsonHandler;
import com.example.android.movieapp.modules.Movie;
import com.example.android.movieapp.R;
import com.example.android.movieapp.Adapters.RecyclerViewAdapter;
import com.example.android.movieapp.Data.okHttp;
import com.example.android.movieapp.Data.AppDatabase;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;

interface AsyncResponse {
    void processFinish(String output);
    void roomProcessFinish(List<Movie> movie);
}

public class MainActivity extends AppCompatActivity
        implements RecyclerViewAdapter.ItemClickListener,AsyncResponse{

    private static final int NUM_COLUM =2;
    public String result;
    private OkHttpClient okHttpClient;
    private List<Movie> All_movies;
    public String JsonData;
    public static TextView no_internet;
    private Context mContext;

    public static String name;
    private String base_url = "https://api.themoviedb.org/3/movie/popular?";
    private String api_key = BuildConfig.ApiKey;
    private String url = base_url+api_key;
    private String str_result;
    private JsonHandler JSONobj;
    private String data;




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
                 new HttpFetchData(this).execute(url);


             }catch (Exception e){
                 e.printStackTrace();
             }
                return true;
                case R.id.popular_sort:

                    try {

                        url = "https://api.themoviedb.org/3/movie/popular?" + api_key;
                        new HttpFetchData(this).execute(url);


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
             case R.id.fav_movies:
                try{
                    new DatabaseFetchData(this).execute();
                }catch(Exception e){
                    e.printStackTrace();
                }
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
                url = "https://api.themoviedb.org/3/movie/popular?" + api_key;
                new HttpFetchData(this).execute(url);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            no_internet.setVisibility(TextView.VISIBLE);
            no_internet.setText(getString(R.string.no_internet_msg));
        }
    }



    void initRecyclerView(List<Movie>moviesList ){

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

    @Override
    public void processFinish(String output) {
        JSONobj = new JsonHandler(output);
        All_movies = JSONobj.move_data();
        initRecyclerView(All_movies);
    }

    @Override
    public void roomProcessFinish(List<Movie> movie) {
        All_movies = movie;
        initRecyclerView(movie);

    }


    //*********** Fetching data from room ****************//
    class DatabaseFetchData extends AsyncTask<Void, Void, Void> {

        public LiveData<List<Movie>> movies;
        private AppDatabase movieBD;
        public AsyncResponse delegate = null;
        public DatabaseFetchData(AsyncResponse delegate){
            this.delegate = delegate;
        }

        @Override
            protected Void doInBackground(Void... params) {
             movieBD = com.example.android.movieapp.Data.AppDatabase_Impl.getInstance(getApplicationContext());
             movies = movieBD.moviedao().loadAllTask();
             movies.observe(MainActivity.this, new Observer<List<Movie>>() {
                 @Override
                 public void onChanged(@Nullable List<Movie> movies) {
                  initRecyclerView(movies);


                 }
             });

                return null;
            }
        @Override
        protected void onPostExecute(Void param) {

           // delegate.roomProcessFinish( movies );

        }

    }



    //*********** Fetching data from online json ****************//
    class HttpFetchData extends AsyncTask<String,String,String>{
        String jsonText;
        public AsyncResponse delegate = null;
        public HttpFetchData(AsyncResponse delegate){
            this.delegate = delegate;
        }

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

            delegate.processFinish(result);

        }

    }

}
