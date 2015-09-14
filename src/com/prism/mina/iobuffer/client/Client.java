package com.prism.mina.iobuffer.client;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.prism.mina.iobuffer.coder.CmccSipcCodecFactory;
import com.prism.mina.iobuffer.dto.SmsObject;

public class Client {
	public Client() {
	}

	public static void main(String args[]) throws Throwable {
		IoConnector connector = new NioSocketConnector();
		connector.setHandler(new IoHandlerAdapter() {
			@Override
			public void exceptionCaught(IoSession session, Throwable cause)
					throws Exception {
				System.out.println("服务器发生异常： {}" + cause.getMessage());
			}
			@Override
			public void messageReceived(IoSession session, Object message)
					throws Exception {
				SmsObject sms = (SmsObject)message;
				System.out.println(sms.getCmd()+"==="+sms.getSize());
			}
			@Override
			public void sessionOpened(IoSession session) {

				try {
					java.io.FileInputStream fis = new java.io.FileInputStream(
							"e:/temp/dw.xls");
					java.io.DataInputStream dis = new DataInputStream(fis);

					byte[] b = new byte[dis.available()];
					dis.read(b);
					SmsObject sms = new SmsObject();
					sms.setCmd("audo");
					sms.setInfo(b);
					session.write(sms);
					dis.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		connector.setConnectTimeoutMillis(30000);
		DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
		filterChain.addLast("codec", new ProtocolCodecFilter(
				new CmccSipcCodecFactory(Charset.forName("UTF-8"))));

		connector.connect(new InetSocketAddress("10.80.1.212", 9000));
	}
}
