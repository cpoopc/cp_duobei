package com.cp.duobei.activity;

import net.simonvt.menudrawer.MenuDrawer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.cp.duobei.R;
import com.cp.duobei.fragment.coursedetail.CourseDetailFragment;
import com.cp.duobei.utils.SwipeBackSherlockFragmentActivity;
public class CourseDetailActivity extends SwipeBackSherlockFragmentActivity implements SearchView.OnQueryTextListener,SearchView.OnSuggestionListener{

	private MenuDrawer mDrawer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionbar();
		initContentview();
	}

	private void initContentview() {
		setContentView(R.layout.activity_coursedetail);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = new CourseDetailFragment();
		ft.add(R.id.container, fragment);
		ft.commit();
	}


	private void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		actionBar.setTitle(title);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

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

	private void initSearch(Menu menu) {
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
		Toast.makeText(CourseDetailActivity.this, "查询:"+query,Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		Fragment fragment = null;
//		switch (position) {
//		case MENU_LOGIN:
//			break;
//		case MENU_HOME:
//			fragment = new HomeFragment();
//			break;
//		case MENU_GROUP:
//			fragment = new GroupFragment();
//			break;
//		case MENU_COURSE:
//			fragment = new CourseFragment();
//			break;
//		case MENU_SETTING:
//			break;
//		default:
//			break;
//		}
//		if(fragment!=null){
//			FragmentManager fm = getSupportFragmentManager();
//			FragmentTransaction ft = fm.beginTransaction();
//			ft.replace(R.id.container, fragment);
//			ft.commit();
//		}
//		mDrawer.closeMenu(true);
//	}

	public void setMenuDrawerEnable(boolean enable) {
		if(enable){
			mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		}else{
			mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
			
		}
	}

}
