package com.example.android.movieapp.UI;


import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.android.movieapp.Adapters.ReviewsRecyclerViewAdpater;
import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.Data.Database.okHttp;
import com.example.android.movieapp.Data.JsonHandlers.ReviewJsonHandler;
import com.example.android.movieapp.R;
import com.example.android.movieapp.modules.Review;
import com.example.android.movieapp.modules.Trailer;

import java.util.ArrayList;

public class ReviewsFetch implements
        LoaderManager.LoaderCallbacks<ArrayList<Review>>
{
    private final String apikey = BuildConfig.ApiKey;
    private long movie_id;
    private String fullUrl;
    private String reviewsUrl= "https://api.themoviedb.org/3/movie/";
    private Context context;
    private final String REVIEW_GET_KEY = "review_key";
    private final int REVIEWS_CONSTANT = 40;
    private RecyclerView recyclerView;
    public ArrayList<Review> reviews = new ArrayList<Review>();
    FragmentActivity frActivity;
    private ReviewsRecyclerViewAdpater.onReviewListItemClicked onReviewClicked;

    public ReviewsFetch(long movie_id, Context context ,RecyclerView recyclerView
    ,FragmentActivity frActivity  ){
        this.movie_id = movie_id;
        fullUrl = reviewsUrl+movie_id+"/reviews?"+apikey;
        this.context = context;
        this.recyclerView = recyclerView;
        this.frActivity = frActivity;
        callReviwes();
    }

    private void callReviwes(){

        Bundle bundel = new Bundle();
        bundel.putString(REVIEW_GET_KEY , fullUrl.toString());

        LoaderManager loaderManager = frActivity.getSupportLoaderManager();

                Loader<ArrayList<Review>> loader = loaderManager.getLoader(REVIEWS_CONSTANT);
        if(loader == null){
            loaderManager.initLoader(REVIEWS_CONSTANT , bundel , this).forceLoad();
        }else{
            loaderManager.restartLoader(REVIEWS_CONSTANT , bundel ,this).forceLoad();
        }

    Log.d(".ReviewsFetch" , "reviews called");

    }


    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int i,final Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<Review>>(context) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(bundle == null ){
                    return;
                }

            }

            @Override
            public ArrayList<Review> loadInBackground() {
               String reviewUrl = bundle.getString(REVIEW_GET_KEY);
               if(reviewUrl == null || TextUtils.isEmpty(reviewUrl)){
                   return null;
               }

               okHttp getReviews = new okHttp();
               String result;

               try{
                result = getReviews.run(reviewUrl);

                   ReviewJsonHandler reviewJsonHandler =
                           new ReviewJsonHandler(movie_id,result);
                   reviews = reviewJsonHandler.reviewsJsonProcess();
                return reviews;
               }catch (Exception e){
                   e.printStackTrace();
                   result = null;
               }

               return reviews;

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> reviews) {
    if (reviews !=null){
        setReviewsRecyclerView(reviews);
    }else {
        Log.d(".ReviewsFetch" , "null rv");
    }
   }

    @Override
    public void onLoaderReset(Loader<ArrayList<Review>> loader) {

    }

    public void setReviewsRecyclerView(ArrayList<Review> reviews){

        ReviewsRecyclerViewAdpater reviewsRecyclerViewAdpater =
                new ReviewsRecyclerViewAdpater(reviews , context);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false);


        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        Log.d(".ReviewsFetch" , "recycler set "+reviews.size());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(reviewsRecyclerViewAdpater);
       // recyclerView.setNestedScrollingEnabled(false);


    }



}
