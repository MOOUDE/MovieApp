package com.example.android.movieapp.Data.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.movieapp.modules.Movie;

import java.util.List;

@Dao
public interface movieDao {
 @Query("SELECT *FROM Movies")
 LiveData<List<Movie>> loadAllTask();

 @Insert (onConflict = OnConflictStrategy.REPLACE)
  void insertTask(Movie movie);

 @Delete
    void deleteTask(Movie movie);

 @Update(onConflict =  OnConflictStrategy.REPLACE)
    void updateTask(Movie movie);

 @Query("SELECT * FROM Movies WHERE id= :id")
    Movie getById(long id);

}
