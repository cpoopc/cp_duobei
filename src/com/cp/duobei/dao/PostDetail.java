package com.cp.duobei.dao;

public class PostDetail {
//	public int pid;//回帖id
//	public int fid;//分类id
//	public int tid;//主贴id
	public int first;//是否为主贴（1是主贴，0为回帖）
	public String author;//作者
//	public int authorid;//作者id
	public String subject;//标题（如果主贴有标题）
	public String dateline;//时间 字符串
	public int dbdateline;//时间 整型
	public String message;//内容

}
