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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        description = (TextView) findViewById(R.id.Movie_desc);
        name = (TextView) findViewById(R.id.Movie_name);
        rate = (TextView) findViewById(R.id.Movie_rate);
        rDate = (TextView) findViewById(R.id.rDate);
        Intent intentActivity = getIntent();

        String desc = intentActivity.getStringExtra("Description");
        String mName = intentActivity.getStringExtra("Name");
        String mRate = intentActivity.getStringExtra("rate");
        String PosterUrl = intentActivity.getStringExtra("PosterImage");
        String released_date = intentActivity.getStringExtra("Released Date");

        description.setText(desc);
        name.setText(mName);
        rate.setText(mRate);
        rDate.setText(released_date);

        posterImage = (ImageView) findViewById(R.id.PosterImage);


        Picasso.get().load(PosterUrl).fit()
                .placeholder(R.drawable.ic_launcher_background)
                .into(posterImage);


    }

}
