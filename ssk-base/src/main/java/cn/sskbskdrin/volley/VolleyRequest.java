package cn.sskbskdrin.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import cn.sskbskdrin.utils.L;

/**
 * VolleyRequest
 */
public abstract class VolleyRequest<T> extends Request<T> {
	public static final String TAG = "VolleyRequest";

	public static final int DEFAULT_ID = -1;

	private Map<String, String> mHeaderMap;
	private Map<String, String> mParamsMap;
	protected int requestId = DEFAULT_ID;

	public VolleyRequest(String url) {
		this(url, DEFAULT_ID);
	}

	public VolleyRequest(String url, int id) {
		this(url, Method.DEPRECATED_GET_OR_POST, id);
	}

	public VolleyRequest(String url, int method, int id) {
		super(method, url, null);
		requestId = id;
		L.i(TAG, "VolleyRequest url=" + url);
	}

	@Override
	protected void deliverResponse(T response) {
		L.i(TAG, "VolleyRequest result=\n" + response.toString());
		onSuccessResponse(response, requestId);
	}

	protected abstract void onSuccessResponse(T response, int id);

	@Override
	public void deliverError(VolleyError error) {
		onErrorResponse(error, requestId);
	}

	protected abstract void onErrorResponse(VolleyError error, int id);

	public void addHeader(String key, String value) {
		if (mHeaderMap == null)
			mHeaderMap = new HashMap<>();
		mHeaderMap.put(key, value);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaderMap == null ? super.getHeaders() : mHeaderMap;
	}

	public void addParams(String key, String value) {
		if (mParamsMap == null)
			mParamsMap = new HashMap<>();
		mParamsMap.put(key, value);
	}

	public void setParams(HashMap<String, String> map) {
		mParamsMap = map;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		if (mParamsMap != null) {
			StringBuilder sb = new StringBuilder("params -->>");
			for (String key : mParamsMap.keySet()) {
				sb.append("\n");
				sb.append(key);
				sb.append("=");
				sb.append(mParamsMap.get(key));
			}
			L.i(TAG, sb.toString(), false);
		}
		return mParamsMap;
	}

	@Override
	public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
		retryPolicy = new DefaultRetryPolicy(20 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		return super.setRetryPolicy(retryPolicy);
	}

	@Override
	public Request<?> setTag(Object tag) {
		return super.setTag(DefaultRetryPolicy.DEFAULT_MAX_RETRIES);
	}

}
