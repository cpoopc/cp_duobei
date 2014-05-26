package com.cp.duobei.fragment.login;


import java.util.ArrayList;


import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import com.cp.duobei.R;
import com.cp.duobei.activity.CourseDetailActivity;
import com.cp.duobei.dao.Constant;
import com.cp.duobei.dao.CourseInfo;
import com.cp.duobei.fragment.AbstractFragment;
import com.cp.duobei.utils.DbManager;
import com.cp.duobei.utils.UilUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyCourseFragment extends AbstractFragment implements OnRefreshListener {
	//文件下载+数据持久化
	private String username;
	//容器+listview+Adapter
	private DailyrecAdapter adapter;
	//uil
	private ImageLoader imageLoader = ImageLoader.getInstance();
	//下拉刷新
	private PullToRefreshLayout mPullToRefreshLayout;
	
	ArrayList<MycourseInfo> mycourseList = new ArrayList<MycourseInfo>();
	class MycourseInfo{
		String coursetitle;
		String imagepath;
		public MycourseInfo(String coursetitle, String imagepath) {
			super();
			this.coursetitle = coursetitle;
			this.imagepath = imagepath;
		}
		
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MyCourseFragment");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MyCourseFragment");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SharedPreferences sp = getActivity().getSharedPreferences("userinfo", 0);
		username = sp.getString("username", "");
		readfromlocaldb();
		View layout = inflater.inflate(R.layout.fragment_dailyrec, null);
		 pulltoreflash(layout);
		initListview(layout);
		return layout;
	}

	private void initListview(View layout) {
		ListView mListView = (ListView) layout.findViewById(R.id.lv_fragment_dailyrec);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),CourseDetailActivity.class);
				MycourseInfo courseInfo = mycourseList.get(position);
				intent.putExtra("imagepath", courseInfo.imagepath);
				intent.putExtra("title", courseInfo.coursetitle);
				//CP 暂用
				intent.putExtra("json_lesson_path", Constant.LESSONINFO_BASE);
				Toast.makeText(getActivity(), "position"+position, Toast.LENGTH_SHORT).show();
				startActivity(intent);
			}
		});
		adapter = new DailyrecAdapter();
		mListView.setAdapter(adapter);
	}
	private void pulltoreflash(View layout) {
		mPullToRefreshLayout = (PullToRefreshLayout) layout.findViewById(R.id.fragment_pulltoreflash_dailyrec);
         ActionBarPullToRefresh.from(getActivity())
                 .allChildrenArePullable()
                 .listener(this)
                 .setup(mPullToRefreshLayout);
	}
	private void readfromlocaldb(){
		mycourseList.clear();
		if(username!=null){
			Cursor query = DbManager.getInstance(getActivity()).query("mycourse", "username=?", new String[]{username});
			boolean toFirst = query.moveToFirst();
			while(toFirst){
				String coursetitle = query.getString(query.getColumnIndex("coursetitle"));
				String imagepath = query.getString(query.getColumnIndex("imagepath"));
				mycourseList.add(new MycourseInfo(coursetitle, imagepath));
				toFirst = query.moveToNext();
			}
			if(adapter!=null){
				adapter.notifyDataSetChanged();
			}
		}
	}
	class DailyrecAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return mycourseList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
		class ViewHolder{
			TextView tv_title;
			ImageView img_title;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout;
			ViewHolder holder;
			if(convertView!=null){
				layout = convertView;
				holder = (ViewHolder) layout.getTag();
			}else{
				layout = getActivity().getLayoutInflater().inflate(R.layout.listview_item_mycourse, null);
				holder= new ViewHolder();
				holder.tv_title = (TextView) layout.findViewById(R.id.tv_courserec_title);
				holder.img_title = (ImageView) layout.findViewById(R.id.img_courserec_title);
				layout.setTag(holder);
			}
			MycourseInfo info;
				info = mycourseList.get(position);
			holder.tv_title.setText(info.coursetitle);
			imageLoader.displayImage(info.imagepath, holder.img_title, UilUtil.options, null);
//			LogUtils.e("DailyRec", "info.imagepath"+info.imagepath);
			return layout;
		}}
	public void reflash() {
		readfromlocaldb();
	}
	@Override
	public void onRefreshStarted(View view) {
		new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2222);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                reflash();
                // Notify PullToRefreshLayout that the refresh has finished
                mPullToRefreshLayout.setRefreshComplete();
            }
        }.execute();
		
	}
	@Override
	public void refresh() {
		Toast.makeText(getActivity(), "刷新我的课表", 0).show();
		readfromlocaldb();
	}
	
}
