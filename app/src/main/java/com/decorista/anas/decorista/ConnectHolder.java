package com.decorista.anas.decorista;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ConnectHolder extends RecyclerView.ViewHolder{
    View mView;

    public ConnectHolder(View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClickListener(v, getAdapterPosition());
            }
        });
    }
    public void setDetails(Context context, String image,String text) {
        TextView nameTV = mView.findViewById(R.id.company_link);
        nameTV.setText(text);


    }

    private ViewHolder.OnItemClick onItemClick;

    public interface OnItemClick {
        void onItemClickListener(View view,int position);
    }

    public void setOnClickListener(ViewHolder.OnItemClick onClickListener) {
        onItemClick = onClickListener;
    }
}
