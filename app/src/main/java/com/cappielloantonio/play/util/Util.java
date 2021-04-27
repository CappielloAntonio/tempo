package com.cappielloantonio.play.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static String getDate() {
        String pattern = "EEEE, MMMM d";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        return date;
    }
}
