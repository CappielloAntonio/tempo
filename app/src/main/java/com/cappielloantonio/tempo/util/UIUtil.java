package com.cappielloantonio.tempo.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;

import androidx.core.os.LocaleListCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.cappielloantonio.tempo.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UIUtil {
    public static int getSpanCount(int itemCount, int maxSpan) {
        int itemSize = itemCount == 0 ? 1 : itemCount;

        if (itemSize / maxSpan > 0) {
            return maxSpan;
        } else {
            return itemSize % maxSpan;
        }
    }

    public static DividerItemDecoration getDividerItemDecoration(Context context) {
        int[] ATTRS = new int[]{android.R.attr.listDivider};

        TypedArray a = context.obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        InsetDrawable insetDivider = new InsetDrawable(divider, 42, 0, 42, 42);
        a.recycle();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(insetDivider);

        return itemDecoration;
    }

    private static LocaleListCompat getLocalesFromResources(Context context) {
        final List<String> tagsList = new ArrayList<>();

        XmlPullParser xpp = context.getResources().getXml(R.xml.locale_config);

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();

                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if ("locale".equals(tagName) && xpp.getAttributeCount() > 0 && xpp.getAttributeName(0).equals("name")) {
                        tagsList.add(xpp.getAttributeValue(0));
                    }
                }

                xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return LocaleListCompat.forLanguageTags(String.join(",", tagsList));
    }

    public static Map<String, String> getLangPreferenceDropdownEntries(Context context) {
        LocaleListCompat localeList = getLocalesFromResources(context);

        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < localeList.size(); i++) {
            Locale locale = localeList.get(i);

            if (locale != null) {
                map.put(Util.toPascalCase(locale.getDisplayName()), locale.toLanguageTag());
            }
        }

        return map;
    }
}
