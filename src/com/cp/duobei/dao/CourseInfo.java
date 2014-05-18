package com.cp.duobei.dao;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CourseInfo {
	public String imagepath;
	public String title;
	public String author;
	public String detailpage;
	//从cursor读取信息
	public CourseInfo readCursor(Cursor cursor){
		CourseInfo info = new CourseInfo();
		info.imagepath = cursor.getString(cursor.getColumnIndex("imagepath"));
		info.title = cursor.getString(cursor.getColumnIndex("title"));
		info.author = cursor.getString(cursor.getColumnIndex("author"));
		info.detailpage = cursor.getString(cursor.getColumnIndex("detailpage"));
		return info;
	}
	//写入数据库
	public void write2DB(SQLiteDatabase mDb,CourseInfo info,String table){
		ContentValues values  = new ContentValues();
		values.put("imagepath", info.imagepath);
		values.put("title", info.title);
		values.put("author", info.author);
		values.put("detailpage", info.detailpage);
		mDb.insert(table, null, values);
	}
	//从cursor读取所有信息
	public List<CourseInfo> readCursor2List(Cursor cursor,List<CourseInfo> list){
		boolean toFirst = cursor.moveToFirst();
		CourseInfo info = new CourseInfo();
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
	public void writeList2DB(SQLiteDatabase mDb,List<CourseInfo> list,String table){
		CourseInfo info;
		for (int i = 0; i < list.size(); i++) {
			info = list.get(i);
			write2DB(mDb, info, table);
		}
	}
	//读取jsonarray
	public List<CourseInfo> readJsonArray(JSONArray jsonArray,List<CourseInfo> list) throws JSONException{
		JSONObject jsonObject;
		for (int i = 0; i < jsonArray.length(); i++) {
			CourseInfo info = new CourseInfo();
			jsonObject = jsonArray.getJSONObject(i);
			info.imagepath = jsonObject.getString("imagepath");
			info.title = jsonObject.getString("title");
			info.author= jsonObject.getString("author");
			info.detailpage= jsonObject.getString("detailpage");
			list.add(info);
		}
		return list;
	}
	public CourseInfo() {
		super();
	}
	public CourseInfo(String imagepath, String title, String author) {
		super();
		this.imagepath = imagepath;
		this.title = title;
		this.author = author;
	}
	public String getImagepath() {
		return imagepath;
	}
	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	@Override
	public String toString() {
		return "CourseInfo [imagepath=" + imagepath + ", title=" + title
				+ ", author=" + author + ", detailpage=" + detailpage + "]";
	}
	
}
