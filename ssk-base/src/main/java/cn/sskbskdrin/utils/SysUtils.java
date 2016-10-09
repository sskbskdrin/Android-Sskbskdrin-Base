package cn.sskbskdrin.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ayke on 2016/8/12 0012.
 * 系统工具类
 */
public class SysUtils {
	private static Context mContext;

	public static void init(Context context) {
		mContext = context;
	}

	public static Context getContext() {
		return mContext;
	}

	public static float dp2px(Context context, float dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return scale * dp + 0.5f;
	}

	// 隐藏软键盘
	public static void hideSoftInput(Context context) {
		InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
		View view = ((Activity) context).getCurrentFocus();
		if (view != null)
			imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void callPhone(Context context, String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		Uri data = Uri.parse("tel:" + phone);
		intent.setData(data);
		context.startActivity(intent);
	}

}
