package com.decorista.anas.decorista;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DepartmentFragment extends Fragment implements
        RecycleAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    //  RecycleAdapter adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;

    public static int index = -1;
    public static int top = -1;
    int scrollPosition;
    GridLayoutManager gridLayoutManager;
    FirebaseRecyclerAdapter<TypesModel, ViewHolder> firebaseRecyclerAdapter;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        setRetainInstance(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(getString(R.string.firebase_data));
        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    gridLayoutManager.onRestoreInstanceState(mListState);

                }
            }, 50);
        }

        // set up the RecyclerView
        final Bundle bundle = new Bundle();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TypesModel, ViewHolder>(TypesModel.class,
                R.layout.type_list, ViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, final TypesModel model, int position) {


                viewHolder.setDetails(getContext(), model.getName(), model.getImage());
                viewHolder.setOnClickListener(new ViewHolder.OnItemClick() {
                    @Override
                    public void onItemClickListener(View view, int position) {
                        Intent intent = new Intent(getContext(), ImagesActivity.class);
                        bundle.putString(getString(R.string.name), model.getName());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }


        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

        return view;
    }


    @Override
    public void onItemClick(View view, int position) {

    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mBundleRecyclerViewState = new Bundle();
        mListState =gridLayoutManager.onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }


//    @Override
//    public void onPause() {
//        super.onPause();
//        mBundleRecyclerViewState = new Bundle();
//        mListState =gridLayoutManager.onSaveInstanceState();
//
//        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
//    }
}
