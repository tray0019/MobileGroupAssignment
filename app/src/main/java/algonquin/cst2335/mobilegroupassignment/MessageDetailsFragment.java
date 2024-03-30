package algonquin.cst2335.mobilegroupassignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.application.R;
import com.android.application.databinding.SongDetailsAddBinding;
import com.android.application.databinding.SongListBinding;

public class MessageDetailsFragment extends Fragment {

    DeezerSong selected;

    public MessageDetailsFragment(DeezerSong m){
        selected = m;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        SongDetailsAddBinding binding = SongDetailsAddBinding.inflate(inflater);

        binding.titleView.setText(selected.getSong());

        // Get the formatted time obtained from the setter method in the DeezerSong class
        binding.timeView.setText(selected.getFormattedTime());

        // Get the albumName obtained from the setter method in the DeezerSong class
        binding.albumView.setText(selected.getAlbumName());

//        // Load album cover image using Glide
//        Glide.with(this)
//                .load(selected.getAlbumCoverUrl())  // Load the image from the URL
//                .placeholder(R.drawable.placeholder)  // Placeholder image while loading
//                .error(R.drawable.error)  // Image to show if loading fails
//                .into(binding.albumCoverImageView);  // Set the image into ImageView

       // binding.timeView.setText(time);
        return binding.getRoot();

    }
}
