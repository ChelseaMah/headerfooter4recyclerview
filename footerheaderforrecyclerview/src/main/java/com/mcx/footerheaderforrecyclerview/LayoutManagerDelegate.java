package com.mcx.footerheaderforrecyclerview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by machenxi on 2016/11/11.
 */
public class LayoutManagerDelegate {
	@Nullable
	protected final LinearLayoutManager mLayoutManager;

	public LayoutManagerDelegate(@NonNull RecyclerView.LayoutManager manager) {
		if (manager instanceof LinearLayoutManager) {
			mLayoutManager = (LinearLayoutManager) manager;
		} else {
			throw new IllegalArgumentException("Currently RecyclerViewHeader supports only LinearLayoutManager and GridLayoutManager.");
		}
	}

	public static LayoutManagerDelegate with(@NonNull RecyclerView.LayoutManager layoutManager) {
		return new LayoutManagerDelegate(layoutManager);
	}

	public boolean isLastRowVisible() {
		return mLayoutManager != null && mLayoutManager.findLastVisibleItemPosition() == mLayoutManager.getItemCount() - 1;
	}

	public int getRowSpan() {
		if (mLayoutManager != null) {
			if (mLayoutManager instanceof GridLayoutManager) {
				return ((GridLayoutManager) mLayoutManager).getSpanCount();
			} else {
				return 1;
			}
		}
		return 0;
	}



	public boolean isFirstRowVisible() {
		if (mLayoutManager != null) {
			return mLayoutManager.findFirstVisibleItemPosition() == 0;
		}
		return false; //shouldn't get here
	}

	public boolean isReversed() {
		return mLayoutManager != null && mLayoutManager.getReverseLayout();
	}

	public boolean isVertical() {
		return mLayoutManager != null && mLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL;
	}
}
