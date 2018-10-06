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
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Movie> movies ;
    private List<Movie> MvList;
    private String posterImg,movieName;
    private Context context;
    final private ItemClickListener itemClicked;


    public RecyclerViewAdapter(Context context ,List<Movie> movies ,ItemClickListener item) {

        this.MvList = movies;
        itemClicked = item;
        this.context = context;
        this.movies = toArrayList(movies);



    }
    public ArrayList toArrayList(List listData) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(listData);
        return arrayList;

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
                    Intent intent = new Intent(itemView.getContext() ,
                            Movie_details.class);


                    Log.d(TAG , "id in intent "+movies.get(posetion).getId());

                    intent.putExtra("movie" , movies.get(posetion));


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


