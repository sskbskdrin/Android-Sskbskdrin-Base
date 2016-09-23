package cn.sskbskdrin.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;

import cn.sskbskdrin.R;

/**
 * @author ayke 带标题的FragmentActivity可以继承它
 */

public class BaseFragmentActivity extends FragmentActivity implements TitleView.TitleOnClickListener {

	protected static String TAG = null;

	protected TitleView mTitleView;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setTheme(R.style.BaseTheme);
		TAG = getClass().getSimpleName();
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		// 隐藏输入法
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		View view = $(R.id.title_layout);
		if (view != null) {
			mTitleView = new TitleView(view);
			mTitleView.setTitleOnClickListener(this);
		}
	}

	public void showTitle(CharSequence title) {
		if (mTitleView != null) {
			mTitleView.setTitle(title);
		}
	}

	public void showTitle(CharSequence title, CharSequence right) {
		if (mTitleView != null) {
			mTitleView.setTitle(title);
			mTitleView.setRightText(right);
		}
	}

	public void showTitle(CharSequence title, int rightImgRes) {
		if (mTitleView != null) {
			mTitleView.setTitle(title);
			mTitleView.setRightImageResource(rightImgRes);
		}
	}

	public void showTitle(CharSequence title, int leftRes, int rightRes) {
		if (mTitleView != null) {
			mTitleView.setTitle(title);
			mTitleView.setLeftImageResource(leftRes);
			mTitleView.setRightImageResource(rightRes);
		}
	}

	public void showTitle(int titleResId) {
		if (mTitleView != null) {
			showTitle(getString(titleResId));
		}
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T $(int id) {
		return (T) findViewById(id);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T $(View parent, int id) {
		return (T) parent.findViewById(id);
	}

	@Override
	public void onClickBack() {
		finish();
	}

	@Override
	public void onClickRightMenu() {
	}

	@Override
	public void onClickRightSecondMenu() {
	}
}
