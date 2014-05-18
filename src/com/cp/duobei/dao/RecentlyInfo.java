package com.cp.duobei.dao;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RecentlyInfo {
	public String title;
	public String author;
	public String content;
	//从cursor读取信息
	public RecentlyInfo readCursor(Cursor cursor){
		RecentlyInfo info = new RecentlyInfo();
		info.title = cursor.getString(cursor.getColumnIndex("title"));
		info.author = cursor.getString(cursor.getColumnIndex("author"));
		info.content = cursor.getString(cursor.getColumnIndex("content"));
		return info;
	}
	//写入数据库
	public void write2DB(SQLiteDatabase mDb,RecentlyInfo info,String table){
		ContentValues values  = new ContentValues();
		values.put("title", info.title);
		values.put("author", info.author);
		values.put("content", info.content);
		mDb.insert(table, null, values);
	}
	//从cursor读取所有信息
	public List<RecentlyInfo> readCursor2List(Cursor cursor,List<RecentlyInfo> list){
		boolean toFirst = cursor.moveToFirst();
		RecentlyInfo info = new RecentlyInfo();
		while(toFirst){
			info = info.readCursor(cursor);
			list.add(info);
			toFirst = cursor.moveToNext();
		}
		return list;
	}
	/**
	 * 清空数据库后,把容器内的CourseInfo写入数据库
	 * @param list
	 *  
	 */
	public void writeList2DB(SQLiteDatabase mDb,List<RecentlyInfo> list,String table){
		RecentlyInfo info;
		for (int i = 0; i < list.size(); i++) {
			info = list.get(i);
			write2DB(mDb, info, table);
		}
	}
	//读取jsonarray
	public List<RecentlyInfo> readJsonArray(JSONArray jsonArray,List<RecentlyInfo> list) throws JSONException{
		JSONObject jsonObject;
		for (int i = 0; i < jsonArray.length(); i++) {
			RecentlyInfo info = new RecentlyInfo();
			jsonObject = jsonArray.getJSONObject(i);
			info.title = jsonObject.getString("title");
			info.author= jsonObject.getString("author");
			info.content= jsonObject.getString("content");
			list.add(info);
		}
		return list;
	}
	public RecentlyInfo() {
		super();
	}
	public RecentlyInfo(String title, String author, String content) {
		super();
		this.title = title;
		this.author = author;
		this.content = content;
	}
	@Override
	public String toString() {
		return "RecentlyInfo [title=" + title + ", author=" + author
				+ ", content=" + content + "]";
	}

	
}
