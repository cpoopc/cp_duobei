package com.cp.duobei.activity;

import java.io.File;

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
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class SettingActivity extends SwipeBackSherlockPreferenceActivity{
	
	FeedbackAgent agent;
	
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
		ActionBar supportActionBar = getSupportActionBar();
//		supportActionBar.setTitle("dddddddd");
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.setting);
		getPreferenceScreen().setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {

				ToastUtils.showToast(SettingActivity.this, "change");
				return false;
			}
		});
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
		if("key_clear".equals(preference.getKey())){
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

	File STORE_PATH;
	private void showClearDialog() {
		new AlertDialog.Builder(SettingActivity.this)
				.setTitle("即将清空缓存")
				.setMessage("是否清空缓存?")
				.setNegativeButton("取消", null)
				.setPositiveButton("清空", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ToastUtils.showToast(SettingActivity.this, "开始清理缓存");
						//sd卡地址
						File sdPath = Environment.getExternalStorageDirectory();
						//数据保存文件夹
						STORE_PATH = new File(sdPath+"/images/");
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								deleteFile(STORE_PATH);
							}
						});
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
