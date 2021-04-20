package com.cappielloantonio.play.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recent_search")
public class RecentSearch {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "search")
    private String search;

    public RecentSearch(String search) {
        this.search = search;
    }

    @NonNull
    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
