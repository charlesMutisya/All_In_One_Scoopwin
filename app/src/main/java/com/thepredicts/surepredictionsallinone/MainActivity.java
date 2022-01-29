package com.thepredicts.surepredictionsallinone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.messaging.FirebaseMessaging;
import com.thepredicts.surepredictionsallinone.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn1,btn2,btn3,btn4,btn6;
    private final String TAG = MainActivity.class.getSimpleName();
    InterstitialAd mInterstatialAd;
    AdView adView;
    AdView adView2;
    FrameLayout adcontainerView;


   @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




       btn1 = findViewById(R.id.butn1);
        btn2 = findViewById(R.id.butn2);
        btn3 = findViewById(R.id.butn3);
        btn4 = findViewById(R.id.butn4);
        btn6 = findViewById(R.id.butn6);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn6.setOnClickListener(this);


       FirebaseMessaging.getInstance().subscribeToTopic("scoopwin");
       AudienceNetworkAds.initialize(this);
       MobileAds.initialize(this, new OnInitializationCompleteListener() {
    @Override
    public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

    }
});


       adcontainerView = findViewById(R.id.adViewhome);
       adcontainerView.post(new Runnable() {
           @Override
           public void run() {
            loadBannergd();
           }
       });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent settingsIntent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(settingsIntent);
        }else if (id== R.id.policy){


            Intent policyintent= new Intent(this,LoadingWeb.class);
            policyintent.putExtra("head","Privacy Policy");
            policyintent.putExtra("link","https://charlpolicy.blogspot.com/2021/10/all-in-one-privacy-policy.html");
            startActivity(policyintent);
        }else if (id== R.id.about){
            Intent aboutintent= new Intent(this,LoadingWeb.class);
            aboutintent.putExtra("head","About Us");
            aboutintent.putExtra("link","https://scoopwin.blogspot.com/p/scoopwin-info.html");
            startActivity(aboutintent);
        }else if (id==R.id.terms){
            Intent terms= new Intent(this,LoadingWeb.class);
            terms.putExtra("head","Terms & Conditions");
            terms.putExtra("link","https://scoopwin.blogspot.com/p/terms-conditions.html");
            startActivity(terms);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.butn1:

                Intent intentt = new Intent(v.getContext(), FootBallPicks.class);
                intentt.putExtra("title","Daily FootBall Picks");
                intentt.putExtra("db","jackpot");
                intentt.putExtra("selectedp","dailyplays");
                startActivity(intentt);

                break;
            case R.id.butn2:

                Intent intent1= new Intent(v.getContext(),FootBallPicks.class);
                intent1.putExtra("title","JackPot Guide");
                intent1.putExtra("db","jackpot");
                intent1.putExtra("selectedp","2+odds");
                startActivity(intent1);
                loadInterstial();
                break;
            case R.id.butn3:
                Intent intent2 = new Intent(v.getContext(),FootBallPicks.class);
                intent2.putExtra("title"," HandBall Picks");
                intent2.putExtra("db","scoopwin");
                intent2.putExtra("selectedp","handball picks");
                startActivity(intent2);
                break;

            case  R.id.butn4:
                Intent intent3 = new Intent(v.getContext(), FootBallPicks.class);
                intent3.putExtra("title"," BasketBall Picks ");
                intent3.putExtra("db","scoopwin");
                intent3.putExtra("selectedp","basketball picks");
                startActivity(intent3);
                break;

            case R.id.butn6:
                Intent intent5 = new Intent(v.getContext(),FootBallPicks.class);
                intent5.putExtra("title"," Tennis Picks");
                intent5.putExtra("db","scoopwin");
                intent5.putExtra("selectedp","tennis picks");
                startActivity(intent5);
                loadInterstial9();
        }
    }


    private void loadBannergd(){
       adView2 = new AdView(this);
       adView2.setAdUnitId(getString(R.string.banner_testad));
       adcontainerView.removeAllViews();
       adcontainerView.addView(adView2);
       AdSize adSize1 = getAdSize();
       adView2.setAdSize(adSize1);
       AdRequest adRequest1 = new AdRequest.Builder().build();
       adView2.loadAd(adRequest1);

       /**
        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        adcontainerView.removeAllViews();
        adcontainerView.addView(adView);
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);

**/
    }
    private AdSize getAdSize(){
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adcontainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }



    private InterstitialAd loadInterstial() {
        AdRequest adRequest= new AdRequest.Builder().build();
        InterstitialAd.load(this,getString(R.string.interstitial_test),adRequest, new InterstitialAdLoadCallback(){
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstatialAd = interstitialAd;
                mInterstatialAd.show(MainActivity.this);
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

    private InterstitialAd loadInterstial9() {
        AdRequest adRequest= new AdRequest.Builder().build();
        InterstitialAd.load(this,getString(R.string.interstitial_test),adRequest, new InterstitialAdLoadCallback(){
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstatialAd = interstitialAd;
                mInterstatialAd.show(MainActivity.this);
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

}
