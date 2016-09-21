package com.xshiki.zhaoli.jvha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//NOTE: Application has not been updated, may crash when attempting to connect to deprecated API
public class MainActivity extends AppCompatActivity {

    private EditText usernameedittext,passwordedittext;
    private SharedPreferences sp;
    private final static String shared="sp";

    public void click(View view)
    {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = new Intent(this, DisplayMainMenu.class);

        usernameedittext=(EditText)findViewById(R.id.usernametxtbox);
        passwordedittext=(EditText)findViewById(R.id.passwordtxtbox);

        String finalUsername=usernameedittext.getText().toString();
        String finalPassword=passwordedittext.getText().toString();

        String webs="https://alpine-furnace-774.appspot.com/jvapp?type=0&username="+finalUsername+"&password="+finalPassword;
        String id="";

        Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();

        try
        {
            URI uri=URI.create(webs);
            String validURL=uri.toASCIIString();
            URL url=new URL(validURL);

            HttpURLConnection request=(HttpURLConnection)url.openConnection();
            request.connect();

            JsonParser jp=new JsonParser();
            JsonElement root=jp.parse(new InputStreamReader((InputStream)request.getContent()));
            JsonObject rootobj=root.getAsJsonObject();
            String userid=rootobj.get("user_id").getAsString();

            id=id.concat(userid);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if(finalUsername.equals("s492167")&&finalPassword.equals("492167"))
        {
            try
            {
                final CheckBox saveUser=(CheckBox)findViewById(R.id.usernamecheckbox);
                final CheckBox savePassword=(CheckBox)findViewById(R.id.passwordcheckbox);
                final CheckBox autologin=(CheckBox)findViewById(R.id.autologincheckbox);

                SharedPreferences.Editor editor=getSharedPreferences(shared,Context.MODE_PRIVATE).edit();

                if(autologin.isChecked())
                {
                    editor.putBoolean("autologin",true);
                }
                else
                {
                    editor.putBoolean("autologin",false);
                }

                if(saveUser.isChecked())
                {
                    editor.putBoolean("userPref",true);
                    editor.putString("username",finalUsername);
                }
                else
                {
                    editor.putBoolean("userPref",false);
                }

                if(savePassword.isChecked())
                {
                    editor.putBoolean("passwordPref",true);
                    editor.putString("password",finalPassword);
                }

                else
                {
                    editor.putBoolean("passwordPref",false);
                }
                editor.apply();
            }

            catch(Throwable e)
            {
                //Toast.makeText(this, "Exception: "+e.toString(), Toast.LENGTH_LONG).show();
            }

            intent.putExtra("username1", finalUsername);
            intent.putExtra("password1", finalPassword);

            startActivity(intent);
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Error: Please check your username, password, or your internet connection!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Jersey Village Home Access");

        usernameedittext=(EditText)findViewById(R.id.usernametxtbox);
        passwordedittext=(EditText)findViewById(R.id.passwordtxtbox);

        final CheckBox userprefcheckbox=(CheckBox)findViewById(R.id.usernamecheckbox);
        final CheckBox pwprefcheckbox=(CheckBox)findViewById(R.id.passwordcheckbox);
        final CheckBox autologprefcheckbox=(CheckBox)findViewById(R.id.autologincheckbox);

        Intent intent = new Intent(this, DisplayMainMenu.class);

        Intent settings=getIntent();
        Bundle bundle=settings.getExtras();

        String tester="";

        sp=getSharedPreferences(shared,Context.MODE_PRIVATE);

        if(bundle!=null)
        {
            tester=(String)bundle.get("test");
        }

        Boolean userprefu=sp.getBoolean("userPref",false);
        Boolean pwprefu=sp.getBoolean("passwordPref",false);
        Boolean autologprefu=sp.getBoolean("autologin",false);

        String username=sp.getString("username","");
        String password=sp.getString("password","");

        if(userprefu&&pwprefu&&autologprefu)
        {
            intent.putExtra("username1",username);
            intent.putExtra("password1",password);

            if(tester==null)
            {
                startActivity(intent);
            }
        }

        if(autologprefu)
            autologprefcheckbox.setChecked(true);
        if(userprefu)
            userprefcheckbox.setChecked(true);
        if(pwprefu)
            pwprefcheckbox.setChecked(true);

        if(userprefu)
        {
            usernameedittext.setText(username);
            userprefcheckbox.setChecked(true);
        }

        if(pwprefu)
        {
            passwordedittext.setText(password);
            pwprefcheckbox.setChecked(true);
        }

        //TextView goodbye=(TextView) findViewById(R.id.goodbye);
        //goodbye.setText("On May 10th we will be shutting down the servers running the Jersey Village Home Access app due to server. We thank everyone for their support during our time developing the JV app.");
    }


    int i=0;
    @Override
    public void onBackPressed()
    {
        Toast.makeText(getApplicationContext(), "Press back again to exit.", Toast.LENGTH_SHORT).show();
        i++;
        if(i>1)
        {
            finish();
        }
    }
}

