package com.cp.duobei.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

import pl.droidsonroids.gif.GifDrawable;

import com.cp.duobei.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.widget.TextView;

public class GifHtmlLoader extends AsyncTask<String, Void, Spanned>{
	private TextView mtextView;
	Context context;
	
	public GifHtmlLoader(Context context) {
		super();
		this.context = context;
	}
	public void setTextview(TextView tv){
		mtextView = tv;
	}
	ImageGetter imageGetter = new Html.ImageGetter() {

        @Override

        public Drawable getDrawable(String source) {
     	   Drawable drawable = null;
            URL url;  
            Log.e("帖子详情source", source+".");
            try {
            	if(source.startsWith("ima")){
            		source = "http://cpduobei.qiniudn.com/ic_heihei.png";
//         			  source = "http://bbs.51cto.com/"+source;
            	}
            	else if(source.endsWith(".gif")){
         		   url = new URL(source);
         		   //可以弄个默认图片
         		  InputStream openStream = url.openStream();
         		  BufferedInputStream bufferedInputStream = new BufferedInputStream(openStream);
				  GifDrawable gifDrawable = new GifDrawable(bufferedInputStream);
         		   drawable = context.getResources().getDrawable(R.drawable.back);
//         		   drawable = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_duobei);
         		   drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
         				   .getIntrinsicHeight());
         		   return drawable;
//         		   return gifDrawable;
         	   }
//         	   url = new URL("http://img7.9158.com/200709/01/11/53/200709018758949.jpg");
         	   url = new URL(source);
         	   Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                drawable = new BitmapDrawable(bitmap);
            } catch (Exception e) {  
         	   e.printStackTrace();
         	   return null;  
            }  
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                          .getIntrinsicHeight());
              return drawable;
        }
};
	@Override
	protected Spanned doInBackground(String... params) {
		Spanned fromHtml = Html.fromHtml(params[0], imageGetter, null);
		return fromHtml;
	}
	@Override
	protected void onPostExecute(Spanned result) {
		if(result!=null){
			mtextView.setText(result);
		}
		super.onPostExecute(result);
	}
}
