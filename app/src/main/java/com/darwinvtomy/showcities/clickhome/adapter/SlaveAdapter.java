package com.darwinvtomy.showcities.clickhome.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.darwinvtomy.showcities.clickhome.HttpManagers.HttpButtonManager;
import com.darwinvtomy.showcities.clickhome.R;
import com.darwinvtomy.showcities.clickhome.model.Slave;

import java.util.List;

public class SlaveAdapter extends ArrayAdapter<Slave> {
    private Context context;
    SeekBar seekbar1;
   // TextView seek;
    //TextView result;
    MediaPlayer mp;
    int statusvalue = 0;
    private List<Slave> slaveList;
    SharedPreferences pref;
    static String ipaddress;
    private LruCache<Integer, Bitmap> imagCache;
    private RequestQueue queue;

    public SlaveAdapter(Context context, int resource, List<Slave> objects) {
        super(context, resource, objects);
        this.context = context;
        this.slaveList = objects;
        pref = context.getSharedPreferences("mypref", 0);
        ipaddress = pref.getString("ipaddr", "");
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imagCache = new LruCache<>(cacheSize);

        queue = Volley.newRequestQueue(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.item_flower, parent, false);

        final Slave slave = slaveList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
       // seek = (TextView) view.findViewById(R.id.seek);
        seekbar1 = (SeekBar) view.findViewById(R.id.sbBar);
    //    result = (TextView) view.findViewById(R.id.out);

      //  seek.setText(slave.getValue());
        SeekBar sb = (SeekBar) view.findViewById(R.id.sbBar);
        Switch sw = (Switch) view.findViewById(R.id.switch1);
        String url1 = "http://" + ipaddress + "/hafinal/update_onoff.php?sid=";
        String url2 = "&status=on";
        String url3 = "&status=off";
        final String urlon = url1 + slave.getSid() + url2;
        final String urloff = url1 + slave.getSid() + url3;
        if (slave.getAlarmstatus().equals("1")) {
          //  result.setText(slave.getTime());
        }
        if (slave.getType().equals("fan")) {
            seekbar1.setMax(4);
            try {
                seekbar1.setProgress(findMode(slave.getValue()));
            } catch (NumberFormatException e) {
                seekbar1.setProgress(0);
                e.printStackTrace();
            }
         //   seek.setVisibility(View.VISIBLE);
            sb.setVisibility(View.VISIBLE);
            sw.setVisibility(View.INVISIBLE);
        } else if (slave.getType().equals("dimer")) {
            seekbar1.setMax(898);
            try {
                seekbar1.setProgress(Integer.parseInt(slave.getValue()) - 2);
            } catch (NumberFormatException e) {
                seekbar1.setProgress(0);
                e.printStackTrace();
            }
         //   seek.setVisibility(View.VISIBLE);
            sb.setVisibility(View.VISIBLE);
            sw.setVisibility(View.INVISIBLE);
        } else {

            if (slave.getEnable().equals("0")) {
                sb.setVisibility(View.INVISIBLE);
                sw.setVisibility(View.INVISIBLE);
            } else {
             //   seek.setVisibility(View.INVISIBLE);
                sb.setVisibility(View.INVISIBLE);
                sw.setVisibility(View.VISIBLE);
                if (slave.getStatus().equals("off")) {
                    sw.setChecked(false);
                } else {
                    sw.setChecked(true);
                }
            }

        }
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (slave.getType().equals("fan")) {
                    statusvalue = setstatusvalue(progress);
                } else {
                    statusvalue = progress + 4;
                }
              //  Slave selectedSlave = slaveList.get(position);
             //   seek = (TextView) view.findViewById(R.id.seek);
             //   seek.setText(statusvalue + "");


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Uri path = Uri.parse("android.resource://com.darwinvtomy.showcities.clickhome/" + R.raw.seekbar_sound);

                mp = MediaPlayer.create(getContext(), path);
                //  mp.setLooping(true);
                mp.start();

                //  Toast.makeT

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String statusurl = null;
                if (slave.getType().equals("fan")) {
                    statusurl = "http://" + ipaddress + "/hafinal/fan.php?sid=" + slave.getSid() + "&value=" + statusvalue;
                } else {
                    statusurl = "http://" + ipaddress + "/hafinal/dimer.php?sid=" + slave.getSid() + "&value=" + statusvalue;
                }

                Log.i("DATA", statusurl);
                Slave selectedSlave = slaveList.get(position);
                selectedSlave.setValue(statusvalue + "");
                requestData(statusurl);
                if (mp.isPlaying()) {
                    mp.stop();
                }

            }
        });
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Slave selectedSlave = slaveList.get(position);


                if (isChecked) {

                    requestData(urlon);
                    Log.i("DATA", urlon);
                    Uri path = Uri.parse("android.resource://com.darwinvtomy.showcities.clickhome/" + R.raw.switch_on);

                    mp = MediaPlayer.create(getContext(), path);
                    mp.start();
                    selectedSlave.setStatus("on");
                    //  Toast.makeText(getContext(), selectedSlave.getName()+" is ON", Toast.LENGTH_SHORT).show();

                } else {
                    Log.i("DATA", urloff);
                    requestData(urloff);
                    Uri path = Uri.parse("android.resource://com.darwinvtomy.showcities.clickhome/" + R.raw.switch_off);

                    mp = MediaPlayer.create(getContext(), path);
                    mp.start();
                    selectedSlave.setStatus("off");
                    //  Toast.makeText(getContext(), selectedSlave.getName()+" is OF", Toast.LENGTH_SHORT).show();
                }

            }
        });


        tv.setText(slave.getName()/*+"\n"+master.getId()+"\n"+master.getMid()+"\n"+master.getImage()*/);

        Bitmap bitmap = imagCache.get(slave.getId());
        final ImageView image = (ImageView) view.findViewById(R.id.imageView2);
        if (bitmap != null) {

            image.setImageBitmap(bitmap);
        } else {

            String imageUrl = "http://" + ipaddress + "/hafinal/img/" + slave.getImg();
            ImageRequest request = new ImageRequest(imageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            image.setImageBitmap(bitmap);
                            imagCache.put(slave.getId(), bitmap);
                        }
                    },

                    60, 60,
                    Bitmap.Config.ARGB_8888,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("SlaveAdapter", volleyError.getMessage());
                        }
                    }
            );
            queue.add(request);

        }


        return view;
    }

    private int setstatusvalue(int progress) {

        int statusvalue = 0;
        switch (progress) {
            case 0:
                statusvalue = 4;
                break;
            case 1:
                statusvalue = 250;
                break;
            case 2:
                statusvalue = 350;
                break;
            case 3:
                statusvalue = 500;
                break;
            case 4:
                statusvalue = 899;
                break;
            default:
                statusvalue = 4;


        }
        return statusvalue;
    }

    private int findMode(String value) {
        int fanmode = 0;
        switch (value) {
            case "4":
                fanmode = 0;
                break;
            case "250":
                fanmode = 1;
                break;
            case "350":
                fanmode = 2;
                break;
            case "500":
                fanmode = 3;
                break;
            case "899":
                fanmode = 4;
                break;
            default:
                fanmode = 0;

        }
        return fanmode;

    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay(String message) {

      //  result.setText(message);
        //   Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
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
