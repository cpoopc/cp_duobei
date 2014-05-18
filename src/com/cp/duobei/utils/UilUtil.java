package com.cp.duobei.utils;

import com.cp.duobei.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class UilUtil {
	static public DisplayImageOptions options= new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.ic_duobei)
	.showImageForEmptyUri(R.drawable.ic_empty)
	.showImageOnFail(R.drawable.ic_error)
	.cacheInMemory(true)
//	.cacheOnDisk(true)
	.cacheOnDisc(true)
	.considerExifParams(true)
	.build();
}
