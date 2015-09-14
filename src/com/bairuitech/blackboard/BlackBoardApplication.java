package com.bairuitech.blackboard;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.bairuitech.blackboard.common.CallBack;
import com.bairuitech.blackboard.common.Utils;
import com.prism.mina.iobuffer.dto.HachiKeepAliveFilterInMina;

public class BlackBoardApplication extends Application {
	private static final String TAG = "BlackBoardApplication";
	public ConnectFuture future;
	private Map<String, Object> params = new HashMap<String, Object>();
	public Set<Integer> tea_online = new HashSet<Integer>();
	public Set<Integer> stu_online = new HashSet<Integer>();
	public CallBack mina_cl;
	private NioDatagramConnector connector = new NioDatagramConnector();

	@Override
	public void onCreate() {

		new MinaTask().execute();

		Log.d(TAG, "[BlackBoardApplication] onCreate");
		super.onCreate();

	}

	public void send(IoBuffer buff) {
		if (future != null) {
			if (future.isConnected()) {
				IoSession session = future.getSession();
				session.write(buff);
			}
		}
	}

	public boolean isConnected() {
		if (future != null) {
			if (future.isConnected()) {
				return true;
			}
		}
		return false;
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

	// 录制
	class MinaTask extends AsyncTask<Void, IoBuffer, Void> {
		@SuppressLint("NewApi")
		@Override
		protected Void doInBackground(Void... args) {
			try {

				connector.setHandler(new IoHandlerAdapter() {
					@Override
					public void exceptionCaught(IoSession session,
							Throwable cause) throws Exception {
						System.out.println("服务器发生异常： {}" + cause.getMessage());
					}

					@Override
					public void messageReceived(IoSession session,
							Object message) throws Exception {
						IoBuffer buff = (IoBuffer) message;
						publishProgress(buff);

					}
				});

				connector.setConnectTimeoutMillis(30000);
				connector.setConnectTimeoutCheckInterval(10000);
				DefaultIoFilterChainBuilder filterChain = connector
						.getFilterChain();
				// filterChain.addLast("codec", new ProtocolCodecFilter(
				// new CmccSipcCodecFactory(Charset.forName("UTF-8"))));
				// 心跳
				filterChain.addLast("keep-alive",
						new HachiKeepAliveFilterInMina());

				DatagramSessionConfig dcfg = connector.getSessionConfig();
				dcfg.setReadBufferSize(Utils.SIZE);// 设置接收最大字节默认2048
				dcfg.setMaxReadBufferSize(Utils.SIZE);
				dcfg.setReceiveBufferSize(Utils.SIZE);// 设置输入缓冲区的大小
				dcfg.setSendBufferSize(Utils.SIZE);// 设置输出缓冲区的大小
				dcfg.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用

				future = connector.connect(new InetSocketAddress("10.80.1.212",
						9999));

				// future = connector.connect(new InetSocketAddress(
				// "120.24.76.197", 9999));
				future.awaitUninterruptibly();

				// IoSession session = future.getSession();
				// 关闭连接
				// session.getCloseFuture().awaitUninterruptibly();
				// connector.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(IoBuffer... progress) {
			IoBuffer buff = progress[0];
			if (buff.limit() == 8) {// 状态
				char type = buff.getChar(); // 老师 或 学生
				char io = buff.getChar(); // 开通或关闭
				int id = buff.getInt(); // id
				IoSession session = future.getSession();
				session.setAttribute("TYPE", type);
				session.setAttribute("ID", id);
				if (type == 'T') {
					if (io == 'O') {
						tea_online.add(id);
					}
					if (io == 'C') {
						tea_online.remove(id);
					}
					if (io == 'W') {// 弹出白板

						int myId = Integer.parseInt(params.get("UserID") + "");

						if (id == myId) {
							Map<String, Object> m = new HashMap<String, Object>();
							m.put("WAKEUP", buff);
							mina_cl.run(m);
						}

					}
				}
				if (type == 'S') {
					if (io == 'O') {
						stu_online.add(id);
					}
					if (io == 'C') {
						stu_online.remove(id);
					}

				}
			} else {
				if (mina_cl != null) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("IOBUFFER", buff);
					mina_cl.run(m);
				}
			}

			super.onProgressUpdate(progress);

		}

		protected void onPostExecute(Void result) {
		}

		protected void onPreExecute() {
		}

	}

}
