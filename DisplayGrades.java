package com.xshiki.zhaoli.jvha;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;


public class DisplayGrades extends AppCompatActivity {

    public String[] gradesarray;
    public String[] assignments;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    private Intent disgradez;

    private String user="";
    private String pw="";

    private TextView coloredgrades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_grades);
        setTitle("Grades");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        disgradez=new Intent(this,DisplayAssignments.class);

        final Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        if(bundle!=null)
        {
            user=user.concat((String) bundle.get("username2"));
            pw=pw.concat((String)bundle.get("password2"));
        }

        new downloadGrades().execute("");
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

    public void startCalc(View view)
    {
        Intent calc=new Intent(this, Calculator.class);
        startActivity(calc);
    }

    private class downloadGrades extends AsyncTask<String,Integer,String>
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

            try
            {
                final String webs="https://alpine-furnace-774.appspot.com/jvapp?type=1&username="+user+"&password="+pw;
                //"https://alpine-furnace-774.appspot.com/jvapp?type=1&username=s492167&password=Orange30628";

                URI uri=URI.create(webs);
                String validURL=uri.toASCIIString();
                URL url=new URL(webs);

                HttpURLConnection connection=(HttpURLConnection)url.openConnection();

                JsonParser parser=new JsonParser();
                JsonElement element=parser.parse(new InputStreamReader((InputStream) connection.getContent()));

                JsonObject grades=element.getAsJsonObject();
                JsonArray array=grades.get("results:").getAsJsonArray();
                gradesarray=new String[array.size()];

                for(int a=0; a<array.size(); a++)
                {
                    JsonObject elements=array.get(a).getAsJsonObject();
                    String courseName=elements.get("courseName").getAsString().replaceAll("(\\d)", "").replaceAll("-", "");

                    JsonArray assigns=elements.get("assignments").getAsJsonArray();
                    assignments=new String[assigns.size()];

                    for(int b=0; b<assigns.size(); b++)
                    {
                        JsonObject object=assigns.get(b).getAsJsonObject();
                        String work=object.get("name").getAsString();
                        String type=object.get("type").getAsString();
                        int score=object.get("score").getAsInt();

                        assignments[b]=type+"- "+work+":  "+score;
                    }

                    disgradez.putExtra("assign"+a,assignments);
                    double average=elements.get("average").getAsDouble();
                    DecimalFormat df=new DecimalFormat("0.00");
                    gradesarray[a]=courseName+":  "+df.format(average);
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

            if(exception==null)
            {
                listView=(ListView)findViewById(R.id.gradelistview);
                    if(gradesarray!=null)
                    {
                        arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.layout,R.id.gradetextview,gradesarray);
                        //arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,gradesarray);
                        listView.setAdapter(arrayAdapter);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Error: Please check your internet connection!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            disgradez.putExtra("value", position);
                            startActivity(disgradez);
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
