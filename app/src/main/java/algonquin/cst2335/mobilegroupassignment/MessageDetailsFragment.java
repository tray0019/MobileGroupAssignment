package algonquin.cst2335.mobilegroupassignment;

/**
 * This class represents the DeezerRoom activity, which allows users to search for songs from Deezer API.
 * Author: Yandom Youmbi Farock Natanael
 * Date : 04/04/2024
 * Version: 01
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.application.R;
import com.android.application.databinding.SongDetailsAddBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Fragment for displaying details of a selected Deezer song.
 * This fragment allows users to view details of a selected Deezer song, including title, duration, album name, and cover image.
 * Users can also mark a song as a favorite and view a list of favorite songs.
 */
public class MessageDetailsFragment extends Fragment {
    private RecyclerView recyclerView;

    private DeezerSong selected;

    private byte[] imageByteArray; // Declare imageByteArray as a member variable
    private DeezerViewModel deezerViewModel;
    private DeezerSongDAO dDAO;
    private SongAdapter songAdapter;
    DeezerRoom droom = new DeezerRoom();
    /**
     * Default constructor for the MessageDetailsFragment.
     */
    public MessageDetailsFragment() {
    }
    /**
     * Constructor for the MessageDetailsFragment with a selected Deezer song.
     * @param selected The selected Deezer song to display details for.
     */
    public MessageDetailsFragment(DeezerSong selected) {
        this.selected = selected;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ViewModel
        deezerViewModel = new ViewModelProvider(requireActivity()).get(DeezerViewModel.class);
        // Initialize the DAO
        dDAO = Room.databaseBuilder(requireContext(), SongDatabase.class, "favourite_songs2").build().dsDAO();

        SongListFragment songListFragment = new SongListFragment();
        // Initialize your SongAdapter with an empty list initially
        songAdapter = new SongAdapter(new ArrayList<>(),getChildFragmentManager(),songListFragment);

    }


    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SongDetailsAddBinding binding = SongDetailsAddBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Hide the ImageView
        //binding.imageView.setVisibility(View.GONE);


        binding.titleView.setText(selected.getSong());

        // Get the formatted time obtained from the setter method in the DeezerSong class
        binding.timeView.setText(selected.getFormattedTime());

        // Get the albumName obtained from the setter method in the DeezerSong class
        binding.albumView.setText(selected.getAlbumName());

        Glide.with(requireContext())
                .asBitmap()


                .load(selected.getAlbumCoverUrl())
                .placeholder(R.drawable.absent_cover)
                .error(R.drawable.app_icon)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        imageByteArray = outputStream.toByteArray(); // Assign imageByteArray here

                        binding.imageView.setImageBitmap(resource);
                    }
                });



        // Get reference to the favouriteButton
        ImageButton favouriteButton = rootView.findViewById(R.id.favouriteButton);
        favouriteButton.setOnClickListener(v -> {
            DeezerSong songDetails = getSongDetails();

            // Insert the new DeezerSong into the database
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                dDAO.insertMessage(songDetails);
            });
        });


        // Find the favorite button by its ID
        Button favouriteView = rootView.findViewById(R.id.favouriteView);

        // Set click listener for the favorite button
        favouriteView.setOnClickListener(v -> {
            // Call the updateAdapter method to fetch songs from the database and update the adapter
            updateAdapter();

        });
        return rootView;
    }


    // Method to get all song details and cover image as one object
    public DeezerSong getSongDetails() {

        // Retrieve song details
        String title = selected.getSong();
        String time = selected.getFormattedTime();
        String album = selected.getAlbumName();

        // Return all song details and cover image as one object
        return new DeezerSong(title,time,album,imageByteArray);
    }

    // Update the adapter with songs from the database
    private void updateAdapter() {
        // Fetch songs from the database
        Executors.newSingleThreadExecutor().execute(() -> {
            List<DeezerSong> songs = dDAO.getAllSongs();
            requireActivity().runOnUiThread(() -> {
                // Pass the list of songs to the SongListFragment and replace the current fragment
                SongListFragment listFragment = new SongListFragment();
                listFragment.setSongs(songs); // Pass the list of songs to the fragment
                // Replace the current fragment with SongListFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentLocation, listFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            });
        });
    }

}
