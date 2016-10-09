package cn.sskbskdrin.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.sskbskdrin.R;
import cn.sskbskdrin.utils.SysUtils;

/**
 * Created by ayke on 2016/8/22 0022.
 * 标题栏
 */
public class TitleView {

	private RelativeLayout mRootView;

	protected ImageView mLeftImage;
	protected ImageView mRightImage1;
	protected ImageView mRightImage2;
	protected TextView mRightText;    // 标题右文字
	protected TextView mTitleText;

	private TitleOnClickListener mTitleOnClickListener;

	public interface TitleOnClickListener {
		/**
		 * /**
		 * 处理左边的菜单的点击的事件
		 */
		void onClickBack();

		/**
		 * 处理右边的菜单的点击的事件
		 */
		void onClickRightMenu();

		/**
		 * 处理右边第二个的菜单的点击的事件
		 */
		void onClickRightSecondMenu();
	}

	public TitleView(View view) {
		mRootView = (RelativeLayout) view;
		mLeftImage = (ImageView) mRootView.findViewById(R.id.title_left_img);
		mTitleText = (TextView) mRootView.findViewById(R.id.title_text);
		mRightText = (TextView) mRootView.findViewById(R.id.title_right_text);
		mRightImage1 = (ImageView) mRootView.findViewById(R.id.title_right_img1);
		mRightImage2 = (ImageView) mRootView.findViewById(R.id.title_right_img2);

		View.OnClickListener mOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				if (id == R.id.title_left_img) {
					SysUtils.hideSoftInput(v.getContext());
					mTitleOnClickListener.onClickBack();
				} else if (id == R.id.title_right_text) {
					mTitleOnClickListener.onClickRightMenu();
				} else if (id == R.id.title_right_img1) {
					mTitleOnClickListener.onClickRightMenu();
				} else if (id == R.id.title_right_img2) {
					mTitleOnClickListener.onClickRightSecondMenu();
				}
			}
		};
		mLeftImage.setOnClickListener(mOnClickListener);
		mRightText.setOnClickListener(mOnClickListener);
		mRightImage1.setOnClickListener(mOnClickListener);
		mRightImage2.setOnClickListener(mOnClickListener);
	}

	public void setTitleOnClickListener(TitleOnClickListener listener) {
		mTitleOnClickListener = listener;
	}

	public void setBackgroundColor(int color) {
		mRootView.setBackgroundColor(color);
	}

	/**
	 * 设置标题左图标
	 *
	 * @param resId
	 */
	public void setLeftImageResource(int resId) {
		if (mLeftImage != null)
			mLeftImage.setImageResource(resId);
		setLeftImageVisible(View.VISIBLE);
	}

	/**
	 * 设置标题右图标
	 *
	 * @param resId
	 */
	public void setRightImageResource(int resId) {
		if (mRightImage1 != null)
			mRightImage1.setImageResource(resId);
		setRightImageVisible(View.VISIBLE);
	}

	/**
	 * 设置标题右文字
	 *
	 * @param text
	 */
	public void setRightText(CharSequence text) {
		if (mRightText != null) {
			mRightText.setText(text);
			setRightTextVisible(View.VISIBLE);
		}
	}

	/**
	 * 设置标题右文字是否可见
	 *
	 * @param visibility
	 */
	public void setRightTextVisible(int visibility) {
		if (mRightText != null)
			mRightText.setVisibility(visibility);
	}

	/**
	 * 标题左图标可见性
	 *
	 * @param visibility
	 */
	public void setLeftImageVisible(int visibility) {
		if (mLeftImage != null)
			mLeftImage.setVisibility(visibility);
	}

	/**
	 * 标题右图标可见性
	 *
	 * @param visibility
	 */
	public void setRightImageVisible(int visibility) {
		if (mRightImage1 != null)
			mRightImage1.setVisibility(visibility);
	}

	public void setTitle(CharSequence title) {
		if (mTitleText != null) {
			mTitleText.setText(title);
		}
	}

	public String getHeadTitle() {
		if (mTitleText != null) {
			return mTitleText.getText().toString();
		}
		return "";
	}
}
