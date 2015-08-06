package com.haya.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.haya.gifwallet.R;
import com.squareup.picasso.Picasso;

	public class GridAdapter extends BaseAdapter {

	private Context context;
	private List<String> gifList;

    public GridAdapter(Context c) {
        context = c;
        gifList = new ArrayList<String>();
    }    
    
    public GridAdapter(Context c, ArrayList<String> pGifList) {
    	context = c;
    	gifList = pGifList;
    	Picasso.with(c).setIndicatorsEnabled(true);
    }
    
    public int getCount() {
    	return gifList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        
    	ImageView imageView;
    	int side = getDimensions();     
    	
    	if (convertView == null) {
    		imageView = new ImageView(context);    		
    		imageView.setLayoutParams(new GridView.LayoutParams(side, side));
    		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    		imageView.setPadding(5, 5, 5, 5);
        } else {
        	imageView = (ImageView) convertView;
        }        

    	imageView.setImageResource(R.drawable.canaca);
    	
//		Picasso.with(context)
//		.load(context.getResources().getIdentifier("drawable/ic_launcher", null, context.getPackageName()))
//		.into(imageView);
    	
    	
//    	if ( thumbnailsPath.isEmpty() ) {
//    		Picasso.with(context)
//            .load(context.getResources().getIdentifier("drawable/ic_launcher", null, context.getPackageName()))
//            .placeholder(context.getResources().getIdentifier("drawable/loading", null, context.getPackageName()))
//            .into(imageView);
//    	} else {
//            Picasso.with(context)
//            .load(thumbnailsPath.get(position))
//            .placeholder(context.getResources().getIdentifier("drawable/loading", null, context.getPackageName()))
//            .into(imageView);
//    	}

        return imageView;
    }

    private int getDimensions() {

    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    	Display display = wm.getDefaultDisplay();
    	Point size = new Point();
    	display.getSize(size);
    	int width = size.x;

    	return (int) ((width / 3) * 0.8);
    }
}

