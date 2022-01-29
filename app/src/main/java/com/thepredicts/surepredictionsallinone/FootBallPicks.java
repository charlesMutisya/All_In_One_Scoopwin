package com.thepredicts.surepredictionsallinone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.thepredicts.surepredictionsallinone.R;

public class FootBallPicks extends AppCompatActivity {
    private final String TAG = FootBallPicks.class.getSimpleName();

    DatabaseReference dbref;
    View view;
    RecyclerView mrecycler;
    LinearLayoutManager mlinearlayout;
    String db,selectedp,title;
    TextView loading;
   AdView mAdView;
   FrameLayout adcontainerView;
    FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Model> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_foot_ball_picks);
        db= getIntent().getExtras().getString("db");
        selectedp= getIntent().getExtras().getString("selectedp");
        title= getIntent().getExtras().getString("title");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        dbref = FirebaseDatabase.getInstance().getReference().child(db).child(selectedp);
        Query query = dbref.limitToFirst(20);
        this.setTitle(title);


        loading= findViewById(R.id.loadwait);
        mrecycler = findViewById(R.id.recycler1);
        mrecycler.setHasFixedSize(false);
        mlinearlayout = new LinearLayoutManager(getApplicationContext());


        
        options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(query, Model.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Model model) {
                final String item_key = getRef(position).getKey();
                holder.setTitle(model.getTitle());
                holder.setDetails(model.getBody());
                holder.setTime(model.getTime());
                loading.setVisibility(View.GONE);
                holder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent tipDetails = new Intent(view.getContext(), PostDetails.class);
                        tipDetails.putExtra("postkey", item_key);
                        tipDetails.putExtra("dbs",db);
                        tipDetails.putExtra("selection", selectedp);
                        tipDetails.putExtra("title",title);
                        startActivity(tipDetails);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //inflating layout row.xml
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewcard, parent, false);
                ViewHolder viewHolder = new ViewHolder(itemView);
                return viewHolder;
            }
        };

    mrecycler.setLayoutManager(mlinearlayout);
    firebaseRecyclerAdapter.startListening();
    mrecycler.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();

        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    protected void onDestroy() {
        if (mAdView != null){
            mAdView.destroy();
        }
        super.onDestroy();
    }
}

