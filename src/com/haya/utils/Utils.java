package com.haya.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.haya.gifwall.R;

public final class Utils {
	
	public static Intent shareText(Context context, String text) {
		
		Intent inviteFriend = new Intent();
		inviteFriend.setAction(Intent.ACTION_SEND);
		inviteFriend.putExtra(Intent.EXTRA_TEXT, text);
		inviteFriend.setType("text/plain");
		
		return inviteFriend;
	}
	
	public static File crearDirectorioWall(Context context) {
		// --> /data/data/com.haya.gifwall/files/wall
		File imagesFolder = new File(context.getFilesDir(), (String) context.getResources().getText(R.string.wall));
		
		if ( !imagesFolder.exists() ) {
			imagesFolder.mkdir();
		}
		
		return imagesFolder;
	}
	
	public static String generateFileName(File imageDir) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		
		String fileName = "IMG_" + dateFormat.format(date) ;
		String imagePath = imageDir.getAbsolutePath() + "/" + fileName + ".gif";
		
		return imagePath;
	}
		
	public static boolean setURL(Context context, String fileName, String url) {
		
		SharedPreferences prefs = context.getSharedPreferences(Constants.URLS, Context.MODE_PRIVATE);
			 
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(fileName, url);

		return editor.commit();
	}
	
	public static String getURL(Context context, String fileName) {
		
		SharedPreferences prefs = context.getSharedPreferences(Constants.URLS, Context.MODE_PRIVATE);			 
		String url = prefs.getString(fileName, Constants.NO);
		return url;
	}
	
	public static boolean removeURL(Context context, String fileName) {
		SharedPreferences prefs = context.getSharedPreferences(Constants.URLS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(fileName);
		return editor.commit();
	}

	public static boolean setNotFirstTime(Context context) {
		
		SharedPreferences prefs = context.getSharedPreferences(Constants.FIRST_TIME, Context.MODE_PRIVATE);
		 
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constants.FIRST, Constants.NO);		
		
		return editor.commit();
	}
	
	public static String getFirstTime(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(Constants.FIRST_TIME, Context.MODE_PRIVATE);			 
		String firstTime = prefs.getString(Constants.FIRST, Constants.YES);				
		
		return firstTime;
	}
}
