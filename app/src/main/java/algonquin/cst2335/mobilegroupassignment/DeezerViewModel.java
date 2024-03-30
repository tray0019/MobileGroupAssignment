package algonquin.cst2335.mobilegroupassignment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DeezerViewModel extends ViewModel {
    final MutableLiveData<ArrayList<DeezerSong>> songs = new MutableLiveData<>(new ArrayList<>());
    final MutableLiveData<ArrayList<DeezerSong>> songsDetails = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<DeezerSong> selectedSong = new MutableLiveData< >();

}
