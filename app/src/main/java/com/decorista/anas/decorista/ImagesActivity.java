package com.decorista.anas.decorista;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

public class ImagesActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ArrayList<String> images;
    private ProgressDialog pDialog;
    private GridRecyclerView recyclerView;
    StorageReference storageRef;
    String deviceId;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent in = getIntent();
        Bundle b = in.getExtras();
        final String nameType = b.getString(getString(R.string.name));

        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference(getString(R.string.images)).child(nameType);
        storageRef = FirebaseStorage.getInstance().getReference();
        databaseReference.keepSynced(true);
        recyclerView = (GridRecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        final Bundle bundle = new Bundle();
        final DatabaseReference reference = firebaseDatabase.getReference();
        FirebaseRecyclerAdapter<Image, GallaryHolder> imageGallaryHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Image, GallaryHolder>(Image.class,
                R.layout.gallery_thumbnail, GallaryHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(final GallaryHolder viewHolder, final Image model, final int position) {
                Log.i("nameAnas", nameType + " " + model.getLikes() + "");
                viewHolder.setDetails(getApplicationContext(), model.getLink());
                final ImageView like = viewHolder.mView.findViewById(R.id.like);
                final TextView count = viewHolder.mView.findViewById(R.id.count);
                databaseReference.child(String.valueOf(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (Integer.valueOf(dataSnapshot.child(getString(R.string.firebase_likes)).getValue().toString()) >= 1) {
                            like.setImageResource(R.drawable.lovefill);
                            count.setText(String.valueOf(model.getLikes()));

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(String.valueOf(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(getString(R.string.firebase_id))) {
                                    databaseReference.child(String.valueOf(position)).child(getString(R.string.firebase_id)).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            boolean exist=false;
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if (snapshot.getValue().equals(deviceId)) {
                                                    exist = true;
                                                }
                                            }
                                            //  Log.i("device_id", snapshot.getValue().toString() + " " + deviceId);
                                            if (exist) {
                                                like.setImageResource(R.drawable.lovefill);

                                            } else {
                                                like.setImageResource(R.drawable.lovefill);
                                                databaseReference.child(String.valueOf(position)).child("id").child((model.getLikes() + 1) + "").setValue(deviceId);
                                                databaseReference.child(String.valueOf(position)).child("likes").setValue(model.getLikes() + 1);
                                                count.setText(String.valueOf(model.getLikes()));
                                            }
//
//                                }
                                        }


                                        //                                    }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }

                                    });
                                } else {
                                    databaseReference.child(String.valueOf(position)).child(getString(R.string.firebase_id)).child((model.getLikes() + 1) + "").setValue(deviceId);
                                    databaseReference.child(String.valueOf(position)).child(getString(R.string.firebase_likes)).setValue(model.getLikes() + 1);
                                    count.setText(String.valueOf(model.getLikes()));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
                ImageView download = viewHolder.mView.findViewById(R.id.download);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DownloadImage().execute(model.getLink());
                    }
                });

                images.add(model.getLink());
            }

            @Override
            public GallaryHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

                GallaryHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new ViewHolder.OnItemClick() {
                    @Override
                    public void onItemClickListener(View view, int position) {
                        bundle.putSerializable(getString(R.string.images), images);
                        bundle.putInt(getString(R.string.position), position);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        SlideFragment newFragment =
                                SlideFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "slideshow");
                    }
                });
                return viewHolder;
            }
        };

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(imageGallaryHolderFirebaseRecyclerAdapter);
        imageGallaryHolderFirebaseRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mBundleRecyclerViewState = new Bundle();
        mListState =recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    recyclerView.getLayoutManager().onRestoreInstanceState(mListState);

                }
            }, 50);
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ImagesActivity.this, R.string.download,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];
            Random random = new Random();
            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
                OutputStream output;


                // Find the SD Card path
                File filepath = Environment.getExternalStorageDirectory();

                // Create a new folder in SD Card
                File dir = new File(filepath.getAbsolutePath()
                        + "/Decorista/");
                dir.mkdirs();

                // Create a name for the saved image
                File file = new File(dir, random.nextInt() + "myimage.png");

                // Show a toast message on successful save

                try {

                    output = new FileOutputStream(file);

                    // Compress into png format image from 0% - 100%
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                    output.flush();
                    output.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            // Close progressdialog
            Toast.makeText(ImagesActivity.this, R.string.save,
                    Toast.LENGTH_SHORT).show();
        }
    }

}




