package com.cp.duobei.activity;

import com.cp.duobei.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingAdapter extends BaseAdapter {
	Context mContext;
	public String[] mTitle;
	int images[];
	TextView mTitleTextview;
	public void setTitle(String name){
		if(mTitleTextview!=null){
			mTitleTextview.setText(name);
		}
	}
	public void setTitle0(String name){
		mTitle[0] = name;
//		mTitle = new String[]{name,mTitle[1],mTitle[2],mTitle[3],mTitle[4]};
	}
	public SettingAdapter(Context mContext, String[] title,int images[]) {
		super();
		this.mContext = mContext;
		this.mTitle = title;
		this.images = images;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mTitle.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean isEnabled(int position) {
		return true;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("侧边getview", mTitle[position]);
		Log.e("mTitle", mTitle+"");
		View inflate = LayoutInflater.from(mContext).inflate(R.layout.listview_item_setting_01, null);
		TextView textView = (TextView) inflate.findViewById(R.id.textView1);
		if(position==0){
			mTitleTextview = textView;
		}
		ImageView imageView = (ImageView) inflate.findViewById(R.id.imageView1);
		imageView.setImageResource(images[position]);
		textView.setText(mTitle[position]);
		return inflate;
	}

}
