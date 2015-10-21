package com.bairuitech.blackboard.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bairuitech.blackboard.BlackBoardApplication;
import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.adapter.MyBaseAdapter;
import com.bairuitech.blackboard.adapter.MyViewPagerAdapter;
import com.bairuitech.blackboard.common.CallBack;
import com.bairuitech.blackboard.common.HttpTask;
import com.bairuitech.blackboard.common.HttpUtil;
import com.bairuitech.blackboard.common.JsonUtil;
import com.bairuitech.blackboard.common.Utils;
import com.easemob.chatuidemo.activity.VoiceCallActivity;

public class TeacterMoreActivity extends Activity {
	private Context context;
	private static final String TAG = "TeacterMoreActivity";
	private TextView object;
	private TextView grade;
	private TextView unver;
	private RadioGroup rb_main;
	private ViewPager mPager;
	private View view1, view2, view3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teactermore);
		context = this;
		initView();
		initLogic();
	}

	private void initView() {
		object = (TextView) this.findViewById(R.id.object);
		grade = (TextView) this.findViewById(R.id.grade);
		unver = (TextView) this.findViewById(R.id.unver);
		rb_main = (RadioGroup) this.findViewById(R.id.rb_main);
		mPager = (ViewPager) findViewById(R.id.pager);
	}

	private void initLogic() {
		Intent intent = this.getIntent();
		String s_Grade = intent.getStringExtra("Grade");
		String s_Subject = intent.getStringExtra("Subject");
		String s_NickyName = intent.getStringExtra("NickyName");
		object.setText(s_Subject);
		grade.setText(s_Grade);
		unver.setText(s_NickyName);
		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.view_teacher1, null);// 简介
		view2 = inflater.inflate(R.layout.view_teacher2, null);// 评价
		view3 = inflater.inflate(R.layout.view_teacher3, null);// 精品课

		MyViewPagerAdapter mPagerAdapter = new MyViewPagerAdapter(view1, view2,
				view3);
		mPager.setAdapter(mPagerAdapter);
		initView1();
		rb_main.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup rg, int arg1) {
				int radioButtonId = rg.getCheckedRadioButtonId();
				switch (radioButtonId) {
				case R.id.radio_1:
					initView1();
					mPager.setCurrentItem(0);
					break;
				case R.id.radio_2:
					initView2();
					mPager.setCurrentItem(1);
					break;
				case R.id.radio_3:
					mPager.setCurrentItem(2);
					break;
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initView2() {// 评价
		Intent intent = ((Activity) context).getIntent();
		String s_TeacherID = intent.getStringExtra("TeacherID");
		Map<String, String> params = new HashMap<String, String>();
		params.put("TeacherID", s_TeacherID);
		params.put("Action", "GetTeacherAppraiseList");
		new HttpTask(HttpUtil.URL, context, new CallBack() {
			@Override
			public void run(Map<String, Object> m) {
				String json = (String) m.get("data");
				Object obj = JsonUtil.str2Object(json);

				if (obj instanceof Map) {
					Toast.makeText(context, "没有查询到结果！", Toast.LENGTH_LONG)
							.show();
				} else {
					List<Map<String, Object>> l = (List<Map<String, Object>>) obj;
					MyBaseAdapter adapter = new MyBaseAdapter(view2
							.getContext(), R.layout.item_teacher_appraise, l,
							new CallBack() {
								@Override
								public void run(Map<String, Object> m) {
									View view = (View) m.get("view");
									Map<String, Object> data = (Map<String, Object>) m
											.get("data");
									TextView student_nicky_name = (TextView) view
											.findViewById(R.id.student_nicky_name);
									TextView content = (TextView) view
											.findViewById(R.id.content);
									TextView create_time = (TextView) view
											.findViewById(R.id.create_time);
									Utils.setTextView(data, create_time,
											"CreateTime");
									Utils.setTextView(data, student_nicky_name,
											"StudentNickyName");
									Utils.setTextView(data, content, "Content");

								}

							});
					ListView lv_list = (ListView) view2
							.findViewById(R.id.lv_list);
					lv_list.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			}
		}).execute(params);
	}

	@SuppressWarnings("unchecked")
	private void initView1() {
		Intent intent = ((Activity) context).getIntent();
		final String s_TeacherID = intent.getStringExtra("TeacherID");
		Map<String, String> params = new HashMap<String, String>();
		params.put("TeacherID", s_TeacherID);
		params.put("Action", "GetTeacherResumeInfo");
		new HttpTask(HttpUtil.URL, context, new CallBack() {
			@Override
			public void run(Map<String, Object> m) {
				TextView level2 = (TextView) view1.findViewById(R.id.level2);
				TextView experience = (TextView) view1
						.findViewById(R.id.experience);
				TextView undergo = (TextView) view1.findViewById(R.id.undergo);
				TextView characteristic = (TextView) view1
						.findViewById(R.id.characteristic);
				TextView certificate = (TextView) view1
						.findViewById(R.id.certificate);
				String json = (String) m.get("data");
				Map<String, Object> result = JsonUtil.str2Json(json);
				Log.d(TAG, result + "=" + json);
				if ("10001".equals(result.get("Result") + "")) {
					Toast.makeText(context, "查询失败或是查询无记录", Toast.LENGTH_LONG)
							.show();
					level2.setText("老师职称:");
					experience.setText("教学年限:");
					undergo.setText("");
					characteristic.setText("");
					certificate.setText("");

				} else {
					Utils.setTextViewEx(result, level2, "TeacherTitle",
							"老师职称: %1$s", "", "小教一级", "小教二级", "小教三级", "小教高级",
							"小教特级", "中教一级", "中教二级", "中教三级", "中教高级", "中教特级");

					Utils.setTextViewEx(result, experience, "TeachingYear",
							"教学年限: %1$s年");
					Utils.setTextView(result, undergo, "Characteristic");
					Utils.setTextView(result, characteristic, "Experience");
					Utils.setTextView(result, certificate, "Certificate");
					
					// Button room = (Button) view1.findViewById(R.id.room);
					// room.setOnClickListener(new View.OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// Intent intent = new Intent(context,
					// TeacherEWhiteBoardActivity.class);
					// intent.putExtra("TeacherID", s_TeacherID);
					// context.startActivity(intent);
					// }
					// });
					// 学生进入教室
					Button room2 = (Button) view1.findViewById(R.id.room2);
					room2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							context.startActivity(new Intent(context, VoiceCallActivity.class).
				                    putExtra("username", "t"+s_TeacherID).putExtra("isComingCall", false).
				                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
						}
					});

				}
			}
		}).execute(params);
	}

	public void shoucang(View v) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("是否加入收藏？");
		builder.setTitle("问答帮");
		builder.setPositiveButton("确认", new OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BlackBoardApplication app = (BlackBoardApplication) context
						.getApplicationContext();
				Intent intent = ((Activity) context).getIntent();
				String s_TeacherID = intent.getStringExtra("TeacherID");
				Map<String, String> params = new HashMap<String, String>();
				params.put("Action", "AddMyFavorite");
				params.put("StudentID", Utils.toString(app.get("UserID")));
				params.put("Type", "1");
				params.put("TypeID", s_TeacherID);
				new HttpTask(HttpUtil.URL, context, cb).execute(params);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private CallBack cb = new CallBack() {
		@Override
		public void run(Map<String, Object> m) {

			String json = (String) m.get("data");
			Map<String, Object> result = JsonUtil.str2Json(json);
			Log.d(TAG, result + "=" + json);
			if ("10000".equals(result.get("Result") + "")) {
				Toast.makeText(context, "添加成功", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "添加失败", Toast.LENGTH_LONG).show();
			}
		}
	};

	public void back(View v) {
		((Activity) context).finish();
	}
}
