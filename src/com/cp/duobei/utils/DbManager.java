package com.cp.duobei.utils;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {
	private SQLiteDatabase  mDb;
	private static  DbManager dbManager;
	private DbManager(Context context){
		SqlUtils sqlUtils = new SqlUtils(context);
		mDb = sqlUtils.getReadableDatabase();
	}
	public static DbManager getInstance(Context context){
		if(dbManager==null){
			synchronized (DbManager.class) {
				if(dbManager==null){
				dbManager = new DbManager(context);
				}
			}
		}
		return dbManager;
	}
	public SQLiteDatabase getDB(){
		return mDb;
	}
	public Cursor query(String table,String selection,String[] selectionArgs){
//		mDb.query(table, columns, selectionArgs, selectionArgs, groupBy, having, orderBy)
		Cursor query = mDb.query(table, null, selection, selectionArgs, null, null, null); 
		return query;
	}
	//插入数据到我的课表
	public void insert(String table,String username,String coursetitle,String imagepath){
		ContentValues values = new ContentValues();
		values.put("username", username);
		values.put("coursetitle", coursetitle);
		values.put("imagepath", imagepath);
		mDb.insert(table, null, values );
	}
}
