package cn.sskbskdrin.recycler;

import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.view.ViewGroup;

import cn.sskbskdrin.base.IBaseAdapter;
import cn.sskbskdrin.base.ViewHolder;

public class RecyclerViewAdapter extends Adapter<ViewHolder> {

	private IBaseAdapter mBaseAdapter;
	private DataSetObserver mDataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			notifyDataSetChanged();
		}

		@Override
		public void onInvalidated() {
		}
	};

	public RecyclerViewAdapter(IBaseAdapter adapter) {
		mBaseAdapter = adapter;
	}

	public IBaseAdapter getAdapter() {
		return mBaseAdapter;
	}

	@Override
	public int getItemCount() {
		return mBaseAdapter.getCount();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return mBaseAdapter.getViewHolder(parent, viewType);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		mBaseAdapter.bindViewHolder(holder, mBaseAdapter.getItem(position));
	}

	@Override
	public long getItemId(int position) {
		return mBaseAdapter.getItemId(position);
	}

	@Override
	public int getItemViewType(int position) {
		return mBaseAdapter.getItemViewType(position);
	}

	@Override
	public void registerAdapterDataObserver(AdapterDataObserver observer) {
		mBaseAdapter.registerDataSetObserver(mDataSetObserver);
		super.registerAdapterDataObserver(observer);
	}

	@Override
	public void setHasStableIds(boolean hasStableIds) {
		super.setHasStableIds(mBaseAdapter.hasStableIds());
	}

	@Override
	public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
		mBaseAdapter.unregisterDataSetObserver(mDataSetObserver);
		super.unregisterAdapterDataObserver(observer);
	}

}
