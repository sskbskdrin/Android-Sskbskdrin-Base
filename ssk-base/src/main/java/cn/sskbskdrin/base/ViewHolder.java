package cn.sskbskdrin.base;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by ayke on 2016/9/1 0001.
 */
public class ViewHolder extends RecyclerView.ViewHolder {
	HashMap<Integer, View> map;

	ViewHolder(View view) {
		super(view);
		map = new HashMap<>();
	}

	public void setText(int id, CharSequence text) {
		TextView tv = getView(id);
		tv.setText(text);
	}

	public void setTextColor(int id, int color) {
		TextView tv = getView(id);
		tv.setTextColor(color);
	}

	public void setImageResource(int id, int res) {
		ImageView view = getView(id);
		view.setImageResource(res);
	}

	public void setBackground(int id, int res) {
		View view = getView(id);
		view.setBackgroundResource(res);
	}

	public void setBackgroundDrawable(int id, Drawable drawable) {
		View view = getView(id);
		view.setBackgroundDrawable(drawable);
	}

	public <T extends View> T getView(int id) {
		if (!map.containsKey(id)) {
			map.put(id, itemView.findViewById(id));
		}
		return (T) map.get(id);
	}
}
