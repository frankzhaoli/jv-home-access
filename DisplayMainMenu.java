package com.xshiki.zhaoli.jvha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.transition.Scene;
import android.transition.Transition;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class DisplayMainMenu extends AppCompatActivity {

    private Scene scene1, scene2;
    private Transition transition;
    private boolean start;

    public void clickCalender(View view)
    {
        Intent calender=new Intent(this,DisplayCalender.class);
        startActivity(calender);

        tracker.setScreenName("Calendar");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        tracker.send(new HitBuilders.EventBuilder().setCategory("Norm").setAction("CalendarViewClick").setLabel("CalendarSettings").setValue(1).build());

        if(interstitialAd.isLoaded())
        {
            interstitialAd.show();
        }
    }
    public void clickSettings(View view)
    {
        Intent settings=new Intent(this,DisplaySettings.class);
        startActivity(settings);

        tracker.setScreenName("Settings");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        tracker.send(new HitBuilders.EventBuilder().setCategory("Norm").setAction("SettingsViewClick").setLabel("ViewSettings").setValue(1).build());
    }
    public void clickGrades(View view)
    {
        Intent grades=new Intent(this,DisplayGrades.class);
        Intent intent=getIntent();
        String user="";
        String pw="";
        Bundle bundle=intent.getExtras();

        if(bundle!=null)
        {
            user=bundle.getString("username1");
            pw=bundle.getString("password1");
        }

        grades.putExtra("username2",user);
        grades.putExtra("password2", pw);
        startActivity(grades);

        //tracker.send(new HitBuilders.EventBuilder().setCategory("MainMenu").setAction("GradeClick").build());
        //tracker.send(new HitBuilders.ScreenViewBuilder().build());
        tracker.setScreenName("Grades");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        tracker.send(new HitBuilders.EventBuilder().setCategory("Norm").setAction("GradeViewClick").setLabel("ViewGrades").setValue(1).build());

        if(interstitialAd.isLoaded())
        {
            interstitialAd.show();
        }
    }

    public void clickTeachers(View view)
    {
        Intent teachers=new Intent(this,DisplayTeachers.class);
        startActivity(teachers);

        tracker.setScreenName("Teachers");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        tracker.send(new HitBuilders.EventBuilder().setCategory("Norm").setAction("TeacherViewClick").setLabel("ViewTeachers").setValue(1).build());

        if(interstitialAd.isLoaded())
        {
            interstitialAd.show();
        }
    }

    public void clickAnnouncements(View view)
    {
        Intent anc=new Intent(this,DisplayAnnouncements.class);
        startActivity(anc);

        tracker.send(new HitBuilders.EventBuilder().setCategory("Norm").setAction("AnnounceViewClick").setLabel("ViewAnnounce").setValue(1).build());
        tracker.setScreenName("Announcements");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        if(interstitialAd.isLoaded())
        {
            interstitialAd.show();
        }
    }

    public void clickNews(View view)
    {
        Intent news=new Intent(this,DisplayNews.class);
        startActivity(news);

        tracker.send(new HitBuilders.EventBuilder().setCategory("Norm").setAction("NewsViewClick").setLabel("ViewNews").setValue(1).build());
        tracker.setScreenName("News");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        if(interstitialAd.isLoaded())
        {
            interstitialAd.show();
        }
    }

    private Tracker tracker;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_main_menu);
        setTitle("Jersey Village Home Access");

        //AdView adView=(AdView)findViewById(R.id.adView);
        //AdRequest adRequest=new AdRequest.Builder().build();
        //adView.loadAd(adRequest);

        /*
        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-5983943916641335/7890543803");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();

        AnalyticsApplication app=(AnalyticsApplication)getApplication();
        tracker=app.getDefaultTracker();
        */

        RelativeLayout baseLayout=(RelativeLayout)findViewById(R.id.mainmenulayout);

    }

    private void requestNewInterstitial()
    {
        AdRequest adRequest=new AdRequest.Builder().addTestDevice("C573223840491A033FA6C561245B9FD2").build();
        interstitialAd.loadAd(adRequest);
    }

    private int t=0;
    @Override
    public void onBackPressed()
    {
        if(t<1)
        {
            Toast.makeText(getApplicationContext(), "Press back again to exit to the login screen.", Toast.LENGTH_SHORT).show();
            t++;
        }
        else
        {
            finish();
        }
    }
}