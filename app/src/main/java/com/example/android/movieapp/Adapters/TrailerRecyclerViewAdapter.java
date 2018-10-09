package com.example.android.movieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieapp.R;
import com.example.android.movieapp.modules.Movie;
import com.example.android.movieapp.modules.Trailer;

import java.util.ArrayList;
import java.util.List;



public class TrailerRecyclerViewAdapter extends
        RecyclerView.Adapter<TrailerRecyclerViewAdapter.viewHolder>{

    private Context context;
    private ArrayList<Trailer> trailers;


    public TrailerRecyclerViewAdapter(Context context , ArrayList<Trailer> trailers){

        this.context = context;
        this.trailers = trailers;

    }


    @NonNull
    @Override
    public TrailerRecyclerViewAdapter.viewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.trailer ,parent, false);

        viewHolder holder = new viewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull TrailerRecyclerViewAdapter.viewHolder holder,
                                 int position) {
    String movieName = trailers.get(position).getName();

    holder.movie_url.setText(movieName);

    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView movie_url;

        public viewHolder(View itemView) {
            super(itemView);
            this.movie_url = (TextView) itemView.findViewById(R.id.trailer_url);

        }

        @Override
        public void onClick(View view) {

        }
    }

}
