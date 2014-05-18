package com.cp.duobei.activity;

import java.util.ArrayList;

import com.cp.duobei.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UserGuidActivity extends Activity {
	private int[] mDrawRes = new int[]{
			R.drawable.teacher_0,
			R.drawable.teacher_1,
			R.drawable.teacher_2,
			R.drawable.teacher_3
	};
	private ArrayList<View> mViewList = new ArrayList<View>();
	private View view;
//	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_userguid);
		for(int i=0;i<mDrawRes.length;i++){
			view = getLayoutInflater().inflate(R.layout.pager, null);
			view.setBackgroundResource(mDrawRes[i]);
//			view.setBackground(getResources().getDrawable(mDrawRes[i]));
			if(i==mDrawRes.length-1){
				Button button = (Button) view.findViewById(R.id.button1);
				button.setVisibility(View.VISIBLE);
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startActivity(new Intent(UserGuidActivity.this,MainActivity.class));
						finish();
					}
				});
			}
			mViewList.add(view);
		}
		final ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager1);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(new PagerAdapter() {
			@Override
			public Object instantiateItem(View container, int position) {
				Log.e("adapter", "instantiateItem:"+position);
				View layout = mViewList.get(position);
				mViewPager.addView(layout);
				return layout;
			}
			@Override
			public void destroyItem(View container, int position, Object object) {
				Log.e("adapter", "destroyItem:position:"+position);
				View layout = mViewList.get(position);
				mViewPager.removeView(layout);
			}
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return mDrawRes.length;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
