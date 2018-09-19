package com.decorista.anas.decorista;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.support.constraint.Constraints.TAG;


public class ConnectFragment extends Fragment {

    TextView company_con, company_link, face_link, linkedin_link;

    LinearLayout company_url, lindin_url, face_url;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment]
        View view = inflater.inflate(R.layout.fragment_connect, container, false);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(getString(R.string.connect_us));
        recyclerView = view.findViewById(R.id.connection_recycle);

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
        FirebaseRecyclerAdapter<ConnectItemModel, ConnectHolder> connectItemModelConnectHolderFirebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ConnectItemModel, ConnectHolder>(ConnectItemModel.class
                        , R.layout.connect_item, ConnectHolder.class, databaseReference) {
                    @Override
                    protected void populateViewHolder(ConnectHolder viewHolder, ConnectItemModel model, int position) {
                        viewHolder.setDetails(getContext(), model.getIcon(), model.getText());
                        Log.i("abag", model.getText());
                        Log.i("abag", model.getIcon());

                    }

                    @Override
                    public ConnectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ConnectHolder connectHolder = super.onCreateViewHolder(parent, viewType);
                        connectHolder.setOnClickListener(new ViewHolder.OnItemClick() {
                            @Override
                            public void onItemClickListener(View view, int position) {
                                TextView textView = view.findViewById(R.id.company_link);
//                                (text.matches("[0-9]+") && text.length() > 2)
                                if (textView.getText().toString().matches("[0-9]+")) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + textView.getText()));
                                    startActivity(intent);
                                } else {
                                    String url = textView.getText().toString();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse("http://"+url));
                                    startActivity(i);
                                }
                            }
                        });

                        return connectHolder;
                    }
                };
        recyclerView.setAdapter(connectItemModelConnectHolderFirebaseRecyclerAdapter);

        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.text);
        a.reset();

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
