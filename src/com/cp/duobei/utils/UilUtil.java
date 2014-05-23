package com.cp.duobei.utils;

import java.io.File;

import android.util.Log;
import android.widget.ImageView;

import com.cp.duobei.R;
import com.cp.duobei.activity.MyApplication;
import com.cp.duobei.dao.Constant;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class UilUtil {
	static public DisplayImageOptions options= new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.ic_duobei)
	.showImageForEmptyUri(R.drawable.ic_empty)
	.showImageOnFail(R.drawable.ic_error)
	.cacheInMemory(true)
//	.cacheOnDisk(true)
	.cacheOnDisk(true)
	.considerExifParams(true)
	.build();
	static private Md5FileNameGenerator generator;
//	private static void init(){
//		if(MyApplication.isWifiMode){
//			generator = new Md5FileNameGenerator();
//		}
//	}
	private static ImageLoader imageLoader = ImageLoader.getInstance();
	static public void loadimg(String uri, ImageView imageView, DisplayImageOptions option,
			ImageLoadingListener listener) {
		if(MyApplication.isWifiMode){
			//TODO 如果开了wifi,也下载图片
			if(generator==null){
				generator = new Md5FileNameGenerator();
			}
			//如果已经缓存好了,显示出来
//			Log.e("Constant.STORE_PATH_UIL+generator.generate(uri)", Constant.STORE_PATH_UIL+generator.generate(uri));
			if(new File(Constant.STORE_PATH_UIL+"/"+generator.generate(uri)).exists()){
				imageLoader .displayImage(uri, imageView, options, listener);
//				Log.e("uri", MyApplication.isWifiMode+uri);
			}
		}else{
//			Log.e("uri", MyApplication.isWifiMode+uri);
			imageLoader .displayImage(uri, imageView, options, listener);
		}
	}
}
