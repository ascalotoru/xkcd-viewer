package com.xkcdviewer.views;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.ascalotoru.xkcd.R;
import com.xkcdviewer.data.Comic;
import com.xkcdviewer.data.Image;
import com.xkcdviewer.data.JSONParser;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnGestureListener{

	private String LOG_TAG = "MainActivity";
	
	private ProgressBar pbarProgreso;
	public static ProgressDialog pDialog;
	private ImageView image;
	private TextView title;
	private URL url;
	private GestureDetector gDetector;
	
	private static String LAST_COMIC_URL = "http://xkcd.com/info.0.json";
	private JSONParser jsonParser;
	private int currentComic;
	private int lastComic;
	private Comic comic;
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
        gDetector = new GestureDetector(this.getApplicationContext(), this);
        
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Procesando...");
        pDialog.setMax(100);
        
        jsonParser = new JSONParser(this.getApplicationContext());
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
        	extractJSONData(json);
        	lastComic = Integer.parseInt(comic.getNum());
        }catch (JSONException e) {
			Log.e(this.LOG_TAG, "Error extracting data " + e.toString());
		}
        
		try {
			printTheData();
		} catch (MalformedURLException e) {
			Log.e(this.LOG_TAG, "Error getting the image " + e.toString());
		} catch (IOException e) {
			Log.e(this.LOG_TAG, "Error getting the image " + e.toString());
		} catch (NumberFormatException e) {
			Log.e(this.LOG_TAG, "Error getting the number of the current comic "
					+ e.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void showComic(int comicNumber){
    	jsonParser = new JSONParser(this.getApplicationContext());
    	try {
			json = jsonParser.execute("http://xkcd.com/"+comicNumber+
					"/info.0.json").get();
			extractJSONData(json);
			printTheData();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void extractJSONData(JSONObject json) throws JSONException{
    	comic = new Comic(json.getString(TAG_MONTH), json.getString(TAG_NUM),
    			json.getString(TAG_LINK), json.getString(TAG_YEAR), 
    			json.getString(TAG_NEWS), json.getString(TAG_SAFE_TITLE), 
    			json.getString(TAG_TRANSCRIPT), json.getString(TAG_ALT), 
    			json.getString(TAG_IMG), json.getString(TAG_TITLE), 
    			json.getString(TAG_DAY));
    }
    
    private void printTheData() throws MalformedURLException, IOException,
    						NumberFormatException, InterruptedException, 
    						ExecutionException{
    	// Get the current number of the comic
    	currentComic = Integer.parseInt(comic.getNum());
    	Image img = new Image();
    	image.setImageBitmap(img.execute(comic).get());
    	title.setText(comic.getTitle());
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX,
			float velocityY) {
		if(start.getRawX() < finish.getRawX()){
			// Previous
			Log.d(this.LOG_TAG, "A la derecha el dedo.");
			showComic(currentComic-1);
		}else{
			// Next
			Log.d(this.LOG_TAG, "A la izquierda el dedo.");
			if(currentComic>=lastComic){
				Toast.makeText(this.getApplicationContext(), "Is the last comic."
						, Toast.LENGTH_SHORT).show();
			}else{	
				showComic(currentComic+1);
			}
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
