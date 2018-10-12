package com.example.android.movieapp.UI;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.Context;
import android.content.Intent;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.movieapp.Adapters.ReviewsRecyclerViewAdpater;
import com.example.android.movieapp.Adapters.TrailerRecyclerViewAdapter;
import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.Data.Database.AppDatabase;
import com.example.android.movieapp.Data.Database.okHttp;
import com.example.android.movieapp.Data.JsonHandlers.TrailerJSONHanler;
import com.example.android.movieapp.modules.Movie;

import com.example.android.movieapp.Data.Database.AppExecuters;
import com.example.android.movieapp.R;
import com.example.android.movieapp.modules.Review;
import com.example.android.movieapp.modules.Trailer;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;


interface TrailerAsyncResponse  {
    void processFinish(ArrayList<Trailer> output);
}

public class Movie_details extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>>,
        TrailerRecyclerViewAdapter.onListItemClicked
    {

private TextView description,name,rate,rDate;
private ImageView posterImage;
private Movie movie;
private AppDatabase movieBD;
private ImageView AddToFav;
private ImageView DelFromFav;
private Intent intentActivity;
private final int TRAILERS_CONSTANT = 22;
private final int Reviews_CONSTANT = 40;
private final String TRAILER_GET_KEY = "trailer_key";
private final String REVIEW_GET_KEY = "review_key";
private ProgressBar progressBar;
private RecyclerView recyclerView,reviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        movieBD = AppDatabase.getInstance(Movie_details.this);

        setDetails();

        AddToFav = (ImageView) findViewById(R.id.add_fav);
        DelFromFav = (ImageView) findViewById(R.id.delete_fav);
        recyclerView = (RecyclerView) findViewById(R.id.trailer_play);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        AddToFav.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onAddToFavioroutClicked(Movie_details.this);
            }
        });


        DelFromFav.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onDelFromFavioroutClicked(Movie_details.this);
            }
        });
        callJSONdb();

        reviesRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycleView);
        new ReviewsFetch(movie.getId() ,this,reviesRecyclerView , this);



    }



    @Override
    protected void onResume() {
        super.onResume();
        setDetails();
    }



    void callDB() {
        final Context context = Movie_details.this;
        AppExecuters.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                boolean found;
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
    void  onDelFromFavioroutClicked(final Context context){

        AppExecuters.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                boolean deleted;
                try {
                    movieBD.moviedao().deleteTask(movie);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(".Movie_details","finshed ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    void onAddToFavioroutClicked(final Context context) {

        AppExecuters.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                boolean added = false;
                Movie mv;
                try {
                   movieBD.moviedao().insertTask(movie);
                    added = true;

                }catch (Exception e){
                    added = false;
                }
                final boolean finalAdded = added;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalAdded) {
                            DelFromFav.setVisibility(View.VISIBLE);
                            AddToFav.setVisibility(View.INVISIBLE);
                            Toast.makeText(context, "Added To the Faivourt", Toast.LENGTH_SHORT).show();
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
        rDate.setText(String.valueOf(movie.getVote_avg()));


        Picasso.get().load(movie.getPosterImg()).fit()
                .placeholder(R.drawable.ic_launcher_background)
                .into(posterImage);
        callDB();


    }

    public void setRecyclerView(ArrayList<Trailer> trailers){


        TrailerRecyclerViewAdapter trailerRecyclerViewAdapter =
                new TrailerRecyclerViewAdapter(this , trailers , this);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL,
                        false );

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(trailerRecyclerViewAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }




    /////////////////////////////////////////////////////////////////////

    private void callJSONdb(){
        String  apikey = BuildConfig.ApiKey;
        String trailerUrl=
                "https://api.themoviedb.org/3/movie/"+movie.getId()+"/videos?"+
                apikey;

        Bundle bundel = new Bundle();
        bundel.putString(TRAILER_GET_KEY , trailerUrl.toString());


        LoaderManager loaderManager = getSupportLoaderManager();

        Loader<ArrayList<Trailer>> loader = loaderManager.getLoader(TRAILERS_CONSTANT);
        if(loader == null){
            loaderManager.initLoader(TRAILERS_CONSTANT , bundel , this).forceLoad();
        }else{
            loaderManager.restartLoader(TRAILERS_CONSTANT , bundel ,this).forceLoad();
        }



    }




    ArrayList<Trailer> trailers;

    @Override
    public Loader<ArrayList<Trailer>> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<Trailer>>(this){

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(bundle == null ){
                    return;
                }

            }


            @Override
            public ArrayList<Trailer> loadInBackground() {
                String trailersUrl = bundle.getString(TRAILER_GET_KEY);
                if(trailersUrl == null || TextUtils.isEmpty(trailersUrl)){
                    return null;
                }
                //

                okHttp getData = new okHttp();
                String result;
                try {
                    String data = getData.run(trailersUrl);
                    result = data;


                    TrailerJSONHanler jsonHanler =
                            new TrailerJSONHanler(movie.getId() , Movie_details.this);
                    trailers = jsonHanler.JsonProcess(result);


                } catch (IOException e) {
                    e.printStackTrace();
                    result = null;
                }

                //
                Log.d(".Movie_details", "sizes is " + trailers.size());
                return trailers;
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> s) {
        Log.d(".Movie_details", "sizes is " + s.size());

        if (s != null && s.size() > 0) {
            setRecyclerView(s);
            Log.d(".Movie_details", "size is " + s.size());
        }else{
            Log.d(".Movie_details", "Null trailers");
        }
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);


    }
    @Override
    public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

    }

    @Override
    public void onItemClicked(int position) {

        Intent appIntent =
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse("vnd.youtube:" + trailers.get(position).getKey()));
        Intent webIntent =
                new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(position).getKey()));
        try {
            startActivity(appIntent);
        } catch (Exception ex) {
            startActivity(webIntent);
        }

    }






}
