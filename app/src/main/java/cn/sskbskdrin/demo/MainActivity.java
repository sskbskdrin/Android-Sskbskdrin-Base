package cn.sskbskdrin.demo;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.sskbskdrin.base.BaseFragmentActivity;
import cn.sskbskdrin.picker.PickerView;

public class MainActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme);
		setContentView(R.layout.activity_main);
		PickerView pickerView = (PickerView) findViewById(R.id.main_picker);
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			list.add("" + i);
		}
		pickerView.setDataList(list);
		pickerView.setOnSelectListener(new PickerView.onSelectListener() {
			@Override
			public void onSelect(int position) {
				if (position == 10) {
					startActivity(new Intent(MainActivity.this, AlertActivity.class));
				}
			}

			@Override
			public void onTicker(int oldPosition, int newPosition) {

			}
		});
	}
}
