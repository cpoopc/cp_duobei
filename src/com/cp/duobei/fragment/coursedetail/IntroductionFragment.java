package com.cp.duobei.fragment.coursedetail;

import com.cp.duobei.R;
import com.cp.duobei.dao.Constant;
import com.example.ex.AbstractFileAsynctask;
import com.example.ex.FileUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class IntroductionFragment extends Fragment {
	//文件下载+数据持久化
	private boolean hasNet;
	private String JSONPATH;
	private String LOCALPATH = Constant.STORE_PATH+"/introduction.txt";
	
	private String[] randomname = new String[]{"introduction1.txt","introduction2.txt","introduction3.txt"};
	private String jsonname = "introduction1.txt";
	private TextView tv_title;
	private TextView tv_content;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_introduction, container,
						false);
		tv_title = (TextView) layout.findViewById(R.id.tv_introduction_title);
		tv_content = (TextView) layout.findViewById(R.id.tv_introduction_content);
		Intent intent = getActivity().getIntent();
		JSONPATH = intent.getStringExtra("json_lesson_path");
		String title = intent.getStringExtra("title");
		readfromlocal(JSONPATH);
		filedownload();
		if(title!=null){
			tv_title.setText(title);
		}
		return layout;
	}

	private void filedownload() {
		FiledownTask filedownTask = new FiledownTask();
		jsonname = randomname[(int)(Math.random()*3)];//随机
		filedownTask.execute(JSONPATH+jsonname,LOCALPATH);
	}

//本地读取缓存
private void readfromlocal(String filepath){
//	long start = System.currentTimeMillis();
	hasNet = false;
		//把文件读取成utf-8格式的String
		String jsonstr  = FileUtil.file2string(filepath);
		tv_content.setText(jsonstr);
}
	class FiledownTask extends AbstractFileAsynctask{
		@Override
		protected void onPostExecute(String result) {
			tv_content.setText(result);
			super.onPostExecute(result);
		}
	}

}
