package com.bairuitech.blackboard.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

public class MyCollectActivity extends Activity {
	private static final String TAG = "MyCollectActivity";
	private Context context;
	private ListView lv_list;
	private MyBaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycollect);
		context = this;
		initView();
		initLogic();
	}

	private void initView() {
		lv_list = (ListView) this.findViewById(R.id.lv_list);
	}

	private void initLogic() {
		initList();
	}

	@SuppressWarnings("unchecked")
	private void initList() {
		Map<String, String> params = new HashMap<String, String>();
		BlackBoardApplication app = (BlackBoardApplication) this
				.getApplicationContext();
		String user_id = Utils.toString(app.get("UserID"));
		params.put("Action", "GetMyFavorite");
		params.put("StudentID", user_id);
		new HttpTask(HttpUtil.URL, context, cb).execute(params);
	}

	private CallBack cb = new CallBack() {
		@Override
		public void run(Map<String, Object> m) {
			String json = (String) m.get("data");
			Object obj = JsonUtil.str2Object(json);
			Log.d(TAG, json + "==" + obj);
			if (obj instanceof Map) {
				Toast.makeText(context, "没有查询到结果！", Toast.LENGTH_LONG).show();
			} else if (obj instanceof List) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> l = (List<Map<String, Object>>) obj;
				adapter = new MyBaseAdapter(context, R.layout.item_my_collect,
						l, new CallBack() {
							@Override
							public void run(Map<String, Object> m) {
								View view = (View) m.get("view");
								@SuppressWarnings("unchecked")
								final Map<String, Object> data = (Map<String, Object>) m
										.get("data");
								TextView type = (TextView) view
										.findViewById(R.id.type); // 类型
								TextView name = (TextView) view
										.findViewById(R.id.name); // 名称
								Button shoucang = (Button) view
										.findViewById(R.id.shoucang); // 收藏
								Utils.setTextViewEx(data, type, "Type",
										"类型：%1$s", "", "教师", "课程");
								Utils.setTextView(data, name, "Name");
								shoucang.setOnClickListener(new OnClickListener() {
									@SuppressWarnings("unchecked")
									@Override
									public void onClick(View v) {
										Map<String, String> params = new HashMap<String, String>();
										params.put("Action", "DelMyFavorite");
										params.put("FavID", Utils.toString(data
												.get("FavID")));
										new HttpTask(HttpUtil.URL, context,
												new CallBack() {
													@Override
													public void run(
															Map<String, Object> m) {
														String json = (String) m.get("data");
														Map<String, Object> data = JsonUtil.str2Json(json);
														
														if("10000".equals(data.get("Result")+"")){
															initList();
														}else{
															Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
														}
													}
												}).execute(params);
									}
								});
							}
						});
				lv_list.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}
	};

	public void back(View v) {
		((Activity) context).finish();
	}
}
