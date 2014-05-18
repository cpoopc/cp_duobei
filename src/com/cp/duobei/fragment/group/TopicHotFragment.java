package com.cp.duobei.fragment.group;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.cp.duobei.R;
import com.cp.duobei.R.layout;
import com.cp.duobei.activity.PostDetailActivity;
import com.cp.duobei.dao.Constant;
import com.cp.duobei.dao.RecentlyInfo;
import com.cp.duobei.fragment.AbstractFragment;
import com.cp.duobei.widget.XListView;
import com.cp.duobei.widget.XListView.IXListViewListener;
import com.example.ex.AbstractFileAsynctask;
import com.example.ex.FileUtil;
import com.example.ex.LogUtils;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class TopicHotFragment extends AbstractFragment implements IXListViewListener, OnRefreshListener, OnItemClickListener {

	private ArrayList<Post> tieziList = new ArrayList<Post>();
	private ArrayList<Post> tieziListLocal = new ArrayList<Post>();
	private MyAdapter myAdapter;
	//下拉刷新
	private PullToRefreshLayout mPullToRefreshLayout;
	//上拉加载
	private XListView mXlistView;
	int page = 1;
	//网络下载+数据持久化
	private boolean hasNet;
	private String JSONPATH = Constant.POST_LIST;
	private String LOCALPATH = Constant.STORE_PATH+"/topichot.txt";
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("TopicHotFragment");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("TopicHotFragment");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_topic_hot, container, false);
		readfromlocal(LOCALPATH);
		if(tieziList.size()==0){
			page = 1;
			filedownload(JSONPATH,LOCALPATH);
		};
		pulltoreflash(layout);
		initListview(layout);
		return layout;
	}
	//本地读取缓存
	private void readfromlocal(String filepath){
		hasNet = false;
		if(tieziListLocal.size()==0){
			//把文件读取成utf-8格式的String
			String jsonstr  = FileUtil.file2string(filepath);
			if(jsonstr!=null){
				JSONArray jsonArray;
				try {
					jsonArray = new JSONArray(jsonstr);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Post post = new Post();
						post.tid = jsonObject.getInt("tid");
						post.title= jsonObject.getString("subject");
						post.author= jsonObject.getString("author");
						post.views= jsonObject.getInt("views");
						post.replies= jsonObject.getInt("replies");
						tieziListLocal.add(post);
					}
					if(myAdapter!=null){
						myAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private void initListview(View layout) {
		mXlistView = (XListView) layout.findViewById(R.id.lv_fragment_topichot);
//		mXlistView = (XListView) layout.findViewById(R.id.lv_fragment_topichot);
		mXlistView.setPullRefreshEnable(false);
		mXlistView.setPullLoadEnable(true);
		mXlistView.setXListViewListener(this);
		myAdapter = new MyAdapter();
		mXlistView.setAdapter(myAdapter);
		mXlistView.setOnItemClickListener(this);
	}
	private void pulltoreflash(View layout) {
		mPullToRefreshLayout = (PullToRefreshLayout) layout.findViewById(R.id.fragment_pulltoreflash_topichot);
         ActionBarPullToRefresh.from(getActivity())
                 .allChildrenArePullable()
                 .listener(this)
                 .setup(mPullToRefreshLayout);
	}
	private void onLoad() {
		mXlistView.stopLoadMore();
		mXlistView.setRefreshTime("刚刚");
	}
	/**若有传入本地path则下载并保存**/
	private void filedownload(String url, String lOCALPATH2) {
		FiledownTask filedowTask = new FiledownTask();
		if(lOCALPATH2!=null){
			filedowTask.execute(url+page,lOCALPATH2);//下载并保存
		}else{
			filedowTask.execute(url+page);//下载
		}
	}
	class Post{
		int tid;
		String title;
		String author;
		int views;
		int replies;
	}
	class FiledownTask extends AbstractFileAsynctask{
		@Override
		protected void onPostExecute(String result) {
			if(result!=null){
				try {
					JSONArray jsonArray = new JSONArray(result);
//					tieziList.clear();
					page++;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Post post = new Post();
						post.tid = jsonObject.getInt("tid");
						post.title= jsonObject.getString("subject");
						post.author= jsonObject.getString("author");
						post.views= jsonObject.getInt("views");
						post.replies= jsonObject.getInt("replies");
						tieziList.add(post);
					}
					if(myAdapter!=null){
						myAdapter.notifyDataSetChanged();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				Toast.makeText(getActivity(), "没有了", 0).show();
			}
			onLoad();
			super.onPostExecute(result);
		}
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tieziList.size();
		}

		@Override
		public Post getItem(int position) {
			return tieziList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout = getActivity().getLayoutInflater().inflate(R.layout.listview_item_topichot, null);
			TextView title = (TextView) layout.findViewById(R.id.tv_title);
			TextView author = (TextView) layout.findViewById(R.id.tv_author);
			TextView reply = (TextView) layout.findViewById(R.id.tv_reply);
			Post post = getItem(position);
			title.setText(post.title);
			author.setText(post.author);
			reply.setText("回复数:"+post.replies+"/浏览数:"+post.views);
			return layout;
		}}
	@Override
	public void onRefresh() {
		refresh();
	}
	@Override
	public void onLoadMore() {
		filedownload(JSONPATH,null);
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
                // Notify PullToRefreshLayout that the refresh has finished
                mPullToRefreshLayout.setRefreshComplete();
            }
        }.execute();
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(),PostDetailActivity.class);
		intent.putExtra("tid", myAdapter.getItem(position-1).tid);
		startActivity(intent);
	}
	@Override
	public void refresh() {
		tieziList.clear();
		page = 1;
		filedownload(JSONPATH,LOCALPATH);
	}
}
