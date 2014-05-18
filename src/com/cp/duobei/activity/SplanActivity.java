package com.cp.duobei.activity;



import com.cp.duobei.R;
import com.cp.duobei.utils.FlakeView;
import com.example.ex.LogUtils;
import com.example.ex.UpgradeManager;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

public class SplanActivity extends Activity {
	Handler mHandler = new Handler();
	int DURATION = 1000;
	//判断启动引导界面还是主界面
	private void judgeStart() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		boolean isFirst = preferences.getBoolean("isFirst3", true);
		if(isFirst){
			startActivity(new Intent(SplanActivity.this,
											UserGuidActivity.class));
			Editor edit = preferences.edit();
			edit.putBoolean("isFirst3", false);
			edit.commit();
		}else{
			startActivity(new Intent(SplanActivity.this,
												MainActivity.class));
		}
		finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splan);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(DURATION);
		findViewById(R.id.imageView1).startAnimation(alphaAnimation);
		final FlakeView flakeView = new FlakeView(this);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.container);
		linearLayout.addView(flakeView);
//    flakeView.setBackgroundColor(getResources().getColor(R.color.white));
		flakeView.addFlakes(flakeView.getNumFlakes());
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				judgeStart();
			}
		}, DURATION);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
