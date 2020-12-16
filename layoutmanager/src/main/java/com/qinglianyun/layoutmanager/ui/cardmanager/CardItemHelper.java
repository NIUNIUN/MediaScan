package com.qinglianyun.layoutmanager.ui.cardmanager;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by tang_xqing on 2020/12/16.
 */
public interface CardItemHelper {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);
}
