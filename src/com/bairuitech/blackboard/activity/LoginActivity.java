package com.bairuitech.blackboard.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bairuitech.blackboard.BlackBoardApplication;
import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.common.CallBack;
import com.bairuitech.blackboard.common.HttpTask;
import com.bairuitech.blackboard.common.HttpUtil;
import com.bairuitech.blackboard.common.JsonUtil;
import com.bairuitech.blackboard.common.Utils;
import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.exceptions.EaseMobException;

/**
 * 登录
 */

public class LoginActivity extends Activity {
	private EditText username;
	private EditText password;
	private Button login;
	private Context context;
	private CheckBox save;
	private TextView student_rg, forget_password,teacher_rg;
	private static final String TAG = "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;
		initView();
		initLogic();
	}

	private void initView() {
		username = (EditText) this.findViewById(R.id.username);
		password = (EditText) this.findViewById(R.id.password);

		login = (Button) this.findViewById(R.id.login);
		save = (CheckBox) this.findViewById(R.id.save);
		student_rg = (TextView) this.findViewById(R.id.student_rg);
		teacher_rg = (TextView) this.findViewById(R.id.teacher_rg);
		forget_password = (TextView) this.findViewById(R.id.forget_password);
	}

	private void initLogic() {
		EMChat.getInstance().init(this.getApplication());
		EMChat.getInstance().setDebugMode(true);
		// 第一次访问
		SharedPreferences sharedPreferences = getSharedPreferences("public",
				Activity.MODE_PRIVATE);
		String welcome = sharedPreferences.getString("welcome", "");
		if (!"ok".equals(welcome)) {
			Intent intent = new Intent(LoginActivity.this,
					WelComeActivity.class);
			LoginActivity.this.startActivity(intent);
			LoginActivity.this.finish();
		}
		BlackBoardApplication app = (BlackBoardApplication) context
				.getApplicationContext();

		// 登录
		login.setOnClickListener(loginClick);
		// 学生注册
		student_rg.setOnClickListener(student_rgClick);
		teacher_rg.setOnClickListener(teacher_rgClick);
		
		// 初始化账号密码
		String s_username = sharedPreferences.getString("username", "");
		String s_password = sharedPreferences.getString("password", "");
		username.setText(s_username);
		password.setText(s_password);
		if (!"".equals(s_username)) {
			save.setChecked(true);
		} else {
			save.setChecked(false);
		}
		// 忘记
		forget_password.setOnClickListener(forget_passwordClick);
	}

	// 忘记密码
	private OnClickListener forget_passwordClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LoginActivity.this,
					ForgetPasswordActivity.class);
			LoginActivity.this.startActivity(intent);
			LoginActivity.this.finish();
		}
	};
	// 老师注册
	
	private OnClickListener teacher_rgClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			intent.putExtra("regType", "teacher");
			LoginActivity.this.startActivity(intent);
			LoginActivity.this.finish();
		}

	};

	// 学生注册
	private OnClickListener student_rgClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			intent.putExtra("regType", "student");
			LoginActivity.this.startActivity(intent);
			LoginActivity.this.finish();
		}

	};
	// 登录
	private OnClickListener loginClick = new OnClickListener() {
		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View v) {
			// Intent intent = new Intent(LoginActivity.this,
			// StuFindTeacherActivity.class);
			// LoginActivity.this.startActivity(intent);
			// LoginActivity.this.finish();

			try {
				String s_username = Utils.toString(username.getText());
				String s_password = Utils.toString(password.getText());
				if (Utils.isNull(s_username)) {
					Toast.makeText(context, "输入账号", Toast.LENGTH_LONG).show();
					return;
				}
				if (Utils.isNull(s_password)) {
					Toast.makeText(context, "输入密码", Toast.LENGTH_LONG).show();
					return;
				}
				Map<String, String> params = new HashMap<String, String>();
				params.put("Action", "login");
				params.put("Mobile", s_username);
				params.put("Password", Utils.MD5(s_password));
				if (save.isChecked()) {
					SharedPreferences sp = getSharedPreferences("public",
							Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("username", s_username);
					editor.putString("password", s_password);
					editor.commit();
				} else {
					SharedPreferences sp = getSharedPreferences("public",
							Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("username", "");
					editor.putString("password", "");
					editor.commit();
				}
				new HttpTask(HttpUtil.URL, context, cb).execute(params);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};
	private CallBack cb = new CallBack() {
		@Override
		public void run(Map<String, Object> m) {

			String json = (String) m.get("data");
			final Map<String, Object> result = JsonUtil.str2Json(json);
			Log.d(TAG, result + "=" + json);
			if ("10000".equals(result.get("Result") + "")) {// 登录成功
				if ("1".equals(result.get("UserType"))) {// 学生
					BlackBoardApplication app = (BlackBoardApplication) context
							.getApplicationContext();
					app.put("UserID", result.get("UserID"));
					app.put("UserType", result.get("UserType"));
					app.put("NickyName", result.get("NickyName"));

					Intent intent = new Intent(LoginActivity.this,
							StuFindTeacherActivity.class);
					LoginActivity.this.startActivity(intent);
					LoginActivity.this.finish();
					// 设置在线
					new Thread() {
						public void run() {
							try {
								EMChatManager.getInstance()
										.createAccountOnServer(
												"s" + result.get("UserID"),
												"s" + result.get("UserID"));
							} catch (EaseMobException e) {
//								e.printStackTrace();
							}
							EMChatManager.getInstance().login(
									"s" + result.get("UserID"),
									"s" + result.get("UserID"),
									new EMCallBack() {// 回调
										@Override
										public void onSuccess() {
											EMGroupManager.getInstance().loadAllGroups();
											EMChatManager.getInstance().loadAllConversations();
											// 处理好友和群组
											initializeContacts();
											runOnUiThread(new Runnable() {
												public void run() {
													EMGroupManager
															.getInstance()
															.loadAllGroups();
													EMChatManager
															.getInstance()
															.loadAllConversations();
													System.out
															.println("登陆聊天服务器成功！");
												}
											});
										}

										@Override
										public void onProgress(int progress,
												String status) {

										}

										@Override
										public void onError(int code,
												String message) {
											System.out.println("登陆聊天服务器失败！");
										}
									});
						}
					}.start();

				} else if ("2".equals(result.get("UserType"))) {// 老师
					BlackBoardApplication app = (BlackBoardApplication) context
							.getApplicationContext();
					app.put("UserID", result.get("UserID"));
					app.put("UserType", result.get("UserType"));
					app.put("NickyName", result.get("NickyName"));

					Intent intent = new Intent(LoginActivity.this,
							TeaMainActivity.class);
					LoginActivity.this.startActivity(intent);
					LoginActivity.this.finish();
					System.out.println("t" + result.get("UserID")
							+ "================203");
					// 设置在线
					new Thread() {
						public void run() {
							try {
								EMChatManager.getInstance()
										.createAccountOnServer(
												"t" + result.get("UserID"),
												"t" + result.get("UserID"));
							} catch (EaseMobException e) {
							}
							EMChatManager.getInstance().login(
									"t" + result.get("UserID"),
									"t" + result.get("UserID"),
									new EMCallBack() {// 回调
										@Override
										public void onSuccess() {
											runOnUiThread(new Runnable() {
												public void run() {
													EMGroupManager
															.getInstance()
															.loadAllGroups();
													EMChatManager
															.getInstance()
															.loadAllConversations();
													System.out
															.println("登陆聊天服务器成功！");
												}
											});
										}

										@Override
										public void onProgress(int progress,
												String status) {

										}

										@Override
										public void onError(int code,
												String message) {
											System.out.println("登陆聊天服务器失败！");
										}
									});
						}
					}.start();

				}
			} else if ("10001".equals(result.get("Result") + "")) {
				Toast.makeText(context, "登录失败", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "其他问题", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	private void initializeContacts() {
		Map<String, User> userlist = new HashMap<String, User>();
		// 添加user"申请与通知"
		User newFriends = new User();
		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
		String strChat = getResources().getString(
				R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
		// 添加"群聊"
		User groupUser = new User();
		String strGroup = getResources().getString(R.string.group_chat);
		groupUser.setUsername(Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(Constant.GROUP_USERNAME, groupUser);
		
		// 添加"Robot"
		User robotUser = new User();
		String strRobot = getResources().getString(R.string.robot_chat);
		robotUser.setUsername(Constant.CHAT_ROBOT);
		robotUser.setNick(strRobot);
		robotUser.setHeader("");
		userlist.put(Constant.CHAT_ROBOT, robotUser);
		
		// 存入内存
		((DemoHXSDKHelper)HXSDKHelper.getInstance()).setContactList(userlist);
		// 存入db
		UserDao dao = new UserDao(LoginActivity.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);
	}
}
