package com.bairuitech.blackboard.activity;

/**
 * 学生找老师界面
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.widget.FindTeacher;
import com.bairuitech.blackboard.widget.KanHuiFang;
import com.bairuitech.blackboard.widget.MyCenter;

public class StuFindTeacherActivity extends Activity {
	private RadioGroup rgmain;
	private RelativeLayout tabcontent;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_find_teacher);
		context = this;
		initView();
		initLogic();
	}

	@SuppressLint("CommitTransaction")
	private void initView() {

		rgmain = (RadioGroup) this.findViewById(R.id.rgmain);
		tabcontent = (RelativeLayout) this.findViewById(R.id.tabcontent);

	}

	private void initLogic() {
		tabcontent.removeAllViews();
		tabcontent.addView(new FindTeacher(context));
		RadioButton r = (RadioButton) this.findViewById(R.id.findteacher);
		r.setChecked(true);

		rgmain.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup rg, int arg1) {
				int radioButtonId = rg.getCheckedRadioButtonId();
				switch (radioButtonId) {
				case R.id.findteacher:
					// 找老师
					tabcontent.removeAllViews();
					tabcontent.addView(new FindTeacher(context));
					break;
				case R.id.kanhuifang:
					// 看回放
					tabcontent.removeAllViews();
					tabcontent.addView(new KanHuiFang(context));
					break;
				case R.id.mycenter:
					//个人中心
					tabcontent.removeAllViews();
					tabcontent.addView(new MyCenter(context));
					
				}
			}
		});
	}

}
