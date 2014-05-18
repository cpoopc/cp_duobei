package com.cp.duobei.fragment.group;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.cp.duobei.R;
import com.cp.duobei.dao.Constant;
import com.cp.duobei.dao.GroupInfo;
import com.cp.duobei.fragment.AbstractFragment;
import com.cp.duobei.utils.UilUtil;
import com.cp.duobei.widget.XListView;
import com.cp.duobei.widget.XListView.IXListViewListener;
import com.example.ex.AbstractFileAsynctask;
import com.example.ex.FileUtil;
import com.example.ex.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import android.widget.Toast;

public class GroupFindFragment extends AbstractFragment implements OnRefreshListener, IXListViewListener {
	//网络下载+数据持久化
	private boolean hasNet;
	private String JSONPATH = Constant.JSON_GROUP;
	private String LOCALPATH = Constant.STORE_PATH+"/group.txt";
	//内容
	private XListView mListView;
	private GroupAdapter adapter;
	private ArrayList<GroupInfo> groupList = new ArrayList<GroupInfo>();
	private ArrayList<GroupInfo> groupListLocal = new ArrayList<GroupInfo>();
	//下拉刷新
	private PullToRefreshLayout mPullToRefreshLayout;
	//uil
	ImageLoader imageLoader = ImageLoader.getInstance();
	//上拉加载
	private int page = 1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View layout = inflater.inflate(R.layout.fragment_groupfind, container, false);
		pulltoreflash(layout);
		readfromlocal(LOCALPATH);
		filedownload();
		initListview(layout);
		return layout;
	}
	private void initListview(View layout) {
		mListView = (XListView) layout.findViewById(R.id.lv_groupfind);
		adapter = new GroupAdapter();
		mListView.setPullRefreshEnable(false);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setAdapter(adapter);
	}
	private void pulltoreflash(View layout) {
		mPullToRefreshLayout = (PullToRefreshLayout) layout.findViewById(R.id.fragment_pulltoreflash_group);
         ActionBarPullToRefresh.from(getActivity())
                 .allChildrenArePullable()
                 .listener(this)
                 .setup(mPullToRefreshLayout);
	}
	private void filedownload() {
		groupList.clear();
		page  = 1;
		FiledownTask filedowTask = new FiledownTask();
		filedowTask.execute(JSONPATH+page,LOCALPATH);//下载并保存
	}
	//本地读取缓存
	private void readfromlocal(String filepath){
		hasNet = false;
		if(groupListLocal.size()==0){
			//把文件读取成utf-8格式的String
			String jsonstr  = FileUtil.file2string(filepath);
			if(jsonstr!=null){
				JSONArray jsonArray;
				try {
					jsonArray = new JSONArray(jsonstr);
					GroupInfo info = new GroupInfo();
					info.readJsonArray(jsonArray, groupListLocal);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class GroupAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			if(hasNet){
				return groupList.size();
			}
			return groupListLocal.size();
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
			ImageView img_pic;
			TextView tv_title;
			TextView tv_members;
			TextView tv_introduce;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout;
			ViewHolder holder;
			if(convertView!=null){
				layout = convertView;
				holder = (ViewHolder) layout.getTag();
			}else{
				layout = getActivity().getLayoutInflater().inflate(R.layout.listview_item_groupfind, null);
				holder= new ViewHolder();
				holder.img_pic = (ImageView) layout.findViewById(R.id.img_ma);
				holder.tv_title = (TextView) layout.findViewById(R.id.tv_group_title);
				holder.tv_members = (TextView) layout.findViewById(R.id.tv_group_members);
				holder.tv_introduce = (TextView) layout.findViewById(R.id.tv_group_introduce);
				layout.setTag(holder);
			}
			GroupInfo info;
			if(hasNet){
				info = groupList.get(position);
			}else{
				info = groupListLocal.get(position);
			}
			imageLoader.displayImage(info.imagepath, holder.img_pic, UilUtil.options);
			holder.tv_title.setText(info.title);
			holder.tv_members.setText(info.members);
			holder.tv_introduce.setText(info.introduce);
			return layout;
		}}
	class FiledownTask extends AbstractFileAsynctask{

		@Override
		protected void onPostExecute(String result) {
			if(result==null){
				LogUtils.e("groupFragment", "json下载失败");
//				mListView.mFooterView.mHintView.setText("没有了");
				Toast.makeText(getActivity(), "没有了", Toast.LENGTH_SHORT).show();
				mListView.stopLoadMore();
			}else{
				FileOutputStream fos = null;
				try { 
					page++;//加载成功,++
					JSONArray jsonArray = new JSONArray(result);
					new GroupInfo().readJsonArray(jsonArray, groupList);
					hasNet = true;
					if(adapter!=null){
						adapter.notifyDataSetChanged();
					}
					groupListLocal.clear();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}finally{
					if(fos!=null){
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				mListView.stopLoadMore();
			}
			super.onPostExecute(result);
		}
	}
	public void reflash() {
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
                reflash();
                // Notify PullToRefreshLayout that the refresh has finished
                mPullToRefreshLayout.setRefreshComplete();
            }
        }.execute();
		
	}
	@Override
	public void onRefresh() {
		refresh();
		
	}
	@Override
	public void onLoadMore() {
		//上拉加载
		new FiledownTask().execute(JSONPATH+page);
	}
	@Override
	public void refresh() {
		page = 1;
		groupList.clear();
		new FiledownTask().execute(JSONPATH+page);
	}
}
