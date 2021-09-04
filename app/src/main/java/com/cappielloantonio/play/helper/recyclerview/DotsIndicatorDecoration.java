package com.cappielloantonio.play.helper.recyclerview;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class DotsIndicatorDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "DotsIndicatorDecoration";

    private final int indicatorHeight;
    private final int indicatorItemPadding;
    private final int radius;

    private final Paint inactivePaint = new Paint();
    private final Paint activePaint = new Paint();

    public DotsIndicatorDecoration(int radius, int padding, int indicatorHeight, @ColorInt int colorInactive, @ColorInt int colorActive) {
        float strokeWidth = Resources.getSystem().getDisplayMetrics().density * 1;
        this.radius = radius;

        inactivePaint.setStrokeCap(Paint.Cap.ROUND);
        inactivePaint.setStrokeWidth(strokeWidth);
        inactivePaint.setStyle(Paint.Style.STROKE);
        inactivePaint.setAntiAlias(true);
        inactivePaint.setColor(colorInactive);

        activePaint.setStrokeCap(Paint.Cap.ROUND);
        activePaint.setStrokeWidth(strokeWidth);
        activePaint.setStyle(Paint.Style.FILL);
        activePaint.setAntiAlias(true);
        activePaint.setColor(colorActive);

        this.indicatorItemPadding = padding;
        this.indicatorHeight = indicatorHeight;
    }

    @Override
    public void onDrawOver(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if (parent.getAdapter() == null) return;

        int itemCount = (int) Math.ceil((double) parent.getAdapter().getItemCount() / 5);

        if (itemCount <= 1) {
            return;
        }

        // center horizontally, calculate width and subtract half from center
        float totalLength = this.radius * 2 * itemCount;
        float paddingBetweenItems = Math.max(0, itemCount - 1) * indicatorItemPadding;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;
        float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2f;

        // center vertically in the allotted space
        float indicatorPosY = parent.getHeight() - indicatorHeight - (float) indicatorItemPadding / 4;

        drawInactiveDots(c, indicatorStartX, indicatorPosY, itemCount);

        final int activePosition;

        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            activePosition = ((GridLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            activePosition = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        } else {
            // not supported layout manager
            return;
        }

        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        // find offset of active page if the user is scrolling
        final View activeChild = parent.getLayoutManager().findViewByPosition(activePosition);
        if (activeChild == null) {
            return;
        }

        drawActiveDot(c, indicatorStartX, indicatorPosY, activePosition);
    }

    private void drawInactiveDots(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
        // width of item indicator including padding
        final float itemWidth = this.radius * 2 + indicatorItemPadding;

        float start = indicatorStartX + radius;
        for (int i = 0; i < itemCount; i++) {
            c.drawCircle(start, indicatorPosY, radius, inactivePaint);
            start += itemWidth;
        }
    }

    private void drawActiveDot(Canvas c, float indicatorStartX, float indicatorPosY, int highlightPosition) {
        // width of item indicator including padding
        final float itemWidth = this.radius * 2 + indicatorItemPadding;
        float highlightStart = (float) Math.ceil(indicatorStartX + radius + itemWidth * highlightPosition / 5);
        c.drawCircle(highlightStart, indicatorPosY, radius, activePaint);
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = indicatorHeight;
    }
}