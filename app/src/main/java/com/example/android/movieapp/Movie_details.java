package com.example.android.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Movie_details extends AppCompatActivity {
private TextView description,name,rate,rDate;
private ImageView posterImage;
private  Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        setDetails();

    }
    void setDetails(){
        description = (TextView) findViewById(R.id.Movie_desc);
        name = (TextView) findViewById(R.id.Movie_name);
        rate = (TextView) findViewById(R.id.Movie_rate);
        rDate = (TextView) findViewById(R.id.rDate);
        posterImage = (ImageView) findViewById(R.id.PosterImage);


        Intent intentActivity = getIntent();

         movie = intentActivity.getParcelableExtra("movie");

        description.setText(movie.getDescrtptin());
        name.setText(movie.getName());
        rate.setText(String.valueOf(movie.getVote_avg()));
        rDate.setText(movie.getRelease_date());


        Picasso.get().load(movie.getPosterImg()).fit()
                .placeholder(R.drawable.ic_launcher_background)
                .into(posterImage);
    }

}
