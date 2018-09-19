package com.decorista.anas.decorista;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class AboutFragment extends Fragment {

    TextView textView;
    ImageView imageView;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
//
        imageView=(ImageView) view.findViewById(R.id.imageView_logo);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(getString(R.string.about_firebase));
         recyclerView =  view.findViewById(R.id.connection_recycle);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    recyclerView.getLayoutManager().onRestoreInstanceState(mListState);

                }
            }, 50);
        }

        FirebaseRecyclerAdapter<String, AboutHolder> aboutModelAboutHolderFirebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<String, AboutHolder>(String.class
                        , R.layout.about_item, AboutHolder.class, databaseReference) {
                    @Override
                    protected void populateViewHolder(AboutHolder viewHolder, String model, int position) {
                        viewHolder.setDetails(getContext(),model);


                    }
                };
        recyclerView.setAdapter(aboutModelAboutHolderFirebaseRecyclerAdapter);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("about");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.text);
                a.reset();
                imageView.clearAnimation();
                Log.i("image",dataSnapshot.child("image").getValue().toString());
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(imageView);
                imageView.setAnimation(a);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mBundleRecyclerViewState = new Bundle();
        mListState =recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

}
