package com.bairuitech.blackboard.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bairuitech.blackboard.BlackBoardApplication;
import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.activity.TeacterListActivity;
import com.bairuitech.blackboard.adapter.MyBaseAdapter;
import com.bairuitech.blackboard.common.CallBack;
import com.bairuitech.blackboard.common.JsonUtil;
import com.bairuitech.blackboard.common.Utils;
import com.bairuitech.blackboard.data.ClassEnum;

public class FindTeacher extends RelativeLayout {
	public static final String TAG = "FindTeacher";
	private Context context;
	private GridView gv_grid;
	private Map<Button, CharSequence> btnText = new HashMap<Button, CharSequence>();
	private Map<Button, String[]> btnList = new HashMap<Button, String[]>();
	private String grade = "无";
	private MyBaseAdapter adapter;
	private TextView t_id ; 
	public FindTeacher(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.widget_find_teacher,
				this, true);
		init();
	}
	

	private void init() {
		ImageView lp = (ImageView) this.findViewById(R.id.lp);
		lp.setOnClickListener(lpClick);
		t_id = (TextView) this.findViewById(R.id.t_id);
		Button min = (Button) this.findViewById(R.id.min);
		btnList.put(min, new String[] { "小学四年级", "小学五年级", "小学六年级" });
		min.setOnClickListener(minClick);
		btnText.put(min, min.getText());

		Button cen = (Button) this.findViewById(R.id.cen);
		btnList.put(cen, new String[] { "七年级", "八年级", "九年级" });
		cen.setOnClickListener(minClick);
		btnText.put(cen, cen.getText());

		Button max = (Button) this.findViewById(R.id.max);
		btnList.put(max, new String[] { "高中一年级", "高中二年级", "高中三年级" });
		max.setOnClickListener(minClick);
		btnText.put(max, max.getText());

		gv_grid = (GridView) this.findViewById(R.id.gv_grid);
		adapter = new MyBaseAdapter(context, R.layout.item_gridview,
				initDate(""), cb);
		gv_grid.setAdapter(adapter);
		gv_grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if ("无".equals(grade)) {
					showShortToast("请您先选择辅导年级");
					return;
				} else {
					// 跳转到老师列表
					TextView tv_title = (TextView) v
							.findViewById(R.id.tv_title);
					Intent intent = new Intent(context,
							TeacterListActivity.class);
					// intent.putExtra("Grade", grade);
					// intent.putExtra("Subject", tv_title.getText()+"");
					intent.putExtra("Grade", "1");
					intent.putExtra("Subject", "1");
					intent.putExtra("Action", "SearchTeacherByGrade");
					context.startActivity(intent);
				}
			}

		});
	}

	private CallBack cb = new CallBack() {
		@Override
		public void run(Map<String, Object> m) {
			View v = (View) m.get("view");
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) m.get("data");

			TextView tv = (TextView) v.findViewById(R.id.tv_title);
			ImageView iv = (ImageView) v.findViewById(R.id.iv_icon);
			tv.setText(map.get("title") + "");
			int drawableId = context.getResources().getIdentifier(
					map.get("icon") + "", "drawable", context.getPackageName());
			
			iv.setImageResource(drawableId);
		}
	};
	public Toast mToast;

	protected void showShortToast(String pMsg) {
		if (mToast != null) {
			mToast.setText(pMsg);
		} else {
			mToast = Toast.makeText(context, pMsg, Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	private List<Map<String, Object>> initDate(String key) {
		// 小学
		String json1 = "[{\"title\":\"语文\",\"icon\":\"yuwen\"},"
				+ "{\"title\":\"数学\",\"icon\":\"math\"},"
				+ "{\"title\":\"英语\",\"icon\":\"english\"},"
				+ "{\"title\":\"科学\",\"icon\":\"kexue\"},"
				+ "{\"title\":\"吐心事\",\"icon\":\"xinshi\"}]";

		Map<String, String> m = new HashMap<String, String>();
		m = ClassEnum.json;

		try {
			if (m.containsKey(key)) {
				return JsonUtil.str2JsonList(m.get(key));
			} else {
				return JsonUtil.str2JsonList(json1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String, Object>>();
		}
	}

	private OnClickListener lpClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 跳转到老师列表

			Intent intent = new Intent(context, TeacterListActivity.class);
			intent.putExtra("SearchStr", Utils.toString(t_id.getText()));
			intent.putExtra("Action", "SearchTeacherByName");
			context.startActivity(intent);
		}

	};

	private OnClickListener minClick = new OnClickListener() {
		private String[] items;

		@Override
		public void onClick(final View v) {
			Button b = (Button) v;
			items = btnList.get(b);
			new AlertDialog.Builder(context)
					.setTitle("请选择")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(items, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									for (Entry<Button, CharSequence> en : btnText
											.entrySet()) {
										en.getKey().setText(en.getValue());
									}
									Button b = (Button) v;
									b.setText(items[which]);
									grade = items[which];
									dialog.dismiss();
									adapter = new MyBaseAdapter(context,
											R.layout.item_gridview,
											initDate(grade), cb);
									gv_grid.setAdapter(adapter);
									adapter.notifyDataSetChanged();
								}
							}).setNegativeButton("取消", null).show();

		}
	};

}
