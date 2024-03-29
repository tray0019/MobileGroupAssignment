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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DeezerRoom extends AppCompatActivity {

    ActivityDeezerRoomBinding binding;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    private static ArrayList<DeezerSong> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeezerRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DeezerSong deezerSong = new DeezerSong();

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        songs = new ArrayList<>(); // Initialize the ArrayList

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
            }

            @Override
            public int getItemCount() {
                return songs.size();
            }
        };

        binding.recycleView.setAdapter(myAdapter);

        binding.searchButton.setOnClickListener(click -> {
            // Get the artist name entered by the user
            String artistName = binding.editText.getText().toString();
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

                                                        // Check if the title is already in the set
                                                        if (!uniqueTitles.contains(songTitle)) {
                                                            // If not, add it to the set and also to the list
                                                            uniqueTitles.add(songTitle);
                                                            songs.add(new DeezerSong(songTitle));
                                                        }
                                                        deezerSong.setSong(songTitle);
                                                    }
                                                    myAdapter.notifyDataSetChanged();
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
    }

    static class MyRowHolder extends RecyclerView.ViewHolder {
        TextView songText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                songDetails(getAdapterPosition(), itemView);
            });
            songText = itemView.findViewById(R.id.message);
        }

        public void songDetails(int position, View itemView) {

            DeezerSong deezerSong = new DeezerSong();
            if (position != RecyclerView.NO_POSITION) {
                String clickedSong = songs.get(position).getSong();
                Toast.makeText(itemView.getContext(), "Clicked Song: " + clickedSong, Toast.LENGTH_SHORT).show();

                // Inflate the song details layout
                LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                @SuppressLint("InflateParams") View songDetailsView = inflater.inflate(R.layout.song_details_add, null);

                TextView songTextView = songDetailsView.findViewById(R.id.titleView);
                songTextView.setText(clickedSong);
                // Replace the current layout in the RecyclerView item with the song details layout
                ViewGroup parentView = (ViewGroup) itemView;
                parentView.removeAllViews();
                parentView.addView(songDetailsView);

            }
        }
    }
}
