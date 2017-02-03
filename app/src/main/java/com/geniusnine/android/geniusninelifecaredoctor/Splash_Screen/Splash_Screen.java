package com.geniusnine.android.geniusninelifecaredoctor.Splash_Screen;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.geniusnine.android.geniusninelifecaredoctor.Login_Patient.Patient_Login;
import com.geniusnine.android.geniusninelifecaredoctor.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by Dev on 12-01-2017.
 */

public class Splash_Screen extends AwesomeSplash {

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!


    @Override
    public void initSplash(ConfigSplash configSplash) {

            /* you don't have to override every property */

        //Customize Circular Reveal
      configSplash.setBackgroundColor(R.color.black); //any color you want form colors.xml

        configSplash.setAnimCircularRevealDuration(1000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.geniuslogo); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
      //  configSplash.setPathSplash(SyncStateContract.Constants.DROID_LOGO); //set path String
        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
       // configSplash.setPathSplashStrokeColor(R.color.accent); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
       // configSplash.setPathSplashFillColor(R.color.Wheat); //path object filling color


        //Customize Title
        configSplash.setTitleSplash("GeniusNine Health Care");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);


    }

    @Override
    public void animationsFinished() {
        Intent intent=new Intent(Splash_Screen.this, Patient_Login.class);
        finish();
        startActivity(intent);
        //transit to another activity here
        //or do whatever you want
    }
}