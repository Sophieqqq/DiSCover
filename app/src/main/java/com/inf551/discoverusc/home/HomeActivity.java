package com.inf551.discoverusc.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inf551.discoverusc.R;
import com.inf551.discoverusc.login.LoginActivity;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private Button LogoutButton;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    private RecyclerView mPostList;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Recommendation");

        mPostList =(RecyclerView) findViewById(R.id.post_list);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();

        // this listener will be called when there is change in firebase user session
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        FloatingActionButton Add = (FloatingActionButton) findViewById(R.id.action_add);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(HomeActivity.this, PostActivity.class));
            }
        });

        LogoutButton = (Button) findViewById(R.id.signoutBtn);
        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    public static class RecommendationViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public RecommendationViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc(String desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);

        FirebaseRecyclerAdapter<Recommendation,RecommendationViewHolder> firebaseRecyclerAdpter = new FirebaseRecyclerAdapter<Recommendation,RecommendationViewHolder>(
                Recommendation.class,
                R.layout.post_row,
                RecommendationViewHolder.class,
                mDatabase
        ){
            @Override
            protected void populateViewHolder(RecommendationViewHolder viewHolder, Recommendation model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setImage(getApplicationContext(),model.getImage());
            }
        };

        mPostList.setAdapter(firebaseRecyclerAdpter);
    }



    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }



}
