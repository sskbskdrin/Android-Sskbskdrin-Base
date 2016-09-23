package cn.sskbskdrin.volley;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ayke on 2016/8/29 0029.
 */
public class Result<E> {
	@SerializedName("msg")
	public String message;
	@SerializedName("code")
	public int code;
	@SerializedName("data")
	public E entity;
}
