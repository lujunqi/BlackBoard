package com.bairuitech.blackboard.activity.paint;

/**
 * 学生 绘图
 */
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.bairuitech.blackboard.BlackBoardApplication;
import com.bairuitech.blackboard.common.JsonUtil;
import com.bairuitech.blackboard.common.ScreenShot;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;

public class StudentPaintView extends View {
	// 画笔，定义绘制属性
	private Paint myPaint;
	private Paint mBitmapPaint;

	// 绘制路径
	private Path myPath;

	// 画布及其底层位图
	private Bitmap myBitmap;
	private Canvas myCanvas;

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	// 记录宽度和高度
	private int mWidth;
	private int mHeight;
	private Context context;
	private BlackBoardApplication app;
	private int myColor = 0xffffffff;
	private int strokeWidth = 9;
	String username = "t1";
	public boolean isRecording = true;// 是否录放的标记

	public StudentPaintView(Context context) {
		super(context);

		this.context = context;
		initialize();
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;

		app.send(username, "[wendabang]LineWidth; \"w\":\"" + strokeWidth
				+ "\"");
	}

	@SuppressLint("MissingSuperCall")
	@Override
	protected void onDetachedFromWindow() {
		System.out.println("==========");
	}

	public StudentPaintView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		app = (BlackBoardApplication) context.getApplicationContext();
		initialize();
	}

	public StudentPaintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		app = (BlackBoardApplication) context.getApplicationContext();
		initialize();
	}

	/**
	 * 初始化工作
	 */
	public void initialize() {
		System.out.println("=================100");
		// 绘制自由曲线用的画笔
		myPaint = new Paint();
		myPaint.setAntiAlias(true);
		myPaint.setDither(true);
		myPaint.setColor(myColor);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setStrokeJoin(Paint.Join.ROUND);
		myPaint.setStrokeCap(Paint.Cap.ROUND);
		myPaint.setStrokeWidth(strokeWidth);
		myPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);

		isRecording = true;

//		PlayTask play = new PlayTask();
//		play.execute();
	}

	/**
	 * edit 普通笔
	 */
	public void edit(int myColor) {
		this.myColor = myColor;
		Paint myPaint = new Paint();
		myPaint.setAntiAlias(true);
		myPaint.setDither(true);
		myPaint.setColor(myColor);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setStrokeJoin(Paint.Join.ROUND);
		myPaint.setStrokeCap(Paint.Cap.ROUND);
		myPaint.setStrokeWidth(strokeWidth);
		this.myPaint = myPaint;
		linetype = "DrawLine";
		postInvalidate();
	}

	// 橡皮
	public void eraser() {
		Paint m_eraserPaint = new Paint();
		m_eraserPaint.setAntiAlias(true);
		m_eraserPaint.setDither(true);
		m_eraserPaint.setColor(0xFF000000);
		m_eraserPaint.setStrokeWidth(50);
		m_eraserPaint.setStyle(Paint.Style.STROKE);
		m_eraserPaint.setStrokeJoin(Paint.Join.ROUND);
		m_eraserPaint.setStrokeCap(Paint.Cap.SQUARE);
		m_eraserPaint.setXfermode(new PorterDuffXfermode(
				PorterDuff.Mode.DST_OUT));
		this.myPaint = m_eraserPaint;
		postInvalidate();
		linetype = "EraseLine";
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;
		myBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		myCanvas = new Canvas(myBitmap);
	}

	private String linetype = "DrawLine";
	String line = "";

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start2(x, y);
			invalidate();
			line = "{\"x\":\"" + x + "\",\"y\":\"" + y + "\"},";
			
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move2(x, y);
			line += "{\"x\":\"" + x + "\",\"y\":\"" + y + "\"},";
			invalidate();

			break;
		case MotionEvent.ACTION_UP:
			touch_up2();
			invalidate();
			line += "{\"x\":\"" + x + "\",\"y\":\"" + y + "\"}";

//			app.send(username, "[wendabang]" + linetype + ";[" + line + "]");

			break;
		}
		return false;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 背景颜色
		// canvas.drawColor(getResources().getColor(R.color.blue_dark));
		// 如果不调用这个方法，绘制结束后画布将清空
		canvas.drawBitmap(myBitmap, 0, 0, mBitmapPaint);

		// 绘制路径
		canvas.drawPath(myPath, myPaint);
	}

	private void touch_start(float x, float y) {
		
		myPath.reset();
		myPath.moveTo(x, y);
		mX = x;
		mY = y;
		postInvalidate();
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			myPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
		postInvalidate();
	}

	private void touch_up() {

		myPath.lineTo(mX, mY);
		myCanvas.drawPath(myPath, myPaint);
		myPath.reset();
		postInvalidate();
	}

	private void touch_start2(float x, float y) {
		System.out.println("==========217");
		myPath.reset();
		myPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move2(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			myPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up2() {
		myPath.lineTo(mX, mY);
		// commit the path to our offscreen
		myCanvas.drawPath(myPath, myPaint);
		myPath.reset();
	}
	
	/**
	 * 清除整个图像
	 */
	public void clear() {
		// 清除方法1：重新生成位图
		myBitmap = Bitmap
				.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
		myCanvas = new Canvas(myBitmap);

		myPath.reset();
		// command("c,0,0");
		app.send("t1", "[wendabang]PanelClear; \"p\":\"1\"");
		invalidate();

	}

	// 插入图片
	public void drawBitmap(Bitmap photo) {

		try {
			UUID uuid = UUID.randomUUID();
			File file = File.createTempFile(uuid.toString(), "jpg");
			ScreenShot.savePic(photo, file);
			myCanvas.drawBitmap(photo, mX + 10, mY + 10, myPaint);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	class PlayTask extends AsyncTask<Void, EMMessage, Void> {
		@SuppressLint("NewApi")
		@Override
		protected Void doInBackground(Void... args) {
			try {
				EMChatManager.getInstance().registerEventListener(
						new EMEventListener() {
							@Override
							public void onEvent(EMNotifierEvent event) {
								EMMessage msg = (EMMessage) event.getData();
								publishProgress(msg);
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(EMMessage... progress) {
			try {
				EMMessage msg = progress[0];
				EMMessage.Type type = msg.getType();
				System.out.println(type + "=================266");
				if (type == EMMessage.Type.TXT) {
					TextMessageBody mBody = (TextMessageBody) msg.getBody();
					String info = mBody.getMessage();
					if (info.startsWith("[wendabang]")) {// 命令

						if (info.startsWith("[wendabang]DrawLine")) {// 普通笔
							edit(myColor);
							String[] buff = info.split(";");
							if (buff.length == 2) {
								List<Map<String, Object>> list = JsonUtil
										.str2JsonList(buff[1]);
								for (int i = 0; i < list.size(); i++) {
									Map<String, Object> map = list.get(i);
									float x = Float.parseFloat(map.get("x")
											+ "");
									float y = Float.parseFloat(map.get("y")
											+ "");
									if (i == 0) {
										touch_start(x, y);

									} else if (i == list.size() - 1) {
										touch_up();

									} else {
										touch_move(x, y);

									}
								}
							}
						}
						if (info.startsWith("[wendabang]EraseLine")) {// 橡皮
							eraser();
							postInvalidate();
							String[] buff = info.split(";");
							if (buff.length == 2) {
								List<Map<String, Object>> list = JsonUtil
										.str2JsonList(buff[2]);
								for (int i = 0; i < list.size(); i++) {
									Map<String, Object> map = list.get(i);
									float x = Float.parseFloat(map.get("x")
											+ "");
									float y = Float.parseFloat(map.get("y")
											+ "");
									if (i == 0) {
										touch_start(x, y);
										postInvalidate();
									} else if (i == list.size() - 1) {
										touch_up();
										postInvalidate();
									} else {
										touch_move(x, y);
										postInvalidate();
									}
								}
							}
						}

						if (info.startsWith("[wendabang]PanelClear")) {// 清屏幕
							System.out.println("325====================="
									+ info);
							clear();
							postInvalidate();
						}
						if (info.startsWith("[wendabang]LineColor")) {// 颜色
							String[] buff = info.split(";");
							if (buff.length == 2) {
								int color = Integer.parseInt(buff[1]);
								myColor = color;
							}
							postInvalidate();
						}
						if (info.startsWith("[wendabang]LineWidth")) {// 线宽
							String[] buff = info.split(";");
							if (buff.length == 2) {
								Map<String, Object> m = JsonUtil.str2Json("{"
										+ buff[1] + "}");
								int w = Integer.parseInt(m.get("w") + "");
								strokeWidth = w;
							}
							postInvalidate();
						}
					}
				} else if (type == EMMessage.Type.IMAGE) {// 贴图
					System.out.println("======================349");
					ImageMessageBody imgBody = (ImageMessageBody) msg.getBody();
					String msgtype = msg.getStringAttribute("type");
					if ("wendabang".equals(msgtype)) {
						String filePath = imgBody.getLocalUrl();
						Bitmap bm = getBitmapFromFile(new File(filePath),
								imgBody.getWidth(), imgBody.getHeight());
						drawBitmap(bm);
						postInvalidate();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onProgressUpdate(progress);
		}

		protected void onPostExecute(Void result) {
		}

		protected void onPreExecute() {
		}

	}

	public Bitmap getBitmapFromFile(File dst, int width, int height) {
		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(dst.getPath(), opts);
				// 计算图片缩放比例
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			try {
				return BitmapFactory.decodeFile(dst.getPath(), opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
