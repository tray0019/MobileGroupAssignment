package algonquin.cst2335.mobilegroupassignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.application.R;

import java.util.ArrayList;
import java.util.List;



public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    static DeezerViewModel deezerViewModel;

    private static List<DeezerSong> songs;

    // Constructor to initialize the list of songs
    public SongAdapter(List<DeezerSong> songs) {
        this.songs = songs != null ? songs : new ArrayList<>();
    }

    // Method to set the list of songs
//    public void setSongs(List<DeezerSong> songs) {
//        this.songs = songs != null ? songs : new ArrayList<>();
//        notifyDataSetChanged(); // Notify RecyclerView that the dataset has changed
//    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        // Bind the data to the views in each item
        DeezerSong song = songs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the list, or 0 if the list is null
        return songs != null ? songs.size() : 0;
    }

    // ViewHolder class to hold the views for each item in the RecyclerView
    static class SongViewHolder extends RecyclerView.ViewHolder {
        // Views in the item layout
        private TextView songTitleTextView;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                //This line retrieves the position of the item that was clicked within the RecyclerView
                int position = getAdapterPosition();
                //After obtaining the position of the clicked item, this line retrieves the DeezerSong object at that position from the songs ArrayList
                DeezerSong selected = songs.get(position);

                //This line of code posts a DeezerSong object to the LiveData object selectedSong within the deezerViewModel
                deezerViewModel.selectedSong.postValue(selected);

                //songDetails(getAdapterPosition(), itemView);
            });
            // Initialize views
            songTitleTextView = itemView.findViewById(R.id.message);
        }

        // Method to bind data to the views
        public void bind(DeezerSong song) {
            songTitleTextView.setText(song.getSong());
        }
    }
}
