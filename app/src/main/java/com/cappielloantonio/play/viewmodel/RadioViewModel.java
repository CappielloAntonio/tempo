package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.repository.RadioRepository;
import com.cappielloantonio.play.subsonic.models.InternetRadioStation;

import java.util.List;

public class RadioViewModel extends AndroidViewModel {
    private final RadioRepository radioRepository;

    private final MutableLiveData<List<InternetRadioStation>> internetRadioStations = new MutableLiveData<>(null);

    public RadioViewModel(@NonNull Application application) {
        super(application);

        radioRepository = new RadioRepository();
    }

    public LiveData<List<InternetRadioStation>> getInternetRadioStations(LifecycleOwner owner) {
        radioRepository.getInternetRadioStations().observe(owner, internetRadioStations::postValue);
        return internetRadioStations;
    }

    public void refreshInternetRadioStations(LifecycleOwner owner) {
        radioRepository.getInternetRadioStations().observe(owner, internetRadioStations::postValue);
    }
}
