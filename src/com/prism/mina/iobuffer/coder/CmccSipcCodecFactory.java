package com.prism.mina.iobuffer.coder;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CmccSipcCodecFactory implements ProtocolCodecFactory {

	private final CmccSipcEncoder encoder;
	private final CmccSipcDecoder decoder;

	public CmccSipcCodecFactory() {
		this(Charset.defaultCharset());
	}

	/**
	 * @param defaultCharset
	 */
	public CmccSipcCodecFactory(Charset charSet) {
		this.encoder = new CmccSipcEncoder(charSet);
		this.decoder = new CmccSipcDecoder(charSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.filter.codec.ProtocolCodecFactory#getDecoder(org.apache
	 * .mina.core.session.IoSession)
	 */
	@Override
	public ProtocolDecoder getDecoder(IoSession iosession) throws Exception {
		// TODO Auto-generated method stub
		return decoder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.filter.codec.ProtocolCodecFactory#getEncoder(org.apache
	 * .mina.core.session.IoSession)
	 */
	@Override
	public ProtocolEncoder getEncoder(IoSession iosession) throws Exception {
		// TODO Auto-generated method stub
		return encoder;
	}

}
