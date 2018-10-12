package com.example.android.movieapp.UI;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Parcelable;
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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.Data.JsonHandlers.MoviesJsonHandler;
import com.example.android.movieapp.modules.Movie;
import com.example.android.movieapp.R;
import com.example.android.movieapp.Adapters.RecyclerViewAdapter;
import com.example.android.movieapp.Data.Database.okHttp;
import com.example.android.movieapp.Data.Database.AppDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

interface AsyncResponse {
    void processFinish(String output , Parcelable state);
    void roomProcessFinish(List<Movie> movie);
}

public class MainActivity extends AppCompatActivity
        implements RecyclerViewAdapter.ItemClickListener,AsyncResponse{

    private static final int NUM_COLUM =2;
    private static final int LANDSCAPE_NUM_COLUM =3;

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
    private MoviesJsonHandler JSONobj;
    private String data;
    private ProgressBar progressBar;

    private final String STATE_KEY = "statKey";
    private final String LAYOUT_KEY = "LayoutKey";
    private final String ARRAYLIST_KEY = "arrayListKey";

RecyclerView.LayoutManager layoutManager;
    public int Finalstate;
    private RecyclerView recyclerView;
    private Parcelable saveInstanceParcel;

    private final String higest_rate_url = "https://api.themoviedb.org/3/movie/top_rated?" + api_key;
    private final String popular_url = "https://api.themoviedb.org/3/movie/popular?" + api_key;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu , menu);

        return true;

    }

    private void getDataAcoordingMenu(String url , int state , Parcelable sState){
        setSavedState(state);

        new HttpFetchData(this , sState).execute(url);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.highest_rated:
             try {
                 getDataAcoordingMenu(higest_rate_url , 1 , recyclerView.getLayoutManager().onSaveInstanceState());
             }catch (Exception e){
                 e.printStackTrace();
             }
              return true;
                case R.id.popular_sort:
                    try {

                        getDataAcoordingMenu(popular_url ,0 , recyclerView.getLayoutManager().onSaveInstanceState());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
             case R.id.fav_movies:
                try{
                   callFavList();
                }catch(Exception e){
                    e.printStackTrace();
                }
         }

            return true;

    }

    private void callFavList(){
        setSavedState(2);
        new DatabaseFetchData(this).execute();
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

                if(savedInstanceState != null){

                    int position = savedInstanceState.getInt(LAYOUT_KEY);
                    All_movies = savedInstanceState.getParcelableArrayList(ARRAYLIST_KEY);
                    saveInstanceParcel = savedInstanceState.getParcelable(LAYOUT_KEY);

                    Log.d("positin"," y :"+position);
                    if(savedInstanceState.containsKey(STATE_KEY)){
                        int state = savedInstanceState.getInt(STATE_KEY);
                        Log.d(".MainActivity" , "the state is : "+state);
                        if(state == 0) {
                            getDataAcoordingMenu(popular_url, 0 , saveInstanceParcel);

                        }
                        else if(state == 1) {

                            getDataAcoordingMenu(higest_rate_url, 1 , saveInstanceParcel);

                        }
                        else if(state == 2) {
                            callFavList();

                        }
                        }

                }else {
                    Log.d(".MainActivity" , "the state is : Null ");
                    getDataAcoordingMenu(popular_url , 0 , saveInstanceParcel);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            no_internet.setVisibility(TextView.VISIBLE);
            no_internet.setText(getString(R.string.no_internet_msg));
        }
    }



    void initRecyclerView(List<Movie>moviesList , Parcelable state){

         recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        RecyclerViewAdapter adapter =
                new RecyclerViewAdapter(this, moviesList , this );



            if (this.getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT) {
                layoutManager =
                        new StaggeredGridLayoutManager(NUM_COLUM, LinearLayoutManager.VERTICAL);
            } else {
                layoutManager =
                        new StaggeredGridLayoutManager(LANDSCAPE_NUM_COLUM, LinearLayoutManager.VERTICAL);
            }


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        layoutManager.onRestoreInstanceState(state);



    }

    @Override
    public void onItemClick(int clicked_position) {

    }

    @Override
    public void processFinish(String output , Parcelable state) {
        JSONobj = new MoviesJsonHandler(output);
        All_movies = JSONobj.move_data();
        initRecyclerView(All_movies , state);
    }

    @Override
    public void roomProcessFinish(List<Movie> movie) {
        All_movies = movie;
        initRecyclerView(movie , recyclerView.getLayoutManager().onSaveInstanceState());

    }


    /*Saved State code */

    public void setSavedState(int state){
        Log.d("MainActivity" , "setSavedstat "+state);
        this.Finalstate = state;
    }
    public int getSavedState(){
        return Finalstate;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        int state = recyclerView.getScrollState();
        outState.putInt(LAYOUT_KEY , state);
        Log.d(".MainActivity","scroll state  is "+state);

        outState.putParcelableArrayList(ARRAYLIST_KEY ,  toArrayList(All_movies));
        outState.putParcelable(LAYOUT_KEY , recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putInt(STATE_KEY , getSavedState());
    }



    //To Array List
    public ArrayList toArrayList(List listData) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(listData);
        return arrayList;

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
             movieBD = AppDatabase.getInstance(getApplicationContext());
             movies = movieBD.moviedao().loadAllTask();
             movies.observe(MainActivity.this, new Observer<List<Movie>>() {
                 @Override
                 public void onChanged(@Nullable List<Movie> movies) {
                  initRecyclerView(movies , recyclerView.getLayoutManager().onSaveInstanceState());


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
        Parcelable state;
        public HttpFetchData(AsyncResponse delegate , Parcelable state){
            this.delegate = delegate;
            this.state = state;
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
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String s) {
            delegate.processFinish(result, state);

        }

    }

}
