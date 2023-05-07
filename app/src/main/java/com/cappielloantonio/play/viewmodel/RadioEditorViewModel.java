package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.repository.RadioRepository;
import com.cappielloantonio.play.subsonic.models.InternetRadioStation;

public class RadioEditorViewModel extends AndroidViewModel {
    private static final String TAG = "RadioEditorViewModel";

    private final RadioRepository radioRepository;

    private InternetRadioStation toEdit;

    public RadioEditorViewModel(@NonNull Application application) {
        super(application);

        radioRepository = new RadioRepository();
    }

    public InternetRadioStation getRadioToEdit() {
        return toEdit;
    }

    public void setRadioToEdit(InternetRadioStation internetRadioStation) {
        this.toEdit = internetRadioStation;
    }

    public void createRadio(String name, String streamURL, String homepageURL) {
        radioRepository.createInternetRadioStation(name, streamURL, homepageURL);
    }

    public void updateRadio(String name, String streamURL, String homepageURL) {
        if (toEdit != null) radioRepository.updateInternetRadioStation(toEdit.getId(), name, streamURL, homepageURL);
    }

    public void deleteRadio() {
        if (toEdit != null) radioRepository.deleteInternetRadioStation(toEdit.getId());
    }
}
