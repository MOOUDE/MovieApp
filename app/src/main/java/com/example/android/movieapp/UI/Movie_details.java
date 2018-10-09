package com.example.android.movieapp.UI;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.movieapp.Adapters.TrailerRecyclerViewAdapter;
import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.modules.Movie;

import com.example.android.movieapp.AppExecuters;
import com.example.android.movieapp.Data.*;
import com.example.android.movieapp.Data.AppDatabase_Impl;
import com.example.android.movieapp.Data.*;
import com.example.android.movieapp.modules.Movie;
import com.example.android.movieapp.R;
import com.example.android.movieapp.modules.Trailer;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;


interface TrailerAsyncResponse {
    void processFinish(ArrayList<Trailer> output);
}

public class Movie_details extends AppCompatActivity
        implements  TrailerAsyncResponse {

private TextView description,name,rate,rDate;
private ImageView posterImage;
private Movie movie;
private AppDatabase movieBD;
private Button AddToFav;
private Button DelFromFav;
private Intent intentActivity;
private final int TRAILERS_CONSTANT = 22;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setDetails();

        AddToFav = (Button) findViewById(R.id.add_fav);
        DelFromFav = (Button) findViewById(R.id.delete_fav);


        AddToFav.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onAddToFavioroutClicked();
            }
        });


        DelFromFav.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onDelFromFavioroutClicked();
            }
        });
        callJSONdb();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setDetails();
    }



    void callDB() {
        final Context context = getApplicationContext();
        AppExecuters.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                boolean found;
                movieBD = AppDatabase.getInstance(getApplicationContext());
                Movie mv;
                try {
                    mv = movieBD.moviedao().getById(movie.getId());
                }catch (Exception e){
                    mv = null;
                }
                if(mv == null){
                    found = false;
                }else {
                    found =true;
                }
                final boolean finalFound = found;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalFound == true) {
                            AddToFav.setVisibility(View.INVISIBLE);
                            DelFromFav.setVisibility(View.VISIBLE);
                        } else {
                            AddToFav.setVisibility(View.VISIBLE);
                            DelFromFav.setVisibility(View.INVISIBLE);
                        }
                    }

                });
            }
        });
    }


    /**********/
    void  onDelFromFavioroutClicked(){

        final Context context = getApplicationContext();
        AppExecuters.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                boolean deleted;
                try {
                    movieBD.moviedao().deleteTask(movie);
                    finish();
                    deleted = true;
                } catch (Exception e) {
                    deleted = false;
                }
                final boolean finalDeleted = deleted;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalDeleted) {
                            Toast.makeText(context, "Deleted From Faviourt", Toast.LENGTH_SHORT).show();
                            DelFromFav.setVisibility(View.INVISIBLE);
                            AddToFav.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(context, "Not From Faviourt", Toast.LENGTH_SHORT).show();
                        }
                    }

                    ;
                });

            }
        });
    }
    void onAddToFavioroutClicked() {

        final Context context = getApplicationContext();

        AppExecuters.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                boolean added = false;
                Movie mv;
                try {
                    movieBD.moviedao().insertTask(movie);
                    finish();
                    added = true;
                }catch (Exception e){
                    added = false;
                }
                final boolean finalAdded = added;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalAdded) {
                            Toast.makeText(context, "Added To the Faivourt", Toast.LENGTH_SHORT).show();
                            DelFromFav.setVisibility(View.VISIBLE);
                            AddToFav.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(context, "Already in the Faivourt", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    void setDetails(){
        description = (TextView) findViewById(R.id.Movie_desc);
        name = (TextView) findViewById(R.id.Movie_name);
        rate = (TextView) findViewById(R.id.Movie_rate);
        rDate = (TextView) findViewById(R.id.rDate);
        posterImage = (ImageView) findViewById(R.id.PosterImage);
        intentActivity = getIntent();
        movie = intentActivity.getParcelableExtra("movie");
        description.setText(movie.getDescrtptin());
        name.setText(movie.getName());
        rate.setText(movie.getRelease_date());
        rDate.setText(movie.getRelease_date());


        Picasso.get().load(movie.getPosterImg()).fit()
                .placeholder(R.drawable.ic_launcher_background)
                .into(posterImage);
        callDB();

    }

    public void setRecyclerView(ArrayList<Trailer> trailers){

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.trailer_play);
        TrailerRecyclerViewAdapter trailerRecyclerViewAdapter =
                new TrailerRecyclerViewAdapter(this , trailers);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL,
                        false );

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(trailerRecyclerViewAdapter);

    }


    @Override
    public void processFinish(ArrayList<Trailer> output) {
        Log.d(".Movie_details", "size is : "+output.size());
        setRecyclerView(output);

    }

    /////////////////////////////////////////////////////////////////////

    private void callJSONdb(){
        String  apikey = BuildConfig.ApiKey;
        String trailerUrl="https://api.themoviedb.org/3/movie/"+movie.getId()+"/videos?"+
                apikey;

        new fetchData(this).execute(trailerUrl);
    }


    class fetchData extends AsyncTask<String ,String ,String> {

        String jsonText;
        String url,result;
        TrailerJSONHanler jsonHanler;
        ArrayList<Trailer> trailers;
        public TrailerAsyncResponse delegate = null;
        public fetchData(TrailerAsyncResponse delegate){
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... strings) {
            url = strings[0];
            okHttp getData = new okHttp();
            try {
                String data = getData.run(url);
                result = data;


                TrailerJSONHanler jsonHanler =
                        new TrailerJSONHanler(movie.getId() , Movie_details.this);
                trailers = jsonHanler.JsonProcess(result);


            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            delegate.processFinish(trailers);
            Log.d(".Movie_details" , "size is "+trailers.size());
        }
    }





}
