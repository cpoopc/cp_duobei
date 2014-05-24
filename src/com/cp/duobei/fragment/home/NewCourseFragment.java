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
import com.cp.duobei.utils.ConnectiveUtils;
import com.cp.duobei.utils.UilUtil;
import com.cp.duobei.widget.ChildViewPager;
import com.cp.duobei.widget.PagerIndicator;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex.AbstractFileAsynctask;
import com.example.ex.FileUtil;
import com.example.ex.LogUtils;
import com.example.ex.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
public class NewCourseFragment extends AbstractFragment implements OnRefreshListener {
	//文件下载+数据持久化
	private boolean hasNet;
	private String JSONPATH = Constant.JSON_NEWCOURSE;
	private String LOCALPATH = Constant.STORE_PATH+"/newcourse.txt";
	//下拉刷新
	private PullToRefreshLayout mPullToRefreshLayout;
	//内容容器
	private ArrayList<CourseInfo> courseList = new ArrayList<CourseInfo>();
	private ArrayList<CourseInfo> courseListLocal = new ArrayList<CourseInfo>();
	//gridview+Adapter
	private CourseAdapter mCourseAdapter;
	private GridView mGridView;
	//uil
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	//viewpager,banner
	private ChildViewPager mViewPager;
	private int[] images = new int[]{
			R.drawable.banner01,
			R.drawable.banner02,
			R.drawable.banner03,
			R.drawable.banner04
		};
		private final int BIGDIG = 80000;
		Handler mhandler;
		ArrayList<ImageView> imgIndicatorList = new ArrayList<ImageView>();
		private long delayMillis = 3000;
		private boolean isDragging;
		private Runnable runnable;
		private MainActivity mainActivity;
		private PagerIndicator mIndicator;
		@Override
		public void onResume() {
			super.onResume();
			MobclickAgent.onPageStart("NewCourseFragment");
		}
		
		@Override
		public void onPause() {
			super.onPause();
			MobclickAgent.onPageEnd("NewCourseFragment");
		}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_newcourse, null); 
		pulltoreflash(layout);
		readfromlocal(LOCALPATH);
		filedownload();
		initgridview(layout);
		initviewpager(layout);
		return layout;
	}
	private void pulltoreflash(View layout) {
		mPullToRefreshLayout = (PullToRefreshLayout) layout.findViewById(R.id.fragment_pulltoreflash_newcourse);
         ActionBarPullToRefresh.from(getActivity())
                 .allChildrenArePullable()
                 .listener(this)
                 .setup(mPullToRefreshLayout);
	}
	@Override
	public void onStart() {
		super.onStart();
		autoscrollbanner();
//		LogUtils.e("newcoursefragment", "onStart");
	}
	@Override
	public void onStop() {
		super.onStop();
//		LogUtils.e("newcoursefragment", "onstop");
		mhandler.removeCallbacks(runnable);
	}
	private void autoscrollbanner() {
		mhandler = new Handler();
		runnable = new Runnable() {
			public void run() {
				if(!isDragging){
					mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
				}
				mhandler.postDelayed(runnable, delayMillis);
			}
		};
		mhandler.postDelayed(runnable, delayMillis );
	}
	private void initviewpager(View inflate) {
		mIndicator = (PagerIndicator) inflate.findViewById(R.id.pagerIndicator_home);
		mIndicator.initCount(4);
		mViewPager = (ChildViewPager) inflate.findViewById(R.id.fragment_banner_viewpager);
		mViewPager.setOffscreenPageLimit(2);
		FragmentManager fm = getChildFragmentManager();
		BannerAdapter bannerAdapter = new BannerAdapter(fm );
		mViewPager.setAdapter(bannerAdapter);
		mViewPager.setOnPageChangeListener(new BannerPagelistener());
		mViewPager.setCurrentItem(BIGDIG/2);
		FragmentActivity activity = getActivity();
		if(activity instanceof MainActivity){
			mainActivity = (MainActivity) activity;
		}
		//3.0以上才
		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
			mViewPager.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
//				LogUtils.e("mViewPager", "onTouch");
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mainActivity.setMenuDrawerEnable(false);
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						mainActivity.setMenuDrawerEnable(true);
						break;
					default:
						break;
					}
					return false;
				}
			});
		}
	}
//	public void setFocus() {
//		//解决scrollview自动滚动到底部
//		mViewPager.setFocusable(true);
//		mViewPager.setFocusableInTouchMode(true);
//		mViewPager.requestFocus();
//	}

	class BannerPagelistener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case ViewPager.SCROLL_STATE_DRAGGING:
				isDragging = true;
				break;
			case ViewPager.SCROLL_STATE_IDLE:
				isDragging = false;
				break;
			case ViewPager.SCROLL_STATE_SETTLING:
				break;

			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			mIndicator.setIndicator(arg0, arg1);
		}

		@Override
		public void onPageSelected(int arg0) {
//			for (int i = 0; i < images.length; i++) {
//				if((arg0 % images.length)==i){
//					imgIndicatorList.get(i).setImageResource(R.drawable.ic_banner_corner_unselected);
//				}else{
//					imgIndicatorList.get(i).setImageResource(R.drawable.ic_banner_corner_selected);
//				}
//			}
		}
	}
	class BannerAdapter extends FragmentPagerAdapter{

		public BannerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			ImgFragment fragment = new ImgFragment();
			fragment.changeImg(images[arg0 % images.length]);
			return fragment;
		}

		@Override
		public int getCount() {
			return BIGDIG;
		}
		
	}
//	private void initDB() {
//		SqlUtils sqlUtils = new SqlUtils(getActivity());
//		mDb = sqlUtils.getReadableDatabase();
//	}
	//本地读取缓存
	private void readfromlocal(String filepath){
//		long start = System.currentTimeMillis();
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
//		long stop = System.currentTimeMillis();
//		LogUtils.e("readfromlocal: useTime", (stop-start)+"毫秒");
	}
	private void filedownload() {
		if(!ConnectiveUtils.isConnected(getActivity())){
			ToastUtils.showToast(getActivity(), Constant.CONNECT_ERRO);
			return ;
		}
			courseList.clear();
			FiledownAsynctask filedownAsynctask = new FiledownAsynctask();
			filedownAsynctask.execute(JSONPATH,LOCALPATH);
	}
	
	private void initgridview(View inflate) {
		mGridView = (GridView) inflate.findViewById(R.id.fragment_newcourse_gridView1);
		mCourseAdapter = new CourseAdapter();
		//防止自动跳到底
		mGridView.setFocusable(false);
		mGridView.setAdapter(mCourseAdapter);
//		mGridView.setOnClickListener(new GridviewListener());
		mGridView.setOnItemClickListener(new GridviewListener());
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
					Log.e("courselist", courseList.toString());
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
				inflate = getActivity().getLayoutInflater().inflate(R.layout.newcourse_gridview_item, null);
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
//			HashCodeFileNameGenerator generator = new HashCodeFileNameGenerator();
			//TODO 打log时发现第一张图片被多次加载,全部图片被加载2次
			
//			//使用UIL异步加载图片
//			imageLoader.displayImage(imagePath, imageView, UilUtil.options, null);
			// 如果是WIFI模式,计算文件名,判断文件是否存在,若存在才加载,不存在则不下载
			UilUtil.loadimg(getActivity(),imagePath, imageView, null, null);
			return inflate;
		}}
	public void refresh() {
		JSONPATH = "http://cpduobei.qiniudn.com/newcourse/newcourse1.txt";
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
