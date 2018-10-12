package com.example.android.movieapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapp.R;
import com.example.android.movieapp.modules.Review;

import java.util.ArrayList;


public class ReviewsRecyclerViewAdpater  extends
        RecyclerView.Adapter<ReviewsRecyclerViewAdpater.viewHolder> {

    public interface onReviewListItemClicked{

        public void onReviewItemClicked(int position);
    }

    private ArrayList<Review> reviews;
    private Context context;


    public ReviewsRecyclerViewAdpater(ArrayList<Review> reviews,
                                      Context context ) {
        this.reviews = reviews;
        this.context = context;

    }


    @NonNull
    @Override
    public ReviewsRecyclerViewAdpater.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

     View view = LayoutInflater.from(parent.getContext())
             .inflate(R.layout.reviews,parent, false);

        viewHolder holder = new viewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsRecyclerViewAdpater.viewHolder holder, int position) {

        holder.authorName.setText(reviews.get(position).getAuthor());
        holder.content.setText(reviews.get(position).getContent());


    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
    ////////////////////



    ///////////////////

class viewHolder extends RecyclerView.ViewHolder
    implements  View.OnClickListener{

        private TextView authorName;
        private TextView content;

        public viewHolder(final View itemView) {
            super(itemView);
            this.authorName = (TextView) itemView.findViewById(R.id.authorName);
            this.content = (TextView) itemView.findViewById(R.id.content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Opening ... ", Toast.LENGTH_SHORT).show();
                    Intent webIntent =
                            new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(reviews.get(getAdapterPosition()).getUrl()));

                        context.startActivity(webIntent);
                }

            });
        }

    @Override
    public void onClick(View view) {
            int clicked = getAdapterPosition();
           // reviewClicked.onReviewItemClicked(clicked);
            Log.d(".ReviewsRecycler","Clicked ");

    }
}


}
