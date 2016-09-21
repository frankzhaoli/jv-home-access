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

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class DisplayNews extends AppCompatActivity {

    private ArrayAdapter adapter;
    private ListView listView;
    private String[]list;
    private List<String>cfisdnewsurl=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_news);
        setTitle("CFISD News");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new downloadNews().execute("");
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

    private class downloadNews extends AsyncTask<String,Integer,String>
    {
        Exception exception=null;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String...string)
        {
            try
            {
                String webs="http://www.cfisd.net/tools/blocks/page_list/rss?bID=14350&cID=141&arHandle=Main";
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
                        String date=entry.getPublishedDate().toString();
                        String link=entry.getLink().substring(6);

                        list[i]=title+"\n\n"+description+"\n\n"+date;
                        cfisdnewsurl.add(link);
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
                listView=(ListView)findViewById(R.id.newslistview);
                adapter=new ArrayAdapter(getApplicationContext(),R.layout.layout,R.id.announcetextview,list);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent webs = new Intent(Intent.ACTION_VIEW, Uri.parse(cfisdnewsurl.get(position)));
                        startActivity(webs);
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
