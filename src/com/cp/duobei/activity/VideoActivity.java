package com.cp.duobei.activity;

import com.cp.duobei.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		VideoView videoView = (VideoView) findViewById(R.id.videoView1);
//		MediaController mediaCrl = (MediaController) findViewById(R.id.mediaController1);
		MediaController mediaCrl = new MediaController(this, true);
		videoView.setMediaController(mediaCrl);
		videoView.setVideoPath("android.resource://com.cp.duobei/raw/videoviewdemo");
		videoView.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}

}
