package com.prism.mina.iobuffer.coder;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.prism.mina.iobuffer.dto.SmsObject;

public class CmccSipcDecoder extends CumulativeProtocolDecoder {

	private final Charset charset;

	/**
	 * @param charset
	 */
	public CmccSipcDecoder(Charset charset) {
		this.charset = charset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.filter.codec.CumulativeProtocolDecoder#doDecode(org.apache
	 * .mina.core.session.IoSession, org.apache.mina.core.buffer.IoBuffer,
	 * org.apache.mina.filter.codec.ProtocolDecoderOutput)
	 */
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		CharsetDecoder decoder = charset.newDecoder();
		
		byte[] info = null;
		
		if(session.containsAttribute("sms")){//粘包
			SmsObject sms = (SmsObject)session.getAttribute("sms");
			if(sms.getSize()==in.limit()){
				info = new byte[sms.getSize()];
				in.get(info);
				sms.setInfo(info);
				out.write(sms);
			}
		}else{
			if(in.hasRemaining()) {
				String cmd = "";
				cmd = in.getString(8, decoder);
				int info_len = in.getInt(8);
				in.getString(4, decoder);
				
				SmsObject sms = new SmsObject();
				sms.setCmd(cmd);
				sms.setSize(info_len);
				
				if(info_len<=in.limit()){
					info = new byte[info_len];
					in.get(info);
					sms.setInfo(info);
					out.write(sms);
				}else{
					session.setAttributeIfAbsent("sms", sms);	
				}
			}
		}
//			System.out.println(in.get()+"==========="+(i++));

////			
//			info = new byte[info_len];
//			in.get(info);
			
		
		
		return false;
	}

}
