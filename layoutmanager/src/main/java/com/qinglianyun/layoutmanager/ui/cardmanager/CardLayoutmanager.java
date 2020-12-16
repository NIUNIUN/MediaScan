package com.qinglianyun.layoutmanager.ui.cardmanager;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by tang_xqing on 2020/12/16.
 */
public class CardLayoutmanager extends RecyclerView.LayoutManager {
    /**
     * 页面上最多显示多少个item
     */
    private int showViewCount = 4;

    private float mSectionScale = 0.075f;

    private float mSectionTranslation = 10f;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        /**
         * isPreLayout() ：是否为预布局阶段，指布局的逻辑设置动画的初始状态。
         */
        if (0 == getItemCount() || state.isPreLayout()) {
            return;
        }

        // 先移除RecyclerView所有的子view。 为什么使用detachAndScrapAttachedViews(),而不是removeAndRecycleAllViews()
        detachAndScrapAttachedViews(recycler);

        int count = getItemCount() > showViewCount ? showViewCount : getItemCount();
        for (int i = count - 1; i >= 0; i--) {
            final View view = recycler.getViewForPosition(i);
            addView(view);

            // 测量view
            measureChildWithMargins(view, 0, 0);

            //  view在RecyclerView里面的剩余宽度
            int measuredWidth = getDecoratedMeasuredWidth(view);
            int widthSpec = getWidth() - measuredWidth;

            // 设置view的位置
            layoutDecoratedWithMargins(view, widthSpec / 2, 0, widthSpec / 2 + measuredWidth, getDecoratedMeasuredHeight(view));

            // 重叠效果
            view.setScaleX(getScaleX(i));
            view.setScaleY(getScaleY(i));
            view.setTranslationX(getTranslationX(i));
            view.setTranslationY(getTranslationY(i));
        }
        // onScrollStateChanged
    }

    /**
     * position越大，缩放比例越大
     *
     * @param position
     * @return
     */
    private float getScaleX(int position) {
        return 1f - position * mSectionScale;
    }

    private float getScaleY(int position) {
        return 1f;
    }

    private float getTranslationX(int position) {
        return 0f;
    }

    /**
     * position 越大，平移越大
     *
     * @param position
     * @return
     */
    private float getTranslationY(int position) {
        return position * mSectionTranslation;
    }
}
