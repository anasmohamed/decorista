package com.decorista.anas.decorista;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AboutHolder extends RecyclerView.ViewHolder {
    View mView;

    public AboutHolder(View itemView) {
        super(itemView);
        mView = itemView;

    }

    public void setDetails(Context context, String name) {
        TextView nameTV = mView.findViewById(R.id.fadeTextView);
        nameTV.setText(name);


    }

    private ViewHolder.OnItemClick onItemClick;

    public interface OnItemClick {
        void onItemClickListener(View view, int position);
    }

    public void setOnClickListener(ViewHolder.OnItemClick onClickListener) {
        onItemClick = onClickListener;
    }
}
