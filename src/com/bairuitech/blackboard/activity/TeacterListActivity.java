package com.bairuitech.blackboard.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bairuitech.blackboard.BlackBoardApplication;
import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.adapter.MyBaseAdapter;
import com.bairuitech.blackboard.common.CallBack;
import com.bairuitech.blackboard.common.HttpTask;
import com.bairuitech.blackboard.common.HttpUtil;
import com.bairuitech.blackboard.common.JsonUtil;
import com.bairuitech.blackboard.common.Utils;
import com.bairuitech.blackboard.data.ClassEnum;

/**
 * 老师列表
 */

public class TeacterListActivity extends Activity {
	private Context context;
	private static final String TAG = "TeacterListActivity";
	private ListView lv_friendlist;
	private MyBaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacterlist);
		context = this;
		initView();
		initLogic();
	}

	private void initView() {
		lv_friendlist = (ListView) this.findViewById(R.id.lv_friendlist);
		// Intent intent = this.getIntent();
		// String grade = intent.getStringExtra("Grade");
		// Log.d(TAG, grade + "===");
	}

	@SuppressWarnings("unchecked")
	private void initLogic() {
		Intent intent = this.getIntent();
		String grade = intent.getStringExtra("Grade");
		String subject = intent.getStringExtra("Subject");
		String searchStr = intent.getStringExtra("SearchStr");
		String action = intent.getStringExtra("Action");

		Map<String, String> params = new HashMap<String, String>();
		params.put("Action", action);
		params.put("Grade", grade);
		params.put("Subject", subject);
		params.put("SearchStr", searchStr);

		Log.d(TAG, params + "===");
		new HttpTask(HttpUtil.URL, context, cb).execute(params);
	}

	private CallBack cb = new CallBack() {
		@Override
		public void run(Map<String, Object> m) {
			String json = (String) m.get("data");
			Object obj = JsonUtil.str2Object(json);

			if (obj instanceof Map) {
				Toast.makeText(context, "没有查询到结果！", Toast.LENGTH_LONG).show();
			} else if (obj instanceof List) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> l = (List<Map<String, Object>>) obj;
				adapter = new MyBaseAdapter(context, R.layout.item_teacher, l,
						new CallBack() {
							@Override
							public void run(Map<String, Object> m) {
								View view = (View) m.get("view");
								@SuppressWarnings("unchecked")
								Map<String, Object> data = (Map<String, Object>) m
										.get("data");
								TextView name = (TextView) view
										.findViewById(R.id.name);
								TextView grade = (TextView) view
										.findViewById(R.id.grade);
								TextView object = (TextView) view
										.findViewById(R.id.object);
								Button btn_next = (Button) view
										.findViewById(R.id.btn_next);
								Button shoucang = (Button) view
										.findViewById(R.id.shoucang);
								ImageView status = (ImageView) view
										.findViewById(R.id.status);
								final String s_grade = ClassEnum
										.getClassName(Utils.toString(data
												.get("Grade")));
								final String s_NickyName = Utils.toString(data
										.get("NickyName"));
								final String s_subject = ClassEnum
										.getSubjectName(Utils.toString(data
												.get("Subject")));
								final String s_TeacherID = Utils.toString(data
										.get("TeacherID"));

								name.setText(s_NickyName);
								grade.setText(s_grade);
								object.setText(s_subject);
								shoucang.setOnClickListener(new OnClickListener() {
									@SuppressWarnings("unchecked")
									@Override
									public void onClick(View v) {
										BlackBoardApplication app = (BlackBoardApplication) context
												.getApplicationContext();
										Map<String, String> params = new HashMap<String, String>();
										params.put("Action", "AddMyFavorite");
										params.put("StudentID", Utils
												.toString(app.get("UserID")));
										params.put("Type", "1");
										params.put("TypeID", s_TeacherID);
										new HttpTask(HttpUtil.URL, context, cb)
												.execute(params);
									}
								});
								BlackBoardApplication app = (BlackBoardApplication) context
										.getApplicationContext();
								Set<Integer> tea_online = app.tea_online;
								if (tea_online.contains(Integer
										.parseInt(s_TeacherID))) {
									status.setBackgroundResource(R.drawable.status);
								}
								btn_next.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										
										Intent intent = new Intent(context,
												TeacterMoreActivity.class);
										intent.putExtra("TeacherID",
												s_TeacherID);
										intent.putExtra("Grade", s_grade);
										intent.putExtra("Subject", s_subject);
										intent.putExtra("NickyName",
												s_NickyName);
										context.startActivity(intent);
									}
								});
							}
						});
				lv_friendlist.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}

		}
	};

	public void back(View v) {
		((Activity) context).finish();
	}
}
