package com.decorista.anas.decorista;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PrivacyFragment extends Fragment {
TextView policy;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_team_work, container, false);

      policy=(TextView)view.findViewById(R.id.policy);
      policy.setMovementMethod(new ScrollingMovementMethod());

      DatabaseReference reference= FirebaseDatabase.getInstance().getReference(getString(R.string.policy_firebase));
      reference.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {

              policy.setText(dataSnapshot.getValue().toString());
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });

      return view;
    }


}
