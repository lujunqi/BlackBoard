package com.prism.mina.iobuffer.server;

import java.util.Collection;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class TimeServerHandler extends IoHandlerAdapter {

	public TimeServerHandler() {
	}

	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// System.out.println("============="+session.getId());
		cause.printStackTrace();
	}

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		Collection<IoSession> sessions = session.getService()
				.getManagedSessions().values();
		// 向所有客户端发送数据
		for (IoSession sess : sessions) {
			sess.write(message);
		}
	}

	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// System.out.println((new StringBuilder()).append("IDLE ")
		// .append(session.getIdleCount(status)).toString());
	}

}
