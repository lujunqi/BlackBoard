package com.bairuitech.blackboard.widget;

/**
 * 学生个人中心
 */
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bairuitech.blackboard.BlackBoardApplication;
import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.activity.AboutActivity;
import com.bairuitech.blackboard.activity.MyCollectActivity;
import com.bairuitech.blackboard.activity.MyDataActivity;
import com.bairuitech.blackboard.activity.WebShowActivity;
import com.bairuitech.blackboard.common.CallBack;
import com.bairuitech.blackboard.common.HttpTask;
import com.bairuitech.blackboard.common.HttpUtil;
import com.bairuitech.blackboard.common.JsonUtil;
import com.bairuitech.blackboard.common.Utils;

public class MyCenter extends RelativeLayout {
	private Context context;
	private TextView choujiang, mydata, tealist, learntotal, 
			yaoqingma, about, shares;
	private static final String TAG = "MyCenter";
	public MyCenter(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.widget_my_center, this,
				true);
		initView();
		initLogic();
	}

	private void initView() {
		choujiang = (TextView) this.findViewById(R.id.choujiang);
		mydata = (TextView) this.findViewById(R.id.mydata);
		tealist = (TextView) this.findViewById(R.id.tealist);
		learntotal = (TextView) this.findViewById(R.id.learntotal);
		yaoqingma = (TextView) this.findViewById(R.id.yaoqingma);
		about = (TextView) this.findViewById(R.id.about);
		shares = (TextView) this.findViewById(R.id.shares);
	}

	private void initLogic() {
		about.setOnClickListener(new OnClickListener() {//关于
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, AboutActivity.class);
				context.startActivity(intent);
			}
			
		});
		yaoqingma.setOnClickListener(new OnClickListener() {// 我的邀请码
					@SuppressWarnings("unchecked")
					@Override
					public void onClick(View arg0) {
						Map<String, String> params = new HashMap<String, String>();
						BlackBoardApplication app = (BlackBoardApplication) context
								.getApplicationContext();
						String user_id = Utils.toString(app.get("UserID"));
						params.put("Action", "GetStudentInfo");
						params.put("StudentID", user_id);
						new HttpTask(HttpUtil.URL, context, new CallBack() {
							@Override
							public void run(Map<String, Object> m) {
								//InvitationCode
								String json = (String) m.get("data");
								Map<String, Object> obj = JsonUtil.str2Json(json);
								Log.d(TAG, json + "==" + obj);
								String info = "您的邀请码为%1$s，推荐邀请码给别人注册可获得学币赠送哟！";
								new AlertDialog.Builder(context).setTitle("我的邀请码")
								.setMessage(String.format(info,Utils.toString(obj.get("InvitationCode")) ))
								.setPositiveButton("我知道了", null).show();
							}
						}).execute(params);
					}
				});
		choujiang.setOnClickListener(new OnClickListener() {// 抽奖
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(context,
								WebShowActivity.class);
						intent.putExtra("tv_title", "幸运大转盘");
						intent.putExtra("url", "http://www.baidu.com");
						context.startActivity(intent);
					}
				});
		mydata.setOnClickListener(new OnClickListener() {// 个人资料
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, MyDataActivity.class);
				context.startActivity(intent);
			}
		});
		tealist.setOnClickListener(new OnClickListener() {// 学习收藏
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, MyCollectActivity.class);
				context.startActivity(intent);
			}
		});
		learntotal.setOnClickListener(new OnClickListener() {// 学习统计
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(context,
								WebShowActivity.class);
						intent.putExtra("tv_title", "学习统计");
						intent.putExtra("url", "http://www.baidu.com");
						context.startActivity(intent);
					}
				});


	}
}
