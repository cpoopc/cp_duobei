package com.cp.duobei.activity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cp.duobei.R;
import com.cp.duobei.dao.Constant;
import com.cp.duobei.dao.PostDetail;
import com.cp.duobei.utils.SwipeBackSherlockActivity;
import com.example.ex.AbstractFileAsynctask;
import com.example.ex.LogUtils;
import com.example.ex.ToastUtils;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PostDetailActivity extends SwipeBackSherlockActivity{

//	private TextView mTextView;
	private MyAdapter myAdapter;
	SparseArray<SoftReference<View>> cacheViewArray = new SparseArray<SoftReference<View>>();
	ArrayList<PostDetail> postList = new ArrayList<PostDetail>();
	private ListView mListView;
	private View loadinglayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_detail);
		ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		Intent data = getIntent();
		String title = data.getStringExtra("title");
		supportActionBar.setTitle(title);
		int tid = data.getIntExtra("tid", 0);
		LogUtils.e("tid", tid+"");
		FiledownTask filedownTask = new FiledownTask();
		filedownTask.execute(Constant.POST_DETAIL+tid);
		loadinglayout = findViewById(R.id.view_loading);
		mListView = (ListView) findViewById(R.id.lv_postdetail);
		
		myAdapter = new MyAdapter();
		mListView.setAdapter(myAdapter);
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return postList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public boolean isEnabled(int position) {
			return false;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//保存webview缓存,避免每次都load
//			Log.e("position", position+"..:convertView"+convertView);
			SoftReference<View> che = cacheViewArray.get(position);
			if(che!=null){
				View  cache = che.get();
				if(cache!=null){
					return cache;
				}else{
					Log.e("cache", ""+cache);
					ToastUtils.showToast(PostDetailActivity.this, "null空指针");
				}
			}
			View layout = getLayoutInflater().inflate(R.layout.listview_item_postdetail, null);
			TextView tv_lou = (TextView) layout.findViewById(R.id.tv_lou);
			TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
			TextView tv_dateline = (TextView) layout.findViewById(R.id.tv_dateline);
//			TextView tv_message = (TextView) layout.findViewById(R.id.tv_message);
			PostDetail info = postList.get(position);
			if(info.first==1){
//				tv_lou.setTextSize(22);
				tv_lou.setTextColor(Color.RED);
				tv_lou.setText("楼主");
//				tv_title.setTextSize(22);
				tv_title.setTextColor(Color.RED);
			}else{
				tv_lou.setText(position+" 楼");
			}
			tv_title.setText(info.author);
			tv_dateline.setText(info.dateline);
			WebView webView = (WebView) layout.findViewById(R.id.webView1);
			WebSettings settings = webView.getSettings();
			settings.setJavaScriptEnabled(false);
//			webView.lo
			webView.loadDataWithBaseURL("http://bbs.51cto.com/", info.message,
					"text/html", "utf-8", null);
			SoftReference<View> softReference = new SoftReference<View>(layout);
			cacheViewArray.put(position, softReference);
//			Spanned fromHtml = Html.fromHtml(info.message);
//			tv_message.setText(fromHtml);
//			HtmlLoader htmlLoader = new HtmlLoader(PostDetailActivity.this);
//			htmlLoader.setTextview(tv_message);
//			htmlLoader.executeOnExecutor(Constant.THREAD_POOL_EXECUTOR,info.message);
			return layout;
		}
	}
	
	class FiledownTask extends AbstractFileAsynctask{
		@Override
		protected void onPostExecute(String result) { 
			if(result!=null){
				try {
					JSONObject jsonObject2 = new JSONObject(result);
					Iterator<String> keys = jsonObject2.keys();
					while(keys.hasNext()){
						String key = (String) keys.next();
						LogUtils.e("key", key+"");
						JSONObject jsonObject = jsonObject2.getJSONObject(key);
						PostDetail info = new PostDetail();
						info.dateline = jsonObject.getString("dateline");
						info.dbdateline = jsonObject.getInt("dbdateline");
						info.author = jsonObject.getString("author");
						info.first = jsonObject.getInt("first");
						info.message = jsonObject.getString("message");
						info.subject = jsonObject.getString("subject");
						postList.add(info);
					}
					Collections.sort(postList, new Comparator<PostDetail>() {
						
						@Override
						public int compare(PostDetail lhs, PostDetail rhs) {
							return lhs.dbdateline - rhs.dbdateline;
						}
					});
					if(myAdapter!=null){
						myAdapter.notifyDataSetChanged();
						loadinglayout.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				ToastUtils.showToast(PostDetailActivity.this,"请检查网络连接");
			}
		}
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.post_detail, menu);
		return true;
	}

}
