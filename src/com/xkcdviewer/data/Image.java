package com.xkcdviewer.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class Image extends AsyncTask<Comic, Void, Bitmap>{

	public Image(){
		
	}
	
	@Override
	protected Bitmap doInBackground(Comic... comics) {
		Bitmap bmp = null;
		try {
			URL url = new URL(comics[0].getImg());
			bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bmp;
	}

	
}
