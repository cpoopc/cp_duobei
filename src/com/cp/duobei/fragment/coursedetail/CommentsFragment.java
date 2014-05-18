package com.cp.duobei.fragment.coursedetail;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.cp.duobei.R;
import com.cp.duobei.dao.Constant;
import com.cp.duobei.dao.RecentlyInfo;
import com.cp.duobei.utils.SqlUtils;
import com.example.ex.AbstractFileAsynctask;
import com.example.ex.FileUtil;
import com.example.ex.LogUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class CommentsFragment extends Fragment {	
	//文件下载+数据持久化
	private boolean hasNet;
	private String JSONPATH = Constant.JSON_RECENTLY;
	private String LOCALPATH = Constant.STORE_PATH+"/comments.txt";
	private ListView mListView;
	private RecentlyAdapter adapter;
	private ArrayList<RecentlyInfo> recentlyList = new ArrayList<RecentlyInfo>();
	private ArrayList<RecentlyInfo> recentlyListLocal = new ArrayList<RecentlyInfo>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View layout = inflater.inflate(R.layout.fragment_comments, container, false);
		readfromlocal(LOCALPATH);
		filedownload();
		initListview(layout);
		return layout;
	}

	private void initListview(View layout) {
		mListView = (ListView) layout.findViewById(R.id.lv_fragment_recently);
		adapter = new RecentlyAdapter();
		mListView.setAdapter(adapter);
	}

	private void filedownload() {
		FiledownTask filedowTask = new FiledownTask();
		filedowTask.execute(JSONPATH,LOCALPATH);//下载+保存
	}

//本地读取缓存
	private void readfromlocal(String filepath){
	//	long start = System.currentTimeMillis();
		hasNet = false;
		if(recentlyListLocal.size()==0){
			//把文件读取成utf-8格式的String
			String jsonstr  = FileUtil.file2string(filepath);
			if(jsonstr!=null){
				JSONArray jsonArray;
				try {
					jsonArray = new JSONArray(jsonstr);
					RecentlyInfo info = new RecentlyInfo();
					info.readJsonArray(jsonArray, recentlyListLocal);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	class RecentlyAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			if(hasNet){
				return recentlyList.size();
			}
			return recentlyListLocal.size();
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
			TextView tv_content;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout;
			ViewHolder holder;
			if(convertView!=null){
				layout = convertView;
				holder = (ViewHolder) layout.getTag();
			}else{
				layout = getActivity().getLayoutInflater().inflate(R.layout.listview_item_recently, null);
				holder= new ViewHolder();
				holder.tv_title = (TextView) layout.findViewById(R.id.tv_recently_title);
				holder.tv_author = (TextView) layout.findViewById(R.id.tv_recently_author);
				holder.tv_content = (TextView) layout.findViewById(R.id.tv_recently_content);
				layout.setTag(holder);
			}
			RecentlyInfo info;
			if(hasNet){
				info = recentlyList.get(position);
			}else{
				info = recentlyListLocal.get(position);
			}
			holder.tv_title.setText(info.title);
			holder.tv_author.setText(info.author);
			holder.tv_content.setText(info.content);
			return layout;
		}}
	class FiledownTask extends AbstractFileAsynctask{
		@Override
		protected void onPostExecute(String result) {
			if(result==null){
				LogUtils.e("recentlyFragment", "json下载失败");
			}else{
				try { 
					JSONArray jsonArray = new JSONArray(result);
					new RecentlyInfo().readJsonArray(jsonArray, recentlyList);
					hasNet = true;
					if(adapter!=null){
						adapter.notifyDataSetChanged();
					}
					recentlyListLocal.clear();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			super.onPostExecute(result);
		}
	}
}
