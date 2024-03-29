package algonquin.cst2335.mobilegroupassignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.application.R;
import com.android.application.databinding.ActivityDeezerRoomBinding;
import com.android.application.databinding.SongListBinding;
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


public class DeezerRoom extends AppCompatActivity {

    ActivityDeezerRoomBinding binding;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    private ArrayList<DeezerSong> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeezerRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                                                try {
                                                    JSONArray tracklistData = tracklistResponse.getJSONArray("data");
                                                    for (int j = 0; j < tracklistData.length(); j++) {
                                                        JSONObject trackObject = tracklistData.getJSONObject(j);
                                                        String songTitle = trackObject.getString("title");
                                                        songs.add(new DeezerSong(idString));
                                                        songs.add(new DeezerSong(songTitle));
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

            itemView.setOnClickListener(clk ->{

            });
            songText = itemView.findViewById(R.id.message);
        }
    }
}