package cn.sskbskdrin.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @author ayke
 */
public class RecyclerDecoration extends RecyclerView.ItemDecoration {

	private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
	private Drawable mDivider;

	private int mWidth = 1;
	private int mHeight = 1;

	public RecyclerDecoration(Context context) {
		final TypedArray a = context.obtainStyledAttributes(ATTRS);
		mDivider = a.getDrawable(0);
		a.recycle();
	}

	public void setDrawable(Drawable drawable) {
		mDivider = drawable;
	}

	public void setWidth(int width) {
		mWidth = width;
	}

	public void setHeight(int height) {
		mHeight = height;
	}

	@Override
	public void onDrawOver(Canvas c, RecyclerView parent, State state) {
		if (getSpanCount(parent) == 1) {
			drawListDecoration(c, parent);
		} else {
			drawGridDecoration(c, parent);
		}
	}

	private void drawListDecoration(Canvas c, RecyclerView parent) {
		int childCount = parent.getChildCount();
		final int itemCount = parent.getAdapter().getItemCount();
		final boolean isVertical = isVertical(parent);
		Rect rect = new Rect();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			int position = params.getViewLayoutPosition();
			if (isVertical) {
				rect.left = child.getLeft();
				rect.top = child.getBottom() + params.bottomMargin;
				rect.right = child.getRight();
				rect.bottom = rect.top + getHeight();
			} else {
				rect.left = child.getRight() + params.rightMargin;
				rect.top = child.getTop();
				rect.right = rect.left + getWidth();
				rect.bottom = child.getBottom();
			}
			if (position == itemCount - 1)
				rect.setEmpty();
			mDivider.setBounds(rect);
			mDivider.draw(c);
		}
	}

	private void drawGridDecoration(Canvas c, RecyclerView parent) {
		final int childCount = parent.getChildCount();
		final int itemCount = parent.getAdapter().getItemCount();
		final int spanCount = getSpanCount(parent);
		final boolean isVertical = isVertical(parent);
		final int lastRowPosition = getLastRowPosition(itemCount, spanCount);
		Rect rect = new Rect();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			int position = params.getViewLayoutPosition();
			if (isDrawBottom(position, itemCount, spanCount, isVertical)) {
				rect.left = child.getLeft() - params.leftMargin;
				rect.top = child.getBottom() + params.bottomMargin;
				rect.right = child.getRight() + params.rightMargin + getWidth();
				if (lastRowPosition <= position)
					rect.right -= getWidth();
				rect.bottom = rect.top + getHeight();
				mDivider.setBounds(rect);
				mDivider.draw(c);
			}
			if (isDrawRight(position, itemCount, spanCount, isVertical)) {
				rect.left = child.getRight() + params.rightMargin;
				rect.top = child.getTop() - params.topMargin;
				rect.right = rect.left + getWidth();
				rect.bottom = child.getBottom() + params.bottomMargin;
				mDivider.setBounds(rect);
				mDivider.draw(c);
			}
		}
	}

	/**
	 * 获取列数
	 */
	private int getSpanCount(RecyclerView parent) {
		int spanCount = 1;
		LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {
			spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
		}
		return spanCount;
	}

	private boolean isVertical(RecyclerView parent) {
		LayoutManager layoutManager = parent.getLayoutManager();
		int orientation = OrientationHelper.VERTICAL;
		if (layoutManager instanceof LinearLayoutManager) {
			orientation = ((LinearLayoutManager) layoutManager).getOrientation();
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
		}
		return orientation == OrientationHelper.VERTICAL;
	}

	private boolean isDrawRight(int pos, int childCount, int spanCount, boolean isVertical) {
		if (pos != childCount - 1) {
			if (isVertical) {
				return (pos + 1) % spanCount != 0;
			} else {
				if (pos < getLastRowPosition(childCount, spanCount))
					return true;
			}
		}
		return false;
	}

	private boolean isDrawBottom(int pos, int childCount, int spanCount, boolean isVertical) {
		if (pos != childCount - 1) {
			if (!isVertical) {
				return (pos + 1) % spanCount != 0;
			} else {
				if (pos < getLastRowPosition(childCount, spanCount))
					return true;
			}
		}
		return false;
	}

	private int getLastRowPosition(int childCount, int spanCount) {
		int row = childCount % spanCount;
		int line = childCount / spanCount;
		int position;
		if (row == 0) {
			position = (line - 1) * spanCount;
		} else {
			position = line * spanCount;
		}
		return position;
	}

	private int getWidth() {
		int width = mDivider.getIntrinsicWidth();
		if (width == -1)
			width = mWidth;
		return width;
	}

	private int getHeight() {
		int height = mDivider.getIntrinsicHeight();
		if (height == -1) {
			height = mHeight;
		}
		return height;
	}

	@Override
	public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
		int spanCount = getSpanCount(parent);
		int childCount = parent.getAdapter().getItemCount();
		boolean isVertical = isVertical(parent);
		int row = itemPosition % spanCount;
		int left = 0;
		int top = 0;
		int right = 0;
		int bottom = 0;
		if (spanCount == 1) {
			if (itemPosition != childCount - 1) {
				if (isVertical)
					bottom = getHeight();
				else
					right = getWidth();
			}
		} else {
			if (isVertical) {
				float step = getWidth() * 1f / spanCount;
				left = (int) (step * row);
				right = (int) Math.ceil(step * (spanCount - row - 1));
				bottom = getHeight();
			} else {
				float step = getHeight() * 1f / spanCount;
				top = (int) (step * row);
				bottom = (int) Math.ceil(step * (spanCount - row - 1));
				right = getWidth();
			}
		}
		outRect.set(left, top, right, bottom);
//		System.out.println("itemPosition=" + itemPosition + " rect=" + outRect);
	}
}
