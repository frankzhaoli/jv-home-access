package com.xshiki.zhaoli.jvha;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class   DisplayCalender extends AppCompatActivity {

    private TextView text;
    private String[]list;
    private ArrayAdapter adapter;
    private ListView listView;
    private List<String> cfisdurl=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_calender);
        setTitle("CFISD Calendar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new downloadCalendar().execute("");
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

    private class downloadCalendar extends AsyncTask<String,Integer,String>
    {
        Exception exception=null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String...param)
        {
            try
            {
                final String webs="http://www.cfisd.net/tools/blocks/pro_event_list/rss.php?ctID=All%20Categories&bID=13116&ordering=ASC";
                URI uri=URI.create(webs);
                String finalurl=uri.toASCIIString();
                URL url=new URL(finalurl);
                HttpURLConnection http=(HttpURLConnection)url.openConnection();

                SyndFeedInput input=new SyndFeedInput();
                SyndFeed feed=input.build(new XmlReader(http));

                if(feed!=null)
                {
                    List entries=feed.getEntries();
                    Iterator it=entries.iterator();

                    list=new String[entries.size()];

                    int i=0;
                    while(it.hasNext())
                    {
                        SyndEntry entry=(SyndEntry)it.next();
                        String title=entry.getTitle();
                        String description=entry.getDescription().getValue();
                        String date=entry.getPublishedDate().toString().replaceAll(":","").replaceAll("CDT","").replaceAll("000000  ","");
                        String link=entry.getLink();

                        if(description.equals(""))
                            list[i]=title+"\n\n"+date;
                        else
                            list[i]=title+"\n\n"+description+"\n\n"+date;

                        cfisdurl.add(link);
                        i++;
                    }
                }
                else
                    throw new Exception();
            }
            catch(Exception e)
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
                listView=(ListView)findViewById(R.id.calendarlistview);
                adapter=new ArrayAdapter(getApplicationContext(),R.layout.layout,R.id.announcetextview,list);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse(cfisdurl.get(position)));
                        startActivity(web);
                    }
                });
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error: please check your internet connection!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

