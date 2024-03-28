package algonquin.cst2335.mobilegroupassignment;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class DeezerViewModel {
    final MutableLiveData<ArrayList<DeezerSong>> song = new MutableLiveData<>(new ArrayList<>());

}
