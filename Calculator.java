package com.xshiki.zhaoli.jvha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;

public class Calculator extends AppCompatActivity {

    public EditText first,second,third,finaltestgrade,semestergrade;
    public CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        setTitle("Average Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        first=(EditText)findViewById(R.id.firstsix);
        second=(EditText)findViewById(R.id.secondsix);
        third=(EditText)findViewById(R.id.thirdsix);
        finaltestgrade=(EditText)findViewById(R.id.finaltestgrade);
        semestergrade=(EditText)findViewById(R.id.semestergrade);
        checkbox=(CheckBox)findViewById(R.id.finalgradecheck);
    }

    public void calc(View view)
    {
        try
        {
            Boolean check=checkbox.isChecked();
            DecimalFormat df=new DecimalFormat("0.00");
            int f=Integer.parseInt(first.getText().toString());
            int s=Integer.parseInt(second.getText().toString());
            int t=Integer.parseInt(third.getText().toString());
            int finals=0;
            double semestergradenum=0;

            if(check)
            {
                finals=Integer.parseInt(finaltestgrade.getText().toString());
                semestergradenum=(((f+s+t)*2.0)+finals)/7.0;
            }
            else
            {
                semestergradenum=(f+s+t)/3.0;

            }
            //semestergradenum=Math.round(semestergradenum);

            String text=String.valueOf(df.format(semestergradenum));
            semestergrade.setText(text);
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Error: Please check the numbers!", Toast.LENGTH_SHORT).show();
        }
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
