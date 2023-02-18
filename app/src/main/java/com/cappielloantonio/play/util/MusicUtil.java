package com.cappielloantonio.play.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.text.Html;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MusicUtil {
    private static final String TAG = "MusicUtil";

    public static Uri getStreamUri(Context context, String id) {
        Map<String, String> params = App.getSubsonicClientInstance(App.getInstance(), false).getParams();

        StringBuilder uri = new StringBuilder();

        uri.append(App.getSubsonicClientInstance(App.getInstance(), false).getUrl());
        uri.append("stream");

        if (params.containsKey("u") && params.get("u") != null)
            uri.append("?u=").append(params.get("u"));
        if (params.containsKey("p") && params.get("p") != null)
            uri.append("&p=").append(params.get("p"));
        if (params.containsKey("s") && params.get("s") != null)
            uri.append("&s=").append(params.get("s"));
        if (params.containsKey("t") && params.get("t") != null)
            uri.append("&t=").append(params.get("t"));
        if (params.containsKey("v") && params.get("v") != null)
            uri.append("&v=").append(params.get("v"));
        if (params.containsKey("c") && params.get("c") != null)
            uri.append("&c=").append(params.get("c"));
        uri.append("&id=").append(id);

        if (getConnectivityManager(context).getActiveNetworkInfo() != null) {
            uri.append("&maxBitRate=")
                    .append(getBitratePreference(context))
                    .append("&format=")
                    .append(getTranscodingFormatPreference(context));
        }

        // Log.d(TAG, "getStreamUri(): " + uri);

        return Uri.parse(uri.toString());
    }

    public static Uri getDownloadUri(String id) {
        Map<String, String> params = App.getSubsonicClientInstance(App.getInstance(), false).getParams();

        StringBuilder uri = new StringBuilder();

        uri.append(App.getSubsonicClientInstance(App.getInstance(), false).getUrl());
        uri.append("stream");

        if (params.containsKey("u") && params.get("u") != null)
            uri.append("?u=").append(params.get("u"));
        if (params.containsKey("p") && params.get("p") != null)
            uri.append("&p=").append(params.get("p"));
        if (params.containsKey("s") && params.get("s") != null)
            uri.append("&s=").append(params.get("s"));
        if (params.containsKey("t") && params.get("t") != null)
            uri.append("&t=").append(params.get("t"));
        if (params.containsKey("v") && params.get("v") != null)
            uri.append("&v=").append(params.get("v"));
        if (params.containsKey("c") && params.get("c") != null)
            uri.append("&c=").append(params.get("c"));
        uri.append("&id=").append(id);

        // Log.d(TAG, "getDownloadUri(): " + uri);

        return Uri.parse(uri.toString());
    }

    public static String getReadableDurationString(long duration, boolean millis) {
        long minutes;
        long seconds;

        if (millis) {
            minutes = (duration / 1000) / 60;
            seconds = (duration / 1000) % 60;
        } else {
            minutes = duration / 60;
            seconds = duration % 60;
        }

        if (minutes < 60) {
            return String.format(Locale.getDefault(), "%01d:%02d", minutes, seconds);
        } else {
            long hours = minutes / 60;
            minutes = minutes % 60;
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        }
    }

    public static String getReadablePodcastDurationString(long duration) {
        long minutes = duration / 60;

        if (minutes < 60) {
            return String.format(Locale.getDefault(), "%01d min", minutes);
        } else {
            long hours = minutes / 60;
            minutes = minutes % 60;
            return String.format(Locale.getDefault(), "%d h %02d min", hours, minutes);
        }
    }

    public static String getReadableString(String string) {
        if (string != null) {
            return Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT).toString();
        }

        return "";
    }

    public static String forceReadableString(String string) {
        if (string != null) {
            return getReadableString(string)
                    .replaceAll("&#34;", "\"")
                    .replaceAll("&#39;", "'")
                    .replaceAll("&amp;", "'")
                    .replaceAll("<a[\\s]+([^>]+)>((?:.(?!</a>))*.)</a>", "");
        }

        return "";
    }

    public static String getReadableLyrics(String string) {
        if (string != null) {
            return string
                    .replaceAll("&#34;", "\"")
                    .replaceAll("&#39;", "'")
                    .replaceAll("&amp;", "'")
                    .replaceAll("&#xA;", "\n");
        }

        return "";
    }

    public static String normalizedArtistName(String string) {
        if (string != null) {
            if (string.toLowerCase().contains(" feat."))
                return Pattern.compile(" feat.", Pattern.CASE_INSENSITIVE).split(string)[0].trim();
            else if (string.toLowerCase().contains(" featuring"))
                return Pattern.compile(" featuring", Pattern.CASE_INSENSITIVE).split(string)[0].trim();
            else return string;
        }

        return "";
    }

    public static List<String> getReadableStrings(List<String> strings) {
        List<String> readableStrings = new ArrayList<>();

        if (strings.size() > 0) {
            for (String string : strings) {
                if (string != null) {
                    readableStrings.add(Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT).toString());
                }
            }
        }

        return readableStrings;
    }

    public static String passwordHexEncoding(String plainPassword) {
        return "enc:" + plainPassword.chars().mapToObj(Integer::toHexString).collect(Collectors.joining());
    }

    public static int getDefaultPicPerCategory(String category) {
        switch (category) {
            case CustomGlideRequest.SONG_PIC:
                return R.drawable.default_album_art;
            case CustomGlideRequest.ALBUM_PIC:
                return R.drawable.default_album_art;
            case CustomGlideRequest.ARTIST_PIC:
                return R.drawable.default_album_art;
            case CustomGlideRequest.PLAYLIST_PIC:
                return R.drawable.default_album_art;
            default:
                return R.drawable.default_album_art;
        }
    }

    public static String getBitratePreference(Context context) {
        Network network = getConnectivityManager(context).getActiveNetwork();
        NetworkCapabilities networkCapabilities = getConnectivityManager(context).getNetworkCapabilities(network);
        String audioTranscodeFormat = getTranscodingFormatPreference(context);

        if (audioTranscodeFormat.equals("raw") || network == null || networkCapabilities == null)
            return "0";

        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return PreferenceUtil.getInstance(context).getMaxBitrateWifi();
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return PreferenceUtil.getInstance(context).getMaxBitrateMobile();
        } else {
            return PreferenceUtil.getInstance(context).getMaxBitrateWifi();
        }
    }

    public static String getTranscodingFormatPreference(Context context) {
        Network network = getConnectivityManager(context).getActiveNetwork();
        NetworkCapabilities networkCapabilities = getConnectivityManager(context).getNetworkCapabilities(network);

        if (network == null || networkCapabilities == null) return "raw";

        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return PreferenceUtil.getInstance(context).getAudioTranscodeFormatWifi();
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return PreferenceUtil.getInstance(context).getAudioTranscodeFormatMobile();
        } else {
            return PreferenceUtil.getInstance(context).getAudioTranscodeFormatWifi();
        }
    }

    private static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
