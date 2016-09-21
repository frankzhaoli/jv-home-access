package com.xshiki.zhaoli.jvha;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DisplaySettings extends AppCompatActivity {

    public String[]list={"Credits: Frank Li & Kevin Turner","Version 1.1.1","Average Calculator","Changelog","Log Out"};

    private ListView listView;
    private ArrayAdapter adapter;

    /*
    public void notification(View view)
    {
        NotificationCompat.Builder notification=new NotificationCompat.Builder(this);
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setContentTitle("Notification!");
        notification.setContentText("Hello!");

        Intent result=new Intent(this,MainActivity.class);

        TaskStackBuilder stack=TaskStackBuilder.create(this);
        stack.addParentStack(MainActivity.class);
        stack.addNextIntent(result);

        PendingIntent pending=stack.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pending);

        NotificationManager notificationManager=(NotificationManager)getSystemService(view.getContext().NOTIFICATION_SERVICE);
        notificationManager.notify();
    }
    */

    Button btn;
    private int clicks=0;

    public void maniClicks(View view)
    {
        clicks++;
        if(clicks>5)
        {
            Intent easterEgg=new Intent(this,easterEgg.class);
            startActivity(easterEgg);
        }
    }

    public void logOff(View view)
    {
        final Intent intent=new Intent(this,MainActivity.class);
        DialogInterface.OnClickListener dialogClickListener=new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int num)
            {
                switch(num)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("test","true");
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");
        builder.setNegativeButton("No",dialogClickListener);
        builder.setPositiveButton("Yes",dialogClickListener).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings);
        setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //clickListener();
        final Intent changelog = new Intent(this,DisplayChangelog.class);
        final Intent calc=new Intent(this,Calculator.class);
        listView=(ListView)findViewById(R.id.settinglistview);
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4)
                    logOff(view);
                if (position == 1)
                    maniClicks(view);
                if(position==2)
                    startActivity(calc);
                if (position == 3)
                    startActivity(changelog);
            }
        });
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

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }
}
