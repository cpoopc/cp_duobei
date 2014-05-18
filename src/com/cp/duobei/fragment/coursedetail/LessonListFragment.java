package com.cp.duobei.fragment.coursedetail;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.cp.duobei.R;
import com.cp.duobei.activity.VideoActivity;
import com.cp.duobei.dao.LessonInfo;
import com.cp.duobei.utils.SqlUtils;
import com.example.ex.AbstractFileAsynctask;
import com.example.ex.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 课程详细列表
 * 根据传来
 */
public class LessonListFragment extends Fragment {

	private ListView mListView;
	//TODO 从外界传来
//	private  String JSON_URL = "http://192.168.56.101:8080/lessoninfo/suiyuchen.txt";
	private  String JSON_URL;
	private ArrayList<LessonInfo> lessoninfoList = new ArrayList<LessonInfo>();
	private ArrayList<LessonInfo> lessoninfoListLocal = new ArrayList<LessonInfo>();
	private LessonListAdapter madapter;
	private SQLiteDatabase mDb;
	private boolean hasNet;
	private String[] randomname = new String[]{"lessonlist1.txt","lessonlist2.txt","lessonlist3.txt"};
	private String jsonname = "lessonlist1.txt";
	private String lessonname; 
	private String imagepathHead;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SqlUtils sqlUtils = new SqlUtils(getActivity());
		mDb = sqlUtils.getReadableDatabase();
		Intent intent = getActivity().getIntent();
		imagepathHead = intent.getStringExtra("imagepath");
		lessonname = intent.getStringExtra("title");
		jsonname = randomname[(int)(Math.random()*3)];//随机
		JSON_URL = intent.getStringExtra("json_lesson_path")+jsonname ;
		if(JSON_URL!=null){
			LogUtils.e("JSON_URL!=null", JSON_URL);
		}else{
			LogUtils.e("intent.JSON_URL==null", JSON_URL);
			JSON_URL = "http://192.168.56.101:8080/cpdata/lessoninfo/lessonlist.txt";
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		FiledownTask filedownTask = new FiledownTask();
		filedownTask.execute(JSON_URL);//下载
		hasNet = false;
		View layout = inflater.inflate(R.layout.fragment_lessonlist, container, false);
		readfromlocal();
		initListview(inflater, layout);
		return layout;
	}
	private void initListview(LayoutInflater inflater, View layout) {
		mListView = (ListView) layout.findViewById(R.id.lv_lessonlist);
		madapter = new LessonListAdapter();
		View headview = inflater.inflate(R.layout.listview_headview_courselist, null);
		TextView tv_title = (TextView) headview.findViewById(R.id.tv_headview_title);
		ImageView imghead = (ImageView) headview.findViewById(R.id.img_head);
		tv_title.setText(lessonname);
		ImageLoader.getInstance().displayImage(imagepathHead, imghead);
		mListView.addHeaderView(headview, null, false);
		mListView.setAdapter(madapter);
		mListView.setOnItemClickListener(new LessonItemListener());
	}
	//本地读取缓存
	private void readfromlocal(){
		hasNet = false;
		Cursor query = mDb.query("lessonlist", null, null, null, null, null, null);
		LessonInfo info = new LessonInfo();
		lessoninfoListLocal = (ArrayList<LessonInfo>) info.readCursor2List(query, lessoninfoListLocal);
	}
		
	class LessonItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
				startActivity(new Intent(getActivity(),VideoActivity.class));
		}}
	class FiledownTask extends AbstractFileAsynctask{
		@Override
		protected void onPostExecute(String result) {
			if(result==null){
				LogUtils.e("LessonlistFragment", "json下载失败");
			}else{
				try {
					JSONArray jsonArray = new JSONArray(result);
					lessoninfoList.clear();
					new LessonInfo().readJsonArray(jsonArray, lessoninfoList);
					hasNet = true;
					if(madapter!=null){ 
						madapter.notifyDataSetChanged();
						lessoninfoListLocal.clear();//节省内存
						mDb.delete("lessonlist", null, null);//清空所有缓存数据
						//写入数据库
						new LessonInfo().writeList2DB(mDb, lessoninfoList, "lessonlist");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onPostExecute(result);
			}
		}
	}
	class LessonListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(hasNet){
				return lessoninfoList.size();
			}else{
				return lessoninfoListLocal.size();
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
			TextView tv_title;
			TextView tv_learnning;
			TextView tv_num;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout;
			ViewHolder holder;
			if(convertView!=null){
				layout = convertView;
				holder = (ViewHolder) layout.getTag();
			}else{
				layout = getActivity().getLayoutInflater().inflate(R.layout.listview_item_courselist, null);
				holder= new ViewHolder();
				holder.tv_title = (TextView) layout.findViewById(R.id.tv_title);
				holder.tv_learnning = (TextView) layout.findViewById(R.id.tv_learnning);
				holder.tv_num = (TextView) layout.findViewById(R.id.tv_num);
				layout.setTag(holder);
			}
			LessonInfo info;
			if(hasNet){
				info = lessoninfoList.get(position);
			}else{
				info = lessoninfoListLocal.get(position);
			}
			holder.tv_title.setText(info.title);
			holder.tv_learnning.setText(info.learnning);
			holder.tv_num.setText(info.num);
			return layout;
		}}
}
