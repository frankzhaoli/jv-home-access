package com.xshiki.zhaoli.jvha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DisplayChangelog extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter adapter;
    private String[]list={
            "1.0.0 RELEASE\n"+"-Full release version.",
            "1.0.1 RELEASE\n"+"-Clarification on release version.",
            "1.0.2 RELEASE\n"+"-Fixed automatic refresh time in multiple views.",
            "1.0.3 RELEASE\n"+"-Fixes crashing on Calendar/News views.\n"+"NOTE: Known issues with Calendar/News view and automatic log in.",
            "1.1.0 RELEASE\n"+"-General code cleanup.\n"+"-Possible fix for auto log in.\n"+"-Possible fix for Calendar/News views.\n"+"-A few general graphic updates.",
            "1.1.1 RELEASE\n"+"-Added average calculator to setting view."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_changelog);
        setTitle("Changelog");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView=(ListView)findViewById(R.id.changeloglistview);
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
