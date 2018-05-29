package com.treasure.lovetravel.helper;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by treasure on 2017/11/11.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = SpaceItemDecoration.class.getName();
    private int space;
    private int columnCount;

    public SpaceItemDecoration(int space, int columnCount) {
        this.space = space;
        this.columnCount = columnCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view); // item position
        switch (columnCount) {
            case 1:
                outRect.top = space;
                break;
            case 3:
                if (position == 0 || position == 1 || position == 2) {
                    outRect.top = space / 3;
                } else {
                    outRect.top = space / 2;
                }
                if (position % 3 == 0) {
                    outRect.left = 0;
                    outRect.right = space / 2;
                } else if (position % 3 == 1) {
                    outRect.left = space / 2;
                    outRect.right = space / 2;
                } else if (position % 3 == 2) {
                    outRect.left = space / 2;
                    outRect.right = 0;
                }
                break;
            case 200://九宫格式
                outRect.left = space;
                outRect.right = space;
                outRect.top = space;
                outRect.bottom = space;
                break;
            default:
                outRect.left = 0;
                outRect.right = space;
                break;
        }
    }
}
