package com.bairuitech.blackboard.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bairuitech.blackboard.BlackBoardApplication;
import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.activity.paint.TeacherPaintView;
import com.bairuitech.blackboard.adapter.MyBaseAdapter;
import com.bairuitech.blackboard.common.CallBack;

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
	private ImageView color;
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
		color = (ImageView) this.findViewById(R.id.color);
		
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

		// send();

	}

	public void takephoto(View v) {

	}

	public void edit(View v) {
		view_paint.edit(myColor);
	}

	public void eraser(View v) {
		view_paint.eraser();
	}

	public void color(View v) {
		Map<String,Object> map = new HashMap<String,Object>();
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		map = new HashMap<String,Object>();
		map.put("color", Color.RED);
		map.put("name", "红色");
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("color", Color.BLUE);
		map.put("name", "蓝色");
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("color", Color.YELLOW);
		map.put("name", "黄色");
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("color", Color.WHITE);
		map.put("name", "白色");
		list.add(map);
		
		
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.view_color, null);
		ListView info_list_view = (ListView) view
				.findViewById(R.id.info_list_view);
		MyBaseAdapter adapter = new MyBaseAdapter(context,
				R.layout.item_color, list, new CallBack() {
					@Override
					public void run(Map<String, Object> m) {
						View view = (View) m.get("view");
						@SuppressWarnings("unchecked")
						Map<String, Object> data = (Map<String, Object>) m
								.get("data");
						View myColor = (View) view.findViewById(R.id.myColor);
						TextView myColorText = (TextView) view.findViewById(R.id.myColorText);
						myColorText.setText(data.get("name")+"");
						myColor.setBackgroundColor(Integer.parseInt(data.get("color")+""));
					}
				});
		info_list_view.setAdapter(adapter);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setView(view);
		final AlertDialog ad =builder.create(); 
		ad.show();
		
		info_list_view.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long lng) {
				Map<String,Object> map = list.get(arg2);
				int clr = Integer.parseInt(""+map.get("color"));
				myColor = clr;
				color.setBackgroundColor(clr);
				view_paint.edit(myColor);
				ad.dismiss();
			}
		});
		

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