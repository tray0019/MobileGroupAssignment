package algonquin.cst2335.mobilegroupassignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.application.R;
import com.android.application.databinding.ActivityDeezerRoomBinding;
import com.android.application.databinding.FavouriteSongListBinding;
import com.android.application.databinding.SongDetailsAddBinding;

import java.util.ArrayList;
import java.util.List;

public class SongListFragment extends Fragment {
    private static FragmentManager fragmentManager;

    private List<DeezerSong> songs;

    public SongListFragment() {

    }

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
        SongListFragment songListFragment = new SongListFragment();
        SongAdapter adapter = new SongAdapter(songs, getChildFragmentManager(), songListFragment);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        // Return the root view of the binding
        return binding.getRoot();
    }

}
