package com.bairuitech.blackboard.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.adapter.MyBaseAdapter;
import com.bairuitech.blackboard.adapter.MyViewPagerAdapter;
import com.bairuitech.blackboard.common.CallBack;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;

@SuppressLint("InlinedApi")
public class TeaMainActivity extends Activity {
	private Context context;
	private static final String TAG = "TeaMainActivity";
	private View view1, view2, view3;
	private ViewPager mPager;
	private RadioGroup rb_main;
	private SwipeRefreshLayout swipeView;
	private ListView info_list_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tea_main);
		context = this;
		initView();
		initLogic();
	}

	public void initView() {
		mPager = (ViewPager) findViewById(R.id.pager);
		rb_main = (RadioGroup) this.findViewById(R.id.rb_main);

	}

	public void initLogic() {
		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.view_huifang_teacher1, null);// 回放
		view2 = inflater.inflate(R.layout.view_teacher2, null);// 评价
		view3 = inflater.inflate(R.layout.view_teacher3, null);// 精品课

		MyViewPagerAdapter mPagerAdapter = new MyViewPagerAdapter(view1, view2,
				view3);

		mPager.setAdapter(mPagerAdapter);
		initView1();
		mPager.setCurrentItem(0);
		RadioButton r = (RadioButton) this.findViewById(R.id.radio_1);
		r.setChecked(true);

		EMContactManager.getInstance().setContactListener(
				new EMContactListener() {

					@Override
					public void onContactAgreed(String username) {
						// 好友请求被同意
					}

					@Override
					public void onContactRefused(String username) {
						// 好友请求被拒绝
					}

					@Override
					public void onContactInvited(String username, String reason) {
						// 收到好友邀请
						System.out.println(username+"===="+reason);
					}

					@Override
					public void onContactDeleted(List<String> usernameList) {
						// 被删除时回调此方法
					}

					@Override
					public void onContactAdded(List<String> usernameList) {
						// 增加了联系人时回调此方法
					}
				});

		rb_main.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup rg, int arg1) {
				int radioButtonId = rg.getCheckedRadioButtonId();
				switch (radioButtonId) {
				case R.id.radio_1:
					Log.d(TAG, "=================1");
					initView1();
					mPager.setCurrentItem(0);
					break;
				case R.id.radio_2:
					// initView2();
					mPager.setCurrentItem(1);
					break;
				case R.id.radio_3:
					mPager.setCurrentItem(2);
					break;
				}
			}
		});
	}

	CallBack cb = new CallBack() {

		@Override
		public void run(Map<String, Object> m) {
			View view = (View) m.get("view");
			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) m.get("data");
			TextView name = (TextView) view.findViewById(R.id.name);
			name.setText(data.get("name") + "0");
		}

	};
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@SuppressLint("InlinedApi")
	private void initView1() {
		swipeView = (SwipeRefreshLayout) view1
				.findViewById(R.id.swipe_container);
		info_list_view = (ListView) view1.findViewById(R.id.info_list_view);
		for (int i = 0; i < 10; i++) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("name", "name" + i);
			list.add(m);
		}
		MyBaseAdapter adapter = new MyBaseAdapter(view1.getContext(),
				R.layout.item_record, list, cb);
		info_list_view.setAdapter(adapter);

		swipeView.setColorScheme(android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		swipeView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				MyBaseAdapter adapter = new MyBaseAdapter(view1.getContext(),
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
