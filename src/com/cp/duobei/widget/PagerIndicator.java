package com.cp.duobei.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

public class PagerIndicator extends ImageView{
	private Paint bgPaint;
	private Paint circlePaint;
	private float cx = 25;
	private float cy = 18;
	private float radius = 10;
	private float length = 3*radius;
//	private float ra = 3*radius;
	private float shift = 0;
	private int count = 1;
	public void initCount(int count){
		this.count = count;
	}
	public void setIndicator(int pager,float percent){
		pager %= count;
		shift = (pager+percent)*length;
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < count; i++) {
			canvas.drawCircle(cx + i*3*radius, cy, radius, bgPaint);
		}
		canvas.drawCircle(cx+shift, cy, radius+2, circlePaint);
	}
	public PagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}
	private void initPaint() {
		bgPaint = new Paint();
		bgPaint.setColor(Color.WHITE);
		bgPaint.setStyle(Paint.Style.STROKE);
		bgPaint.setStrokeWidth(2);
		bgPaint.setAntiAlias(true);
		circlePaint = new Paint();
		circlePaint.setColor(Color.WHITE);
		circlePaint.setAntiAlias(true);
		
	}
//	@Override 
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
//		// 这里要计算一下控件的实际大小，然后调用setMeasuredDimension来设置
//	  int width = this.getMeasuredSize(widthMeasureSpec, true);
//	  int height = this.getMeasuredSize(heightMeasureSpec, false);
//	  setMeasuredDimension(width, height);
//	} 

	/**
		* 计算控件的实际大小
		* @param length onMeasure方法的参数，widthMeasureSpec或者heightMeasureSpec
		* @param isWidth 是宽度还是高度
		* @return int 计算后的实际大小
		*/
	private int getMeasuredSize(int length, boolean isWidth){
		// 模式
		int specMode = MeasureSpec.getMode(length);
		// 尺寸
		int specSize = MeasureSpec.getSize(length);
		// 计算所得的实际尺寸，要被返回
		int retSize = 0;        
		// 得到两侧的padding（留边）
		int padding = (isWidth? getPaddingLeft()+getPaddingRight():getPaddingTop()+getPaddingBottom());
	        
		// 对不同的指定模式进行判断
		if(specMode==MeasureSpec.EXACTLY){  // 显式指定大小，如40dp或fill_parent
			retSize = specSize;
		}else{                              // 如使用wrap_content
			retSize = (int) (isWidth? 14*radius: 4*radius);
			if(specMode==MeasureSpec.UNSPECIFIED){
			retSize = Math.min(retSize, specSize);
			}
		}        

		return retSize;
	}

}
