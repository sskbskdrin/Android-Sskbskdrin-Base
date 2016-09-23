package cn.sskbskdrin.utils;

import android.util.Log;

import cn.sskbskdrin.BuildConfig;

/**
 * 日志的过滤
 *
 * @author sskbskdrin
 *         mLevel = 0 时,日志将不显示在控制台上
 */
public class L {

	private static int mLevel = Log.VERBOSE;
	private static boolean releaseShowDefault = true;

	/**
	 * @param level level的值与android.util.Log中定义的值相同
	 */
	public static void setLevel(int level) {
		mLevel = level;
	}

	public static void setReleaseShow(boolean show) {
		releaseShowDefault = show;
	}

	public static void v(String tag, String msg) {
		v(tag, msg, releaseShowDefault);
	}

	public static void v(String tag, String msg, boolean releaseShow) {
		if (mLevel > Log.VERBOSE) {
			if (releaseShow || BuildConfig.DEBUG) {
				Log.v(tag, msg);
			}
		}
	}

	public static void d(String tag, String msg) {
		d(tag, msg, releaseShowDefault);
	}

	public static void d(String tag, String msg, boolean releaseShow) {
		if (mLevel > Log.DEBUG) {
			if (releaseShow || BuildConfig.DEBUG) {
				Log.d(tag, msg);
			}
		}
	}

	public static void i(String tag, String msg) {
		i(tag, msg, releaseShowDefault);
	}

	public static void i(String tag, String msg, boolean releaseShow) {
		if (mLevel > Log.INFO) {
			if (releaseShow || BuildConfig.DEBUG) {
				Log.i(tag, msg);
			}
		}
	}

	public static void w(String tag, String msg) {
		w(tag, msg, releaseShowDefault);
	}

	public static void w(String tag, String msg, boolean releaseShow) {
		if (mLevel > Log.WARN) {
			if (releaseShow || BuildConfig.DEBUG) {
				Log.w(tag, msg);
			}
		}
	}

	public static void e(String tag, String msg) {
		e(tag, msg, releaseShowDefault);
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (mLevel > Log.ERROR) {
			if (BuildConfig.DEBUG) {
				Log.e(tag, msg, tr);
			}
		}
	}

	public static void e(String tag, String msg, boolean releaseShow) {
		if (mLevel > Log.ERROR) {
			if (releaseShow || BuildConfig.DEBUG) {
				Log.e(tag, msg);
			}
		}
	}
}
