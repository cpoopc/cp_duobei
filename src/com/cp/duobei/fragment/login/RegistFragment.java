package com.cp.duobei.fragment.login;

import com.cp.duobei.R;
import com.cp.duobei.fragment.AbstractFragment;
import com.cp.duobei.utils.SqlUtils;
import com.umeng.analytics.MobclickAgent;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistFragment extends AbstractFragment implements OnClickListener {

	private SQLiteDatabase mDB;
	private EditText ed_username;
	private EditText ed_password;
	private EditText ed_comfirm;

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("RegistFragment");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("RegistFragment");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SqlUtils sqlUtils = new SqlUtils(getActivity());
		mDB = sqlUtils.getReadableDatabase();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_regist, container, false);
		layout.findViewById(R.id.btn_regist).setOnClickListener(this);
		ed_username = (EditText) layout.findViewById(R.id.et_username);
		ed_password = (EditText) layout.findViewById(R.id.et_password);
		ed_comfirm = (EditText) layout.findViewById(R.id.et_comfirm);
		return layout;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_regist:
			btn_regist();
			break;

		default:
			break;
		}
		
	}

	private void btn_regist() {
		String username = ed_username.getText().toString().trim();
		String password = ed_password.getText().toString().trim();
		if(username.equals("")||password.equals("")){
			Toast.makeText(getActivity(), "账号密码不能为空", 0).show();
		}else{
			if(password.equals(ed_comfirm.getText().toString().trim())){
				ContentValues values = new ContentValues();
				values.put("username", username);
				values.put("password", password);
				try {
					mDB.insert("user", null, values);
					Toast.makeText(getActivity(), "注册成功!", 0).show();
				} catch (Exception e) {
					Toast.makeText(getActivity(), "用户已存在", 0).show();
				}
			}else{
				Toast.makeText(getActivity(), "请确认密码", 0).show();
			}
		}
	}
	@Override
	public void refresh() {
		Toast.makeText(getActivity(), "注册界面刷新", 0).show();
		ed_username.setText("");
		ed_password.setText("");
		ed_comfirm.setText("");
	}

}
