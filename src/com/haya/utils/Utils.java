package com.haya.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.haya.gifwallet.R;

public final class Utils {
	
	public static Intent shareText(Context context, String text) {
		
		Intent inviteFriend = new Intent();
		inviteFriend.setAction(Intent.ACTION_SEND);
		inviteFriend.putExtra(Intent.EXTRA_TEXT, text);
		inviteFriend.setType("text/plain");
//		context.startActivity(Intent.createChooser(inviteFriend, title));
		
		return inviteFriend;
	}
	
	public static File crearDirectorioWallet(Context context) {
		// --> /data/data/com.haya.gifwallet/files/wallet
		File imagesFolder = new File(context.getFilesDir(), (String) context.getResources().getText(R.string.wallet));
		Log.d("GifWallet", "Creado directorio: " + imagesFolder.getAbsolutePath());
		
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
		
		SharedPreferences prefs = context.getSharedPreferences("urls", Context.MODE_PRIVATE);
			 
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(fileName, url);
		
		Log.d("GifWallet", "SharedPreferences set: " + fileName + " - " + url);
		
		return editor.commit();
	}
	
	
	public static String getURL(Context context, String fileName) {
		
		SharedPreferences prefs = context.getSharedPreferences("urls", Context.MODE_PRIVATE);			 
		String url = prefs.getString(fileName, "null");		
		
		Log.d("GifWallet", "SharedPreferences get: " + fileName + " - " + url);
		
		return url;
	}
	
	public static boolean removeURL(Context context, String fileName) {
		SharedPreferences prefs = context.getSharedPreferences("urls", Context.MODE_PRIVATE);
		 
		Log.d("GifWallet", "SharedPreferences remove: " + fileName);
		
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(fileName);
		return editor.commit();
	}
}
