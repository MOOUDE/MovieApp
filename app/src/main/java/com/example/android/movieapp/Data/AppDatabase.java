package com.example.android.movieapp.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.movieapp.modules.Movie;

@Database(entities = Movie.class , exportSchema = false , version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static Object Lock = new Object();
    private static final String DAABAS_NAME = "Movies";
    private static AppDatabase sInsance;

    public static  AppDatabase getInstance(Context context) {
        if (sInsance == null) {
            synchronized (Lock) {
                sInsance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class
                        , AppDatabase.DAABAS_NAME)

                        .build();
            }
        }

        return sInsance;
    }
    public abstract movieDao moviedao();

}
