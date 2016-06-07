package com.darwinvtomy.showcities.clickhome;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.darwinvtomy.showcities.clickhome.HttpManagers.HttpButtonManager;


public class Mood_Activity extends Activity {



  //  RelativeLayout Normal_Mode;
    TextView output;
    RelativeLayout Living_Mode;
    RelativeLayout Movie_mode;
    RelativeLayout Relax_Mode;
    RelativeLayout Dinner_Mode;
    RelativeLayout exit;
    String mood1=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = (TextView) findViewById(R.id.moodout);
       // String id = i.getStringExtra("id");
     //   String devicename = i.getStringExtra("name");
        Intent i = getIntent();
      final  String masterdevice = i.getStringExtra("mid");
       final String ipaddress=i.getStringExtra("ip");
   /*     String ipaddress = "11.253.139.30:84";
        private String masterdevice = "m1";*/
        mood1 ="http://"+ipaddress+"/hafinal/mode.php?mid="+masterdevice+"&mode=0" ;
        final String mood2 ="http://"+ipaddress+"/hafinal/mode.php?mid="+masterdevice+"&mode=1" ;
        final String mood3 ="http://"+ipaddress+"/hafinal/mode.php?mid="+masterdevice+"&mode=2" ;
        final String mood4 ="http://"+ipaddress+"/hafinal/mode.php?mid="+masterdevice+"&mode=3" ;
        final String mood5 ="http://"+ipaddress+"/hafinal/mode.php?mid="+masterdevice+"&mode=4" ;

      // Normal_Mode = (RelativeLayout) findViewById(R.id.living);
        Living_Mode = (RelativeLayout) findViewById(R.id.relax);
        Movie_mode = (RelativeLayout) findViewById(R.id.dinner);
        Relax_Mode = (RelativeLayout) findViewById(R.id.movie);
        Dinner_Mode = (RelativeLayout) findViewById(R.id.emergency);
        exit = (RelativeLayout) findViewById(R.id.exit);

       /* Normal_Mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData(mood1);
                Toast.makeText(Mood_Activity.this, "Normal Mode On", Toast.LENGTH_SHORT).show();
            }
        });*/
        Living_Mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData(mood2);
                Toast.makeText(Mood_Activity.this, "Living Mode On", Toast.LENGTH_SHORT).show();
            }
        });
        Movie_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData(mood3);
                Toast.makeText(Mood_Activity.this, "Movie Mode On", Toast.LENGTH_SHORT).show();
            }
        });
        Relax_Mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData(mood4);
                Toast.makeText(Mood_Activity.this, "Relax Mode On", Toast.LENGTH_SHORT).show();
            }
        });
        Dinner_Mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData(mood5);
                Toast.makeText(Mood_Activity.this, "Dinner Mode On", Toast.LENGTH_SHORT).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData(mood1);

            }
        });


    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }
    protected void updateDisplay(String message) {
        output.setText(message);
    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            updateDisplay("Starting task");

        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpButtonManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
           String exit = result.trim();
            if(exit.equals("exit")){
                finish();
            }else {
                updateDisplay(result);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            updateDisplay(values[0]);
        }

    }

    @Override
    public void onBackPressed() {
        requestData(mood1);

    }
}
