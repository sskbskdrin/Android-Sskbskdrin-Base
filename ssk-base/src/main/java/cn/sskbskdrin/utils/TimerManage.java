package cn.sskbskdrin.utils;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TimerManage {
	private static final String TAG = "TimerManage";

	private final List<TimerInfo> tasks = new ArrayList<TimerInfo>(10);
	private Thread mThread;

	private boolean contains(String tag) {
		synchronized (tasks) {
			for (TimerInfo info : tasks) {
				if (info.mTag.equals(tag))
					return true;
			}
			return false;
		}
	}

	private void insert(TimerInfo info) {
		synchronized (tasks) {
			int lo = 0;
			int hi = tasks.size() - 1;

			while (lo <= hi) {
				int mid = (lo + hi) >>> 1;
				TimerInfo midVal = tasks.get(mid);

				if (midVal.startTime < info.startTime) {
					lo = mid + 1;
				} else if (midVal.startTime > info.startTime) {
					hi = mid - 1;
				} else {
					tasks.add(mid, info);
					return;
				}
			}
			tasks.add(lo, info);
		}
	}

	private boolean remove(String tag) {
		synchronized (tasks) {
			for (int i = 0; i < tasks.size(); i++) {
				if (tasks.get(i).mTag.equals(tag)) {
					return tasks.remove(i).isRemove = true;
				}
			}
		}
		return false;
	}

	private void purge() {
		TimerInfo first = null;
		synchronized (tasks) {
			if (tasks.size() > 0) {
				first = tasks.remove(0);
				if (first.isRemove)
					first = null;
				for (int i = 0; i < tasks.size(); ) {
					if (tasks.get(i).isRemove) {
						tasks.remove(i);
					} else {
						i++;
					}
				}
			}
		}
		if (first != null) {
			insert(first);
		}
	}

	private static TimerManage mInstance;

	public static TimerManage getInstance() {
		if (mInstance == null) {
			mInstance = new TimerManage();
		}
		return mInstance;
	}

	private Handler mHandler = null;

	private TimerManage() {
		mHandler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				TimerInfo info = (TimerInfo) msg.obj;
				if (info != null) {
					TimerTaskListener listener = info.getListener();
					if (listener != null) {
						listener.onTimer(info.getTag(), info.getCount());
					}
					if (info.isRemove) {
						L.d(TAG, "Timer task end " + info.mTag);
					}
				}
				return true;
			}
		});
	}

	public void startTimerTask(String tag, long period, TimerTaskListener listener) {
		startTimerTask(tag, period, 1, listener);
	}

	public void startTimerTask(String tag, long period, int count, TimerTaskListener listener) {
		startTimerTask(tag, period, count, 0, listener);
	}


	/**
	 * 添加计时器，时间以10ms为倍数
	 *
	 * @param tag      计时器标识
	 * @param count    计时次数，count != 0,大于0时为计时次数，小于0时，为无限循环
	 * @param delay    延迟时间
	 * @param period   计时周期，period > 10
	 * @param listener 计时回调 listener != null;
	 */
	public void startTimerTask(String tag, long period, int count, long delay,
	                           TimerTaskListener listener) {
		if (TextUtils.isEmpty(tag) || delay < 0 || period < 10 || count == 0 || listener == null) {
			throw new IllegalArgumentException("params is illegal");
		}
		if (!contains(tag)) {
			L.d(TAG, "Timer task add " + tag);
			insert(new TimerInfo(tag, count, delay, period, listener));
		} else {
			Log.e(TAG, "Timer task start " + tag + " no exist");
		}
		startTimerTask();
	}

	/**
	 * 启动计时器
	 */
	private void startTimerTask() {
		if (mThread == null) {
			mThread = new TaskThread();
		}
		if (isRunning) {
			synchronized (mThread) {
				mThread.notify();
			}
		} else {
			mThread.start();
			isRunning = true;
		}
	}

	/**
	 * 停止计时器
	 *
	 * @param tag 计时器标识
	 */
	public void stopTimerTask(String tag) {
		if (TextUtils.isEmpty(tag)) {
			throw new IllegalArgumentException("tag is null");
		}
		if (remove(tag)) {
			L.d(TAG, "Timer task stop " + tag);
		} else {
			L.w(TAG, "Timer task stop " + tag + " no exist");
		}
	}

	/**
	 * 停止所有计时器
	 */
	public void stopTimerAll() {
		L.d(TAG, "Timer task remove all");
		isRunning = false;
		mInstance = null;
		mThread = null;
	}

	public interface TimerTaskListener {
		void onTimer(String tag, int count);
	}

	private boolean isRunning = false;

	private class TaskThread extends Thread {

		TaskThread() {
			super(TAG);
		}

		@Override
		public void run() {
			while (isRunning) {
				TimerInfo info;
				synchronized (this) {
					synchronized (tasks) {
						if (tasks.isEmpty()) {
							isRunning = false;
							continue;
						}
						info = tasks.get(0);
					}
					synchronized (info.lock) {
						if (info.isRemove) {
							tasks.remove(0);
							continue;
						}
					}

					long currentTime = System.currentTimeMillis();
					long timeToSleep = info.startTime - currentTime;
					if (timeToSleep > 0) {
						try {
							wait(timeToSleep);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					synchronized (info.lock) {
						if (info.mCount > 0) {
							if (--info.mCount == 0)
								info.isRemove = true;
						}
						info.startTime += info.mPeriodTime;
					}

					Message msg = Message.obtain();
					msg.what = 0;
					msg.obj = info;
					mHandler.sendMessage(msg);
					purge();
				}
			}
			mThread = null;
		}
	}

	private class TimerInfo {

		private final byte[] lock = new byte[0];
		private boolean isRemove = false;
		private String mTag;
		private long mPeriodTime;
		private long startTime;
		private TimerTaskListener mListener;

		private int mCount;

		TimerInfo(String tag, int count, long delay, long period,
		          TimerTaskListener listener) {
			mTag = tag;
			mCount = count;
			mPeriodTime = period;
			startTime = System.currentTimeMillis() + delay + period;
			mListener = listener;
		}

		String getTag() {
			return mTag;
		}

		int getCount() {
			return mCount;
		}

		TimerTaskListener getListener() {
			return mListener;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null)
				return false;
			if (o instanceof TimerInfo) {
				TimerInfo info = (TimerInfo) o;
				return mTag == null ? info.mTag == null : mTag.equals(info.getTag());
			}
			return false;
		}
	}

}
