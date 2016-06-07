
package com.darwinvtomy.showcities.clickhome.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.darwinvtomy.showcities.clickhome.R;
import com.darwinvtomy.showcities.clickhome.model.Master;

import java.util.List;

public class MasterAdapter extends ArrayAdapter<Master> {
	private Context context;

	private List<Master> masterList;
	SharedPreferences pref;
	static String ipaddress;
	private LruCache<Integer, Bitmap> imagCache;
    private RequestQueue queue;

	public MasterAdapter (Context context, int resource, List<Master> objects){
		super(context, resource, objects);
		this.context = context;
		this.masterList = objects;
		pref =	context.getSharedPreferences("mypref", 0);
     	ipaddress = pref.getString("ipaddr", "");
		final int maxMemory = (int) ( Runtime.getRuntime().maxMemory()/1024) ;
		final int cacheSize = maxMemory / 8;
		imagCache = new LruCache<>(cacheSize);

        queue = Volley.newRequestQueue(context);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.grid_item, parent, false);

		final Master master = masterList.get(position);
		TextView tv = (TextView) view.findViewById(R.id.griditemname);
		tv.setText(master.getName()/*+"\n"+master.getId()+"\n"+master.getMid()+"\n"+master.getImage()*/);
		Bitmap bitmap = imagCache.get(master.getId());
       final ImageView image = (ImageView) view.findViewById(R.id.imageView1);
		if (bitmap != null) {		

			image.setImageBitmap(bitmap);
		}else{
/*			MasterAndView container = new MasterAndView();
			container.master = master;
			container.view = view;
			ImagerLoader loader  = new ImagerLoader();
			loader.execute(container);*/
            String imageUrl = "http://"+ipaddress+"/hafinal/img/"+master.getImage();
            ImageRequest request = new ImageRequest(imageUrl,
                   new Response.Listener<Bitmap>() {
                       @Override
                       public void onResponse(Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                           imagCache.put(master.getId(),bitmap);
                       }
                   },

           80,80,
                 Bitmap.Config.ARGB_8888,
                   new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError volleyError) {
                           Log.d("MasterAdapter",volleyError.getMessage() );
                       }
                   }
                    );
            queue.add(request);

		}


		return view;
	}


/*	class MasterAndView {
		public Master master;
		public View view;
		public Bitmap bitmap;


	}*/
	/*private class ImagerLoader extends AsyncTask<MasterAndView, Void ,MasterAndView >{

		@Override
		protected MasterAndView doInBackground(MasterAndView... params) {

			MasterAndView container = params[0];
			Master master = container.master;
			try {
				String imageUrl = "http://"+ipaddress+"/hafinal/img/"+master.getImage();
				InputStream in = (InputStream) new URL (imageUrl).getContent();
				Bitmap bitmap = BitmapFactory.decodeStream(in);
				master.setBitmap(bitmap);
				in.close();
				container.bitmap = bitmap;
				return container;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(MasterAndView result) {

			ImageView image = (ImageView) result.view.findViewById(R.id.imageView1);
			image.setImageBitmap(result.bitmap);
			//	result.master.setBitmap(result.bitmap);
			//	super.onPostExecute(result);
			imagCache.put(result.master.getId(), result.bitmap);
		}

	}*/
}
