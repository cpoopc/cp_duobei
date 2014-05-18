package com.cp.duobei.dao;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LessonInfo {
	public String num;
	public String url;//视频地址
	public String title;
	public String learnning;//学习人数
	//从cursor读取信息
	public LessonInfo readCursor(Cursor cursor){
		LessonInfo info = new LessonInfo();
		info.num = cursor.getString(cursor.getColumnIndex("num"));
		info.url = cursor.getString(cursor.getColumnIndex("url"));
		info.title = cursor.getString(cursor.getColumnIndex("title"));
		info.learnning = cursor.getString(cursor.getColumnIndex("learnning"));
		return info;
	}
	//写入数据库
	public void write2DB(SQLiteDatabase mDb,LessonInfo info,String table){
		ContentValues values  = new ContentValues();
		values.put("num", info.num);
		values.put("url", info.url);
		values.put("title", info.title);
		values.put("learnning", info.learnning);
		mDb.insert(table, null, values);
	}
	//从cursor读取所有信息
	public List<LessonInfo> readCursor2List(Cursor cursor,List<LessonInfo> list){
		boolean toFirst = cursor.moveToFirst();
		LessonInfo info = new LessonInfo();
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
	public void writeList2DB(SQLiteDatabase mDb,List<LessonInfo> list,String table){
		LessonInfo info;
		for (int i = 0; i < list.size(); i++) {
			info = list.get(i);
			write2DB(mDb, info, table);
		}
	}
	public List<LessonInfo> readJsonArray(JSONArray jsonArray,List<LessonInfo> list) throws JSONException{
		JSONObject jsonObject;
		for (int i = 0; i < jsonArray.length(); i++) {
			LessonInfo info = new LessonInfo();
			jsonObject = jsonArray.getJSONObject(i);
			info.num = jsonObject.getString("num");
			info.url = jsonObject.getString("url");
			info.title = jsonObject.getString("title");
			info.learnning = jsonObject.getString("learnning");
			list.add(info);
		}
		return list;
	}
	public LessonInfo() {
		super();
	}
	public LessonInfo(String num, String url, String title, String learnning) {
		super();
		this.num = num;
		this.url = url;
		this.title = title;
		this.learnning = learnning;
	}
	@Override
	public String toString() {
		return "LessonInfo [num=" + num + ", url=" + url + ", title=" + title
				+ ", learnning=" + learnning + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((learnning == null) ? 0 : learnning.hashCode());
		result = prime * result + ((num == null) ? 0 : num.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LessonInfo other = (LessonInfo) obj;
		if (learnning == null) {
			if (other.learnning != null)
				return false;
		} else if (!learnning.equals(other.learnning))
			return false;
		if (num == null) {
			if (other.num != null)
				return false;
		} else if (!num.equals(other.num))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
}
