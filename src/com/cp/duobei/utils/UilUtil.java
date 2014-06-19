package com.cp.duobei.utils;

import java.io.File;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.cp.duobei.R;
import com.cp.duobei.activity.MyApplication;
import com.cp.duobei.dao.Constant;
import com.example.ex.LogUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class UilUtil {
	static public DisplayImageOptions options= new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.ic_duobei)
	.showImageForEmptyUri(R.drawable.ic_empty)
	.showImageOnFail(R.drawable.ic_error)
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.displayer(new RoundedBitmapDisplayer(20))
	.considerExifParams(true)
	.build();
	static private Md5FileNameGenerator generator;
	private static ImageLoader imageLoader = ImageLoader.getInstance();
	static public void loadimg(Context context,String uri, ImageView imageView, DisplayImageOptions option,
			ImageLoadingListener listener) {
		if(MyApplication.isWifiMode){
			//没开WIFI,判断是否有缓存
			if(!ConnectiveUtils.isWIFI(context)){
				if(generator==null){
					generator = new Md5FileNameGenerator();
				}
				//没有缓存
				if(!new File(Constant.STORE_PATH_UIL+"/"+generator.generate(uri)).exists()){
//					LogUtils.e("WIFI模式","没有缓存"); 
					imageView.setImageResource(R.drawable.ic_duobei);
					return;
				}
//				LogUtils.e("WIFI模式","有缓存");
			}
//			LogUtils.e("WIFI模式","开了wifi");
		}
			imageLoader .displayImage(uri, imageView, options, listener);
		
	}
}
