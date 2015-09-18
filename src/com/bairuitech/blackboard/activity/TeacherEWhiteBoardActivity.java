package com.bairuitech.blackboard.activity;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
	public static Context context;

	private static final String TAG = "TeacherEWhiteBoardActivity";
	private TeacherPaintView view_paint;
	private int myColor;
	private BlackBoardApplication app;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ewhiteboard);
		context = this;
		Intent intent = this.getIntent();
		username = intent.getStringExtra("username");
		app = (BlackBoardApplication) context.getApplicationContext();
		initView();
		initLogic();
	}

	private void initView() {
		view_paint = (TeacherPaintView) this.findViewById(R.id.view_paint);
		view_paint.username = username;
		view_paint.app = app;
		
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

//		send();

	}

	public void takephoto(View v) {

	}

//	public void send() {
//
//		isRecording = true;
//
//		new Thread() { // 录屏
//			public void run() {
//				while (isRecording) {
//					Vector<String> v = view_paint.getCmds();
//					for (int i = 0; i < v.size(); i++) {
//						String content = v.get(i);
//						app.send(username, content);
//					}
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}.start();
//	}

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