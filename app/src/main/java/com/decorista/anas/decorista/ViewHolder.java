package com.decorista.anas.decorista;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClickListener(v,getAdapterPosition());
            }
        });
    }

    public void setDetails(Context context, String name, String image) {
        TextView nameTV = mView.findViewById(R.id.title);
        ImageView imageView = mView.findViewById(R.id.image);
        nameTV.setText(name);
        Picasso.get().load(image).into(imageView);

    }

    private ViewHolder.OnItemClick onItemClick;

    public interface OnItemClick {
        void onItemClickListener(View view,int position);
    }

    public void setOnClickListener(ViewHolder.OnItemClick onClickListener) {
        onItemClick = onClickListener;
    }
}
