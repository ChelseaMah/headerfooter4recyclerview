package com.mcx.footerheaderforrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * Created by machenxi on 2016/11/11.
 */
public class RecyclerViewDelegate {
	@NonNull
	private final RecyclerView mRecyclerView;
	private RecyclerViewFooter.FooterItemDecoration mFooterItemDecoration;
	private RecyclerView.OnScrollListener onScrollListener;
	private RecyclerView.OnChildAttachStateChangeListener onChildAttachListener;
	private RecyclerViewHeader.HeaderItemDecoration mHeaderItemDecoration;

	private RecyclerViewDelegate(final @NonNull RecyclerView recyclerView) {
		this.mRecyclerView = recyclerView;
	}

	@NonNull
	public RecyclerView getRecyclerView() {
		return mRecyclerView;
	}

	public static RecyclerViewDelegate with(@NonNull RecyclerView recyclerView) {
		return new RecyclerViewDelegate(recyclerView);
	}

	public void onHeaderSizeChanged(int height, int width) {
		if (mHeaderItemDecoration != null) {
			mHeaderItemDecoration.setHeight(height);
			mHeaderItemDecoration.setWidth(width);
			mRecyclerView.post(new Runnable() {
				@Override
				public void run() {
					invalidateItemDecorations();
				}
			});
		}
	}

	public void onFooterSizeChanged(int height, int width) {
		if (mFooterItemDecoration != null) {
			mFooterItemDecoration.setHeight(height);
			mFooterItemDecoration.setWidth(width);
			mRecyclerView.post(new Runnable() {
				@Override
				public void run() {
					invalidateItemDecorations();
				}
			});
		}
	}

	public void invalidateItemDecorations() {
		if (!mRecyclerView.isComputingLayout()) {
			mRecyclerView.invalidateItemDecorations();
		}
	}

	public boolean hasItems() {
		return mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter().getItemCount() != 0;
	}

	public void setHeaderDecoration(RecyclerViewHeader.HeaderItemDecoration headerDecoration) {
		clearHeaderDecoration();
		this.mHeaderItemDecoration = headerDecoration;
		mRecyclerView.addItemDecoration(this.mHeaderItemDecoration);
	}

	final void clearHeaderDecoration() {
		if (mHeaderItemDecoration != null) {
			mRecyclerView.removeItemDecoration(mHeaderItemDecoration);
			mHeaderItemDecoration = null;
		}
	}

	public void setFooterDecoration(RecyclerViewFooter.FooterItemDecoration decoration) {
		clearFooterDecoration();
		this.mFooterItemDecoration = decoration;
		mRecyclerView.addItemDecoration(this.mFooterItemDecoration);
	}

	private void clearFooterDecoration() {
		if (mFooterItemDecoration != null) {
			mRecyclerView.removeItemDecoration(mFooterItemDecoration);
			mFooterItemDecoration = null;
		}
	}

	void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
		clearOnScrollListener();
		this.onScrollListener = onScrollListener;
		mRecyclerView.addOnScrollListener(this.onScrollListener);
	}

	private void clearOnScrollListener() {
		if (onScrollListener != null) {
			mRecyclerView.removeOnScrollListener(onScrollListener);
			onScrollListener = null;
		}
	}

	final int getScrollOffset(boolean isVertical) {
		return isVertical ? mRecyclerView.computeVerticalScrollOffset() : mRecyclerView.computeHorizontalScrollOffset();
	}

	final int getTranslationBase(boolean isVertical) {
		return isVertical ? mRecyclerView.computeVerticalScrollRange() - mRecyclerView.getHeight() :
				mRecyclerView.computeHorizontalScrollRange() - mRecyclerView.getWidth();
	}

	public void setOnChildAttachListener(RecyclerView.OnChildAttachStateChangeListener onChildAttachListener) {
		clearOnChildAttachListener();
		this.onChildAttachListener = onChildAttachListener;
		mRecyclerView.addOnChildAttachStateChangeListener(this.onChildAttachListener);
	}

	private void clearOnChildAttachListener() {
		if (onChildAttachListener != null) {
			mRecyclerView.removeOnChildAttachStateChangeListener(onChildAttachListener);
			onChildAttachListener = null;
		}
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mRecyclerView.onInterceptTouchEvent(ev);
	}

	public boolean onTouchEvent(MotionEvent ev) {
		return mRecyclerView.onTouchEvent(ev);
	}

	public void reset() {
		clearHeaderDecoration();
		clearFooterDecoration();
		clearOnScrollListener();
		clearOnChildAttachListener();
	}
}
