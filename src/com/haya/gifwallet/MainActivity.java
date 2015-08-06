package com.haya.gifwallet;

import java.util.ArrayList;

import com.haya.adapter.GridAdapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {

	private GridView galeria;
	
	private ArrayList<String> mock = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		
		init();
	}

	private void init() {
		
		for(int i = 0; i < 11; i++) {
			mock.add("string");
		}
		
		this.galeria = (GridView) findViewById(R.id.gallery);
		this.galeria.setAdapter(new GridAdapter(this, mock));
		this.galeria.setOnItemClickListener(this);
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
			Toast.makeText(this, "Add GIF", Toast.LENGTH_LONG).show();
			return true;
		} else if (id == R.id.invite) {
			Toast.makeText(this, "Invite friends", Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		Toast.makeText(this, "Wanna see a Gif? Still working on it!", Toast.LENGTH_SHORT).show();
		
	}
}
