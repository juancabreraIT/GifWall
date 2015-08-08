package com.haya.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.haya.adapter.GridAdapter;
import com.haya.filemanager.FilesManager;
import com.haya.gifwallet.R;
import com.haya.utils.Constants;
import com.haya.utils.Utils;

public class MainActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {

	final static private int RESULT_LOAD_IMAGE = 1;
	final static private FilesManager FILES = new FilesManager();
	private GridView galeria;		
	private ArrayList<File> imagesArray = new ArrayList<File>();
	private GridAdapter adaptador;
	
	private String url;
	
	ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

		
		// instantiate it within the onCreate method
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Downloading image. . .");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);
		
		try {
			loadGallery();
//			mock();
			init();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void loadGallery() {
				
		File imagesFolder = new File(this.getFilesDir(), (String) getResources().getText(R.string.wallet));
		if ( imagesFolder.exists() && imagesFolder.isDirectory() ) {			
			
			imagesArray.clear();
			
			for (File file : imagesFolder.listFiles()) {
				imagesArray.add(file);
			}
		} else {
			imagesFolder.mkdir();
		}
	}
	
	private void mock() throws IOException {
//
//		File imagesFolder = crearDirectorioWallet();		
//		String fileName = generateFileName(imagesFolder);
//		File image = new File(fileName);
//		
//		InputStream input = getAssets().open("gif_99.gif");		
//
//		if ( FILES.copyFile(input, image) < 0) {
//			Log.e("GifWallet", "copyFile FAILED");
//		}
//
//		imagesArray.add(image);
	}
	
	private void init() {

		this.adaptador = new GridAdapter(this, imagesArray);
		
		this.galeria = (GridView) findViewById(R.id.gallery);
		this.galeria.setAdapter(adaptador);
		this.galeria.setOnItemClickListener(this);
		this.galeria.setOnItemLongClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		if (id == R.id.add) {
			addImage();
		} else if (id == R.id.invite) {			
			
			Intent inviteFriend = Utils.shareText(this, getResources().getText(R.string.invite_msg).toString()); 
			startActivity(Intent.createChooser(inviteFriend, getResources().getText(R.string.invite).toString()));
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {			
		super.onActivityResult(requestCode, resultCode, data);
	      
	     if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {	    	 
	         Uri selectedImage = data.getData();
	         String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	         Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	         cursor.moveToFirst();
	 
	         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	         String picturePath = cursor.getString(columnIndex);
	         cursor.close();
	                      
	         // String picturePath contains the path of selected Image
	         
	         String imagePath = saveFromGalleryToApp(picturePath);
	         Utils.setURL(getBaseContext(), imagePath.substring(imagePath.lastIndexOf("/") + 1), "gallery");
	         
	         loadGallery();
		     adaptador.changeData(imagesArray);
	     }
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
		Intent intent = new Intent(getBaseContext(), DisplayActivity.class);
	    intent.putExtra(Constants.GIF, imagesArray.get(position));
	    startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		confirmDelete(position);
		return true;		
	}

	private void addImage() {
		
		CharSequence addMethods[] = new CharSequence[] {
				getResources().getText(R.string.fromURL), 
				getResources().getText(R.string.fromGallery)
				};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getText(R.string.addImage));
		builder.setItems(addMethods, new DialogInterface.OnClickListener() {
		    
			@Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on addMethods[which]				
				if ( which == 0 ) {
					loadFromURL();
				} else if ( which == 1 ) {
					Toast.makeText(getBaseContext(), getResources().getText(R.string.fromGallery), Toast.LENGTH_LONG).show();
					
					Intent i = new Intent(
							Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							 
					startActivityForResult(i, RESULT_LOAD_IMAGE);
				}
		    }
		});
		builder.show();
	}

	private void loadFromURL() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("***Set the URL****");

		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	Toast.makeText(getBaseContext(), "Downloading your image...", Toast.LENGTH_LONG).show();
		    	url = input.getText().toString();

		    	final DownloadTask downloadTask = new DownloadTask(getBaseContext());
		    	downloadTask.execute(url);
		    	
		    	mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
		    	    @Override
		    	    public void onCancel(DialogInterface dialog) {
		    	        downloadTask.cancel(true);
		    	    }
		    	});
		    			    	
		    }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		builder.show();
		
	}	
	
	private void confirmDelete(int position) {

		final int selectedItem = position;

		new AlertDialog.Builder(this)
	    .setMessage(getResources().getText(R.string.delete))
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {

	        	FILES.deleteFile(imagesArray.get(selectedItem));
	        	Utils.removeURL(getBaseContext(), imagesArray.get(selectedItem).getName());
	        	imagesArray.remove(selectedItem);
	        	adaptador.changeData(imagesArray);
	        	
	        	if (imagesArray.isEmpty()) {
	        		setContentView(R.layout.empty);
	        	}
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	// void
	        }
	     })
	     .show();
	}

	class DownloadTask extends AsyncTask<String, Integer, Integer> {

		private Context context;
		private PowerManager.WakeLock mWakeLock;
		
		public DownloadTask(Context context) {
			this.context = context;
		}
    
	    protected Integer doInBackground(String... urls) {
	        
			InputStream is = null;
			FileOutputStream fos = null;
	    	
	    	try {
	        	int totalLeido = 0;

	    		File imagesFolder = Utils.crearDirectorioWallet(context);		
	    		String imagePath = Utils.generateFileName(imagesFolder);
	        	
    			URL url = new URL(urls[0]);
    			HttpURLConnection httpcon;
    			httpcon = (HttpURLConnection) url.openConnection();
    			httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
    			
    			Log.d("GifWallet", "Content Type: " + httpcon.getContentType() );
    			
    			int fileLength = httpcon.getContentLength();
    			
				is = httpcon.getInputStream();
				fos = new FileOutputStream(imagePath);		
				
				byte[] array = new byte[1000]; // buffer temporal
				int leido = is.read(array);
				while (leido > 0) {
					
					if ( isCancelled() ) {
						is.close();
						fos.close();
						return -1;
					}
					
					totalLeido += leido;
					if ( fileLength > 0 ) {
	                   publishProgress((int) (totalLeido * 100 / fileLength));
					}
						    					
					fos.write(array, 0, leido);
					leido = is.read(array);
				}
				
				Utils.setURL(getBaseContext(), imagePath.substring(imagePath.lastIndexOf("/") + 1), url.toString());
    				
			} catch(IOException e) {
				e.printStackTrace();
    		} catch (Exception e) {
    			e.printStackTrace();
    			return -1;	        	
	        } finally {
	        	try {
					if ( is != null ) {						
							is.close();					
					}
	
					if ( fos != null ) {
						fos.close();
					}
	        	} catch (IOException e) {
					e.printStackTrace();
				}
			}
	        return 1;
	    }
	    
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();

	        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
	        mWakeLock.acquire();
	        mProgressDialog.show();
	    }	   
	    
	    protected void onProgressUpdate(Integer... progress) {
	    	super.onProgressUpdate(progress);
	    	
	        mProgressDialog.setIndeterminate(false);
	        mProgressDialog.setMax(100);
	        mProgressDialog.setProgress(progress[0]);	        
	    }

	    @Override
	    protected void onPostExecute(Integer result) {
	    	
	    	mWakeLock.release();
	        mProgressDialog.dismiss();
	    	
	    	loadGallery();
	    	adaptador.changeData(imagesArray);
	    }
	}
	
	private String saveFromGalleryToApp(String picturePath) {
		
		try {
	         File imagesFolder = Utils.crearDirectorioWallet(this);		
	    	 String imagePath = Utils.generateFileName(imagesFolder);
	    	 File file = new File(imagePath);
	         InputStream in = new FileInputStream(picturePath);	    	 
	         
	         FilesManager filesManager = new FilesManager();
	         filesManager.copyFile(in, file);

	         return imagePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "error";
	}
	
}
