package com.prism.mina.iobuffer.coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.View;

import com.bairuitech.blackboard.common.Utils;

public class ScreenShot {
	private Activity activity;
	public ScreenShot(Activity activity){
		this.activity = activity;
	}
	// 获取指定Activity的截屏，保存到png文件
	public  Bitmap takeScreenShot() {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		
		// 获取屏幕长和高
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		// 去掉标题栏
		// Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
		
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}

	// 保存到sdcard
	private static byte[] savePic(Bitmap b) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		byte[] buff = Utils.gzip(baos.toByteArray());
		System.out.println(baos.size()+"=============[48]");
		return buff;
	}

	
	// 程序入口
	public  byte[] shoot() {
		return ScreenShot.savePic(takeScreenShot());
	}
}
