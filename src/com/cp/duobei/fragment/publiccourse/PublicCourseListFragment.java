package com.cp.duobei.fragment.publiccourse;

import java.util.ArrayList;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import com.cp.duobei.R;
import com.cp.duobei.R.layout;
import com.cp.duobei.dao.Constant;
import com.cp.duobei.dao.CourseRecInfo;
import com.cp.duobei.fragment.AbstractFragment;
import com.cp.duobei.fragment.home.DailyRecFragment;
import com.cp.duobei.utils.ConnectiveUtils;
import com.cp.duobei.utils.UilUtil;
import com.example.ex.AbstractExAdapter;
import com.example.ex.AbstractFileAsynctask;
import com.example.ex.LogUtils;
import com.example.ex.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class PublicCourseListFragment extends AbstractFragment implements OnChildClickListener {

	private ExpandableListView mExListView;
	private MyExAdapter adapter;
	ArrayList<String> mGroupName = new ArrayList<String>();
	HashMap<String, ArrayList<String>> mGroupChild = new HashMap<String, ArrayList<String>>();
	private ListView mListView;
	private boolean hasNet;
	ArrayList<CourseRecInfo> publiccourseList = new ArrayList<CourseRecInfo>();
	ArrayList<CourseRecInfo> publiccourseListLocal = new ArrayList<CourseRecInfo>();
	private PublicCourseAdapter adapter2;
	ImageLoader imageLoader = ImageLoader.getInstance();
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("PublicCourseListFragment");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("PublicCourseListFragment");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_public_course_list,
				container, false);
		publiccourseList.clear();
		hasNet = true;
		initData();
		filedownload(Constant.JSON_PUBLICCOURSE);
		initExListview(layout);
		initListview(layout);
		return layout;
	}
	private void initListview(View layout) {
		mListView = (ListView) layout.findViewById(R.id.lv_publiccourse);
		adapter2 = new PublicCourseAdapter();
		mListView.setAdapter(adapter2);
	}
	private void filedownload(String url) {
		if(!ConnectiveUtils.isConnected(getActivity())){
			ToastUtils.showToast(getActivity(), Constant.CONNECT_ERRO);
			return ;
		}
//		publiccourseList.clear();
		FiledownTask filedowTask = new FiledownTask();
		filedowTask.execute(url);//下载
	}
	class FiledownTask extends AbstractFileAsynctask{
		@Override
		protected void onPostExecute(String result) {
			if(result==null){
				LogUtils.e("DailyRecFragment", "json下载失败");
			}else{
				try { 
					JSONArray jsonArray = new JSONArray(result);
					publiccourseList.clear();
					new CourseRecInfo().readJsonArray(jsonArray, publiccourseList);
					hasNet = true;
					if(adapter2!=null){
						adapter2.notifyDataSetChanged();
					}
//					courserecListLocal.clear();
//					mDb.delete("courserec", null, null);//清空所有缓存数据
//					LogUtils.e("courserecList", courserecList.toString());
					//写入数据库
//					new CourseRecInfo().writeList2DB(mDb, courserecList, "courserec");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			super.onPostExecute(result);
		}
	}
	private void initExListview(View layout) {
		mExListView = (ExpandableListView) layout.findViewById(R.id.exListView_publiccourse);
		adapter = new MyExAdapter();
		mExListView.setAdapter(adapter);
		mExListView.setOnChildClickListener(this);
	}
	private void initData() {
		String[] group = new String[]{"运动","娱乐","学习","工作"};
		String[] child1 = new String[]{"篮球","足球","羽毛球","乒乓球","台球"};
		String[] child2 = new String[]{"电视","电影","音乐"};
		String[] child3 = new String[]{"code","看书","发呆","鞋子","你好"};
		String[] child4 = new String[]{"上班","office","偷懒","下班","公交"};
		ArrayList<String[]> childlist = new ArrayList<String[]>();
		childlist.add(child1);
		childlist.add(child2);
		childlist.add(child3);
		childlist.add(child4);
		mGroupName.clear();
		mGroupChild.clear();
		for (int i = 0; i < group.length; i++) {
			mGroupName.add(group[i]);
			ArrayList<String> list = new ArrayList<String>();
			String[] strings = childlist.get(i);
			for (int j = 0; j < childlist.get(i).length; j++) {
				list.add(strings[j]);
				mGroupChild.put(group[i], list);
			}
		}
	}
	
	class MyExAdapter extends AbstractExAdapter{
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		@Override
		public String getChild(int groupPosition, int childPosition) {
			String groupname = mGroupName.get(groupPosition);
			return mGroupChild.get(groupname).get(childPosition);
		}
		@Override
		public int getGroupCount() {
			return mGroupName.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			String groupname = mGroupName.get(groupPosition);
			return mGroupChild.get(groupname).size();
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View layout = getActivity().getLayoutInflater().inflate(R.layout.listview_item_ex_publiccourse_01, null);
			TextView textView = (TextView) layout.findViewById(R.id.title);
			String str = mGroupName.get(groupPosition);
			textView.setTextSize(25);
			textView.setText(str);
			return layout;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView textView = new TextView(getActivity());
			textView.setTextSize(20);
			String str = getChild(groupPosition, childPosition);
			textView.setText(str);
			return textView;
		}
		
	}
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		String str = adapter.getChild(groupPosition, childPosition);
		Toast.makeText(getActivity(), str, 0).show();
		int x = (int)(Math.random()*4);
		filedownload(Constant.PATH_PUBLICCOURSE+"/publiccourse"+x+".txt");
		return false;
	}
	
	class PublicCourseAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			if(hasNet){
				return publiccourseList.size();
			}
			return publiccourseListLocal.size();
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
				info = publiccourseList.get(position);
			}else{
				info = publiccourseListLocal.get(position);
			}
			holder.tv_title.setText(info.title);
			holder.tv_author.setText(info.author);
			holder.tv_good.setText(info.good);
			UilUtil.loadimg(getActivity(), info.imagepath,  holder.img_title, null, null);
//			imageLoader.displayImage(info.imagepath, holder.img_title, UilUtil.options, null);
			return layout;
		}}
	@Override
	public void refresh() {
		Toast.makeText(getActivity(), "公开课刷新", 0).show();
		
	}
}
