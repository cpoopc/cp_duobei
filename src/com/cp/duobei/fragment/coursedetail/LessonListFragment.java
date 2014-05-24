package com.cp.duobei.fragment.coursedetail;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.cp.duobei.R;
import com.cp.duobei.activity.VideoActivity;
import com.cp.duobei.dao.LessonInfo;
import com.cp.duobei.utils.ConnectiveUtils;
import com.cp.duobei.utils.DbManager;
import com.cp.duobei.utils.SqlUtils;
import com.example.ex.AbstractFileAsynctask;
import com.example.ex.LogUtils;
import com.example.ex.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 课程详细列表
 * 根据传来
 */
public class LessonListFragment extends Fragment {

	private ListView mListView;
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
		initButton(headview);
		tv_title.setText(lessonname);
		ImageLoader.getInstance().displayImage(imagepathHead, imghead);
		mListView.addHeaderView(headview, null, false);
		mListView.setAdapter(madapter);
		mListView.setOnItemClickListener(new LessonItemListener());
	}
	private void initButton(View headview) {
		final Button button = (Button) headview.findViewById(R.id.btn_addtomycourse);
		//TODO 1..登陆状态显示,没登陆则隐藏(或者提示登陆).2.读取数据库,是否在课表内
		SharedPreferences sp = getActivity().getSharedPreferences("userinfo", 0);
		final String username = sp.getString("username", "");
		if("".equals(username)){
			button.setText("登陆后添加课表");
		}else{
			Cursor query = DbManager.getInstance(getActivity()).query("mycourse", "username=? AND coursetitle=?", new String[]{username,lessonname});
			if(query.moveToFirst()){
				button.setText("已经添加");
			}else{
				button.setText("添加到我的课表");
				button.setClickable(true);
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ToastUtils.showToast(getActivity(), lessonname);
						DbManager.getInstance(getActivity()).insert("mycourse", username, lessonname,imagepathHead);
						button.setText("已经添加");
						button.setClickable(false);
					}
				});
			}
		}
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
				if(ConnectiveUtils.isWIFI(getActivity())){
					startActivity(new Intent(getActivity(),VideoActivity.class));
				}else{
					new AlertDialog.Builder(getActivity()).setTitle("提示")
					.setMessage("建议在WIFI环境下观看!")
					.setPositiveButton("仍然播放", new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startActivity(new Intent(getActivity(),VideoActivity.class));
						}
					})
					.setNegativeButton("取消", null)
					.create().show();
				}
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
