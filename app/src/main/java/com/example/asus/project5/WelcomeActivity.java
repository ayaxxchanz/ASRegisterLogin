package com.example.asus.project5;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager mPager;
    private int[] layouts = {R.layout.first_slide, R.layout.second_slide, R.layout.third_slide};
    private MpagerAdapter mpagerAdapter;

    private LinearLayout Dots_Layout;
    private ImageView[] dots;

    Button BnNext, BnSkip;
    boolean doubleTap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (new PreferenceManager(this).checkPreference()){
            loadHome();
        }

        if(Build.VERSION.SDK_INT >= 19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_welcome);

        mPager = (ViewPager) findViewById(R.id.viewPager);
        mpagerAdapter = new MpagerAdapter(layouts,this);
        mPager.setAdapter(mpagerAdapter);

        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        BnNext = (Button)   findViewById(R.id.btnNext);
        BnSkip = (Button) findViewById(R.id.btnSkip);
        BnNext.setOnClickListener(this);
        BnSkip.setOnClickListener(this);
        createDots(0);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                createDots(i);
                if (i == layouts.length-1){
                    BnNext.setText("Let's get started");
                    BnSkip.setVisibility(View.INVISIBLE);
                }
                else{
                    BnNext.setText("Next");
                    BnSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void createDots(int current_position){
        if (Dots_Layout != null){
            Dots_Layout.removeAllViews();
        }
        dots = new ImageView[layouts.length];

        for (int i = 0; i < layouts.length; i++){
            dots[i] = new ImageView(this);
            if (i == current_position){
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
            }
            else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dots));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT );
            params.setMargins(4, 0, 4, 0);

            Dots_Layout.addView(dots[i],params);
        }

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnNext:
                loadNextSlide();
                break;
            case R.id.btnSkip:
                loadHome();
                new PreferenceManager(this).writePreference();
                break;

        }
    }

    private void loadHome(){
        startActivity(new Intent(this,ProfileActivity.class));
        finish();
    }

    private void loadNextSlide(){
        int next_slide = mPager.getCurrentItem()+1;

        if (next_slide < layouts.length){
            mPager.setCurrentItem(next_slide);
        }
        else{
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }
    @Override
    public void onBackPressed() {
        if (doubleTap) {
            super.onBackPressed();
            finishAffinity();
        }

        else{
            Toast.makeText(this,"Press again to exit.",Toast.LENGTH_SHORT).show();
            doubleTap = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleTap = false;
                }
            }, 2000);
        }
    }
}
