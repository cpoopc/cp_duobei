package com.cp.duobei.dao;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CourseRecInfo {
	public String imagepath;
	public String title;
	public String author;
	public String detailpage;
	public String good;
	public CourseRecInfo readCursor(Cursor cursor){
		CourseRecInfo info = new CourseRecInfo();
		info.imagepath = cursor.getString(cursor.getColumnIndex("imagepath"));
		info.title = cursor.getString(cursor.getColumnIndex("title"));
		info.author = cursor.getString(cursor.getColumnIndex("author"));
		info.detailpage = cursor.getString(cursor.getColumnIndex("detailpage"));
		info.good = cursor.getString(cursor.getColumnIndex("good"));
		return info;
	}
	//写入数据库
	public void write2DB(SQLiteDatabase mDb,CourseRecInfo info,String table){
		ContentValues values  = new ContentValues();
		values.put("imagepath", info.imagepath);
		values.put("title", info.title);
		values.put("author", info.author);
		values.put("detailpage", info.detailpage);
		values.put("good", info.good);
		mDb.insert(table, null, values);
	}
	//从cursor读取所有信息
	public List<CourseRecInfo> readCursor2List(Cursor cursor,List<CourseRecInfo> list){
		boolean toFirst = cursor.moveToFirst();
		CourseRecInfo info = new CourseRecInfo();
		while(toFirst){
			info = info.readCursor(cursor);
			list.add(info);
			toFirst = cursor.moveToNext();
		}
		return list;
	}
	/**
	 * 清空数据库后,把容器内的CourseRecInfo写入数据库
	 * @param list
	 *  
	 */
	public void writeList2DB(SQLiteDatabase mDb,List<CourseRecInfo> list,String table){
		CourseRecInfo info;
		for (int i = 0; i < list.size(); i++) {
			info = list.get(i);
			write2DB(mDb, info, table);
		}
	}
	//读取jsonarray
	public List<CourseRecInfo> readJsonArray(JSONArray jsonArray,List<CourseRecInfo> list) throws JSONException{
		JSONObject jsonObject;
		for (int i = 0; i < jsonArray.length(); i++) {
			CourseRecInfo info = new CourseRecInfo();
			jsonObject = jsonArray.getJSONObject(i);
			info.imagepath = jsonObject.getString("imagepath");
			info.title = jsonObject.getString("title");
			info.author= jsonObject.getString("author");
			info.detailpage= jsonObject.getString("detailpage");
			info.good= jsonObject.getString("good");
			list.add(info);
		}
		return list;
	}
	@Override
	public String toString() {
		return "CourseRecInfo [imagepath=" + imagepath + ", title=" + title
				+ ", author=" + author + ", detailpage=" + detailpage
				+ ", good=" + good + "]";
	}
	public CourseRecInfo() {
		super();
	}
	
}
