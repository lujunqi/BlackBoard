package com.bairuitech.blackboard.activity;

/**
 * 注册
 */
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.common.CallBack;
import com.bairuitech.blackboard.common.HttpTask;
import com.bairuitech.blackboard.common.HttpUtil;
import com.bairuitech.blackboard.common.JsonUtil;
import com.bairuitech.blackboard.common.Utils;

public class RegisterActivity extends Activity {
	private String regType;// 注册类型
	private TextView rg_type;
	private EditText ed_phone, ed_email, ed_nickname, ed_password,
			ed_password2, ed_yaoqing, ed_code;
	private LinearLayout yaoqing;
	private Context context;
	private static final String TAG = "RegisterActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		regType = getIntent().getStringExtra("regType");
		context = this;
		initView();
		initLogic();
	}

	private void initView() {
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		ed_email = (EditText) findViewById(R.id.ed_email);
		ed_nickname = (EditText) findViewById(R.id.ed_nickname);
		ed_password = (EditText) findViewById(R.id.ed_password);
		ed_password2 = (EditText) findViewById(R.id.ed_password2);
		ed_yaoqing = (EditText) findViewById(R.id.ed_yaoqing);
		ed_code = (EditText) findViewById(R.id.ed_code);
		rg_type = (TextView) findViewById(R.id.rg_type);
		yaoqing = (LinearLayout) findViewById(R.id.yaoqing);
	}

	private void initLogic() {
		if ("student".equals(regType)) {
			rg_type.setText("学生注册");
		}
		if ("teacher".equals(regType)) {
			rg_type.setText("老师注册");
		}
		
	}

	// 获取验证码
	@SuppressWarnings("unchecked")
	public void getcode_click(View v) {
		String s_ed_phone = Utils.toString(ed_phone.getText());

		if (Utils.isNull(s_ed_phone)) {
			Toast.makeText(context, "请输入手机号码", Toast.LENGTH_LONG).show();
			return;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("Action", "getcode");
		params.put("ed_phone", s_ed_phone);

		new HttpTask(HttpUtil.URL, context, new CallBack() {
			@Override
			public void run(Map<String, Object> m) {
				String json = (String) m.get("data");
				// 待续**********************************************
			}
		}).execute(params);
	}

	// 返回
	public void back(View v) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
		RegisterActivity.this.finish();
	}

	// 注册
	@SuppressWarnings("unchecked")
	public void btn_next_Click(View v) {
		String s_ed_phone = Utils.toString(ed_phone.getText());
		String s_ed_email = Utils.toString(ed_email.getText());
		String s_ed_nickname = Utils.toString(ed_nickname.getText());
		String s_ed_password = Utils.toString(ed_password.getText());
		String s_ed_password2 = Utils.toString(ed_password2.getText());
		String s_ed_code = Utils.toString(ed_code.getText());
		String s_ed_yaoqing = Utils.toString(ed_yaoqing.getText());
		Log.d(TAG, s_ed_phone + "+" + s_ed_email + "+" + s_ed_nickname + "+"
				+ s_ed_password + "+" + s_ed_password2+"=="+regType);
		if (Utils.isNulls(s_ed_phone, s_ed_nickname, s_ed_password,
				s_ed_password2)) {
			Toast.makeText(context, "请输入完整的注册信息", Toast.LENGTH_LONG).show();
			return;
		}
		if (!s_ed_password.equals(s_ed_password2)) {
			Toast.makeText(context, "请输入密码不一致", Toast.LENGTH_LONG).show();
			return;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("ed_yaoqing", s_ed_yaoqing);
		params.put("ed_code", s_ed_code);

		params.put("Action", "AddStudent");
		if ("teacher".equals(regType)) {
			params.put("Action", "AddTeacher");
			params.put("Grade", "0");
			params.put("Subject", "0");
			
		}
		
		params.put("Mobile", s_ed_phone);
		params.put("ed_email", s_ed_email);
		params.put("NickName", s_ed_nickname);
		params.put("Password", Utils.MD5(s_ed_password));
		new HttpTask(HttpUtil.URL, context, new CallBack() {
			@Override
			public void run(Map<String, Object> m) {
				String json = (String) m.get("data");
				Map<String, Object> result = JsonUtil.str2Json(json);
				Log.d(TAG, result + "=" + json);
				if ("student".equals(regType)) {
					if ("10000".equals(result.get("Result")+"")) {
						Toast.makeText(context, "注册成功~", Toast.LENGTH_LONG)
								.show();
						Intent intent = new Intent(RegisterActivity.this,
								LoginActivity.class);
						RegisterActivity.this.startActivity(intent);
						RegisterActivity.this.finish();
					} else if ("1062".equals(result.get("Result")+"")) {
						Toast.makeText(context, "电话号码已存在", Toast.LENGTH_LONG)
								.show();
					} else if ("10001".equals(result.get("Result")+"")) {
						Toast.makeText(context, "注册失败", Toast.LENGTH_LONG)
								.show();
					} else {
						Toast.makeText(context, "其他问题", Toast.LENGTH_LONG)
								.show();
					}

				}
				if ("teacher".equals(regType)) {
					if ("10000".equals(result.get("Result")+"")) {
						Toast.makeText(context, "注册成功~", Toast.LENGTH_LONG)
								.show();
						Intent intent = new Intent(RegisterActivity.this,
								LoginActivity.class);
						RegisterActivity.this.startActivity(intent);
						RegisterActivity.this.finish();
					} else if ("1062".equals(result.get("Result")+"")) {
						Toast.makeText(context, "电话号码已存在", Toast.LENGTH_LONG)
								.show();
					} else if ("10001".equals(result.get("Result")+"")) {
						Toast.makeText(context, "注册失败", Toast.LENGTH_LONG)
								.show();
					} else {
						Toast.makeText(context, "其他问题", Toast.LENGTH_LONG)
								.show();
					}

				}
			}
		}).execute(params);
	}

}
