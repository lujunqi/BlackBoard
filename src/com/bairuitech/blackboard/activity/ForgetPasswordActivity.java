package com.bairuitech.blackboard.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.common.CallBack;
import com.bairuitech.blackboard.common.HttpTask;
import com.bairuitech.blackboard.common.HttpUtil;
import com.bairuitech.blackboard.common.Utils;

/**
 * 忘记密码
 * @author HP
 * 
 */
public class ForgetPasswordActivity extends Activity {
	private EditText ed_phone, ed_code, ed_password, ed_password2;
	private Context context;
	private static final String TAG = "ForgetPasswordActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpassword);
		context = this;
		initView();
		initLogic();
	}

	private void initView() {
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		ed_code = (EditText) findViewById(R.id.ed_code);
		ed_password = (EditText) findViewById(R.id.ed_password);
		ed_password2 = (EditText) findViewById(R.id.ed_password2);
	}

	private void initLogic() {

	}
	//获取验证码
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
	// 提交
	@SuppressWarnings("unchecked")
	public void finish_click(View v) {
		String s_ed_phone = Utils.toString(ed_phone.getText());
		String s_ed_code = Utils.toString(ed_code.getText());
		String s_ed_password = Utils.toString(ed_password.getText());
		String s_ed_password2 = Utils.toString(ed_password2.getText());

		if (Utils.isNulls(s_ed_phone, s_ed_code, s_ed_password, s_ed_password2)) {
			showLongToast("请输入完整的修改信息");
			return;
		}
		if (!Utils.isMobileNO(s_ed_phone)) {
			showLongToast("请输入正确的手机号码");
			return;
		}
		if (s_ed_password.length() < 6) {
			showLongToast("密码不得小于6位");
			return;
		}
		if (!s_ed_password.equals(s_ed_password2)) {
			showLongToast("两次密码输入不相同");
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("Action", "reg_next");
		params.put("ed_phone", s_ed_phone);
		params.put("ed_code", s_ed_code);
		params.put("ed_password", Utils.MD5(s_ed_password));
		new HttpTask(HttpUtil.URL, context,  new CallBack() {
			@Override
			public void run(Map<String, Object> m) {
				String json = (String) m.get("data");
				// 修改成功*************************************************
			}
		}).execute(params);
	}

	public void showLongToast(String s) {
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}


	public void back(View v) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
		ForgetPasswordActivity.this.finish();
	}
}
