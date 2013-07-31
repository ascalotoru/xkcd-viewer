package com.xkcdviewer.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.xkcdviewer.views.MainActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class JSONParser extends AsyncTask<String, Integer, JSONObject>{

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	
	private Context mContext;
	
	public JSONParser(Context context){
		mContext = context;
	}
	
	public JSONObject getJSONFromUrl(String url){
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			
		} catch (ClientProtocolException e) {
			Log.e("JSON Parser", "Protocol error " + e.toString());
		} catch (IOException e) {
			Log.e("JSON Parser", "IO Error " + e.toString());
		} catch (IllegalArgumentException e) {
			Log.e("JSON Parser", "Argument Error " + e.toString());
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,
					"iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Try to parse the String to JSON
		try{
			jObj = new JSONObject(json);
		}catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return jObj;
	}

	@Override
	protected JSONObject doInBackground(String... url) {
		return this.getJSONFromUrl(url[0]);
	}

	@Override
	protected void onPreExecute() {
		MainActivity.pDialog.show();
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		MainActivity.pDialog.dismiss();		
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		int progreso = values[0].intValue();
		MainActivity.pDialog.setProgress(progreso);
	}
}