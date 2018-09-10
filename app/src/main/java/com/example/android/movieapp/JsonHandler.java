package com.example.android.movieapp;

import android.content.res.Resources;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonHandler {

    private TextView test;
    private JSONObject jsonObj;
    private ArrayList<Movie> movies_list;
    private JSONObject movie_json;
    private String movie_triler;
    private String movie_poster;
    private String movie_overview;
    private double movie_rate;
    private Movie movie_obj;
    private double popularity;
    private String release_date;
    private double vote_avg;
    private String movie_name;



    public JsonHandler(String text){
        movies_list = new ArrayList<Movie>();

        try {

            jsonObj = new JSONObject(text);

            JSONArray moves_array = jsonObj.getJSONArray("results");

            for(int i =0 ; i<moves_array.length() ; i++){

                movie_json =  moves_array.getJSONObject(i);


                movie_poster = movie_json.getString("poster_path");
                movie_overview = movie_json.getString("overview");
                movie_name = movie_json.getString("title");
                popularity = movie_json.getDouble("popularity");
                vote_avg = movie_json.getDouble("vote_average");
                release_date = movie_json.getString("release_date");


            movie_obj = new Movie(movie_name,movie_overview
                    ,movie_poster,vote_avg , release_date , popularity);
                movies_list.add(movie_obj);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    ArrayList<Movie> move_data(){
        return(movies_list);
    }


}
