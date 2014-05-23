package com.cp.duobei.activity;

import java.io.File;
import java.text.DecimalFormat;

import me.imid.swipebacklayout.lib.app.SwipeBackPreferenceActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.cp.duobei.R;
import com.cp.duobei.dao.Constant;
import com.cp.duobei.utils.SwipeBackSherlockPreferenceActivity;
import com.example.ex.ToastUtils;
import com.example.ex.UpgradeManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

public class SettingActivity extends SwipeBackSherlockPreferenceActivity{
	
	FeedbackAgent agent;
	float total = 0;
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("RecentlyFragment");
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("RecentlyFragment");
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		setTheme(SampleList.THEME);
		super.onCreate(savedInstanceState);
		agent = new FeedbackAgent(this);
		// to sync the default conversation. If there is new reply, there
		// will be notification in the status bar. If you do not want
		// notification, you can use
		// agent.getDefaultConversation().sync(listener);
		// instead.

		agent.sync();
		
		calcCache(Constant.STORE_PATH);
		total /=1024;
		String cachesize;
		DecimalFormat fnum = new DecimalFormat("##0.00"); 
//		String dd=fnum.format(scale); 
		if(total>1024){
			total /=1024;
			cachesize = fnum.format(total);
			cachesize += "MB";
		}else{
			cachesize = fnum.format(total);
			cachesize += "KB";
		}
		ActionBar supportActionBar = getSupportActionBar();
//		supportActionBar.setTitle("dddddddd");
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.setting);
		findPreference("key_clear").setSummary("缓存大小:"+cachesize);
		
//		getPreferenceScreen().setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//
//				ToastUtils.showToast(SettingActivity.this, "change");
//				return false;
//			}
//		});
//		getPreferenceScreen().getSharedPreferences()
//		.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
//			
//			@Override
//			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
//					String key) {
//				ToastUtils.showToast(SettingActivity.this, "change"+key);
//				if("key_wifi".equals(key)){
//					boolean boolean1 = sharedPreferences.getBoolean(key, false);
//					Log.e("wifi on off", ""+boolean1);
//				}
//			}
//		});
	}
	
	private float calcCache(File filepath) {
		File[] listFiles = filepath.listFiles();
		for(File file:listFiles){
			if(file.isFile()){
				total +=file.length();
			}else{
				calcCache(file);
			}
		}
		return total;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	@Deprecated
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if("key_wifi".equals(preference.getKey())){
			//状态改变
			MyApplication.isWifiMode = getPreferenceScreen().getPreferenceManager().getDefaultSharedPreferences(this).getBoolean("key_wifi", false);
//			Log.e("wifi on off", ""+MyApplication.isWifiMode);
		}else if("key_clear".equals(preference.getKey())){
			showClearDialog();
		}
		else if("key_update".equals(preference.getKey())){
			ToastUtils.showToast(SettingActivity.this, "检查更新");
			new UpgradeManager(this,Constant.PATH_UPDATE,Constant.STORE_PATH+"/update.apk").checkandupdate();
		}else if("key_req".equals(preference.getKey())){
			ToastUtils.showToast(SettingActivity.this, "意见反馈");
			agent.startFeedbackActivity();
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

//	File STORE_PATH;
	private void showClearDialog() {
		new AlertDialog.Builder(SettingActivity.this)
				.setTitle("即将清空缓存")
				.setMessage("是否清空缓存?")
				.setNegativeButton("取消", null)
				.setPositiveButton("清空", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ToastUtils.showToast(SettingActivity.this, "开始清理缓存");
//						//sd卡地址
//						File sdPath = Environment.getExternalStorageDirectory();
//						//数据保存文件夹
//						STORE_PATH = new File(sdPath+"/images/");
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								deleteFile(Constant.STORE_PATH);
							}
						}).start();
						SettingActivity.this.findPreference("key_clear").setSummary("缓存大小:0.00KB");
					}
				})
				.create().show();
	}
	private void deleteFile(File path) {
		File[] listFiles = path.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			if(listFiles[i].isFile()){
				listFiles[i].delete();
			}else{
				deleteFile(listFiles[i]);
			}
		}
	}
}
