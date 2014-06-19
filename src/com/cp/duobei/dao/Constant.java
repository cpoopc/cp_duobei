package com.cp.duobei.dao;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.cp.duobei.activity.MyApplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.preference.PreferenceManager;

public interface Constant {
	//sd卡地址
	File sdPath = Environment.getExternalStorageDirectory();
	//数据保存文件夹
	File STORE_PATH = new File(sdPath+"/cp/com.duobei.cp/");
	File STORE_PATH_UIL = new File(sdPath+"/cp/com.duobei.cp/uil/");
	//基本地址
//	String PATH = "http://192.168.1.100:8080/cpdata";
//	String PATH = "http://cpduobei.qiniudn.com";
	String PATH = "http://cpduobeinew.qiniudn.com";
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
	String POST_LIST = "http://bbs.51cto.com/51bbsclient.php?do=hot&page=";
//	String POST_LIST = "http://bbs.51cto.com/51bbsclient.php?do=rec&page=";
	String POST_DETAIL = "http://bbs.51cto.com/51bbsclient.php?do=threadinfo&tid=";
	//发现小组
	String PATH_GROUP = PATH + "/group";
	String JSON_GROUP= PATH_GROUP + "/group&page=";
//	 if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
//         ThemeManager.modify(ThemeManager.FULLSCREEN);
//     }
	String CONNECT_ERRO = "网络连接异常!";
	String  LESSONINFO_BASE= "http://cpduobei.qiniudn.com/lessoninfo/";
	//解决异步任务阻塞
	static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	static final int KEEP_ALIVE = 1;
    static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);
    static final ThreadFactory sThreadFactory = new ThreadFactory() {
    private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };
	static final Executor THREAD_POOL_EXECUTOR
    = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
}
