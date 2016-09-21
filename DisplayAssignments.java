package com.xshiki.zhaoli.jvha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;

public class DisplayAssignments extends AppCompatActivity {

    private String[]list;
    private String[]grade;
    private ListView listView;
    private ArrayAdapter <String>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_assignments);
        setTitle("Assignments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            int position=(int)bundle.get("value");
            list=bundle.getStringArray("assign"+position);
            Arrays.sort(list);
        }

        listView=(ListView)findViewById(R.id.assignview);
        adapter=new ArrayAdapter<>(this,R.layout.layout,R.id.assigntextview,list);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu)
    {
        switch (menu.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(menu);
    }
}
