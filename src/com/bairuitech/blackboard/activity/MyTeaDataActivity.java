package com.bairuitech.blackboard.activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bairuitech.blackboard.BlackBoardApplication;
import com.bairuitech.blackboard.R;
import com.bairuitech.blackboard.common.CallBack;
import com.bairuitech.blackboard.common.HttpTask;
import com.bairuitech.blackboard.common.HttpUtil;
import com.bairuitech.blackboard.common.JsonUtil;
import com.bairuitech.blackboard.common.Utils;

public class MyTeaDataActivity extends Activity {
	private Context context;
	private static final String TAG = "MyDataActivity";
	private TextView name, nickname, sex, bir, area, school, grade, email,
			phone, learn_coin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydata);
		context = this;
		initView();
		initLogic();
	}

	private void initView() {
		name = (TextView) this.findViewById(R.id.name);
		nickname = (TextView) this.findViewById(R.id.nickname);
		sex = (TextView) this.findViewById(R.id.sex);
		bir = (TextView) this.findViewById(R.id.bir);
		area = (TextView) this.findViewById(R.id.area);
		school = (TextView) this.findViewById(R.id.school);
		grade = (TextView) this.findViewById(R.id.grade);
		email = (TextView) this.findViewById(R.id.email);
		phone = (TextView) this.findViewById(R.id.phone);
		learn_coin = (TextView) this.findViewById(R.id.learn_coin);

	}

	@SuppressWarnings("unchecked")
	private void initLogic() {
		Map<String, String> params = new HashMap<String, String>();
		BlackBoardApplication app = (BlackBoardApplication) this
				.getApplicationContext();
		String user_id = Utils.toString(app.get("UserID"));
		params.put("TeacherID", user_id);
		params.put("Action", "GetTeacherBaseInfo");

		String url = HttpUtil.URL;
		new HttpTask(url, context, cb).execute(params);

		name.setOnClickListener(btn_click);
		nickname.setOnClickListener(btn_click);
		sex.setOnClickListener(btn_click);
		bir.setOnClickListener(btn_click);
		area.setOnClickListener(btn_click);
		school.setOnClickListener(btn_click);
		grade.setOnClickListener(btn_click);
		email.setOnClickListener(btn_click);
		phone.setOnClickListener(btn_click);
		learn_coin.setOnClickListener(btn_click);

	}

	private View.OnClickListener btn_click = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d(TAG, (v == name) + "========");
			if (v == name) {
				input("姓名", (TextView) v);
			}
			if (v == nickname) {
				input("昵称", (TextView) v);
			}
			if (v == sex) {
				radio("性别", (TextView) v, "不确定", "男", "女");
			}
			if (v == bir) {
				date((TextView) v);
			}
			if (v == area) {
				input("地址", (TextView) v);
			}
			if (v == school) {
				input("学校", (TextView) v);
			}
			if (v == grade) {
				input("班级", (TextView) v);
			}
			if (v == email) {
				input("邮箱", (TextView) v);
			}
			if (v == phone) {
				input("电话", (TextView) v, InputType.TYPE_CLASS_PHONE);
			}
		}

	};

	private void input(String title, final TextView tv, int inputType) {
		final EditText texta = new EditText(context);
		texta.setInputType(inputType);
		texta.setText(tv.getText());
		new AlertDialog.Builder(context).setTitle(title)
				.setIcon(android.R.drawable.ic_dialog_info).setView(texta)
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv.setText(texta.getText());
						dialog.dismiss();
					}
				}).setNegativeButton("取消", null).show();
	}

	private void input(String title, final TextView tv) {
		input(title, tv, InputType.TYPE_CLASS_TEXT);
	}

	private void radio(String title, final TextView tv, final String... items) {
		new AlertDialog.Builder(context)
				.setTitle(title)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(items, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								tv.setText(items[which]);
								dialog.dismiss();
							}
						}).setNegativeButton("取消", null).show();
	}

	private void date(final TextView tv) {
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		String[] dates = Utils.toString(tv.getText()).split("-");
		if (dates.length == 3) {
			mYear = Integer.parseInt(dates[0]);
			mMonth = Integer.parseInt(dates[1]) - 1;
			mDay = Integer.parseInt(dates[2]);

		}
		new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker dp, int year, int month,
					int dayOfMonth) {
				tv.setText(year + "-" + lpad(month + 1) + "-"
						+ lpad(dayOfMonth));
			}
		}, mYear, mMonth, mDay).show();
	}

	private String lpad(int i) {
		if (i < 10) {
			return "0" + i;
		} else {
			return "" + i;
		}
	}

	private CallBack cb = new CallBack() {
		@Override
		public void run(Map<String, Object> m) {

			String json = (String) m.get("data");
			Map<String, Object> result = JsonUtil.str2Json(json);
			if ("10001".equals(Utils.toString(result.get("Result")))) {
				Toast.makeText(context, "查询失败或是查询无记录", Toast.LENGTH_LONG)
						.show();
			} else {
				Utils.setTextView(result, name, "Name");
				Utils.setTextView(result, nickname, "NickyName");
				Utils.setTextView(result, sex, "Sex", "不确定", "男", "女");
				Utils.setTextView(result, phone, "Mobile");
				Utils.setTextView(result, email, "Email");
				Utils.setTextView(result, bir, "Birthday");
				Utils.setTextView(result, school, "School");
				Utils.setTextView(result, grade, "Class");
				Utils.setTextView(result, area, "Address");
				Utils.setTextView(result, learn_coin, "LearnCoin");

			}
		}
	};

	public void tv_finish(View v) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("确认修改个人资料吗？");
		builder.setTitle("编辑");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// * 修改个人信息****************************//

				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public void back(View v) {
		this.finish();
	}
}
