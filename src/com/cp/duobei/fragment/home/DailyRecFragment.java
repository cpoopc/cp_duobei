package com.cp.duobei.fragment.home;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.cp.duobei.R;
import com.cp.duobei.dao.Constant;
import com.cp.duobei.dao.CourseInfo;
import com.cp.duobei.dao.CourseRecInfo;
import com.cp.duobei.fragment.AbstractFragment;
import com.cp.duobei.utils.SqlUtils;
import com.cp.duobei.utils.UilUtil;
import com.example.ex.AbstractFileAsynctask;
import com.example.ex.FileUtil;
import com.example.ex.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DailyRecFragment extends AbstractFragment implements OnRefreshListener {
	//文件下载+数据持久化
	private boolean hasNet;
	private String JSONPATH = Constant.JSON_DAILYREC;
	private String LOCALPATH = Constant.STORE_PATH+"/dailyrec.txt";
	//容器+listview+Adapter
	ArrayList<CourseRecInfo> courserecList = new ArrayList<CourseRecInfo>();
	ArrayList<CourseRecInfo> courserecListLocal = new ArrayList<CourseRecInfo>();
	private DailyrecAdapter adapter;
	//uil
	private ImageLoader imageLoader = ImageLoader.getInstance();
	//下拉刷新
	private PullToRefreshLayout mPullToRefreshLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_dailyrec, null);
		 pulltoreflash(layout);
		 readfromlocal(LOCALPATH);
		filedownload();
		ListView mListView = (ListView) layout.findViewById(R.id.lv_fragment_dailyrec);
		adapter = new DailyrecAdapter();
		mListView.setAdapter(adapter);
		return layout;
	}
	private void pulltoreflash(View layout) {
		mPullToRefreshLayout = (PullToRefreshLayout) layout.findViewById(R.id.fragment_pulltoreflash_dailyrec);
         ActionBarPullToRefresh.from(getActivity())
                 .allChildrenArePullable()
                 .listener(this)
                 .setup(mPullToRefreshLayout);
	}
	private void filedownload() {
		courserecList.clear();
		FiledownTask filedowTask = new FiledownTask();
		filedowTask.execute(JSONPATH,LOCALPATH);//下载
	}
	//本地读取缓存
	private void readfromlocal(String filepath){
//		long start = System.currentTimeMillis();
		hasNet = false;
		if(courserecListLocal.size()==0){
			//把文件读取成utf-8格式的String
			String jsonstr  = FileUtil.file2string(filepath);
			if(jsonstr!=null){
				JSONArray jsonArray;
				try {
					jsonArray = new JSONArray(jsonstr);
					CourseRecInfo info = new CourseRecInfo();
					info.readJsonArray(jsonArray, courserecListLocal);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
//		long stop = System.currentTimeMillis();
//		LogUtils.e("readfromlocal: useTime", (stop-start)+"毫秒");
	}
	class FiledownTask extends AbstractFileAsynctask{
		@Override
		protected void onPostExecute(String result) {
			if(result==null){
				LogUtils.e("DailyRecFragment", "json下载失败");
			}else{
				try { 
					JSONArray jsonArray = new JSONArray(result);
					new CourseRecInfo().readJsonArray(jsonArray, courserecList);
					hasNet = true;
					if(adapter!=null){
						adapter.notifyDataSetChanged();
					}
					courserecListLocal.clear();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			super.onPostExecute(result);
		}
	}
	class DailyrecAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			if(hasNet){
				return courserecList.size();
			}
			return courserecListLocal.size();
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
			TextView tv_author;
			TextView tv_good;
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
				layout = getActivity().getLayoutInflater().inflate(R.layout.listview_item_courserec, null);
				holder= new ViewHolder();
				holder.tv_title = (TextView) layout.findViewById(R.id.tv_courserec_title);
				holder.tv_author = (TextView) layout.findViewById(R.id.tv_courserec_author);
				holder.tv_good = (TextView) layout.findViewById(R.id.tv_courserec_good);
				holder.img_title = (ImageView) layout.findViewById(R.id.img_courserec_title);
				layout.setTag(holder);
			}
			CourseRecInfo info;
			if(hasNet){
				info = courserecList.get(position);
			}else{
				info = courserecListLocal.get(position);
			}
			holder.tv_title.setText(info.title);
			holder.tv_author.setText(info.author);
			holder.tv_good.setText(info.good);
			imageLoader.displayImage(info.imagepath, holder.img_title, UilUtil.options, null);
//			LogUtils.e("DailyRec", "info.imagepath"+info.imagepath);
			return layout;
		}}
	public void refresh() {
		filedownload();
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
                refresh();
                // Notify PullToRefreshLayout that the refresh has finished
                mPullToRefreshLayout.setRefreshComplete();
            }
        }.execute();
		
	}
	
}
