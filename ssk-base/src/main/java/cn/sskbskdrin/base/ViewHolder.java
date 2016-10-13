package cn.sskbskdrin.base;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ayke on 2016/9/1 0001.
 */
public class ViewHolder extends RecyclerView.ViewHolder {
	private SparseArray<View> map;

	ViewHolder(View view) {
		super(view);
		map = new SparseArray<>();
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

	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int id) {
		int index = map.indexOfKey(id);
		if (index < 0) {
			map.put(id, itemView.findViewById(id));
		}
		return (T) map.get(id);
	}
}
