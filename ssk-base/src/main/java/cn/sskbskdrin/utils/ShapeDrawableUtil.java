package cn.sskbskdrin.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import cn.sskbskdrin.R;

/**
 * @author ayke
 */
public class ShapeDrawableUtil {

	public static Drawable makeRectDrawable(Context context, int frameColor, int bgColor, int radius) {
		return makeRectDrawable(context, frameColor, 1, bgColor, radius);
	}

	public static Drawable makeRectDrawable(Context context, int color) {
		return makeRectDrawable(context, color, 1);
	}

	public static Drawable makeRectDrawable(Context context, int frameColor, int frameWidth) {
		return makeRectDrawable(context, frameColor, frameWidth, 0);
	}

	public static Drawable makeRectDrawable(Context context, int frameColor, int frameWidth, int bgColor, int radius) {
		GradientDrawable drawable;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			drawable = (GradientDrawable) context.getDrawable(R.drawable.rect_bg);
		else
			drawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.rect_bg);
		if (drawable == null)
			drawable = new GradientDrawable();
		drawable.setColor(bgColor);
		drawable.setStroke(frameWidth, frameColor);
		drawable.setCornerRadius(radius);
		return drawable;
	}
}
