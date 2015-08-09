package com.haya.adapter;

import java.io.File;
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

import com.squareup.picasso.Picasso;

	public class GridAdapter extends BaseAdapter {

	private Context context;
	private List<File> gifList;

    public GridAdapter(Context c) {
        context = c;
        gifList = new ArrayList<File>();
    }    
    
    public GridAdapter(Context c, ArrayList<File> pGifList) {
    	context = c;
    	gifList = pGifList;
//    	Picasso.with(c).setIndicatorsEnabled(true);
    }
    
    public void changeData(ArrayList<File> data) {
    	gifList = data;
        notifyDataSetChanged();               
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

    	// ****** GLIDE *******
//    	Glide.with(context)
//    	   .load(gifList.get(position))
//    	   .placeholder(R.drawable.ic_cheese)	    
//    	   .fitCenter()
//    	   .into(imageView);

    	// ******* PICASSO *******    	
        Picasso.with(context)
        .load(gifList.get(position))
        .into(imageView);

        return imageView;
    }

    private int getDimensions() {

    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    	Display display = wm.getDefaultDisplay();
    	Point size = new Point();
    	display.getSize(size);
    	int width = size.x;

    	return (int) ((width / 3) * 0.99);
    }
}

