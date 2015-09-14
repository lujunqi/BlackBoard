package com.bairuitech.blackboard.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bairuitech.blackboard.common.CallBack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyBaseAdapter extends BaseAdapter {
	private List<Map<String, Object>> list;
	private LayoutInflater inflater = null;
	private int resourceId;
	private Context context;
	private CallBack cb;

	public MyBaseAdapter(Context context, int resourceId,
			List<Map<String, Object>> list, CallBack cb) {
		this.context = context;
		this.list = list;
		this.resourceId = resourceId;
		this.cb = cb;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Map<String, Object> m = list.get(position);
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(resourceId, null);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", m);
		map.put("view", view);
		
		cb.run(map);
		return view;
	}

}
