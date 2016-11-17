package com.mcx.footerviewforrecyclerview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcx.footerviewforrecyclerview.R;


public class ColorItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int[] colors;

    public ColorItemsAdapter(Context context, int numberOfItems) {
        colors = new int[numberOfItems];

        int startColor = ContextCompat.getColor(context, R.color.colorAccent);
        int startR = Color.red(startColor);
        int startG = Color.green(startColor);
        int startB = Color.blue(startColor);

        int endColor = ContextCompat.getColor(context, R.color.colorPrimary);
        int endR = Color.red(endColor);
        int endG = Color.green(endColor);
        int endB = Color.blue(endColor);

        ValueInterpolator interpolatorR = new ValueInterpolator(0, numberOfItems - 1, endR, startR);
        ValueInterpolator interpolatorG = new ValueInterpolator(0, numberOfItems - 1, endG, startG);
        ValueInterpolator interpolatorB = new ValueInterpolator(0, numberOfItems - 1, endB, startB);

        for (int i = 0; i < numberOfItems; ++i) {
            colors[i] = Color.argb(255, (int) interpolatorR.map(i), (int) interpolatorG.map(i), (int) interpolatorB.map(i));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SampleViewHolder viewHolder = (SampleViewHolder) holder;
        LayerDrawable bgDrawable = (LayerDrawable) viewHolder.mainLayout.getBackground();
        GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.background_shape);
        shape.setColor(colors[position]);
        viewHolder.itemTxt.setText("Item " + position);
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

    public static class SampleViewHolder extends RecyclerView.ViewHolder {
        public View mainLayout;
        public TextView itemTxt;

        public SampleViewHolder(View view) {
            super(view);
            mainLayout = view.findViewById(R.id.layout);
            itemTxt = (TextView) view.findViewById(R.id.item_txt);
        }
    }

    class ValueInterpolator {
        private float mRangeMapFromMin;
        private float mRangeMapToMin;
        private float mScaleFactor;

        public ValueInterpolator(float rangeMapFromMin, float rangeMapFromMax, float rangeMapToMin, float rangeMapToMax) {
            mRangeMapFromMin = rangeMapFromMin;
            mRangeMapToMin = rangeMapToMin;

            float rangeMapFromSpan = rangeMapFromMax - rangeMapFromMin;
            float rangeMapToSpan = rangeMapToMax - rangeMapToMin;

            mScaleFactor = rangeMapToSpan / rangeMapFromSpan;
        }

        public float map(float value) {
            return mRangeMapToMin + ((value - mRangeMapFromMin) * mScaleFactor);
        }
    }

}