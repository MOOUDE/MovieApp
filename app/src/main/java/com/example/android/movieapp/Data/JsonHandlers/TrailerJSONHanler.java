package com.example.android.movieapp.Data.JsonHandlers;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.UI.Movie_details;
import com.example.android.movieapp.modules.Movie;
import com.example.android.movieapp.modules.Trailer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TrailerJSONHanler{

    private Trailer trailer;
    private String full_url;
    private long movie_id;
    private ArrayList<Trailer> trailers;
    private Context context;


    public TrailerJSONHanler(long movie_id ,Context context){
       this.movie_id = movie_id;
       this.context = context;
    }

    public ArrayList<Trailer> JsonProcess(String jsontxt){
        JSONObject jsonObj,jsonInArray;
        String id,key,name,site,type;
        trailers = new ArrayList<Trailer>();
        int size;

        if(jsontxt == null || jsontxt.isEmpty()){
       //     showErrorMsg();
        }else{
            try {
                jsonObj = new JSONObject(jsontxt);
                JSONArray trailersArray = jsonObj.getJSONArray("results");

                for(int i =0 ; i<trailersArray.length() ; i++){

                    jsonInArray =  trailersArray.getJSONObject(i);

                    id = jsonInArray.getString("id");
                    key = jsonInArray.getString("key");
                    name = jsonInArray.getString("name");
                    site = jsonInArray.getString("site");
                    type = jsonInArray.getString("type");
                    size = jsonInArray.getInt("size");


                    Trailer trailer = new Trailer(id,key,name,site,size,type);
                    Log.d(".Movie_details" , " id :  "+trailer.getId());
                    trailers.add(trailer);
                }
                return trailers;
            }catch(Exception e){
                e.printStackTrace();
            }


        }
        return null;
    }

}
