package cn.sskbskdrin.dialog;

import android.app.Dialog;
import android.content.Context;

import cn.sskbskdrin.R;

/**
 * Created by ayke on 2016/10/13 0013.
 */
public class AlertDialog extends Dialog {

	public enum Style {
		ActionSheet,
		Alert
	}

	AlertDialog(Builder builder) {
		super(builder.context, R.style.BaseDialogStyle);
	}

	/**
	 * Builder for arguments
	 */
	public static class Builder {
		private Context context;
		private Style style;
		private String title;
		private String content;
		private String cancel;
		private String[] destructive;
		private String[] others;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setContext(Context context) {
			this.context = context;
			return this;
		}

		public Builder setStyle(Style style) {
			if (style != null) {
				this.style = style;
			}
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setMessage(String msg) {
			this.content = msg;
			return this;
		}

		public Builder setCancelText(String cancel) {
			this.cancel = cancel;
			return this;
		}

		public Builder setDestructive(String... destructive) {
			this.destructive = destructive;
			return this;
		}

		public Builder setOthers(String[] others) {
			this.others = others;
			return this;
		}

		public AlertDialog build() {
			return new AlertDialog(this);
		}
	}
}
