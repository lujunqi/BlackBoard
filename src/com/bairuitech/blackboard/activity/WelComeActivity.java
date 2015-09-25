package com.bairuitech.blackboard.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.widget.OnViewChangeListener;
import com.bairuitech.blackboard.widget.ScrollLayout;

/**
 * 欢迎页
 */

public class WelComeActivity extends Activity implements OnViewChangeListener {
	private ScrollLayout mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private Button startBtn;
	private LinearLayout pointLLayout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		WelComeActivity.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		init();
	}

	private void init() {
		mScrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayout);
		pointLLayout = (LinearLayout) findViewById(R.id.llayout);
		startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(onClick);
		count = mScrollLayout.getChildCount();
		imgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			imgs[i] = (ImageView) pointLLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);
	}

	private View.OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.startBtn:
				mScrollLayout.setVisibility(View.GONE);
				pointLLayout.setVisibility(View.GONE);
				SharedPreferences sp = getSharedPreferences("public",
						Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("welcome", "ok");
				editor.commit();
				Intent intent = new Intent(WelComeActivity.this,
						LoginActivity.class);
				WelComeActivity.this.startActivity(intent);
				WelComeActivity.this.finish();
			}
		}
	};

	@Override
	public void OnViewChange(int position) {
		if (position < 0 || position > count - 1 || currentItem == position) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;

	}
}
