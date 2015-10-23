package com.bairuitech.blackboard.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
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
	private ImageView shape;

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
		shape = (ImageView) this.findViewById(R.id.share);
		myColor = 0xffffffff;
	}

	private void initLogic() {

		view_paint.touch = true;

		// Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}

	private File tempFile = new File(Environment.getExternalStorageDirectory(),
			getPhotoFileName());
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	public void takephoto(View v) {// 插入照片
		AlertDialog.Builder builder = new AlertDialog.Builder(context);  
        builder.setIcon(R.drawable.icon);  
        builder.setTitle("插入照片");  
        builder.setMessage("选择照片来源");  
        builder.setPositiveButton("拍照",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                    	choseImageFromCamera();  
                    }  
                });  
        builder.setNeutralButton("本地照片",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                    	choseImageFromGallery();  
                    }  
                });  
      
        builder.show(); 
		
		// 
	}

	private void choseImageFromCamera() {
		Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
		startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
	}

	private void choseImageFromGallery() {
		Intent intentFromGallery = new Intent();
		// 设置文件类型
		intentFromGallery.setType("image/*");
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, PHOTO_REQUEST_GALLERY);
	}

	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
			startPhotoZoom(Uri.fromFile(tempFile));
			break;
		case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
			// 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
			if (data != null)
				startPhotoZoom(data.getData());
			break;
		case PHOTO_REQUEST_CUT:// 返回的结果
			if (data != null) {
				Bundle bundle = data.getExtras();
				Bitmap photo = bundle.getParcelable("data");
				view_paint.drawBitmap(photo);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	public void edit(View v) {
		view_paint.edit(myColor);
	}

	public void eraser(View v) {
		view_paint.eraser();
	}

	public void shape(View v) {
		final String[] items = new String[] { "自由线", "矩形", "圆形" };
		new AlertDialog.Builder(this).setTitle("选择形状")
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int which) {
						if (items[which].equals("矩形")) {
							view_paint.drawRect("RECT");
						} else if (items[which].equals("圆形")) {
							view_paint.drawRect("CIRCLE");
						} else if (items[which].equals("自由线")) {
							view_paint.edit(myColor);
						}
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