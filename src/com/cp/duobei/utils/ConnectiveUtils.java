package com.cp.duobei.utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ConnectiveUtils {
	public static boolean isMobile(Context context) {
		if (!isConnected(context)) {
			return false;
		}
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		int type = info.getType();
		return ConnectivityManager.TYPE_MOBILE == type;
	}

	public static boolean isWIFI(Context context) {
		if (!isConnected(context)) {
			return false;
		}
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		int type = info.getType();
		return ConnectivityManager.TYPE_WIFI == type;
	}

	/**
	 * 判断手机是否可以联网
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}
		boolean available = networkInfo.isAvailable();
		return available;
	}
}
