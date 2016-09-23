package cn.sskbskdrin.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import cn.sskbskdrin.base.IBaseAdapter;
import cn.sskbskdrin.utils.L;

/**
 * Created by ayke on 2016/9/1 0001.
 */
public class BaseRecyclerView extends RecyclerView {

	private static final String TAG = "BaseRecyclerView";

	private Adapter<cn.sskbskdrin.base.ViewHolder> mAdapter;

	private OnItemClickListener mOnItemClickListener;
	private OnItemLongClickListener mOnItemLongClickListener;

	public interface OnItemClickListener {
		void onItemClick(RecyclerView parent, View view, int position);
	}

	public interface OnItemLongClickListener {
		void onItemLongClick(RecyclerView parent, View view, int position);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickListener = listener;
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		mOnItemLongClickListener = listener;
	}

	public BaseRecyclerView(Context context) {
		this(context, null);
	}

	public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		addItemDecoration(new RecyclerDecoration(context));
		// 设置item动画
		setItemAnimator(new DefaultItemAnimator());

		final GestureDetector mGestureDetector = new GestureDetector(context,
				new GestureDetector.SimpleOnGestureListener() { //这里选择SimpleOnGestureListener实现类，可以根据需要选择重写的方法
					//单击事件
					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						View childView = findChildViewUnder(e.getX(), e.getY());
						if (childView != null && mOnItemClickListener != null) {
							mOnItemClickListener.onItemClick(BaseRecyclerView.this, childView, getChildLayoutPosition(childView));
							return true;
						}
						return false;
					}

					//长按事件
					@Override
					public void onLongPress(MotionEvent e) {
						View childView = findChildViewUnder(e.getX(), e.getY());
						if (childView != null && mOnItemLongClickListener != null) {
							L.d(TAG, "onLongPress");
							mOnItemLongClickListener.onItemLongClick(BaseRecyclerView.this, childView, getChildLayoutPosition(childView));
						}
					}
				});
		addOnItemTouchListener(new OnItemTouchListener() {
			@Override
			public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
				View childView = findChildViewUnder(e.getX(), e.getY());
				switch (e.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if (childView != null) {
							childView.setPressed(true);
						}
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						if (childView != null) {
							childView.setPressed(false);
						}
						break;
				}
				return mGestureDetector.onTouchEvent(e);
			}

			@Override
			public void onTouchEvent(RecyclerView rv, MotionEvent e) {
				L.d(TAG, "onTouchEvent action = " + e.getAction());
			}

			@Override
			public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
				L.d(TAG, "onRequestDisallowInterceptTouchEvent action = " + disallowIntercept);
			}
		});
	}

	@Override
	public void onScrollStateChanged(int state) {
//		Log.d(TAG, "onScrollStateChanged: state=" + state);
	}

	@Override
	public void onScrolled(int dx, int dy) {
//		int lastPosi = ((LinearLayoutManager) getLayoutManager()).findLastCompletelyVisibleItemPosition();
//		int firstPosi = ((LinearLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition();
//		Log.d(TAG, "onScrollStateChanged: last=" + lastPosi + " first=" + firstPosi);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		boolean flag = super.onTouchEvent(e);
		return flag;
	}

	public void setBaseAdapter(IBaseAdapter adapter) {
		mAdapter = new RecyclerViewAdapter(adapter);
		setAdapter(mAdapter);
	}

	public IBaseAdapter getBaseAdapter() {
		return ((RecyclerViewAdapter) mAdapter).getAdapter();
	}

}
