package com.darwinvtomy.showcities.clickhome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.darwinvtomy.showcities.clickhome.adapter.MasterAdapter;
import com.darwinvtomy.showcities.clickhome.model.Master;
import com.darwinvtomy.showcities.clickhome.parsers.MasterParser;

import java.util.List;


public class MasterListActivity extends ActionBarActivity {
	
	private static final int DLG_EXAMPLE1 = 0;
    private static final int TEXT_ID = 0;
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	TextView output;
	public String ipaddress;
	//List<MyTask> tasks;

	List<Master> masterList;
	GridView gridView;
	
private MasterAdapter adapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		setContentView(R.layout.master_gridview);
		
		//Shared preferences 
		pref = getSharedPreferences("mypref", MODE_PRIVATE);
		editor = pref.edit();

	 ipaddress = pref.getString("ipaddr", "");
		 if (ipaddress.equals("")) {
			 showDialog(DLG_EXAMPLE1);
		}
		
		//Shared Preferwences
		
		gridView = (GridView) findViewById(R.id.gridView1);
	//	tasks = new ArrayList<>();
		if (isOnline()) {
			requestData("http://"+ipaddress+"/hafinal/ck_xml.php?node=Master");
		} else {
			Toast.makeText(this, "Network isn't available",
					Toast.LENGTH_LONG).show();
		}
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				try {
				//	Toast.makeText(MasterListActivity.this, "Item Clicked "+adapter.getItem(position).getName()+" Devi id "+adapter.getItem(position).getMid(), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MasterListActivity.this, SlaveList.class);
					int ID = adapter.getItem(position).getId();
					intent.putExtra("id", ""+ID);
					intent.putExtra("mid", adapter.getItem(position).getMid());
					intent.putExtra("name", adapter.getItem(position).getName());
					startActivity(intent);
				} catch (Exception e) {
					Log.i("ITEM CLICK", e.getMessage());
					e.printStackTrace();
				}
				
			}
		});
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MasterListActivity.this,"Mood Settings",Toast.LENGTH_SHORT).show();
               try {
                    Intent intent = new Intent(MasterListActivity.this, Mood_Activity.class);

                    intent.putExtra("id",adapter.getItem(position).getId()+"");
                    intent.putExtra("mid", adapter.getItem(position).getMid());
                    intent.putExtra("name", adapter.getItem(position).getName());
                    intent.putExtra("ip",ipaddress);
                    startActivity(intent);
                }catch (Exception e){
                    Log.i("ITEM CLICK", e.getMessage());
                    e.printStackTrace();
                }
                return true;
            }
        });
		
		
	}


	private void requestData(String uri) {
		/*MyTask task = new MyTask();
		task.execute(uri);*/
        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    masterList = MasterParser.parseFeed(response);
                        updateDisplay();
                    }
                },

        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError ex) {
                Toast.makeText(MasterListActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

	}

	protected void updateDisplay() {
		// Use FlowerAdapter to display data
		adapter = new MasterAdapter(this, R.layout.grid_item,
				masterList);
		gridView.setAdapter(adapter);
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

/*	private class MyTask extends AsyncTask<String, String, List<Master>> {

		@Override
		protected void onPreExecute() {
		//	tasks.add(this);
		}

		@Override
		protected List<Master> doInBackground(String... params) {

			String content = HttpManager.getData(params[0]);
			masterList = MasterParser.parseFeed(content);

			return masterList;
		}

		@Override
		protected void onPostExecute(List<Master> result) {

		//	tasks.remove(this);

			if (result == null) {
				Toast.makeText(GridExample.this, "Web service not available",
						Toast.LENGTH_LONG).show();
				return;
			}

			masterList = result;
			updateDisplay();

		}

	}*/
	
    @Override
    protected Dialog onCreateDialog(int id) {
 
        switch (id) {
            case DLG_EXAMPLE1:
                return createExampleDialog();
            default:
                return null;
        }
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
 
        switch (id) {
            case DLG_EXAMPLE1:
                // Clear the input box.
                EditText text = (EditText) dialog.findViewById(TEXT_ID);
          
                String value = pref.getString("ipaddr", "");
                text.setText(value);
                break;
        }
    }
    
    private Dialog createExampleDialog() {
    	 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hello User");
        builder.setMessage("Enter new Ip Address");
 
         // Use an EditText view to get user input.
         final EditText input = new EditText(this);
        
         input.setId(TEXT_ID);
     	
         builder.setView(input);
 
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                editor.putString("ipaddr", value);
				editor.commit();
				requestData("http://"+value+"/hafinal/ck_xml.php?node=Master");
				Toast.makeText(MasterListActivity.this,
						"Saved data into preference", Toast.LENGTH_LONG)
						.show();
                return;
            }
        });
 
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
                return;
                
            }
        });
 
        return builder.create();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	 showDialog(DLG_EXAMPLE1);
        	
            return true;
        }
        return super.onOptionsItemSelected(item);
        
    
    }

}
