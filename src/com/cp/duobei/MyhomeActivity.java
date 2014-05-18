package com.cp.duobei;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cp.duobei.activity.MainActivity;
import com.cp.duobei.fragment.home.DailyRecFragment;
import com.cp.duobei.fragment.home.NewCourseFragment;
import com.cp.duobei.fragment.login.MyCourseFragment;
import com.cp.duobei.fragment.login.MyInfoFragment;
import com.cp.duobei.utils.SwipeBackSherlockFragmentActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;

public class MyhomeActivity extends SwipeBackSherlockFragmentActivity implements OnTabChangeListener, OnPageChangeListener{

	private ViewPager mViewPager;
	ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
	ArrayList<String> tabnameList = new ArrayList<String>();
	private TabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myhome);
		addpager("我的课表",new MyCourseFragment());
		addpager("我的资料",new MyInfoFragment());
		ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setTitle("我的课表");
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		initViewpager();
		initTabhost();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_logout:
			SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putBoolean("autologin", false);
			edit.commit();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initViewpager() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager_myhome_activity);
		FragmentManager fm = getSupportFragmentManager();
		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(fm);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(this);
	}
	private void initTabhost() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		for (int i = 0; i < tabnameList.size(); i++) {
			mTabHost.addTab(mTabHost.newTabSpec(tabnameList.get(i)).setIndicator(tabnameList.get(i)).setContent(new DumyTabcontent()));
		}
		mTabHost.setOnTabChangedListener(this);
	}
	public void addpager(String tabname,Fragment fragment) {
		tabnameList.add(tabname);
		fragmentList.add(fragment);
	}
	class DumyTabcontent implements TabContentFactory{

		@Override
		public View createTabContent(String tag) {
			TextView textView = new TextView(MyhomeActivity.this);
			return textView;
		}}
	class MyFragmentPagerAdapter extends FragmentPagerAdapter{

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.myhome, menu);
		return true;
	}
	@Override
	public void onTabChanged(String tabId) {
		mViewPager.setCurrentItem(mTabHost.getCurrentTab());
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageSelected(int arg0) {
		mTabHost.setCurrentTab(arg0);
	}

}
