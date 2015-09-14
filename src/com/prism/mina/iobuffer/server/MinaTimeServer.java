package com.prism.mina.iobuffer.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.prism.mina.iobuffer.coder.CmccSipcCodecFactory;

public class MinaTimeServer {
	private static final int PORT = 9000;

	public static void main(String[] args) throws IOException {

		IoAcceptor acceptor = new NioSocketAcceptor();

		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		// acceptor.getFilterChain().addLast("codec", new
		// ProtocolCodecFilter(new
		// TextLineCodecFactory(Charset.forName("UTF-8"))));

		acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new CmccSipcCodecFactory(Charset
						.forName("UTF-8"))));

		acceptor.setHandler(new TimeServerHandler());
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acceptor.bind(new InetSocketAddress(PORT));

	}
}
