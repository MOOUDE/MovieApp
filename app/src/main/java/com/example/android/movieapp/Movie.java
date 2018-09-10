package com.example.android.movieapp;

import android.content.res.Resources;
import android.support.annotation.NonNull;

public class Movie implements Comparable<Movie> {
    private String name;
    private String descrtptin;
    private double rating,popularity;
    private String posterImg;
    private String release_date;
    private double vote_avg;



    public Movie(String name, String descrtptin,
                 String posterImg , double vote_avg , String release_date , double popularity) {
        this.name = name;
        this.descrtptin = descrtptin;
        this.rating = rating;

        this.posterImg = "http://image.tmdb.org/t/p/w185/"+ posterImg;
        this.vote_avg = vote_avg;
        this.release_date = release_date;
        this.popularity = popularity;
    }

    public String getRelease_date() {
        return release_date;
    }

    public double getVote_avg() {
        return vote_avg;
    }

    public String getName() {
        return name;
    }

    public String getDescrtptin() {
        return descrtptin;
    }

    public double getRating() {
        return rating;
    }

    public String getPosterImg() {
        return posterImg;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVote_avg(int vote_avg) {
        this.vote_avg = vote_avg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescrtptin(String descrtptin) {
        this.descrtptin = descrtptin;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setPosterImg(String posterImg) {
        this.posterImg = posterImg;
    }


    @Override
    public int compareTo(@NonNull Movie movie) {
        if(this.getRating() > movie.getRating()){
            return 1;
        }else if(this.getVote_avg() < movie.getVote_avg()){
            return -1;
        }
        return 0;

    }
    public int compareToPopular(@NonNull Movie movie) {
        if(this.getPopularity() > movie.getPopularity()){
            return 1;
        }else if(this.getPopularity() < movie.getPopularity()){
            return -1;
        }
        return 0;

    }
}
