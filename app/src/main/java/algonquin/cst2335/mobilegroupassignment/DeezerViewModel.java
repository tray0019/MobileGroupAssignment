package algonquin.cst2335.mobilegroupassignment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
/**
 * ViewModel class for managing Deezer songs and their details.
 * This class holds LiveData objects for storing lists of DeezerSong objects and selected song details.
 */
public class DeezerViewModel extends ViewModel {
    /**
     * LiveData object for storing a list of DeezerSong objects.
     */
    final MutableLiveData<ArrayList<DeezerSong>> songs = new MutableLiveData<>(new ArrayList<>());
    /**
     * LiveData object for storing a list of DeezerSong objects' details.
     */
    final MutableLiveData<ArrayList<DeezerSong>> songsDetails = new MutableLiveData<>(new ArrayList<>());
    /**
     * LiveData object for storing details of the selected DeezerSong object.
     */
    public MutableLiveData<DeezerSong> selectedSong = new MutableLiveData< >();

}
