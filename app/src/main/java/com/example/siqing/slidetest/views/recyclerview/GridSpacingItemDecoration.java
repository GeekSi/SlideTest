package com.example.siqing.slidetest.views.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.siqing.slidetest.utils.Utils;

/**
 * Created by Administrator on 2015/12/7.
 * 网上找的，原谅我。不想重复造轮子
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;
    private int firstLineTop;
    private int bottomSpace;

    public GridSpacingItemDecoration(Context context, int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
        this.firstLineTop = Utils.dip2px(context, 60);
        this.bottomSpace = Utils.dip2px(context, 46);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

//            if (position < spanCount) { // top edge
//                outRect.top = firstLineTop;
//            }
            outRect.top = 0;
            outRect.bottom = bottomSpace; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }

    public void setSpace(int spacing){
        this.spacing = spacing;
    }
}