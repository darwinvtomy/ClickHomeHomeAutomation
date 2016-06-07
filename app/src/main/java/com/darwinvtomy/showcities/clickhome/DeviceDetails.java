package com.darwinvtomy.showcities.clickhome;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.darwinvtomy.showcities.clickhome.HttpManagers.HttpButtonManager;
import com.darwinvtomy.showcities.clickhome.model.Slave;


public class DeviceDetails extends Activity implements OnSeekBarChangeListener {

    static int TIME_DIALOG_ON_OFF = 0;
    SharedPreferences pref;
    //	SharedPreferences.Editor editor;
    TextView d_sname;
    CheckBox cb;
    ImageView d_image;
    Bitmap bitmap;
    String ipaddress;
    ImageView close;
    int on_hour, on_min;
    int off_hour, off_min;
    //SeekBar seekbar1;
    int statusvalue = 0;

    Switch deviceswSwitch;
    Button alarmButton;
    Button alarmOffButton;
    TimePickerDialog tpd;
    Slave selectedSlave;
    private String id;
    private String mid;
    private String sid;
    private String sname;
    private String type;
    private String status;
    private String value;
    //private String on_time;
    private String alarmstatus;
    private String img;
    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            on_hour = hourOfDay;
            on_min = minute;
            String set_time = ConvertToMilatry(on_hour,on_min);
                  //   on_hour + ":" + on_min;

            //d_alarmstatus.setText(on_time);

            //  d_time.setText(on_time);
            if (TIME_DIALOG_ON_OFF == 0) {
                requestData("http://" + ipaddress + "/hafinal/time_chk.php?sid=" + sid + "&time=" + set_time + "&astatus=1");
                //  Log.e("CLICK HOME","http://" + ipaddress + "/hafinal/time_chk.php?sid=" + sid + "&time=" + set_time + "&astatus=1");
                selectedSlave.setTime(set_time);
                alarmButton.setText(set_time);
            } else {
                requestData("http://" + ipaddress + "/hafinal/alarm_off.php?sid=" + sid + "&off_time=" + set_time + "&astatus=1");
                //  Log.e("CLICK HOME","http://" + ipaddress + "/hafinal/alarm_off.php?sid=" + sid + "&off_time=" + set_time + "&astatus=1");

                selectedSlave.setOf_time(set_time);
                alarmOffButton.setText(set_time);
            }

            cb.setChecked(true);
        }
    };

    private String ConvertToMilatry(int on_hour, int on_min) {
        return String.format("%02d:%02d",on_hour,on_min);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);
        selectedSlave = GlobalClass.getTheSelectedSlave();

        alarmButton = (Button) findViewById(R.id.alrmbtn);
        alarmOffButton = (Button) findViewById(R.id.alarm_offtime);
        //   d_image = (ImageView) findViewById(R.id.singledevicedisplay);
        cb = (CheckBox) findViewById(R.id.alarmcheckBox);
        close = (ImageView) findViewById(R.id.imageView);
        //Shared preferences
        pref = getSharedPreferences("mypref", MODE_PRIVATE);
        //	editor = pref.edit();
        ipaddress = pref.getString("ipaddr", "");

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        mid = intent.getStringExtra("mid");
        sname = intent.getStringExtra("sname");
        sid = intent.getStringExtra("sid");
        type = intent.getStringExtra("type");
        status = intent.getStringExtra("status");
        value = intent.getStringExtra("value");
        //  on_time = intent.getStringExtra("on_time");
        alarmstatus = intent.getStringExtra("alarmstatus");
        img = intent.getStringExtra("img");

        d_sname = (TextView) findViewById(R.id.sname);

        //  d_time = (TextView) findViewById(R.id.on_time);
        //    d_alarmstatus = (TextView) findViewById(R.id.alarmstatus);

        //   d_img.setText(img);
        d_sname.setText(sname);
        //   d_time.setText(on_time);
        alarmButton.setText(selectedSlave.getTime());
        alarmOffButton.setText(selectedSlave.getOf_time());
        //  d_alarmstatus.setText(alarmstatus);
      /*  Calendar c = Calendar.getInstance();
        on_hour = c.get(Calendar.HOUR);
        on_min = c.get(Calendar.MINUTE);*/


        String[] Splittime = selectedSlave.getTime().split(":");
        on_hour = Integer.parseInt(Splittime[0].trim());
        on_min = Integer.parseInt(Splittime[1].trim());

        String[] OFFSplittime = selectedSlave.getOf_time().split(":");
        off_hour = Integer.parseInt(OFFSplittime[0].trim());
        off_min = Integer.parseInt(OFFSplittime[1].trim());





      /*  DateFormat formatter = new SimpleDateFormat("HH:mm");

        try {
            Time ontimeValue = new Time(String.valueOf(formatter.parse(on_time).getTime()));
            Time offtimeValue = new Time(String.valueOf(formatter.parse(selectedSlave.getOf_time()).getTime()));
            on_hour = ontimeValue.hour;
            on_min = ontimeValue.minute;

            off_hour = offtimeValue.hour;
            off_min = offtimeValue.minute;
        } catch (ParseException e) {
            e.printStackTrace();
        }*/


        String url1 = "http://" + ipaddress + "/hafinal/update_onoff.php?sid=";
        String url2 = "&status=on";
        String url3 = "&status=off";
        final String urlon = url1 + sid + url2;
        final String urloff = url1 + sid + url3;

        //   seek.setText(value);
        if (alarmstatus.equals("0")) {
            cb.setChecked(false);
        } else {
            cb.setChecked(true);
        }


        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (selectedSlave.getTime().equals("00:00")) {
                    selectedSlave.setTime("6:00");
                    alarmButton.setText(selectedSlave.getTime());
                    //  d_time.setText("6:00");
                }
                if (isChecked) {
                    GlobalClass.getTheSelectedSlave().setAlarmstatus("1");
                    selectedSlave.setAlarmstatus("1");
                    requestData("http://" + ipaddress + "/hafinal/time_chk.php?sid=" + sid + "&on_time=" + selectedSlave.getTime() + "&astatus=1");
                } else {
                    GlobalClass.getTheSelectedSlave().setAlarmstatus("0");
                    selectedSlave.setAlarmstatus("0");
                    requestData("http://" + ipaddress + "/hafinal/time_chk.php?sid=" + sid + "&on_time=" + selectedSlave.getTime() + "&astatus=0");
                }


            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showTimeDialog(View v) {
        if (v.getId() == R.id.alarm_offtime) {
            TIME_DIALOG_ON_OFF = 1;
        } else {
            TIME_DIALOG_ON_OFF = 0;
        }


        showDialog(TIME_DIALOG_ON_OFF);

    }

    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            return new TimePickerDialog(this, timeSetListener, on_hour, on_min, false);
        }
        return new TimePickerDialog(this, timeSetListener, off_hour, off_min, false);
    }

    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        statusvalue = progress + 51;
        // seek.setText(statusvalue+"");
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

        String statusurl = "http://" + ipaddress + "/hafinal/dimer.php?sid=" + sid + "&value=" + statusvalue;
        Log.i("DATA", statusurl);
        requestData(statusurl);
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay(String message) {
        //  output.setText(message);
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
            updateDisplay(result);

        }

        @Override
        protected void onProgressUpdate(String... values) {
            updateDisplay(values[0]);
        }

    }

}
