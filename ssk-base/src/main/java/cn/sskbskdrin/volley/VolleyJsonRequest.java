package cn.sskbskdrin.volley;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VolleyJsonRequest<T> extends VolleyRequest<String> {
	private static final String TAG = VolleyJsonRequest.class.getSimpleName();

	private static final Set<String> REQUEST_LIST = new HashSet<>();

	private Context mContext;
	private IVolleyRequestListener<T> mVolleyRequestListener;

	private Type mType;

	public VolleyJsonRequest(Context context, String url, IVolleyRequestListener<T> listener) {
		this(context, url, new TypeToken<Result<String>>() {
		}.getType(), listener);
	}

	/**
	 * @param url      请求链接
	 * @param type     返回结果的Type类型
	 * @param listener 回调监听
	 */
	public VolleyJsonRequest(Context context, String url, @NonNull Type type, IVolleyRequestListener<T> listener) {
		this(context, url, type, listener, DEFAULT_ID);
	}

	/**
	 * @param url      请求链接
	 * @param type     返回结果的Type类型
	 * @param listener 回调监听
	 */
	public VolleyJsonRequest(Context context, String url, @NonNull Type type, IVolleyRequestListener<T> listener, int id) {
		super(url, Method.POST, id);
		mContext = context;
		mType = type;
		mVolleyRequestListener = listener;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String data = new String(response.data);
			return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

	/**
	 * 针对Post请求 统一处理接口的调用
	 */
	public void request() {
		if (REQUEST_LIST.contains(getUrl())) {
			Log.w(TAG, "request already exist -->" + getUrl());
		} else {
			REQUEST_LIST.add(getUrl());
			VolleyEncapsulation.startRequest(this);
		}
	}

	public void request(boolean showLoading) {
		if (showLoading) {
//			LoadingDialog.showLoad(mContext, "正在请求…", null);
		}
		request();
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		return super.getHeaders();
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return super.getParams();
	}

	@Override
	protected void onSuccessResponse(String response, int id) {
		REQUEST_LIST.remove(getUrl());
		try {
			Gson gson = new Gson();
			Result<T> result = gson.fromJson(response, mType);
			if (mVolleyRequestListener != null) {
				if (result.code == 10001) {
					mVolleyRequestListener.onVolleySuccess(result.entity, true, id);
				} else {
					mVolleyRequestListener.onVolleyError(new RequestError(result.code, result.message), id);
				}
			}
		} catch (Exception e) {
			onErrorResponse(new ParseError(e), id);
		}
	}

	@Override
	protected void onErrorResponse(VolleyError error, int id) {
		REQUEST_LIST.remove(getUrl());
	}
}
