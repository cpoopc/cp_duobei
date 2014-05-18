package com.cp.duobei.fragment.login;

import com.cp.duobei.R;
import com.cp.duobei.R.layout;
import com.cp.duobei.activity.MainActivity;
import com.cp.duobei.fragment.AbstractFragment;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class MyInfoFragment extends AbstractFragment implements OnClickListener {
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MyInfoFragment");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MyInfoFragment");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_my_info, container, false);
		Button button = (Button) layout.findViewById(R.id.btn_logout);
		button.setOnClickListener(this);
		return layout;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_logout:
			Toast.makeText(getActivity(), "tuichu", 0).show();
			FragmentActivity activity = getActivity();
			if(activity instanceof MainActivity){
				((MainActivity) activity).setLoginedFragment(null);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void refresh() {
		Toast.makeText(getActivity(), "刷新资料", 0).show();
	}

}
