package com.cp.duobei.fragment.login;

import com.cp.duobei.R;
import com.cp.duobei.activity.MainActivity;
import com.cp.duobei.fragment.AbstractFragment;
import com.cp.duobei.utils.DbManager;
import com.cp.duobei.utils.SqlUtils;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class LoginFragment extends AbstractFragment implements OnClickListener {

	private SQLiteDatabase mDB;
	private EditText ed_username;
	private EditText ed_password;
	private CheckBox mCheckBox;
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("LoginFragment");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("LoginFragment");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//连接用户数据库
//		SqlUtils sqlUtils = new SqlUtils(getActivity());
//		mDB = sqlUtils.getReadableDatabase();
		mDB = DbManager.getInstance(getActivity()).getDB();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_login, container, false);
		ed_username = (EditText) layout.findViewById(R.id.et_username);
		ed_password = (EditText) layout.findViewById(R.id.et_password);
		mCheckBox = (CheckBox) layout.findViewById(R.id.ckb_login);
		layout.findViewById(R.id.btn_login).setOnClickListener(this);
		return layout;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			String username = ed_username.getText().toString().trim();
			Cursor query = mDB.query("user", null, "username=?", new String[]{username}, null, null, null);
			if(query.moveToFirst()){
				if(ed_password.getText().toString().trim().equals(query.getString(query.getColumnIndex("password")))){
					Toast.makeText(getActivity(), "登陆成功", 0).show();
					SharedPreferences sp = getActivity().getSharedPreferences("userinfo", 0);
//					SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
					Editor edit = sp.edit();
					edit.putBoolean("autologin", mCheckBox.isChecked());
					edit.putString("username", username);
					edit.commit();
					FragmentActivity activity = getActivity();
					if(activity instanceof MainActivity){
						((MainActivity) activity).setLoginedFragment(username);
					}
				}else{
					Toast.makeText(getActivity(), "密码错误", 0).show();
				}
			}else{
				Toast.makeText(getActivity(), "账号不存在", 0).show();
			}
			break;

		default:
			break;
		}
	}
	@Override
	public void refresh() {
		Toast.makeText(getActivity(), "登陆界面刷新", 0).show();
		ed_username.setText("");
		ed_password.setText("");
	}

}
