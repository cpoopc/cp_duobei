package com.cp.duobei.activity;

import com.cp.duobei.dao.Constant;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		if(!Constant.STORE_PATH.exists()){
			Constant.STORE_PATH.mkdirs();
		}
	}
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
//				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
