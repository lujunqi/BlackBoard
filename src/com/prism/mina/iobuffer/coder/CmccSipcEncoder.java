package com.prism.mina.iobuffer.coder;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.prism.mina.iobuffer.dto.SmsObject;

public class CmccSipcEncoder extends ProtocolEncoderAdapter {

	private final Charset charset;

	public CmccSipcEncoder(Charset charset) {
		this.charset = charset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.core
	 * .session.IoSession, java.lang.Object,
	 * org.apache.mina.filter.codec.ProtocolEncoderOutput)
	 */
	@Override
	public void encode(IoSession iosession, Object obj,
			ProtocolEncoderOutput out) throws Exception {
//		System.out.println(obj);
		SmsObject sms = (SmsObject) obj;
		String cmd = sms.getCmd();
		byte[] info = sms.getInfo();
		CharsetEncoder charst = charset.newEncoder();
		IoBuffer buffer = IoBuffer.allocate(8).setAutoExpand(true);
		buffer.putString(cmd, charst);
//		System.out.println(cmd+"==="+info.length);
		buffer.putInt(info.length);
		buffer.put(info);

		buffer.flip();
		out.write(buffer);

	}

}
