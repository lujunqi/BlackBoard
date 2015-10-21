package com.bairuitech.blackboard.activity.paint;

/**
 * 老师的绘图
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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
import com.easemob.exceptions.EaseMobException;

public class TeacherPaintView extends View {
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
	public BlackBoardApplication app;
	private int myColor = 0xffffffff;
	public boolean touch = false;
	public String username = "";
	private boolean isSend = true;

	public TeacherPaintView(Context context) {
		super(context);

		this.context = context;

		initialize();
	}

	public TeacherPaintView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initialize();
	}

	public TeacherPaintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initialize();
	}

	Toast toast;

	/**
	 * 初始化工作
	 */
	@SuppressLint("ShowToast")
	public void initialize() {
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

		toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

		PlayTask play = new PlayTask();
		play.execute();
	}
	// 插入矩形
	public void drawRect(){
		Rect mRect = new Rect(10, 10, 50, 50);
		//开始画矩形了
		myCanvas.drawRect(mRect, myPaint);
		System.out.println("===============118=========");
	}
	// 插入图片
	public void drawBitmap(Bitmap photo) {
		try {
			System.out.println("=line===============111");
			UUID uuid = UUID.randomUUID();
			File file = File.createTempFile(uuid.toString(), "jpg");
			ScreenShot.savePic(photo, file);
			myCanvas.drawBitmap(photo, mX + 10, mY + 10, myPaint);
			if (isSend) {
				app.send(username, file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void toast(String info) {
		toast.setText(info);
		toast.show();
	}

	private String linetype = "DrawLine";
	private int strokeWidth = 9;

	/**
	 * edit 普通笔
	 */
	public void edit(int myColor) {
		this.myColor = myColor;
		if (isSend) {
			 app.send(username, "[wendabang]LineColor;" + myColor);
		}
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
		// command("e,0,0");
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
		linetype = "EraseLine";
		// command("r,0,0");
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;

		myBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		myCanvas = new Canvas(myBitmap);
	}

	String line = "";

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!touch) {
			return false;
		}
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			// command("s," + x + "," + y);
			// command("{\"x\":" + x + ",\"y\":" + y + "}");
			line = "{\"x\":\"" + x + "\",\"y\":\"" + y + "\"},";
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			// command("m," + x + "," + y);
			// command("{\"x\":" + x + ",\"y\":" + y + "}");
			line += "{\"x\":\"" + x + "\",\"y\":\"" + y + "\"},";
			invalidate();

			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			// command("u,0,0");
			line += "{\"x\":\"" + x + "\",\"y\":\"" + y + "\"}";
			// command("[wendabang]"+linetype+";[" + line + "]");
			if (isSend) {
				app.send(username, "[wendabang]" + linetype + ";[" + line + "]");
			}
			break;
		}

		return true;
	}

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
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			myPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
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
		if (isSend) {
			app.send(username, "[wendabang]PanelClear; \"p\":\"1\"");
		}
		invalidate();
	}

	private Vector<String> cmds = new Vector<String>();

	public Vector<String> getCmds() {
		return cmds;
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
		if (isSend)
			app.send(username, "[wendabang]LineWidth; \"w\":\"" + strokeWidth
					+ "\"");
	}

	class PlayTask extends AsyncTask<Void, Object, Void> {
		@SuppressLint("NewApi")
		@Override
		protected Void doInBackground(Void... args) {
			try {
				EMChatManager.getInstance().registerEventListener(
						new EMEventListener() {
							@Override
							public void onEvent(EMNotifierEvent event) {
								EMMessage msg = (EMMessage) event.getData();
								EMMessage.Type type = msg.getType();
								if (type == EMMessage.Type.IMAGE) {// 贴图
									final ImageMessageBody imgBody = (ImageMessageBody) msg
											.getBody();
									try {
										String msgtype = msg
												.getStringAttribute("type");
										if ("wendabang".equals(msgtype)) {
											Bitmap photo = getBitMBitmap(imgBody
													.getRemoteUrl());

											publishProgress(msg, photo);
										}
									} catch (EaseMobException e) {
										e.printStackTrace();
									}

								} else {
									publishProgress(msg);
								}

							}
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Object... progress) {
			try {
				EMMessage msg = (EMMessage) progress[0];
				EMMessage.Type type = msg.getType();

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
						if (info.startsWith("[wendabang]EraseLine")) {// 橡皮
							eraser();
							postInvalidate();
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
							isSend = false;
							clear();
							isSend = true;
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

					String msgtype = msg.getStringAttribute("type");
					if ("wendabang".equals(msgtype)) {
						isSend = false;
						Bitmap photo = (Bitmap) progress[1];
						drawBitmap(photo);
						isSend = true;
						// myCanvas.drawBitmap(photo, 10, 10, myPaint);
						System.out.println(photo + "----------------------"
								+ photo.getHeight() + "===" + photo.getWidth());

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

	public static Bitmap getBitMBitmap(String urlpath) {
		Bitmap map = null;
		try {
			URL url = new URL(urlpath);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream in;
			in = conn.getInputStream();
			map = BitmapFactory.decodeStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
}
