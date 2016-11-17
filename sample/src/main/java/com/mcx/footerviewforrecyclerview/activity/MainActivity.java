package com.mcx.footerviewforrecyclerview.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mcx.footerviewforrecyclerview.R;
import com.mcx.footerviewforrecyclerview.adapter.PagerAdapter;

public class MainActivity extends AppCompatActivity {

	private TextView mTitle;
	private TextView mIndicator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewPager viewPager = (ViewPager) findViewById(R.id.listViewPager);
		viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
		mTitle = (TextView) findViewById(R.id.title);
		mIndicator = (TextView) findViewById(R.id.indicator);
		mTitle.setText(R.string.list);
		mIndicator.setText(getString(R.string.indicator, 1));

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					mTitle.setText(R.string.list);
				} else if (position == 1) {
					mTitle.setText(R.string.grid);
				} else {
					mTitle.setText(R.string.reversed_list);
				}
				mIndicator.setText(getString(R.string.indicator, position + 1));
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}
}
