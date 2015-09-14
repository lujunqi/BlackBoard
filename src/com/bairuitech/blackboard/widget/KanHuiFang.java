package com.bairuitech.blackboard.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.adapter.MyBaseAdapter;
import com.bairuitech.blackboard.common.CallBack;

public class KanHuiFang extends RelativeLayout {
	public static final String TAG = "KanHuiFang";
	private Context context;
	private SwipeRefreshLayout swipeView;
	private ListView info_list_view;

	public KanHuiFang(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.widget_kan_hui_fang,
				this, true);
		swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		info_list_view = (ListView) findViewById(R.id.info_list_view);
		init();
	}

	private CallBack cb = new CallBack() {

		@Override
		public void run(Map<String, Object> m) {
			View view = (View) m.get("view");
			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) m.get("data");
			TextView name = (TextView) view.findViewById(R.id.name);
			name.setText(data.get("name")+"0");
		}

	};

	private void init() {
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("name", "name" + i);
			list.add(m);
		}
		MyBaseAdapter adapter = new MyBaseAdapter(context,
				R.layout.item_record, list, cb);
		info_list_view.setAdapter(adapter);

		swipeView.setColorScheme(android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		swipeView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				MyBaseAdapter adapter = new MyBaseAdapter(context,
						R.layout.item_record, list, cb);
				info_list_view.setAdapter(adapter);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						swipeView.setRefreshing(false);
					}
				}, 1);
			}
		});
	}

}
