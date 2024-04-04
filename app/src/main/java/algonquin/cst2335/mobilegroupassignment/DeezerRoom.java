package algonquin.cst2335.mobilegroupassignment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.application.R;
import com.android.application.databinding.ActivityDeezerRoomBinding;
import com.android.application.databinding.FavouriteSongListBinding;
import com.android.application.databinding.SongListBinding;
import com.android.application.databinding.SongDetailsAddBinding;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DeezerRoom extends AppCompatActivity  {

    ActivityDeezerRoomBinding binding;

    public RecyclerView.Adapter<MyRowHolder> myAdapter;
    private static ArrayList<DeezerSong> songs;
    //private static  ArrayList<DeezerSong> songsDetails;
    DeezerViewModel deezerViewModel;
    DeezerSongDAO dDAO;

    DeezerSong deezerSong;

    //onCreate Method:
    // This method is called when the activity is starting.
    // It sets up the view model and binds the layout defined in ActivityDeezerRoomBinding to the activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeezerRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        MessageDetailsFragment messageDetailsFragment = new MessageDetailsFragment();
//        messageDetailsFragment.setOnFavoriteButtonClickListener(this);

//        // Initialize the DAO
//        dDAO = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "favourite_songs").build().dsDAO();
//
//        // Initialize the songAdapter
//        songAdapter = new SongAdapter(dDAO);

        // Initialize an empty ArrayList for songs
        //songs = new ArrayList<>();

//        newBinding = ActivityDeezerRoomBinding.inflate(getLayoutInflater());
//        setContentView(newBinding.getRoot());


        deezerViewModel = new ViewModelProvider(this).get(DeezerViewModel.class);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        //binding.recycleView2.setLayoutManager(new LinearLayoutManager(this));

        // Create an instance of MessageDetailsFragment
        //MessageDetailsFragment messageDetailsFragment = new MessageDetailsFragment(songAdapter);
       // DeezerSong deezerSong = new DeezerSong();
       // create database connection
//        SongDatabase db = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "favourite_songs").build();
//        dDAO = db.dsDAO();



        //RecyclerView Setup:
        //Configures the RecyclerView to display the list of songs.
        //Sets the layout manager and adapter for the RecyclerView.


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

                //It sets the text of the songText TextView, which is a part of the ViewHolder (holder),
                // to the title of the song obtained from the DeezerSong object using the getSong() method.
                holder.songText.setText(song.getSong());

            }

            @Override
            public int getItemCount() {

                return songs.size();
            }


        };


        //Search Button Click Listener:
        //Defines the click listener for the search button.
        //Handles the process of fetching songs from the Deezer API based on the user input.

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
                            //method to retrieves jason array data
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

                                                        // Retrieve album title from the "album" object
                                                        JSONObject albumObject = trackObject.getJSONObject("album");
                                                        String albumTitle = albumObject.getString("title");
                                                        String albumCoverUrl = albumObject.getString("cover_big");

                                                        // Create a new Deezer song object and set its time
                                                        DeezerSong deezerSong = new DeezerSong(songTitle);
                                                        deezerSong.setTime(time);
                                                        deezerSong.setAlbumName(albumTitle);
                                                        deezerSong.setAlbumCoverUrl(albumCoverUrl);

                                                        // Add the Deezer song object to the songs array list
                                                        songs.add(deezerSong);

                                                    }
                                                    // After updating songs, notify the adapter
                                                    myAdapter.notifyDataSetChanged();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            },
                                            error -> {
                                                // Handle error response from tracklist URL
                                            }
                                    );
                                    //performs a network request using Volley, an HTTP library for Android, to fetch data from a remote server.
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
            //performs a network request using Volley, an HTTP library for Android, to fetch data from a remote server.
            Volley.newRequestQueue(this).add(objectRequest);
        });


        //Selected Song Observer
        //Observes changes in the selected song LiveData.
        //Replaces the current fragment with a details fragment when a song is selected.

        deezerViewModel.selectedSong.observe(this, selectedSong -> {


            MessageDetailsFragment chatFragment = new MessageDetailsFragment(selectedSong);  //newValue is the newly set ChatMessage
            //SongListFragment listFragment = new SongListFragment();
            //chatFragment.setTime(selectedSong.getTime()); // Pass time to the fragment
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

//    @Override
//    public void onFavoriteButtonClick() {
//        // Call the method in the adapter when the favorite button is clicked in the fragment
////        if (songAdapter != null) {
////            songAdapter.updateAdapter(this);
////        }
//    }

    // MyRowHolder Class
    //Represents the ViewHolder for the RecyclerView items.
    //Holds references to the views in each item of the RecyclerView.

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView songText;
        TextView time;
        //TextView album;
        TextView albumCover;

        public MyRowHolder(@NonNull View itemView) {
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

            //obtain a reference to the TextView in the item layout, which will be used to display the song title for each item in the RecyclerView
            songText = itemView.findViewById(R.id.message);
           // time = itemView.findViewById(R.id.timeView);
            //album = itemView.findViewById(R.id.albumView);

            // Store the selected song in the ViewModel
        }

    }

    //onBackPressed Method
    //Overrides the default behavior when the back button is pressed.
    //Handles navigation back from fragments and shows the RecyclerView if needed.
//
//    @Override
//    public void onBackPressed() {
//        // Check if any fragments are in the back stack
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            // Show the RecyclerView
//            binding.recycleView.setVisibility(View.VISIBLE);
//            binding.searchButton.setVisibility(View.VISIBLE);
//            binding.editText.setVisibility(View.VISIBLE);
//
//            // Pop the back stack to navigate back
//            getSupportFragmentManager().popBackStack();
//        } else {
//            // If no fragments in the back stack, perform default back action
//            super.onBackPressed();
//        }
//    }

    public void onBackPressed() {

            // Check if the current fragment is MessageDetailsFragment
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentLocation);
            if (fragment instanceof MessageDetailsFragment && getSupportFragmentManager().getBackStackEntryCount() > 0) {
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
