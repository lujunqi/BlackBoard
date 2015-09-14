package com.prism.mina.iobuffer.dto;

public class SmsObject {
	private String cmd;
	private byte[] info = null;
	private int size =0;
	public String getCmd() {
		if(cmd.length()>=8){
			return cmd.substring(0, 8);
		}else{
			return String.format("%-8s", cmd);
		}
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public byte[] getInfo() {
		return info;
	}

	public void setInfo(byte[] info) {
		this.info = info;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
