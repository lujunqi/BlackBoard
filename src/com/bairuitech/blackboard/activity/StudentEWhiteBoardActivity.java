package com.bairuitech.blackboard.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bairuitech.blackboard.BlackBoardApplication;
import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.activity.paint.TeacherPaintView;
import com.bairuitech.blackboard.adapter.MyBaseAdapter;
import com.bairuitech.blackboard.common.CallBack;

/**
 * 学生 黑板
 * 
 * 
 */
public class StudentEWhiteBoardActivity extends Activity {
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

		// Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}

	private static final int CAMERA_REQUEST = 1888;

	public void takephoto(View v) {// 插入照片

		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 构造intent
		startActivityForResult(cameraIntent, CAMERA_REQUEST);// 发出intent，并要求返回调用结果
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST) {
			if (data != null) {
				Bitmap photo = (Bitmap) data.getExtras().get("data");
				view_paint.drawBitmap(photo);
			}
		}
	}

	public void edit(View v) {
		view_paint.edit(myColor);
	}

	public void eraser(View v) {
		view_paint.eraser();
	}

	public void shape(View v) {
		final String[] items = new String[] { "9", "15", "20", "25" };
		new AlertDialog.Builder(this).setTitle("选择线宽")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int which) {
						view_paint.setStrokeWidth(Integer
								.parseInt(items[which]));
						view_paint.edit(myColor);
					}

				}).setNegativeButton("确定", null).show();

	}

	public void color(View v) {
		Map<String, Object> map = new HashMap<String, Object>();
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		map = new HashMap<String, Object>();
		map.put("color", Color.RED);
		map.put("name", "红色");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("color", Color.BLUE);
		map.put("name", "蓝色");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("color", Color.YELLOW);
		map.put("name", "黄色");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("color", Color.WHITE);
		map.put("name", "白色");
		list.add(map);

		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.view_color, null);
		ListView info_list_view = (ListView) view
				.findViewById(R.id.info_list_view);
		MyBaseAdapter adapter = new MyBaseAdapter(context, R.layout.item_color,
				list, new CallBack() {
					@Override
					public void run(Map<String, Object> m) {
						View view = (View) m.get("view");
						@SuppressWarnings("unchecked")
						Map<String, Object> data = (Map<String, Object>) m
								.get("data");
						View myColor = (View) view.findViewById(R.id.myColor);
						TextView myColorText = (TextView) view
								.findViewById(R.id.myColorText);
						myColorText.setText(data.get("name") + "");
						myColor.setBackgroundColor(Integer.parseInt(data
								.get("color") + ""));
					}
				});
		info_list_view.setAdapter(adapter);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setView(view);
		final AlertDialog ad = builder.create();
		ad.show();

		info_list_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long lng) {
				Map<String, Object> map = list.get(arg2);
				int clr = Integer.parseInt("" + map.get("color"));
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
		System.out.println("onDestroy====");

		super.onDestroy();
	}
}