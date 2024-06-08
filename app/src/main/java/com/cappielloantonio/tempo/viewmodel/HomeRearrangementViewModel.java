package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.model.HomeSector;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.Preferences;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeRearrangementViewModel extends AndroidViewModel {
    private List<HomeSector> sectors = new ArrayList<>();

    public HomeRearrangementViewModel(@NonNull Application application) {
        super(application);
    }

    public List<HomeSector> getHomeSectorList() {
        if (sectors != null && !sectors.isEmpty()) return sectors;

        if (Preferences.getHomeSectorList() != null && !Preferences.getHomeSectorList().equals("null")) {
            sectors = new Gson().fromJson(
                    Preferences.getHomeSectorList(),
                    new TypeToken<List<HomeSector>>() {
                    }.getType()
            );
        } else {
            sectors = fillStandardHomeSectorList();
        }

        return sectors;
    }

    public void orderSectorLiveListAfterSwap(List<HomeSector> sectors) {
        this.sectors = sectors;
    }

    public void saveHomeSectorList(List<HomeSector> sectors) {
        Preferences.setHomeSectorList(sectors);
    }

    public void resetHomeSectorList() {
        Preferences.setHomeSectorList(null);
    }

    public void closeDialog() {
        sectors = null;
    }

    private List<HomeSector> fillStandardHomeSectorList() {
        List<HomeSector> sectors = new ArrayList<>();

        sectors.add(new HomeSector(Constants.HOME_SECTOR_DISCOVERY, getApplication().getString(R.string.home_title_discovery), true, 1));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_MADE_FOR_YOU, getApplication().getString(R.string.home_title_made_for_you), true, 2));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_BEST_OF, getApplication().getString(R.string.home_title_best_of), true, 3));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_RADIO_STATION, getApplication().getString(R.string.home_title_radio_station), true, 4));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_TOP_SONGS, getApplication().getString(R.string.home_title_top_songs), true, 5));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_STARRED_TRACKS, getApplication().getString(R.string.home_title_starred_tracks), true, 6));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_STARRED_ALBUMS, getApplication().getString(R.string.home_title_starred_albums), true, 7));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_STARRED_ARTISTS, getApplication().getString(R.string.home_title_starred_artists), true, 8));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_NEW_RELEASES, getApplication().getString(R.string.home_title_new_releases), true, 9));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_FLASHBACK, getApplication().getString(R.string.home_title_flashback), true, 10));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_MOST_PLAYED, getApplication().getString(R.string.home_title_most_played), true, 11));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_LAST_PLAYED, getApplication().getString(R.string.home_title_last_played), true, 12));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_RECENTLY_ADDED, getApplication().getString(R.string.home_title_recently_added), true, 13));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_PINNED_PLAYLISTS, getApplication().getString(R.string.home_title_pinned_playlists), true, 14));
        sectors.add(new HomeSector(Constants.HOME_SECTOR_SHARED, getApplication().getString(R.string.home_title_shares), true, 15));

        return sectors;
    }
}
