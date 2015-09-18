package com.bairuitech.blackboard.activity.paint;

/**
 * 老师的绘图
 */
import java.util.Vector;

import com.bairuitech.blackboard.BlackBoardApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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
	public String username="";
	
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
		// Get a reference to our resource table.
		// 绘制自由曲线用的画笔
		myPaint = new Paint();
		myPaint.setAntiAlias(true);
		myPaint.setDither(true);
		myPaint.setColor(myColor);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setStrokeJoin(Paint.Join.ROUND);
		myPaint.setStrokeCap(Paint.Cap.ROUND);
		myPaint.setStrokeWidth(12);
		myPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);

		toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
	}

	private void toast(String info) {
		toast.setText(info);
		toast.show();
	}
	private String linetype = "DrawLine";
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
		myPaint.setStrokeWidth(9);
		this.myPaint = myPaint;
		linetype = "DrawLine";
//		command("e,0,0");
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
//		command("r,0,0");
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
//			command("[wendabang]"+linetype+";[" + line + "]");
			app.send("s38", "[wendabang]"+linetype+";[" + line + "]");
			// command("{\"x\":" + x + ",\"y\":" + y + "}");
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
//		command("c,0,0");
		app.send(username,"[wendabang]PanelClear; \"p\":\"1\"");
		invalidate();
	}

	private Vector<String> cmds = new Vector<String>();

	private void command(String cmd) {

		cmds.add(cmd);
		if (cmd.startsWith("c")) {
			cmds = new Vector<String>();
			cmds.add(cmd);
		}

	}

	public Vector<String> getCmds() {
		return cmds;
	}

}
