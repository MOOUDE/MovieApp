package com.example.android.movieapp;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Movie implements Comparable<Movie>, Parcelable {
    private String name;
    private String descrtptin;
    private double rating, popularity;
    private String posterImg;
    private String release_date;
    private double vote_avg;


    public Movie(String name, String descrtptin,
                 String posterImg, double vote_avg, String release_date, double popularity) {
        this.name = name;
        this.descrtptin = descrtptin;
        this.rating = rating;

        this.posterImg = "http://image.tmdb.org/t/p/w185/" + posterImg;
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

    public void setVote_avg(double vote_avg) {
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
        if (this.getRating() > movie.getRating()) {
            return 1;
        } else if (this.getVote_avg() < movie.getVote_avg()) {
            return -1;
        }
        return 0;

    }

    public int compareToPopular(@NonNull Movie movie) {
        if (this.getPopularity() > movie.getPopularity()) {
            return 1;
        } else if (this.getPopularity() < movie.getPopularity()) {
            return -1;
        }
        return 0;

    }

    @Override
    public int describeContents() {
        return 0;

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
    public Movie(Parcel parsel){
        setName(parsel.readString());
        setDescrtptin(parsel.readString());
        setPosterImg(parsel.readString());
        setRelease_date(parsel.readString());
        setPopularity(parsel.readDouble());
        setRating(parsel.readDouble());
        setVote_avg(parsel.readDouble());
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(getName());
        parcel.writeString(getDescrtptin());
        parcel.writeString(getPosterImg());
        parcel.writeString(getRelease_date());
        parcel.writeDouble(getPopularity());
        parcel.writeDouble(getRating());
        parcel.writeDouble(getVote_avg());

    }
}
