package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Movie> movies ;
    private String posterImg,movieName;
    private Context context;
    final private ItemClickListener itemClicked;


    public RecyclerViewAdapter(Context context ,ArrayList<Movie> movies ,ItemClickListener item) {
        this.movies = movies;
        itemClicked = item;
        this.context = context;

    }

    public interface ItemClickListener {

        void onItemClick(int clicked_position);
    }

    @NonNull
     @Override
     public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_view ,parent,false);

        viewHolder holder = new viewHolder(view);
        return holder;
     }



     @Override
     public void onBindViewHolder(@NonNull viewHolder holder, int position) {

         posterImg = movies.get(position).getPosterImg();
         movieName = movies.get(position).getName();

         Picasso.get().load(posterImg).fit()
                 .placeholder(R.drawable.ic_launcher_background)
                 .into(holder.image);

         holder.text.setText(movieName);

         Log.i(TAG, "onBindViewHolder: called.");

     }

     @Override
     public int getItemCount() {
         return movies.size();
     }

     public class viewHolder extends RecyclerView.ViewHolder
             implements OnClickListener{
        private ImageView image;
        private TextView text;
        private ItemClickListener itemListener;

        public viewHolder(final View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.movie_image);
            this.text = (TextView) itemView.findViewById(R.id.movie_desc);


            itemView.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View view) {
                    int posetion = getAdapterPosition();
                    Intent intent = new Intent(itemView.getContext() , Movie_details.class);
                    intent.putExtra("PosterImage" , movies.get(posetion).getPosterImg());
                    intent.putExtra("Description" , movies.get(posetion).getDescrtptin() );
                    intent.putExtra("Name",movies.get(posetion).getName());
                    intent.putExtra("rate" , String.valueOf(movies.get(posetion).getVote_avg()));
                    intent.putExtra("Released Date" , movies.get(posetion).getRelease_date());


                    Log.i(TAG , "Position  IS :"+posetion);
                    itemView.getContext().startActivity(intent);

                }
            } );

        }


         @Override
         public void onClick(View view) {
             if (itemListener != null) {
                 int ClikedPosition = getAdapterPosition();
                 itemListener.onItemClick(ClikedPosition);
             }else{
                 Log.i(TAG , "NULL VIEW ");
             }


         }
     }
    }


