package algonquin.cst2335.mobilegroupassignment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.application.R;
import com.android.application.databinding.ActivityDeezerRoomBinding;
import com.android.application.databinding.SongListBinding;
import com.android.application.databinding.SongDetailsAddBinding;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DeezerRoom extends AppCompatActivity {

    ActivityDeezerRoomBinding binding;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    private static ArrayList<DeezerSong> songs;

    DeezerViewModel deezerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deezerViewModel = new ViewModelProvider(this).get(DeezerViewModel.class);
        binding = ActivityDeezerRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DeezerSong deezerSong = new DeezerSong();

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        songs = deezerViewModel.songs.getValue();
        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());

                SongListBinding binding = SongListBinding.inflate(inflater);
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                DeezerSong song = songs.get(position);
                holder.songText.setText(song.getSong());
                holder.time.setText(song.getTime());
                // Set the visibility of the time TextView to INVISIBLE
               // holder.time.setVisibility(View.INVISIBLE);
                //holder.album.setText(song.getAlbumName());
//
//                // Load the image using Glide
//                Glide.with(holder.itemView.getContext())
//                        .load(deezerSong.getImageUrl())  // Replace "getImageUrl()" with the method to get the image URL from your ChatMessage object
//                        .placeholder(R.drawable.placeholder)  // Placeholder image while loading
//                        .error(R.drawable.error)  // Image to show if loading fails
//                        .into(holder.imageView);
            }

            @Override
            public int getItemCount() {
                return songs.size();
            }
        };

        binding.recycleView.setAdapter(myAdapter);

        binding.searchButton.setOnClickListener(click -> {
            // Clear the previous list of songs
            songs.clear();

            // Get the artist name entered by the user
            String artistName = binding.editText.getText().toString();
            binding.editText.setText("");
            String url = "https://api.deezer.com/search/artist/?q=" + artistName;
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject artistObject = data.getJSONObject(i);
                                String serverArtistName = artistObject.getString("name");
                                String modifiedServerArtistName = serverArtistName.toLowerCase().replaceAll("\\s", "");
                                String modifiedArtistName = artistName.toLowerCase().replaceAll("\\s", "");
                                int id = artistObject.getInt("id");
                                String tracklistUrl = artistObject.getString("tracklist");
                                // Convert the ID to a string
                                String idString = String.valueOf(id);

                                if (tracklistUrl.contains(idString) && modifiedArtistName.equals(modifiedServerArtistName)) {
                                    JsonObjectRequest tracklistRequest = new JsonObjectRequest(
                                            Request.Method.GET,
                                            tracklistUrl,
                                            null,
                                            tracklistResponse -> {
                                                // Define a Set to store unique song titles
                                                Set<String> uniqueTitles = new HashSet<>();
                                                try {
                                                    JSONArray tracklistData = tracklistResponse.getJSONArray("data");
                                                    for (int j = 0; j < tracklistData.length(); j++) {
                                                        JSONObject trackObject = tracklistData.getJSONObject(j);
                                                        String songTitle = trackObject.getString("title");
                                                        String time = trackObject.getString("duration");

                                                        // Check if the title is already in the set
                                                        if (!uniqueTitles.contains(songTitle)) {
                                                            // If not, add it to the set and also to the list
                                                            uniqueTitles.add(songTitle);
                                                            songs.add(new DeezerSong(songTitle,time));
                                                        }
                                                       // deezerSong.setSong(songTitle);
                                                       // deezerSong.setTime(time);
                                                    }
                                                    myAdapter.notifyDataSetChanged();
                                                    // Clear the previous text:
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            },
                                            error -> {
                                                // Handle error response from tracklist URL
                                            }
                                    );
                                    Volley.newRequestQueue(this).add(tracklistRequest);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        // Handle error response
                    }
            );
            Volley.newRequestQueue(this).add(objectRequest);
        });

        deezerViewModel.selectedSong.observe(this, selectedSong -> {

            MessageDetailsFragment chatFragment = new MessageDetailsFragment(selectedSong);  //newValue is the newly set ChatMessage
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.replace(R.id.fragmentLocation, chatFragment);
            tx.addToBackStack(null); // Add to back stack so the user can navigate back
            tx.commit();
            // Hide the RecyclerView
            // Set the visibility of the RecyclerView to invisible
            binding.editText.setVisibility(View.INVISIBLE);
            binding.recycleView.setVisibility(View.INVISIBLE);
            binding.searchButton.setVisibility(View.INVISIBLE);
        });


    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView songText;
        TextView time;
        //TextView album;
        TextView albumCover;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAdapterPosition();
                DeezerSong selected = songs.get(position);
                deezerViewModel.selectedSong.postValue(selected);
                // Store the selected song in the ViewModel
                deezerViewModel.setSelectedSong(selected);

                //songDetails(getAdapterPosition(), itemView);
            });
            songText = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.times);
            //album = itemView.findViewById(R.id.albumView);

            // Store the selected song in the ViewModel
        }

//        public void songDetails(int position, View itemView) {
//
//            DeezerSong deezerSong = new DeezerSong();
//            if (position != RecyclerView.NO_POSITION) {
//                String clickedSong = songs.get(position).getSong();
//                Toast.makeText(itemView.getContext(), "Clicked Song: " + clickedSong, Toast.LENGTH_SHORT).show();
//
//                // Inflate the song details layout
//                LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
//                @SuppressLint("InflateParams") View songDetailsView = inflater.inflate(R.layout.song_details_add, null);
//
//                TextView songTextView = songDetailsView.findViewById(R.id.titleView);
//                songTextView.setText(clickedSong);
//                // Replace the current layout in the RecyclerView item with the song details layout
//                ViewGroup parentView = (ViewGroup) itemView;
//                parentView.removeAllViews();
//                parentView.addView(songDetailsView);
//
//            }
//        }
    }

    @Override
    public void onBackPressed() {
        // Check if any fragments are in the back stack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Show the RecyclerView
            binding.recycleView.setVisibility(View.VISIBLE);
            binding.searchButton.setVisibility(View.VISIBLE);
            binding.editText.setVisibility(View.VISIBLE);

            // Pop the back stack to navigate back
            getSupportFragmentManager().popBackStack();
        } else {
            // If no fragments in the back stack, perform default back action
            super.onBackPressed();
        }
    }
}
