package com.cp.duobei.dao;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GroupInfo {
	public String imagepath;
	public String title;
	public String members;
	public String introduce;
	public String detailpage;
	//从cursor读取信息
	public GroupInfo readCursor(Cursor cursor){
		GroupInfo info = new GroupInfo();
		info.imagepath = cursor.getString(cursor.getColumnIndex("imagepath"));
		info.title = cursor.getString(cursor.getColumnIndex("title"));
		info.members = cursor.getString(cursor.getColumnIndex("members"));
		info.introduce = cursor.getString(cursor.getColumnIndex("introduce"));
		info.detailpage = cursor.getString(cursor.getColumnIndex("detailpage"));
		return info;
	}
	//写入数据库
	public void write2DB(SQLiteDatabase mDb,GroupInfo info,String table){
		ContentValues values  = new ContentValues();
		values.put("imagepath", info.imagepath);
		values.put("title", info.title);
		values.put("members", info.members);
		values.put("introduce", info.introduce);
		values.put("detailpage", info.detailpage);
		mDb.insert(table, null, values);
	}
	//从cursor读取所有信息
	public List<GroupInfo> readCursor2List(Cursor cursor,List<GroupInfo> list){
		boolean toFirst = cursor.moveToFirst();
		GroupInfo info = new GroupInfo();
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
	public void writeList2DB(SQLiteDatabase mDb,List<GroupInfo> list,String table){
		GroupInfo info;
		for (int i = 0; i < list.size(); i++) {
			info = list.get(i);
			write2DB(mDb, info, table);
		}
	}
	//读取jsonarray
	public List<GroupInfo> readJsonArray(JSONArray jsonArray,List<GroupInfo> list) throws JSONException{
		JSONObject jsonObject;
		for (int i = 0; i < jsonArray.length(); i++) {
			GroupInfo info = new GroupInfo();
			jsonObject = jsonArray.getJSONObject(i);
			info.imagepath = jsonObject.getString("imagepath");
			info.title = jsonObject.getString("title");
			info.members = jsonObject.getString("members");
			info.introduce= jsonObject.getString("introduce");
//			info.detailpage= jsonObject.getString("detailpage");
			list.add(info);
		}
		return list;
	}
	public GroupInfo() {
		super();
	}
	public GroupInfo(String imagepath, String title,String members,String introduce) {
		super();
		this.imagepath = imagepath;
		this.title = title;
		this.members = members;
		this.introduce = introduce;
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
	public String getMembers() {
		return members;
	}
	public void setMembers(String members) {
		this.members = members;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getDetailpage() {
		return detailpage;
	}
	public void setDetailpage(String detailpage) {
		this.detailpage = detailpage;
	}
	public String getAuthor() {
		return introduce;
	}
	public void setAuthor(String introduce) {
		this.introduce = introduce;
	}
	@Override
	public String toString() {
		return "GroupInfo [imagepath=" + imagepath + ", title=" + title
				+ ", members=" + members + ", introduce=" + introduce
				+ ", detailpage=" + detailpage + "]";
	}
}
