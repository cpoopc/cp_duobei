package com.cp.duobei.fragment.home;

import java.util.ArrayList;

import com.cp.duobei.R;
import com.cp.duobei.activity.MainActivity;
import com.cp.duobei.fragment.AbstractFragment;
import com.example.ex.LogUtils;

import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
/**
 * 	viewpager+tabhost
 *
 */
public class HomeFragment extends Fragment implements OnTabChangeListener, OnPageChangeListener {
	private ArrayList<View> viewList = new ArrayList<View>();
	private ArrayList<String> tabnameList = new ArrayList<String>();
	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private MyFragmentAdapter mAdapter;
	private MainActivity mainActivity;
	//提供给mainactivity
	public void addpager(String tabname,Fragment fragment) {
		tabnameList.add(tabname);
		fragmentList.add(fragment);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_home, null);
		initMenudrawerState();
		initViewPager(inflate);
		initTabHost(inflate);
		return inflate;
	}
	private void initMenudrawerState() {
		FragmentActivity activity;
		activity = getActivity();
		if(activity instanceof MainActivity){
			mainActivity = (MainActivity) activity;
			mainActivity.setMenuDrawerEnable(true);
		}
	}

	private void initViewPager(View inflate) {
		mViewPager = (ViewPager) inflate.findViewById(R.id.home_pager);
		FragmentManager fm = getChildFragmentManager();
		mAdapter = new MyFragmentAdapter(fm);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOffscreenPageLimit(2);
	}
	private void initTabHost(View inflate) {
		mTabHost = (TabHost) inflate.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabHost.setOnTabChangedListener(this);
//		viewList.add(object)
	 if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
		 for(int i = 0;i<tabnameList.size();i++){
			 mTabHost.addTab(mTabHost.newTabSpec(tabnameList.get(i)).setIndicator(tabnameList.get(i)).setContent(new DommyContentFty()));
		 }
	 }else{
		 for(int i = 0;i<tabnameList.size();i++){
			 View inflate2 = getActivity().getLayoutInflater().inflate(R.layout.testlayout, null);
			 TextView textView = (TextView)inflate2.findViewById(R.id.textView1);
			 if(i==0){
				 ImageView imageView =(ImageView) inflate2.findViewById(R.id.imageView1);
				 imageView.setVisibility(View.INVISIBLE);
			 } 
			 textView.setText(tabnameList.get(i));
			 mTabHost.addTab(mTabHost.newTabSpec(tabnameList.get(i)).setIndicator(inflate2).setContent(new DommyContentFty()));
		 }
	 }
	}
	//
	class DommyContentFty implements TabContentFactory{

		@Override
		public View createTabContent(String tag) {
			TextView textView = new TextView(getActivity());
			return textView;
		}}
	class MyFragmentAdapter extends FragmentPagerAdapter{

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = fragmentList.get(arg0);
			return fragment;
		}

		@Override
		public int getCount() {
			return tabnameList.size();
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageSelected(int arg0) {
		mTabHost.setCurrentTab(arg0);
		LogUtils.e("onPageSelected", ""+arg0);
				if(arg0==0){
					mainActivity.setMenuDrawerEnable(true);
				}else{
					mainActivity.setMenuDrawerEnable(false);
				}
	}
	@Override
	public void onTabChanged(String tabId) {
		int currentTab = mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(currentTab);
	}
	public void refresh() {
		int currentTab = mTabHost.getCurrentTab();
		AbstractFragment fragment = (AbstractFragment) fragmentList.get(currentTab);
		//TODO 改成兼容所有fragment界面的刷新
		fragment.refresh();
	}
}
