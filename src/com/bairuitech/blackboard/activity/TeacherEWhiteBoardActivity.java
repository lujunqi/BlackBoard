package com.bairuitech.blackboard.activity;

import java.util.Arrays;
import java.util.Vector;

import org.apache.mina.core.buffer.IoBuffer;

import android.app.Activity;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;

import com.bairuitech.blackboard.BlackBoardApplication;
import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.activity.paint.TeacherPaintView;

/**
 * 老师 黑板
 * 
 * 
 */
public class TeacherEWhiteBoardActivity extends Activity {
	private Context context;

	private static final String TAG = "EWhiteBoardActivity";
	private TeacherPaintView view_paint;
	private int myColor;
	private BlackBoardApplication app;
	private Object[] draw_mark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ewhiteboard);
		context = this;

		app = (BlackBoardApplication) context.getApplicationContext();
		initView();
		initLogic();
	}

	private void initView() {
		view_paint = (TeacherPaintView) this.findViewById(R.id.view_paint);
		myColor = 0xffffffff;
	}

	private void initLogic() {

		view_paint.touch = true;
		recBufSize = AudioRecord.getMinBufferSize(frequency,
				channelConfiguration, audioEncoding);
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
				channelConfiguration, audioEncoding, recBufSize * 10);

		playBufSize = AudioTrack.getMinBufferSize(frequency,
				channelConfiguration, audioEncoding);
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
				channelConfiguration, audioEncoding, playBufSize,
				AudioTrack.MODE_STREAM);
		// MinaTask mina = new MinaTask();
		// mina.execute();

		send();

	}

	public void takephoto(View v) {

	}

	public void send() {

		isRecording = true;
		new Thread() {// 录音
			public void run() {
				byte[] record_buf = new byte[recBufSize];
				audioRecord.startRecording();// 开始录制
				while (isRecording) {
					if (app.isConnected()) {

						audioRecord.read(record_buf, 0, recBufSize);
						// byte[] buff2 = Utils.gzip(record_buf);
						IoBuffer buf = IoBuffer.allocate(8);
						buf.setAutoExpand(true);
						buf.putChar('A');
						buf.putInt(record_buf.length);
						buf.put(record_buf.clone());
						buf.flip();

//						app.send(buf);

					}
				}
				audioRecord.stop();
			}
		}.start();

		new Thread() { // 录屏
			public void run() {
				int step = 0;
				while (isRecording) {
					if (app.isConnected()) {
						Vector<String> cmds = view_paint.getCmds();

						Object[] buff = cmds.toArray();

						if (!Arrays.equals(buff, draw_mark) || step > 50) {
							IoBuffer buf = IoBuffer.allocate(8);
							buf.setAutoExpand(true);
							buf.putChar('D');
							for (int i = 0; i < cmds.size(); i++) {
								String info = cmds.get(i);
								String[] c = info.split(",");
								buf.putChar(c[0].charAt(0));
								buf.putFloat(Float.parseFloat(c[1]));
								buf.putFloat(Float.parseFloat(c[2]));
							}
							step = 0;
							buf.flip();
							app.send(buf);

						}
						draw_mark = buff.clone();
					}
					try {
						step++;
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void edit(View v) {
		view_paint.edit(myColor);
	}

	public void eraser(View v) {
		view_paint.eraser();
	}

	public void touch(View v) {

	}

	public void clear(View v) {
		view_paint.clear();
	}

	public void back(View v) {

		((Activity) context).finish();
	}

	@Override
	protected void onDestroy() {
		audioRecord.stop();

		isRecording = false;
		System.out.println("onDestroy====");

		super.onDestroy();
	}

	static final int frequency = 8000;// 44100;
	@SuppressWarnings("deprecation")
	static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	int recBufSize, playBufSize;

	AudioRecord audioRecord;
	AudioTrack audioTrack;
	public boolean isRecording = false;// 是否录放的标记
	public boolean isPlaying = false;// 是否录放的标记

}