package cn.sskbskdrin.volley;

import com.android.volley.VolleyError;

/**
 * Created by ayke on 2016/8/19 0019.
 */
public class RequestError extends VolleyError {

	private int mCode;
	private String mMessage;

	public RequestError(int code, String msg) {
		mCode = code;
		mMessage = msg;
	}

	public String getTip() {
		return mMessage;
	}
}
