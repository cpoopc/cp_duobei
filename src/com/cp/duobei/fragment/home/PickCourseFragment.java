/**
 * 新课速递
 */
package com.cp.duobei.fragment.home;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.cp.duobei.R;
import com.cp.duobei.activity.CourseDetailActivity;
import com.cp.duobei.activity.MainActivity;
import com.cp.duobei.dao.Constant;
import com.cp.duobei.dao.CourseInfo;
import com.cp.duobei.fragment.AbstractFragment;
import com.cp.duobei.utils.UilUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex.AbstractFileAsynctask;
import com.example.ex.FileUtil;
import com.example.ex.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
public class PickCourseFragment extends AbstractFragment implements OnRefreshListener {
	//文件下载+数据持久化
	private boolean hasNet;
	private String JSONPATH_PICK = Constant.JSON_PICKCOURSE;
	private String LOCALPATH_PICK = Constant.STORE_PATH+"/pickcourse.txt";
	
	private String JSONPATH_TEACHER = Constant.JSON_PICKTEACHER;
	private String LOCALPATH_TEACHER = Constant.STORE_PATH+"/pickteacher.txt";
	//下拉刷新
	private PullToRefreshLayout mPullToRefreshLayout;
	//内容容器
	private ArrayList<CourseInfo> courseList = new ArrayList<CourseInfo>();
	private ArrayList<CourseInfo> courseListLocal = new ArrayList<CourseInfo>();
	private ArrayList<CourseInfo> teacherList = new ArrayList<CourseInfo>();
	private ArrayList<CourseInfo> teacherListLocal = new ArrayList<CourseInfo>();
	
	//gridview+Adapter
	private CourseAdapter mCourseAdapter;
	private ListView mListViewPickcourse;
	//uil
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private MainActivity mainActivity;
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("PickCourseFragment");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("PickCourseFragment");
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_pickcourse, null); 
		pulltoreflash(layout);
		readfromlocal(LOCALPATH_PICK);
		filedownload();
		initgridview(layout);
		return layout;
	}
	private void pulltoreflash(View layout) {
		mPullToRefreshLayout = (PullToRefreshLayout) layout.findViewById(R.id.fragment_pulltoreflash_newcourse);
         ActionBarPullToRefresh.from(getActivity())
                 .allChildrenArePullable()
                 .listener(this)
                 .setup(mPullToRefreshLayout);
	}
	//本地读取缓存
	private void readfromlocal(String filepath){
		hasNet = false;
		if(courseListLocal.size()==0){
			//把文件读取成utf-8格式的String
			String jsonstr  = FileUtil.file2string(filepath);
			if(jsonstr!=null){
				JSONArray jsonArray;
				try {
					jsonArray = new JSONArray(jsonstr);
					CourseInfo info = new CourseInfo();
					info.readJsonArray(jsonArray, courseListLocal);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private void filedownload() {
		courseList.clear();
		FiledownAsynctask filedownAsynctask = new FiledownAsynctask();
		filedownAsynctask.execute(JSONPATH_PICK,LOCALPATH_PICK);
	}
	
	private void initgridview(View inflate) {
		mListViewPickcourse = (ListView) inflate.findViewById(R.id.fragment_pickcourse_listView1);
		mCourseAdapter = new CourseAdapter();
		//防止自动跳到底
		mListViewPickcourse.setAdapter(mCourseAdapter);
//		mGridView.setOnClickListener(new GridviewListener());
		mListViewPickcourse.setOnItemClickListener(new GridviewListener());
	}
	class GridviewListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(getActivity(),CourseDetailActivity.class);
			CourseInfo courseInfo ;
			if(hasNet){
				courseInfo = courseList.get(position);
			}else{
				courseInfo = courseListLocal.get(position);
			}
			intent.putExtra("imagepath", courseInfo.imagepath);
			intent.putExtra("title", courseInfo.title);
			intent.putExtra("json_lesson_path", courseInfo.detailpage);
			Toast.makeText(getActivity(), "position"+position, Toast.LENGTH_SHORT).show();
			startActivity(intent);
		}
		}
	//json下载
	class FiledownAsynctask extends AbstractFileAsynctask{
		@Override
		protected void onPostExecute(String result) {
			if(result!=null){
				try {
					JSONArray jsonArray = new JSONArray(result);
					new CourseInfo().readJsonArray(jsonArray, courseList);
					hasNet = true;
					mCourseAdapter.notifyDataSetChanged();
					courseListLocal.clear();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
				LogUtils.e("NewCourseFragment-FiledownLoadTask", "json文件下载失败");
			}
			
			super.onPostExecute(result);
		}
	}

	//适配器
	class CourseAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			if(hasNet){
				return courseList.size();
			}else{
				return courseListLocal.size();
			}
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
			ImageView imageView;
			TextView title;
			TextView author;
			public ViewHolder(ImageView imageView, TextView title,
					TextView author) {
				super();
				this.imageView = imageView;
				this.title = title;
				this.author = author;
			}
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflate;
			//优化
			ViewHolder viewHolder;
			ImageView imageView;
			TextView title;
			TextView author;
			if(convertView==null){
				inflate = getActivity().getLayoutInflater().inflate(R.layout.listview_item_pickcourse, null);
				imageView = (ImageView) inflate.findViewById(R.id.imageView1);
				title = (TextView) inflate.findViewById(R.id.textView1);
				author = (TextView) inflate.findViewById(R.id.textView2);
				viewHolder = new ViewHolder(imageView, title, author);
				inflate.setTag(viewHolder);
			}else{
				inflate = convertView;
				viewHolder = (ViewHolder) inflate.getTag();
				imageView = viewHolder.imageView;
				title = viewHolder.title;
				author = viewHolder.author;
			}
			CourseInfo courseInfo;
			if(hasNet){
				courseInfo = courseList.get(position);
			}else{
				courseInfo = courseListLocal.get(position);
			}
			title.setText(courseInfo.getTitle());
			author.setText(courseInfo.getAuthor());
			String imagePath = courseInfo.getImagepath();
			//使用UIL异步加载图片
//			imageLoader.displayImage(imagePath, imageView, UilUtil.options, null);
			UilUtil.loadimg(imagePath, imageView, null, null);
			return inflate;
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
