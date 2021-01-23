package com.example.schoolhub.ui.liveStream;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LiveStreamViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public LiveStreamViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Live Stream fragment!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
