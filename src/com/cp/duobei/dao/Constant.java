package com.cp.duobei.dao;

import java.io.File;


import android.os.Environment;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

public interface Constant {
	//sd卡地址
	File sdPath = Environment.getExternalStorageDirectory();
	//数据保存文件夹
	File STORE_PATH = new File(sdPath+"/cp/com.duobei.cp");
	//基本地址
//	String PATH = "http://192.168.56.1:8080/cpdata";
	String PATH = "http://cpduobei.qiniudn.com";
	//检查升级地址
	String PATH_UPDATE = PATH+"/update/update.json";
	//我的课表
	String PATH_MYCOURSE = PATH + "/myhome";
	String JSON_MYCOURSE = PATH_MYCOURSE + "/mycourse.txt";
	//新课速递
	String PATH_NEWCOURSE = PATH + "/newcourse";
	String JSON_NEWCOURSE = PATH_NEWCOURSE + "/newcourse.txt";
	//每日推荐
	String PATH_DAILYREC = PATH + "/dailyrec";
	String JSON_DAILYREC= PATH_DAILYREC + "/dailyrec.txt";
	//精选课程
	String PATH_PICKCOURSE = PATH + "/pickcourse";
	String JSON_PICKCOURSE = PATH_PICKCOURSE + "/pickcourse.txt";
	//推荐老师
	String PATH_PICKTEACHER = PATH + "/pickcourse";
	String JSON_PICKTEACHER = PATH_PICKTEACHER + "/pickteacher.txt";
	//近期更新
	String PATH_RECENTLY = PATH + "/recently";
	String JSON_RECENTLY= PATH_RECENTLY + "/recently.txt";
	//公开课
	String PATH_PUBLICCOURSE = PATH + "/publiccourse";
	String JSON_PUBLICCOURSE= PATH_PUBLICCOURSE + "/publiccourse.txt";
	//51cto精华帖和推荐的内容
	String POST_LIST = "http://bbs.51cto.com/51bbsclient.php?do=rec&page=";
	String POST_DETAIL = "http://bbs.51cto.com/51bbsclient.php?do=threadinfo&tid=";
	//发现小组
	String PATH_GROUP = PATH + "/group";
	String JSON_GROUP= PATH_GROUP + "/group&page=";
//	 if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
//         ThemeManager.modify(ThemeManager.FULLSCREEN);
//     }
}
