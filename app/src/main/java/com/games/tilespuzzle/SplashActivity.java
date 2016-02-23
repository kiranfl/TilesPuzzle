package com.games.tilespuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;



/**
 * @author kiran on 1/16/2016.
 */
public class SplashActivity extends AppCompatActivity {

    TextView title,sub_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        title = (TextView)findViewById(R.id.title);
        sub_title = (TextView) findViewById(R.id.title1);
//        Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/STONB___.TTF");
//        title.setTypeface(fontFace);
//        sub_title.setTypeface(fontFace);
        MyCount mc = new MyCount(2000, 1000);
        mc.start();
    }

    final class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            Intent myIntent;

            myIntent = new Intent(SplashActivity.this, HomeActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
    }


}
