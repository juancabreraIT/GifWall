package com.haya.activities;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

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
import com.squareup.picasso.Picasso;

public class DisplayActivity extends Activity implements AdListener, EulaListener {

	private File file;
	private ImageView imageView;
	private WebView webView;
	private Main main;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);

		initUI();

		Intent intent = getIntent();
		file = (File) intent.getSerializableExtra(Constants.GIF);			

		imageView = (ImageView) findViewById(R.id.gifView);
		webView = (WebView) findViewById(R.id.webView);
				
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
	
	private void initUI() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	private void setImage(File file) {

		if ( file.getName().contains("." + Constants.GIF) ) {

			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);

			String scale = (size.x < size.y ? "width: 95%; height: auto" : "height: 70%; width: auto");			
			String data = "<html>"
						+	 "<body style=\"background:#0F0F0F\">"
						+ 		"<img src=\""+ "file:///" + file.getAbsolutePath() + "\" style=\"position: absolute; margin: auto; top: 0; left: 0; bottom: 0; right: 0; display: block;" + scale + "\" />"
						+	 "</body>"
						+ "</html>";

			webView.loadDataWithBaseURL("file:///" + file.getAbsolutePath(), data, "text/html","UTF-8" , null);

		} else {
			webView.setVisibility(View.GONE);
			imageView.setVisibility(View.VISIBLE);
			
			Picasso.with(this)
		    .load(file)
		    .placeholder(R.drawable.ic_loading)
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
	public void optinResult(boolean arg0) {	}

	@Override
	public void showingEula() {	}

	@Override
	public void noAdListener() {	}

	@Override
	public void onAdCached(AdType arg0) {	}

	@Override
	public void onAdClickedListener() {	}

	@Override
	public void onAdClosed() {	}

	@Override
	public void onAdError(String arg0) { 	}

	@Override
	public void onAdExpandedListner() {	}

	@Override
	public void onAdLoadedListener() {	}

	@Override
	public void onAdLoadingListener() {	}

	@Override
	public void onAdShowing() {	}

	@Override
	public void onCloseListener() {	}

	@Override
	public void onIntegrationError(String arg0) {	}
	
}
