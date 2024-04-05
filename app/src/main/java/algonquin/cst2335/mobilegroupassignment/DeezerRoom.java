package algonquin.cst2335.mobilegroupassignment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import com.android.application.R;
import com.android.application.databinding.ActivityDeezerRoomBinding;
import com.android.application.databinding.SongListBinding;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the DeezerRoom activity, which allows users to search for songs from Deezer API.
 * Author: Yandom Youmbi Farock Natanael
 * Date : 04/04/2024
 * Version: 01
 */

public class DeezerRoom extends AppCompatActivity {


    ActivityDeezerRoomBinding binding;

    public RecyclerView.Adapter<MyRowHolder> myAdapter;
    private static ArrayList<DeezerSong> songs;
    //private static  ArrayList<DeezerSong> songsDetails;
    DeezerViewModel deezerViewModel;
    DeezerSongDAO dDAO;

    DeezerSong deezerSong;

    /**
     * This holds the text at the centre of the screen
     */
    TextView li = null;
    /**
     * This holds the password
     */
    EditText et = null;
    /**
     * This holds the text at te bottom of the screen
     */

    ImageButton btn = null;
    /**
     * This method is called when the activity is starting.
     * It sets up the view model and binds the layout defined in ActivityDeezerRoomBinding to the activity.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *         then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    //onCreate Method:
    // This method is called when the activity is starting.
    // It sets up the view model and binds the layout defined in ActivityDeezerRoomBinding to the activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeezerRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);

    // Retrieve the artist name from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String savedArtistName = prefs.getString("LoginName", ""); // Use the same key "LoginName" for retrieval
        binding.editText.setText(savedArtistName); // Set the retrieved artist name to the EditText view


        deezerViewModel = new ViewModelProvider(this).get(DeezerViewModel.class);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        //binding.recycleView2.setLayoutManager(new LinearLayoutManager(this));

        songs = deezerViewModel.songs.getValue();
        if (songs == null) {
            songs = new ArrayList<>();
            deezerViewModel.songs.postValue(songs);
        }


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
            //binding.editText.setText("");
            if (isFieldEmpty(artistName)) {
                // Show toast message for correct entry
                showToast("Empty field");
            }else {
                // Show toast message for empty field
                showToast("Correct entry");

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
            }

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
            binding.myToolbar.setVisibility(View.INVISIBLE);
        });

    }

    /**
     * Called when the activity is paused.
     * It saves the artist name to SharedPreferences.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Save the artist name to SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("LoginName", binding.editText.getText().toString()); // Use the same key "LoginName" for saving
        editor.apply();
    }

    /**
     * Represents the ViewHolder for the RecyclerView items.
     * Holds references to the views in each item of the RecyclerView.
     */
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
    /**
     * Overrides the onBackPressed method.
     * Checks if the current fragment is MessageDetailsFragment and handles the back navigation accordingly.
     */
    public void onBackPressed() {
        // Check if the current fragment is MessageDetailsFragment
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentLocation);
        if (fragment instanceof MessageDetailsFragment && getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Show the RecyclerView
            binding.recycleView.setVisibility(View.VISIBLE);
            binding.searchButton.setVisibility(View.VISIBLE);
            binding.editText.setVisibility(View.VISIBLE);
            binding.myToolbar.setVisibility(View.VISIBLE);

            // Pop the back stack to navigate back
            getSupportFragmentManager().popBackStack();
        } else {
            // If no fragments in the back stack, perform default back action
            super.onBackPressed();
        }

    }
    /**
     * Inflates the options menu for the activity.
     * @param menu The options menu in which you place your items.
     * @return True if the menu is inflated successfully; otherwise, false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    /**
     * Handles the selection of menu items.
     * @param item The menu item that was selected.
     * @return True if the item is selected successfully; otherwise, false.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_1) {
            // Show an AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This is how to use the application");
            builder.setMessage("This is application is an application that helps you to search for songs through the internet, " +
                    "select and save it to your device\n On start, Navigate to the search bar and enter the artist name\n After you will have a list of your artist songs " +
                    "Select a song and you will view the details.\n To add a song to your device, click on the star icon and click on the view button to see your list of favourite songs\n" +
                    "Finally, you can delete the song from the list by clicking thee delete button ");
            builder.setPositiveButton("Done", (dialog, which) -> {

            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return true; // Event consumed
        } else if (item.getItemId() == R.id.About) {
            // Handle the About item click here
            Toast.makeText(this, "Created by farock natanael", Toast.LENGTH_LONG).show();
            return true; // Event consumed
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isFieldEmpty(String fieldValue) {
        // Check if the fieldValue is null or empty
        return fieldValue == null || fieldValue.trim().isEmpty();
    }
        // Method to show a toast message
        private void showToast(String message) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

}

