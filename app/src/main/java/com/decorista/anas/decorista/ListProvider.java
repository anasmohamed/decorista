package com.decorista.anas.decorista;

import android.app.Activity;
import android.app.LauncherActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<TypesModel> listItemList = new ArrayList<TypesModel>();
    private Context context = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    private void populateListItem() {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference(context.getString(R.string.firebase_data));

        mDatabaseReference.addValueEventListener(new ValueEventListener() {

            TypesModel typesModel;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    typesModel = new TypesModel();

                    typesModel.setName(postSnapshot.child(context.getString(R.string.firebase_name)).getValue().toString());
                    typesModel.setImage(postSnapshot.child(context.getString(R.string.firebase_image)).getValue().toString());
                    listItemList.add(typesModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreate() {
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(appWidgetId,R.id.words);


    }

    @Override
    public void onDataSetChanged() {
        populateListItem();


    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {

        return listItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.raw);
        TypesModel typesModel = listItemList.get(position);

        remoteView.setTextViewText(R.id.widget_text, typesModel.getName());


        Bitmap b = null;
        try {
            b = Picasso.get().load(typesModel.getImage()).get();
            remoteView.setImageViewBitmap(R.id.widget_image, b);

        } catch (IOException e) {
            e.printStackTrace();
        }






        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(), R.layout.widget);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
