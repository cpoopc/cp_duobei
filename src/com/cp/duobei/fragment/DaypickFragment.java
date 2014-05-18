package com.cp.duobei.fragment;

import com.cp.duobei.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class DaypickFragment extends Fragment implements OnClickListener{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_daypick, null);
		return inflate;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
