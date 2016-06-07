package com.darwinvtomy.showcities.clickhome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.darwinvtomy.showcities.clickhome.adapter.SlaveAdapter;
import com.darwinvtomy.showcities.clickhome.model.Slave;
import com.darwinvtomy.showcities.clickhome.parsers.SlaveParser;

import java.util.List;


public class SlaveList extends AppCompatActivity {
    public String slaveUrl;
    Handler mHandler;
    String id;
    String mid;
    String name;
    ListView lv;
    SharedPreferences pref;
    //   SharedPreferences.Editor editor;
    //TextView tv;
    List<Slave> slaveList;
    //  ListView listView;
    private SlaveAdapter adapter;
    private final Runnable m_Runnable = new Runnable() {
        public void run() {

            if (isOnline()) {

              requestData(slaveUrl);
                //relodTheData(slaveUrl);
                // getListView().onRestoreInstanceState();
            } else {
                Toast.makeText(SlaveList.this, "Network isn't available",
                        Toast.LENGTH_SHORT).show();
            }
            SlaveList.this.mHandler.postDelayed(m_Runnable,
                    60000);
        }

    };

    private void relodTheData(String slaveUrl) {

        relodData(slaveUrl);
        // update data in our adapter
        // fire the event
       // lv.setAdapter(adapter);
        adapter = new SlaveAdapter(this, R.layout.item_flower, slaveList);
        adapter.notifyDataSetChanged();
     //   lv.invalidateViews();
      //  lv.refreshDrawableState();
    }

    //  Parcelable state = getListView().onSaveInstanceState();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave_list);
        this.mHandler = new Handler();
        this.mHandler.postDelayed(m_Runnable, 10000);
        // listView = (ListView) findViewById();
            lv = (ListView) findViewById(R.id.Slavelist);
        pref = getSharedPreferences("mypref", MODE_PRIVATE);
        //editor = pref.edit();
        String ipaddress = pref.getString("ipaddr", "");

        Intent i = getIntent();
        id = i.getStringExtra("id");
        mid = i.getStringExtra("mid");
        name = i.getStringExtra("name");
        slaveUrl = "http://" + ipaddress + "/hafinal/ck_xml.php?node=Slave&mid=" + mid;


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SlaveList.this, DeviceDetails.class);

                GlobalClass.setSelectedSlave(adapter.getItem(position));
                intent.putExtra("img", adapter.getItem(position).getImg());
                intent.putExtra("sname", adapter.getItem(position).getName());
                intent.putExtra("id", adapter.getItem(position).getId());
                intent.putExtra("mid", adapter.getItem(position).getMid());
                intent.putExtra("sid", adapter.getItem(position).getSid());
                intent.putExtra("type", adapter.getItem(position).getType());
                intent.putExtra("status", adapter.getItem(position).getStatus());
                intent.putExtra("value", adapter.getItem(position).getValue());
                intent.putExtra("time", adapter.getItem(position).getTime());
                intent.putExtra("alarmstatus", adapter.getItem(position).getAlarmstatus());
                // Toast.makeText(Slave_List.this,adapter.getItem(position).getEnable(),Toast.LENGTH_SHORT).show();
                if (adapter.getItem(position).getEnable().equals("0")) {
                    Toast.makeText(SlaveList.this, "Device is Not Enabled ", Toast.LENGTH_SHORT).show();
                } else {
                    if(adapter.getItem(position).getType().equals("switch")){
                        startActivity(intent);
                    }

                }

                return true;

            }
        });

        if (isOnline()) {

            requestData(slaveUrl);
        } else {
            Toast.makeText(this, "Network isn't available",
                    Toast.LENGTH_LONG).show();
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_slave_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestData(String uri) {

        StringRequest request = new StringRequest(uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        slaveList = SlaveParser.parseFeed(response);
                        updateDisplay();
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError ex) {
                        Toast.makeText(SlaveList.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    protected void updateDisplay() {

        adapter = new SlaveAdapter(this, R.layout.item_flower, slaveList);

        lv.setAdapter(adapter);


    }

    private void relodData(String uri) {
            slaveList.clear();
        StringRequest request = new StringRequest(uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        slaveList = SlaveParser.parseFeed(response);

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError ex) {
                        Toast.makeText(SlaveList.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);


    }

}
