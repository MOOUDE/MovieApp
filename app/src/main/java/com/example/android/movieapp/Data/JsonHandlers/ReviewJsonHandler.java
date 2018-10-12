package com.example.android.movieapp.Data.JsonHandlers;

import android.text.TextUtils;

import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.modules.Review;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewJsonHandler {

    private Review review;
    private ArrayList<Review> reviews;
    private final String REVIEWS_URL = "https://api.themoviedb.org/3/movie/";
    private long movie_id;
    private String jsonText,fullURL;
    private final String apikey = BuildConfig.ApiKey;


    private String author,id,content,url;



    private JSONObject jsonObj,jsonInArray;


    public ReviewJsonHandler(long movie_id, String jsonText){

        this.movie_id = movie_id;
        this.jsonText = jsonText;
        fullURL = REVIEWS_URL+movie_id+"/reviews?"+apikey;
    }
    public ArrayList<Review> reviewsJsonProcess(){
        reviews = new ArrayList<Review>();
        Review review;
        if(jsonText == null || TextUtils.isEmpty(jsonText)){
            return reviews;
        }else{

            try {

                jsonObj = new JSONObject(jsonText);
                JSONArray reviewsArray = jsonObj.getJSONArray("results");

                for(int i=0 ; i<reviewsArray.length(); i++){

                    jsonInArray =  reviewsArray.getJSONObject(i);

                    id = jsonInArray.getString("id");
                    author = jsonInArray.getString("author");
                    content = jsonInArray.getString("content");
                    url = jsonInArray.getString("url");

                    review = new Review(id,author,content,url);
                    reviews.add(review);

                }

            return reviews;
            }catch (Exception e){
                e.printStackTrace();
            }


        }
        return null;

    }

}
