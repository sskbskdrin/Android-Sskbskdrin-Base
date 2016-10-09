package cn.sskbskdrin.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayke on 2015/6/2.
 * list 适配器基类
 */
public abstract class IBaseAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> mList;
	private int mResId;

	public IBaseAdapter(Context context, List<T> list) {
		this(context, list, 0);
	}

	public IBaseAdapter(Context context, List<T> list, int resId) {
		mContext = context;
		mList = list;
		if (mList == null) {
			mList = new ArrayList<>();
		}
		mResId = resId;
	}

	public List<T> getList() {
		return mList;
	}

	public void updateList(List<T> list) {
		mList = list;
		if (mList == null) {
			mList = new ArrayList<>();
		}
		notifyDataSetChanged();
	}

	public void add(T item) {
		mList.add(item);
		notifyDataSetChanged();
	}

	public void remove(T item) {
		mList.remove(item);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		if (position < mList.size()) {
			mList.remove(position);
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = getViewHolder(parent, getItemViewType(position));
			holder.itemView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		bindViewHolder(holder, getItem(position));
		return holder.itemView;
	}

	public ViewHolder getViewHolder(ViewGroup parent, int viewType) {
		if (mResId <= 0) {
			mResId = getLayoutId(viewType);
		}
		return new ViewHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
	}

	public int getLayoutId(int viewType) {
		return 0;
	}

	public abstract void bindViewHolder(ViewHolder holder, T item);
}
