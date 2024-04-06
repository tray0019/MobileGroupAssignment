package algonquin.cst2335.mobilegroupassignment;
/**
 * This class represents the DeezerRoom activity, which allows users to search for songs from Deezer API.
 * Author: Yandom Youmbi Farock Natanael
 * Date : 04/04/2024
 * Version: 01
 */

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.application.R;
import com.android.application.databinding.SongDetailsRemoveBinding;
import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/**
 * Fragment for displaying details of a selected Deezer song with delete option.
 * This fragment allows users to view details of a selected Deezer song, including title, duration, album name, and cover image.
 * Users can also delete the selected song from the database.
 */
public class SongDetailsFragmentDBS extends Fragment {

    private RecyclerView recyclerView;

    private DeezerSong selected;

    private byte[] imageByteArray; // Declare imageByteArray as a member variable
    private DeezerViewModel deezerViewModel;
    private DeezerSongDAO dDAO;
    private SongAdapter songAdapter;
    private List<DeezerSong> songs;


    DeezerRoom droom = new DeezerRoom();

    SongListFragment songListFragment = new SongListFragment();

    /**
     * Default constructor for the SongDetailsFragmentDBS.
     */
    public SongDetailsFragmentDBS() {
    }
    /**
     * Constructor for the SongDetailsFragmentDBS with a selected Deezer song.
     * @param selected The selected Deezer song to display details for.
     */
    public SongDetailsFragmentDBS(DeezerSong selected) {
        this.selected = selected;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SongDetailsRemoveBinding binding = SongDetailsRemoveBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Hide the ImageView
        //binding.imageView.setVisibility(View.GONE);

        // Set the song details in the layout views
        binding.titleView.setText(selected.getSong());
        binding.albumView.setText(selected.getAlbumName());
        binding.timeView.setText(selected.getTime());

        // Load image from byte array into ImageView using Glide
        if (selected != null && selected.getCoverImage() != null) {
            byte[] imageData = selected.getCoverImage();
            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(imageData));
            Glide.with(requireContext())
                    .load(bitmap)
                    .placeholder(R.drawable.absent_cover) // Placeholder drawable if image is not available
                    .error(R.drawable.app_icon) // Error drawable if Glide fails to load the image
                    .into(binding.imageView);
        }

        // Get reference to the favouriteButton
        //Button delete = rootView.findViewById(R.id.deleteButton);
        // Set OnClickListener on the delete button

        binding.deleteButton.setOnClickListener(v -> {
            // Perform delete operation here
            deleteSelectedItem();
        });

        return rootView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the DAO
        dDAO = Room.databaseBuilder(requireContext(), SongDatabase.class, "favourite_songs2").build().dsDAO();
    }

private void deleteSelectedItem() {
    // Build the AlertDialog
    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
    builder.setMessage(R.string.question)
            .setPositiveButton(R.string.yes, (dialog, which) -> {
                // User clicked Yes, perform delete operation
                performDeleteOperation();
                showToast();

            })
            .setNegativeButton(R.string.no, (dialog, which) -> {
                // User clicked No, do nothing
            });

    // Show the AlertDialog
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
}
    /**
     * Method to perform the delete operation for the selected item from the database.
     */
    private void performDeleteOperation() {

        // Insert the new DeezerSong into the database
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            // Perform delete operation for the selected item from the database
            dDAO.deleteMessage(selected);

            // Hide the current layout
            getActivity().runOnUiThread(() -> {
                View rootView = getView();
                if (rootView != null) {
                    rootView.setVisibility(View.GONE);
                }
            });

            // Reload SongListFragment after deletion
            reloadSongList();
        });
    }
    /**
     * Method to reload the SongListFragment after deleting a song.
     */
    private void reloadSongList() {
        // Retrieve the parent FragmentManager
        FragmentManager fragmentManager = getParentFragmentManager();

        // Begin a new FragmentTransaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with a new instance of SongListFragment
        transaction.replace(R.id.fragmentLocation2, new SongListFragment());

        // Commit the transaction
        transaction.commit();
    }
    /**
     * Method to show a Toast message.
     */
    private void showToast() {
        // Show a Toast message
        Toast.makeText(requireContext(), R.string.adding_song, Toast.LENGTH_SHORT).show();
    }


}

