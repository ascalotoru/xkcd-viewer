package com.xkcdviewer.views;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ascalotoru.xkcd.R;
import com.xkcdviewer.data.Comic;
import com.xkcdviewer.data.JSONParser;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

	private String LOG_TAG = "MainActivity";
	
	private ProgressBar pbarProgreso;
	private ProgressDialog pDialog;
	private ImageView image;
	private TextView title;
	private URL url;
	
	private static String LAST_COMIC_URL = "http://xkcd.com/info.0.json";
	private Comic comic;
	private JSONArray comics;
	private JSONObject json;
	private static final String TAG_MONTH = "month";
	private static final String TAG_NUM = "num";
	private static final String TAG_LINK = "link";
	private static final String TAG_YEAR = "year";
	private static final String TAG_NEWS = "news";
	private static final String TAG_SAFE_TITLE = "safe_title";
	private static final String TAG_TRANSCRIPT = "transcript";
	private static final String TAG_ALT = "alt";
	private static final String TAG_IMG = "img";
	private static final String TAG_TITLE = "title";
	private static final String TAG_DAY = "day";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        image = (ImageView) findViewById(R.id.imagen);
        title = (TextView) findViewById(R.id.title);
        pbarProgreso = (ProgressBar)findViewById(R.id.pbarProgreso);
        
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMessage("Procesando...");
        pDialog.setMax(100);
        
        JSONParser jsonParser = new JSONParser(this.getApplicationContext());
        try {
			json = jsonParser.execute(LAST_COMIC_URL).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        try{
        	comic = new Comic(json.getString(TAG_MONTH), json.getString(TAG_NUM),
        			json.getString(TAG_LINK), json.getString(TAG_YEAR), 
        			json.getString(TAG_NEWS), json.getString(TAG_SAFE_TITLE), 
        			json.getString(TAG_TRANSCRIPT), json.getString(TAG_ALT), 
        			json.getString(TAG_IMG), json.getString(TAG_TITLE), 
        			json.getString(TAG_DAY));
        }catch (JSONException e) {
			Log.e(this.LOG_TAG, "Error extracting data " + e.toString());
		}
        
		try {
			url = new URL(comic.getImg());
			Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
	        image.setImageBitmap(bmp);
	        title.setText(comic.getTitle());
		} catch (MalformedURLException e) {
			Log.e(this.LOG_TAG, "Error getting the image " + e.toString());
		} catch (IOException e) {
			Log.e(this.LOG_TAG, "Error getting the image " + e.toString());
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
