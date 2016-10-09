package cn.sskbskdrin.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 项目名称 ： 营销监测 类名称 ： VolleyEncapsulation 类描述 ： 封装Volley框架向上的接口，使得更加易用
 * 创建时间：2014-09-10
 */
public class VolleyEncapsulation {

	private static final String TAG = VolleyEncapsulation.class.getSimpleName();

	private static RequestQueue mVolleyRequestQueue = null;

	private VolleyEncapsulation(Context context) {
		if (mVolleyRequestQueue == null)
			mVolleyRequestQueue = Volley.newRequestQueue(context);
	}

	private static VolleyEncapsulation mInstance;

	public static void initVolley(Context context) {
		mInstance = new VolleyEncapsulation(context);
	}

	public static void startRequest(VolleyRequest request) {
		if (mVolleyRequestQueue != null) {
			mVolleyRequestQueue.add(request);
		} else {
			Log.e(TAG, "VolleyEncapsulation uninitialized");
		}
	}
}
