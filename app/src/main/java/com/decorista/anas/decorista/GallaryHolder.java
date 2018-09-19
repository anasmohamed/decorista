package com.decorista.anas.decorista;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class GallaryHolder extends RecyclerView.ViewHolder {
    View mView;

    public GallaryHolder(View itemView) {
            super(itemView);
            mView = itemView;


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemClickListener(v, getAdapterPosition());
                }
            });
    }

    public void setDetails(Context context, String image) {
        ImageView imageView = mView.findViewById(R.id.thumbnail);
        final ImageView like = mView.findViewById(R.id.like);
        ImageView download = mView.findViewById(R.id.download);

        Picasso.get().load(image)
                .into(imageView);

    }

    private ViewHolder.OnItemClick onItemClick;

    public interface OnItemClick {
        void onItemClickListener(View view, int position);
    }

    public void setOnClickListener(ViewHolder.OnItemClick onClickListener) {
        onItemClick = onClickListener;
    }
}

