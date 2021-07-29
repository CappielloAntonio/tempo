package com.cappielloantonio.play.util;

public class UIUtil {
    public static int getSpanCount(int itemCount, int maxSpan) {
        int itemSize = itemCount == 0 ? 1 : itemCount;

        if (itemSize / maxSpan > 0) {
            return maxSpan;
        }
        else {
            return itemSize % maxSpan;
        }
    }
}
