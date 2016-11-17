/*
 * Copyright 2015 Bartosz Lipinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcx.footerheaderforrecyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 为RecyclerView加Header, 目前仅支持LinearLayoutManager 和 GridLayoutManager;
 * 使用： RecyclerViewHeader.attachTo(RecyclerView);
 */
public class RecyclerViewHeader extends LinearLayout {

    private int intendedVisibility = VISIBLE;
    private int downTranslation;
    private boolean hidden = false;
    private boolean recyclerWantsTouch;
    private boolean isVertical;
    private boolean isAttachedToRecycler;
    private RecyclerViewDelegate mRecyclerViewDelegate;
    private LayoutManagerDelegate layoutManager;


    public RecyclerViewHeader(Context context) {
        this(context, null);
    }

    public RecyclerViewHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);
    }

    /**
     * Attaches <code>RecyclerViewHeader</code> to <code>RecyclerView</code>.
     * Be sure that <code>setLayoutManager(...)</code> has been called for <code>RecyclerView</code> before calling this method.
     *
     * @param recycler <code>RecyclerView</code> to attach <code>RecyclerViewHeader</code> to.
     */

    public final void attachTo(@NonNull final RecyclerView recycler) {
        validate(recycler);
        this.mRecyclerViewDelegate = RecyclerViewDelegate.with(recycler);
        this.layoutManager = LayoutManagerDelegate.with(recycler.getLayoutManager());
        isVertical = layoutManager.isVertical();
        isAttachedToRecycler = true;
        mRecyclerViewDelegate.setHeaderDecoration(new HeaderItemDecoration(layoutManager.getRowSpan(), layoutManager.isVertical(), layoutManager.isReversed()));
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

    /**
     * Detaches <code>RecyclerViewHeader</code> from <code>RecyclerView</code>.
     */
    public final void detach() {
        if (isAttachedToRecycler) {
            isAttachedToRecycler = false;
            recyclerWantsTouch = false;
            mRecyclerViewDelegate.reset();
            mRecyclerViewDelegate = null;
            layoutManager = null;
        }
    }

    private void onScrollChanged() {
        hidden = mRecyclerViewDelegate.hasItems() && !layoutManager.isFirstRowVisible();
        RecyclerViewHeader.super.setVisibility(hidden ? INVISIBLE : intendedVisibility);
        if (!hidden) {
            final int translation = calculateTranslation();
            if (isVertical) {
                setTranslationY(translation);
            } else {
                setTranslationX(translation);
            }
        }
    }

    private int calculateTranslation() {
        int offset = mRecyclerViewDelegate.getScrollOffset(isVertical);
        int base = layoutManager.isReversed() ? mRecyclerViewDelegate.getTranslationBase(isVertical) : 0;
        return base - offset;
    }

    @Override
    public final void setVisibility(int visibility) {
        this.intendedVisibility = visibility;
        if (!hidden) {
            super.setVisibility(intendedVisibility);
        }
    }

    @Override
    public final int getVisibility() {
        return intendedVisibility;
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
            mRecyclerViewDelegate.onHeaderSizeChanged(getHeight() + verticalMargins, getWidth() + horizontalMargins);
            onScrollChanged();
        }
    }

    @Override
    @CallSuper
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        recyclerWantsTouch = isAttachedToRecycler && mRecyclerViewDelegate.onInterceptTouchEvent(ev);
        if (recyclerWantsTouch && ev.getAction() == MotionEvent.ACTION_MOVE) {
            downTranslation = calculateTranslation();
        }
        return recyclerWantsTouch || super.onInterceptTouchEvent(ev);
    }

    @Override
    @CallSuper
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (recyclerWantsTouch) { // this cannot be true if recycler is not attached
            int scrollDiff = downTranslation - calculateTranslation();
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
            throw new IllegalStateException("Be sure to attach RecyclerViewHeader after setting your RecyclerView's LayoutManager.");
        }
    }

    static class HeaderItemDecoration extends RecyclerView.ItemDecoration {
        private int headerHeight;
        private int headerWidth;
        private int firstRowSpan;
        private boolean isVertical;
        private boolean isReversed;

        HeaderItemDecoration(int firstRowSpan, boolean isVertical, boolean isReVersed) {
            this.firstRowSpan = firstRowSpan;
            this.isVertical = isVertical;
            this.isReversed = isReVersed;
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
            final boolean headerRelatedPosition = parent.getChildLayoutPosition(view) < firstRowSpan;
            int heightOffset = headerRelatedPosition && isVertical ? headerHeight : 0;
            int widthOffset = headerRelatedPosition && !isVertical ? headerWidth : 0;
            if (isReversed) {
                outRect.bottom = heightOffset;
                outRect.right = widthOffset;
            } else {
                outRect.top = heightOffset;
                outRect.left = widthOffset;
            }
        }
    }
}