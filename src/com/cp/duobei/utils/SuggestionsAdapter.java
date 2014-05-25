package com.cp.duobei.utils;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SuggestionsAdapter extends CursorAdapter {
	private Context context;
	private String username = "";
	public void setUsername(String name){
		username = name;
	}
    public SuggestionsAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.context = context;
    }
//    @Override
//    public FilterQueryProvider getFilterQueryProvider() {
//    	Log.e("cursoradpter", "getFilterQueryProvider");
//    	return super.getFilterQueryProvider();
//    }
//    @Override
//    public void setFilterQueryProvider(
//    		FilterQueryProvider filterQueryProvider) {
//    	Log.e("cursoradpter", "setFilterQueryProvider");
//    	// TODO Auto-generated method stub
//    	super.setFilterQueryProvider(filterQueryProvider);
//    }
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
//    	Log.e("cursoradpter", "runQueryOnBackgroundThread"+constraint);
    	if(getFilterQueryProvider()!=null){
//    		Log.e("cursoradpter", "getFilterQueryProvider()!=null");
    		return getFilterQueryProvider().runQuery(constraint);
    	}else{
    		if("".equals(constraint)||constraint==null){
    			return null;
    		}
    		resultList.clear();
    		return DbManager.getInstance(context).rawquery(username ,constraint.toString());
    	}
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return v;
    }
    private ArrayList<String> resultList;
    public void setResultList(ArrayList<String> list){
    	resultList = list;
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view;
        tv.setTextColor(Color.BLUE);
        final int textIndex = cursor.getColumnIndex("coursetitle");
        tv.setText(cursor.getString(textIndex));
        resultList.add(cursor.getString(textIndex));
    }
}
