package com.mcx.footerheaderforrecyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 为RecyclerView加Footer, 目前仅支持LinearLayoutManager 和 GridLayoutManager;
 * 使用： RecyclerViewFooter.attachTo(RecyclerView);
 * Created by machenxi on 2016/10/10.
 */
public class RecyclerViewFooter extends LinearLayout {

    private int mIntendedVisibility = VISIBLE;
    private int mDownTranslation;
    private boolean isHidden = false;
    private boolean recyclerWantsTouch;
    private boolean isVertical;
    private boolean isAttachedToRecycler;
    private RecyclerViewDelegate mRecyclerViewDelegate;
    private LayoutManagerDelegate mLayoutManagerDelegate;

    public RecyclerViewFooter(Context context) {
        this(context, null);
    }

    public RecyclerViewFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewFooter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);
    }

	/**
     *  请在调用 RecyclerView.setLayoutManager()后调用此方法
     * @param recycler RecyclerView
     */
    public final void attachTo(@NonNull final RecyclerView recycler) {
        validate(recycler);
        this.mRecyclerViewDelegate = RecyclerViewDelegate.with(recycler);
        this.mLayoutManagerDelegate = LayoutManagerDelegate.with(recycler.getLayoutManager());
        isVertical = mLayoutManagerDelegate.isVertical();
        if (!isVertical) {
            setOrientation(HORIZONTAL);
        }
        isAttachedToRecycler = true;
        mRecyclerViewDelegate.setFooterDecoration(new FooterItemDecoration(mLayoutManagerDelegate.getRowSpan(), isVertical, mLayoutManagerDelegate.isReversed()));
        mRecyclerViewDelegate.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                onScrollChanged();
            }
        });
        mRecyclerViewDelegate.setOnChildAttachListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerViewDelegate.invalidateItemDecorations();
                        onScrollChanged();
                    }
                });
            }
        });
    }


    private void onScrollChanged() {
        isHidden = mRecyclerViewDelegate.hasItems() && !mLayoutManagerDelegate.isLastRowVisible();
        RecyclerViewFooter.super.setVisibility(isHidden ? INVISIBLE : mIntendedVisibility);
        if (!isHidden) {
            final int translation = calculateTranslation();
            if (isVertical) {
                setTranslationY(translation);
            } else {
                setTranslationX(translation);
            }

            Log.v("RecyclerViewFooter", "trans:" + translation);
        }
    }

    private int calculateTranslation() {
        int last = mRecyclerViewDelegate.getRecyclerView().getAdapter().getItemCount() - 1;
        View view = mRecyclerViewDelegate.getRecyclerView().getLayoutManager().findViewByPosition(last);
        if (view != null)
            if (mLayoutManagerDelegate.isReversed()) {
                return isVertical ? view.getTop() - getMeasuredHeight() : view.getLeft() - getMeasuredWidth();
            } else {
                return isVertical ? view.getBottom() : view.getRight();
            }
        return 0;
    }

    @Override
    public final void setVisibility(int visibility) {
        this.mIntendedVisibility = visibility;
        if (!isHidden) {
            super.setVisibility(mIntendedVisibility);
        }
    }

    @Override
    public final int getVisibility() {
        return mIntendedVisibility;
    }

    @Override
    protected final void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && isAttachedToRecycler) {
            int verticalMargins = 0;
            int horizontalMargins = 0;
            if (getLayoutParams() instanceof MarginLayoutParams) {
                final MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
                verticalMargins = layoutParams.topMargin + layoutParams.bottomMargin;
                horizontalMargins = layoutParams.leftMargin + layoutParams.rightMargin;
            }
            mRecyclerViewDelegate.onFooterSizeChanged(getHeight() + verticalMargins, getWidth() + horizontalMargins);
            onScrollChanged();
        }
    }

    @Override
    @CallSuper
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        recyclerWantsTouch = isAttachedToRecycler && mRecyclerViewDelegate.onInterceptTouchEvent(ev);
        if (recyclerWantsTouch && ev.getAction() == MotionEvent.ACTION_MOVE) {
            mDownTranslation = calculateTranslation();
        }
        return recyclerWantsTouch || super.onInterceptTouchEvent(ev);
    }

    @Override
    @CallSuper
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (recyclerWantsTouch) { // this cannot be true if recycler is not attached
            int scrollDiff = mDownTranslation - calculateTranslation();
            int verticalDiff = isVertical ? scrollDiff : 0;
            int horizontalDiff = isVertical ? 0 : scrollDiff;
            MotionEvent recyclerEvent =
                    MotionEvent.obtain(event.getDownTime(),
                            event.getEventTime(),
                            event.getAction(),
                            event.getX() - horizontalDiff,
                            event.getY() - verticalDiff,
                            event.getMetaState());
            mRecyclerViewDelegate.onTouchEvent(recyclerEvent);
            return false;
        }
        return super.onTouchEvent(event);
    }

    private void validate(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() == null) {
            throw new IllegalStateException("Be sure to attach RecyclerViewFooter after setting your RecyclerView's LayoutManager.");
        }
    }

    static class FooterItemDecoration extends RecyclerView.ItemDecoration {
        private final int lastRowSpan;
        private int headerHeight;
        private int headerWidth;
        private boolean isVertical;
        private boolean isReversed;

        public FooterItemDecoration(int lastRowSpan, boolean isVertical, boolean isReversed) {
            this.lastRowSpan = lastRowSpan;
            this.isVertical = isVertical;
            this.isReversed = isReversed;
        }

        public void setWidth(int width) {
            headerWidth = width;
        }

        public void setHeight(int height) {
            headerHeight = height;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int total = parent.getAdapter().getItemCount();
            int currentPos = parent.getChildLayoutPosition(view);
            final boolean isFootPosition = currentPos >= total - lastRowSpan;
            int heightOffset = isFootPosition && isVertical ? headerHeight : 0;
            int widthOffset = isFootPosition && !isVertical ? headerWidth : 0;
            if (isReversed) {
                outRect.top = heightOffset;
                outRect.left = widthOffset;
            } else {
                outRect.bottom = heightOffset;
                outRect.right = widthOffset;
            }
        }
    }
}