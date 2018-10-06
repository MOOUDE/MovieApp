package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
public class Movie_details extends AppCompatActivity {
private TextView description,name,rate,rDate;
private ImageView posterImage;
private Movie movie;
private AppDatabase movieBD;
private Button AddToFav;
private Button DelFromFav;
private Intent intentActivity;
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
    void  onDelFromFavioroutClicked(){

        final Context context = getApplicationContext();
        AppExecuters.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        boolean found= false;
                        boolean deleted= false;
                        try {
                            Movie mv = movieBD.moviedao().getById(movie.getId());
                            found = true;
                        }catch (Exception e){
                            found = false;
                        }
                        if(found){
                            movieBD.moviedao().deleteTask(movie);
                            finish();
                            deleted = true;
                        }
                        final boolean finalDeleted = deleted;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(finalDeleted) {
                                    Toast.makeText(context, "Deleted From Faviourt", Toast.LENGTH_SHORT).show();
                                    DelFromFav.setVisibility(View.INVISIBLE);
                                    AddToFav.setVisibility(View.VISIBLE);
                                }else{
                                Toast.makeText(context, "Not From Faviourt", Toast.LENGTH_SHORT).show();
                            }
                           }
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
                    mv = movieBD.moviedao().getById(movie.getId());
                } catch (Exception e) {
                    mv = null;
                }

                final Movie finalMv = mv;

                if (finalMv == null) {
                    movieBD.moviedao().insertTask(movie);
                    finish();
                    added = true;
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


}
