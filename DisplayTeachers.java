package com.xshiki.zhaoli.jvha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DisplayTeachers extends AppCompatActivity {

    public String[]list;

    private ListView listView;
    private ArrayAdapter adapter;
    private List<String> teacherurl=new ArrayList<>();
    private static final String PREFSNAME="teachers";
    private  SharedPreferences sp;

    public void getTeachers()
    {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String webs="https://alpine-furnace-774.appspot.com/jvapp?type=2";

        try
        {
            URI uri=URI.create(webs);
            String validURL=uri.toASCIIString();
            URL url=new URL(validURL);

            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            JsonParser parser=new JsonParser();
            JsonElement element=parser.parse(new InputStreamReader((InputStream)connection.getContent()));
            JsonObject obj=element.getAsJsonObject();
            JsonArray array=obj.get("results:").getAsJsonArray();
            list=new String[array.size()-1];

            for(int t=0; t<array.size(); t++)
            {
                JsonObject teach=array.get(t).getAsJsonObject();
                String teachers=teach.get("name").getAsString();
                String pageUrl=teach.get("pageUrl").getAsString();
                teacherurl.add(pageUrl);

                list[t]=teachers;
            }
        }
        catch(Exception e)
        {
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_teachers);
        setTitle("Teachers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp=getSharedPreferences(PREFSNAME,MODE_PRIVATE);

        GregorianCalendar gregorianCalendar=new GregorianCalendar();
        int month=gregorianCalendar.get(Calendar.MONTH);

        if(month==7||month==8)
        {
            new downloadTeachers().execute("");
        }

        if(sp.getString("teachers"+0,null)!=null&&sp.getString("websites"+0,null)!=null&&sp.getInt("number", 0)!=0)
        {
            list=new String[sp.getInt("number",0)];

            for(int i=0;i<list.length;i++)
            {
                list[i]=sp.getString(PREFSNAME+i,"");
                teacherurl.add(sp.getString("websites"+i,""));
            }

            listView=(ListView)findViewById(R.id.teacherlistview);

            if(list!=null)
            {
                adapter=new ArrayAdapter(getApplicationContext(),R.layout.layout,R.id.announcetextview,list);
                listView.setAdapter(adapter);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error: Please check your internet connection!", Toast.LENGTH_SHORT).show();
                finish();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent web=new Intent(Intent.ACTION_VIEW,Uri.parse(teacherurl.get(position)));
                    startActivity(web);
                }
            });
        }

        else
            new downloadTeachers().execute("");
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

    private class downloadTeachers extends AsyncTask<String,Integer,String>
    {
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
            String webs="https://alpine-furnace-774.appspot.com/jvapp?type=2";

            try
            {
                URI uri=URI.create(webs);
                String validURL=uri.toASCIIString();
                URL url=new URL(validURL);

                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                JsonParser parser=new JsonParser();
                JsonElement element=parser.parse(new InputStreamReader((InputStream)connection.getContent()));
                JsonObject obj=element.getAsJsonObject();
                JsonArray array=obj.get("results:").getAsJsonArray();
                list=new String[array.size()];

                for(int t=0; t<array.size()+1; t++)
                {
                    JsonObject teach=array.get(t).getAsJsonObject();
                    String teachers=teach.get("name").getAsString();
                    String pageUrl=teach.get("pageUrl").getAsString();
                    teacherurl.add(pageUrl);

                    list[t]=teachers;

                    SharedPreferences.Editor editor=getSharedPreferences(PREFSNAME,MODE_PRIVATE).edit();

                    editor.putString("teachers"+t,teachers);
                    editor.putString("websites"+t,pageUrl);
                    editor.putInt("number", array.size());
                    editor.apply();
                }
            }
            catch(Exception e)
            {
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                exception=e;
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            if(exception!=null)
            {
                listView=(ListView)findViewById(R.id.teacherlistview);

                if(list!=null)
                {
                    adapter=new ArrayAdapter(getApplicationContext(),R.layout.layout,R.id.announcetextview,list);
                    listView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error: Please check your internet connection!", Toast.LENGTH_SHORT).show();
                    finish();
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse(teacherurl.get(position)));
                        startActivity(web);
                    }
                });
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error: please check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
