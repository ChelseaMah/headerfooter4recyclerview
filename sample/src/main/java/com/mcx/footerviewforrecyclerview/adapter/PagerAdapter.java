package com.mcx.footerviewforrecyclerview.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mcx.footerviewforrecyclerview.fragment.GridRecyclerFragment;
import com.mcx.footerviewforrecyclerview.fragment.ListRecyclerViewFragment;
import com.mcx.footerviewforrecyclerview.fragment.ListReversedFragment;

/**
 * MainActivity Viewpager Adapter
 * Created by machenxi on 2016/11/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {

	public PagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			return ListRecyclerViewFragment.newInstance();
		} else if (position == 1){
			return GridRecyclerFragment.newInstance();
		} else {
			return ListReversedFragment.newInstance();
		}
	}

	@Override
	public int getCount() {
		return 3;
	}
}
