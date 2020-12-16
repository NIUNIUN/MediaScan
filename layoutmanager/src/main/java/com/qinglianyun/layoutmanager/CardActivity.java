package com.qinglianyun.layoutmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.qinglianyun.layoutmanager.adapter.CardAdapter;
import com.qinglianyun.layoutmanager.bean.CardBean;
import com.qinglianyun.layoutmanager.ui.cardmanager.CardItemHelper;
import com.qinglianyun.layoutmanager.ui.cardmanager.CardLayoutmanager;
import com.qinglianyun.layoutmanager.ui.cardmanager.CardTouchCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CardActivity extends AppCompatActivity {

    private RecyclerView mRv;
    private CardAdapter mAdapter;
    private CopyOnWriteArrayList<CardBean> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        mRv = findViewById(R.id.card_rv_data);

        CardLayoutmanager layoutManager = new CardLayoutmanager();
        mRv.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CardTouchCallback(mCardItemHelper));
        itemTouchHelper.attachToRecyclerView(mRv);

        initData();
    }

    private void initData() {
        mAdapter = new CardAdapter(this, mDataList = obtainDataList());
        mRv.setAdapter(mAdapter);
    }

    private CopyOnWriteArrayList<CardBean> obtainDataList() {
        CopyOnWriteArrayList<CardBean> cardList = new CopyOnWriteArrayList<>();
        cardList.add(new CardBean("高安", R.mipmap.ic_gaoan));
        cardList.add(new CardBean("萍乡", R.mipmap.ic_pingxiang));
        cardList.add(new CardBean("吉安", R.mipmap.ic_jian));
        cardList.add(new CardBean("九江", R.mipmap.ic_jiujiang));
        cardList.add(new CardBean("南昌", R.mipmap.ic_nanc));
        cardList.add(new CardBean("上饶", R.mipmap.ic_shangrao));
        cardList.add(new CardBean("宜春", R.mipmap.ic_yichun));
        cardList.add(new CardBean("鹰潭", R.mipmap.ic_yingtan));
        cardList.add(new CardBean("抚州", R.mipmap.ic_fuzhou));
        return cardList;
    }

    private CardItemHelper mCardItemHelper = new CardItemHelper() {
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//            mDataList.remove(viewHolder.getAdapterPosition());
//            mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            int position = viewHolder.getAdapterPosition();

            List<CardBean> cardBeans1 = new ArrayList<>();
            cardBeans1.addAll(mDataList.subList(position + 1, mDataList.size()));
            cardBeans1.add(mDataList.get(position));

            mDataList.clear();
            mDataList.addAll(cardBeans1);
            mAdapter.notifyDataSetChanged();
        }
    };
}
