package com.bairuitech.blackboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bairuitech.blackboard.common.CallBack;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.DemoHXSDKHelper;

public class BlackBoardApplication extends Application {
	private static final String TAG = "BlackBoardApplication";
	private Map<String, Object> params = new HashMap<String, Object>();
	public Set<Integer> tea_online = new HashSet<Integer>();
	public Set<Integer> stu_online = new HashSet<Integer>();
	public CallBack mina_cl;
	public static Context applicationContext;
	private static BlackBoardApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	public void send(String username, String content) {
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation(username);
		// 创建一条文本消息
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		// 设置消息body
		TextMessageBody txtBody = new TextMessageBody(content);
		message.addBody(txtBody);
		// 设置接收人
		message.setReceipt(username);
		// 把消息加入到此会话对象中
		conversation.addMessage(message);
		// 发送消息
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

			@Override
			public void onError(int arg0, String arg1) {
				System.out.println("===============59"+arg1);
			}

			@Override
			public void onProgress(int arg0, String arg1) {
				System.out.println("===============63"+arg1);
			}

			@Override
			public void onSuccess() {
				System.out.println("===============69");
			}
		});
	}

	@Override
	public void onCreate() {
		EMChat.getInstance().init(this);
		EMChat.getInstance().setDebugMode(true);
		Log.d(TAG, "[BlackBoardApplication] onCreate");
		super.onCreate();
		applicationContext = this;
		instance = this;

		hxSDKHelper.onInit(applicationContext);

	}

	public static BlackBoardApplication getInstance() {
	
		return instance;
	}

	/**
	 * 获取当前登陆用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 * 
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 * 
	 * @param user
	 */
	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 * 
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final boolean isGCM, final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		hxSDKHelper.logout(isGCM, emCallBack);
	}

	public void put(String key, Object val) {
		params.put(key, val);
	}

	public Object get(String key) {
		return params.get(key);
	}

	public boolean containsKey(String key) {
		return params.containsKey(key);
	}

}
