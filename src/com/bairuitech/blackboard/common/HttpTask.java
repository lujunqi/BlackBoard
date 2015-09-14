package com.bairuitech.blackboard.common;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HttpTask extends
		AsyncTask<Map<String, String>, String, String> {
	private Context context;
	private static final String TAG = "HttpTask";
	private String _url;
	private CallBack cb;

	public HttpTask(String _url, Context context, CallBack cb) {
		this._url = _url;
		this.context = context;
		this.cb = cb;
	}

	@Override
	protected String doInBackground(Map<String, String>... p) {
		publishProgress("LOADING", "开始");
		String result = null;
		try {
			result = HttpUtil.getString(_url, p[0]);
			publishProgress("END", "完成");
			return result;
		} catch (Exception w) {
			Log.d(TAG, "error", w);
			publishProgress("ERROR", "没有数据");
			publishProgress("END", "完成");
			return null;
		}
	}

	private ProgressDialog pd;

	@Override
	protected void onProgressUpdate(String... values) {
		String p1 = values[0];
		String p2 = values[1];
		if ("ERROR".equals(p1)) {
			Toast.makeText(context, p2, Toast.LENGTH_LONG).show();
		}
		if ("LOADING".equals(p1)) {
			pd = ProgressDialog.show(context, new String("提示"), "正在获取数据，请稍后……",
					true, false);
		}
		if ("END".equals(p1)) {
			pd.dismiss();
		}
	}

	@Override
	protected void onPostExecute(String info) {
		try {
			if (info!=null) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("data", info);
				cb.run(m);
			} else {
				Toast.makeText(context, "没有查询到数据", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception w) {
			Log.d(TAG, "error", w);
		}

	}

}
