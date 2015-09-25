package com.bairuitech.blackboard.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.bairuitech.blackboard.R;

/**
 * 学生 黑板
 * 
 * 
 */
public class StudentEWhiteBoardActivity extends Activity {
	 public static  Context context;
	private static final String TAG = "StudentEWhiteBoardActivity";

	// private MyScrollView myscroll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stu_ewhiteboard);
		context = this;
		initView();
		initLogic();

	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initView() {
//		addCallStateListener();

	}

	private void initLogic() {
		// myscroll.touch = true;
	}

	public void edit(View v) {
		// view_paint.edit(myColor);
	}

	public void eraser(View v) {
		// view_paint.eraser();
	}

	public void color(View v) {

	}

	public void clearBtn(View v) {
		// view_paint.clear();
	}

	public void back(View v) {
		((Activity) context).finish();
	}

	
}