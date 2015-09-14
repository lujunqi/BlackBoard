package com.bairuitech.blackboard.mina;

import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import android.content.Context;

import com.bairuitech.blackboard.common.CallBack;
import com.bairuitech.blackboard.common.JsonUtil;

public class MyIoHandlerAdapter extends IoHandlerAdapter {
	private Context context;
	private CallBack cb;
	public MyIoHandlerAdapter(Context context,CallBack cb) {
		this.context = context;
		this.cb= cb;
	}

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String content = message.toString();
		Map<String,Object> m = JsonUtil.str2Json(content);
		if(!m.isEmpty()){
			cb.run(m);
		}
		
//		System.out.println("client receive a message is : " + content);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
//		System.out.println("messageSent 客户端发送消息：" + message);

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		System.out.println("服务器发生异常： {}" + cause.getMessage());
	}
}
