package com.xshiki.zhaoli.jvha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class easterEgg extends AppCompatActivity {

    private int num=0;
    public void easterEggpt2(View view)
    {
        num++;
        if(num>6)
        {
            Intent satan=new Intent(this,easterEggPt2.class);
            startActivity(satan);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easter_egg);
        setTitle("Jersey Village Home Access");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
