package com.cp.duobei.activity;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.Type;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

import com.cp.duobei.R;
import com.cp.duobei.activity.SettingAdapter;
import com.cp.duobei.fragment.group.GroupFindFragment;
import com.cp.duobei.fragment.group.TopicHotFragment;
import com.cp.duobei.fragment.home.DailyRecFragment;
import com.cp.duobei.fragment.home.HomeFragment;
import com.cp.duobei.fragment.home.NewCourseFragment;
import com.cp.duobei.fragment.home.PickCourseFragment;
import com.cp.duobei.fragment.home.RecentlyFragment;
import com.cp.duobei.fragment.login.LoginFragment;
import com.cp.duobei.fragment.login.MyCourseFragment;
import com.cp.duobei.fragment.login.MyInfoFragment;
import com.cp.duobei.fragment.login.RegistFragment;
import com.cp.duobei.fragment.publiccourse.PublicCourseListFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * actionbar+menudrawer
 * 初始化homefragment
 * 点击menudrawer菜单,切换fragment	
 *
 */
public class MainActivity extends SherlockFragmentActivity implements SearchView.OnQueryTextListener,SearchView.OnSuggestionListener, OnItemClickListener{

	private MenuDrawer mDrawer;
	private ListView mListView;
	private static final int MENU_LOGIN = 0;
	private static final int MENU_HOME = 1;
	private static final int MENU_GROUP = 2;
	private static final int MENU_COURSE = 3;
	private static final int MENU_SETTING = 4;
	private HomeFragment mhomeFragment;
	private boolean autologin;
	private String username = null;//用户名以及登陆状态
	private SettingAdapter settingAdapter;
	private FragmentManager fm;
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( "MainActivity" );
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( "MainActivity"  );
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		new UpgradeManager(this).checkandupdate();
		MobclickAgent.updateOnlineConfig( this );
		MobclickAgent.openActivityDurationTrack(false);
		initContentview();
		initMenuview();
	}
	/**
	 * 主界面初始化
	 */
	public void initContentview() {
		//actionbar
		ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setIcon(R.drawable.ic_duobei);
		supportActionBar.setTitle("多贝公开课");
		//menudrawer
		mDrawer = MenuDrawer.attach(this,Type.OVERLAY);
		mDrawer.setContentView(R.layout.activity_main);
		fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		mhomeFragment = new HomeFragment();
		mhomeFragment.addpager("新课速递",new NewCourseFragment());
		mhomeFragment.addpager("每日推荐",new DailyRecFragment());
		mhomeFragment.addpager("精选课程",new PickCourseFragment());
		ft.add(R.id.container, mhomeFragment);
		ft.commit();
	}
	/**
	 * 侧滑菜单初始化
	 * 
	 */
	public void initMenuview() {
		View settingview = getLayoutInflater().inflate(R.layout.main_setting,null);
		mDrawer.setMenuView(settingview);
		//menudrawer的侧滑界面的listview
		mListView = (ListView) settingview.findViewById(R.id.listView1);
		String[] mTitle = getResources().getStringArray(R.array.menu_str);
		int images[] = new int[]{R.drawable.ic_menu_login,R.drawable.ic_menu_home,
				R.drawable.ic_menu_group,R.drawable.ic_menu_public,R.drawable.ic_menu_setting};
		/**用户登陆判断**/
		SharedPreferences sp = getPreferences(MODE_PRIVATE);
		//自动登陆
		autologin = sp.getBoolean("autologin", false);
		if(autologin){
			username = sp.getString("username", "cpoopc");
		}
		//用户名
		if(username!=null){
			mTitle[0] = username;
		}
		settingAdapter = new SettingAdapter(this,mTitle,images);
		mListView.setAdapter(settingAdapter); 
		mListView.setOnItemClickListener(this);
//		mDrawer.setDropShadowColor(Color.BLUE);
		mDrawer.setSlideDrawable(R.drawable.ic_setting);
		mDrawer.setDrawerIndicatorEnabled(true);
		mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		mDrawer.setMenuSize(250);
		//第一次使用提示可以侧滑
		boolean firstUse = sp.getBoolean("firstUse", true);
		if(firstUse){
			mDrawer.peekDrawer(1000, 0);
			Editor edit = sp.edit();
			edit.putBoolean("firstUse", false);
			edit.commit();
		}
	}

	/**
	 * 
	 */
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			mDrawer.toggleMenu();
			break;
		case R.id.action_refresh:
			mhomeFragment.refresh();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
		initSearch(menu);
		return true;
	}
/**
 * actionbar搜索接口
*	
 */
	private void initSearch(Menu menu) {
		//TODO
		//Create the search view
		SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("查找课表");
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);
        menu.add("Search")
            .setIcon(R.drawable.abs__ic_search)
            .setActionView(searchView)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
	}

	@Override
	public boolean onSuggestionSelect(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSuggestionClick(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		Toast.makeText(MainActivity.this, "查询:"+query, Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}
	/**
	 * 点击切换fragment
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Fragment fragment = null;
		switch (position) {
		case MENU_LOGIN:
			if(username!=null){
				mhomeFragment = new HomeFragment();
				fragment = mhomeFragment;
				mhomeFragment.addpager("我的课表", new MyCourseFragment());
				mhomeFragment.addpager("我的资料", new MyInfoFragment());
			}else{
				mhomeFragment = new HomeFragment();
				fragment = mhomeFragment;
				mhomeFragment.addpager("登陆", new LoginFragment());
				mhomeFragment.addpager("注册", new RegistFragment());
			}
			break;
		case MENU_HOME:
			mhomeFragment = new HomeFragment();
			fragment = mhomeFragment;
			mhomeFragment.addpager("新课速递",new NewCourseFragment());
			mhomeFragment.addpager("每日推荐",new DailyRecFragment());
			mhomeFragment.addpager("精选课程",new PickCourseFragment());
			break;
		case MENU_GROUP:
			mhomeFragment = new HomeFragment();
			fragment = mhomeFragment;
			mhomeFragment.addpager("热门话题",new TopicHotFragment());
			mhomeFragment.addpager("发现小组",new GroupFindFragment());
//			((HomeFragment)fragment).addpager("", "");
			break;
		case MENU_COURSE:
			mhomeFragment = new HomeFragment();
			fragment = mhomeFragment;
			mhomeFragment.addpager("发现课程",new PublicCourseListFragment());
			mhomeFragment.addpager("近期预告",new DailyRecFragment());
			mhomeFragment.addpager("精选",new RecentlyFragment());
			break;
		case MENU_SETTING:
			startActivity(new Intent(this,SettingActivity.class));
			break;
		default:
			break;
		}
		if(fragment!=null){
//			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.container, fragment);
			ft.commit();
		}
		mDrawer.closeMenu(true);
	}
	/**
	 * 提供给fragment设置menudrawer触摸模式
	 */
	public void setMenuDrawerEnable(boolean enable) {
		if(enable){
			mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		}else{
			mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
			
		}
	}
	/**
	 * 设置登陆界面
	 * 已登陆:我的课表+我的资料
	 * 未登陆:登陆+注册
	 * 登陆状态改变时更新menudrawer的菜单显示
	 */
	public void setLoginedFragment(String username){
		this.username = username;
		mhomeFragment = new HomeFragment();
		if(username!=null){
			mhomeFragment.addpager("我的课表", new MyCourseFragment());
			mhomeFragment.addpager("我的资料", new MyInfoFragment());
			settingAdapter.mTitle[0] = username;
		}else{
			mhomeFragment.addpager("登陆", new LoginFragment());
			mhomeFragment.addpager("注册", new RegistFragment());
			SharedPreferences sp = getPreferences(MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putBoolean("autologin", false);
			edit.commit();
			autologin = false;
			settingAdapter.mTitle[0] = "登陆";
		}
		settingAdapter.notifyDataSetChanged();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.container, mhomeFragment);
		ft.commit();
	}
}
