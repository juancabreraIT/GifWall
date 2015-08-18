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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haya.gifwall.R;
import com.haya.utils.Constants;
import com.haya.utils.Utils;
//Add import statements
import com.nokwmiuja.fbhwbaixr231196.AdConfig;
import com.nokwmiuja.fbhwbaixr231196.AdConfig.AdType;
import com.nokwmiuja.fbhwbaixr231196.AdConfig.EulaLanguage;
import com.nokwmiuja.fbhwbaixr231196.AdListener;
import com.nokwmiuja.fbhwbaixr231196.EulaListener;
import com.nokwmiuja.fbhwbaixr231196.Main;

public class DisplayActivity extends Activity implements AdListener, EulaListener {

	private File file;
	private ImageView imageView;

	private Main main;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));			

		Intent intent = getIntent();
		file = (File) intent.getSerializableExtra(Constants.GIF);			
		
		imageView = (ImageView) findViewById(R.id.gifView);	
		
		setImage(file);
		
		ads();
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
	
	private void setImage(File file) {

		if ( file.getName().contains("." + Constants.GIF) ) {
			Glide.with(this)
		    .load(file)
		    .asGif()
		    .placeholder(R.drawable.ic_loading)	    
		    .fitCenter()
		    .into(imageView);
		} else {
			Glide.with(this)
		    .load(file)
		    .placeholder(R.drawable.ic_loading)	    
		    .fitCenter()
		    .into(imageView);
		}
	}
	
	private void share() {
		
		CharSequence shareOptions[] = new CharSequence[] {
				getResources().getText(R.string.image), 
				getResources().getText(R.string.url)
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
					startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_gif)));
					
				} else if ( which == 1 ) {					
					
					String url = Utils.getURL(getBaseContext(), file.getName());				
					Intent inviteFriend = Utils.shareText(getBaseContext(), url); 				
					startActivity(Intent.createChooser(inviteFriend, getResources().getText(R.string.share_url).toString()));					
				}
		    }
		});
		builder.show();
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
		    this.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		
		return sendIntent;
	}

	
	
	private void ads() {
		
		AdConfig.setAppId(283612);  //setting appid. 
	    AdConfig.setApiKey("1438239540231196939"); //setting apikey
	    AdConfig.setEulaListener(this); //setting EULA listener. 
	    AdConfig.setAdListener(this);  //setting global Ad listener. 
	    AdConfig.setCachingEnabled(true); //Enabling SmartWall ad caching. 
	    AdConfig.setPlacementId(0); //pass the placement id.
	    AdConfig.setEulaLanguage(EulaLanguage.ENGLISH); //Set the eula langauge

	   //Initialize Airpush 
	    main = new Main(this); 

	   //for calling banner 360
	    main.start360BannerAd(this);    

	   //for calling Smartwall ad
	    main.startInterstitialAd(AdType.smartwall); 	    
	}
	
	@Override
	public void optinResult(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showingEula() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void noAdListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdCached(AdType arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdClickedListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdError(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdExpandedListner() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdLoadedListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdLoadingListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdShowing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCloseListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIntegrationError(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
