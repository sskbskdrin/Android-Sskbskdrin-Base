package cn.sskbskdrin.volley;

import com.android.volley.VolleyError;

/**
 * Created by ayke on 2016/8/19 0019.
 * 网络请求回调
 */
public interface IVolleyRequestListener<T> {
	void onVolleySuccess(T result, boolean isSuccess, int id);

	void onVolleyError(VolleyError error, int id);
}
