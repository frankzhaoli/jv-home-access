package com.xshiki.zhaoli.jvha;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DisplayAnnouncements extends AppCompatActivity {

    private String[] list;
    private String datestring="";

    private TextView textView,date;

    private ListView listView;
    private ArrayAdapter arrayAdapter;

    SharedPreferences sp;
    private static final String SAVEANNOUNCE="saveannounce";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_announcements);
        setTitle("Announcements");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp=getSharedPreferences(SAVEANNOUNCE,MODE_PRIVATE);

        //GregorianCalendar gregorianCalendar=new GregorianCalendar();
        //int time=(gregorianCalendar.get(Calendar.HOUR_OF_DAY));

        //if(time!=0)
        //{
            new DownloadAnnouncements().execute("");
        //}
        /*
        if(sp.getString("announce"+1,null)!=null)
        {
            list=new String[sp.getInt("size",0)];

            for(int i=0; i<list.length; i++)
            {
                list[i]=sp.getString("announce"+i,"");
            }

            datestring=datestring.concat(sp.getString("announcedate",""));
            textView=(TextView)findViewById(R.id.announcementdate);
            textView.append(datestring);

            listView=(ListView)findViewById(R.id.announcementlistview);
            if(list!=null)
            {
                arrayAdapter=new ArrayAdapter(getApplicationContext(),R.layout.layout,R.id.announcetextview,list);
                listView.setAdapter(arrayAdapter);
            }
            else
            {
                Toast.makeText(this, "Error: Please check your internet connection!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else
            new DownloadAnnouncements().execute("");
            */


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadAnnouncements extends AsyncTask<String,Integer,String>
    {
        String announcedate="";
        Exception exception=null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String...params)
        {
            try
            {
                final String webs="https://alpine-furnace-774.appspot.com/jvapp?type=3";

                URI uri=URI.create(webs);
                String validUrl=uri.toASCIIString();
                URL url=new URL(validUrl);

                HttpURLConnection request=(HttpURLConnection)url.openConnection();
                JsonParser jp=new JsonParser();
                JsonElement root=jp.parse(new InputStreamReader((InputStream) request.getContent()));

                JsonObject rootobj=root.getAsJsonObject();
                JsonArray array=rootobj.get("results:").getAsJsonArray();

                list=new String[array.size()];

                for (int i=0; i<array.size(); i++)
                {
                    JsonObject entries=array.get(i).getAsJsonObject();
                    String entry=entries.get("announcement").getAsString();
                    String entry2=entries.get("date").getAsString();

                    list[i]=entry;

                    if(i==0)
                        announcedate=announcedate.concat(entry2.substring(0,entry2.length()-1));

                    /*
                    SharedPreferences.Editor editor=getSharedPreferences(SAVEANNOUNCE, MODE_PRIVATE).edit();
                    editor.putString("announce"+i,entry+"\n");
                    editor.putString("announcedate", announcedate.substring(0, announcedate.length() - 1));
                    editor.putInt("size", array.size());
                    editor.apply();
                    */
                }
            }
            catch (Exception e)
            {
                exception=e;
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            if(exception==null)
            {
                super.onPostExecute(result);
                date=(TextView)findViewById(R.id.announcementdate);
                textView=(TextView)findViewById(R.id.announcementdate);
                listView=(ListView)findViewById(R.id.announcementlistview);

                textView.append(announcedate);
                if(list!=null)
                {
                    arrayAdapter=new ArrayAdapter(getApplicationContext(),R.layout.layout,R.id.announcetextview,list);
                    listView.setAdapter(arrayAdapter);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error: Please check your internet connection!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error: Please check your internet connection!", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}