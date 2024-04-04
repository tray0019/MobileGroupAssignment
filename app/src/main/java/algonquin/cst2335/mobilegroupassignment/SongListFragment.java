package algonquin.cst2335.mobilegroupassignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.application.R;
import com.android.application.databinding.ActivityDeezerRoomBinding;
import com.android.application.databinding.FavouriteSongListBinding;
import com.android.application.databinding.SongDetailsAddBinding;

import java.util.ArrayList;
import java.util.List;

public class SongListFragment extends Fragment {

    private List<DeezerSong> songs;

    // Method to set the list of songs received from MessageDetailsFragment
    public void setSongs(List<DeezerSong> songs) {
        this.songs = songs;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FavouriteSongListBinding binding = FavouriteSongListBinding.inflate(inflater, container, false);

        // Initialize the RecyclerView
        RecyclerView recyclerView = binding.recycleView;

        // Create a LinearLayoutManager and set it to the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        // Create an adapter
        SongAdapter adapter = new SongAdapter(songs);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        // Return the root view of the binding
        return binding.getRoot();
    }
}
