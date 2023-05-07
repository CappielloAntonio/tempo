package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.repository.RadioRepository;
import com.cappielloantonio.play.subsonic.models.InternetRadioStation;

import java.util.List;

public class RadioViewModel extends AndroidViewModel {
    private final RadioRepository radioRepository;

    public RadioViewModel(@NonNull Application application) {
        super(application);

        radioRepository = new RadioRepository();
    }

    public LiveData<List<InternetRadioStation>> getInternetRadioStations() {
        return radioRepository.getInternetRadioStations();
    }
}
