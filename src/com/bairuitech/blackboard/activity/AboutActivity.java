package com.bairuitech.blackboard.activity;
import com.bairuitech.blackboard.R;

/**
 * 关于
 */
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class AboutActivity extends Activity {
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);
		context = this;
	}
	
	public void back(View v) {
		((Activity) context).finish();
	}	
}
