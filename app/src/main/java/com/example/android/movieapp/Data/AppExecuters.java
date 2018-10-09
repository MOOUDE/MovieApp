package com.example.android.movieapp;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecuters  {

    private Executor diskIO;
    private Executor networkIO;
    private Executor mainThread;
    private static AppExecuters sInstance;
    private static Object Lock = new Object();;

    public AppExecuters(Executor diskIO, Executor networkIO , Executor mainThread){
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public static AppExecuters getInstance(){

        if(sInstance == null){
            synchronized (Lock){
                sInstance = new AppExecuters(Executors.newSingleThreadExecutor() ,
                        Executors.newFixedThreadPool(3) ,
                        new MainThreadExcecuter());
            }

        }
        return sInstance;
    }

    static class MainThreadExcecuter implements Executor {

        Handler MainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable runnable) {
            MainThreadHandler.post(runnable);
        }
    }
}