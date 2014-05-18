package com.cp.duobei.utils;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlUtils extends SQLiteOpenHelper {
	public SqlUtils(Context context) {
		super(context, "duobei", null, 1);
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//改成不用数据库
//		//newcourse
//		String sql1 = "CREATE TABLE newcourse(imagepath VARCHAR(50),title VARCHAR(20),author VARCHAR(20),detailpage VARCHAR(20))";
//		db.execSQL(sql1);
		//lessonlist
		String sql2 = "CREATE TABLE lessonlist(num VARCHAR(20),url VARCHAR(20),title VARCHAR(20),learnning VARCHAR(20))";
		db.execSQL(sql2);
//		//courserec
//		String sql3 = "CREATE TABLE courserec(imagepath VARCHAR(50),title VARCHAR(20),author VARCHAR(20),detailpage VARCHAR(20),good VARCHAR(20))";
//		db.execSQL(sql3);
//		//recently
//		String sql4 = "CREATE TABLE recently(title VARCHAR(20),author VARCHAR(20),content VARCHAR(20))";
//		db.execSQL(sql4);
		//user
		String sql5 = "CREATE TABLE user(username VARCHAR(20) PRIMARY KEY,password VARCHAR(20))";
		db.execSQL(sql5);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
