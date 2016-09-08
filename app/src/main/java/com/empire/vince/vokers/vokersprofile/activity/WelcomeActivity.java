package com.empire.vince.vokers.vokersprofile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.empire.vince.vokers.vokersprofile.R;
import com.empire.vince.vokers.vokersprofile.tracker.AppInitializer;

import static com.empire.vince.vokers.vokersprofile.util.ColorUtil.getSplashScreenColor;
import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.getWindowSize;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class WelcomeActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3;
    private ImageView splashImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().getDecorView().setBackgroundColor(getSplashScreenColor(this));
        setResponsiveSplashLogo();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                finish();
            }
        }, SPLASH_DURATION * 1000);
    }

    private void setResponsiveSplashLogo() {
        splashImageView = (ImageView)findViewById(R.id.splashImageView);
        splashImageView.getLayoutParams().width = (int) (getWindowSize(this).x * 0.75);
    }

    @Override
    protected void onResume() {
        AppInitializer.getInstance().trackScreenView("Welcome Screen");
        super.onResume();
    }
}
