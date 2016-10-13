package cn.sskbskdrin.picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

import cn.sskbskdrin.utils.DrawUtils;

public class PickerView extends View {
	private static final String TAG = "PickerView";

	private static final int MODE_NONE = 0x01;
	private static final int MODE_MOVE = MODE_NONE << 1;//滑动模式
	private static final int MODE_FLING = MODE_NONE << 2;//惯性模式
	private static final int MODE_SCROLL = MODE_NONE << 3;//调整模式
	private int mFlag = 0;

	private boolean isMeasure = false;

	private static final int TEXT_SIZE = 40;
	private static double STEP_SIZE = 0;//每弧度需移动的相素
	private static double STEP_RADIAN = 0;//= Math.PI / 9;每个Item所占的弧度
	private static double STEP_HALF_RADIAN = 0;//= Math.PI / 9 / 2;
	private static double STEP_SIZE_RADIAN = 0;//移动一个Item所需要的相素

	private int centerY = 0;
	private int centerX = 0;

	private Scroller mScroller;
	private VelocityTracker mVelocityTracker = VelocityTracker.obtain();

	private static float mRadius;

	private List<?> mDataList = new ArrayList<>();
	private Paint mPaintText;
	private Paint mPaintCenterText;

	private String mUnitText = "";

	private int mViewHeight;
	private int mViewWidth;

	private int mTopLine;
	private int mBottomLine;

	private int showCount = 9;

	private float mLastDownY;
	private float mLastScrollY;
	private onSelectListener mSelectListener;

	private boolean isCycle = false;

	private Item mHeaderItem = new Item(0);

	public PickerView(Context context) {
		this(context, null);
	}

	public PickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mDataList = new ArrayList<>();
		mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintText.setStyle(Paint.Style.FILL);
		mPaintText.setColor(0xc0888888);
		mPaintText.setTextSize(TEXT_SIZE);

		mPaintCenterText = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintCenterText.setStyle(Paint.Style.FILL);
		mPaintCenterText.setColor(0xff333333);
		mPaintCenterText.setTextSize(TEXT_SIZE + 4);
		mScroller = new Scroller(getContext());
	}

	public void setDataList(List<?> list) {
		if (list != null)
			mDataList = list;
	}

	public void setTextColor(int color) {
		mPaintText.setColor(color);
	}

	public void setSelectTextColor(int color) {
		mPaintCenterText.setColor(color);
	}

	public void setOnSelectListener(onSelectListener listener) {
		mSelectListener = listener;
	}

	public void setShowCount(int count) {
		showCount = count;
		updateItem();
		postInvalidate();
	}

	public void setCurrentSelect(int position) {
		mHeaderItem.position = getPosition(position);
		updateItem();
		postInvalidate();
	}

	public void setCycle(boolean cycle) {
		isCycle = cycle;
		updateItem();
		postInvalidate();
	}

	private void updateItem() {
		if (!isMeasure)
			return;
		Item item = mHeaderItem;
		int count = 0;
		while (count++ < showCount) {
			if (item.next == null) {
				item.next = new Item(item.radian + STEP_RADIAN);
				item.next.parent = item;
				item = item.next;
			}
		}
		if (mHeaderItem.parent == null) {
			mHeaderItem.parent = item;
			item.next = mHeaderItem;
		}
		mHeaderItem.refresh(0);
		setCache(mHeaderItem.parent, true, (int) Math.ceil(count / 2.0f));
		setCache(mHeaderItem.next, false, (int) Math.floor(count / 2.0f));
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		setMode(MODE_NONE);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		float textMaxWidth = measureTextWidth();
		float unitTextWidth = mPaintCenterText.measureText(mUnitText);

		if (widthMode == MeasureSpec.EXACTLY) {
			mViewWidth = widthSize;
			centerX = mViewWidth / 2;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			mViewWidth = (int) (textMaxWidth + unitTextWidth);
			centerX = (int) (textMaxWidth / 2);
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			mViewHeight = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			mViewHeight = showCount * TEXT_SIZE;
		}

		centerY = mViewHeight / 2;
		if (mViewWidth == textMaxWidth + unitTextWidth) {
			centerX = (int) (textMaxWidth / 2);
		} else {
			centerX = mViewWidth / 2;
		}

		mRadius = centerY;

		STEP_SIZE = (float) (mViewHeight * 1.2f / Math.PI);
		STEP_RADIAN = Math.PI / showCount;
		STEP_HALF_RADIAN = STEP_RADIAN / 2;
		STEP_SIZE_RADIAN = STEP_RADIAN * STEP_SIZE;

		mTopLine = (int) ((Math.sin(-STEP_HALF_RADIAN) + 1) * mRadius);
		mBottomLine = (int) ((Math.sin(STEP_HALF_RADIAN) + 1) * mRadius);

		// 按照View的高度计算字体大小
		Log.d(TAG, "onMeasure h=" + mViewHeight + " w=" + mViewWidth);
		isMeasure = true;
		updateItem();
		setMeasuredDimension(mViewWidth, mViewHeight);
	}

	private float measureTextWidth() {
		float maxWidth = 0;
		for (int i = 0; i < getItemCount(); i++) {
			float width = mPaintCenterText.measureText(getItem(i));
			if (width > maxWidth) {
				maxWidth = width;
			}
		}
		return maxWidth;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawLine(0, mTopLine, mViewWidth, mTopLine, mPaintText);
		canvas.drawLine(0, mBottomLine, mViewWidth, mBottomLine, mPaintText);
		Item item = mHeaderItem;
		for (int i = 0; i <= showCount; i++) {
			if (item.isDraw()) {
				canvas.save();
				canvas.clipRect(0, item.top, mViewWidth, item.bottom);
				if (item.isContain(mTopLine, mBottomLine)) {
					canvas.clipRect(0, mTopLine - 1, mViewWidth, mBottomLine + 1, Region.Op.DIFFERENCE);
				}
				canvas.scale(1.0F, (float) item.scale);
				DrawUtils.drawText(canvas, getItem(item.position), centerX, item.getCenterLine(), DrawUtils.AlignMode.CENTER, mPaintText);
				canvas.restore();
				if (item.isContain(mTopLine, mBottomLine)) {
					canvas.save();
					canvas.clipRect(0, mTopLine - 1, mViewWidth, mBottomLine + 1);
					canvas.clipRect(0, item.top, mViewWidth, item.bottom);
					canvas.scale(1.0F, (float) item.scale);
					DrawUtils.drawText(canvas, getItem(item.position), centerX, item.getCenterLine(), DrawUtils.AlignMode.CENTER, mPaintCenterText);
					canvas.restore();
				}
			}
			item = item.next;
		}
		DrawUtils.drawText(canvas, mUnitText, mViewWidth, centerY, DrawUtils.AlignMode.RIGHT_CENTER, mPaintCenterText);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mVelocityTracker.addMovement(event);
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				setMode(MODE_MOVE);
				mLastScrollY = 0;
				mLastDownY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				computeOffset(event.getY() - mLastDownY);
				mLastDownY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
			default:
				mLastDownY = 0;
				mVelocityTracker.computeCurrentVelocity(500);
				int velocity = (int) mVelocityTracker.getYVelocity();
				if (Math.abs(velocity) > 30) {
					fling(velocity);
				} else {
					reviseOffset();
				}
				break;
		}
		return true;
	}

	@Override
	public void computeScroll() {
		if (!isMove()) {
			if (mScroller.computeScrollOffset()) {
				computeOffset(mScroller.getCurrY() - mLastScrollY);
				mLastScrollY = mScroller.getCurrY();
			} else {
				mLastScrollY = 0;
				Log.d(TAG, "computeScroll end");
				if (isFling()) {
					reviseOffset();
				} else if (isScroll()) {
					performSelect(mHeaderItem, false, true);
					setMode(MODE_NONE);
				}
			}
		}
	}

	/**
	 * 调整位置,使选择项处于中间位置
	 */
	private void reviseOffset() {
		setMode(MODE_SCROLL);
		Item item = mHeaderItem.parent;
		for (int i = 0; i < 3; i++) {
			if (item.isDraw() && item.isContain(centerY, centerY)) {
				int de = (int) (((float) Math.sin(item.radian)) * STEP_SIZE);
				mScroller.startScroll(0, 0, 0, -de);
				break;
			}
			item = item.next;
		}
		invalidate();
	}

	/**
	 * 计算增加每个Item的偏移量
	 *
	 * @param delay 变化量大小
	 */
	private void computeOffset(double delay) {
		// 如果变化量等0,直接返回
		if (delay == 0) {
			postInvalidate();
			return;
		}
		// 如果不循环
		if (!isCycle) {
			double dis;
			//如果已经到最上面一个,且向下滚动
			if (mHeaderItem.position == 0 && delay > 0) {
				dis = Math.sin(STEP_HALF_RADIAN - mHeaderItem.radian) * STEP_SIZE - 1;
				if (dis < delay) {
					delay = dis;
					mScroller.abortAnimation();
				}
			}
			//如果已经到最下面一个,且向上滚动
			if (mHeaderItem.position == getItemCount() - 1 && delay < 0) {
				dis = Math.sin(-STEP_HALF_RADIAN - mHeaderItem.radian) * STEP_SIZE + 1;
				if (delay < dis) {
					delay = dis;
					mScroller.abortAnimation();
				}
			}
		}

		// 防止一次滚动超过两个item;
		if (delay > 0) {
			if (delay > STEP_SIZE_RADIAN) {
				delay = STEP_SIZE_RADIAN - 1;
			}
		} else {
			if (delay < -STEP_SIZE_RADIAN)
				delay = -STEP_SIZE_RADIAN + 1;
		}

		double radian = delay / STEP_SIZE;

		Item item = mHeaderItem;
		for (int i = 0; i <= showCount; i++) {
			item.refresh(item.radian + radian);
			if (item.isSelect(centerY)) {
				mHeaderItem = item;
				performSelect(item, radian > 0, false);
			}
			item = item.next;
		}
		item = mHeaderItem;
		while (true) {
			item = radian > 0 ? item.parent : item.next;
			if (!item.isDraw())
				break;
		}
		setCache(item, radian > 0, 1);

		postInvalidate();
	}

	private void setCache(Item item, boolean down, int time) {
		Item parent = item.parent;
		Item next = item.next;
		if (down) {
			item.position = getPosition(next.position - 1);
			item.refresh(next.radian - STEP_RADIAN);
			item = parent;
		} else {
			item.position = getPosition(parent.position + 1);
			item.refresh(parent.radian + STEP_RADIAN);
			item = next;
		}
		if (--time > 0) {
			setCache(item, down, time);
		}
	}

	private String getItem(int position) {
		String result = "";
		if (position < 0 || position >= getItemCount()) {
			return result;
		}
		if (mDataList.get(position) instanceof IPickerViewData)
			result = ((IPickerViewData) mDataList.get(position)).getPickerViewText();
		else
			result = mDataList.get(position).toString();
		return result;
	}

	private int getPosition(int position) {
		if (!isCycle) {
			if (position < 0 || position >= getItemCount())
				return -2;
		}
		if (getItemCount() == 0)
			return 0;
		return (position + getItemCount()) % getItemCount();
	}

	private int getItemCount() {
		return mDataList.size();
	}

	private void fling(int velocityY) {
		setMode(MODE_FLING);
		mScroller.fling(0, 0, 0, velocityY, 0, 0, -800, 800);
		postInvalidate();
	}

	private boolean isFling() {
		return (mFlag & MODE_FLING) > 0;
	}

	private boolean isMove() {
		return (mFlag & MODE_MOVE) > 0;
	}

	private boolean isScroll() {
		return (mFlag & MODE_SCROLL) > 0;
	}

	private void setMode(int mode) {
		mFlag = mode;
	}

	/**
	 * @param item      变化的Item
	 * @param direction 滚动方向，true时向下
	 * @param isSelect  是否选择
	 */
	private void performSelect(Item item, boolean direction, boolean isSelect) {
		if (mSelectListener != null) {
			if (isSelect) {
				mSelectListener.onSelect(getPosition(item.position));
			} else {
				mSelectListener.onTicker(direction ? getPosition(item.position + 1) : getPosition(item.position - 1), getPosition(item.position));
			}
		}
	}

	public interface IPickerViewData {
		String getPickerViewText();
	}

	public interface onSelectListener {
		void onSelect(int position);

		void onTicker(int oldPosition, int newPosition);
	}

	public static class SimpleOnSelectListener implements onSelectListener {

		@Override
		public void onSelect(int position) {
		}

		@Override
		public void onTicker(int oldPosition, int newPosition) {
		}
	}

	private static class Item {
		private static final double HALF_PI = Math.PI / 2;
		private double radian = Math.PI;//当前的弧度

		int position;
		Item next;//后一个Item
		Item parent;//前一个Item

		private double scale = 0;

		float top = 0;
		float bottom = 0;

		private boolean isTicker = false;


		Item(double r) {
			refresh(r);
		}

		/**
		 * 更新Item所在的弧度值
		 *
		 * @param radian 新的弧度值
		 */
		void refresh(double radian) {
			this.radian = radian;
			scale = Math.cos(radian);
			top = (float) (Math.sin(radian - STEP_HALF_RADIAN) + 1) * mRadius;
			bottom = (float) (Math.sin(radian + STEP_HALF_RADIAN) + 1) * mRadius;
		}

		/**
		 * 两条线的范围与Item是否有交叉
		 *
		 * @param t 上面一条线的值
		 * @param b 下面一条线的值
		 * @return true则有交叉
		 */
		boolean isContain(float t, float b) {
			int bottom = (int) Math.ceil(this.bottom);
			int top = (int) Math.floor(this.top);
			return (t >= top && t <= bottom) || (b >= top && b <= bottom);
		}

		/**
		 * 获取Item的中间位置
		 *
		 * @return 中间位置
		 */
		float getCenterLine() {
			return (float) ((bottom + top) / 2 / scale);
		}

		/**
		 * 是否显示出来
		 */
		boolean isDraw() {
			return Math.abs(radian) < HALF_PI;
		}

		/**
		 * 判断Item是否经过中间线,即弧度值为0时的线
		 *
		 * @return 等于0表示没有本次移动没有经过中间线, 大于0表示向下滑动经过, 小于0表示向上滑动经过
		 */
		boolean isSelect(int centerY) {
			boolean contain = centerY > top && centerY < bottom;
			if (!isTicker) {
				if (contain) {
					isTicker = true;
					return true;
				}
			} else {
				if (!contain) {
					isTicker = false;
				}
			}
			return false;
		}
	}
}
