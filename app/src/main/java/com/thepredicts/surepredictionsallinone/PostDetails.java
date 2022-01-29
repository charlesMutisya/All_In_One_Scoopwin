package com.thepredicts.surepredictionsallinone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.thepredicts.surepredictionsallinone.R;

public class PostDetails extends AppCompatActivity {

    private final String TAG = PostDetails.class.getSimpleName();
    DatabaseReference mRef;
    String postKey;
    TextView tvTitle, tvBody, tvTime;
    ProgressDialog pd;
    private String selection,dbs,title;
    AdView adView;
    private InterstitialAd mInterstatialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_post_details2);

        postKey= getIntent().getExtras().getString("postkey");
        selection=getIntent().getExtras().getString("selection");
        dbs=getIntent().getExtras().getString("dbs");
        title= getIntent().getExtras().getString("title");
            this.setTitle(title);

        tvBody =  findViewById(R.id.tvBody);
        tvTitle =  findViewById(R.id.tvTitle);
        tvTime =  findViewById(R.id.post_time);
        pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();


        if (postKey != null){

            mRef = FirebaseDatabase.getInstance().getReference().child(dbs).child(selection).child(postKey);

        }
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("title").getValue().toString();
                String body = dataSnapshot.child("body").getValue().toString();
                Long time = (Long) dataSnapshot.child("time").getValue();

                if (title != null) {
                    tvTitle.setText(title.toUpperCase());
                    pd.dismiss();
                } else {
                    Toast.makeText(PostDetails.this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
                if (body != null) {
                    tvBody.setText(body);

                }
                if (time != null) {
                    setTime(time);
                }
            }
            public void setTime(Long time) {
                TextView txtTime = (TextView) findViewById(R.id.post_time);
                //long elapsedDays=0,elapsedWeeks = 0, elapsedHours=0,elapsedMin=0;
                long elapsedTime;
                long currentTime = System.currentTimeMillis();
                int elapsed = (int) ((currentTime - time) / 1000);
                if (elapsed < 60) {
                    if (elapsed < 2) {
                        txtTime.setText("Just Now");
                    } else {
                        txtTime.setText(elapsed + " sec ago");
                    }
                } else if (elapsed > 604799) {
                    elapsedTime = elapsed / 604800;
                    if (elapsedTime == 1) {
                        txtTime.setText(elapsedTime + " week ago");
                    } else {

                        txtTime.setText(elapsedTime + " weeks ago");
                    }
                } else if (elapsed > 86399) {
                    elapsedTime = elapsed / 86400;
                    if (elapsedTime == 1) {
                        txtTime.setText(elapsedTime + " day ago");
                    } else {
                        txtTime.setText(elapsedTime + " days ago");
                    }
                } else if (elapsed > 3599) {
                    elapsedTime = elapsed / 3600;
                    if (elapsedTime == 1) {
                        txtTime.setText(elapsedTime + " hour ago");
                    } else {
                        txtTime.setText(elapsedTime + " hours ago");
                    }
                } else if (elapsed > 59) {
                    elapsedTime = elapsed / 60;
                    txtTime.setText(elapsedTime + " min ago");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.detailsmenu, menu);
        return true;


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            loadInterstialPD();
            finish();
        }

        if (id == R.id.feedback) {
            startActivity(new Intent(this, FeedBack.class));

        }else if (id == R.id.menu_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey! Check out ScoopWin-the all in one app offering free Daily football, tennis, basketball, hockey, handball & jackpot Predictions for free. Download here https://play.google.com/store/apps/details?id=com.thepredicts.surepredictionsallinone";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " AllInOne Predictions");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(sharingIntent);
        }else if ( id== R.id.rate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }
    private InterstitialAd loadInterstialPD() {
        AdRequest adRequest= new AdRequest.Builder().build();
        InterstitialAd.load(this,getString(R.string.interstitial_test),adRequest, new InterstitialAdLoadCallback(){
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstatialAd = interstitialAd;
                mInterstatialAd.show(PostDetails.this);
                Log.i(TAG, "onAdLoaded");

                mInterstatialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                    }



                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstatialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstatialAd = null;

            }
        });
        return mInterstatialAd;
    }


    @Override
    public void onBackPressed() {
    loadInterstialPD();
    finish();
    }
}

