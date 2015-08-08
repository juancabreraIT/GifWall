package com.haya.activities;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haya.gifwall.R;
import com.haya.utils.Constants;
import com.haya.utils.Utils;

public class DisplayActivity extends Activity {

	private File file;
	private ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));			

		Intent intent = getIntent();
		file = (File) intent.getSerializableExtra(Constants.GIF);			
		
		imageView = (ImageView) findViewById(R.id.gifView);	
		
		setImage(file);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.display, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		if (id == R.id.invite) {			
			Intent inviteFriend = Utils.shareText(this, getResources().getText(R.string.invite_msg).toString()); 
			startActivity(Intent.createChooser(inviteFriend, getResources().getText(R.string.invite).toString()));
		} 
		else if (id == R.id.share) {
			share();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void share() {
		
		CharSequence shareOptions[] = new CharSequence[] {
				"Image",
				"URL"
//				getResources().getText(R.string.fromURL), 
//				getResources().getText(R.string.fromGallery)
				};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getText(R.string.share));
		builder.setItems(shareOptions, new DialogInterface.OnClickListener() {
		    
			@Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on addMethods[which]				
				if ( which == 0 ) {
					Uri contentUri = getUri();
					Intent sendIntent = getIntent(contentUri);
					startActivity(Intent.createChooser(sendIntent, "Share gif"));
					
				} else if ( which == 1 ) {					
					
					String url = Utils.getURL(getBaseContext(), file.getName());
					
					Intent inviteFriend = Utils.shareText(getBaseContext(), url); 				
					startActivity(Intent.createChooser(inviteFriend, getResources().getText(R.string.share_url).toString()));
					
				}
		    }
		});
		builder.show();
	}
	
	private void setImage(File file) {
		
		if ( file.getName().contains("." + Constants.GIF) ) {
			Glide.with(this)
		    .load(file)
		    .asGif()
		    .placeholder(R.drawable.ic_cheese)	    
		    .fitCenter()
		    .into(imageView);
		} else {
			Glide.with(this)
		    .load(file)
		    .placeholder(R.drawable.ic_cheese)	    
		    .fitCenter()
		    .into(imageView);
		}

	}
	
	private Uri getUri() {		
		Uri contentUri = FileProvider.getUriForFile(this, Constants.PACKAGE_FILE_PROVIDER, file);		
		return contentUri;
	}

	private Intent getIntent(Uri contentUri) {
		
		Intent sendIntent = new Intent();		
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.setType("image/gif");
		sendIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

		List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(sendIntent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo resolveInfo : resInfoList) {
		    String packageName = resolveInfo.activityInfo.packageName;
		    Log.d("GifWallet", packageName);
		    this.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		
		return sendIntent;
	}

	
}
